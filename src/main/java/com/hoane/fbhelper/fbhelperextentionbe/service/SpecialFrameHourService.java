package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.SpecialFrameHourRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.SpecialFrameHourResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SpecialFrameHour;
import com.hoane.fbhelper.fbhelperextentionbe.entity.SpecialFrameHourSetting;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.repository.SpecialFrameHourRepository;
import com.hoane.fbhelper.fbhelperextentionbe.repository.SpecialFrameHourSettingRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialFrameHourService {
    @Autowired
    private SpecialFrameHourRepository specialFrameHourRepository;

    @Autowired
    private SpecialFrameHourSettingRepository specialFrameHourSettingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ModelMapper modelMapper;

    public List<SpecialFrameHour> findAllSpecialFrameHoursByUserId(String user_id) {
        return specialFrameHourRepository.findAllByUser_Id(user_id);
    }

    public List<SpecialFrameHourSetting> findAllSpecialFrameHourSettingsByDeviceId(String device_id) {
        return specialFrameHourSettingRepository.findAllByDevice_Id(device_id);
    }

    public SpecialFrameHour findSpecialFrameHourByIdOrThrow(int special_frame_hour_id) {
        return specialFrameHourRepository.findById(special_frame_hour_id)
                .orElseThrow(() -> new RuntimeException("Special frame hour not found with id: " + special_frame_hour_id));
    }

    public List<SpecialFrameHourResponse> findAllSpecialFrameHoursByDeviceIdAndUserId(String user_id, String device_id) {
        List<SpecialFrameHour> specialFrameHours = findAllSpecialFrameHoursByUserId(user_id);
        List<SpecialFrameHourSetting> specialFrameHourSettings = findAllSpecialFrameHourSettingsByDeviceId(device_id);

        List<SpecialFrameHourResponse> frameHourResponses = specialFrameHours.stream()
                .map(specialFrameHour -> {
                    SpecialFrameHourSetting setting = null;
                    for (SpecialFrameHourSetting s : specialFrameHourSettings) {
                        if (s.getSpecialFrameHour().getId() == specialFrameHour.getId()) {
                            setting = s;
                            break;
                        }
                    }
                    if (setting == null) {
                        return new SpecialFrameHourResponse(specialFrameHour);
                    }
                    return new SpecialFrameHourResponse(specialFrameHour, setting);

                }).toList();

        return frameHourResponses;

    }

    @Transactional
    public SpecialFrameHour createSpecialFrameHour(String user_id, SpecialFrameHourRequest.SpecialRequest specialFrameHourRequest) {
        User user = userService.findByIdOrThrow(user_id);

        SpecialFrameHour specialFrameHour = SpecialFrameHour.builder()
                .user(user)
                .startTime(specialFrameHourRequest.getStartTime())
                .endTime(specialFrameHourRequest.getEndTime())
                .maxGroup(specialFrameHourRequest.getMaxGroup())
                .applyDates(specialFrameHourRequest.getApplyDates())
                .build();
        specialFrameHourRepository.save(specialFrameHour);

        String device_id = specialFrameHourRequest.getDeviceId();

        if (device_id != null) {
            Device device = deviceService.findByIdOrThrow(device_id);

            SpecialFrameHourSetting specialFrameHourSetting = new SpecialFrameHourSetting();
            specialFrameHourSetting.setSpecialFrameHour(specialFrameHour);
            specialFrameHourSetting.setDevice(device);
            specialFrameHourSetting.setIsActive(true);

            specialFrameHourSettingRepository.save(specialFrameHourSetting);
        }

        return specialFrameHour;
    }

    @Transactional
    public SpecialFrameHour updateSpecialFrameHour(int special_frame_hour_id, SpecialFrameHourRequest.SpecialRequest specialFrameHourRequest) {
        SpecialFrameHour specialFrameHour = findSpecialFrameHourByIdOrThrow(special_frame_hour_id);

        modelMapper.map(specialFrameHourRequest, specialFrameHour);
        specialFrameHourRepository.save(specialFrameHour);

        return specialFrameHour;
    }

    @Transactional
    public void updateStatusSpecialFrameHour(int special_frame_hour_id, SpecialFrameHourRequest.SpecialFrameSettingRequest specialFrameSettingRequest) {

        String device_id = specialFrameSettingRequest.getDeviceId();

        SpecialFrameHourSetting specialFrameHourSetting = specialFrameHourSettingRepository.findByDevice_IdAndSpecialFrameHour_Id(device_id, special_frame_hour_id);

        if (specialFrameHourSetting == null) {
            SpecialFrameHour specialFrameHour = findSpecialFrameHourByIdOrThrow(special_frame_hour_id);
            Device device = deviceService.findByIdOrThrow(device_id);

            specialFrameHourSetting = new SpecialFrameHourSetting();
            specialFrameHourSetting.setSpecialFrameHour(specialFrameHour);
            specialFrameHourSetting.setDevice(device);
            specialFrameHourSetting.setIsActive(specialFrameSettingRequest.getIsActive());
            specialFrameHourSettingRepository.save(specialFrameHourSetting);
        } else {
            specialFrameHourSetting.setIsActive(specialFrameSettingRequest.getIsActive());
            specialFrameHourSettingRepository.save(specialFrameHourSetting);
        }

    }

    @Transactional
    public void deleteSpecialFrameHour(int special_frame_hour_id) {
        specialFrameHourSettingRepository.deleteAllBySpecialFrameHour_Id(special_frame_hour_id);
        specialFrameHourRepository.deleteById(special_frame_hour_id);
    }

    @Transactional
    public void clearAllSpecialFrameHour(String userId) {
        specialFrameHourSettingRepository.deleteAllByUser_Id(userId);
        specialFrameHourRepository.deleteAllByUser_Id(userId);
    }

}
