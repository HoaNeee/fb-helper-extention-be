package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DataGroupPostRepository extends JpaRepository<DataGroupPost, String> {

    List<DataGroupPost> findAllByUser_Id(String user_id);

    @Query(value = "SELECT dgp FROM DataGroupPost dgp WHERE dgp.user.id = :user_id AND (:keyword IS NULL OR LOWER(dgp.name) LIKE CONCAT('%', LOWER(:keyword), '%') OR LOWER(dgp.title) LIKE CONCAT('%', LOWER(:keyword), '%') )")
    Page<DataGroupPost> findAllByUser_Id(String user_id, Pageable pageable, String keyword);


    @Query("SELECT MAX(dgp.priority) FROM DataGroupPost dgp WHERE dgp.user.id = :user_id")
    Integer getMaxPriorityByUser_Id(String user_id);

    void deleteAllByUser_Id(String user_id);


}
