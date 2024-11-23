function payBill() {
    const addressShip = $('#addressShip').val();
    const shippingPriceText = $('#spanShippingFee').text().trim();
    const voucherPriceText = $('#spanPriceVoucher').text().trim();
    const totalAmountBillText = $('#spanTotalPriceCartItem').text().trim(); // Lấy giá trị từ thẻ spanTotalPriceBill
    const noteBill = $('#noteBill').val();

    const selectedRadioPaymentMethod = document.querySelector('input[name="payment_method_id"]:checked');

    if (selectedRadioPaymentMethod) {
        console.log('Selected payment method value: ' + selectedRadioPaymentMethod.value);
    } else {
        // Nếu không có radio button nào được chọn
        console.log('No payment method selected');
    }


    let shippingPrice = parseFloat(shippingPriceText.replace(/[^0-9.-]+/g, ''));
    let priceVoucher = parseFloat(voucherPriceText.replace(/[^0-9.-]+/g, ''));
    let totalAmountBill = parseFloat(totalAmountBillText.replace(/[^0-9.-]+/g, '')); // Chuyển đổi thành số
    shippingPrice = isNaN(shippingPrice) ? 0 : shippingPrice;
    priceVoucher = isNaN(priceVoucher) ? 0 : priceVoucher;
    totalAmountBill = isNaN(totalAmountBill) ? 0 : totalAmountBill;
    let payMethod = selectedRadioPaymentMethod.value;
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
            noteBill: noteBill,
            payMethod: payMethod
        }),
        success: function (response) {
            window.location.href = response;
        },
        error: function (error) {
            alert("Thanh toán thất bại. Vui lòng thử lại.");
            console.error(error);
        }
    });
}
