const birthDay = new Date();

// Tạo tùy chọn cho ngày
const daySelect = document.getElementById('dob-day');
const daysInMonth = new Date(birthDay.getFullYear(), birthDay.getMonth() + 1, 0).getDate();
for (let day = 1; day <= daysInMonth; day++) {
    const option = document.createElement('option');
    option.value = day;
    option.textContent = day;
    daySelect.appendChild(option);
}

// Tạo tùy chọn cho tháng
const monthSelect = document.getElementById('dob-month');
for (let month = 1; month <= 12; month++) {
    const option = document.createElement('option');
    option.value = month;
    option.textContent = month;
    monthSelect.appendChild(option);
}

// Tạo tùy chọn cho năm
const yearSelect = document.getElementById('dob-year');
for (let year = 1900; year <= 2024; year++) {
    const option = document.createElement('option');
    option.value = year;
    option.textContent = year;
    yearSelect.appendChild(option);
}
document.getElementById('updateProfileForm').addEventListener('submit', function (event) {
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
            // Có thể hiển thị thông báo thành công hoặc thất bại
        })
        .catch(error => console.error('Error:', error));
});

document.getElementById('file-upload').onchange = function (event) {
    const [file] = event.target.files; // Lấy tệp đầu tiên từ danh sách tệp đã chọn
    if (file) {
        const avatarPreview = document.getElementById('avatar-preview');
        avatarPreview.src = URL.createObjectURL(file); // Tạo URL tạm thời và gán cho src của img
    }
};

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('updateProfileForm');
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

