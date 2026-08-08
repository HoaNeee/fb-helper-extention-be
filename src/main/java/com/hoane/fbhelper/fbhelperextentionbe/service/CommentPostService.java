package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.CommentPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.entity.CommentPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.CommentPostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentPostService {

    @Autowired
    private CommentPostRepository commentPostRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    public CommentPost getCommentPostByUserOrThrow(String userId) {
        CommentPost commentPost = commentPostRepository.findByUser_Id(userId);
        if (commentPost == null) {
            throw new ResourceNotFoundException("comment_post", "Comment post not found for user: " + userId);
        }
        return commentPost;
    }

    public CommentPost createNewCommentPostForUser(String userId) {
        User user = userService.findByIdOrThrow(userId);

        CommentPost existingCommentPost = commentPostRepository.findByUser_Id(userId);
        if (existingCommentPost != null) {
            return existingCommentPost;
        }

        CommentPost commentPost = CommentPost.builder()
                .user(user)
                .maxCommentPerPost(Constant.MAX_COMMENT_PER_POST)
                .build();
        return commentPostRepository.save(commentPost);
    }

    public CommentPost updateCommentPostByUser(String userId, CommentPostRequest commentPostRequest) {

        User user = userService.findByIdOrThrow(userId);

        CommentPost commentPost = commentPostRepository.findByUser_Id(userId);

        if (commentPost == null) {
            commentPost = modelMapper.map(commentPostRequest, CommentPost.class);
            commentPost.setUser(user);
        } else {
            modelMapper.map(commentPostRequest, commentPost);
        }

        return commentPostRepository.save(commentPost);
    }

    public CommentPost findCommentPostById(int id) {
        return commentPostRepository.findById(id).orElse(null);
    }

}
