function payBill() {
    const addressShip = $('#addressShip').val();
    const shippingPriceText = $('#spanShippingFee').text().trim();
    const voucherPriceText = $('#spanPriceVoucher').text().trim();
    const totalAmountBillText = $('#spanTotalPriceCartItem').text().trim(); // Lấy giá trị từ thẻ spanTotalPriceBill
    const noteBill = $('#noteBill').val();

    let shippingPrice = parseFloat(shippingPriceText.replace(/[^0-9.-]+/g, ''));
    let priceVoucher = parseFloat(voucherPriceText.replace(/[^0-9.-]+/g, ''));
    let totalAmountBill = parseFloat(totalAmountBillText.replace(/[^0-9.-]+/g, '')); // Chuyển đổi thành số

    // Kiểm tra và thiết lập giá trị mặc định là 0 nếu không hợp lệ
    shippingPrice = isNaN(shippingPrice) ? 0 : shippingPrice;
    priceVoucher = isNaN(priceVoucher) ? 0 : priceVoucher;
    totalAmountBill = isNaN(totalAmountBill) ? 0 : totalAmountBill;

    // In ra console để kiểm tra giá trị
    console.log("Địa chỉ giao hàng: " + addressShip);
    console.log("Giá vận chuyển: " + shippingPrice);
    console.log("Giá giảm: " + priceVoucher);
    console.log("Tổng số tiền hóa đơn: " + totalAmountBill);

    $.ajax({
        url: '/onepoly/payment',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            addressShip: addressShip,
            priceVoucher: priceVoucher,
            shippingPrice: shippingPrice,
            totalAmountBill: totalAmountBill,
            noteBill: noteBill
        }),
        success: function (response) {
            window.location.href = '/onepoly/order-success';
        },
        error: function (error) {
            alert("Thanh toán thất bại. Vui lòng thử lại.");
            console.error(error);
        }
    });
}
