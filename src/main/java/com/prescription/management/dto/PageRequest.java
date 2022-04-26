package com.prescription.management.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class PageRequest {

    private int size = 10;

    private int page = 0;

    private String order = Sort.Direction.DESC.name();

    private String orderProperty = "createdAt";
}
