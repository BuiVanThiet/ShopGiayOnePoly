// document.getElementById('register').addEventListener('click', function(event) {
//     event.preventDefault();
//     fetch('/register-api/register', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({
//             fullName: document.getElementById('fullName').value,
//             gender: document.querySelector('input[name="gender"]:checked').value,
//             email: document.getElementById('email').value,
//             numberPhone: document.getElementById('numberPhone').value,
//             address: document.getElementById('address').value,
//             acount: document.getElementById('username').value,
//             password: document.getElementById('password').value,
//             confirmPassword: document.getElementById('confirmPassword').value,
//
//         })
//     })
//         .then(response => response.json())
//         .then(data => {
//             if (data.success) {
//                 alert(data.message);
//                 window.location.href = '/login';
//             } else {
//                 alert('Đăng ký thất bại: ' + data.message);
//             }
//         })
//         .catch(error => console.error('Lỗi:', error));
// });