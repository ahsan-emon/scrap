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
	private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file, String userName) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        UUID uuid = UUID.randomUUID();
        String fileName = uuid+"_"+userName+"_"+file.getOriginalFilename();
//        String fileName = file.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        System.out.println("fileName====>>>"+fileName);
        return fileName;
    }
}
