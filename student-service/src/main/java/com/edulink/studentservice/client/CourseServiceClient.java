package com.edulink.studentservice.client;

import com.edulink.studentservice.dto.ApiResponse;
import com.edulink.studentservice.dto.LearningMaterialDto;
<<<<<<< HEAD
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
=======
import com.edulink.studentservice.exception.UpstreamServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Component
<<<<<<< HEAD
public class CourseServiceClient {

    private static final Logger log = LoggerFactory.getLogger(CourseServiceClient.class);

=======
@Slf4j
@RequiredArgsConstructor
public class CourseServiceClient {

>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
    @Value("${course.service.url:http://localhost:8083}")
    private String courseServiceUrl;

    private final RestTemplate restTemplate;

<<<<<<< HEAD
    public CourseServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
=======
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06

    public List<LearningMaterialDto> getMaterialsByCourseCode(String courseCode, String token) {
        try {
            String url = courseServiceUrl + "/student/materials/" + courseCode;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<ApiResponse<List<LearningMaterialDto>>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, new ParameterizedTypeReference<ApiResponse<List<LearningMaterialDto>>>() {});
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getData();
            }
<<<<<<< HEAD
        } catch (Exception e) {
            log.error("Failed to fetch materials for course: {}", courseCode, e);
        }
        return List.of();
=======
            throw new UpstreamServiceException("Failed to fetch materials from course-service for courseCode: " + courseCode);
        } catch (UpstreamServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch materials for course: {}", courseCode, e);
            throw new UpstreamServiceException("Failed to fetch materials from course-service for courseCode: " + courseCode, e);
        }
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
    }

    public List<LearningMaterialDto> getAssignmentsByCourseCode(String courseCode, String token) {
        try {
            String url = courseServiceUrl + "/student/assignments/" + courseCode;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<ApiResponse<List<LearningMaterialDto>>> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, new ParameterizedTypeReference<ApiResponse<List<LearningMaterialDto>>>() {});
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getData();
            }
<<<<<<< HEAD
        } catch (Exception e) {
            log.error("Failed to fetch assignments for course: {}", courseCode, e);
        }
        return List.of();
=======
            throw new UpstreamServiceException("Failed to fetch assignments from course-service for courseCode: " + courseCode);
        } catch (UpstreamServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("Failed to fetch assignments for course: {}", courseCode, e);
            throw new UpstreamServiceException("Failed to fetch assignments from course-service for courseCode: " + courseCode, e);
        }
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
    }

    public boolean courseExists(Long courseId, String token) {
        try {
            String url = courseServiceUrl + "/student/courses/" + courseId; // Assuming course-service has /student/courses/{id} to get course
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Failed to check if course exists: {}", courseId, e);
            return false;
        }
    }

    public Long getCourseIdByCode(String courseCode, String token) {
        try {
            String url = courseServiceUrl + "/internal/course/by-code/" + courseCode;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object id = response.getBody().get("id");
                if (id instanceof Number) {
                    return ((Number) id).longValue();
                }
            }
<<<<<<< HEAD
        } catch (Exception e) {
            log.error("Failed to get course ID by code: {}", courseCode, e);
        }
        return null;
    }
}
=======
            return null;
        } catch (Exception e) {
            log.error("Failed to get course ID by code: {}", courseCode, e);
            throw new UpstreamServiceException("Failed to resolve courseCode in course-service: " + courseCode, e);
        }
    }
}

>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
