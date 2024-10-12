package com.example.shopgiayonepoly.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadimp implements FileUpload{
    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String publicId = originalFilename != null ? originalFilename.substring(0, originalFilename.lastIndexOf(".")) : UUID.randomUUID().toString();

        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id",publicId))
                .get("url").toString();
    }
}
