package com.hoane.fbhelper.fbhelperextentionbe.reporitory;

import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DataGroupPostRepository extends JpaRepository<DataGroupPost, String> {

    List<DataGroupPost> findAllByUser_Id(String user_id);

    @Query("SELECT MAX(dgp.priority) FROM DataGroupPost dgp WHERE dgp.user.id = :user_id")
    Integer getMaxPriorityByUser_Id(String user_id);

    void deleteAllByUser_Id(String user_id);
}
