package com.hoane.fbhelper.fbhelperextentionbe.config;

import com.hoane.fbhelper.fbhelperextentionbe.entity.DataGroupPost;
import com.hoane.fbhelper.fbhelperextentionbe.entity.Device;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DataGroupPostRepository;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.DeviceRepository;
import com.hoane.fbhelper.fbhelperextentionbe.reporitory.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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

    @Override
    public void run(String... args) throws Exception {
        User u = User.builder()
                .username("admin")
                .email("admin@gmail.com")
                .build();

        String password = "admin";
        String hash = passwordEncoder.encode(password);
        u.setPassword(hash);

        userRepository.save(u);

        DataGroupPost dataGroupPost = DataGroupPost.builder()
                .title_match("Cau giay")
                .name("79 cau giay")
                .images(java.util.List.of("https://i.imgur.com/1.jpg", "https://i.imgur.com/2.jpg"))
                .contents(java.util.List.of("Content 1", "Content 2"))
                .from_member(0)
                .to_member(100)
                .user(u)
                .build();

        dataGroupPostRepository.save(dataGroupPost);


        Device device = Device.builder()
                .id("abcxyz12")
                .device_name("Hoa chrome device 1")
                .device_type("chrome")
                .user(u)
                .build();

        deviceRepository.save(device);

    }
}
