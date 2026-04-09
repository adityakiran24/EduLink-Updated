package com.edulink.examservice.controller;

import com.edulink.examservice.dto.ApiResponse;
import com.edulink.examservice.entity.Exam;
import com.edulink.examservice.entity.Grade;
import com.edulink.examservice.service.ExamService;
import com.edulink.examservice.service.GradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * JUnit tests for ExamController API contracts.
 * These tests verify role-based authorization, exam creation, grading, and grade retrieval.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ExamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExamService examService;

    @MockBean
    private GradeService gradeService;

    // ============ Test Case 1: Create Exam - Success (Teacher) ============
    @Test
    @WithMockUser(username = "teacher@greenwood.edu", roles = {"TEACHER"})
    public void testCreateExam_Success() throws Exception {
        // Arrange
        Exam exam = new Exam();
        exam.setId(1L);
        exam.setExamTitle("Final Term Mathematics");
        exam.setCourseCode("MATH101");
        exam.setTotalMarks(100);
        exam.setPassingMarks(35);

        when(examService.createExam(any(Exam.class))).thenReturn(exam);

        String requestBody = objectMapper.writeValueAsString(exam);

        // Act & Assert
        mockMvc.perform(post("/teacher/create-exam")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", containsString("Exam created")))
                .andExpect(jsonPath("$.data.examTitle").value("Final Term Mathematics"));

        verify(examService, times(1)).createExam(any(Exam.class));
    }

    // ============ Test Case 2: Create Exam - Forbidden (Student) ============
    @Test
    @WithMockUser(username = "student@greenwood.edu", roles = {"STUDENT"})
    public void testCreateExam_ForbiddenStudent() throws Exception {
        // Arrange
        Exam exam = new Exam();
        exam.setExamTitle("Final Term Mathematics");
        exam.setCourseCode("MATH101");

        String requestBody = objectMapper.writeValueAsString(exam);

        // Act & Assert (Student cannot create exams)
        mockMvc.perform(post("/teacher/create-exam")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isForbidden());

        verify(examService, never()).createExam(any());
    }

    // ============ Test Case 3: Create Exam - Unauthorized (No Token) ============
    @Test
    public void testCreateExam_Unauthorized() throws Exception {
        // Arrange
        Exam exam = new Exam();
        exam.setExamTitle("Final Term Mathematics");

        String requestBody = objectMapper.writeValueAsString(exam);

        // Act & Assert
        mockMvc.perform(post("/teacher/create-exam")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isUnauthorized());

        verify(examService, never()).createExam(any());
    }

    //  Test Case 4: Grade Student - Success (Teacher)
    @Test
    @WithMockUser(username = "teacher@greenwood.edu", roles = {"TEACHER"})
    public void testGradeStudent_Success() throws Exception {
        // Arrange
        Grade grade = new Grade();
        grade.setId(1L);
        grade.setStudentId(1L);
        grade.setExamId(10L);
        grade.setMarksObtained(85);
        grade.setGrade("A");

        when(gradeService.gradeStudent(any(Grade.class))).thenReturn(grade);

        String requestBody = objectMapper.writeValueAsString(grade);

        // Act & Assert
        mockMvc.perform(post("/teacher/grade-student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", containsString("graded")))
                .andExpect(jsonPath("$.data.grade").value("A"));

        verify(gradeService, times(1)).gradeStudent(any(Grade.class));
    }

    // ============ Test Case 5: Grade Student - Forbidden (Student) ============
    @Test
    @WithMockUser(username = "student@greenwood.edu", roles = {"STUDENT"})
    public void testGradeStudent_ForbiddenStudent() throws Exception {
        // Arrange
        Grade grade = new Grade();
        grade.setStudentId(1L);
        grade.setExamId(10L);
        grade.setMarksObtained(85);

        String requestBody = objectMapper.writeValueAsString(grade);

        // Act & Assert
        mockMvc.perform(post("/teacher/grade-student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isForbidden());

        verify(gradeService, never()).gradeStudent(any());
    }

    // ============ Test Case 6: Get Student Grades - Success (Student) ============
    @Test
    @WithMockUser(username = "student@greenwood.edu", roles = {"STUDENT"})
    public void testGetStudentGrades_Success() throws Exception {
        // Arrange
        Grade grade1 = new Grade();
        grade1.setId(1L);
        grade1.setStudentId(1L);
        grade1.setMarksObtained(85);
        grade1.setGrade("A");

        Grade grade2 = new Grade();
        grade2.setId(2L);
        grade2.setStudentId(1L);
        grade2.setMarksObtained(78);
        grade2.setGrade("B+");

        when(gradeService.getGradesByStudentId(1L))
                .thenReturn(List.of(grade1, grade2));

        // Act & Assert
        mockMvc.perform(get("/student/grades")
                .param("studentId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message", containsString("Grades")))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].grade").value("A"));

        verify(gradeService, times(1)).getGradesByStudentId(1L);
    }

    // ============ Test Case 7: Get Student Grades - Forbidden (Teacher) ============
    @Test
    @WithMockUser(username = "teacher@greenwood.edu", roles = {"TEACHER"})
    public void testGetStudentGrades_ForbiddenTeacher() throws Exception {
        // Act & Assert (Teacher cannot access /student/grades)
        mockMvc.perform(get("/student/grades")
                .param("studentId", "1"))
                .andExpect(status().isForbidden());

        verify(gradeService, never()).getGradesByStudentId(anyLong());
    }

    // ============ Test Case 8: Get Student Grades - Unauthorized (No Token) ============
    @Test
    public void testGetStudentGrades_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/student/grades")
                .param("studentId", "1"))
                .andExpect(status().isUnauthorized());

        verify(gradeService, never()).getGradesByStudentId(anyLong());
    }

    // ============ Test Case 9: Get Student Grades - Without explicit studentId (uses context) ============
    @Test
    @WithMockUser(username = "student@greenwood.edu", roles = {"STUDENT"})
    public void testGetStudentGrades_WithoutStudentIdParam() throws Exception {
        // Arrange
        Grade grade = new Grade();
        grade.setId(1L);
        grade.setMarksObtained(85);

        when(gradeService.getGradesByStudentId(anyLong()))
                .thenReturn(List.of(grade));

        // Act & Assert
        mockMvc.perform(get("/student/grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ============ Test Case 10: Create Exam - Invalid Request Body ============
    @Test
    @WithMockUser(username = "teacher@greenwood.edu", roles = {"TEACHER"})
    public void testCreateExam_InvalidBody() throws Exception {
        // Arrange - Empty JSON
        String invalidBody = "{}";

        // Act & Assert
        mockMvc.perform(post("/teacher/create-exam")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidBody))
                .andExpect(status().isOk()); // Controller accepts, but exam fields are null
    }

    // ============ Test Case 11: Grade Student - Missing Required Fields ============
    @Test
    @WithMockUser(username = "teacher@greenwood.edu", roles = {"TEACHER"})
    public void testGradeStudent_MissingFields() throws Exception {
        // Arrange - Grade with missing critical fields
        Grade grade = new Grade();
        grade.setId(null);           // Missing ID
        grade.setStudentId(null);    // Missing studentId
        grade.setMarksObtained(null); // Missing marksObtained

        String requestBody = objectMapper.writeValueAsString(grade);

        // Act & Assert
        mockMvc.perform(post("/teacher/grade-student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk()); // Currently no validation; can improve
    }

    // ============ Test Case 12: Create Exam - Negative Marks ============
    @Test
    @WithMockUser(username = "teacher@greenwood.edu", roles = {"TEACHER"})
    public void testCreateExam_NegativeMarks() throws Exception {
        // Arrange
        Exam exam = new Exam();
        exam.setExamTitle("Math Exam");
        exam.setCourseCode("MATH101");
        exam.setTotalMarks(-100); // Invalid: negative
        exam.setPassingMarks(35);

        when(examService.createExam(any(Exam.class)))
                .thenReturn(exam);

        String requestBody = objectMapper.writeValueAsString(exam);

        // Act & Assert
        mockMvc.perform(post("/teacher/create-exam")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated()); // Currently no validation; should be 400
    }

    // ============ Test Case 13: Get Grades - Multiple Grades Response ============
    @Test
    @WithMockUser(username = "student@greenwood.edu", roles = {"STUDENT"})
    public void testGetStudentGrades_MultipleGrades() throws Exception {
        // Arrange
        Grade grade1 = new Grade();
        grade1.setId(1L);
        grade1.setMarksObtained(90);

        Grade grade2 = new Grade();
        grade2.setId(2L);
        grade2.setMarksObtained(85);

        Grade grade3 = new Grade();
        grade3.setId(3L);
        grade3.setMarksObtained(88);

        when(gradeService.getGradesByStudentId(anyLong()))
                .thenReturn(List.of(grade1, grade2, grade3));

        // Act & Assert
        mockMvc.perform(get("/student/grades")
                .param("studentId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data[0].marksObtained").value(90))
                .andExpect(jsonPath("$.data[2].marksObtained").value(88));
    }

    // ============ Test Case 14: Create Exam - Admin Role (Should Fail) ============
    @Test
    @WithMockUser(username = "admin@greenwood.edu", roles = {"SCHOOL_ADMIN"})
    public void testCreateExam_ForbiddenAdmin() throws Exception {
        // Arrange
        Exam exam = new Exam();
        exam.setExamTitle("Math Exam");

        String requestBody = objectMapper.writeValueAsString(exam);

        // Act & Assert
        mockMvc.perform(post("/teacher/create-exam")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isForbidden());
    }
}

