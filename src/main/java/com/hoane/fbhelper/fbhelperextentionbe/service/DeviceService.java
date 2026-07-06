package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.DeviceRequest;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceExistsException;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DeviceRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public Device create(DeviceRequest deviceRequest, int user_id) {

        User user = userService.findByIdOrThrow(user_id);

        String id = deviceRequest.getId();

        Device existed = deviceRepository.findById(id).orElse(null);

        if (existed != null) {
            throw new ResourceExistsException("device", "Device with id " + id + " already exists");
        }

        Device device = Device.builder()
                .id(deviceRequest.getId())
                .device_name(deviceRequest.getDevice_name())
                .device_type(deviceRequest.getDevice_type())
                .is_online(deviceRequest.getIs_online())
                .is_active(deviceRequest.getIs_active())
                .user(user)
                .build();

        return deviceRepository.save(device);
    }

    public List<Device> findAllByUser(int userId) {
        return deviceRepository.findAllByUser_Id(userId);
    }

    public Device findByIdOrThrow(String id) {
        return deviceRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("device", "Device with id " + id + " not found")
        );
    }

    @Transactional
    public Device update(String id, DeviceRequest deviceRequest) {

        Device device = findByIdOrThrow(id);

        modelMapper.map(deviceRequest, device);

        return deviceRepository.save(device);
    }

    @Transactional
    public void delete(String id) {
        //delete this device -> need do more something, need think this problem

        Device device = findByIdOrThrow(id);

        deviceRepository.delete(device);
    }

}
