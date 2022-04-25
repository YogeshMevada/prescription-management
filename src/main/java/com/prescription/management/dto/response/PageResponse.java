package com.prescription.management.dto.response;

import com.prescription.management.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class PageResponse<T> {

    private Set<T> records;

    private Pagination pagination;
}
