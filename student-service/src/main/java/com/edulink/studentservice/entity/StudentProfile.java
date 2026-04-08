package com.edulink.studentservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String userId;

    private String studentCode;

    @NotBlank
    @Column(nullable = false)
    private String fullName;

    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    private LocalDate dateOfBirth;
    private String grade;
    private String section;

    @NotBlank
    @Column(nullable = false)
    private String schoolId;

    private String parentName;
    private String parentContact;

    @Column(nullable = false)
    private LocalDateTime enrolledAt;

    @NotNull
    @Column(nullable = false)
    private Long classId;

    @PrePersist protected void onCreate() { enrolledAt = LocalDateTime.now(); }
}
