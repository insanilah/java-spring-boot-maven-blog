package com.example.blog.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.blog.dto.ExternalApiDTO;
import com.example.blog.service.interfaces.ExternalApiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExternalApiServiceImpl implements ExternalApiService {
    private static final Logger logger = LoggerFactory.getLogger(ExternalApiServiceImpl.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ExternalApiServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ExternalApiDTO> getExternalApiData() throws Exception {
        // URL dummy
        String url = "https://jsonplaceholder.typicode.com/posts";

        // Hit URL
        String jsonResponse = restTemplate.getForObject(url, String.class);
        logger.info("jsonResponse:{}",jsonResponse);
        // Mapping JSON response ke List<ExternalApiDTO>
        return objectMapper.readValue(jsonResponse, new TypeReference<List<ExternalApiDTO>>() {});
    }
}
