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
// Hàm đặt màu tạm thời
function setTemporaryColor(color, colorId) {
    selectedColorId = colorId; // Lưu ID màu đã chọn
    document.querySelectorAll('.temporary-color').forEach(function (element) {
        element.innerText = color; // Cập nhật biến tạm
    });
    document.getElementById("selected-color").innerText = color; // Cập nhật hiển thị
    document.getElementById("color-modal").innerText = color; // Cập nhật giá trị màu cho color-modal

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
    document.getElementById("selected-size").innerText = size; // Cập nhật hiển thị
    document.getElementById("size-modal").innerText = size; // Cập nhật giá trị kích thước cho size-modal

    // Gọi API nếu đã chọn màu
    if (selectedColorId) {
        getProductDetail(productId, selectedColorId, selectedSizeId); // Gọi API với ID kích thước
    }
}

// Hàm để lấy thông tin chi tiết sản phẩm
function getProductDetail(productId, colorId, sizeId) {
    $.ajax({
        url: '/api-client/products/product-detail',
        method: 'GET',
        data: {
            productId: productId,
            colorId: colorId,
            sizeId: sizeId
        },
        success: function (data) {
            console.log("ProductDetail:", data); // In ra đối tượng để kiểm tra cấu trúc
            if (data) {
                $('#productDetailID-hidden').val(data.productDetailId);
                console.log('Id sản phẩm chi tiết: ' + data.productDetailId);
                const quantity = data.quantity || 0; // Đảm bảo giá trị số lượng là hợp lệ
                $('#quantity-display').text(quantity);
                $('#price-display').text(data.price.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}));
                $('#price-modal').text(data.price.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}));
            } else {
                console.log("Dữ liệu đầu vào không được cập nhật");
                $('#quantity-display').text(0);
                $('#price-display').text("0");
                $('#price-modal').text("0");
            }
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

document.addEventListener("DOMContentLoaded", function () {
    const quantityInput = document.querySelector('.item-quantity');
    const maxQuantityText = $('#quantity-display').text().trim();
    const maxQuantity = parseInt(maxQuantityText) || 0;
    console.log("value max: " + maxQuantity);

    const btnMinus = document.getElementById('qtyminus');
    const btnPlus = document.getElementById('qtyplus');

    // Sự kiện khi bấm nút trừ
    btnMinus.addEventListener("click", function () {
        let currentValue = parseInt(quantityInput.value);
        if (currentValue > 1) {
            quantityInput.value = currentValue - 1;
        }
    });

    // Sự kiện khi bấm nút cộng
    btnPlus.addEventListener("click", function () {
        let currentValue = parseInt(quantityInput.value);
        // if (currentValue < maxQuantity) {
        quantityInput.value = currentValue + 1;
        // }
    });
});

function showCartModal() {
    $('#addCartModal').modal('show');
}

function addToCart(productDetailId, quantityBuy) {
    $.ajax({
        url: '/add-to-cart',
        type: 'POST',
        data: {
            productDetailId: productDetailId,
            quantity: quantityBuy
        },
        success: function (data) {
            // Kiểm tra nếu `data.success` tồn tại
            if (data && data.success) {
                $('#addCartModal').modal('show');
                window.location.href = '/onepoly/cart';
            } else {
                alert(data.message || 'Có lỗi xảy ra khi thêm vào giỏ hàng.');
            }
        },
        error: function (error) {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi thêm vào giỏ hàng.');
        }
    });
}

$(document).on("click", "#add-to-cart", function (event) {
    event.preventDefault();
    const productDetailId = document.getElementById("productDetailID-hidden").value; // Lấy giá trị từ input hidden
    const quantity = $(".item-quantity").val(); // Lấy số lượng từ modal

    // Kiểm tra productDetailId và quantity trước khi gọi addToCart
    if (productDetailId && quantity) {
        addToCart(productDetailId, quantity);
    } else {
        alert("Vui lòng kiểm tra thông tin sản phẩm và số lượng.");
    }
});




