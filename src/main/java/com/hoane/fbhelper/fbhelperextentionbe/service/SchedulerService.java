package com.hoane.fbhelper.fbhelperextentionbe.service;


import com.hoane.fbhelper.fbhelperextentionbe.entity.Scheduler;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.SchedulerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    @Autowired
    private SchedulerRepository schedulerRepository;


    public Scheduler create() {

        return new Scheduler();
    }

}
