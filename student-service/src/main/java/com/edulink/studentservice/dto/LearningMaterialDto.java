package com.edulink.studentservice.dto;

<<<<<<< HEAD
import java.time.LocalDateTime;

=======
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
public class LearningMaterialDto {
    private Long id;
    private Long courseId;
    private String teacherEmail;
    private String title;
    private String description;
    private String fileUrl;
    private String materialType;
    private LocalDateTime uploadedAt;

<<<<<<< HEAD
    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getTeacherEmail() { return teacherEmail; }
    public void setTeacherEmail(String teacherEmail) { this.teacherEmail = teacherEmail; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getMaterialType() { return materialType; }
    public void setMaterialType(String materialType) { this.materialType = materialType; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
=======
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
}