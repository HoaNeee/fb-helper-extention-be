package com.hoane.fbhelper.fbhelperextentionbe.service;


import com.hoane.fbhelper.fbhelperextentionbe.constant.Constant;
import com.hoane.fbhelper.fbhelperextentionbe.utils.IdGenerator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final Path rootLocation = Paths.get(Constant.PATH_UPLOAD_DIR);

    public FileService() {
        try {
            Files.createDirectories(rootLocation);
        } catch (Exception e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String storeFileHelper(MultipartFile file, HttpServletRequest request) throws IOException {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("File trống, không thể lưu trữ!");
            }

            String fileName = file.getOriginalFilename();

            String extension = getFileExtension(fileName);

            if (!fileName.isEmpty()) {
                if (fileName.contains(".")) {
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));
                    fileName = fileName.replaceAll("[-_.]", "");
                    if (fileName.length() > Constant.MAX_LENGTH_FILE_NAME) {
                        fileName = fileName.substring(0, Constant.MAX_LENGTH_FILE_NAME);
                    }
                } else if (fileName.length() > Constant.MAX_LENGTH_FILE_NAME) {
                    fileName = fileName.replaceAll("[-_.]", "");
                    fileName = fileName.substring(0, Constant.MAX_LENGTH_FILE_NAME);
                }
            }


            fileName = fileName + "_" + IdGenerator.generateId(10) + extension;


            // 2. Lưu file vật lý vào thư mục 'uploads'
            Path destinationFile = this.rootLocation.resolve(Paths.get(fileName))
                    .normalize().toAbsolutePath();

            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            // 3. TỰ ĐỘNG TẠO HOST LINK ĐỘNG (Bản chất nằm ở đây)
            // Nếu chạy local, nó sẽ tự ra: http://localhost:8080/uploads/abc-xyz.jpg
            // Nếu lên server thật, nó tự đổi thành: https://domain.com/uploads/abc-xyz.jpg
            String hostLink = ServletUriComponentsBuilder.fromContextPath(request)
                    .path("/uploads/")
                    .path(fileName)
                    .toUriString();

            return hostLink;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi trong quá trình lưu file: " + e.getMessage());
        }
    }

    public String storeFile(MultipartFile file, HttpServletRequest request) {
        try {
            return storeFileHelper(file, request);
        } catch (Exception e) {
            throw new RuntimeException("Could not store file: " + e.getMessage());
        }

    }

    public List<String> storeFile(List<MultipartFile> file, HttpServletRequest request) {
        try {
            List<String> fileLinks = new ArrayList<>();
            for (MultipartFile fileItem : file) {
                String path = storeFileHelper(fileItem, request);
                fileLinks.add(path);
            }

            return fileLinks;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi trong quá trình lưu file: " + e.getMessage());
        }
    }

    // Hàm tiện ích lấy đuôi file (.jpg, .png, .pdf...)
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "jpg";
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
