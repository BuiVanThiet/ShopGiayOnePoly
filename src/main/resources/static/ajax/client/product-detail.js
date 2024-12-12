let selectedColorId;
let selectedSizeId;
const productId = document.getElementById("product-id").value;

document.addEventListener("DOMContentLoaded", function () {
    const firstColorButton = document.querySelector('.color-btn');
    const firstSizeButton = document.querySelector('.size-btn');

    // Kiểm tra và xử lý nút màu và kích thước đầu tiên
    if (firstColorButton && firstSizeButton) {
        const firstColor = firstColorButton.innerText.trim();
        selectedColorId = firstColorButton.getAttribute('data-color-id');

        const firstSize = firstSizeButton.innerText.trim();
        selectedSizeId = firstSizeButton.getAttribute('data-size-id');

        console.log("Color ID:", selectedColorId);
        console.log("Size ID:", selectedSizeId);
        console.log("Product ID:", productId);

        setTemporaryColor(firstColor, selectedColorId);
        setTemporarySize(firstSize, selectedSizeId);

        getProductDetail(productId, selectedColorId, selectedSizeId);
    } else {
        console.warn("Không tìm thấy màu hoặc kích thước đầu tiên.");
    }

    // Đính kèm sự kiện click
    attachClickEvent('.color-btn', setTemporaryColor, 'data-color-id');
    attachClickEvent('.size-btn', setTemporarySize, 'data-size-id');
});

function attachClickEvent(selector, handler, dataAttr) {
    const buttons = document.querySelectorAll(selector);
    buttons.forEach(function (button) {
        button.addEventListener("click", function () {
            const value = button.innerText.trim();
            const id = button.getAttribute(dataAttr);
            if (id) {
                handler(value, id);
            } else {
                console.warn(`Không tìm thấy giá trị ${dataAttr} trên nút.`);
            }
        });
    });
}

function setTemporaryColor(color, colorId) {
    selectedColorId = colorId;

    // Cập nhật giao diện
    updateUI('.temporary-color', color);
    updateTextById("selected-color", color);
    updateTextById("color-modal", color);

    // Gọi API nếu đã chọn kích thước
    if (selectedSizeId) {
        getProductDetail(productId, selectedColorId, selectedSizeId);
    }
}

function setTemporarySize(size, sizeId) {
    selectedSizeId = sizeId;

    // Cập nhật giao diện
    updateUI('.temporary-size', size);
    updateTextById("selected-size", size);
    updateTextById("size-modal", size);

    // Gọi API nếu đã chọn màu
    if (selectedColorId) {
        getProductDetail(productId, selectedColorId, selectedSizeId);
    }
}

function updateUI(selector, text) {
    const elements = document.querySelectorAll(selector);
    elements.forEach(function (element) {
        element.innerText = text;
    });
}

function updateTextById(id, text) {
    const element = document.getElementById(id);
    if (element) {
        element.innerText = text;
    } else {
        console.warn(`Không tìm thấy phần tử với ID: ${id}`);
    }
}

function showCartModal() {
    $('#addCartModal').modal('show');
}

function showPayNowModal() {
    $('#payNowModal').modal('show');
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

                $('#material-name').text(data.materialName);
                $('#manufacturer-name').text(data.manufacturerName);
                $('#origin-name').text(data.originName);
                $('#description-product').text(data.productDetailDescription);

                if (data.price === data.priceDiscount) {
                    $('#price-display').hide();
                } else {
                    $('#price-display').show();
                }

                if (quantity > 0) {
                    $('#btn-add-cart').prop('disabled', false);
                    $('#btn-pay-now').prop('disabled', false);
                } else {
                    $('#btn-add-cart').prop('disabled', true);
                    $('#btn-pay-now').prop('disabled', true);
                }
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
    const discountType = document.getElementById("discount-type-new").value;
    const discountValue = document.getElementById("voucher-value-new").innerText;
    const valueTypeSpan = document.getElementById("value-type");
    const voucherValueSpan = document.getElementById("voucher-value-new");
    console.log("Type vouccher: " + discountType)
    if (discountType === "1") {
        // Nếu discountType = 1 (giảm theo %)
        valueTypeSpan.innerText = "%";
        voucherValueSpan.innerText = Math.round(discountValue.toLocaleString("en-US"));
    } else if (discountType === "2") {
        // Nếu discountType = 2 (giảm theo VND)
        valueTypeSpan.innerText = "₫";
        voucherValueSpan.innerText = parseFloat(discountValue).toLocaleString('en-US');
    }

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




