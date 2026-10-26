package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentWalkConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentWalkConfigRepository extends JpaRepository<CommentWalkConfig, Integer> {
    CommentWalkConfig findByDevice_IdAndUser_Id(String deviceId, String userId);

    CommentWalkConfig findByDevice_Id(String deviceId);

    List<CommentWalkConfig> findAllByDevice_Id(String deviceId);
}
