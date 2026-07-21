package com.group_project.MASS.service.impl;

import com.group_project.MASS.dto.SpecialtyDto;
import com.group_project.MASS.repository.SpecialtyRepository;
import com.group_project.MASS.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Override
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
