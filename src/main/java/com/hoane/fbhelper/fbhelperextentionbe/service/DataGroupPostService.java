package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DataGroupPostResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DataGroupPostRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataGroupPostService {
    @Autowired
    private DataGroupPostRepository repository;

    @Autowired
    private UserService userService;


    public List<DataGroupPost> findAll() {
        return repository.findAll();
    }

    @Transactional
    public DataGroupPostResponse save(DataGroupPostRequest dataGroupPostRequest) {

        User u = userService.findById(dataGroupPostRequest.getUser_id());


        DataGroupPost dataGroupPost = DataGroupPost.builder()
                .title_match(dataGroupPostRequest.getTitle_match())
                .name(dataGroupPostRequest.getName())
                .contents(dataGroupPostRequest.getContents())
                .images(dataGroupPostRequest.getImages())
                .from_member(dataGroupPostRequest.getFrom_member())
                .to_member(dataGroupPostRequest.getTo_member())
                .user(u)
                .priority(dataGroupPostRequest.getPriority())
                .build();

        DataGroupPost dataSave = repository.save(dataGroupPost);

        return new DataGroupPostResponse(dataSave);
    }

    @Transactional
    public DataGroupPostResponse update(DataGroupPostRequest dataGroupPostRequest) {

        
        return new DataGroupPostResponse();
    }
}
