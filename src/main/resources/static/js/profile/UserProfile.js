// document.getElementById('updateProfileForm').addEventListener('submit', function (event) {
//     event.preventDefault();
//     const day = document.getElementById('dob-day').value;
//     const month = document.getElementById('dob-month').value;
//     const year = document.getElementById('dob-year').value;
//
//     const dob = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
//
//     const formData = new FormData(this);
//     formData.append('birthDay', dob); // Gửi ngày sinh
//
//     fetch(this.action, {
//         method: 'POST',
//         body: formData,
//     })
//         .then(response => response.text())
//         .then(data => {
//             // Xử lý phản hồi từ server
//             // Có thể hiển thị thông báo thành công hoặc thất bại
//         })
//         .catch(error => console.error('Error:', error));
// });
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

document.getElementById('file-upload').addEventListener('change', function (event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function (e) {
            document.getElementById('avatar-preview').src = e.target.result;
        }
        reader.readAsDataURL(file);
    }
});
