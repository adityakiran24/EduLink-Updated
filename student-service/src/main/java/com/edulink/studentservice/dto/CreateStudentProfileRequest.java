package com.edulink.studentservice.dto;

<<<<<<< HEAD
public class CreateStudentProfileRequest {
    private String userId;
    private String fullName;
    private String email;
    private String schoolId;
    private Long classId;
    // Add more fields as needed

    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSchoolId() { return schoolId; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
=======
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentProfileRequest {
    @NotBlank(message = "userId is required")
    private String userId;

    @NotBlank(message = "fullName is required")
    private String fullName;

    @NotBlank(message = "email is required")
    @Email(message = "email must be a valid email address")
    private String email;

    @NotBlank(message = "schoolId is required")
    private String schoolId;

    @NotNull(message = "classId is required")
    @Positive(message = "classId must be greater than zero")
    private Long classId;
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
}
