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

//hiển thị ảnh trên web
// document.getElementById('file-upload').addEventListener('change', function(event) {
//     const file = event.target.files[0];
//     const reader = new FileReader();
//
//     if (file) {
//         reader.onload = function(e) {
//             document.getElementById('avatar-preview').src = e.target.result;
//         }
//         reader.readAsDataURL(file);
//     }
// });

//
// // // Hàm để kiểm tra năm nhuận
// // function isLeapYear(year) {
// //     return (year % 4 === 0 && year % 100 !== 0) || (year % 400 === 0);
// // }
//
// // // Hàm để cập nhật số ngày dựa trên tháng và năm đã chọn
// // function updateDays() {
// //     const selectedYear = parseInt(yearSelect.value);
// //     const selectedMonth = parseInt(monthSelect.value);
//
// //     // Số ngày trong mỗi tháng
// //     const daysInMonth = [31, isLeapYear(selectedYear) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
//
// //     // Xóa các option cũ của ngày
// //     daySelect.innerHTML = '<option selected disabled>Chọn ngày</option>';
//
// //     // Thêm option cho các ngày dựa theo tháng và năm
// //     for (let day = 1; day <= daysInMonth[selectedMonth - 1]; day++) {
// //         const option = document.createElement('option');
// //         option.value = day;
// //         option.textContent = day;
// //         daySelect.appendChild(option);
// //     }
// // }
//
// // // Lắng nghe sự kiện thay đổi của tháng và năm để cập nhật ngày
// // monthSelect.addEventListener('change', updateDays);
// // yearSelect.addEventListener('change', updateDays);
