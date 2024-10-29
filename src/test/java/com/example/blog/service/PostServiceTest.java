package com.example.blog.service;

// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyList;
// import static org.mockito.ArgumentMatchers.anyLong;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.http.HttpStatus;
// import org.springframework.web.server.ResponseStatusException;

// import com.example.blog.dto.CategoryDTO;
// import com.example.blog.dto.PostDTO;
// import com.example.blog.dto.TagDTO;
// import com.example.blog.dto.UserDTO;
// import com.example.blog.mapper.CategoryMapper;
// import com.example.blog.mapper.PostMapper;
// import com.example.blog.mapper.TagMapper;
// import com.example.blog.mapper.UserMapper;
// import com.example.blog.model.Category;
// import com.example.blog.model.Post;
// import com.example.blog.model.Tag;
// import com.example.blog.model.User;
// import com.example.blog.repository.CategoryRepository;
// import com.example.blog.repository.PostRepository;
// import com.example.blog.repository.TagRepository;
// import com.example.blog.repository.UserRepository;

// @ExtendWith(MockitoExtension.class)
public class PostServiceTest {

//     @Mock
//     private PostRepository postRepository;

//     @Mock
//     private CategoryRepository categoryRepository;

//     @Mock
//     private TagRepository tagRepository;

//     @Mock
//     private UserRepository userRepository;

//     @InjectMocks
//     private PostServiceImpl postService;

//     @Test
//     public void testCreatePost_Success() {
//         UserDTO authorDTO = new UserDTO(1L, "authorUsername", "authorEmail@example.com");
//         CategoryDTO categoryDTO = new CategoryDTO(2L, "Category 1");
//         TagDTO tagDTO = new TagDTO(3L, "Tag 1");

//         // Given
//         PostDTO postDTO = new PostDTO(
//                 "Sample Title", // title
//                 "Sample Content", // content
//                 "sample-slug", // slug
//                 true, // published
//                 1L, // UserDTO for author
//                 List.of(2L),
//                 List.of(categoryDTO), // List<CategoryDTO>
//                 List.of(3L),
//                 List.of(tagDTO), // List<TagDTO>
//                 "2023-10-10", // createdAt
//                 "2023-10-11" // updatedAt
//         );

//         User author = UserMapper.toEntity(authorDTO);
//         Category category = CategoryMapper.toEntity(categoryDTO);
//         Tag tag = TagMapper.toEntity(tagDTO);
//         Post post = PostMapper.toEntity(postDTO, author, List.of(category), List.of(tag));

//         when(userRepository.findById(anyLong())).thenReturn(Optional.of(author));
//         when(categoryRepository.findAllById(anyList())).thenReturn(List.of(category));
//         when(tagRepository.findAllById(anyList())).thenReturn(List.of(tag));
//         when(postRepository.save(any(Post.class))).thenReturn(post);

//         // When
//         PostDTO createdPost = postService.createPost(postDTO);

//         // Then
//         assertNotNull(createdPost);
//         assertEquals("Sample Title", createdPost.getTitle());
//         verify(postRepository, times(1)).save(any(Post.class));
//     }

//     @Test
//     public void testCreatePost_Failure_Rollback() {
//         // Given
//         CategoryDTO categoryDTO = new CategoryDTO(2L, "Category 1");
//         TagDTO tagDTO = new TagDTO(3L, "Tag 1");

//         // Given
//         PostDTO postDTO = new PostDTO(
//                 "Sample Title", // title
//                 "Sample Content", // content
//                 "sample-slug", // slug
//                 true, // published
//                 1L, // UserDTO for author
//                 List.of(2L),
//                 List.of(categoryDTO), // List<CategoryDTO>
//                 List.of(3L),
//                 List.of(tagDTO), // List<TagDTO>
//                 "2023-10-10", // createdAt
//                 "2023-10-11" // updatedAt
//         );

//         // Simulate that the author is not found
//         when(userRepository.findById(1L)).thenReturn(Optional.empty());

//         // When
//         ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
//             postService.createPost(postDTO);
//         });

//         // Optionally, check the message or status of the exception
//         assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
//         assertEquals("Author not found", exception.getReason());

//         // Then
//         verify(postRepository, times(0)).save(any(Post.class));
//     }
}
