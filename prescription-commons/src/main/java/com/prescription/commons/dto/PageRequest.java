package com.prescription.commons.dto;

import lombok.Data;

@Data
public class PageRequest {

    private int size = 10;

    private int page = 0;

    private String order = "DESC";

    private String orderProperty = "createdAt";
}
