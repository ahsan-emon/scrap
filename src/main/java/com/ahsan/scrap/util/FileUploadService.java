package com.ahsan.scrap.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
	private final String uploadDir = "uploads/user/";
	private final String uploadSellDir = "uploads/sell/";

    public String saveFile(MultipartFile file, String userName, String previousFileName) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        
        // Delete the previous file if it exists
        if (previousFileName != null && !previousFileName.isEmpty()) {
            deleteFile(previousFileName);
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
    public String saveSellFile(MultipartFile file, String previousFileName) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        // Delete the previous file if it exists
        if (previousFileName != null && !previousFileName.isEmpty()) {
        	deleteSellFile(previousFileName);
        }

        UUID uuid = UUID.randomUUID();
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            LocalDate currentDate = LocalDate.now();
            String stringDateFormat = DateUtils.stringDateFormat(currentDate);
            String fileName = stringDateFormat +  "_" + uuid.toString() + fileExtension;
            Path path = Paths.get(uploadSellDir + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());
            return fileName;
        }

        return null;
    }
    
    public boolean deleteFile(String fileName) {
        try {
            Path path = Paths.get(uploadDir + fileName);
            return Files.deleteIfExists(path); // Delete the file if it exists
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception or handle it appropriately
            return false;
        }
    }
    public boolean deleteSellFile(String fileName) {
    	try {
    		Path path = Paths.get(uploadSellDir + fileName);
    		return Files.deleteIfExists(path); // Delete the file if it exists
    	} catch (IOException e) {
    		e.printStackTrace(); // Log the exception or handle it appropriately
    		return false;
    	}
    }


}
