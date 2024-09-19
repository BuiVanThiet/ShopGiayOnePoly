$(document).ready(function() {
    $.ajax({
        url: '/login-api/info', // Địa chỉ API để lấy thông tin người dùng
        method: 'GET',
        success: function(data) {
            $('#fullName').text(data.fullName);
            $('#roleName').text(data.roleName);
        },
        error: function() {
            $('#user-info').html('<p>Chưa đăng nhập!</p>');
        }
    });
});