//package com.example.shopgiayonepoly.config;
//
//import com.cloudinary.Cloudinary;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class CloudinaryConfig {
//    private final String CLOUD_NAME="dfy4umpja";
//    private final String API_KEY="778742222858418";
//    private final String API_SECRET="pMdDIWuOP__Qk4MmGly07uWxVhA";
//    @Bean
//    public Cloudinary cloudinary() {
//        Map<String, String> config = new HashMap<>();
//        config.put("cloud_name", CLOUD_NAME);
//        config.put("api_key", API_KEY);
//        config.put("api_secret", API_SECRET);
//
//        System.out.println("Cloudinary Config: " + config); // In ra cấu hình để kiểm tra
//
//        return new Cloudinary(config);
//    }
//
//}
