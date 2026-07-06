package com.hoane.fbhelper.fbhelperextentionbe.reporitory;

import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DataGroupPostDetailRepository extends JpaRepository<DataGroupPostDetail, Integer> {

    @Modifying
    @Query("DELETE FROM DataGroupPostDetail d WHERE d.data_group_post.id = :data_group_id")
    void deleteAllByData_group_post_id(@Param("data_group_id") int data_group_id);


    @Query("SELECT d FROM DataGroupPostDetail d WHERE d.device.id = :device_id AND d.data_group_post.id = :data_group_post_id")
    DataGroupPostDetail findByDevice_IdAndData_Group_Post_Id(@Param("device_id") String device_id, @Param("data_group_post_id") int data_group_post_id);

    @Query("SELECT d FROM DataGroupPostDetail d WHERE d.device.id = :device_id")
    List<DataGroupPostDetail> findAllDetailsByDevice_Id(@Param("device_id") String device_id);

}
