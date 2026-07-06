package com.hoane.fbhelper.fbhelperextentionbe.dto.response;


import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPostDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataGroupPostDetailResponse {
    private int id;

    private Boolean is_active;

    private String device_id;

    private Integer data_group_post_id;

    public DataGroupPostDetailResponse(DataGroupPostDetail dataGroupPostDetail) {
        this.id = dataGroupPostDetail.getId();
        this.is_active = dataGroupPostDetail.is_active();
        this.device_id = dataGroupPostDetail.getDevice().getId();
        this.data_group_post_id = dataGroupPostDetail.getData_group_post().getId();
    }
}
