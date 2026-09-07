package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.PostConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostConfigRepository extends JpaRepository<PostConfig, Integer> {
    PostConfig findByDevice_IdAndUser_Id(String deviceId, String userId);

    PostConfig findByDevice_Id(String deviceId);
}
