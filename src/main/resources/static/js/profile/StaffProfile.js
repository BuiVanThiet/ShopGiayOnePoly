
document.getElementById('updateStaffProfileForm').addEventListener('submit', function (event) {

    const day = document.getElementById('dob-day').value;
    const month = document.getElementById('dob-month').value;
    const year = document.getElementById('dob-year').value;

    const dob = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;

    const formData = new FormData(this);
    formData.append('birthDay', dob); // Gửi ngày sinh

    fetch(this.action, {
        method: 'POST',
        body: formData,
    })
        .then(response => response.text())
        .then(data => {
            // // Xử lý phản hồi từ server
            // // Có thể hiển thị thông báo thành công hoặc xử lý dữ liệu khác
            // console.log(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
});

// Xử lý tải ảnh
document.getElementById('staff-file-upload').addEventListener('change', function (event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById('staff-avatar-preview').src = e.target.result;
        }
        reader.readAsDataURL(file);
    }
});

document.addEventListener('DOMContentLoaded', function () {
    // Xử lý hiển thị form đổi mật khẩu
    const editButton = document.getElementById('editButton');
    const closeButton = document.getElementById('closeButton');
    const passwordChangeForm = document.getElementById('passwordChangeForm');

    editButton.addEventListener('click', function () {
        passwordChangeForm.classList.remove('hidden'); // Hiện form
        editButton.classList.add('hidden'); // Ẩn nút Chỉnh sửa
        closeButton.classList.remove('hidden'); // Hiện nút Đóng
    });

    closeButton.addEventListener('click', function () {
        passwordChangeForm.classList.add('hidden'); // Ẩn form
        editButton.classList.remove('hidden'); // Hiện nút Chỉnh sửa
        closeButton.classList.add('hidden'); // Ẩn nút Đóng
    });

    // Xử lý hiển thị mật khẩu khi nhấn vào icon mắt
    function togglePasswordVisibility(toggleButtonId, inputId) {
        const toggleButton = document.getElementById(toggleButtonId);
        const input = document.getElementById(inputId);

        toggleButton.addEventListener('click', function () {
            if (input.type === 'password') {
                input.type = 'text';
                toggleButton.querySelector('i').classList.replace('fa-eye-slash', 'fa-eye');
            } else {
                input.type = 'password';
                toggleButton.querySelector('i').classList.replace('fa-eye', 'fa-eye-slash');
            }
        });
    }

    // Áp dụng toggle cho các trường mật khẩu
    togglePasswordVisibility('toggleCurrentPassword', 'currentPassword');
    togglePasswordVisibility('toggleNewPassword', 'newPassword');
    togglePasswordVisibility('toggleRetypePassword', 'confirmPassword');
});