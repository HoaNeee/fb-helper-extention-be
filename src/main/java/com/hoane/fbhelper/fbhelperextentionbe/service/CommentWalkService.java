package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.CommentWalkRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.CommentWalkResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DataGroupPostResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.*;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceExistsException;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.repository.CommentWalkDetailRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.CommentWalkRepository;
import com.hoane.fbhelper.fbhelperextentionbe.utils.IdGenerator;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;

@Service
public class CommentWalkService {
    @Autowired
    private CommentWalkRepository commentWalkRepository;

    @Autowired
    private CommentWalkDetailRepository commentWalkDetailRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ModelMapper modelMapper;


    public List<CommentWalk> findAllByUser(String user_id) {
        return commentWalkRepository.findAllByUser_Id(user_id);
    }

    public List<CommentWalkDetail> findAllDetailsByDeviceIdAndUserId(String deviceId, String userId) {
        return commentWalkDetailRepository.findAllByDevice_IdAndUser_Id(deviceId, userId);
    }

    public CommentWalk findCommentWalkByIdOrThrow(String commentWalkId) {
        return commentWalkRepository.findById(commentWalkId).orElseThrow(() -> new ResourceNotFoundException("comment_walk", "Data comment walk not found"));
    }

    public List<CommentWalkResponse.DataResponse> getListCommentWalkAndDetails(String user_id, String device_id) {
        List<CommentWalk> commentWalks = findAllByUser(user_id);
        List<CommentWalkDetail> commentWalkDetails = findAllDetailsByDeviceIdAndUserId(device_id, user_id);

        deviceService.findByIdOrThrow(device_id);

        List<CommentWalkResponse.DataResponse> list = commentWalks.stream()
                .map(commentWalk -> {
                    CommentWalkDetail commentWalkDetail = commentWalkDetails.stream()
                            .filter(detail -> detail.getCommentWalk().getId().equals(commentWalk.getId()))
                            .findFirst()
                            .orElse(null);

                    CommentWalkResponse.DataResponse commentWalkResponse = new CommentWalkResponse.DataResponse(commentWalk);
                    if (commentWalkDetail != null && commentWalkDetail.getIsActive() != null) {
                        commentWalkResponse.setIsActive(commentWalkDetail.getIsActive());
                    } else {
                        commentWalkResponse.setIsActive(false);
                    }
                    return commentWalkResponse;
                })
                .toList();
        return list;
    }

    @Transactional
    public CommentWalk createCommentWalk(CommentWalkRequest.BaseRequest commentWalkRequest, String user_id) {

        User u = userService.findByIdOrThrow(user_id);

        String id = IdGenerator.generateId(10);

        CommentWalk commentWalk = CommentWalk.builder()
                .id(id)
                .titleQuerySearchs(commentWalkRequest.getTitleQuerySearchs())
                .name(commentWalkRequest.getName())
                .contents(commentWalkRequest.getContents())
                .files(commentWalkRequest.getFiles())
                .keywordQueryIncludes(commentWalkRequest.getKeywordQueryIncludes())
                .keywordQueryExcludes(commentWalkRequest.getKeywordQueryExcludes())
                .keywordsCertainChoice(commentWalkRequest.getKeywordsCertainChoice())
                .matchRateValueContentQueryIncludes(commentWalkRequest.getMatchRateValueContentQueryIncludes())
                .user(u)
                .build();

        String device_id = commentWalkRequest.getDeviceId();

        CommentWalk result = commentWalkRepository.save(commentWalk);

        if (device_id != null) {
            Device device = deviceService.findByIdOrThrow(device_id);

            CommentWalkDetail detail = CommentWalkDetail.builder()
                    .device(device)
                    .commentWalk(result)
                    .isActive(false)
                    .build();

            commentWalkDetailRepository.save(detail);
        }

        return result;
    }

    @Transactional
    public CommentWalkResponse.DataResponse updateCommentWalk(String id, CommentWalkRequest.BaseRequest commentWalkRequest) {

        CommentWalk existCommentWalk = findCommentWalkByIdOrThrow(id);

        modelMapper.map(commentWalkRequest, existCommentWalk);

        commentWalkRepository.save(existCommentWalk);

        return new CommentWalkResponse.DataResponse(existCommentWalk);
    }

    @Transactional
    public void delete(String id) {

        commentWalkDetailRepository.deleteAllByCommentWalk_Id(id);

        commentWalkRepository.delete(findCommentWalkByIdOrThrow(id));
    }

    @Transactional
    public void deleteAll(String userId) {

        commentWalkDetailRepository.deleteAllByUser_Id(userId);

        commentWalkRepository.deleteAllByUser_Id(userId);
    }


    @Transactional
    public CommentWalkDetail createCommentWalkDetail(String userId, CommentWalkRequest.CommentWalkDetailRequest commentWalkDetailRequest) {

        Device device = deviceService.findByIdOrThrow(commentWalkDetailRequest.getDeviceId());
        CommentWalk commentWalk = findCommentWalkByIdOrThrow(commentWalkDetailRequest.getCommentWalkId());

        CommentWalkDetail existed = commentWalkDetailRepository.findByDevice_IdAndCommentWalk_Id(
                commentWalkDetailRequest.getDeviceId(),
                commentWalkDetailRequest.getCommentWalkId()
        );
        if (existed != null) {
            throw new ResourceExistsException("comment_walk_detail", "Comment walk detail already exists for this device and comment walk");
        }

        CommentWalkDetail commentWalkDetail = CommentWalkDetail.builder()
                .device(device)
                .commentWalk(commentWalk)
                .isActive(commentWalkDetailRequest.getIsActive())
                .build();

        return commentWalkDetailRepository.save(commentWalkDetail);
    }


    @Transactional
    public CommentWalkDetail updateCommentWalkDetail(CommentWalkRequest.CommentWalkDetailRequest commentWalkDetailRequest) {

        CommentWalkDetail commentWalkDetail = findCommentWalkDetail(commentWalkDetailRequest.getDeviceId(), commentWalkDetailRequest.getCommentWalkId());

        if (commentWalkDetail == null) {
            commentWalkDetail = CommentWalkDetail.builder()
                    .device(deviceService.findByIdOrThrow(commentWalkDetailRequest.getDeviceId()))
                    .commentWalk(findCommentWalkByIdOrThrow(commentWalkDetailRequest.getCommentWalkId()))
                    .isActive(commentWalkDetailRequest.getIsActive())
                    .build();
        } else {
            commentWalkDetail.setIsActive(commentWalkDetailRequest.getIsActive());
        }


        return commentWalkDetailRepository.save(commentWalkDetail);
    }

    public CommentWalkDetail findCommentWalkDetail(String device_id, String comment_walk_id) {
        return commentWalkDetailRepository.findByDevice_IdAndCommentWalk_Id(device_id, comment_walk_id);
    }


}
