package com.hoane.fbhelper.fbhelperextentionbe.dto.request;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class UserPageRequest {
    private Integer page = 0;
    private Integer size = 100;


    private String sortBy = "id";
    private String sortDirection = "desc";


    public Pageable toPageable() {

        Sort.Direction direction = sortDirection.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        return PageRequest.ofSize(size).withPage(page).withSort(sort);
    }

}
