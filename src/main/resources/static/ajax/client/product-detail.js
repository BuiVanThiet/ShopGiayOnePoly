let selectedColorId; // Biến toàn cục để lưu ID màu sắc đã chọn
let selectedSizeId;  // Biến toàn cục để lưu ID kích thước đã chọn

const productId = document.getElementById("product-id").value;
document.addEventListener("DOMContentLoaded", function () {
    // Gán sự kiện click cho tất cả các nút màu
    attachClickEvent('.color-btn', setTemporaryColor);

    // Gán sự kiện click cho tất cả các nút kích thước
    attachClickEvent('.size-btn', setTemporarySize);
});

function attachClickEvent(selector, handler) {
    const buttons = document.querySelectorAll(selector);
    buttons.forEach(function (button) {
        button.addEventListener("click", function () {
            const value = button.innerText.trim(); // Lấy giá trị và loại bỏ khoảng trắng
            const id = button.getAttribute('data-color-id') || button.getAttribute('data-size-id'); // Lấy ID từ data-color-id hoặc data-size-id
            handler(value, id);
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
        url: '/api-client/products/product-detail', method: 'GET', data: {
            productId: productId, colorId: colorId, sizeId: sizeId
        }, success: function (data) {
            if (data) {

                const quantity = data.quantity || 0;
                $('#quantity-display').text(quantity);

                // Kiểm tra nếu quantity > 0, hiển thị giá và enable nút thêm vào giỏ
                if (quantity > 0) {
                    $('#productDetailID-hidden').val(data.productDetailId);
                    $('#price-display').text(data.price.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'}));

                    // Hiển thị giá giảm (nếu có)
                    $('#price-apply-discount').text(data.priceDiscount.toLocaleString('vi-VN', {
                        style: 'currency', currency: 'VND'
                    }));
                    $('#price-modal').text(data.priceDiscount.toLocaleString('vi-VN', {
                        style: 'currency', currency: 'VND'
                    }));

                    // Enable nút thêm vào giỏ
                    $('#btn-add-cart').prop('disabled', false);
                } else {
                    // Khi quantity = 0, đặt giá về 0 và disable nút thêm vào giỏ
                    $('#price-display').text("0");
                    $('#price-apply-discount').text("0");
                    $('#price-modal').text("0");

                    // Disable nút thêm vào giỏ
                    $('#btn-add-cart').prop('disabled', true);
                }
            } else {
                // Nếu không có dữ liệu, hiển thị 0 và disable nút thêm vào giỏ
                $('#quantity-display').text(0);
                $('#price-display').text("0");
                $('#price-apply-discount').text("0");
                $('#price-modal').text("0");

                // Disable nút thêm vào giỏ
                $('#btn-add-cart').prop('disabled', true);
            }
        }, error: function (xhr) {
            console.log("Dữ liệu đầu vào không được cập nhật");
            console.log("Error" + xhr.responseText);
        }
    });
}

function checkQuantity() {
    const quantityText = document.getElementById('quantity-display').innerText;
    var quantity = parseInt(quantityText);

    if (quantity <= 0) {
        $('#btn-add-cart').prop('disabled', true);
    } else {
        $('#btn-add-cart').prop('disabled', false);
    }
}


// Hàm changeColor - xử lý thay đổi màu sắc
function changeColor(button) {
    const color = button.innerText.trim(); // Lấy giá trị màu
    const colorId = button.getAttribute('data-color-id'); // Lấy ID màu
    setTemporaryColor(color, colorId);
}

// Hàm changeSize - xử lý thay đổi kích thước
function changeSize(button) {
    const size = button.innerText.trim(); // Lấy giá trị kích thước
    const sizeId = button.getAttribute('data-size-id'); // Lấy ID kích thước
    setTemporarySize(size, sizeId);
}

document.addEventListener("DOMContentLoaded", function () {
    const quantityInput = $('input[name="quantity"]');
    const quantityText = document.getElementById('quantity-display').innerText;
    var quantity = parseInt(quantityText);

    const btnMinus = document.getElementById('qtyminus');
    const btnPlus = document.getElementById('qtyplus');

    function showCartModal() {
        // Logic hiển thị modal lỗi
        $('#cartModal').modal('show');
    }

    // Sự kiện khi bấm nút trừ
    btnMinus.addEventListener("click", function () {
        let quantityBuy = parseInt(quantityInput.val());
        if (quantityBuy > 1) {
            quantityBuy -= 1;
            quantityInput.val(quantityBuy);
        }
    });

    // Sự kiện khi bấm nút cộng
    btnPlus.addEventListener("click", function () {
        let quantityBuy = parseInt(quantityInput.val());
        const addToCartButton = document.getElementById('add-to-cart');
        const quantityNowText = document.getElementById('quantity-display').innerText;
        var quantityNow = parseInt(quantityNowText);
        // Kiểm tra nếu số lượng mua vượt quá số lượng hiện có
        if (quantityBuy > quantityNow) {
            $('#container-message .message-error').text("Bạn đã mua vượt quá số lượng hiện có.");
            showCartModal();
            addToCartButton.disable();
            return;
        }

        // Kiểm tra nếu số lượng mua vượt quá giới hạn tối đa (10 sản phẩm)
        if (quantityBuy >= 10) {
            $('#container-message .message-error').text("Chỉ được mua tối đa 10 sản phẩm cùng loại.");
            quantityInput.val(10);
            showCartModal();
            return;
        }

        // Nếu chưa đạt giới hạn, tăng số lượng mua lên 1
        quantityBuy += 1;
        quantityInput.val(quantityBuy);
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

    $.ajax({
        url: '/api-client/add-to-cart', type: 'POST', contentType: "application/json", data: JSON.stringify({
            productDetailId: productDetailId, quantity: quantityBuy
        }), success: function (data) {
            if (data && data.success) {
                alert("Sản phẩm đã được thêm vào giỏ hàng!");
                window.location.href = '/onepoly/cart';
            } else {
                $('#container-message .message-error').text(data.message || 'Có lỗi xảy ra khi thêm vào giỏ hàng.');
                showCartModal();  // Hiển thị modal khi có lỗi
            }
        }, error: function (error) {
            console.error('Error:', error);
            $('#container-message .message-error').text('Có lỗi xảy ra khi thêm vào giỏ hàng.');
            showCartModal();  // Hiển thị modal khi có lỗi
        }
    });
}


//
// // Hàm xóa sản phẩm khỏi giỏ hàng
// window.removeItem = function (productDetailId) {
//     $.ajax({
//         url: '/onepoly/remove-from-cart',
//         type: 'POST',
//         contentType: "application/json",
//         data: JSON.stringify({productDetailId: productDetailId}),
//         success: function (data) {
//             if (data.success) {
//                 alert("Sản phẩm đã được xóa khỏi giỏ hàng!");
//                 fetchCartItems(); // Cập nhật lại danh sách giỏ hàng
//             } else {
//                 console.log(data.message || 'Có lỗi xảy ra khi xóa sản phẩm.');
//             }
//         },
//         error: function (error) {
//             console.error('Error removing item:', error);
//             alert('Có lỗi xảy ra khi xóa sản phẩm.');
//         }
//     });
// };
$(document).ready(function () {
    checkQuantity();
});





