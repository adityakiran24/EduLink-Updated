package com.edulink.complianceservice.controller;
import com.edulink.complianceservice.dto.ApiResponse;
import com.edulink.complianceservice.entity.ComplianceRecord;
import com.edulink.complianceservice.service.ComplianceService;
import com.edulink.complianceservice.service.RegulatorService;
import com.edulink.complianceservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@PreAuthorize("hasRole('REGULATOR')") @RequiredArgsConstructor
@RestController
@RequestMapping("/regulator")
public class RegulatorController {


    @Autowired
    private ComplianceService complianceService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RegulatorService regulatorService;



    @GetMapping("/compliance-report")
    public ResponseEntity<ApiResponse<List<ComplianceRecord>>> complianceReport(){
        return ResponseEntity.ok(ApiResponse.success("Compliance-Report",complianceService.getAll()));
    }

    @GetMapping("/accreditation-status/{schoolId}")
    public ResponseEntity<Map<String,String>> accreditationStatus(@PathVariable String schoolId){
        Map<String,String> tem=regulatorService.getAccreditationStatus(schoolId);
        return ResponseEntity.ok(tem);
    }

}
