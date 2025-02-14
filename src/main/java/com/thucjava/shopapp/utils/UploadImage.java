package com.thucjava.shopapp.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class UploadImage {
    public static String uploadImage(MultipartFile imageFile){
        String fileName = "";
        if (imageFile != null && imageFile.getSize() > 0) {
            // Đảm bảo thư mục tồn tại trước khi lưu tệp
            String uploadDir = "C:/Users/T/Desktop/images";
            Path uploadPath = Paths.get(uploadDir);
            fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Lấy InputStream từ file và tạo đường dẫn tệp
            try (InputStream inputStream = imageFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);  // Đảm bảo đường dẫn đầy đủ đến tệp
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING); // Lưu tệp
            } catch (IOException e) {
                e.printStackTrace();  // Xử lý lỗi khi lưu tệp
            }
        }
        // end upload image
        return fileName;

    }
}
