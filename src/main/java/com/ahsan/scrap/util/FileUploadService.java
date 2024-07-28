package com.ahsan.scrap.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	private final String uploadDir = "uploads/user/";

    public String saveFile(MultipartFile file, String userName) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        UUID uuid = UUID.randomUUID();
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = uuid.toString() + "_" + userName + fileExtension;
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return fileName;
        }
        return null;
    }
}
