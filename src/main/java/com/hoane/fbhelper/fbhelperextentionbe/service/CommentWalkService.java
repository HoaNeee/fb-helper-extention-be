package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.CommentWalkRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RequestModels;
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
import java.util.stream.Collectors;

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
    public void updateStatusAllDevice(String userId, String commentWalkId, CommentWalkRequest.CommentWalkUpdateStatusAllDeviceRequest request) {

        CommentWalk commentWalk = findCommentWalkByIdOrThrow(commentWalkId);

        List<Device> devices = deviceService.findAllByUser(userId);

        Boolean isActive = request.getIsActive();

        devices.forEach(device -> {
            CommentWalkDetail commentWalkDetail = findCommentWalkDetail(device.getId(), commentWalkId);
            if (commentWalkDetail != null) {
                commentWalkDetail.setIsActive(isActive);
                commentWalkDetailRepository.save(commentWalkDetail);
            } else {
                commentWalkDetail = new CommentWalkDetail();
                commentWalkDetail.setDevice(device);
                commentWalkDetail.setCommentWalk(commentWalk);
                commentWalkDetail.setIsActive(isActive);
                commentWalkDetailRepository.save(commentWalkDetail);
            }
        });
    }

    @Transactional
    public List<CommentWalk> importCommentWalk(String userId, RequestModels.CommentWalkImportRequest request) {

        User u = userService.findByIdOrThrow(userId);

        String deviceId = request.deviceId();

        List<CommentWalkRequest.BaseRequest> listCommentWalk = request.listCommentWalk();
        if (listCommentWalk != null) {
            List<CommentWalk> commentWalkList = commentWalkRepository.saveAll(listCommentWalk.stream()
                    .map(rq -> CommentWalk.builder()
                            .id(IdGenerator.generateId(10))
                            .name(rq.getName())
                            .titleQuerySearchs(rq.getTitleQuerySearchs())
                            .keywordsCertainChoice(rq.getKeywordsCertainChoice())
                            .keywordQueryIncludes(rq.getKeywordQueryIncludes())
                            .keywordQueryExcludes(rq.getKeywordQueryExcludes())
                            .user(u)
                            .contents(rq.getContents())
                            .files(rq.getFiles())
                            .descriptionForAi(rq.getDescriptionForAi())
                            .matchRateValueContentQueryIncludes(rq.getMatchRateValueContentQueryIncludes())
                            .build())
                    .collect(Collectors.toList()));

            if (deviceId != null) {
                commentWalkDetailRepository.saveAll(commentWalkList.stream()
                        .map(cw -> CommentWalkDetail.builder()
                                .device(deviceService.findByIdOrThrow(deviceId))
                                .commentWalk(cw)
                                .isActive(false)
                                .build())
                        .collect(Collectors.toList()));
            }

            return commentWalkList;
        }

        return List.of();
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

    @Transactional
    public List<CommentWalkResponse.DataResponse> syncCommentWalkToDevice(String userId, RequestModels.DeviceSyncDataRequest request) {
        String currentDeviceId = request.currentDeviceId();
        String targetDeviceId = request.targetDeviceId();

        Device currentDevice = deviceService.findByIdOrThrow(currentDeviceId);
        Device targetDevice = deviceService.findByIdOrThrow(targetDeviceId);

        List<CommentWalkDetail> targetCommentWalkDetails = commentWalkDetailRepository.findAllByDevice_IdAndUser_Id(targetDeviceId, userId);
        List<CommentWalkDetail> commentWalkDetails = commentWalkDetailRepository.findAllByDevice_IdAndUser_Id(currentDeviceId, userId);

        List<CommentWalk> commentWalkList = commentWalkRepository.findAllByUser_Id(userId);


        commentWalkList.forEach(comment -> {
            CommentWalkDetail targetDetail = findCommentWalkDetailByDeviceIdAndCommentWalkIdWithList(targetDeviceId, comment.getId(), targetCommentWalkDetails);
            CommentWalkDetail existingDetail = findCommentWalkDetailByDeviceIdAndCommentWalkIdWithList(currentDeviceId, comment.getId(), commentWalkDetails);
            if (targetDetail == null) {
                targetDetail = new CommentWalkDetail();
                targetDetail.setCommentWalk(comment);
                targetDetail.setIsActive(false);
                targetDetail.setDevice(targetDevice);
                commentWalkDetailRepository.save(targetDetail);
            }
            if (existingDetail != null) {
                existingDetail.setIsActive(targetDetail.getIsActive());
                commentWalkDetailRepository.save(existingDetail);
            } else {
                existingDetail = new CommentWalkDetail();
                existingDetail.setCommentWalk(comment);
                existingDetail.setIsActive(targetDetail.getIsActive());
                existingDetail.setDevice(currentDevice);
                commentWalkDetailRepository.save(existingDetail);
            }
        });

        return getListCommentWalkAndDetails(userId, currentDeviceId);
    }


    CommentWalkDetail findCommentWalkDetailByDeviceIdAndCommentWalkIdWithList(String deviceId, String commentWalkId, List<CommentWalkDetail> commentWalkDetails) {
        return commentWalkDetails.stream()
                .filter(cwd -> cwd.getCommentWalk().getId().equals(commentWalkId) && cwd.getDevice().getId().equals(deviceId))
                .findFirst()
                .orElse(null);
    }

}
