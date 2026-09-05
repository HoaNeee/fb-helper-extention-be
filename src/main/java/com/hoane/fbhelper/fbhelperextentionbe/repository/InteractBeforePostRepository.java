package com.hoane.fbhelper.fbhelperextentionbe.repository;

import com.hoane.fbhelper.fbhelperextentionbe.entity.InteractBeforePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractBeforePostRepository extends JpaRepository<InteractBeforePost, Integer> {
    InteractBeforePost findByUser_Id(String userId);
}
