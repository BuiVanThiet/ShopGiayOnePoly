function showDropdown() {
    document.getElementById('authDropdown').classList.add('show'); // Thêm class để hiển thị
}

function hideDropdown() {
    document.getElementById('authDropdown').classList.remove('show'); // Gỡ bỏ class để ẩn
}

function login() {
    window.location.href = "/onepoly/login";
}

function register() {
    window.location.href = "/onepoly/register";
}

function logout() {
    // Gửi yêu cầu đăng xuất đến server
    window.location.href = '/onepoly/logout';
}

function profile() {
    // Gửi yêu cầu đăng xuất đến server
    window.location.href = '/profile/userProfile';
}

function updateCartQuantity() {
    fetch('/api-client/quantity-product-from-cart')
        .then(response => response.json())
        .then(data => {
            document.getElementById("cart-count").textContent = data;
        })
        .catch(error => console.error('Error fetching cart quantity:', error));
}

// Gọi hàm để cập nhật giỏ hàng khi trang tải
updateCartQuantity();

