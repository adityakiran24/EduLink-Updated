package com.edulink.studentservice.dto;

<<<<<<< HEAD
import java.time.LocalDate;
import java.time.LocalDateTime;

=======
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
public class AttendanceDto {
    private Long id;
    private Long studentId;
    private Long courseId;
    private String schoolId;
    private LocalDate attendanceDate;
    private String status;
    private String markedBy;
    private LocalDateTime createdAt;

<<<<<<< HEAD
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public String getSchoolId() { return schoolId; }
    public void setSchoolId(String schoolId) { this.schoolId = schoolId; }
    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) { this.attendanceDate = attendanceDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getMarkedBy() { return markedBy; }
    public void setMarkedBy(String markedBy) { this.markedBy = markedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
=======
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
}
