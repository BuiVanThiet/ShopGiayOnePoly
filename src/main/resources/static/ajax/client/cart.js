// Khi áp dụng voucher và lưu vào sessionStorage
function applyVoucher() {
    // Lấy voucher được chọn
    const selectedRadio = document.querySelector('input[name="radioVoucher"]:checked');

    if (selectedRadio) {
        // Lấy ID voucher từ phần tử span ẩn
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

                    // Lấy thông tin voucher
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

                    // Lưu giá trị giảm và tổng tiền vào sessionStorage cùng với thời gian hiện tại
                    sessionStorage.setItem('priceVoucherReduced', priceReduced.toFixed(2));
                    sessionStorage.setItem('finalPrice', finalPrice.toFixed(2));
                    sessionStorage.setItem('voucherTimestamp', new Date().getTime().toString());

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

// Khi trang tải lại, lấy giá trị từ sessionStorage và hiển thị
window.addEventListener('load', function () {
    const priceVoucherReduced = sessionStorage.getItem('priceVoucherReduced');
    const finalPrice = sessionStorage.getItem('finalPrice');
    const voucherTimestamp = sessionStorage.getItem('voucherTimestamp');

    // Kiểm tra xem dữ liệu có tồn tại và không quá 30 phút
    if (priceVoucherReduced && finalPrice && voucherTimestamp) {
        const currentTime = new Date().getTime();
        const timeDiff = (currentTime - parseInt(voucherTimestamp)) / 1000 / 60; // thời gian tính bằng phút

        if (timeDiff <= 30) { // Nếu trong vòng 30 phút
            // Cập nhật giao diện
            document.getElementById('priceVoucherReduced').textContent = priceVoucherReduced + ' đ';
            document.getElementById('cart-spanTotalPriceCart').textContent = finalPrice + ' ₫';
            document.getElementById('price-Calculator').textContent = finalPrice + ' ₫';
        } else {
            // Nếu quá 30 phút, xóa dữ liệu khỏi sessionStorage
            sessionStorage.removeItem('priceVoucherReduced');
            sessionStorage.removeItem('finalPrice');
            sessionStorage.removeItem('voucherTimestamp');
        }
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
    // Lấy idProductDetail từ input ẩn trong phần tử chứa nút xóa
    const idProductDetail = $(button).siblings('#idProductDetailFromCart').val();

    // Tiến hành gọi API xóa sản phẩm khỏi giỏ hàng
    $.ajax({
        url: `/onepoly/remove-from-cart/${idProductDetail}`,
        method: 'GET',
        success: function (data) {
            // Xóa sản phẩm khỏi DOM khi xóa thành công
            $(button).closest('.cart-item').remove();
        },
        error: function (xhr, status, error) {
            console.error('Error removing product from cart:', error);
        }
    });
}
