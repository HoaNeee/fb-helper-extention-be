package com.hoane.fbhelper.fbhelperextentionbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePageRequest {
    private Integer page = 0;
    private Integer size = 1000;

    private String sortBy = "createdAt";
    private String direction = "desc";

    public Pageable toPageable() {
        Sort.Direction direction = this.direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.ofSize(size).withPage(page).withSort(sort);
    }
}
