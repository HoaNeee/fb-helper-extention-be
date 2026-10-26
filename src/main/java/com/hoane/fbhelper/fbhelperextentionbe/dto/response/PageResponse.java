package com.hoane.fbhelper.fbhelperextentionbe.dto.response;

import com.hoane.fbhelper.fbhelperextentionbe.entity.Pagination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private Integer status;
    private String message;
    private Boolean success;
    private T data;
    private Pagination pagination;
}
