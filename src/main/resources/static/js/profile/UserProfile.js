document.getElementById('confirmSaveButton').addEventListener('click', function () {
    document.getElementById('passwordChangeForm').submit();
});
document.getElementById('confirmUpdateButton').addEventListener('click', function () {
    document.getElementById('updateProfileForm').submit();
});

document.getElementById('file-upload').addEventListener('change', function (event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            // Cập nhật ảnh xem trước
            document.getElementById('avatar-preview').src = e.target.result;
        }
        reader.readAsDataURL(file);
    }
});

// Xử lý gửi biểu mẫu
// document.getElementById('updateStaffProfileForm').addEventListener('submit', function (event) {
//     // event.preventDefault(); // Ngăn chặn hành vi mặc định của biểu mẫu
//
//     const day = document.getElementById('dob-day').value;
//     const month = document.getElementById('dob-month').value;
//     const year = document.getElementById('dob-year').value;
//
//     // Định dạng ngày sinh
//     const dob = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
//
//     // Tạo đối tượng FormData và thêm ngày sinh
//     const formData = new FormData(this);
//     formData.append('birthDay', dob); // Gửi ngày sinh
//
//     // Gửi yêu cầu fetch
//     fetch(this.action, {
//         method: 'POST',
//         body: formData,
//     })
//         .then(response => response.text())
//         .then(data => {
//             // Xử lý phản hồi từ server
//             // Có thể hiển thị thông báo thành công hoặc chuyển hướng
//             alert('Cập nhật thành công!'); // Hiển thị thông báo thành công
//             console.log(data); // In phản hồi để xem chi tiết
//         })
//         .catch(error => {
//             console.error('Error:', error);
//             alert('Đã xảy ra lỗi khi cập nhật thông tin.'); // Thông báo lỗi
//         });
// });
