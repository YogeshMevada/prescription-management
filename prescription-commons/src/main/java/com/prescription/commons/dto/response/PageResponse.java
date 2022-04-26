package com.prescription.commons.dto.response;

import com.prescription.commons.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Builder
@Getter
public class PageResponse<T> {

    private Set<T> records;

    private Pagination pagination;
}
