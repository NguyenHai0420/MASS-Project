package com.group_project.MASS.service;

import com.group_project.MASS.dto.SpecialtyDto;
import com.group_project.MASS.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyService {

    @Autowired
    private SpecialtyRepository specialtyRepository;

    public List<SpecialtyDto> getAllSpecialties() {
        return specialtyRepository.findAll().stream().map(s -> 
            SpecialtyDto.builder()
                .id(s.getId())
                .name(s.getName())
                .description(s.getDescription())
                .imageUrl(s.getImageUrl())
                .build()
        ).collect(Collectors.toList());
    }
}
