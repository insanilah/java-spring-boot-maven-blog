package com.example.blog.service.interfaces;

import java.util.List;

import com.example.blog.dto.ExternalApiDTO;

public interface ExternalApiService {
    List<ExternalApiDTO> getExternalApiData() throws Exception;
}
