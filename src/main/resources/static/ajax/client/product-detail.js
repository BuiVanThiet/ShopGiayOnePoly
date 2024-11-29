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
            const value = button.innerText.trim();
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
                    $('#price-display').text(data.price.toLocaleString('en-US') + " ₫");

                    $('#price-apply-discount').text(data.priceDiscount.toLocaleString('en-US') + " ₫");
                    $('#price-modal').text(data.priceDiscount.toLocaleString('en-US') + " ₫");

                    if (data.price === data.priceDiscount) {
                        $('#price-display').hide();
                    } else {
                        $('#price-display').show();
                    }

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

    btnMinus.addEventListener("click", function () {
        let quantityBuy = parseInt(quantityInput.val());
        if (quantityBuy > 1) {
            quantityBuy -= 1;
            quantityInput.val(quantityBuy);
        }
    });
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
        quantityBuy += 1;
        quantityInput.val(quantityBuy);
    });
});

function showCartModal() {
    $('#addCartModal').modal('show');
}

function showPayNowModal() {
    $('#payNowModal').modal('show');
}

function addToCart() {
    // Lấy productDetailId
    var productDetailId = $('#productDetailID-hidden').val();

    // Lấy số lượng
    var quantityBuy = parseInt($('input[name="quantity"]').val());

    // Lấy các thông tin sản phẩm
    var productName = $('#product-name').text();
    var productPrice = $('#price-modal').text();
    var productColor = $('#color-modal').text();
    var productSize = $('#size-modal').text();
    var productImage = $('.item-img img').attr('src');

    $.ajax({
        url: '/api-client/add-to-cart',
        type: 'POST',
        contentType: "application/json",
        data: JSON.stringify({
            productDetailId: productDetailId,
            quantity: quantityBuy
        }),
        success: function (data) {
            if (data && data.success) {
                console.log(data)
                // Cập nhật thông tin trong modal thành công
                $('#success-product-image').attr('src', productImage);
                $('#success-product-name').text(productName);
                $('#success-price-modal').text(productPrice);
                $('#success-color-modal').text(productColor);
                $('#success-size-modal').text(productSize);
                $('#success-quantity-modal').text(quantityBuy);

                var successModal = new bootstrap.Modal(document.getElementById('addCartSuccessModal'));
                successModal.show();
            } else {
                // Hiển thị lỗi
                $('#container-message .message-error').text(data.message || 'Có lỗi xảy ra khi thêm vào giỏ hàng.');
                showCartModal();
            }
        },
        error: function (error) {
            console.error('Error:', error);
            $('#container-message .message-error').text('Có lỗi xảy ra khi thêm vào giỏ hàng.');
            showCartModal();
        }
    });
}


// Sự kiện cho nút "Tiếp tục mua sắm" trong modal
$('#continue-shopping').click(function () {
    $('#successModal').modal('hide');
});

document.addEventListener("DOMContentLoaded", function () {
    // Lấy các phần tử cần thiết cho modal "Mua ngay"
    const quantityInputPayNow = $('input[name="quantity"]'); // Input số lượng trong modal "Mua ngay"
    const quantityTextPayNow = document.getElementById('quantity-display-paynow').innerText;
    var quantityPayNow = parseInt(quantityTextPayNow);

    const btnMinusPayNow = document.getElementById('qtyminus-paynow');
    const btnPlusPayNow = document.getElementById('qtyplus-paynow');

    const addToCartButtonPayNow = document.getElementById('add-to-cart-paynow'); // Nút "Mua ngay" trong modal "Mua ngay"
    const quantityNowTextPayNow = document.getElementById('quantity-display-paynow').innerText;
    var quantityNowPayNow = parseInt(quantityNowTextPayNow);

    // Hàm hiển thị modal lỗi khi vượt quá số lượng
    function showPayNowModalError(message) {
        $('#container-message-paynow .message-error').text(message);
        $('#payNowModal').modal('show'); // Hiển thị modal khi có lỗi
    }

    // Sự kiện cho nút trừ số lượng
    btnMinusPayNow.addEventListener("click", function () {
        let quantityBuyPayNow = parseInt(quantityInputPayNow.val());
        if (quantityBuyPayNow > 1) {
            quantityBuyPayNow -= 1;
            quantityInputPayNow.val(quantityBuyPayNow);
        }
    });

    // Sự kiện cho nút cộng số lượng
    btnPlusPayNow.addEventListener("click", function () {
        let quantityBuyPayNow = parseInt(quantityInputPayNow.val());
        if (quantityBuyPayNow >= quantityNowPayNow) {
            showPayNowModalError("Bạn đã mua vượt quá số lượng hiện có.");
            addToCartButtonPayNow.disabled = true; // Vô hiệu hóa nút mua
            return;
        }

        // Kiểm tra nếu số lượng mua vượt quá giới hạn tối đa (10 sản phẩm)
        if (quantityBuyPayNow >= 10) {
            showPayNowModalError("Chỉ được mua tối đa 10 sản phẩm cùng loại.");
            quantityInputPayNow.val(10); // Đặt số lượng tối đa là 10
            return;
        }
        quantityBuyPayNow += 1;
        quantityInputPayNow.val(quantityBuyPayNow); // Cập nhật số lượng
    });
});

function buyNow() {
    var productDetailId = $('#productDetailID-hidden').val();
    // Lấy quantity
    var quantityBuy = parseInt($('input[name="quantity"]').val());

    $.ajax({
        url: '/api-client/add-to-cart', type: 'POST', contentType: "application/json", data: JSON.stringify({
            productDetailId: productDetailId, quantity: quantityBuy
        }), success: function (data) {
            if (data && data.success) {
                window.location.href = '/onepoly/cart';
            } else {
                $('#container-message .message-error').text(data.message || 'Có lỗi xảy ra khi thêm vào giỏ hàng.');
                showCartModal();
            }
        }, error: function (error) {
            console.error('Error:', error);
            $('#container-message .message-error').text('Có lỗi xảy ra khi thêm vào giỏ hàng.');
            showCartModal();
        }
    });
}

$(document).ready(function () {
    checkQuantity();
});





