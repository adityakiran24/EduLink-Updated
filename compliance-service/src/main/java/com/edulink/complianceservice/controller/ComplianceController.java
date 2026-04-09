package com.edulink.complianceservice.controller;
import com.edulink.complianceservice.dto.ComplianceRecordDto;
import com.edulink.complianceservice.dto.UserDto;
import com.edulink.complianceservice.entity.ComplianceRecord;
import com.edulink.complianceservice.entity.User;
import com.edulink.complianceservice.service.ComplianceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/compliance")
public class ComplianceController {


    @Autowired
    private ComplianceService complianceService;


    //create School Admin
    @PostMapping("/create-school-admin")
    public ResponseEntity<UserDto> createSchoolAdmin(@RequestBody @Valid UserDto newUser){
        return ResponseEntity.ok(complianceService.createSchoolAdmin(newUser));
    }


    //This controller call the complianceService.addData function for add data
    @PostMapping("/audit-school")
    public ResponseEntity<ComplianceRecord> schoolAudit(@RequestBody @Valid ComplianceRecordDto schoolAssignDto)throws Exception{

            ComplianceRecord getData=complianceService.addData(schoolAssignDto);
            return ResponseEntity.ok(getData);
    }

    @GetMapping({"/audit-records","/compliance-status"})
    public ResponseEntity<List<ComplianceRecord>> auditRecords(){
        return ResponseEntity.ok(complianceService.getAll());
    }

}
