package com.prescription.management.util;

import com.prescription.management.dto.PageRequest;
import com.prescription.management.dto.Pagination;
import com.prescription.management.dto.response.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class CommonUtil {

    public static Pageable getPageableInfo(final PageRequest page) {
        return getPageable(page.getPage(), page.getSize(), page.getOrder(), page.getOrderProperty());
    }

    private static Pageable getPageable(final int page, final int size, final String direction, final String property) {
        final int pageLocal = page <= 0 ? 0 : page;
        final int sizeLocal = size <= 0 ? 20 : (size > 100 ? 100 : size);
        final Sort.Direction directionLocal = StringUtils.isNotEmpty(direction) ? Sort.Direction.fromString(direction) : Sort.Direction.DESC;
        final String propertyLocal = StringUtils.isNotEmpty(property) ? property : "createdAt";
        return org.springframework.data.domain.PageRequest.of(pageLocal, sizeLocal, directionLocal, propertyLocal);
    }

    public static <T, R> PageResponse<R> createPageResponse(Page<T> page, Function<T, R> mapper) {
        final Pagination pagination = new Pagination();
        pagination.setTotalElements(page.getTotalElements());
        pagination.setTotalPages(pagination.getTotalPages());
        return PageResponse.<R>builder()
                .records(page.stream()
                        .map(mapper)
                        .collect(Collectors.toSet()))
                .pagination(pagination)
                .build();
    }
}
