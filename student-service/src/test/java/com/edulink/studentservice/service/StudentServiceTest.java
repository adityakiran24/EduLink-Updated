package com.edulink.studentservice.service;

import com.edulink.studentservice.dto.SubmitAssignmentRequest;
import com.edulink.studentservice.entity.AssignmentSubmission;
import com.edulink.studentservice.entity.Enrollment;
import com.edulink.studentservice.entity.StudentProfile;
import com.edulink.studentservice.exception.StudentAlreadyEnrolledException;
import com.edulink.studentservice.exception.StudentNotEnrolledInCourseException;
import com.edulink.studentservice.exception.StudentProfileNotFoundException;
import com.edulink.studentservice.repository.AssignmentSubmissionRepository;
import com.edulink.studentservice.repository.EnrollmentRepository;
import com.edulink.studentservice.repository.StudentProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * JUnit tests for StudentService business logic (service layer).
 * 
 * INTERVIEW KEY POINTS:
 * - Tests validate domain rules (no duplicates, enrollment checks, profile existence)
 * - Use Mockito to isolate service from DB dependencies
 * - Test both success and failure paths
 * - Assertions verify expected behavior and exception messages
 * - Verify repository calls to ensure data persistence
 */
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentProfileRepository profileRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private AssignmentSubmissionRepository submissionRepository;

    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentService = new StudentService(profileRepository, enrollmentRepository, submissionRepository);
    }

    // ============ Test Case 1: Get Student Profile by Email - Success ============
    @Test
    public void testGetStudentProfileByEmail_Success() {
        // Arrange
        StudentProfile expectedProfile = new StudentProfile();
        expectedProfile.setId(1L);
        expectedProfile.setEmail("student@greenwood.edu");
        expectedProfile.setFullName("Alice Smith");
        expectedProfile.setUserId("USR123");

        when(profileRepository.findByEmail("student@greenwood.edu"))
                .thenReturn(Optional.of(expectedProfile));

        // Act
        StudentProfile result = studentService.getStudentProfileByEmail("student@greenwood.edu");

        // Assert
        assertNotNull(result);
        assertEquals("Alice Smith", result.getFullName());
        assertEquals("student@greenwood.edu", result.getEmail());
        verify(profileRepository, times(1)).findByEmail("student@greenwood.edu");
    }

    // ============ Test Case 2: Get Student Profile by Email - Not Found ============
    @Test
    public void testGetStudentProfileByEmail_NotFound() {
        // Arrange
        when(profileRepository.findByEmail("nonexistent@greenwood.edu"))
                .thenReturn(Optional.empty());

        // Act & Assert
        StudentProfileNotFoundException exception = assertThrows(
                StudentProfileNotFoundException.class,
                () -> studentService.getStudentProfileByEmail("nonexistent@greenwood.edu"),
                "Expected StudentProfileNotFoundException"
        );

        assertTrue(exception.getMessage().contains("email"));
        verify(profileRepository, times(1)).findByEmail("nonexistent@greenwood.edu");
    }

    // ============ Test Case 3: Get Student Profile by UserId - Not Found ============
    @Test
    public void testGetStudentProfile_NotFound() {
        // Arrange
        when(profileRepository.findByUserId("UNKNOWN_USER"))
                .thenReturn(Optional.empty());

        // Act & Assert
        StudentProfileNotFoundException exception = assertThrows(
                StudentProfileNotFoundException.class,
                () -> studentService.getStudentProfile("UNKNOWN_USER")
        );

        assertTrue(exception.getMessage().contains("userId"));
    }

    // ============ Test Case 4: Enroll in Course - Duplicate Enrollment Prevented ============
    @Test
    public void testEnrollInCourse_AlreadyEnrolled() {
        // Arrange
        StudentProfile profile = new StudentProfile();
        profile.setId(1L);
        profile.setEmail("student@greenwood.edu");

        Enrollment existingEnrollment = new Enrollment();
        existingEnrollment.setStudentId(1L);
        existingEnrollment.setCourseId(10L);

        when(profileRepository.findByEmail("student@greenwood.edu"))
                .thenReturn(Optional.of(profile));
        when(enrollmentRepository.findByStudentId(1L))
                .thenReturn(List.of(existingEnrollment));

        // Act & Assert
        StudentAlreadyEnrolledException exception = assertThrows(
                StudentAlreadyEnrolledException.class,
                () -> studentService.enrollInCourseByEmail("student@greenwood.edu", 10L)
        );

        assertTrue(exception.getMessage().contains("already enrolled"));
        verify(enrollmentRepository, never()).save(any()); // Should NOT save
    }

    // ============ Test Case 5: Enroll in Course - Success ============
    @Test
    public void testEnrollInCourse_Success() {
        // Arrange
        StudentProfile profile = new StudentProfile();
        profile.setId(1L);
        profile.setEmail("student@greenwood.edu");

        when(profileRepository.findByEmail("student@greenwood.edu"))
                .thenReturn(Optional.of(profile));
        when(enrollmentRepository.findByStudentId(1L))
                .thenReturn(new ArrayList<>()); // No existing enrollments
        when(enrollmentRepository.save(any(Enrollment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        studentService.enrollInCourseByEmail("student@greenwood.edu", 10L);

        // Assert
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    // ============ Test Case 6: Submit Assignment - Success ============
    @Test
    public void testSubmitAssignmentByEmail_Success() {
        // Arrange
        StudentProfile profile = new StudentProfile();
        profile.setId(1L);
        profile.setEmail("student@greenwood.edu");

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(1L);
        enrollment.setCourseId(10L);

        SubmitAssignmentRequest request = new SubmitAssignmentRequest();
        request.setAssignmentId(100L);
        request.setCourseId(10L);
        request.setAssignmentTitle("Math Assignment");
        request.setSubmissionContent("My solutions");
        request.setFileUrl(null);

        when(profileRepository.findByEmail("student@greenwood.edu"))
                .thenReturn(Optional.of(profile));
        when(enrollmentRepository.findByStudentId(1L))
                .thenReturn(List.of(enrollment));
        when(submissionRepository.save(any(AssignmentSubmission.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        AssignmentSubmission result = studentService.submitAssignmentByEmail("student@greenwood.edu", request);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getStudentId());
        assertEquals("SUBMITTED", result.getStatus());
        verify(submissionRepository, times(1)).save(any(AssignmentSubmission.class));
    }

    // ============ Test Case 7: Submit Assignment - Student Not Enrolled ============
    @Test
    public void testSubmitAssignmentByEmail_NotEnrolledInCourse() {
        // Arrange
        StudentProfile profile = new StudentProfile();
        profile.setId(1L);
        profile.setEmail("student@greenwood.edu");

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(1L);
        enrollment.setCourseId(20L); // Different course

        SubmitAssignmentRequest request = new SubmitAssignmentRequest();
        request.setAssignmentId(100L);
        request.setCourseId(10L); // Not enrolled in this
        request.setAssignmentTitle("Math Assignment");
        request.setSubmissionContent("My solutions");

        when(profileRepository.findByEmail("student@greenwood.edu"))
                .thenReturn(Optional.of(profile));
        when(enrollmentRepository.findByStudentId(1L))
                .thenReturn(List.of(enrollment));

        // Act & Assert
        StudentNotEnrolledInCourseException exception = assertThrows(
                StudentNotEnrolledInCourseException.class,
                () -> studentService.submitAssignmentByEmail("student@greenwood.edu", request)
        );

        assertTrue(exception.getMessage().contains("not enrolled"));
        verify(submissionRepository, never()).save(any());
    }

    // ============ Test Case 8: Submit Assignment - Empty Content and File ============
    @Test
    public void testSubmitAssignmentByEmail_NoContentOrFile() {
        // Arrange
        StudentProfile profile = new StudentProfile();
        profile.setId(1L);
        profile.setEmail("student@greenwood.edu");

        SubmitAssignmentRequest request = new SubmitAssignmentRequest();
        request.setAssignmentId(100L);
        request.setCourseId(10L);
        request.setAssignmentTitle("Math Assignment");
        request.setSubmissionContent("   "); // Blank
        request.setFileUrl("   ");           // Blank

        when(profileRepository.findByEmail("student@greenwood.edu"))
                .thenReturn(Optional.of(profile));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.submitAssignmentByEmail("student@greenwood.edu", request)
        );

        assertTrue(exception.getMessage().contains("submissionContent or fileUrl"));
        verify(submissionRepository, never()).save(any());
    }

    // ============ Test Case 9: Get Enrolled Courses by Email ============
    @Test
    public void testGetEnrolledCoursesByEmail() {
        // Arrange
        StudentProfile profile = new StudentProfile();
        profile.setId(1L);
        profile.setEmail("student@greenwood.edu");

        Enrollment enrollment1 = new Enrollment();
        enrollment1.setStudentId(1L);
        enrollment1.setCourseId(10L);
        enrollment1.setCourseName("Mathematics");

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudentId(1L);
        enrollment2.setCourseId(20L);
        enrollment2.setCourseName("Science");

        when(profileRepository.findByEmail("student@greenwood.edu"))
                .thenReturn(Optional.of(profile));
        when(enrollmentRepository.findByStudentId(1L))
                .thenReturn(List.of(enrollment1, enrollment2));

        // Act
        List<Enrollment> result = studentService.getEnrolledCoursesByEmail("student@greenwood.edu");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Mathematics", result.get(0).getCourseName());
        assertEquals("Science", result.get(1).getCourseName());
    }

    // ============ Test Case 10: Get All Students by School ============
    @Test
    public void testGetAllStudentsBySchool() {
        // Arrange
        StudentProfile student1 = new StudentProfile();
        student1.setId(1L);
        student1.setFullName("Alice Smith");

        StudentProfile student2 = new StudentProfile();
        student2.setId(2L);
        student2.setFullName("Bob Johnson");

        when(profileRepository.findBySchoolId("SCH001"))
                .thenReturn(List.of(student1, student2));

        // Act
        List<StudentProfile> result = studentService.getAllStudentsBySchool("SCH001");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice Smith", result.get(0).getFullName());
    }

    // ============ Test Case 11: Create Student Profile ============
    @Test
    public void testCreateProfile() {
        // Arrange
        StudentProfile profile = new StudentProfile();
        profile.setUserId("USR123");
        profile.setFullName("Alice Smith");
        profile.setEmail("alice@greenwood.edu");

        when(profileRepository.findByUserId("USR123"))
                .thenReturn(Optional.empty());
        when(profileRepository.save(any(StudentProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        StudentProfile result = studentService.createProfile(profile);

        // Assert
        assertNotNull(result);
        assertEquals("Alice Smith", result.getFullName());
        verify(profileRepository, times(1)).save(any(StudentProfile.class));
    }

    // ============ Test Case 12: Get Submissions by UserId ============
    @Test
    public void testGetSubmissions() {
        // Arrange
        StudentProfile profile = new StudentProfile();
        profile.setId(1L);
        profile.setUserId("USR123");

        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setId(1L);
        submission.setStudentId(1L);

        when(profileRepository.findByUserId("USR123"))
                .thenReturn(Optional.of(profile));
        when(submissionRepository.findByStudentId(1L))
                .thenReturn(List.of(submission));

        // Act
        List<AssignmentSubmission> result = studentService.getSubmissions("USR123");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}

