package com.prescription.commons.dto.response;

import com.prescription.commons.dto.Pagination;
import lombok.Data;

import java.util.Set;

@Data
public class PageResponse<T> {

    private Set<T> records;

    private Pagination pagination;
}
