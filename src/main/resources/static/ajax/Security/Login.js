$(document).ready(function() {
    $("#loginForm").submit(function(event) {
        event.preventDefault(); // Ngăn chặn form gửi thông thường
        var account = $("#account").val();
        var password = $("#password").val();
        // Dữ liệu gửi đi
        var loginData = {
            account: account,
            password: password
        };
        $.ajax({
            url: "/login-api/login",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(loginData),
            xhrFields: {
                withCredentials: true // Đảm bảo session/cookie được gửi
            },
            success: function(response) {
                // Đăng nhập thành công
                fetchUserInfo();
            },
            error: function(xhr, status, error) {
                console.error("Lỗi đăng nhập:", error);
                $("#result").text("Đăng nhập thất bại, vui lòng kiểm tra lại.");
            }
        });
    });

    function fetchUserInfo() {
        $.ajax({
            url: "/login-api/info",
            type: "GET",
            xhrFields: {
                withCredentials: true // Đảm bảo session/cookie được gửi
            },
            success: function(data) {
                console.log("Thông tin người dùng:", data);
                $("#userInfo").text("Tên: " + data.fullName + ", Vai trò: " + data.roleName);
            },
            error: function(xhr, status, error) {
                console.error("Không thể lấy thông tin người dùng:", error);
            }
        });
    }
});
