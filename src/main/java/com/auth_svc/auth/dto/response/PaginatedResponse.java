package com.auth_svc.auth.dto.response;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedResponse<T> {
    @Builder.Default
    int code = 1000;

    String message;
    java.util.List<T> content;
    PaginationMetadata pagination;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PaginationMetadata {
        int page;
        int size;
        long totalElements;
        int totalPages;
        boolean first;
        boolean last;
        boolean hasNext;
        boolean hasPrevious;
        int numberOfElements;
    }

    public static <T> PaginatedResponse<T> of(Page<T> page) {
        PaginationMetadata metadata = PaginationMetadata.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .numberOfElements(page.getNumberOfElements())
                .build();

        return PaginatedResponse.<T>builder()
                .content(page.getContent())
                .pagination(metadata)
                .build();
    }
}
