
document.getElementById('updateStaffProfileForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Ngăn chặn form gửi mặc định

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
            // Xử lý phản hồi từ server
            // Có thể hiển thị thông báo thành công hoặc xử lý dữ liệu khác
            console.log(data);
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
    const form = document.getElementById('updateStaffProfileForm');
    const fullNameInput = form.querySelector('input[name="fullName"]');
    const emailInput = form.querySelector('input[name="email"]');
    const phoneInput = form.querySelector('input[name="numberPhone"]');
    const passwordInput = form.querySelector('input[name="password"]');

    form.addEventListener('submit', function (event) {
        let isValid = true;

        // Reset error messages
        form.querySelectorAll('.error-message').forEach(function (error) {
            error.textContent = '';
        });

        // Validate full name (required)
        if (!fullNameInput.value.trim()) {
            fullNameInput.nextElementSibling.textContent = 'Vui lòng nhập họ và tên';
            isValid = false;
        }

        // Validate email (required and valid format)
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailInput.value.trim()) {
            emailInput.nextElementSibling.textContent = 'Vui lòng nhập email';
            isValid = false;
        } else if (!emailPattern.test(emailInput.value)) {
            emailInput.nextElementSibling.textContent = 'Định dạng email không hợp lệ';
            isValid = false;
        }

        // Validate phone number (required and valid format)
        const phonePattern = /^0[0-9]{9}$/;
        if (!phoneInput.value.trim()) {
            phoneInput.nextElementSibling.textContent = 'Vui lòng nhập số điện thoại';
            isValid = false;
        } else if (!phonePattern.test(phoneInput.value)) {
            phoneInput.nextElementSibling.textContent = 'Số điện thoại không hợp lệ (bắt đầu bằng 0 và có 10 chữ số)';
            isValid = false;
        }

        // Validate password (required)
        if (!passwordInput.value.trim()) {
            passwordInput.nextElementSibling.textContent = 'Vui lòng nhập mật khẩu';
            isValid = false;
        }

        // Prevent form submission if not valid
        if (!isValid) {
            event.preventDefault();
        }
    });
});
