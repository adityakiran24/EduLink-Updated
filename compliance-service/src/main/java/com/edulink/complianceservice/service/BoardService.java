package com.edulink.complianceservice.service;


import com.edulink.complianceservice.client.SchoolClient;
import com.edulink.complianceservice.dto.ApiResponse;
import com.edulink.complianceservice.dto.PerformanceDto;
import com.edulink.complianceservice.dto.SchoolDto;
import com.edulink.complianceservice.entity.Performance;
import com.edulink.complianceservice.exception.ResourceCreateException;
import com.edulink.complianceservice.exception.ResourceFoundException;
import com.edulink.complianceservice.repository.PerformanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardService {
    @Autowired
    private PerformanceRepository performanceRepository;

    @Autowired
    private SchoolClient schoolClient;


//    public List<SchoolDto> allSchool()throws ResourceFoundException {
//        List<SchoolDto> schoolDto=schoolClient.getSchools();
//        if(schoolDto.isEmpty()){
//            throw new ResourceFoundException("Resource is not available");
//        }
//        return schoolDto;
//    }

//    public String healthcheck(){
//        String st=schoolClient.health();
//        System.out.println("res = "+st);
//        return st;
//    }

    public SchoolDto getSchoolById(String schoolId)throws ResourceFoundException {
        ResponseEntity<ApiResponse<SchoolDto>> res=schoolClient.getSchoolById(schoolId);
        ApiResponse<SchoolDto> body=res.getBody();
        if(body==null || body.getData()==null){
            throw new ResourceFoundException("Resource is not available");
        }
        return body.getData();
    }

    public Performance saveData(PerformanceDto performanceDto)throws ResourceCreateException {
        Performance tem=performanceDto.getPerformance();
        tem.setSchoolId(performanceDto.getSchoolId());
        return performanceRepository.save(tem);
    }

    public List<Performance> getAllData()throws ResourceFoundException{
        List<Performance>  allData=performanceRepository.findAll();
        if(allData.isEmpty()){
            throw new ResourceFoundException("Resource is not available");
        }
        return allData;
    }

    public Map<String,String> getAllReport(String schoolId) throws ResourceFoundException {
        ResponseEntity<ApiResponse<SchoolDto>> res=schoolClient.getSchoolById(schoolId);
        if( res.getBody()==null || res.getBody().getData()==null){
            throw new ResourceFoundException("Resource is not available");
        }
        SchoolDto school=res.getBody().getData();
        long totalTeacherCount=0;
        long totalStudentCount=0;

        return Map.of("schoolName",school.getName(),"totalTeacher",school.getTeacherNumber()+"","totalStudent",school.getStudentNumber()+"");
    }
}
