package com.edulink.complianceservice.service;

import com.edulink.complianceservice.client.SchoolClient;
import com.edulink.complianceservice.dto.ApiResponse;
import com.edulink.complianceservice.dto.ComplianceRecordDto;
import com.edulink.complianceservice.dto.UserDto;
import com.edulink.complianceservice.entity.ComplianceRecord;
import com.edulink.complianceservice.entity.Performance;
import com.edulink.complianceservice.entity.User;
import com.edulink.complianceservice.exception.ResourceCreateException;
import com.edulink.complianceservice.exception.ResourceFoundException;
import com.edulink.complianceservice.repository.ComplianceRecordRepository;
import com.edulink.complianceservice.repository.UserRepository;
import com.edulink.complianceservice.utils.TokenContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplianceService {
    @Autowired
    private ComplianceRecordRepository complianceRecordRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SchoolClient schoolClient;


    public UserDto createSchoolAdmin(UserDto newUser)throws  ResourceFoundException{

        String token="Bearer "+TokenContext.getToken();
        ResponseEntity<ApiResponse<UserDto>> tem=schoolClient.createSchoolAdmin(token,newUser);

        if (tem.getBody() == null) {

            throw new ResourceFoundException("Empty response from School Service");
        }

        if (tem.getBody().getData() == null) {
            throw new ResourceFoundException("User not found");
        }

        return tem.getBody().getData();
    }

    //This service save record in Compliance_record table
    public ComplianceRecord addData(ComplianceRecordDto schoolAudit)throws ResourceCreateException{
        ComplianceRecord temp=new ComplianceRecord();
        temp.setAnotherId(schoolAudit.getAnotherId());
        temp.setAuditType(schoolAudit.getAuditType());
        temp.setAuditorEmail(schoolAudit.getAuditorEmail());
        temp.setStatus(schoolAudit.getStatus());
        return complianceRecordRepository.save(temp);
    }

    public List<ComplianceRecord> getAll()throws ResourceFoundException {
        List<ComplianceRecord>  allData=complianceRecordRepository.findAll();
        if(allData.isEmpty()){
            throw new ResourceFoundException("Resource is not available");
        }
        return allData;
    }
}
