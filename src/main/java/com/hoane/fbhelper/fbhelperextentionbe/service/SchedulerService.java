package com.hoane.fbhelper.fbhelperextentionbe.service;


import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.SchedulerRequest;
import com.hoane.fbhelper.fbhelperextentionbe.entity.*;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.SchedulerType;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceExistsException;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ResourceNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DeviceRepository;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.SchedulerDetailRepository;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.SchedulerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SchedulerService {

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SchedulerDetailRepository schedulerDetailRepository;

    @Autowired
    private UserService userService;


    public Scheduler findSchedulerByDeviceIdAndUserId(String deviceId, String userId) {
        return schedulerRepository.findByDevice_IdAndUser_Id(deviceId, userId);
    }

    public Scheduler findSchedulerByDeviceIdOrThrow(String deviceId, String userId) {
        Scheduler scheduler = schedulerRepository.findByDevice_IdAndUser_Id(deviceId, userId);
        if (scheduler == null) {
            throw new ResourceNotFoundException("scheduler", "scheduler not found");
        }

        return scheduler;
    }

    public Scheduler getSchedulerByDeviceIdAndUserId(String deviceId, String userId) {
        Scheduler scheduler = schedulerRepository.findByDevice_IdAndUser_Id(deviceId, userId);


        if (scheduler == null) {
            Device device = deviceService.findByIdOrThrow(deviceId);
            User u = userService.findByIdOrThrow(userId);

            scheduler = Scheduler.builder()
                    .schedulerType(SchedulerType.DAILY_HOURS)
                    .device(device)
                    .user(u)
                    .build();
            schedulerRepository.save(scheduler);
        }

        return scheduler;
    }

    public SchedulerDetail findSchedulerDetailBySchedulerIdAndType(int scheduler_id, SchedulerType scheduler_type) {
        SchedulerDetail schedulerDetail = schedulerDetailRepository.findByScheduler_IdAndSchedulerType(scheduler_id, scheduler_type);

        return schedulerDetail;
    }

    public SchedulerDetail findSchedulerDetailBySchedulerIdAndSchedulerType(int scheduler_id, SchedulerType scheduler_type) {
        return schedulerDetailRepository.findByScheduler_IdAndSchedulerType(scheduler_id, scheduler_type);
    }

    public List<SchedulerDetail> findAllSchedulerDetailsBySchedulerId(int scheduler_id) {
        return schedulerDetailRepository.findAllByScheduler_Id(scheduler_id);
    }

    @Transactional
    public Scheduler createNewScheduler(SchedulerRequest schedulerRequest, String userId) {

        String device_id = schedulerRequest.getDeviceId();

        Scheduler existed = findSchedulerByDeviceIdAndUserId(device_id, userId);

        if (existed != null) {
            throw new ResourceExistsException("scheduler", "scheduler with device id: " + device_id + " already exists");
        }

        Device device = deviceService.findByIdOrThrow(device_id);

        Scheduler scheduler = new Scheduler();

        scheduler.setDevice(device);

        schedulerRepository.save(scheduler);

        return scheduler;
    }

    @Transactional
    public SchedulerDetail updateScheduler(String device_id, String userId, SchedulerRequest schedulerRequest) {

        SchedulerType type = schedulerRequest.getSchedulerType();

//        if (type.equals(SchedulerType.DAILY_HOURS)) {
//            throw new RuntimeException("Cannot update scheduler with type DAILY_HOURS");
//        }

        Scheduler scheduler = findSchedulerByDeviceIdOrThrow(device_id, userId);
        scheduler.setSchedulerType(type);

        Integer scheduler_time_value = schedulerRequest.getSchedulerTimeValue();

        SchedulerDetail existedSchedulerDetail = findSchedulerDetailBySchedulerIdAndSchedulerType(scheduler.getId(), type);
        if (existedSchedulerDetail == null) {
            existedSchedulerDetail = SchedulerDetail.builder()
                    .schedulerTimeValue(scheduler_time_value)
                    .scheduler(scheduler)
                    .schedulerType(type)
                    .schedulerTimeList(schedulerRequest.getSchedulerTimeList())
                    .build();
        } else {
            existedSchedulerDetail.setSchedulerTimeValue(scheduler_time_value);
            existedSchedulerDetail.setSchedulerType(type);
            existedSchedulerDetail.setSchedulerTimeList(schedulerRequest.getSchedulerTimeList());
        }
        schedulerDetailRepository.save(existedSchedulerDetail);

        return existedSchedulerDetail;
    }

    @Transactional
    public SchedulerDetail changeTypeScheduler(String device_id, String userId, SchedulerType schedulerType) {
        Scheduler scheduler = findSchedulerByDeviceIdOrThrow(device_id, userId);
        scheduler.setSchedulerType(schedulerType);
        schedulerRepository.save(scheduler);

        SchedulerDetail schedulerDetail = schedulerDetailRepository.findByScheduler_IdAndSchedulerType(scheduler.getId(), schedulerType);

        if (schedulerDetail == null) {
            schedulerDetail = SchedulerDetail.builder()
                    .schedulerTimeValue(getDefaultTimeValue(schedulerType))
                    .scheduler(scheduler)
                    .schedulerType(schedulerType)
                    .build();
            schedulerDetailRepository.save(schedulerDetail);
        }

        return schedulerDetail;
    }

    private int getDefaultTimeValue(SchedulerType schedulerType) {
        if (schedulerType.equals(SchedulerType.EVERY_MINUTES) || schedulerType.equals(SchedulerType.CUSTOM_DAILY_MINUTES))
            return Constant.TIME_DEFAULT_SCHEDULER_MINUTES;
        if (schedulerType.equals(SchedulerType.EVERY_HOURS) || schedulerType.equals(SchedulerType.CUSTOM_DAILY_HOURS))
            return Constant.TIME_DEFAULT_SCHEDULER_HOURS;
        return 0;
    }

}
