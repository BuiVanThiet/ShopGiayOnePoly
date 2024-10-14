// Giả sử bạn có biến birthDay chứa giá trị ngày sinh
const birthDay = new Date(); // Ví dụ: Thay đổi cho phù hợp với giá trị thực tế

// Tạo danh sách ngày
const daySelect = document.getElementById('dob-day');
const monthSelect = document.getElementById('dob-month');
const yearSelect = document.getElementById('dob-year');

// Tạo tùy chọn cho ngày
const daysInMonth = new Date(birthDay.getFullYear(), birthDay.getMonth() + 1, 0).getDate();
for (let day = 1; day <= daysInMonth; day++) {
    const option = document.createElement('option');
    option.value = day;
    option.textContent = day;
    if (day === birthDay.getDate()) {
        option.selected = true; // Chọn ngày hiện tại
    }
    daySelect.appendChild(option);
}

// Tạo tùy chọn cho tháng
for (let month = 1; month <= 12; month++) {
    const option = document.createElement('option');
    option.value = month;
    option.textContent = month;
    if (month === (birthDay.getMonth() + 1)) {
        option.selected = true; // Chọn tháng hiện tại
    }
    monthSelect.appendChild(option);
}

// Tạo tùy chọn cho năm
for (let year = 1900; year <= 2024; year++) {
    const option = document.createElement('option');
    option.value = year;
    option.textContent = year;
    if (year === birthDay.getFullYear()) {
        option.selected = true; // Chọn năm hiện tại
    }
    yearSelect.appendChild(option);
}

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
