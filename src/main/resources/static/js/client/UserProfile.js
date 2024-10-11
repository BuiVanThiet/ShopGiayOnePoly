// // Lấy thẻ select cho dropdown năm
// const yearSelect = document.getElementById('dob-year');
//
// // Tạo dropdown danh sách năm từ 1910 đến 2024
// for (let year = 2024; year >= 1910; year--) {
//     const option = document.createElement('option');
//     option.value = year;
//     option.textContent = year;
//     yearSelect.appendChild(option);
// }
//
// // Lặp để tạo dropdown cho ngày và tháng (nếu cần)
// const daySelect = document.getElementById('dob-day');
// for (let day = 1; day <= 31; day++) {
//     const option = document.createElement('option');
//     option.value = day;
//     option.textContent = day;
//     daySelect.appendChild(option);
// }
//
// const monthSelect = document.getElementById('dob-month');
// const months = ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'];
// months.forEach((month, index) => {
//     const option = document.createElement('option');
//     option.value = index + 1;
//     option.textContent = month;
//     monthSelect.appendChild(option);
// });
//
//
// //Hàm tính năm nhuận
// // const daySelect = document.getElementById('dob-day');
// // const monthSelect = document.getElementById('dob-month');
// // const yearSelect = document.getElementById('dob-year');
//
// // // Tạo dropdown cho năm từ 1910 đến năm hiện tại
// // const currentYear = new Date().getFullYear();
// // for (let year = currentYear; year >= 1910; year--) {
// //     const option = document.createElement('option');
// //     option.value = year;
// //     option.textContent = year;
// //     yearSelect.appendChild(option);
// // }
//
// // // Tạo dropdown cho tháng
// // const months = ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'];
// // months.forEach((month, index) => {
// //     const option = document.createElement('option');
// //     option.value = index + 1;
// //     option.textContent = month;
// //     monthSelect.appendChild(option);
// // });
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
