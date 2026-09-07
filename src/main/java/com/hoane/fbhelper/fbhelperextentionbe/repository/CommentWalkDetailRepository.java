package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentWalkDetail;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentWalkDetailRepository extends JpaRepository<CommentWalkDetail, Integer> {
    void deleteAllByCommentWalk_Id(String data_group_id);

    CommentWalkDetail findByDevice_IdAndCommentWalk_Id(String device_id, String comment_walk_id);

    @Modifying
    @Query(value = "SELECT d.* FROM comment_walk_details d WHERE d.device_id = :device_id AND d.comment_walk_id IN (SELECT id FROM comment_walks WHERE user_id = :user_id)", nativeQuery = true)
    List<CommentWalkDetail> findAllByDevice_IdAndUser_Id(String device_id, String user_id);

    @Modifying
    @Query(value = "DELETE FROM comment_walk_details WHERE comment_walk_id IN (SELECT d.id FROM comment_walks d WHERE d.user_id = :userId)", nativeQuery = true)
    void deleteAllByUser_Id(String userId);
}
