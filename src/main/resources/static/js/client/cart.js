// Hàm tính tổng giá sản phẩm trong giỏ hàng
function calculateTotalPrice() {
    let totalPrice = 0;
    // Lặp qua các sản phẩm trong giỏ hàng và tính tổng
    document.querySelectorAll('.cart-item').forEach(function(item) {
        let price = parseFloat(item.querySelector('.cart-price-item').innerText.replace('₫', '').trim());
        let quantity = parseInt(item.querySelector('.cart-item-quantity').innerText);
        totalPrice += price * quantity;
    });

    // Cập nhật tổng tiền
    document.getElementById('totalPriceCartItem').innerText = totalPrice.toLocaleString('vi-VN') + " đ";
    return totalPrice;
}

// Hàm tính giảm giá từ voucher
function calculateVoucherDiscount(totalPrice) {
    let discount = 0;
    // Lấy voucher được chọn
    let selectedVoucher = document.querySelector('input[name="radioVoucher"]:checked');
    if (selectedVoucher) {
        let discountType = selectedVoucher.closest('.cart-voucher-item').querySelector('.voucher-type').innerText;
        let discountValue = parseFloat(selectedVoucher.closest('.cart-voucher-item').querySelector('.voucher-discount-voucher').innerText.replace('₫', '').trim());

        if (discountType === '1') {
            discount = totalPrice * (discountValue / 100);
        } else if (discountType === '2') {
            discount = discountValue;
        }

        // Cập nhật giá giảm
        document.getElementById('priceVoucherReduced').innerText = discount.toLocaleString('vi-VN') + " đ";
    }
    return discount;
}

// Hàm tính giá cuối cùng
function calculateFinalPrice() {
    let totalPrice = calculateTotalPrice();
    let discount = calculateVoucherDiscount(totalPrice);
    let finalPrice = totalPrice - discount;

    // Cập nhật giá cuối cùng
    document.getElementById('price-Calculator').innerText = finalPrice.toLocaleString('vi-VN') + " đ";
}

// Gọi hàm khi thay đổi giỏ hàng hoặc chọn voucher
document.addEventListener('DOMContentLoaded', function() {
    // Cập nhật giá khi tải trang
    calculateFinalPrice();

    // Tính lại khi thay đổi số lượng sản phẩm trong giỏ hàng
    document.querySelectorAll('.cart-qty-btn').forEach(function(button) {
        button.addEventListener('click', function() {
            calculateFinalPrice();
        });
    });

    // Tính lại khi chọn hoặc thay đổi voucher
    document.querySelectorAll('.cart-voucher-radio').forEach(function(voucherRadio) {
        voucherRadio.addEventListener('change', function() {
            calculateFinalPrice();
        });
    });
});







