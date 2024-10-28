package com.example.blog.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.blog.dto.Response;
import com.example.blog.dto.UserActivityDTO;
import com.example.blog.model.UserActivity;
import com.example.blog.service.interfaces.UserActivityService;
import com.example.blog.util.ResponseBuilder;

@RestController
@RequestMapping("/api/user-activities")
public class UserActivityController {

    private final UserActivityService userActivityService;

    @Autowired
    public UserActivityController(UserActivityService userActivityService) {
        this.userActivityService = userActivityService;
    }

    // Endpoint untuk mendapatkan aktivitas pengguna berdasarkan username
    @GetMapping("/{username}")
    public ResponseEntity<Response<List<UserActivity>>> getUserActivities(@PathVariable String username) {
        List<UserActivity> activities = userActivityService.getUserActivities(username);
        return ResponseBuilder.buildResponse("200", "Success", activities, HttpStatus.OK);
    }

    // Endpoint untuk mendapatkan aktivitas pengguna dalam format string
    @GetMapping("/{username}/formatted")
    public ResponseEntity<Response<List<UserActivityDTO>>> getUserActivitiesFormatted(@PathVariable String username) {
        List<UserActivityDTO> formattedActivities = userActivityService.getUserActivitiesFormatted(username);
        return ResponseBuilder.buildResponse("200", "Success", formattedActivities, HttpStatus.OK);
    }

    // Endpoint untuk mendapatkan aktivitas pengguna dalam format string
    @GetMapping("/{username}/sort-by-day")
    public ResponseEntity<Response<List<Map<String,Object>>>> aggregateUserActivitiesSortedByDay(@PathVariable String username) {
        List<Map<String,Object>> formattedActivities = userActivityService.aggregateUserActivitiesSortedByDay(username);
        return ResponseBuilder.buildResponse("200", "Success", formattedActivities, HttpStatus.OK);
    }
    
    @GetMapping("/beranda")
    public Principal user(Principal principal){
        System.out.println("username : "+principal.getName());
        return principal;
    }

}
