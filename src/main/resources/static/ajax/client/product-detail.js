let selectedColorId;
let selectedSizeId;
const productId = document.getElementById("product-id").value;
document.addEventListener("DOMContentLoaded", function () {
    // Lấy màu và kích thước đầu tiên (nếu có)
    const firstColorButton = document.querySelector('.color-btn');
    const firstSizeButton = document.querySelector('.size-btn');

    // Kiểm tra nếu có màu và kích thước
    if (firstColorButton && firstSizeButton) {
        const firstColor = firstColorButton.innerText.trim();
        const firstColorId = firstColorButton.getAttribute('data-color-id');

        const firstSize = firstSizeButton.innerText.trim();
        const firstSizeId = firstSizeButton.getAttribute('data-size-id');

        // Cập nhật màu và kích thước đầu tiên
        setTemporaryColor(firstColor, firstColorId);
        setTemporarySize(firstSize, firstSizeId);

        // Gọi API lấy chi tiết sản phẩm cho màu và kích thước đầu tiên
        getProductDetail(productId, firstColorId, firstSizeId);
    } else {
        // Nếu không tìm thấy màu hoặc kích thước đầu tiên, vẫn gọi API với các giá trị mặc định (nếu có)
        getProductDetail(productId, selectedColorId, selectedSizeId);
    }

    // Đính kèm sự kiện click vào các nút màu và kích thước
    attachClickEvent('.color-btn', setTemporaryColor);
    attachClickEvent('.size-btn', setTemporarySize);
});

function showCartModal() {
    $('#addCartModal').modal('show');
}

function showPayNowModal() {
    $('#payNowModal').modal('show');
}

function attachClickEvent(selector, handler) {
    const buttons = document.querySelectorAll(selector);
    buttons.forEach(function (button) {
        button.addEventListener("click", function () {
            const value = button.innerText.trim();
            const id = button.getAttribute('data-color-id') || button.getAttribute('data-size-id');
            handler(value, id);
        });
    });
}

function setTemporaryColor(color, colorId) {
    selectedColorId = colorId;
    document.querySelectorAll('.temporary-color').forEach(function (element) {
        element.innerText = color;
    });
    document.getElementById("selected-color").innerText = color;
    document.getElementById("color-modal").innerText = color;

    // Gọi API nếu đã chọn kích thước
    if (selectedSizeId) {
        getProductDetail(productId, selectedColorId, selectedSizeId);
    }
}

function setTemporarySize(size, sizeId) {
    selectedSizeId = sizeId;
    document.querySelectorAll('.temporary-size').forEach(function (element) {
        element.innerText = size;
    });
    document.getElementById("selected-size").innerText = size;
    document.getElementById("size-modal").innerText = size;

    // Gọi API nếu đã chọn màu
    if (selectedColorId) {
        getProductDetail(productId, selectedColorId, selectedSizeId);
    }
}


function getProductDetail(productId, colorId, sizeId) {
    console.log(`Gọi API với productId: ${productId}, colorId: ${colorId}, sizeId: ${sizeId}`);
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
                const quantity = data.quantity || 0;
                $('#quantity-display').text(quantity);
                $('#price-display').text(data.price.toLocaleString('en-US') + " ₫");
                $('#price-apply-discount').text(data.priceDiscount.toLocaleString('en-US') + " ₫");
                $('#price-modal').text(data.priceDiscount.toLocaleString('en-US') + " ₫");
                $('#price-modal-pay-now').text(data.priceDiscount.toLocaleString('en-US') + " ₫");

                $('#productDetailID-hidden').val(data.productDetailId);
                $('#product-name-pay-now').text(data.productName);
                $('#product-name').text(data.productName);
                $('#color-modal').text(data.colorName);
                $('#color-modal-pay-now').text(data.colorName);
                $('#size-modal').text(data.sizeName);
                $('#size-modal-pay-now').text(data.sizeName);

                if (data.price === data.priceDiscount) {
                    $('#price-display').hide();
                } else {
                    $('#price-display').show();
                }

                // Cập nhật trạng thái nút mua hàng
                if (quantity > 0) {
                    $('#btn-add-cart').prop('disabled', false);
                    $('#btn-pay-now').prop('disabled', false);
                } else {
                    $('#btn-add-cart').prop('disabled', true);
                    $('#btn-pay-now').prop('disabled', true);
                }
            } else {
                // Nếu không có dữ liệu, đặt giá về 0 và disable các nút
                // $('#quantity-display').text(0);
                // $('#price-display').text("0 ₫");
                // $('#price-apply-discount').text("0 ₫");
                // $('#price-modal').text("0 ₫");
                // $('#price-modal-pay-now').text("0 ₫");

                $('#btn-add-cart').prop('disabled', true);
                $('#btn-pay-now').prop('disabled', true);
            }
        },
        error: function (xhr) {
            console.log("Dữ liệu đầu vào không được cập nhật");
            console.log("Error" + xhr.responseText);
        }
    });
}


function checkQuantity() {
    const quantityText = $('#quantity-display').text();
    var quantity = parseInt(quantityText);

    if (quantity <= 0) {
        $('#btn-add-cart').prop('disabled', true);
        $('#btn-pay-now').prop('disabled', true);
    } else {
        $('#btn-add-cart').prop('disabled', false);
        $('#btn-pay-now').prop('disabled', false);
    }
}

$(document).ready(function () {
    checkQuantity();
});


function changeColor(button) {
    const color = button.innerText.trim(); // Lấy giá trị màu
    const colorId = button.getAttribute('data-color-id'); // Lấy ID màu

    // Đảm bảo chỉ cập nhật nút trong nhóm màu sắc
    document.querySelectorAll('.btn-color-circle').forEach(btn => {
        btn.classList.remove('thumbnail-active');
    });

    button.classList.add('thumbnail-active'); // Thêm lớp cho nút được nhấn

    // Cập nhật màu sắc hiển thị
    document.getElementById('selected-color').textContent = color;

    // Lưu giá trị tạm thời (nếu cần)
    setTemporaryColor(color, colorId);
}

function changeSize(button) {
    const size = button.innerText.trim();
    const sizeId = button.getAttribute('data-size-id'); // Lấy ID kích thước

    // Đảm bảo chỉ cập nhật nút trong nhóm kích thước
    document.querySelectorAll('.btn-size-square').forEach(btn => {
        btn.classList.remove('thumbnail-active');
    });

    button.classList.add('thumbnail-active');

    // Cập nhật kích thước hiển thị
    document.getElementById('selected-size').textContent = size;

    // Lưu giá trị tạm thời (nếu cần)
    setTemporarySize(size, sizeId);
}

document.addEventListener("DOMContentLoaded", function () {
    const quantityInput = $('input[name="quantity-add-to-cart"]');
    const btnMinus = document.getElementById('qtyminus');
    const btnPlus = document.getElementById('qtyplus');

    // Hàm hiển thị modal giỏ hàng (nếu cần)
    function showCartModal() {
        $('#cartModal').modal('show');
    }

    // Giảm số lượng
    btnMinus.addEventListener("click", function () {
        let quantityBuy = parseInt(quantityInput.val());
        if (isNaN(quantityBuy) || quantityBuy <= 1) {
            quantityBuy = 1;
        } else {
            quantityBuy -= 1;
        }
        quantityInput.val(quantityBuy);
        // quantityDisplay.text(quantityBuy);  // Cập nhật lại số lượng hiển thị trong thẻ <p>
    });

    // Tăng số lượng
    btnPlus.addEventListener("click", function () {
        let quantityBuy = parseInt(quantityInput.val());

        if (isNaN(quantityBuy) || quantityBuy <= 0) {
            quantityBuy = 1;
        }

        const addToCartButton = document.getElementById('add-to-cart');
        // Xóa thông báo lỗi trước đó
        $('#container-message-add-to-cart .message-error-add-to-cart').text("");

        // // Kiểm tra nếu số lượng mua vượt quá số lượng có sẵn
        // if (quantityBuy > quantityNow) {
        //     $('#container-message-add-to-cart .message-error-add-to-cart').text("Số lượng sản phẩm trong kho không đủ.");
        //     quantityInput.val(quantityNow);
        //     addToCartButton.disabled = true;
        // } else
        if (quantityBuy >= 10) {
            $('#container-message-add-to-cart .message-error-add-to-cart').text("Chỉ được mua tối đa 10 sản phẩm cùng loại.");
            quantityInput.val(10);  // Giới hạn tối đa là 10
            addToCartButton.disabled = true;  // Vô hiệu hóa nút thêm vào giỏ hàng
            showCartModal();  // Hiển thị modal lỗi
        } else {
            addToCartButton.disabled = false;
            quantityInput.val(quantityBuy + 1);
            // quantityDisplay.text(quantityBuy + 1);  // Cập nhật lại số lượng hiển thị trong thẻ <p>
        }
    });
});

function addToCart() {
    var productDetailId = $('#productDetailID-hidden').val();
    var quantityBuy = parseInt($('input[name="quantity-add-to-cart"]').val());
    var productName = $('#product-name').text();
    var productPrice = $('#price-modal').text();
    var productColor = $('#color-modal').text();
    var productSize = $('#size-modal').text();
    var productImage = $('.item-img img').attr('src');
    const messagesErrorAddToCart = $('.message-error-add-to-cart');
    messagesErrorAddToCart.text("");

    if (!productDetailId) {
        messagesErrorAddToCart.text("Sản phẩm không tồn tại.");
        showCartModal();
        return;
    }

    if (!Number.isInteger(quantityBuy) || quantityBuy <= 0) {
        messagesErrorAddToCart.text("Số lượng sản phẩm không hợp lệ. Vui lòng nhập một số nguyên lớn hơn 0.");
        showCartModal();
        return;
    }

    if (quantityBuy > 10) {
        messagesErrorAddToCart.text("Không thể thêm quá 10 sản phẩm cùng loại vào giỏ hàng.");
        showCartModal();
        return;
    }

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
                $('#addCartModal').modal('hide');
                $('#success-product-image').attr('src', productImage);
                $('#success-product-name').text(productName);
                $('#success-price-modal').text(productPrice);
                $('#success-color-modal').text(productColor);
                $('#success-size-modal').text(productSize);
                $('#success-quantity-modal').text(quantityBuy);

                var successModal = new bootstrap.Modal(document.getElementById('addCartSuccessModal'));
                successModal.show();
            } else {
                messagesErrorAddToCart.text(data.message || 'Có lỗi xảy ra khi thêm vào giỏ hàng.');
            }
        },
        error: function (error) {
            var errorMessage = error.responseJSON ? error.responseJSON.message : 'Đã xảy ra lỗi không xác định.';
            messagesErrorAddToCart.text(errorMessage);
        }
    });
}


$('#continue-shopping').click(function () {
    $('#successModal').modal('hide');
});
document.addEventListener("DOMContentLoaded", function () {
    const quantityInput = $('input[name="quantity-pay-now"]');
    const quantityDisplay = $('#quantity-display');
    const messageErrorPayNow = $('#message-error-pay-now'); // Lấy phần tử hiển thị thông báo lỗi
    let quantityNow = parseInt(quantityDisplay.text()) || 0;

    const btnMinus = document.getElementById('qtyminus-pay-now');
    const btnPlus = document.getElementById('qtyplus-pay-now');

    function showPayNowModal() {
        $('#payNowModal').modal('show');
    }

    // Giảm số lượng
    btnMinus.addEventListener("click", function () {
        let quantityBuy = parseInt(quantityInput.val());
        if (isNaN(quantityBuy) || quantityBuy <= 1) {
            quantityBuy = 1;
        } else {
            quantityBuy -= 1;
        }
        quantityInput.val(quantityBuy);
        messageErrorPayNow.text(''); // Xóa thông báo lỗi khi giảm số lượng hợp lệ
    });

    // Tăng số lượng
    btnPlus.addEventListener("click", function () {
        let quantityBuy = parseInt(quantityInput.val());

        if (isNaN(quantityBuy) || quantityBuy <= 0) {
            quantityBuy = 1;
        }

        // Kiểm tra giới hạn số lượng
        if (quantityBuy >= 10) {
            messageErrorPayNow.text("Chỉ được mua tối đa 10 sản phẩm cùng loại.");
            quantityInput.val(10);
        } else {
            quantityInput.val(quantityBuy + 1);
            messageErrorPayNow.text(''); // Xóa thông báo lỗi nếu số lượng hợp lệ
        }
    });
});

function buyNow() {
    var productDetailId = $('#productDetailID-hidden').val();
    var quantityBuy = parseInt($('input[name="quantity-pay-now"]').val());
    const messageErrorPayNow = $('#message-error-pay-now');
    messageErrorPayNow.text("");

    if (!productDetailId) {
        messageErrorPayNow.text("Sản phẩm không tồn tại.");
        showPayNowModal();
        return;
    }

    if (!Number.isInteger(quantityBuy) || quantityBuy <= 0) {
        messageErrorPayNow.text("Số lượng sản phẩm không hợp lệ. Vui lòng nhập một số nguyên lớn hơn 0.");
        showPayNowModal();
        return;
    }

    if (quantityBuy > 10) {
        messageErrorPayNow.text("Không thể thêm quá 10 sản phẩm cùng loại vào giỏ hàng.");
        showPayNowModal();
        return;
    }

    $.ajax({
        url: '/api-client/add-to-cart', type: 'POST', contentType: "application/json", data: JSON.stringify({
            productDetailId: productDetailId, quantity: quantityBuy
        }), success: function (data) {
            if (data && data.success) {
                console.log("Mua ngay thành công");
                window.location.href = '/onepoly/cart';
            } else {
                messageErrorPayNow.text(data.message || 'Có lỗi xảy ra khi thêm vào giỏ hàng.'); // Hiển thị thông báo lỗi
            }
        }, error: function (error) {
            var errorMessage = error.responseJSON ? error.responseJSON.message : 'Đã xảy ra lỗi không xác định.';
            messageErrorPayNow.text(errorMessage); // Hiển thị thông báo lỗi
        }
    });
}




