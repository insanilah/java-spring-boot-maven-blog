package com.example.blog.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.blog.dto.TagDTO;
import com.example.blog.model.Tag;

public interface TagService {
    Page<Tag> getAllTags(Pageable pageable);
    Page<Tag> getAllTagByName(String name, Pageable pageable);
    Tag getTagById(Long id);
    Tag createTag(TagDTO categoryDTO);
    Tag updateTag(Long id, TagDTO categoryDTO);
    TagDTO deleteTag(Long id);
}