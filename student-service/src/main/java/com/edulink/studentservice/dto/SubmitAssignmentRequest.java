package com.edulink.studentservice.dto;
<<<<<<< HEAD
import jakarta.validation.constraints.NotNull;
public class SubmitAssignmentRequest {
    @NotNull private Long assignmentId;
    @NotNull private Long courseId;
=======

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAssignmentRequest {
    @NotNull private Long assignmentId;
    @NotNull private Long courseId;
    @NotBlank(message = "assignmentTitle is required")
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
    private String assignmentTitle;
    private String submissionContent;
    private String fileUrl;

<<<<<<< HEAD
    public SubmitAssignmentRequest() {
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getSubmissionContent() {
        return submissionContent;
    }

    public void setSubmissionContent(String submissionContent) {
        this.submissionContent = submissionContent;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
=======

    @AssertTrue(message = "Either submissionContent or fileUrl is required")
    public boolean isSubmissionProvided() {
        return hasText(submissionContent) || hasText(fileUrl);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
>>>>>>> 7cd35352abea80eb91e75cf8ea2946e5a5613c06
    }
}
