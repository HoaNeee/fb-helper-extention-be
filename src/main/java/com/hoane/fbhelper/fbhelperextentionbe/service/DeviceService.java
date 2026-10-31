package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.*;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.*;
import com.hoane.fbhelper.fbhelperextentionbe.entity.*;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceSettingStatus;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.DeviceStatus;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.RequestPlatform;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.repository.CommentWalkConfigRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.DeviceRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.DeviceSettingRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.PostConfigRepository;
import com.hoane.fbhelper.fbhelperextentionbe.utils.MyRequestContext;
import com.hoane.fbhelper.fbhelperextentionbe.utils.SecurityContextUtils;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<Device> list = deviceRepository.findAllByUser_Id(userId);

        return list;
    }

    public Boolean isUserExistDevice(String deviceId, String userId) {
        return deviceSettingRepository.findByDevice_IdAndUser_id(deviceId, userId) != null;
    }

    public Integer countAllDeviceActive() {
        return deviceRepository.countByStatus(DeviceStatus.ACTIVE);
    }

    public Integer countAllDeviceInactive() {
        return deviceRepository.countByStatus(DeviceStatus.INACTIVE);
    }

    public BasePageResponse<List<AdminDeviceResponse>> findAllDeviceWithDeviceSetting(Pageable pageable, String keyword, String status) {

        DeviceStatus deviceStatus = status != null && !status.trim().isEmpty() ? DeviceStatus.valueOf(status.trim()) : null;
        keyword = keyword != null && !keyword.trim().isEmpty() ? keyword.trim() : null;

        Page<Device> devicePage = deviceRepository.findAll(pageable, keyword, deviceStatus);
        List<Device> list = devicePage.getContent();

        List<AdminDeviceResponse> adminDeviceResponses = new ArrayList<>();

        for (Device device : list) {
            Integer active = deviceSettingRepository.countByStatusAndDevice_Id(DeviceSettingStatus.ACTIVE, device.getId());
            Integer inactive = deviceSettingRepository.countByStatusAndDevice_Id(DeviceSettingStatus.INACTIVE, device.getId());
            adminDeviceResponses.add(new AdminDeviceResponse(new DeviceResponse(device), active, inactive));
        }

        Pagination pagination = new Pagination(devicePage.getNumber(), devicePage.getSize(), devicePage.getTotalElements(), devicePage.getTotalPages(), devicePage.hasNext());

        return new BasePageResponse<>(adminDeviceResponses, pagination);

    }

    public BasePageResponse<List<DeviceSettingResponse>> findAllDeviceSettingByDeviceIdForAdmin(String deviceId, Pageable pageable, String keyword, String status) {
        keyword = keyword != null && !keyword.trim().isEmpty() ? keyword.trim() : null;
        DeviceSettingStatus deviceSettingStatus = status != null && !status.trim().isEmpty() ? DeviceSettingStatus.valueOf(status.trim()) : null;

        Page<DeviceSetting> deviceSettingPage = deviceSettingRepository.findAllByDevice_Id(deviceId, pageable, keyword, deviceSettingStatus);
        List<DeviceSetting> deviceSettings = deviceSettingPage.getContent();
        List<PostConfig> postConfigs = postConfigRepository.findAllByDevice_Id(deviceId);
        List<CommentWalkConfig> commentWalkConfigs = commentWalkConfigRepository.findAllByDevice_Id(deviceId);

        List<DeviceSettingResponse> res = deviceSettings.stream().map(ds -> {
            PostConfig postConfig = postConfigs.stream().filter(pc -> pc.getDevice().getId().equals(ds.getDevice().getId())).findFirst().orElse(null);
            CommentWalkConfig commentWalkConfig = commentWalkConfigs.stream().filter(cwc -> cwc.getDevice().getId().equals(ds.getDevice().getId())).findFirst().orElse(null);
            DeviceSettingResponse deviceSettingResponse = new DeviceSettingResponse(ds, postConfig, commentWalkConfig);
            deviceSettingResponse.setUser(new UserDetailsResponse(ds.getUser()));
            return deviceSettingResponse;
        }).toList();


        Pagination pagination = new Pagination(deviceSettingPage.getNumber(), deviceSettingPage.getSize(), deviceSettingPage.getTotalElements(), deviceSettingPage.getTotalPages(), deviceSettingPage.hasNext());
        return new BasePageResponse<>(res, pagination);
    }

    public BasePageResponse<List<DeviceResponse>> findAllByUserWithDeviceSetting(String userId, Pageable pageable, String keyword, String status) {
        keyword = keyword != null && !keyword.trim().isEmpty() ? keyword.trim() : null;
        DeviceStatus deviceStatus = status != null && !status.trim().isEmpty() ? DeviceStatus.valueOf(status.trim()) : null;

        Page<Device> devicePage = deviceRepository.findAllByUser_Id(userId, pageable, keyword, deviceStatus);
        List<Device> list = devicePage.getContent();

        List<DeviceSetting> deviceSettingList = deviceSettingRepository.findAllByUser_Id(userId);

        List<DeviceResponse> deviceResponseList = list.stream().map(d -> {
            DeviceResponse deviceResponse = new DeviceResponse(d);

            DeviceSetting existed = deviceSettingList.stream().filter(ds -> ds.getDevice().getId().equals(d.getId())).findFirst().orElse(null);
            if (existed != null) {
                deviceResponse.setDeviceSetting(new DeviceSettingResponse(existed));
            }

            return deviceResponse;
        }).toList();

        Pagination pagination = new Pagination(devicePage.getNumber(), devicePage.getSize(), devicePage.getTotalElements(), devicePage.getTotalPages(), devicePage.hasNext());
        return new BasePageResponse<>(deviceResponseList, pagination);
    }

    public Device findByIdOrThrow(String id) {
        return deviceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("device", "Device with id " + id + " not found")
        );
    }

    @Transactional
    public DeviceSetting getOrCreateDeviceSettingByDeviceIdAndUserId(String deviceId, String userId) {
        DeviceSetting deviceSetting = deviceSettingRepository.findByDevice_IdAndUser_id(deviceId, userId);
        if (deviceSetting == null) {
            deviceSetting = createDeviceSetting(deviceId, userId);
        }

        return deviceSetting;
    }

    @Transactional
    public PostConfig findPostConfigByDeviceIdAndUserId(String deviceId, String userId) {
        PostConfig postConfig = postConfigRepository.findByDevice_IdAndUser_Id(deviceId, userId);
        return postConfig;
    }

    @Transactional
    public PostConfig getOrCreatePostConfigByDeviceIdAndUserId(String deviceId, String userId) {
        PostConfig postConfig = postConfigRepository.findByDevice_IdAndUser_Id(deviceId, userId);
        if (postConfig == null) {
            postConfig = createNewPostConfig(deviceId, userId);
        }
        return postConfig;
    }

    @Transactional
    public CommentWalkConfig findCommentWalkConfigByDeviceIdAndUserId(String deviceId, String userId) {
        CommentWalkConfig commentWalkConfig = commentWalkConfigRepository.findByDevice_IdAndUser_Id(deviceId, userId);
        return commentWalkConfig;
    }

    @Transactional
    public CommentWalkConfig getOrCreateCommentWalkConfigByDeviceIdAndUserId(String deviceId, String userId) {
        CommentWalkConfig commentWalkConfig = commentWalkConfigRepository.findByDevice_IdAndUser_Id(deviceId, userId);
        if (commentWalkConfig == null) {
            commentWalkConfig = createNewCommentWalkConfig(deviceId, userId);
        }
        return commentWalkConfig;
    }

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

        DeviceSetting deviceSetting = getOrCreateDeviceSettingByDeviceIdAndUserId(device_id, user_id);

        PostConfig postConfig = getOrCreatePostConfigByDeviceIdAndUserId(device_id, user_id);

        CommentWalkConfig commentWalkConfig = null;

        if (user.isMember()) {
            commentWalkConfig = getOrCreateCommentWalkConfigByDeviceIdAndUserId(device_id, user_id);
        }

        return new DeviceSettingResponse(deviceSetting, postConfig, commentWalkConfig);
    }


    @Transactional
    public Device createNewDevice(DeviceRequest deviceRequest, String userId) {

        String id = deviceRequest.getId();

        Device existed = deviceRepository.findById(id).orElse(null);

        if (existed != null) {
            return existed;
        }

        Device device = new Device();

        modelMapper.map(deviceRequest, device);

        Device deviceSaved = deviceRepository.save(device);

        if (userId != null) {
            createDeviceSetting(deviceSaved.getId(), userId);
        }

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

        if (deviceSettingRequest.getStatus() != null) {
            DeviceSettingStatus status = deviceSettingRequest.getStatusEnum();
            if (!status.equals(existingSetting.getStatus()) && !user.isAdmin()) {
                throw new AuthorizationDeniedException("You are not allowed to change the status of the device setting");
            }

            existingSetting.setStatus(status);
            deviceSettingRequest.setStatus(null);
        }

        modelMapper.map(deviceSettingRequest, existingSetting);

        if (!canUpdateSetting(deviceSettingRequest, user)) {
            Role role = user.getRole();
            throw new AuthorizationDeniedException("User with role " + role + " is not allowed to update device settings with isRandomBreakBatch or isSpecialFrameHours set to true");
        }

        RequestPlatform requestPlatform = MyRequestContext.getRequestPlatform();
        if (requestPlatform.equals(RequestPlatform.WEB)) {
            existingSetting.setIsChanged(true);
        }

        deviceSettingRepository.save(existingSetting);
    }

    @Transactional
    public PriorityTask updatePriorityTask(String userId, String device_id, PriorityTask priorityTaskRequest) {
        DeviceSetting existingSetting = findDeviceSettingByDeviceIdAndUserIdOrThrow(device_id, userId);

        existingSetting.setPriorityTaskPost(priorityTaskRequest.getPriorityTaskPost());
        existingSetting.setPriorityTaskCommentWalk(priorityTaskRequest.getPriorityTaskCommentWalk());

        deviceSettingRepository.save(existingSetting);
        return priorityTaskRequest;
    }

    @Transactional
    public void updatePostConfig(String userId, PostConfigRequest postConfigRequest) {
        String device_id = postConfigRequest.getDeviceId();
        PostConfig existingPostConfig = findPostConfigByDeviceIdAndUserIdOrThrow(device_id, userId);

        postConfigRequest.setDeviceId(null);

        modelMapper.map(postConfigRequest, existingPostConfig);

        postConfigRepository.save(existingPostConfig);

        this.updateDeviceSettingChangedFromWeb(userId, device_id, true);
    }

    @Transactional
    public void updateCommentWalkConfig(String userId, CommentWalkConfigRequest commentWalkConfigRequest) {
        String device_id = commentWalkConfigRequest.getDeviceId();
        CommentWalkConfig existingCommentWalkConfig = findCommentWalkConfigByDeviceIdAndUserIdOrThrow(device_id, userId);

        commentWalkConfigRequest.setDeviceId(null);

        modelMapper.map(commentWalkConfigRequest, existingCommentWalkConfig);

        commentWalkConfigRepository.save(existingCommentWalkConfig);

        this.updateDeviceSettingChangedFromWeb(userId, device_id, true);

    }

    @Transactional
    public void changeStatusTool(String userId, String deviceId, RequestModels.ChangeStatusTool request) {
        DeviceSetting deviceSetting = findDeviceSettingByDeviceIdAndUserIdOrThrow(deviceId, userId);

        deviceSetting.setIsStopTask(request.isStopTask());

        RequestPlatform requestPlatform = MyRequestContext.getRequestPlatform();
        if (requestPlatform.equals(RequestPlatform.WEB)) {
            deviceSetting.setIsChanged(true);
        }

        deviceSettingRepository.save(deviceSetting);
    }

    //maybe dont need or just for admin
    @Transactional
    public void deleteDevice(String id) {
        //delete this device -> need to do more something, need think this problem

        Device device = findByIdOrThrow(id);
        PostConfig postConfig = postConfigRepository.findByDevice_Id(id);
        CommentWalkConfig commentWalkConfig = commentWalkConfigRepository.findByDevice_Id(id);

        postConfigRepository.delete(postConfig);
        commentWalkConfigRepository.delete(commentWalkConfig);
        deviceRepository.delete(device);
    }

    @Transactional
    public DeviceSettingResponse syncDeviceSetting(String userId, RequestModels.DeviceSyncDataRequest syncDataRequest) {
        String currentDeviceId = syncDataRequest.currentDeviceId();
        String targetDeviceId = syncDataRequest.targetDeviceId();

        DeviceSetting targetDeviceSetting = findDeviceSettingByDeviceIdAndUserIdOrThrow(targetDeviceId, userId);
        DeviceSetting currentDeviceSetting = findDeviceSettingByDeviceIdAndUserIdOrThrow(currentDeviceId, userId);

        if (targetDeviceSetting == null) {
            targetDeviceSetting = new DeviceSetting();
            targetDeviceSetting.setDevice(targetDeviceSetting.getDevice());
            targetDeviceSetting.setUser(targetDeviceSetting.getUser());
        }

        PostConfig postConfig = null;
        CommentWalkConfig commentWalkConfig = null;

        PostConfig currentPostConfig = findPostConfigByDeviceIdAndUserId(currentDeviceId, userId);
        if (currentPostConfig != null) {
            PostConfig targetPostConfig = findPostConfigByDeviceIdAndUserId(targetDeviceId, userId);
            if (targetPostConfig == null) {
                targetPostConfig = new PostConfig();
                targetPostConfig.setDevice(targetDeviceSetting.getDevice());
                targetPostConfig.setUser(targetDeviceSetting.getUser());
            }
            BeanUtils.copyProperties(targetPostConfig, currentPostConfig, "id", "device", "user");
            postConfig = postConfigRepository.save(currentPostConfig);
        }

        CommentWalkConfig currentCommentWalkConfig = findCommentWalkConfigByDeviceIdAndUserId(currentDeviceId, userId);

        if (currentCommentWalkConfig != null) {
            CommentWalkConfig targetCommentWalkConfig = findCommentWalkConfigByDeviceIdAndUserId(targetDeviceId, userId);
            if (targetCommentWalkConfig == null) {
                targetCommentWalkConfig = new CommentWalkConfig();
                targetCommentWalkConfig.setDevice(targetDeviceSetting.getDevice());
                targetCommentWalkConfig.setUser(targetDeviceSetting.getUser());
            }
            BeanUtils.copyProperties(targetCommentWalkConfig, currentCommentWalkConfig, "id", "device", "user");
            commentWalkConfig = commentWalkConfigRepository.save(currentCommentWalkConfig);
        }

        BeanUtils.copyProperties(targetDeviceSetting, currentDeviceSetting, "id", "device", "user");

        RequestPlatform requestPlatform = MyRequestContext.getRequestPlatform();
        if (requestPlatform.equals(RequestPlatform.WEB)) {
            currentDeviceSetting.setIsChanged(true);
        }

        DeviceSetting newDevicesetting = deviceSettingRepository.save(currentDeviceSetting);

        DeviceSettingResponse response = new DeviceSettingResponse(newDevicesetting, postConfig, commentWalkConfig);
        return response;
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

    @Transactional
    public void updateStatusDevice(String id, DeviceStatus status) {
        Device device = findByIdOrThrow(id);
        device.setStatus(status);
        deviceRepository.save(device);
    }

    @Transactional
    public Boolean updateDeviceStatusSetting(RequestModels.UpdateStatusDeviceSettingRequest request) {
        String deviceId = request.deviceId();
        String userId = request.userId();
        DeviceSettingStatus status = DeviceSettingStatus.valueOf(request.status());

        DeviceSetting deviceSetting = findDeviceSettingByDeviceIdAndUserIdOrThrow(deviceId, userId);
        deviceSetting.setStatus(status);
        deviceSettingRepository.save(deviceSetting);

        return true;
    }

    @Transactional
    public Boolean updateDeviceSettingChanged(String userId, String deviceId, Boolean changed) {
        DeviceSetting deviceSetting = getOrCreateDeviceSettingByDeviceIdAndUserId(deviceId, userId);
        if (deviceSetting.getIsRemoteControl()) {
            deviceSetting.setIsChanged(changed);
            deviceSettingRepository.save(deviceSetting);
        }
        return true;
    }

    @Transactional
    public void updateDeviceSettingChangedFromWeb(String userId, String deviceId, Boolean changed) {
        RequestPlatform requestPlatform = MyRequestContext.getRequestPlatform();
        if (requestPlatform.equals(RequestPlatform.WEB)) {
            updateDeviceSettingChanged(userId, deviceId, changed);
        }

    }

    public void updateAllDeviceSettingChangedByUserId(String userId, Boolean changed) {
        deviceSettingRepository.updateAllChangedByUserId(userId, changed);
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

    private boolean isUserActionAllowedOrThrow(String deviceId) {
        String userId = SecurityContextUtils.getUserIdFromContext();
        DeviceSetting deviceSetting = findDeviceSettingByDeviceIdAndUserIdOrThrow(deviceId, userId);
        return isUserActionAllowedOrThrow(deviceSetting);
    }

    private boolean isUserActionAllowedOrThrow(DeviceSetting deviceSetting) {
        if (deviceSetting == null) {
            throw new AuthorizationDeniedException("User is not allowed to perform this action on device with id " + deviceSetting.getDevice().getId());
        }
        return true;
    }
}
