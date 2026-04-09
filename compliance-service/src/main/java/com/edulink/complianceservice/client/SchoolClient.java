package com.edulink.complianceservice.client;


import com.edulink.complianceservice.config.FeignClientConfig;
import com.edulink.complianceservice.dto.ApiResponse;
import com.edulink.complianceservice.dto.SchoolDto;

import com.edulink.complianceservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="identity-service-school",url="http://localhost:8081",configuration = FeignClientConfig.class)
public interface SchoolClient {


    @GetMapping("/compliance/identity/schools/{schoolId}")
    public ResponseEntity<ApiResponse<SchoolDto>> getSchoolById(@PathVariable String schoolId);


    @PostMapping("/compliance/identity/create-school-admin")
    public ResponseEntity<ApiResponse<UserDto>> createSchoolAdmin(@RequestHeader("Authorization") String token, @RequestBody UserDto userDto);


    @PostMapping({"/create-school"})
    public ResponseEntity<ApiResponse<SchoolDto>> createSchool( @RequestBody  SchoolDto schoolDto);

//    GetMapping()
}
