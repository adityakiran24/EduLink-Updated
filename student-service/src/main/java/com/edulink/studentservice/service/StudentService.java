package com.edulink.studentservice.service;

import com.edulink.studentservice.client.CourseServiceClient;
import com.edulink.studentservice.dto.SubmitAssignmentRequest;
import com.edulink.studentservice.entity.*;
<<<<<<< HEAD
import com.edulink.studentservice.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
=======
import com.edulink.studentservice.exception.CourseNotFoundException;
import com.edulink.studentservice.exception.StudentAlreadyEnrolledException;
import com.edulink.studentservice.exception.StudentNotEnrolledInCourseException;
import com.edulink.studentservice.exception.StudentProfileNotFoundException;
import com.edulink.studentservice.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
import org.springframework.stereotype.Service;
import java.util.List;

@Service
<<<<<<< HEAD
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

=======
@Slf4j
@RequiredArgsConstructor
public class StudentService {

>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
    private final StudentProfileRepository profileRepo;
    private final EnrollmentRepository enrollmentRepo;
    private final AssignmentSubmissionRepository submissionRepo;

<<<<<<< HEAD
    public StudentService(StudentProfileRepository profileRepo,
                          EnrollmentRepository enrollmentRepo,
                          AssignmentSubmissionRepository submissionRepo) {
        this.profileRepo = profileRepo;
        this.enrollmentRepo = enrollmentRepo;
        this.submissionRepo = submissionRepo;
    }

    public StudentProfile getStudentProfile(String userId) {
        return profileRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
=======

    public StudentProfile getStudentProfile(String userId) {
        return profileRepo.findByUserId(userId)
                .orElseThrow(() -> new StudentProfileNotFoundException("userId", userId));
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
    }

    public StudentProfile getStudentProfileByEmail(String email) {
        return profileRepo.findByEmail(email)
<<<<<<< HEAD
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
=======
                .orElseThrow(() -> new StudentProfileNotFoundException("email", email));
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
    }

    public List<Enrollment> getEnrolledCoursesByEmail(String email) {
        StudentProfile profile = getStudentProfileByEmail(email);
        return enrollmentRepo.findByStudentId(profile.getId());
    }

    public AssignmentSubmission submitAssignmentByEmail(String email, SubmitAssignmentRequest request) {
        StudentProfile profile = getStudentProfileByEmail(email);
<<<<<<< HEAD
=======
        if (!hasText(request.getSubmissionContent()) && !hasText(request.getFileUrl())) {
            throw new IllegalArgumentException("Either submissionContent or fileUrl is required");
        }
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
        // Check if student is enrolled in the course
        if (request.getCourseId() != null) {
            List<Enrollment> enrollments = enrollmentRepo.findByStudentId(profile.getId());
            boolean enrolled = enrollments.stream().anyMatch(e -> e.getCourseId().equals(request.getCourseId()));
            if (!enrolled) {
<<<<<<< HEAD
                throw new RuntimeException("Student is not enrolled in the specified course");
=======
                throw new StudentNotEnrolledInCourseException(email, request.getCourseId());
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
            }
        }
        AssignmentSubmission submission = AssignmentSubmission.builder()
                .studentId(profile.getId())
                .assignmentId(request.getAssignmentId())
                .assignmentTitle(request.getAssignmentTitle())
                .submissionContent(request.getSubmissionContent())
                .fileUrl(request.getFileUrl())
                .status("SUBMITTED")
                .build();
        return submissionRepo.save(submission);
    }

<<<<<<< HEAD
=======
    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
    public void enrollInCourse(String userId, Long courseId) {
        StudentProfile profile = getStudentProfile(userId);
        // Check if already enrolled
        List<Enrollment> enrollments = enrollmentRepo.findByStudentId(profile.getId());
        boolean alreadyEnrolled = enrollments.stream().anyMatch(e -> e.getCourseId().equals(courseId));
        if (alreadyEnrolled) {
<<<<<<< HEAD
            throw new RuntimeException("Student is already enrolled in this course");
=======
            throw new StudentAlreadyEnrolledException(profile.getEmail(), String.valueOf(courseId));
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
        }
        // TODO: Optionally validate course exists by calling course-service
        Enrollment enrollment = Enrollment.builder()
                .studentId(profile.getId())
                .courseId(courseId)
                .enrolledAt(java.time.LocalDateTime.now())
                .build();
        enrollmentRepo.save(enrollment);
    }

    public void enrollInCourseByEmail(String email, Long courseId) {
        StudentProfile profile = getStudentProfileByEmail(email);
        // Check if already enrolled
        List<Enrollment> enrollments = enrollmentRepo.findByStudentId(profile.getId());
        boolean alreadyEnrolled = enrollments.stream().anyMatch(e -> e.getCourseId().equals(courseId));
        if (alreadyEnrolled) {
<<<<<<< HEAD
            throw new RuntimeException("Student is already enrolled in this course");
=======
            throw new StudentAlreadyEnrolledException(email, String.valueOf(courseId));
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
        }
        // TODO: Optionally validate course exists by calling course-service
        Enrollment enrollment = Enrollment.builder()
                .studentId(profile.getId())
                .courseId(courseId)
                .enrolledAt(java.time.LocalDateTime.now())
                .build();
        enrollmentRepo.save(enrollment);
    }

    public void enrollInCourseByEmailAndCode(String email, String courseCode, CourseServiceClient courseServiceClient, String token) {
        StudentProfile profile = getStudentProfileByEmail(email);
        // Get courseId from courseCode
        Long courseId = courseServiceClient.getCourseIdByCode(courseCode, token);
        if (courseId == null) {
<<<<<<< HEAD
            throw new RuntimeException("Course not found");
=======
            throw new CourseNotFoundException(courseCode);
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
        }
        // Check if already enrolled
        List<Enrollment> enrollments = enrollmentRepo.findByStudentId(profile.getId());
        boolean alreadyEnrolled = enrollments.stream().anyMatch(e -> e.getCourseId().equals(courseId));
        if (alreadyEnrolled) {
<<<<<<< HEAD
            throw new RuntimeException("Student is already enrolled in this course");
=======
            throw new StudentAlreadyEnrolledException(email, courseCode);
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
        }
        Enrollment enrollment = Enrollment.builder()
                .studentId(profile.getId())
                .courseId(courseId)
                .enrolledAt(java.time.LocalDateTime.now())
                .build();
        enrollmentRepo.save(enrollment);
    }

    public List<AssignmentSubmission> getSubmissions(String userId) {
        StudentProfile profile = getStudentProfile(userId);
        return submissionRepo.findByStudentId(profile.getId());
    }

    public StudentProfile createProfile(StudentProfile profile) {
        log.info("Saving student profile for userId: {}", profile.getUserId());

        profileRepo.findByUserId(profile.getUserId()).ifPresent(existing -> profile.setId(existing.getId()));
        if (profile.getId() == null && profile.getEmail() != null) {
            profileRepo.findByEmail(profile.getEmail()).ifPresent(existing -> profile.setId(existing.getId()));
        }

        StudentProfile saved = profileRepo.save(profile);
        log.info("Saved student profile with id: {}", saved.getId());
        return saved;
    }

    // Admin methods
    public List<StudentProfile> getAllStudentsBySchool(String schoolId) {
        return profileRepo.findBySchoolId(schoolId);
    }
}
