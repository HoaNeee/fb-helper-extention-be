package com.hoane.fbhelper.fbhelperextentionbe.reporitory;

import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DataGroupPostRepository extends JpaRepository<DataGroupPost, Integer> {

    List<DataGroupPost> findAllByUser_Id(Integer userId);
}
