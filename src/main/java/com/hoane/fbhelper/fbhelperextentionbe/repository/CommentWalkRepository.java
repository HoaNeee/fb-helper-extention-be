package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentWalk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentWalkRepository extends JpaRepository<CommentWalk, String> {

    List<CommentWalk> findAllByUser_Id(String user_id);
    
    void deleteAllByUser_Id(String user_id);

}
