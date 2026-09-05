package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DataGroupPostDetailRepository extends JpaRepository<DataGroupPostDetail, Integer> {


    void deleteAllByDataGroupPost_Id(String data_group_id);

    DataGroupPostDetail findByDevice_IdAndDataGroupPost_Id(String device_id, String data_group_post_id);

    @Modifying
    @Query(value = "SELECT d.* FROM data_group_post_details d WHERE d.device_id = :device_id AND d.data_group_post_id IN (SELECT id FROM data_group_posts WHERE user_id = :user_id)", nativeQuery = true)
    List<DataGroupPostDetail> findAllByDevice_IdAndUser_Id(String device_id, String user_id);

    @Modifying
    @Query(value = "DELETE FROM data_group_post_details WHERE data_group_post_id IN (SELECT d.id FROM data_group_posts d WHERE d.user_id = :userId)", nativeQuery = true)
    void deleteAllByUser_Id(String userId);

    @Modifying
    @Query(value = "UPDATE data_group_post_details SET is_active = :isActive WHERE data_group_post_id = :dataGroupPostId", nativeQuery = true)
    void updateStatusAllDeviceByUserAndDataGroupPost(String dataGroupPostId, boolean isActive);
}
