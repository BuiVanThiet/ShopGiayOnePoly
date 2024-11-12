function applyVoucher() {
    const selectedRadio = document.querySelector('input[name="radioVoucher"]:checked');

    if (selectedRadio) {
        const voucherId = selectedRadio.closest('.cart-voucher-item').querySelector('.voucher-id').textContent.trim();
        console.log("ID Voucher: " + voucherId);

        const totalPrice = parseFloat(document.getElementById('totalPriceCartItem').textContent.trim());

        // Gửi yêu cầu AJAX
        $.ajax({
            url: `/api-client/selected-voucher/${voucherId}`,
            method: 'GET',
            success: function (data) {
                if (data) {
                    alert('Voucher đã được chọn thành công!');

                    const discountType = data.voucherType;
                    const discountValue = parseFloat(data.priceReduced);

                    // Tính toán giá trị giảm và tổng tiền sau khi giảm
                    let priceReduced = 0;
                    if (discountType === 1) {  // Giảm theo %
                        priceReduced = totalPrice * (discountValue / 100);
                    } else if (discountType === 2) {  // Giảm theo số tiền cố định
                        priceReduced = discountValue;
                    }

                    const finalPrice = totalPrice - priceReduced;

                    // Định dạng giá trị và thêm chữ "đ"
                    const formattedPriceReduced = priceReduced.toLocaleString('vi-VN') + " đ";
                    const formattedFinalPrice = finalPrice.toLocaleString('vi-VN') + " đ";

                    // Lưu giá trị giảm và tổng tiền vào sessionStorage
                    sessionStorage.setItem('priceVoucherReduced', formattedPriceReduced);
                    sessionStorage.setItem('finalPrice', formattedFinalPrice);

                    // Tải lại trang sau 500ms để cập nhật giao diện
                    setTimeout(function () {
                        window.location.reload();
                    }, 500);
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


document.addEventListener("DOMContentLoaded", function () {
    const priceVoucherReduced = sessionStorage.getItem('priceVoucherReduced');
    const finalPrice = sessionStorage.getItem('finalPrice');

    if (priceVoucherReduced && finalPrice) {
        // Hiển thị giá trị giảm và tổng tiền sau giảm
        document.getElementById('priceVoucherReduced').textContent = priceVoucherReduced;
        document.getElementById('price-Calculator').textContent = finalPrice;
        document.getElementById('cart-spanTotalPriceCart').textContent = finalPrice;
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

function removeProductDetailFromCart(button) {
    // Lấy idProductDetail từ input ẩn gần nhất chứa nút xóa
    const idProductDetail = $(button).closest('.cart-item').find('#idProductDetailFromCart').val();

    // Kiểm tra xem idProductDetail có hợp lệ không
    if (!idProductDetail) {
        console.error('idProductDetail is undefined');
        return;
    }

    // Tiến hành gọi API xóa sản phẩm khỏi giỏ hàng
    $.ajax({
        url: `/onepoly/remove-from-cart/${idProductDetail}`,
        method: 'POST',
        success: function (data) {
            $(button).closest('.cart-item').remove();
        },
        error: function (xhr, status, error) {
            console.error('Error removing product from cart:', error);
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
