package com.edulink.complianceservice.controller;
import com.edulink.complianceservice.dto.PerformanceDto;
import com.edulink.complianceservice.dto.SchoolDto;
import com.edulink.complianceservice.entity.Performance;
import com.edulink.complianceservice.exception.ResourceFoundException;
import com.edulink.complianceservice.service.BoardService;
import com.edulink.complianceservice.service.ComplianceService;
import com.edulink.complianceservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/board")
public class BoardController {
    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private BoardService boardService;

    @Autowired
    private ComplianceService complianceService;

    @GetMapping("/schools/{schoolId}")
    public ResponseEntity<SchoolDto> getSchoolById(@PathVariable String schoolId){
            SchoolDto schools= boardService.getSchoolById(schoolId);
            return ResponseEntity.ok(schools);
    }

    @PostMapping("/audit-performance-create")
    public ResponseEntity<Performance> setPerformance(@RequestBody PerformanceDto performanceDto){
          return ResponseEntity.ok(boardService.saveData(performanceDto));
    }

    @GetMapping("/audit-performance")
    public ResponseEntity<List<Performance>> getPerformance(){
        return ResponseEntity.ok(boardService.getAllData());
    }

    @GetMapping("/report/{schoolId}")
    public ResponseEntity<Map<String,String>> report(@PathVariable String schoolId){
        return ResponseEntity.ok(boardService.getAllReport(schoolId));
    }

}
