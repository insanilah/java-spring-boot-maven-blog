package com.example.blog.service.interfaces;
import java.util.List;
import java.util.Map;

import com.example.blog.dto.UserActivityDTO;
import com.example.blog.enums.ActivityType;
import com.example.blog.model.UserActivity;

public interface UserActivityService {
    void createUserActivity(String username, Long postId, ActivityType activityType);
    List<UserActivity> getUserActivities(String username);
    List<UserActivityDTO> getUserActivitiesFormatted(String username);
    List<Map<String, Object>> aggregateUserActivitiesSortedByDay(String username);
}
