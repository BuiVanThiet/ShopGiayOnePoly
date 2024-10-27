// var totalAmount = document.getElementById('totalCash');
// var cashClient = document.getElementById('cashClient');
// var btnCreateBill = document.getElementById('createBill');
// var erorrCash = document.getElementById('erorrCash');
// var formErorrCash = document.getElementById('formErorrCash');
// var payMethod = document.getElementById('payMethod');
// var formPayMethod = document.getElementById('formPay');
//
// var surplusMoney = document.getElementById('surplusMoney');
// var surplusMoneySpan = document.getElementById('surplusMoneySpan');
// var textSurplusMoney = document.getElementById('textSurplusMoney');
// //
// var cashClientText = document.getElementById('cashClientText');
// var cashAccount = document.getElementById('cashAccount');
var checkButonCreateBill = true;
var checkTypeBill = true;
function formatNumber(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}
//
function parseNumber(value) {
    // Xóa tất cả dấu phân cách và chuyển thành số
    return parseFloat(value.replace(/,/g, '')) || 0;
}
//
var payMethodChecked = parseInt(payMethod.value);
//
//
if(payMethodChecked === 1 || payMethodChecked === 3) {
    document.getElementById('formAtTheSpot').style.display = 'block';
}else {
    console.log(payMethodChecked)
    if(btnCreateBill != null) {
        btnCreateBill.disabled = false;
    }
    document.getElementById('formAtTheSpot').style.display = 'none';
}

if (cashClient.value.trim() === "" && payMethodChecked === 1 || payMethodChecked === 3) {
    formErorrCash.style.display = 'block';
    erorrCash.innerText = 'Mời nhập đủ giá!';
    btnCreateBill.disabled = true;
    surplusMoneySpan.style.display = 'none';
    surplusMoney.innerText = '';
    textSurplusMoney.value = '0';
    cashClientText.value = '0';
}

function validate(cash) {

    var cashClientValue = cash.trim();

    // Nếu không nhập gì
    if (cashClientValue === "") {
        formErorrCash.style.display = 'block';
        erorrCash.innerText = 'Mời nhập đủ giá!';
        btnCreateBill.disabled = true;
        surplusMoneySpan.style.display = 'none';
        surplusMoney.innerText = '';
        textSurplusMoney.value = '0';
        cashClientText.value = '0';
        checkButonCreateBill = false;
    }else {
        var totalAmountNumber = parseNumber(totalAmount.value);
        var cashClientNumber = parseNumber(cashClientValue);
        console.log(cashClientValue)
        // Kiểm tra nếu dữ liệu đầu vào không phải là số
        if (isNaN(cashClientValue)) {
            formErorrCash.style.display = 'block';
            erorrCash.innerText = 'Đây không phải là số!';
            btnCreateBill.disabled = true;
            surplusMoneySpan.style.display = 'none';
            surplusMoney.innerText = '';
            textSurplusMoney.value = '0';
            cashClientText.value = cashClientValue;
            checkButonCreateBill = false;
        } else {
            console.log('Neu la so thi vao day');
            // // Kiểm tra nếu dữ liệu là số nhỏ hơn 0
            // if (cashClientNumber < 500) {
            //     formErorrCash.style.display = 'block';
            //     erorrCash.innerText = 'Tiền nhập không được nhỏ hơn 500 VNĐ!';
            //     btnCreateBill.disabled = true;
            // }
            // Kiểm tra nếu số tiền nhập vào lớn hơn hoặc nhỏ hơn tổng tiền
            if(cashClientNumber < 1000) {
                formErorrCash.style.display = 'block';
                erorrCash.innerText = 'Tiền nhập vào không được thấp hơn 1,000 VNĐ!';
                btnCreateBill.disabled = true;
                surplusMoneySpan.style.display = 'none';
                surplusMoney.innerText = '';
                textSurplusMoney.value = '0';
                cashClientText.value = cashClientValue;
                checkButonCreateBill = false;
            }
            else if (cashClientNumber < totalAmountNumber && parseNumber(payMethod.value) === 1) {
                formErorrCash.style.display = 'block';
                erorrCash.innerText = 'Tiền nhập vào phải bằng với hóa đơn!';
                btnCreateBill.disabled = true;
                surplusMoneySpan.style.display = 'none';
                surplusMoney.innerText = '';
                textSurplusMoney.value = '0';
                cashClientText.value = cashClientValue;
                checkButonCreateBill = false;

            }else if (cashClientNumber > totalAmountNumber  && parseNumber(payMethod.value) === 1) {
                formErorrCash.style.display = 'none';
                erorrCash.innerText = '';
                btnCreateBill.disabled = false;
                surplusMoneySpan.style.display = 'block';
                surplusMoney.innerText = formatNumber(cashClientNumber - totalAmountNumber).toLocaleString('en-US') + ' VNĐ';
                textSurplusMoney.value = cashClientNumber - totalAmountNumber;
                cashClientText.value = cashClientNumber;
                cashAccount.value = 0;
                checkButonCreateBill = true;
            }else if (cashClientNumber - totalAmountNumber > 20000 && parseNumber(payMethod.value) === 3) {
                formErorrCash.style.display = 'block';
                erorrCash.innerText = 'Để tạo hóa đơn điện tử cần dư ra 20,000 VNĐ!';
                btnCreateBill.disabled = true;
                surplusMoneySpan.style.display = 'none';
                surplusMoney.innerText = '';
                textSurplusMoney.value = '0';
                cashClientText.value = cashClientValue;
                checkButonCreateBill = false;
            }else {
                formErorrCash.style.display = 'none';
                erorrCash.innerText = '';
                btnCreateBill.disabled = false;
                surplusMoneySpan.style.display = 'none';
                surplusMoney.innerText = '';
                textSurplusMoney.value = '0';
                cashClientText.value = cashClientNumber;
                checkButonCreateBill = true;
                if(payMethodChecked === 3) {
                    cashAccount.value = totalAmountNumber - cashClientNumber;
                    checkButonCreateBill = true;
                }
            }
        }
    }
}



function validateAll() {
    if (payMethodChecked != 2) {
        var rawValue = cashClient.value.replace(/,/g, ''); // Xóa dấu phẩy hiện tại

        // Format lại số nếu nó là số hợp lệ
        if (!isNaN(rawValue) && rawValue.trim() !== "") {
            cashClient.value = formatNumber(rawValue);
        }

        // Gọi hàm kiểm tra hợp lệ sau khi nhập dữ liệu
        validate(rawValue);
    }
}

var notePaymentFromBill = document.getElementById('notePayment');
notePaymentFromBill.addEventListener('input', function () {  // Thay 'change' bằng 'input'
    validateNodte('notePayment','formErorrNote','erorrNote');
});

function validateNodte(note,fromError,textError) {
    var noteEvent = document.getElementById(note);
    console.log(noteEvent.value);
    if(noteEvent.value.length > 500000) {
        document.getElementById(fromError).style.display = 'block';
        document.getElementById(textError).innerText = 'Số lượng ký tự phải dưới 500 nghìn ký tự!';
        if(checkTypeBill == true) {
            validateAll();
        }
        btnCreateBill.disabled = true;
    } else {
        document.getElementById(fromError).style.display = 'none';
        document.getElementById(textError).innerText = '';
        if(checkTypeBill == true) {
            validateAll();
        }
        if (btnCreateBill.disabled == false) {
            btnCreateBill.disabled = false;
        }
    }
}



cashClient.addEventListener('input', function () {
    validateAll();
});

// Xử lý sự kiện khi form được gửi
// document.getElementById('formPay').addEventListener('submit', function(event) {
//     var cashClientValue = cashClient.value.replace(/,/g, ''); // Xóa dấu phẩy hiện tại
//     var totalAmountNumber = parseNumber(totalAmount.value);
//     var cashClientNumber = parseNumber(cashClientValue);
//
//     if (cashClientValue === '' || isNaN(cashClientNumber) || cashClientNumber < 500 || (cashClientNumber < totalAmountNumber && parseInt(payMethod.value) === 1)) {
//         event.preventDefault(); // Ngăn chặn gửi form nếu có lỗi
//         validate(cashClientValue); // Hiển thị lỗi nếu cần thiết
//     }
// });

