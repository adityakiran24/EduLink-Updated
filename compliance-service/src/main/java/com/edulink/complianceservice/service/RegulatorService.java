package com.edulink.complianceservice.service;

import com.edulink.complianceservice.dto.SchoolDto;
import com.edulink.complianceservice.exception.ResourceFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RegulatorService {

    @Autowired
    private BoardService boardService;

    public Map<String,String> getAccreditationStatus(String schoolId)throws ResourceFoundException {

        SchoolDto school=boardService.getSchoolById(schoolId);
        if(school==null){
            throw new ResourceFoundException("Resource is not available");
        }

        List<Map<String,String>> tem=new ArrayList<>();
        Map<String,String> map=Map.of("schoolId", school.getId(),"school",school.getName(),"status","Active","validUntil","Valid");


        return  map;
    }
}
