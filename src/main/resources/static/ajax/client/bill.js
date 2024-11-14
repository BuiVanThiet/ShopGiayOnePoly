function payBill() {
    const addressShip = $('#addressShip').val();
    const shippingPriceText = $('#spanShippingFee').text().trim();
    const voucherPriceText = $('#spanPriceVoucher').text().trim();
    const totalAmountBillText = $('#spanTotalPriceCartItem').text().trim(); // Lấy giá trị từ thẻ spanTotalPriceBill
    const noteBill = $('#noteBill').val();

    const shippingPrice = parseFloat(shippingPriceText.replace(/[^0-9.-]+/g, ''));
    const priceVoucher = parseFloat(voucherPriceText.replace(/[^0-9.-]+/g, ''));
    const totalAmountBill = parseFloat(totalAmountBillText.replace(/[^0-9.-]+/g, '')); // Chuyển đổi thành số

    // In ra console để kiểm tra giá trị
    console.log("Địa chỉ giao hàng: " + addressShip);
    console.log("Giá vận chuyển: " + shippingPrice);
    console.log("Giá giảm: " + priceVoucher);
    console.log("Tổng số tiền hóa đơn: " + totalAmountBill);

    if (isNaN(shippingPrice)) {
        alert("Giá vận chuyển không hợp lệ.");
        return;
    }
    if (isNaN(priceVoucher)) {
        alert("Giá giảm không hợp lệ.");
        return;
    }
    if (isNaN(totalAmountBill)) {
        alert("Tổng số tiền hóa đơn không hợp lệ.");
        return;
    }

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
            window.location.href = '/client/bill_customer';
        },
        error: function (error) {
            alert("Thanh toán thất bại. Vui lòng thử lại.");
            console.error(error);
        }
    });
}
