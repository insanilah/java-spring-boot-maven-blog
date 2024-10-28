package com.example.blog.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.blog.dto.PostDTO;
import com.example.blog.enums.ActivityType;
import com.example.blog.mapper.PostMapper;
import com.example.blog.model.Category;
import com.example.blog.model.Post;
import com.example.blog.model.Tag;
import com.example.blog.model.User;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.TagRepository;
import com.example.blog.repository.UserRepository;
import com.example.blog.service.interfaces.PostService;
import com.example.blog.service.interfaces.UserActivityService;
import com.example.blog.util.RedisDataUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserActivityService userActivityService;
    private final RedisTemplate<String, Object> redisTemplate;

    public PostServiceImpl(
            PostRepository postRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository,
            UserActivityService userActivityService,
            RedisTemplate<String, Object> redisTemplate) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.userActivityService = userActivityService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();  // Ambil semua Post dari repository
        return posts.stream() // Lakukan stream untuk mapping setiap Post ke PostDTO
                .map(PostMapper::toDTO) // Mapping ke PostDTO
                .collect(Collectors.toList());    // Kumpulkan hasilnya ke dalam List<PostDTO>
    }

    @Override
    public Page<PostDTO> getAllPosts(Pageable pageable) {
        Page<Post> listPost = postRepository.findAll(pageable);
        return listPost.map(PostMapper::toDTO);
    }

    @Override
    public Map<String, Object> getAllPostsCustomPagination(int page, int size, String[] sort) {
        // Parsing sorting input
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        String sortBy = sort[0];

        Page<Post> listPost = postRepository.findAll(PageRequest.of(page - 1, size, Sort.by(direction, sortBy)));
        Page<PostDTO> postDTOPage = listPost.map(PostMapper::toDTO);

        // Membuat response
        Map<String, Object> data = new HashMap<>();
        data.put("posts", postDTOPage.getContent());
        data.put("totalPages", postDTOPage.getTotalPages());
        data.put("totalElements", postDTOPage.getTotalElements());
        data.put("currentPage", postDTOPage.getNumber() + 1);
        data.put("pageSize", postDTOPage.getSize());

        return data;
    }

    @Override
    public Map<String, Object> getAllPostsCustomQuery(String title, int page, int size, String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        String sortBy = sort[0];

        Page<Post> postPage = postRepository.findPostsByTitle(title, PageRequest.of(page - 1, size, Sort.by(direction, sortBy)));
        Page<PostDTO> postDTOPage = postPage.map(PostMapper::toDTO);

        // Membuat response
        Map<String, Object> data = new HashMap<>();
        data.put("posts", postDTOPage.getContent());
        data.put("totalPages", postDTOPage.getTotalPages());
        data.put("totalElements", postDTOPage.getTotalElements());
        data.put("currentPage", postDTOPage.getNumber() + 1);
        data.put("pageSize", postDTOPage.getSize());

        return data;
    }

    @Override
    @Cacheable(value = "posts", key = "#id")
    public PostDTO getPostById(String username, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        // save to mongo
        userActivityService.createUserActivity(username, id, ActivityType.VIEW);
        return PostMapper.toDTO(post);
    }

    @Override
    @Transactional
    public PostDTO createPost(PostDTO postDTO) {
        // Cek apakah post dengan title yang sama sudah ada
        Optional<Post> existingPost = postRepository.findByTitle(postDTO.getTitle());
        if (existingPost.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Post with the same title already exists");
        }

        // Ambil author berdasarkan authorId
        User author = userRepository.findById(postDTO.getAuthorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        // Ambil list categories berdasarkan categoryIds
        List<Category> categories = categoryRepository.findAllById(postDTO.getCategoryIds());

        // Ambil list tags berdasarkan tagIds
        List<Tag> tags = tagRepository.findAllById(postDTO.getTagIds());

        // Map DTO to entity menggunakan PostMapper dengan argumen yang benar
        postDTO.setCreatedAt(LocalDateTime.now().toString());
        Post post = PostMapper.toEntity(postDTO, author, categories, tags);
        log.info("post: {}", post);

        // Simpan post ke repository
        Post createdPost = postRepository.save(post);

        String redisKey = RedisDataUtil.buildRedisKey("posts", String.valueOf(post.getId()));
        redisTemplate.opsForValue().set(redisKey, postDTO, 3600, TimeUnit.SECONDS);

        return PostMapper.toDTO(createdPost);
    }

    @Override
    @Transactional
    @CacheEvict(value = "posts", key = "#postId")
    public PostDTO updatePost(Long postId, PostDTO postDTO) {
        // Ambil post yang ada berdasarkan ID
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        // Ambil author berdasarkan authorId, jika akan diubah
        User author = userRepository.findById(postDTO.getAuthorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found"));

        // Ambil list categories berdasarkan categoryIds, jika akan diubah
        List<Category> categories = categoryRepository.findAllById(postDTO.getCategoryIds());

        // Ambil list tags berdasarkan tagIds, jika akan diubah
        List<Tag> tags = tagRepository.findAllById(postDTO.getTagIds());

        // Update properti post yang ada dengan nilai baru dari DTO
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        existingPost.setSlug(postDTO.getSlug());
        existingPost.setPublished(postDTO.isPublished());
        existingPost.setAuthor(author); // Set author yang baru
        existingPost.setCategories(categories); // Set categories yang baru
        existingPost.setTags(tags); // Set tags yang baru
        existingPost.setUpdatedAt(LocalDateTime.now()); // Set waktu update

        // Simpan perubahan ke repository
        Post updatedPost = postRepository.save(existingPost);
        return PostMapper.toDTO(updatedPost);
    }

    @Override
    @Transactional
    public PostDTO deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        postRepository.deleteById(id);
        return PostMapper.toDTO(post);
    }

    // Mengambil post dari Redis langsung
    @Override
    public PostDTO getPostFromRedis(Long id) {
        String redisKey = RedisDataUtil.buildRedisKey("posts", String.valueOf(id));
        Object redisData = redisTemplate.opsForValue().get(redisKey);
        Optional<PostDTO> optionalPost = RedisDataUtil.convertRedisDataToObject(redisData, PostDTO.class);

        if (optionalPost.isPresent()) {
            return optionalPost.get();
        }

        Post post = postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        PostDTO postDTO = PostMapper.toDTO(post);
        redisTemplate.opsForValue().set(redisKey, (Object) postDTO, 3600, TimeUnit.SECONDS);

        return postDTO;
    }

    @Override
    public PostDTO setPostFromRedis(PostDTO postDTO) {
        // Ambil ID dari PostDTO untuk digunakan sebagai bagian dari kunci Redis
        String redisKey = RedisDataUtil.buildRedisKey("posts", String.valueOf(postDTO.getId()));

        // Cek apakah data dengan kunci tersebut sudah ada di Redis
        if (redisTemplate.hasKey(redisKey)) {
            // Jika sudah ada, lempar ResponseStatusException
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Data for Post ID " + postDTO.getId() + " already exists in Redis.");
        }

        // Jika belum ada, simpan data PostDTO ke Redis dengan TTL 3600 detik
        redisTemplate.opsForValue().set(redisKey, postDTO, 3600, TimeUnit.SECONDS);
        log.info("Data for Post ID {} has been stored in Redis.", postDTO.getId());

        return postDTO;
    }

}
