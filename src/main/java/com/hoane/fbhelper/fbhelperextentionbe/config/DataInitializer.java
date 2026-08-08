package com.hoane.fbhelper.fbhelperextentionbe.config;

import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.entity.*;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.UserStatus;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    private DataGroupPostDetailRepository dataGroupPostDetailRepository;

    @Autowired
    private SchedulerRepository schedulerRepository;

    @Autowired
    private CommentPostRepository commentPostRepository;

    @Autowired
    private InteractBeforePostRepository interactBeforePostRepository;

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
                .files(java.util.List.of("https://picsum.photos/200/300", "https://picsum.photos/300/200"))
                .contents(java.util.List.of("Content 1", "Content 2"))
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

        DataGroupPostDetail dataGroupPostDetail = DataGroupPostDetail.builder()
                .device(device)
                .dataGroupPost(dataGroupPost)
                .isActive(true)
                .build();

        dataGroupPostDetailRepository.save(dataGroupPostDetail);

        DeviceSetting deviceSetting = new DeviceSetting();
        deviceSetting.setDevice(device);
        deviceSetting.setUser(u);
        deviceSettingRepository.save(deviceSetting);


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
    }
}
