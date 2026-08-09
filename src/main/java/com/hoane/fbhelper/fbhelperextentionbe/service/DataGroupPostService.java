package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RequestModels;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DataGroupPostResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPostDetail;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceExistsException;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DataGroupPostDetailRepository;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DataGroupPostRepository;
import com.hoane.fbhelper.fbhelperextentionbe.utils.IdGenerator;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataGroupPostService {
    @Autowired
    private DataGroupPostRepository dataGroupPostRepository;

    @Autowired
    private DataGroupPostDetailRepository dataGroupPostDetailRepository;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    public List<DataGroupPost> findAllByUser(String user_id) {
        return dataGroupPostRepository.findAllByUser_Id(user_id);
    }

    public List<DataGroupPostDetail> findAllDetailsByDeviceIdAndUserId(String deviceId, String userId) {
        return dataGroupPostDetailRepository.findAllByDevice_IdAndUser_Id(deviceId, userId);
    }

    public DataGroupPost findDataGroupPostByIdOrThrow(String data_group_post_id) {
        return dataGroupPostRepository.findById(data_group_post_id).orElseThrow(() -> new ResourceNotFoundException("data_group_post", "Data group post not found"));
    }

    @Transactional
    public DataGroupPost createDataGroupPost(DataGroupPostRequest.DataGroupRequest dataGroupPostRequest, String user_id) {

        User u = userService.findByIdOrThrow(user_id);

        String id = IdGenerator.generateId(10);

        DataGroupPost dataGroupPost = DataGroupPost.builder()
                .id(id)
                .title(dataGroupPostRequest.getTitle())
                .name(dataGroupPostRequest.getName())
                .contents(dataGroupPostRequest.getContents())
                .files(dataGroupPostRequest.getFiles())
                .fromMember(dataGroupPostRequest.getFromMember())
                .toMember(dataGroupPostRequest.getToMember())
                .user(u)
                .priority(dataGroupPostRequest.getPriority())
                .build();

        String device_id = dataGroupPostRequest.getDeviceId();

        DataGroupPost result = dataGroupPostRepository.save(dataGroupPost);

        if (device_id != null) {
            Device device = deviceService.findByIdOrThrow(device_id);

            DataGroupPostDetail detail = DataGroupPostDetail.builder()
                    .device(device)
                    .dataGroupPost(result)
                    .isActive(false)
                    .build();

            dataGroupPostDetailRepository.save(detail);
        }

        return result;
    }

    @Transactional
    public DataGroupPostResponse.DataGroupResponse updateDataGroupPost(String id, DataGroupPostRequest.DataGroupRequest dataGroupPostRequest) {

        DataGroupPost existingDataGroupPost = dataGroupPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DataGroupPost", "Data group post not found"));


        modelMapper.map(dataGroupPostRequest, existingDataGroupPost);

        dataGroupPostRepository.save(existingDataGroupPost);

        return new DataGroupPostResponse.DataGroupResponse(existingDataGroupPost);
    }

    @Transactional
    public void delete(String id) {

        dataGroupPostDetailRepository.deleteAllByDataGroupPost_Id(id);

        dataGroupPostRepository.delete(dataGroupPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DataGroupPost", "Data group post not found")));
    }

    @Transactional
    public void deleteAll(String userId) {

        dataGroupPostDetailRepository.deleteAllByUser_Id(userId);

        dataGroupPostRepository.deleteAllByUser_Id(userId);
    }


    @Transactional
    public DataGroupPostDetail createDataGroupPostDetail(String userId, DataGroupPostRequest.DataGroupPostDetailRequest dataGroupPostDetailRequest) {

        User u = userService.findByIdOrThrow(userId);

        Device device = deviceService.findByIdOrThrow(dataGroupPostDetailRequest.getDeviceId());
        DataGroupPost dataGroupPost = findDataGroupPostByIdOrThrow(dataGroupPostDetailRequest.getDataGroupPostId());

        DataGroupPostDetail existed = dataGroupPostDetailRepository.findByDevice_IdAndDataGroupPost_Id(
                dataGroupPostDetailRequest.getDeviceId(),
                dataGroupPostDetailRequest.getDataGroupPostId()
        );
        if (existed != null) {
            throw new ResourceExistsException("data_group_post_detail", "Data group post detail already exists for this device and data group post");
        }

        DataGroupPostDetail dataGroupPostDetail = DataGroupPostDetail.builder()
                .device(device)
                .dataGroupPost(dataGroupPost)
                .isActive(dataGroupPostDetailRequest.getIsActive())
                .build();

        return dataGroupPostDetailRepository.save(dataGroupPostDetail);
    }


    @Transactional
    public DataGroupPostDetail updateDataGroupPostDetail(DataGroupPostRequest.DataGroupPostDetailRequest dataGroupPostDetailRequest) {

        DataGroupPostDetail dataGroupPostDetail = findDataGroupPostDetail(dataGroupPostDetailRequest.getDeviceId(), dataGroupPostDetailRequest.getDataGroupPostId());

        if (dataGroupPostDetail == null) {
            dataGroupPostDetail = DataGroupPostDetail.builder()
                    .device(deviceService.findByIdOrThrow(dataGroupPostDetailRequest.getDeviceId()))
                    .dataGroupPost(findDataGroupPostByIdOrThrow(dataGroupPostDetailRequest.getDataGroupPostId()))
                    .isActive(dataGroupPostDetailRequest.getIsActive())
                    .build();
        } else {
            dataGroupPostDetail.setIsActive(dataGroupPostDetailRequest.getIsActive());
        }


        return dataGroupPostDetailRepository.save(dataGroupPostDetail);
    }

    public DataGroupPostDetail findDataGroupPostDetail(String device_id, String data_group_id) {

        return dataGroupPostDetailRepository.findByDevice_IdAndDataGroupPost_Id(device_id, data_group_id);
    }

    public DataGroupPostDetail findDataGroupPostDetailOrThrow(String device_id, String data_group_id) {

        DataGroupPostDetail dataGroupPostDetail = dataGroupPostDetailRepository.findByDevice_IdAndDataGroupPost_Id(device_id, data_group_id);

        if (dataGroupPostDetail == null) {
            throw new ResourceNotFoundException("data_group_post_detail", "Data group post detail not found for this device and data group post");
        }

        return dataGroupPostDetail;
    }

    public List<DataGroupPost> importDataGroupPost(String userId, RequestModels.DataGroupPostImportRequest request) {


        User u = userService.findByIdOrThrow(userId);

        String deviceId = request.deviceId();

        List<DataGroupPostRequest.DataGroupRequest> listDataGroupPostRequest = request.listDataGroupPost();
        if (listDataGroupPostRequest != null) {
            List<DataGroupPost> dataGroupPostList = dataGroupPostRepository.saveAll(listDataGroupPostRequest.stream()
                    .map(dataGroupPostRequest -> DataGroupPost.builder()
                            .name(dataGroupPostRequest.getName())
                            .title(dataGroupPostRequest.getTitle())
                            .priority(dataGroupPostRequest.getPriority())
                            .id(IdGenerator.generateId(10))
                            .user(u)
                            .contents(dataGroupPostRequest.getContents())
                            .files(dataGroupPostRequest.getFiles())
                            .build())
                    .collect(Collectors.toList()));

            if (deviceId != null) {
                dataGroupPostDetailRepository.saveAll(dataGroupPostList.stream()
                        .map(dataGroupPost -> DataGroupPostDetail.builder()
                                .device(deviceService.findByIdOrThrow(deviceId))
                                .dataGroupPost(dataGroupPost)
                                .isActive(false)
                                .build())
                        .collect(Collectors.toList()));
            }

            return dataGroupPostList;
        }

        return List.of();


    }

    public Integer getMaxPriority(String userId) {
        return dataGroupPostRepository.getMaxPriorityByUser_Id(userId);
    }
}
