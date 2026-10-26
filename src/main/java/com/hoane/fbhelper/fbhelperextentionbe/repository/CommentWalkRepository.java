package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentWalk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CommentWalkRepository extends JpaRepository<CommentWalk, String> {
    @Query("SELECT c FROM CommentWalk c WHERE c.user.id = :user_id AND (:keyword IS NULL OR LOWER(c.name) LIKE CONCAT('%', LOWER(:keyword), '%'))")
    Page<CommentWalk> findAllByUser_Id(String user_id, Pageable pageable, String keyword);

    List<CommentWalk> findAllByUser_Id(String user_id);

    void deleteAllByUser_Id(String user_id);

}
