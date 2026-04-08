package com.edulink.studentservice.controller;

import com.edulink.studentservice.client.AttendanceServiceClient;
import com.edulink.studentservice.client.CourseServiceClient;
import com.edulink.studentservice.client.ExamServiceClient;
import com.edulink.studentservice.dto.ApiResponse;
import com.edulink.studentservice.dto.CreateStudentProfileRequest;
import com.edulink.studentservice.dto.EnrollCourseRequest;
import com.edulink.studentservice.dto.SubmitAssignmentRequest;
import com.edulink.studentservice.entity.Enrollment;
import com.edulink.studentservice.exception.StudentProfileNotFoundException;
import com.edulink.studentservice.service.StudentService;
import com.edulink.studentservice.util.JwtExtractor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * JUnit tests for StudentController API contracts.
 * These tests verify request/response validation, HTTP status codes, and error handling.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @MockBean
    private JwtExtractor jwtExtractor;

    @MockBean
    private CourseServiceClient courseServiceClient;

    @MockBean
    private ExamServiceClient examServiceClient;

    @MockBean
    private AttendanceServiceClient attendanceServiceClient;

    private static final String VALID_TOKEN = "Bearer valid.jwt.token";
    private static final String TEST_EMAIL = "student@greenwood.edu";

    @BeforeEach
    public void setUp() {
        // Mock JWT extraction for authenticated endpoints
        when(jwtExtractor.extractEmail(any())).thenReturn(TEST_EMAIL);
        when(jwtExtractor.extractToken(any())).thenReturn("valid.jwt.token");

        // Prevent NullPointerException in create profile success path
        com.edulink.studentservice.entity.StudentProfile savedProfile = new com.edulink.studentservice.entity.StudentProfile();
        savedProfile.setId(1L);
        savedProfile.setUserId("USR123");
        savedProfile.setEmail("alice@greenwood.edu");
        savedProfile.setFullName("Alice Smith");
        savedProfile.setSchoolId("SCH001");
        savedProfile.setClassId(1L);
        when(studentService.createProfile(any())).thenReturn(savedProfile);
    }

    // ============ Test Case 1: Enroll - Success ============
    @Test
    public void testEnrollInCourse_Success() throws Exception {
        // Arrange
        EnrollCourseRequest request = new EnrollCourseRequest();
        request.setCourseCode("MATH101");

        // Act & Assert
        mockMvc.perform(post("/student/enroll")
                .header("Authorization", VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", containsString("Enrolled in course")));

        verify(studentService, times(1)).enrollInCourseByEmailAndCode(
                eq(TEST_EMAIL), eq("MATH101"), any(), anyString());
    }

    // ============ Test Case 2: Enroll - Missing courseCode ============
    @Test
    public void testEnrollInCourse_MissingCourseCode() throws Exception {
        // Arrange
        EnrollCourseRequest request = new EnrollCourseRequest();
        request.setCourseCode(null); // Missing required field

        // Act & Assert
        mockMvc.perform(post("/student/enroll")
                .header("Authorization", VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("courseCode")));
    }

    // ============ Test Case 3: Enroll - Blank courseCode ============
    @Test
    public void testEnrollInCourse_BlankCourseCode() throws Exception {
        // Arrange
        EnrollCourseRequest request = new EnrollCourseRequest();
        request.setCourseCode("   "); // Blank string

        // Act & Assert
        mockMvc.perform(post("/student/enroll")
                .header("Authorization", VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // ============ Test Case 4: Enroll - No Authentication ============
    @Test
    public void testEnrollInCourse_NoToken() throws Exception {
        // Arrange
        EnrollCourseRequest request = new EnrollCourseRequest();
        request.setCourseCode("MATH101");

        // Act & Assert (security filters disabled in this test class)
        mockMvc.perform(post("/student/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    // ============ Test Case 5: Create Student Profile - Success ============
    @Test
    public void testCreateStudentProfile_Success() throws Exception {
        // Arrange
        CreateStudentProfileRequest request = new CreateStudentProfileRequest();
        request.setUserId("USR123");
        request.setFullName("Alice Smith");
        request.setEmail("alice@greenwood.edu");
        request.setSchoolId("SCH001");
        request.setClassId(1L);

        // Act & Assert
        mockMvc.perform(post("/student/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(studentService, times(1)).createProfile(any());
    }

    // ============ Test Case 6: Create Student Profile - Missing Required Fields ============
    @Test
    public void testCreateStudentProfile_MissingFields() throws Exception {
        // Arrange
        CreateStudentProfileRequest request = new CreateStudentProfileRequest();
        request.setUserId("USR123");
        // Missing: fullName, email, schoolId, classId

        // Act & Assert
        mockMvc.perform(post("/student/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // ============ Test Case 7: Create Student Profile - Invalid Email ============
    @Test
    public void testCreateStudentProfile_InvalidEmail() throws Exception {
        // Arrange
        CreateStudentProfileRequest request = new CreateStudentProfileRequest();
        request.setUserId("USR123");
        request.setFullName("Alice Smith");
        request.setEmail("not-an-email"); // Invalid format
        request.setSchoolId("SCH001");
        request.setClassId(1L);

        // Act & Assert
        mockMvc.perform(post("/student/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("email")));
    }

    // ============ Test Case 8: Create Student Profile - Negative ClassId ============
    @Test
    public void testCreateStudentProfile_NegativeClassId() throws Exception {
        // Arrange
        CreateStudentProfileRequest request = new CreateStudentProfileRequest();
        request.setUserId("USR123");
        request.setFullName("Alice Smith");
        request.setEmail("alice@greenwood.edu");
        request.setSchoolId("SCH001");
        request.setClassId(-1L); // Invalid: negative

        // Act & Assert
        mockMvc.perform(post("/student/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // ============ Test Case 9: Submit Assignment - Success ============
    @Test
    public void testSubmitAssignment_Success() throws Exception {
        // Arrange
        SubmitAssignmentRequest request = new SubmitAssignmentRequest();
        request.setAssignmentId(100L);
        request.setCourseId(10L);
        request.setAssignmentTitle("Algebra Assignment 1");
        request.setSubmissionContent("My solutions to all problems");
        request.setFileUrl(null);

        // Act & Assert
        mockMvc.perform(post("/student/assignments/upload")
                .header("Authorization", VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", containsString("submitted")));

        verify(studentService, times(1)).submitAssignmentByEmail(eq(TEST_EMAIL), any());
    }

    // ============ Test Case 10: Submit Assignment - Missing Required Fields ============
    @Test
    public void testSubmitAssignment_MissingFields() throws Exception {
        // Arrange
        SubmitAssignmentRequest request = new SubmitAssignmentRequest();
        request.setAssignmentId(null); // Missing required
        request.setCourseId(null);     // Missing required
        request.setAssignmentTitle(""); // Missing required
        request.setSubmissionContent(null);
        request.setFileUrl(null);

        // Act & Assert
        mockMvc.perform(post("/student/assignments/upload")
                .header("Authorization", VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    // ============ Test Case 11: Submit Assignment - Content and File Both Empty ============
    @Test
    public void testSubmitAssignment_NoContentOrFile() throws Exception {
        // Arrange
        SubmitAssignmentRequest request = new SubmitAssignmentRequest();
        request.setAssignmentId(100L);
        request.setCourseId(10L);
        request.setAssignmentTitle("Algebra Assignment 1");
        request.setSubmissionContent("   "); // Blank
        request.setFileUrl("   ");           // Blank

        // Act & Assert
        mockMvc.perform(post("/student/assignments/upload")
                .header("Authorization", VALID_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("submissionContent or fileUrl")));
    }

    // ============ Test Case 12: Get Courses - Success ============
    @Test
    public void testGetCourses_Success() throws Exception {
        // Arrange
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setStudentId(1L);
        enrollment.setCourseId(10L);
        enrollment.setCourseName("Mathematics");

        when(studentService.getEnrolledCoursesByEmail(TEST_EMAIL))
                .thenReturn(List.of(enrollment));

        // Act & Assert
        mockMvc.perform(get("/student/courses")
                .header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].courseName").value("Mathematics"));
    }

    // ============ Test Case 13: Get Courses - Student Not Found ============
    @Test
    public void testGetCourses_StudentNotFound() throws Exception {
        // Arrange
        when(studentService.getEnrolledCoursesByEmail(TEST_EMAIL))
                .thenThrow(new StudentProfileNotFoundException("email", TEST_EMAIL));

        // Act & Assert
        mockMvc.perform(get("/student/courses")
                .header("Authorization", VALID_TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

    // ============ Test Case 14: Get Courses - No Authentication ============
    @Test
    public void testGetCourses_NoToken() throws Exception {
        // Arrange
        when(studentService.getEnrolledCoursesByEmail(TEST_EMAIL)).thenReturn(List.of());

        // Act & Assert (security filters disabled in this test class)
        mockMvc.perform(get("/student/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ============ Test Case 15: Get Materials - Success ============
    @Test
    public void testGetMaterials_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/student/materials/MATH101")
                .header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", containsString("Materials")));

        verify(courseServiceClient, times(1)).getMaterialsByCourseCode(
                eq("MATH101"), anyString());
    }

    // ============ Test Case 16: Get Assignments - Success ============
    @Test
    public void testGetAssignments_Success() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/student/assignments/MATH101")
                .header("Authorization", VALID_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", containsString("Assignments")));

        verify(courseServiceClient, times(1)).getAssignmentsByCourseCode(
                eq("MATH101"), anyString());
    }
}

