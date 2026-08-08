package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.CommentPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.InteractBeforePostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.InteractBeforePost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.InteractBeforePostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InteractBeforePostService {

    @Autowired
    private InteractBeforePostRepository interactBeforePostRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    public InteractBeforePost getInteractPostByUserOrThrow(String userId) {
        InteractBeforePost interactBeforePost = interactBeforePostRepository.findByUser_Id(userId);
        if (interactBeforePost == null) {
            throw new ResourceNotFoundException("interact_before_post", "Interact before post not found for user: " + userId);
        }
        return interactBeforePost;
    }

    public InteractBeforePost createInteractBeforePostForUser(String userId) {
        User user = userService.findByIdOrThrow(userId);

        InteractBeforePost existed = interactBeforePostRepository.findByUser_Id(userId);
        if (existed != null) {
            return existed;
        }

        InteractBeforePost interactBeforePost = InteractBeforePost.builder()
                .user(user)
                .maxPostInteractPerBatch(Constant.MAX_POST_INTERACT_PER_BATCH)
                .build();
        return interactBeforePostRepository.save(interactBeforePost);
    }

    public InteractBeforePost updateInteractBeforePostByUser(String userId, InteractBeforePostRequest interactBeforePostRequest) {

        User user = userService.findByIdOrThrow(userId);

        InteractBeforePost interactBeforePost = interactBeforePostRepository.findByUser_Id(userId);

        if (interactBeforePost == null) {
            interactBeforePost = modelMapper.map(interactBeforePostRequest, InteractBeforePost.class);
            interactBeforePost.setUser(user);
        } else {
            modelMapper.map(interactBeforePostRequest, interactBeforePost);
        }

        return interactBeforePostRepository.save(interactBeforePost);
    }

    public InteractBeforePost findInteractBeforePostById(int id) {
        return interactBeforePostRepository.findById(id).orElse(null);
    }
}
