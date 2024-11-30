$('#cashClient-billInfo').on('input', function() {
    var cashInput = $(this);
    var priceBill = Number($('#cashBillPay').val());
    var payMethod = Number($('#payMethod').val());
    validateCashInBillManage(cashInput,priceBill,payMethod);
});

$('#cashClient-billInfo-exchange').on('input', function() {
    var cashInput = $(this);
    var priceBill = Number($('#cashBillPayExchange').val());
    var payMethod = Number($('#payMethodExchange').val());
    validateCashInBillManage(cashInput,priceBill,payMethod);
});

    function validateCashInBillManage(valueInput,priceBill,payMethod) {
        var inputElement = valueInput;
        var rawValue = inputElement.val().replace(/,/g, ''); // Loại bỏ dấu phẩy để lấy giá trị thực
        var priceBill = priceBill;
        var payMethod = payMethod;
        // truyen du lieu
        $('#cashPay').val(rawValue);
        $('#cashPayExchange').val(rawValue);
        // Kiểm tra nếu giá trị nhập vào là số
        if ($.isNumeric(rawValue)) {
            // Định dạng lại giá trị với dấu phẩy mỗi 3 chữ số
            var formattedValue = Number(rawValue).toLocaleString('en');
            inputElement.val(formattedValue); // Hiển thị giá trị với dấu phẩy
            var priceNumber = Number(rawValue);
            // nếu giá thấp hơn giá trị bill
            if(priceNumber < priceBill && payMethod == 1) {
                $('#formErorrCash-billInfo').css('display', 'block');
                $('#erorrCash-billInfo').text('*Giá tiền ít nhất phải bằng giá hóa đơn!');
                $('#btnPaymentInBill').attr('disabled', true);
                $('#surplusMoneySpan-billInfo').css('display', 'none');
                $('#surplusMoney-billInfo').text('');
            }else if (priceNumber < 1000 && payMethod == 3) {
                $('#formErorrCash-billInfo').css('display', 'block');
                $('#erorrCash-billInfo').text('*Giá tiền ít nhất phải 1,000 VNĐ!');
                $('#btnPaymentInBill').attr('disabled', true);
                $('#surplusMoneySpan-billInfo').css('display', 'none');
                $('#surplusMoney-billInfo').text('');
            } else if ((priceBill-priceNumber) < 10000 && payMethod == 3) {
                $('#formErorrCash-billInfo').css('display', 'block');
                $('#erorrCash-billInfo').text('*Giá tiền ít nhất phải 10,000 VND mới có th!');
                $('#btnPaymentInBill').attr('disabled', true);
                $('#surplusMoneySpan-billInfo').css('display', 'none');
                $('#surplusMoney-billInfo').text('');
            }else {
                // Thực hiện kiểm tra giá trị hợp lệ
                $('#formErorrCash-billInfo').css('display', 'none');
                $('#erorrCash-billInfo').text('');
                $('#btnPaymentInBill').attr('disabled', false);
                console.log("Dữ liệu nhập vào là số: " + rawValue); // Giá trị thực không có dấu phẩy
                if(rawValue > priceBill && payMethod == 1) {
                    $('#surplusMoneySpan-billInfo').css('display', 'block');
                    $('#surplusMoney-billInfo').text((priceNumber-priceBill).toLocaleString('en')+'VND');
                    $('#surplusMoneyPay').val(rawValue - priceBill);
                    $('#surplusMoneyPayExchange').val(rawValue - priceBill);
                }else {
                    $('#surplusMoneySpan-billInfo').css('display', 'none');
                    $('#surplusMoney-billInfo').text('0.00');
                    $('#surplusMoneyPay').val('0.00');
                    $('#surplusMoneyPayExchange').val('0.00');
                    $('#cashAcountPay').val((priceBill-priceNumber))
                    $('#cashAcountPayExchange').val((priceBill-priceNumber))

                }
            }

            if (priceNumber > 10000000000) {
                $('#formErorrCash-billInfo').css('display', 'block');
                $('#erorrCash-billInfo').text('**Đã vượt quá giới hạn(trên 10 tỷ VNĐ)!');
                $('#btnPaymentInBill').attr('disabled', true);
                $('#surplusMoneySpan-billInfo').css('display', 'none');
                $('#surplusMoney-billInfo').text('');
            }
        } else {
            $('#formErorrCash-billInfo').css('display', 'block');
            $('#erorrCash-billInfo').text('*Giá trị nhập vào phải là số!');
            $('#btnPaymentInBill').attr('disabled', true);
            $('#surplusMoneySpan-billInfo').css('display', 'none');
            $('#surplusMoney-billInfo').text('');
            console.log("Dữ liệu nhập vào không phải là số");
        }
    }
$('#notePayment-billInfo').on('input', function() {
    $('#notePay').val($(this).val());
    $('#notePayExchange').val($(this).val());
});

//set nut phuong thuc thanh toán
function setActivePayment(element,value) {
    // Xóa lớp active khỏi tất cả các liên kết
    var links = document.querySelectorAll('.btn-outline-primary');
    links.forEach(function(link) {
        link.classList.remove('active');
    });
    element.classList.add('active');
    $('#payMethod').val(value)
    $('#payMethodExchange').val(value)
    if(value == 1) {
        $('#formMoney-billInfo').css('display', 'block');
        $('#formErorrCash-billInfo').css('display', 'block');
        $('#btnPaymentInBill').attr('disabled', true);
        $('#cashAcountPay').val('0.00')
        $('#cashAcountPayExchange').val('0.00')
        setUpPayment();
    }else if ( value == 3){
        $('#formMoney-billInfo').css('display', 'block');
        $('#formErorrCash-billInfo').css('display', 'block');
        $('#btnPaymentInBill').attr('disabled', true);
        $('#cashAcountPay').val('0.00')
        $('#cashAcountPayExchange').val('0.00')
        setUpPayment();
    }else {
        setUpPayment();
        $('#formMoney-billInfo').css('display', 'none');
        $('#formErorrCash-billInfo').css('display', 'none');
        $('#btnPaymentInBill').attr('disabled', false);
        $('#surplusMoneySpan-billInfo').css('display', 'none');
        $('#cashClient-billInfo').val('');
        $('#cashClient-billInfo-exchange').val('');
        $('#cashAcountPay').val($('#cashBillPay').val())
        $('#cashAcountPayExchange').val($('#cashBillPayExchange').val())
        $('#surplusMoneyPay').val('0.00');
        $('#surplusMoneyPayExchange').val('0.00');
    }
}

function setUpPayment() {
    $('#btnPaymentInBill').attr('disabled', true);
    $('#formErorrCash-billInfo').css('display', 'block');
    $('#erorrCash-billInfo').text('*Mời nhập đủ giá!');
    $('#btnPaymentInBill').attr('disabled', true);
    $('#surplusMoneySpan-billInfo').css('display', 'none');
    $('#surplusMoney-billInfo').text('');
    // du lieu
    $('#cashPay').val('0.00');
    $('#cashAcountPay').val('0.00');
    $('#surplusMoneyPay').val('0.00');
    $('#cashClient-billInfo').val('');
    //du lieu exchange
    $('#cashPayExchange').val('0.00');
    $('#cashAcountPayExchange').val('0.00');
    $('#surplusMoneyPayExchange').val('0.00');
    $('#cashClient-billInfo-exchange').val('');
}

$(document).ready(function () {
   setUpPayment();
    $('#payMethod').val('1');
    $('#payMethodExchange').val('1');
    $('#notePay').val('');
    $('#notePayExchange').val('');
})