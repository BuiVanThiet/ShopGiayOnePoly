document.addEventListener('DOMContentLoaded', function () {
    // Lấy tham số từ URL
    var urlParams = new URLSearchParams(window.location.search);

    // Kiểm tra xem có tham số 'noAccess' và giá trị của nó là 'true' không
    if (urlParams.has('accessDenied') && urlParams.get('accessDenied') === 'true') {
        // Khởi tạo và hiển thị Modal
        var accessDeniedModal = new bootstrap.Modal(document.getElementById('accessDeniedModal'));
        accessDeniedModal.show();
    }
});