package com.edulink.studentservice.config;

import com.edulink.studentservice.entity.*;
import com.edulink.studentservice.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DataInitializer {
    private final StudentProfileRepository profileRepo;
    private final EnrollmentRepository enrollmentRepo;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (profileRepo.count() == 0) {
                StudentProfile s1 = StudentProfile.builder()
                        .userId("student@greenwood.edu").studentCode("STU001").fullName("Alice Smith")
                        .email("student@greenwood.edu").grade("Grade 10").section("A")
                        .schoolId("SCH001").parentName("John Smith").parentContact("9876543210")
                        .dateOfBirth(LocalDate.of(2007, 5, 15)).build();
                profileRepo.save(s1);

                enrollmentRepo.save(Enrollment.builder().studentId(1L).courseId(1L)
                        .courseName("Mathematics").courseCode("MATH101").status("ACTIVE").build());
                enrollmentRepo.save(Enrollment.builder().studentId(1L).courseId(2L)
                        .courseName("Physics").courseCode("PHY101").status("ACTIVE").build());
                enrollmentRepo.save(Enrollment.builder().studentId(1L).courseId(3L)
                        .courseName("English Literature").courseCode("ENG101").status("ACTIVE").build());
                log.info("==> Student sample data initialized");
            }
        };
    }
}
