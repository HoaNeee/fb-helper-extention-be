package com.hoane.fbhelper.fbhelperextentionbe.config;

import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.entity.*;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.UserStatus;
import com.hoane.fbhelper.fbhelperextentionbe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DataGroupPostRepository dataGroupPostRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DeviceSettingRepository deviceSettingRepository;

    @Autowired
    private PostConfigRepository postConfigRepository;

    @Autowired
    private CommentWalkConfigRepository commentWalkConfigRepository;

    @Autowired
    private DataGroupPostDetailRepository dataGroupPostDetailRepository;

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private CommentPostRepository commentPostRepository;

    @Autowired
    private InteractBeforePostRepository interactBeforePostRepository;

    @Autowired
    private SpecialFrameHourRepository specialFrameHourRepository;

    @Autowired
    private SpecialFrameHourSettingRepository specialFrameHourSettingRepository;

    @Autowired
    private CommentWalkRepository commentWalkRepository;

    @Autowired
    private CommentWalkDetailRepository commentWalkDetailRepository;

    private final Path rootLocation = Paths.get(Constant.PATH_UPLOAD_DIR);


    @Override
    public void run(String... args) throws Exception {

        try {
            FileSystemUtils.deleteRecursively(rootLocation.toFile());
            Files.createDirectory(rootLocation);
        } catch (Exception e) {
            System.out.println("Could not delete uploads directory: " + e.getMessage());
        }

        String userId = Constant.USER_ID;
        String deviceId = Constant.DEVICE_ID;
        String dataGroupPostId = Constant.DATA_GROUP_POST_ID;

        User u = User.builder()
                .id(userId)
                .username("admin")
                .email("admin@gmail.com")
                .role(Role.ROLE_ADMIN)
                .status(UserStatus.ACTIVE)
                .build();

        User u2 = User.builder()
                .id("xyz1234567")
                .username("user1")
                .email("user@gmail.com")
                .build();

        User u3 = User.builder()
                .id("xyz1234568")
                .username("user2")
                .email("user2@gmail.com")
                .status(UserStatus.LOCKED)
                .build();

        User u4 = User.builder()
                .id("xyz1234569")
                .username("user3")
                .email("user3@gmail.com")
                .status(UserStatus.ACTIVE)
                .isNewUser(true)
                .build();

        String password = "admin";
        String hash = passwordEncoder.encode(password);
        u.setPassword(hash);

        String pwd2 = "user1";
        String hash2 = passwordEncoder.encode(pwd2);
        u2.setPassword(hash2);

        String pwd3 = "user2";
        String hash3 = passwordEncoder.encode(pwd3);
        u3.setPassword(hash3);

        String pwd4 = "user3";
        String hash4 = passwordEncoder.encode(pwd4);
        u4.setPassword(hash4);

        userRepository.save(u);
        userRepository.save(u2);
        userRepository.save(u3);
        userRepository.save(u4);

        DataGroupPost dataGroupPost = DataGroupPost.builder()
                .title("Cau giay")
                .name("79 cau giay")
                .files(List.of("https://picsum.photos/200/300", "https://picsum.photos/300/200"))
                .contents(List.of("Content 1", "Content 2"))
                .fromMember(0)
                .toMember(100)
                .user(u)
                .id(dataGroupPostId)
                .priority(1)
                .build();

        dataGroupPostRepository.save(dataGroupPost);

        Device device = Device.builder()
                .id(deviceId)
                .deviceName("Hoa chrome device 1")
                .deviceType("chrome")
                .build();

        deviceRepository.save(device);

        Device device1 = Device.builder()
                .id("xyz12348")
                .deviceName("Hoa chrome device 2")
                .deviceType("chrome")
                .build();

        deviceRepository.save(device1);

        DataGroupPostDetail dataGroupPostDetail = DataGroupPostDetail.builder()
                .device(device)
                .dataGroupPost(dataGroupPost)
                .isActive(true)
                .build();

        dataGroupPostDetailRepository.save(dataGroupPostDetail);

        DeviceSetting deviceSetting = new DeviceSetting();
        deviceSetting.setDevice(device);
        deviceSetting.setUser(u);
        deviceSetting.setIsSpecialFrameHours(true);
        deviceSettingRepository.save(deviceSetting);

        DeviceSetting deviceSetting1 = new DeviceSetting();
        deviceSetting1.setDevice(device1);
        deviceSetting1.setUser(u);
        deviceSettingRepository.save(deviceSetting1);

        PostConfig postConfig = new PostConfig();
        postConfig.setDevice(device);
        postConfig.setUser(u);
        postConfigRepository.save(postConfig);

        CommentWalkConfig commentWalkConfig = new CommentWalkConfig();
        commentWalkConfig.setDevice(device);
        commentWalkConfig.setUser(u);
        commentWalkConfigRepository.save(commentWalkConfig);

        Scheduler scheduler = Scheduler.builder()
                .device(device)
                .user(u)
                .build();

        schedulerRepository.save(scheduler);

        CommentPost commentPost = CommentPost.builder()
                .user(u)
                .maxCommentPerPost(Constant.MAX_COMMENT_PER_POST)
                .build();

        commentPostRepository.save(commentPost);

        InteractBeforePost interactBeforePost = InteractBeforePost.builder()
                .user(u)
                .maxPostInteractPerBatch(Constant.MAX_POST_INTERACT_PER_BATCH)
                .build();
        interactBeforePostRepository.save(interactBeforePost);

        SpecialFrameHour specialFrameHour = SpecialFrameHour.builder()
                .user(u)
                .startTime(2).endTime(4).maxGroup(8)
                .applyDates(List.of(1, 2, 3, 4))
                .build();

        SpecialFrameHourSetting specialFrameHourSetting = new SpecialFrameHourSetting();
        specialFrameHourSetting.setDevice(device);
        specialFrameHourSetting.setSpecialFrameHour(specialFrameHour);
        specialFrameHourSetting.setIsActive(true);

        specialFrameHourRepository.save(specialFrameHour);

        specialFrameHourSettingRepository.save(specialFrameHourSetting);

        CommentWalk commentWalk = CommentWalk.builder()
                .titleQuerySearchs(List.of("query1", "query2"))
                .user(u)
                .keywordsCertainChoice(List.of("keyword1", "keyword2"))
                .keywordQueryIncludes(List.of("include1", "include2"))
                .keywordQueryExcludes(List.of("exclude1", "exclude2"))
                .contents(List.of("content1", "content2"))
                .files(List.of("https://picsum.photos/200/300"))
                .matchRateValueContentQueryIncludes(2)
                .name("Comment Walk 1")
                .id(Constant.COMMENT_WALK_ID)
                .build();

        commentWalkRepository.save(commentWalk);

        CommentWalkDetail commentWalkDetail = CommentWalkDetail.builder()
                .commentWalk(commentWalk)
                .device(device)
                .isActive(true)
                .build();

        commentWalkDetailRepository.save(commentWalkDetail);


    }
}
