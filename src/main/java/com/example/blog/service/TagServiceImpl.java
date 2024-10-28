package com.example.blog.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.blog.dto.TagDTO;
import com.example.blog.mapper.TagMapper;
import com.example.blog.model.Tag;
import com.example.blog.repository.TagRepository;
import com.example.blog.service.interfaces.TagService;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Page<Tag> getAllTags(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Page<Tag> getAllTagByName(String name, Pageable pageable) {
        return tagRepository.findTagsByName(name, pageable);
    }

    @Override
    public Tag getTagById(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
    }
    
    @Override
    public Tag createTag(TagDTO tagDTO) {
        // Cek apakah post dengan name yang sama sudah ada
        Optional<Tag> existingTag = tagRepository.findByName(tagDTO.getName());
        if (existingTag.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Tag with the same name already exists");
        }

        Tag tag = new Tag();
        tag.setName(tagDTO.getName());
        return tagRepository.save(tag);
    }

    @Override
    public Tag updateTag(Long id, TagDTO tagDTO) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        tag.setName(tagDTO.getName());
        return tagRepository.save(tag);
    }

    @Override
    public TagDTO deleteTag(Long id) {
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        tagRepository.delete(tag);
        return TagMapper.mapToDTO(tag);
    }
}
