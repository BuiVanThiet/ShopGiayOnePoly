// Khi áp dụng voucher và cập nhật giá trị trực tiếp
function applyVoucher() {
    const selectedRadio = document.querySelector('input[name="radioVoucher"]:checked');
    if (selectedRadio) {
        const voucherId = selectedRadio.closest('.cart-voucher-item').querySelector('.voucher-id').textContent.trim();
        console.log("ID Voucher: " + voucherId);

        const totalPrice = parseFloat(document.getElementById('totalPriceCartItem').textContent.trim());

        // Gửi yêu cầu AJAX để lấy thông tin voucher
        $.ajax({
            url: `/api-client/selected-voucher/${voucherId}`,
            method: 'GET',
            success: function (data) {
                if (data) {
                    alert('Voucher đã được chọn thành công!');

                    const discountType = data.voucherType;
                    const discountValue = parseFloat(data.priceReduced);

                    let priceReduced = 0;
                    if (discountType === 1) {  // Giảm theo %
                        priceReduced = totalPrice * (discountValue / 100);
                    } else if (discountType === 2) {  // Giảm theo số tiền cố định
                        priceReduced = discountValue;
                    }

                    const finalPrice = totalPrice - priceReduced;

                    // Cập nhật giao diện ngay lập tức
                    document.getElementById('priceVoucherReduced').textContent = priceReduced.toFixed(2) + ' đ';
                    document.getElementById('cart-spanTotalPriceCart').textContent = finalPrice.toFixed(2) + ' ₫';
                    document.getElementById('price-Calculator').textContent = finalPrice.toFixed(2) + ' ₫';

                    // Lưu dữ liệu voucher vào sessionStorage
                    sessionStorage.setItem('priceVoucherReduced', priceReduced.toFixed(2));

                    // Tải lại trang sau khi lưu vào sessionStorage
                    setTimeout(function () {
                        window.location.reload();
                    }, 300);
                } else {
                    alert('Voucher không tồn tại hoặc đã hết hạn.');
                }
            },
            error: function (error) {
                console.error('Lỗi:', error);
                alert('Có lỗi xảy ra khi chọn voucher.');
            }
        });
    }
}
window.addEventListener('load', function () {
    // Lấy giá trị giảm từ sessionStorage
    const priceVoucherReduced = parseFloat(sessionStorage.getItem('priceVoucherReduced'));
    const totalPrice = parseFloat(document.getElementById('totalPriceCartItem').textContent.trim());

    if (priceVoucherReduced) {
        // Tính lại giá sau khi áp dụng giảm giá
        const finalPrice = totalPrice - priceVoucherReduced;

        // Cập nhật giao diện với giá trị tính toán lại
        document.getElementById('priceVoucherReduced').textContent = priceVoucherReduced.toFixed(2) + ' đ';
        document.getElementById('cart-spanTotalPriceCart').textContent = finalPrice.toFixed(2) + ' ₫';
        document.getElementById('price-Calculator').textContent = finalPrice.toFixed(2) + ' ₫';

        // Xóa dữ liệu voucher khỏi sessionStorage sau 2 giây
        setTimeout(function () {
            sessionStorage.removeItem('priceVoucherReduced');
        }, 2000);
    }
});

function toggleVoucherList() {
    const voucherList = document.getElementById('voucher-list');
    const voucherButtonText = document.getElementById('voucher-button-text');
    const voucherButtonImg = document.getElementById('voucher-button-img');

    if (voucherList.style.display === 'none') {
        // Hiển thị danh sách và ẩn ảnh
        voucherList.style.display = 'block';
        voucherButtonImg.style.display = 'none';  // Ẩn ảnh
        voucherButtonText.textContent = "Đóng danh sách";  // Thay đổi text
    } else {
        // Ẩn danh sách và hiển thị ảnh
        voucherList.style.display = 'none';
        voucherButtonImg.style.display = 'inline-block';  // Hiển thị ảnh
        voucherButtonText.textContent = "Áp dụng voucher";  // Thay đổi text về ban đầu
    }
}

function removeFromCart(idProductDetailFromCart) {
    fetch(`/onepoly/remove-from-cart/${idProductDetailFromCart}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            // Cập nhật lại tổng giá trị giỏ hàng
            const totalPrice = data.totalPrice;
            const priceReduced = data.priceReduced;
            const cartItems = data.cartItems;

            // Cập nhật tổng giá trị vào giao diện
            document.getElementById('totalPriceCartItem').textContent = totalPrice + ' ₫';

            // Cập nhật lại giá trị giảm giá (nếu có)
            if (priceReduced !== '0') {
                document.getElementById('priceVoucherReduced').textContent = priceReduced + ' ₫';
            } else {
                document.getElementById('priceVoucherReduced').textContent = '0 ₫';
            }

            // Cập nhật lại danh sách sản phẩm trong giỏ hàng
            const cartItemsContainer = document.getElementById('voucher-list');
            cartItemsContainer.innerHTML = ''; // Xóa danh sách cũ

            cartItems.forEach(item => {
                const productDiv = document.createElement('div');
                productDiv.textContent = `${item.productDetail.name} - ${item.quantity} x ${item.productDetail.price} ₫`;
                cartItemsContainer.appendChild(productDiv);
            });

            // Nếu giỏ hàng trống, hiển thị thông báo giỏ hàng rỗng
            if (cartItems.length === 0) {
                cartItemsContainer.innerHTML = '<p>Giỏ hàng của bạn đang trống.</p>';
            }

        })
        .catch(error => console.error('Error removing product from cart:', error));
}

function removeProductDetailFromCart(button) {
    // Tìm đến phần tử <input> có id là 'idProductDetailFromCart' trong cùng một div với nút xóa
    const cartItemDiv = button.closest('.cart-item');  // Tìm div chứa sản phẩm
    const productDetailId = cartItemDiv.querySelector('#idProductDetailFromCart').value; // Lấy giá trị của hidden input

    // In ra giá trị của ID sản phẩm trong giỏ
    console.log("ID sản phẩm trong giỏ hàng: " + productDetailId);

    // Gọi AJAX để xóa sản phẩm khỏi giỏ
    removeFromCart(productDetailId);
}


// Hàm cập nhật số lượng sản phẩm trong giỏ hàng
function updateQuantity() {
    const productDetailIdText = document.getElementById("idProductDetailFromCart").value;  // Sử dụng .value để lấy đúng giá trị của input hidden
    const idProductDetailFromCart = parseInt(productDetailIdText.trim());

    const quantityItemText = document.getElementById("quantityProductFormCart").innerText;  // Sử dụng .innerText để lấy giá trị hiển thị
    const quantityItem = parseInt(quantityItemText.trim());

    // Kiểm tra nếu quantityItem không phải là một số hợp lệ
    if (isNaN(quantityItem) || isNaN(idProductDetailFromCart)) {
        console.error("Số lượng hoặc ID sản phẩm không hợp lệ.");
        alert("Số lượng hoặc ID sản phẩm không hợp lệ.");
        return;
    }

    $.ajax({
        url: `/api-client/update-from-cart/${idProductDetailFromCart}`,
        type: 'POST',
        data: { quantityItem: quantityItem },
        success: function (response) {
            console.log(response);
        },
        error: function (error) {
            console.error('Error updating quantity:', error.responseText || error);
            alert('Có lỗi xảy ra khi cập nhật số lượng.');
        }
    });
}

document.addEventListener("DOMContentLoaded", function () {
    // Hàm cập nhật số lượng và tính toán lại giá
    function updateQuantity(button, change) {
        const quantityElem = button.parentNode.querySelector("#quantityProductFormCart");
        let quantity = parseInt(quantityElem.innerText);

        // Cập nhật số lượng
        quantity += change;

        if (quantity < 1) {
            quantity = 1;  // Đảm bảo không giảm dưới 1
        } else if (quantity > 10) {
            alert("Chỉ được mua tối đa 10 sản phẩm cùng loại.");
            quantity = 10;
        }

        // Cập nhật giá trị hiển thị số lượng
        quantityElem.innerText = quantity;

        // Cập nhật lại giá sản phẩm và tổng tiền
        updateCartTotal();
    }

    // Hàm tính toán lại tổng tiền giỏ hàng
    function updateCartTotal() {
        let totalPrice = 0;

        // Lặp qua từng sản phẩm trong giỏ để tính tổng tiền
        document.querySelectorAll(".cart-item").forEach(item => {
            const priceElem = item.querySelector("#cart-spanPriceCartItem");
            const quantityElem = item.querySelector("#quantityProductFormCart");

            const price = parseFloat(priceElem.innerText.replace(/₫|,/g, ""));
            const quantity = parseInt(quantityElem.innerText);

            totalPrice += price * quantity;
        });

        // Hiển thị tổng tiền và giá cuối cùng
        document.getElementById("totalPriceCartItem").innerText = totalPrice.toLocaleString("vi-VN") + " ₫";

        // Thay đổi giá giảm và giá cuối cùng
        const priceReduced = totalPrice * 0.1; // Ví dụ giảm 10%
        const finalPrice = totalPrice - priceReduced;

        document.getElementById("priceVoucherReduced").innerText = priceReduced.toLocaleString("vi-VN") + " ₫";
        document.getElementById("price-Calculator").innerText = finalPrice.toLocaleString("vi-VN") + " ₫";
        document.getElementById("cart-spanTotalPriceCart").innerText = finalPrice.toLocaleString("vi-VN") + " ₫";
    }

    // Gắn sự kiện cho các nút cộng và trừ
    document.querySelectorAll(".cart-qtyminus").forEach(button => {
        button.addEventListener("click", function () {
            updateQuantity(this, -1);
        });
    });

    document.querySelectorAll(".cart-qtyplus").forEach(button => {
        button.addEventListener("click", function () {
            updateQuantity(this, 1);
        });
    });
});


