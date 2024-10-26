let selectedColorId; // Biến toàn cục để lưu ID màu sắc đã chọn
let selectedSizeId;  // Biến toàn cục để lưu ID kích thước đã chọn

// Lấy ID sản phẩm từ input hidden
const productId = document.getElementById("product-id").value;

// Khởi tạo các sự kiện khi trang đã tải xong
document.addEventListener("DOMContentLoaded", function () {
    // Gán sự kiện click cho tất cả các nút màu
    attachClickEvent('.color-btn', setTemporaryColor);

    // Gán sự kiện click cho tất cả các nút kích thước
    attachClickEvent('.size-btn', setTemporarySize);
});

// Hàm gán sự kiện click cho các nút
function attachClickEvent(selector, handler) {
    const buttons = document.querySelectorAll(selector);
    buttons.forEach(function (button) {
        button.addEventListener("click", function () {
            const value = button.innerText.trim(); // Lấy giá trị và loại bỏ khoảng trắng
            const id = button.getAttribute('data-color-id') || button.getAttribute('data-size-id'); // Lấy ID từ data-color-id hoặc data-size-id
            handler(value, id); // Gọi hàm handler với giá trị và ID
        });
    });
}

// Hàm đặt màu tạm thời
function setTemporaryColor(color, colorId) {
    selectedColorId = colorId; // Lưu ID màu đã chọn
    document.querySelectorAll('.temporary-color').forEach(function (element) {
        element.innerText = color; // Cập nhật biến tạm
    });
    document.getElementById("selected-color").innerText = "Màu: " + color; // Cập nhật hiển thị
    // Gọi API nếu đã chọn kích thước
    if (selectedSizeId) {
        getProductDetail(productId, selectedColorId, selectedSizeId); // Gọi API với ID màu sắc
    }
}

// Hàm đặt kích thước tạm thời
function setTemporarySize(size, sizeId) {
    selectedSizeId = sizeId; // Lưu ID kích thước đã chọn
    document.querySelectorAll('.temporary-size').forEach(function (element) {
        element.innerText = size; // Cập nhật biến tạm
    });
    document.getElementById("selected-size").innerText = "Kích thước: " + size; // Cập nhật hiển thị
    // Gọi API nếu đã chọn màu
    if (selectedColorId) {
        getProductDetail(productId, selectedColorId, selectedSizeId); // Gọi API với ID kích thước
    }
}

// Hàm để lấy thông tin chi tiết sản phẩm
function getProductDetail(productId, colorId, sizeId) {
    console.log("Color ID: " + colorId);
    console.log("Size ID: " + sizeId);
    console.log("Product ID: " + productId);
    $.ajax({
        url: '/api-client/products/product-detail',
        method: 'GET',
        data: {
            productId: productId,
            colorId: colorId,
            sizeId: sizeId
        },
        success: function (data) {
            console.log(data);
            if (data) {
                $('#quantity-display').text(data.quantity); // Cập nhật số lượng
                $('#price-display').text(data.price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })); // Cập nhật giá
            } else {
                $('#quantity-display').text(0); // Nếu không có dữ liệu, hiển thị số lượng là 0
                $('#price-display').text("0"); // Hiển thị thông báo không có giá
            }
        },
        error: function () {
            console.log("Không tìm thấy thông tin sản phẩm.");
            $('#quantity-display').text(0); // Đặt số lượng về 0 khi có lỗi
            $('#price-display').text("0"); // Hiển thị thông báo không có giá
        }
    });
}

// Hàm changeColor - xử lý thay đổi màu sắc
function changeColor(button) {
    const color = button.innerText.trim(); // Lấy giá trị màu
    const colorId = button.getAttribute('data-color-id'); // Lấy ID màu
    setTemporaryColor(color, colorId); // Gọi hàm để cập nhật màu
}

// Hàm changeSize - xử lý thay đổi kích thước
function changeSize(button) {
    const size = button.innerText.trim(); // Lấy giá trị kích thước
    const sizeId = button.getAttribute('data-size-id'); // Lấy ID kích thước
    setTemporarySize(size, sizeId); // Gọi hàm để cập nhật kích thước
}
