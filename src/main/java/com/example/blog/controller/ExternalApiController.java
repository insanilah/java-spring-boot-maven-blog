package com.example.blog.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.dto.ExternalApiDTO;
import com.example.blog.dto.Response;
import com.example.blog.service.interfaces.ExternalApiService;
import com.example.blog.util.ResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/get-external-data")
public class ExternalApiController {

    @Autowired
    private ExternalApiService externalApiService;

    @GetMapping
     public ResponseEntity<Response<List<ExternalApiDTO>>> getExternalData() throws Exception {
        List<ExternalApiDTO> data = externalApiService.getExternalApiData();
        log.info("responseData: {}",data);
        return ResponseBuilder.buildResponse("200", "Success", data, HttpStatus.OK);
    }
}
