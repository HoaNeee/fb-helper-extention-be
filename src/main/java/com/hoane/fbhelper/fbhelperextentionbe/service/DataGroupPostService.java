package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostDetailRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DataGroupPostRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DataGroupPostResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPostDetail;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceExistsException;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DataGroupPostDetailRepository;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DataGroupPostRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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


    public List<DataGroupPost> findAllByUser(int user_id) {
        return dataGroupPostRepository.findAllByUser_Id(user_id);
    }


    public DataGroupPost findDataGroupPostByIdOrThrow(int data_group_post_id) {
        return dataGroupPostRepository.findById(data_group_post_id).orElseThrow(() -> new ResourceNotFoundException("data_group_post", "Data group post not found"));
    }

    @Transactional
    public DataGroupPost createDataGroupPost(DataGroupPostRequest dataGroupPostRequest, int user_id) {

        User u = userService.findByIdOrThrow(user_id);


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

        return dataGroupPostRepository.save(dataGroupPost);
    }

    @Transactional
    public DataGroupPostResponse update(int id, DataGroupPostRequest dataGroupPostRequest) {

        DataGroupPost existingDataGroupPost = dataGroupPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DataGroupPost", "Data group post not found"));

        modelMapper.map(dataGroupPostRequest, existingDataGroupPost);

        dataGroupPostRepository.save(existingDataGroupPost);

        return new DataGroupPostResponse(existingDataGroupPost);
    }

    @Transactional
    public void delete(int id) {

        dataGroupPostDetailRepository.deleteAllByData_group_post_id(id);

        dataGroupPostRepository.delete(dataGroupPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DataGroupPost", "Data group post not found")));
    }


    @Transactional
    public DataGroupPostDetail createDataGroupPostDetail(DataGroupPostDetailRequest dataGroupPostDetailRequest) {

        Device device = deviceService.findByIdOrThrow(dataGroupPostDetailRequest.getDevice_id());
        DataGroupPost dataGroupPost = findDataGroupPostByIdOrThrow(dataGroupPostDetailRequest.getData_group_post_id());

        DataGroupPostDetail existed = dataGroupPostDetailRepository.findByDevice_IdAndData_Group_Post_Id(
                dataGroupPostDetailRequest.getDevice_id(),
                dataGroupPostDetailRequest.getData_group_post_id()
        );
        if (existed != null) {
            throw new ResourceExistsException("data_group_post_detail", "Data group post detail already exists for this device and data group post");
        }

        DataGroupPostDetail dataGroupPostDetail = DataGroupPostDetail.builder()
                .device(device)
                .data_group_post(dataGroupPost)
                .is_active(dataGroupPostDetailRequest.getIs_active())
                .build();

        return dataGroupPostDetailRepository.save(dataGroupPostDetail);
    }


    public DataGroupPostDetail findDataGroupPostDetailOrThrow(String device_id, int data_group_id) {

        DataGroupPostDetail dataGroupPostDetail = dataGroupPostDetailRepository.findByDevice_IdAndData_Group_Post_Id(device_id, data_group_id);

        if (dataGroupPostDetail == null) {
            throw new ResourceNotFoundException("data_group_post_detail", "Data group post detail not found for this device and data group post");
        }

        return dataGroupPostDetail;
    }


    @Transactional
    public DataGroupPostDetail updateDataGroupPostDetail(DataGroupPostDetailRequest dataGroupPostDetailRequest) {

        DataGroupPostDetail dataGroupPostDetail = findDataGroupPostDetailOrThrow(dataGroupPostDetailRequest.getDevice_id(), dataGroupPostDetailRequest.getData_group_post_id());

        dataGroupPostDetail.set_active(dataGroupPostDetailRequest.getIs_active());

        return dataGroupPostDetailRepository.save(dataGroupPostDetail);
    }

    public List<DataGroupPostDetail> findAllDetailsByDeviceId(String device_id) {
        return dataGroupPostDetailRepository.findAllDetailsByDevice_Id(device_id);
    }
}
