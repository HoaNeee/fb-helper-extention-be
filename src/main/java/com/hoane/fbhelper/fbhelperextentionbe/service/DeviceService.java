package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DeviceRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DeviceSettingRequest;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.entity.DeviceSetting;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceExistsException;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DeviceRepository;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DeviceSettingRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceSettingRepository deviceSettingRepository;

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

    public DeviceSetting findDeviceSettingByDeviceIdAndUserId(String device_id, String user_id) {
        DeviceSetting deviceSetting = deviceSettingRepository.findByDevice_IdAndUser_id(device_id, user_id);
        User user = userService.findByIdOrThrow(user_id);
        if (deviceSetting == null) {
            deviceSetting = new DeviceSetting();
            Device device = findByIdOrThrow(device_id);
            deviceSetting.setDevice(device);
            deviceSetting.setUser(user);
            deviceSettingRepository.save(deviceSetting);
        }


        return deviceSetting;
    }

    public DeviceSetting findDeviceSettingByDeviceIdAndUserIdOrThrow(String device_id, String user_id) {
        DeviceSetting deviceSetting = findDeviceSettingByDeviceIdAndUserId(device_id, user_id);
        if (deviceSetting == null) {
            throw new ResourceNotFoundException("device", "Device with id " + device_id + " not found");
        }
        return deviceSetting;
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

        DeviceSetting deviceSetting = new DeviceSetting();
        deviceSetting.setDevice(deviceSaved);
        deviceSetting.setUser(userService.findByIdOrThrow(userId));
        deviceSettingRepository.save(deviceSetting);

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

    public void updateDeviceSetting(String userId, DeviceSettingRequest deviceSettingRequest) {
        String device_id = deviceSettingRequest.getDeviceId();
        DeviceSetting existingSetting = findDeviceSettingByDeviceIdAndUserId(device_id, userId);

        User user = userService.findByIdOrThrow(userId);

        deviceSettingRequest.setDeviceId(null);

        modelMapper.map(deviceSettingRequest, existingSetting);

        if (existingSetting.getIsRandomBreakBatch() || existingSetting.getIsSpecialFrameHours()) {
            Role role = user.getRole();
            if (!(role.equals(Role.ROLE_ADMIN) || role.equals(Role.ROLE_MEMBER))) {
                throw new AuthorizationDeniedException("User with role " + role + " is not allowed to update device settings with isRandomBreakBatch or isSpecialFrameHours set to true");
            }
        }

        deviceSettingRepository.save(existingSetting);
    }

    //maybe dont need
    @Transactional
    public void deleteDevice(String id) {
        //delete this device -> need do more something, need think this problem

        Device device = findByIdOrThrow(id);

        deviceRepository.delete(device);
    }

}
