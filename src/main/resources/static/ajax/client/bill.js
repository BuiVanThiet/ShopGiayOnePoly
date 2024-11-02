function payBill() {
    const address = $('#addressShip').val();
    const shippingPrice = $('#spanShippingFee').val();

    $.ajax({
        url: '/onepoly/payment',
        type: 'POST',
        data: {
            address: address,
            shippingPrice: shippingPrice
        },
        success: function(response) {
            window.location.href = '/client/bill_customer';
        },
        error: function(error) {
            // Xử lý khi có lỗi
            alert("Thanh toán thất bại. Vui lòng thử lại.");
            console.error(error);
        }
    });
}