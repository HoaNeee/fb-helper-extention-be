package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.*;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.DeviceSettingResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.*;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.repository.CommentWalkConfigRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.DeviceRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.DeviceSettingRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.PostConfigRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceSettingRepository deviceSettingRepository;

    @Autowired
    private PostConfigRepository postConfigRepository;

    @Autowired
    private CommentWalkConfigRepository commentWalkConfigRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    public List<Device> findAllByUser(String userId) {
        return deviceRepository.findAllByUser_Id(userId);
    }

    public Device findByIdOrThrow(String id) {
        return deviceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("device", "Device with id " + id + " not found")
        );
    }

    @Transactional
    public DeviceSetting findDeviceSettingByDeviceIdAndUserId(String deviceId, String userId) {
        DeviceSetting deviceSetting = deviceSettingRepository.findByDevice_IdAndUser_id(deviceId, userId);
        if (deviceSetting == null) {
            deviceSetting = createDeviceSetting(deviceId, userId);
        }

        return deviceSetting;
    }

    @Transactional
    public PostConfig findPostConfigByDeviceIdAndUserId(String deviceId, String userId) {
        PostConfig postConfig = postConfigRepository.findByDevice_IdAndUser_Id(deviceId, userId);
        if (postConfig == null) {
            postConfig = createNewPostConfig(deviceId, userId);
        }
        return postConfig;
    }

    @Transactional
    public CommentWalkConfig findCommentWalkConfigByDeviceIdAndUserId(String deviceId, String userId) {
        CommentWalkConfig commentWalkConfig = commentWalkConfigRepository.findByDevice_IdAndUser_Id(deviceId, userId);
        if (commentWalkConfig == null) {
            commentWalkConfig = createNewCommentWalkConfig(deviceId, userId);
        }
        return commentWalkConfig;
    }

    @Transactional
    public DeviceSetting findDeviceSettingByDeviceIdAndUserIdOrThrow(String device_id, String user_id) {
        DeviceSetting deviceSetting = deviceSettingRepository.findByDevice_IdAndUser_id(device_id, user_id);
        if (deviceSetting == null) {
            throw new ResourceNotFoundException("device", "Device with id " + device_id + " not found");
        }
        return deviceSetting;
    }

    public CommentWalkConfig findCommentWalkConfigByDeviceIdAndUserIdOrThrow(String deviceId, String userId) {
        CommentWalkConfig commentWalkConfig = commentWalkConfigRepository.findByDevice_IdAndUser_Id(deviceId, userId);
        if (commentWalkConfig == null) {
            throw new ResourceNotFoundException("commentWalkConfig", "CommentWalkConfig with deviceId " + deviceId + " and userId " + userId + " not found");
        }
        return commentWalkConfig;
    }


    public PostConfig findPostConfigByDeviceIdAndUserIdOrThrow(String deviceId, String userId) {
        PostConfig postConfig = postConfigRepository.findByDevice_IdAndUser_Id(deviceId, userId);
        if (postConfig == null) {
            throw new ResourceNotFoundException("postConfig", "PostConfig with deviceId " + deviceId + " and userId " + userId + " not found");
        }
        return postConfig;
    }

    @Transactional
    public DeviceSettingResponse getAllDataDeviceSetting(String device_id, String user_id) {

        User user = userService.findByIdOrThrow(user_id);

        DeviceSetting deviceSetting = findDeviceSettingByDeviceIdAndUserId(device_id, user_id);

        PostConfig postConfig = findPostConfigByDeviceIdAndUserId(device_id, user_id);

        CommentWalkConfig commentWalkConfig = null;

        if (user.isMember()) {
            commentWalkConfig = findCommentWalkConfigByDeviceIdAndUserId(device_id, user_id);
        }

        return new DeviceSettingResponse(deviceSetting, postConfig, commentWalkConfig);
    }


    @Transactional
    public Device createNewDevice(String userId, DeviceRequest deviceRequest) {

        String id = deviceRequest.getId();

        Device existed = deviceRepository.findById(id).orElse(null);

        if (existed != null) {
            return existed;
        }

        Device device = new Device();

        modelMapper.map(deviceRequest, device);

        Device deviceSaved = deviceRepository.save(device);

        createDeviceSetting(deviceSaved.getId(), userId);

        return deviceSaved;
    }

    public DeviceSetting createDeviceSetting(DeviceSettingRequest request, String userId) {
        DeviceSetting deviceSetting = DeviceSetting.builder()
                .device(findByIdOrThrow(request.getDeviceId()))
                .user(userService.findByIdOrThrow(userId))
                .build();
        return deviceSettingRepository.save(deviceSetting);
    }

    @Transactional
    public Device updateDevice(String id, DeviceRequest deviceRequest) {

        Device device = findByIdOrThrow(id);

        modelMapper.map(deviceRequest, device);

        return deviceRepository.save(device);
    }

    @Transactional
    public void updateDeviceSetting(String userId, DeviceSettingRequest deviceSettingRequest) {
        String device_id = deviceSettingRequest.getDeviceId();
        DeviceSetting existingSetting = findDeviceSettingByDeviceIdAndUserIdOrThrow(device_id, userId);

        User user = userService.findByIdOrThrow(userId);

        deviceSettingRequest.setDeviceId(null);

        modelMapper.map(deviceSettingRequest, existingSetting);

        if (!canUpdateSetting(deviceSettingRequest, user)) {
            Role role = user.getRole();
            throw new AuthorizationDeniedException("User with role " + role + " is not allowed to update device settings with isRandomBreakBatch or isSpecialFrameHours set to true");
        }

        deviceSettingRepository.save(existingSetting);
    }

    @Transactional
    public void updatePostConfig(String userId, PostConfigRequest postConfigRequest) {
        String device_id = postConfigRequest.getDeviceId();
        PostConfig existingPostConfig = findPostConfigByDeviceIdAndUserIdOrThrow(device_id, userId);

        postConfigRequest.setDeviceId(null);

        modelMapper.map(postConfigRequest, existingPostConfig);

        postConfigRepository.save(existingPostConfig);
    }

    @Transactional
    public void updateCommentWalkConfig(String userId, CommentWalkConfigRequest commentWalkConfigRequest) {
        String device_id = commentWalkConfigRequest.getDeviceId();
        CommentWalkConfig existingCommentWalkConfig = findCommentWalkConfigByDeviceIdAndUserIdOrThrow(device_id, userId);

        commentWalkConfigRequest.setDeviceId(null);

        modelMapper.map(commentWalkConfigRequest, existingCommentWalkConfig);

        commentWalkConfigRepository.save(existingCommentWalkConfig);
    }

    @Transactional
    public void changeStatusTool(String userId, String deviceId, RequestModels.ChangeStatusTool request) {
        DeviceSetting deviceSetting = findDeviceSettingByDeviceIdAndUserIdOrThrow(deviceId, userId);
        deviceSetting.setIsStopTask(request.isStopTask());
        deviceSettingRepository.save(deviceSetting);
    }

    //maybe dont need
    @Transactional
    public void deleteDevice(String id) {
        //delete this device -> need do more something, need think this problem

        Device device = findByIdOrThrow(id);

        deviceRepository.delete(device);
    }

    @Transactional
    public DeviceSetting syncDeviceSetting(String userId, RequestModels.DeviceSyncDataRequest syncDataRequest) {
        String currentDeviceId = syncDataRequest.currentDeviceId();
        String targetDeviceId = syncDataRequest.targetDeviceId();

        DeviceSetting targetDeviceSetting = findDeviceSettingByDeviceIdAndUserIdOrThrow(targetDeviceId, userId);
        DeviceSetting currentDeviceSetting = findDeviceSettingByDeviceIdAndUserIdOrThrow(currentDeviceId, userId);

        BeanUtils.copyProperties(targetDeviceSetting, currentDeviceSetting, "id", "device", "user");

        return deviceSettingRepository.save(currentDeviceSetting);
    }


    @Transactional
    public DeviceSetting createDeviceSetting(String deviceId, String userId) {
        DeviceSetting existed = deviceSettingRepository.findByDevice_IdAndUser_id(deviceId, userId);
        if (existed != null) {
            return existed;
        }

        User u = userService.findByIdOrThrow(userId);
        Device device = findByIdOrThrow(deviceId);

        DeviceSetting deviceSetting = new DeviceSetting();
        deviceSetting.setDevice(device);
        deviceSetting.setUser(u);

        deviceSettingRepository.save(deviceSetting);
        return deviceSetting;
    }


    @Transactional
    public PostConfig createNewPostConfig(String deviceId, String userId) {
        PostConfig existed = postConfigRepository.findByDevice_IdAndUser_Id(userId, deviceId);
        if (existed != null) {
            return existed;
        }

        User u = userService.findByIdOrThrow(userId);
        Device device = findByIdOrThrow(deviceId);

        PostConfig postConfig = new PostConfig();

        postConfig.setDevice(device);
        postConfig.setUser(u);
        return postConfigRepository.save(postConfig);
    }

    @Transactional
    public CommentWalkConfig createNewCommentWalkConfig(String deviceId, String userId) {
        CommentWalkConfig existed = commentWalkConfigRepository.findByDevice_IdAndUser_Id(deviceId, userId);
        if (existed != null) {
            return existed;
        }

        User u = userService.findByIdOrThrow(userId);
        Device device = findByIdOrThrow(deviceId);

        CommentWalkConfig commentWalkConfig = new CommentWalkConfig();


        commentWalkConfig.setDevice(device);
        commentWalkConfig.setUser(u);
        return commentWalkConfigRepository.save(commentWalkConfig);
    }

    private boolean canUpdateSetting(DeviceSettingRequest request, User user) {
        if (!user.isMember()) {
            if (request.getIsRandomBreakBatch() != null && request.getIsRandomBreakBatch()) {
                return false;
            }
            if (request.getIsSpecialFrameHours() != null && request.getIsSpecialFrameHours()) {
                return false;
            }
            if (request.getIsFixStealAllFocus() != null && request.getIsFixStealAllFocus()) {
                return false;
            }
        }
        return true;
    }
}
