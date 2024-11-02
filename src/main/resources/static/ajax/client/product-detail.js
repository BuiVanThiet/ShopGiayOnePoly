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

            if (data) {
                console.log("ProductDetail: ", data); // In ra đối tượng để kiểm tra cấu trúc
                $('#productDetailID-hidden').val(data.productDetailId);
                console.log('Id sản phẩm chi tiết: ' + data.productDetailId);
                const quantity = data.quantity || 0; // Đảm bảo giá trị số lượng là hợp lệ
                $('#quantity-display').text(quantity);
                $('#price-display').text(data.price.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}));
                $('#price-modal').text(data.price.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}));
            } else {
                $('#quantity-display').text(0);
                $('#price-display').text("0");
                $('#price-modal').text("0");
            }
        }, error: function (xhr) {
            console.log("Dữ liệu đầu vào không được cập nhật");

            console.log("Error" + xhr.responseText)
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
        if (currentValue <= 10) {
            quantityInput.value = currentValue + 1;
        } else {
            alert("Không  thể lớn hơn 10 sản phẩm")
        }
    });
});

function showCartModal() {
    $('#addCartModal').modal('show');
}

function addToCart() {
    // Lấy productDetailId
    var productDetailId = $('#productDetailID-hidden').val();

    // Lấy quantity
    var quantityBuy = parseInt($('input[name="quantity"]').val());

    if (isNaN(quantityBuy) || quantityBuy <= 0) {
        alert("Số lượng không hợp lệ, phải lớn hơn 0.");
        return;
    }

    $.ajax({
        url: '/onepoly/add-to-cart',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify({
            productDetailId: productDetailId,
            quantity: quantityBuy
        }),
        success: function (data) {
            if (data && data.success) {
                alert("Sản phẩm đã được thêm vào giỏ hàng!");
                window.location.href = '/onepoly/cart';
            } else {
                console.log(data.message || 'Có lỗi xảy ra khi thêm vào giỏ hàng.');
            }
        },
        error: function (error) {
            console.error('Error:', error);
            alert('Có lỗi xảy ra khi thêm vào giỏ hàng.');
        }
    });
}

// Hàm cập nhật số lượng sản phẩm trong giỏ hàng
window.updateQuantity = function (productDetailId, change) {
    $.ajax({
        url: '/onepoly/update-cart',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify({productDetailId: productDetailId, change: change}),
        success: function (data) {
            if (data.success) {
                fetchCartItems(); // Cập nhật lại danh sách giỏ hàng
            } else {
                console.log(data.message || 'Có lỗi xảy ra khi cập nhật số lượng.');
            }
        },
        error: function (error) {
            console.error('Error updating quantity:', error);
            alert('Có lỗi xảy ra khi cập nhật số lượng.');
        }
    });
};

// Hàm xóa sản phẩm khỏi giỏ hàng
window.removeItem = function (productDetailId) {
    $.ajax({
        url: '/onepoly/remove-from-cart',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify({productDetailId: productDetailId}),
        success: function (data) {
            if (data.success) {
                alert("Sản phẩm đã được xóa khỏi giỏ hàng!");
                fetchCartItems(); // Cập nhật lại danh sách giỏ hàng
            } else {
                console.log(data.message || 'Có lỗi xảy ra khi xóa sản phẩm.');
            }
        },
        error: function (error) {
            console.error('Error removing item:', error);
            alert('Có lỗi xảy ra khi xóa sản phẩm.');
        }
    });
};





