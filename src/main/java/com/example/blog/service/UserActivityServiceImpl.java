package com.example.blog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.example.blog.dto.UserActivityDTO;
import com.example.blog.enums.ActivityType;
import com.example.blog.model.UserActivity;
import com.example.blog.repository.UserActivityRepository;
import com.example.blog.service.interfaces.UserActivityService;
import com.example.blog.util.DateFormatterUtil;

@Service
public class UserActivityServiceImpl implements UserActivityService {

    private final UserActivityRepository userActivityRepository;
    private final MongoTemplate mongoTemplate;

    // Constructor Injection, di-inject oleh Spring
    public UserActivityServiceImpl(UserActivityRepository userActivityRepository, MongoTemplate mongoTemplate) {
        this.userActivityRepository = userActivityRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void createUserActivity(String username, Long postId, ActivityType activityType) {
        UserActivity activity = new UserActivity();
        activity.setUsername(username);
        activity.setPostId(postId);
        activity.setActivityType(activityType.getValue());
        activity.setTimestamp(LocalDateTime.now());

        userActivityRepository.save(activity);
    }

    @Override
    public List<UserActivity> getUserActivities(String username) {
        return userActivityRepository.findByUsername(username);
    }

    @Override
    public List<UserActivityDTO> getUserActivitiesFormatted(String username) {
        List<UserActivity> activities = userActivityRepository.findByUsername(username);
        // Format timestamp menjadi string dengan format Indonesia
        List<UserActivityDTO> formattedActivities = activities.stream()
                .map(activity -> new UserActivityDTO(
                activity.getId(),
                activity.getUsername(),
                activity.getActivityType(),
                activity.getPostId(),
                DateFormatterUtil.formatLocalDateTime(activity.getTimestamp()) // Format date here
        ))
                .collect(Collectors.toList());

        return formattedActivities;
    }

    @Override
    public List<Map<String, Object>> aggregateUserActivitiesSortedByDay(String username) {
        Aggregation aggregation = Aggregation.newAggregation(
            // Step 1: Project the fields that we need
            Aggregation.project()
                .andExpression("dateToString('%Y-%m-%d', timestamp)").as("day") // Convert timestamp to date string (day)
                .and("activityType").as("activityType"),
            
            // Step 2: Group by day and activityType, and count the activities
            Aggregation.group("day", "activityType")
                .count().as("activityCount"),
            
            // Step 3: Project to reshape the output
            Aggregation.project()
                .and("_id.day").as("day")
                .and("_id.activityType").as("activityType")
                .and("activityCount").as("activityCount")
                .andExclude("_id"),
            
            // Step 4: Sort the result by day (if necessary)
            Aggregation.sort(Sort.by(Sort.Direction.ASC, "day"))
        );

        // Menjalankan agregasi, pastikan tipe hasil diatur dengan benar
        AggregationResults<Map> results = mongoTemplate.aggregate(aggregation, "user_activities", Map.class);

        // Mengonversi hasil ke List<Map<String, Object>> dengan casting eksplisit menggunakan stream
        List<Map<String, Object>> formattedResults = results.getMappedResults()
                .stream()
                .map(map -> (Map<String, Object>) map) // Casting manual setiap map
                .collect(Collectors.toList());

        return formattedResults;
    }

}
