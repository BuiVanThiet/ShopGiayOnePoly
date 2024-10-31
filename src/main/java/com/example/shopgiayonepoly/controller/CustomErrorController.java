package com.example.shopgiayonepoly.controller;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class CustomErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private final ErrorAttributes errorAttributes;

    public CustomErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Ghi nhận thông tin lỗi
        System.out.println("Request URL: " + request.getRequestURL());

        // Chuyển đổi từ HttpServletRequest sang WebRequest
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributes = getErrorAttributes(webRequest);

        // Kiểm tra mã lỗi để trả về trang tương ứng
        Integer statusCode = (Integer) errorAttributes.get("status");
        if (statusCode != null && statusCode == HttpStatus.FORBIDDEN.value()) {
            return "errorTemplate/403"; // Đường dẫn đến trang lỗi 403 của bạn
        } else if (statusCode != null && statusCode == HttpStatus.NOT_FOUND.value()) {
            return "errorTemplate/404"; // Đường dẫn đến trang lỗi 404 của bạn
        }

        return "errorTemplate/404"; // Đường dẫn đến trang lỗi chung nếu không xác định
    }

    private Map<String, Object> getErrorAttributes(WebRequest request) {
        // Tùy chỉnh các tùy chọn ở đây
        ErrorAttributeOptions options = ErrorAttributeOptions.defaults().including(ErrorAttributeOptions.Include.STACK_TRACE);
        return errorAttributes.getErrorAttributes(request, options);
    }
}



