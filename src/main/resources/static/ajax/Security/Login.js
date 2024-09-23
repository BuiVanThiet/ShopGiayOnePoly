$(document).ready(function() {
    $.ajax({
        url: '/login-api/info',
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