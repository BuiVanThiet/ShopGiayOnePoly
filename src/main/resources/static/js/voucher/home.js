document.addEventListener("DOMContentLoaded", function () {
    // document.getElementById("button1").addEventListener("click", function () {
    //     changeTable(1);
    // });
    // document.getElementById("button2").addEventListener("click", function () {
    //     changeTable(2);
    // });

    document.getElementById("discountType").addEventListener("change", function () {
        const selectValueType = this.value;
        const discountTextDola = document.getElementById("discountTextDola");
        const discountTextCash = document.getElementById("discountTextCash");

        if (selectValueType === "1") {
            discountTextDola.style.display = "inline";
            discountTextCash.style.display = "none";
        } else if (selectValueType === "2") {
            discountTextCash.style.display = "inline";
            discountTextDola.style.display = "none";
        } else {
            discountTextDola.style.display = "none";
            discountTextCash.style.display = "none";
        }
    });

    var toastEl = document.querySelector('.custom-toast');
    if (toastEl) {
        var toast = new bootstrap.Toast(toastEl, {
            delay: 5000
        });
        toast.show();
    }

    document.querySelectorAll('.search-label').forEach(label => {
        label.addEventListener('click', function () {
            const targetSelector = this.getAttribute('data-target');
            const targetElement = document.querySelector(targetSelector);

            if (targetElement) {
                targetElement.style.display = (targetElement.style.display === 'none' || targetElement.style.display === '') ? 'block' : 'none';
            } else {
                console.error('Element not found:', targetSelector);
            }
        });
    });
    document.getElementById("discountType").addEventListener("change", function () {
        const selectedVoucherType = this.value;
        const maxiumPrice = document.getElementById("maxDiscount");
        const boxMaxiumPrice = document.getElementById("boxOfMaxiumDiscount");
        const valueVoucher = document.getElementById("value");
        if (selectedVoucherType === '2') {
            boxMaxiumPrice.style.display = 'none';
            maxiumPrice.value = valueVoucher.value;
        } else if (selectedVoucherType === '1') {
            boxMaxiumPrice.style.display = 'block';
        }
    })
});

document.getElementById('codeVoucher').addEventListener('input',function () {
    validateAllVoucher();
})
document.getElementById('nameVoucher').addEventListener('input',function () {
    validateAllVoucher();
})
document.getElementById('nameVoucher').addEventListener('input',function () {
    validateAllVoucher();
})
document.getElementById('discountType').addEventListener('change',function () {
    validateAllVoucher();
})
document.getElementById('value').addEventListener('input',function () {
    validateAllVoucher();
})
document.getElementById('applyValue').addEventListener('input',function () {
    validateAllVoucher();
})
document.getElementById('maxDiscount').addEventListener('input',function () {
    validateAllVoucher();
})
document.getElementById('note').addEventListener('input',function () {
    validateAllVoucher();
})
document.getElementById('startDate').addEventListener('input',function () {
    validateAllVoucher();
})
document.getElementById('endDate').addEventListener('input',function () {
    validateAllVoucher();
})
document.getElementById('quantity').addEventListener('input',function () {
    validateAllVoucher();
})

function resetFormAddVoucher() {
    document.getElementById('codeVoucher').value = '';
    document.getElementById('nameVoucher').value = '';
    document.getElementById('discountType').value = '2';
    document.getElementById('value').value = '';
    document.getElementById('applyValue').value = '';
    document.getElementById('maxDiscount').value = '';
    document.getElementById('note').value = '';
    document.getElementById('quantity').value = '';
    today = new Date();
    document.getElementById('startDate').value = formatDateVoucher(today);
    nexttoday = today.setDate(today.getDate() + 1);
    document.getElementById('endDate').value = formatDateVoucher(nexttoday);
    validateAllVoucher()
}
function validateAllVoucher() {
    var codeVoucher = document.getElementById('codeVoucher');
    var nameVoucher = document.getElementById('nameVoucher');
    var discountType = document.getElementById('discountType');
    var value = document.getElementById('value');
    var applyValue = document.getElementById('applyValue');
    var maxDiscount = document.getElementById('maxDiscount');
    var note = document.getElementById('note');
    var startDate = document.getElementById('startDate');
    var endDate = document.getElementById('endDate');
    var quantity = document.getElementById('quantity');
    //span error
    var codeVoucherError = document.getElementById('codeVoucherError');
    var nameVoucherError = document.getElementById('nameVoucherError');
    var discountTypeError = document.getElementById('discountTypeError');
    var valueError = document.getElementById('valueError');
    var applyValueError = document.getElementById('applyValueError');
    var maxDiscountError = document.getElementById('maxDiscountError');
    var noteError = document.getElementById('noteError');
    var startDateError = document.getElementById('startDateError');
    var endDateError = document.getElementById('endDateError');
    var quantityError = document.getElementById('quantityError');



    var checkCodeVoucher = checkNullInputVoucher(codeVoucher.value.trim(),codeVoucherError,'Mời nhập mã phiếu giảm giá');
    var checkNameVoucher = checkNullInputVoucher(nameVoucher.value.trim(),nameVoucherError,'Mời nhập tên phiếu giảm giá');
    var checkDiscountType = true;
    var checkValue = discountType.value === '2' ? checkNumberLimitInputVoucherCash(value.value,valueError) : checkNumberLimitInputVoucherPercent(value.value,valueError);
    var checkApplyValue = checkNumberApplyLimitInputVoucherCash(applyValue.value,applyValueError);
    var checkMaxDiscount = discountType.value === '2' ? true : checkNumberMaxLimitInputVoucherCash(maxDiscount.value,maxDiscountError);
    var checkNote = checkNullInputVoucher(note.value,noteError,'Mời nhập ghi chú');
    var checkStartDate = checkStartDateVoucher(startDate.value,startDateError);
    var checkEndDate = checkEndDateVoucher(endDate.value,startDate.value,endDateError);
    var checkQuantity = checkNumberMaxQuantityInputVoucher(quantity.value,quantityError);
// In kết quả ra console
    console.log("checkCodeVoucher: ", checkCodeVoucher);
    console.log("checkNameVoucher: ", checkNameVoucher);
    console.log("checkDiscountType: ", checkDiscountType); // Kiểm tra hàm cho discount type nếu có
    console.log("checkValue: ", checkValue);
    console.log("checkApplyValue: ", checkApplyValue);
    console.log("checkMaxDiscount: ", checkMaxDiscount);
    console.log("checkNote: ", checkNote);
    console.log("checkStartDate: ", checkStartDate);
    console.log("checkEndDate: ", checkEndDate);
    console.log("checkQuantity: ", checkQuantity);
    // Kiểm tra tất cả các biến, tất cả phải là true
    if (checkCodeVoucher && checkNameVoucher && checkValue && checkApplyValue && checkMaxDiscount && checkNote && checkStartDate && checkEndDate && checkQuantity) {
        document.getElementById('btnOpenModalConfirmAddVoucher').disabled = false;
    } else {
        document.getElementById('btnOpenModalConfirmAddVoucher').disabled = true;
    }
}
//check trong
function checkNullInputVoucher(inputValue,spanError,mess) {
    var nameCheck = inputValue;
    if(nameCheck === '' || nameCheck.length < 1){
        spanError.style.display = 'block';
        spanError.innerText = mess;
        return false;
    } else {
        spanError.style.display = 'none';
        spanError.innerText = '';
        return true;
    }
}

//check Giá trị phai la so duong be hon 10tr
function checkNumberLimitInputVoucherCash(inputValue, spanError) {
    if(inputValue === '' || inputValue.length < 1) {
        spanError.style.display = 'block';
        spanError.innerText = 'Giá trị không được để trống!';
        return false;
    }else {
        var value = parseFloat(inputValue); // Chuyển đổi chuỗi thành số thực
        // Kiểm tra giá trị nhập có hợp lệ hay không
        if (isNaN(value) || inputValue === '' || value < 0) {
            spanError.style.display = 'block';
            spanError.innerText = 'Mời nhập giá trị hợp lệ!';
            return false;
        } else if (value > 10000000) {
            spanError.style.display = 'block';
            spanError.innerText = 'Giá trị không được vượt quá 10 triệu!';
            return false;
        } else {
            spanError.style.display = 'none';
            spanError.innerText = '';
            return true;
        }
    }
}

//check Giá trị phai la so duong lon hon hon 90%
function checkNumberLimitInputVoucherPercent(inputValue, spanError) {
    if(inputValue === '' || inputValue.length < 1) {
        spanError.style.display = 'block';
        spanError.innerText = 'Giá trị không được để trống!';
        return false;
    }else {
        var value = parseFloat(inputValue); // Chuyển đổi chuỗi thành số thực
        // Kiểm tra giá trị nhập có hợp lệ hay không
        if (isNaN(value) || inputValue === '' || value < 0) {
            spanError.style.display = 'block';
            spanError.innerText = 'Mời nhập giá trị hợp lệ!';
            return false;
        } else if (value > 90) {
            spanError.style.display = 'block';
            spanError.innerText = 'Giá trị không được vượt quá 90%!';
            return false;
        } else {
            spanError.style.display = 'none';
            spanError.innerText = '';
            return true;
        }
    }
}

//check Giá trị ap dung phai la so duong be hon 10tr
function checkNumberApplyLimitInputVoucherCash(inputValue, spanError) {
    if(inputValue === '' || inputValue.length < 1) {
        spanError.style.display = 'block';
        spanError.innerText = 'Giá trị áp dụng không được để trống!';
        return false;
    }else {
        var value = parseFloat(inputValue); // Chuyển đổi chuỗi thành số thực
        // Kiểm tra giá trị nhập có hợp lệ hay không
        if (isNaN(value) || inputValue === '' || value < 0) {
            spanError.style.display = 'block';
            spanError.innerText = 'Mời nhập giá trị áp dụng hợp lệ!';
            return false;
        } else if (value > 10000000) {
            spanError.style.display = 'block';
            spanError.innerText = 'Giá trị áp dụng không được vượt quá 10 triệu!';
            return false;
        } else {
            spanError.style.display = 'none';
            spanError.innerText = '';
            return true;
        }
    }
}

//check Giá trị toi da phai la so duong be hon 10tr
function checkNumberMaxLimitInputVoucherCash(inputValue, spanError) {
    if(inputValue === '' || inputValue.length < 1) {
        spanError.style.display = 'block';
        spanError.innerText = 'Giá trị tối đa không được để trống!';
        return false;
    }else {
        var value = parseFloat(inputValue); // Chuyển đổi chuỗi thành số thực
        // Kiểm tra giá trị nhập có hợp lệ hay không
        if (isNaN(value) || inputValue === '' || value < 0) {
            spanError.style.display = 'block';
            spanError.innerText = 'Mời nhập giá trị tối đa hợp lệ!';
            return false;
        } else if (value > 10000000) {
            spanError.style.display = 'block';
            spanError.innerText = 'Giá trị tối đa không được vượt quá 10 triệu!';
            return false;
        } else {
            spanError.style.display = 'none';
            spanError.innerText = '';
            return true;
        }
    }
}

//check ngay bat dau
function checkStartDateVoucher(inputDate,spanError) {
    var dateValue = new Date(inputDate);  // Chuyển đổi giá trị ngày nhập thành đối tượng Date
    var currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0); // Đặt thời gian hiện tại về 00:00 để chỉ so sánh ngày

    if (dateValue < currentDate) {
        spanError.style.display = 'block';
        spanError.innerText = 'Ngày bắt đầu phải là ngày hôm nay hoặc sau đó!';
        return false;
    } else {
        spanError.style.display = 'none';
        spanError.innerText = '';
        return true;
    }
}

//check ngay ket thuc
function checkEndDateVoucher(inputDateEnd, inputDateStart, spanError) {
    // Chuyển đổi các giá trị ngày vào thành đối tượng Date
    var endDateValue = new Date(inputDateEnd);
    var startDateValue = new Date(inputDateStart);

    // Kiểm tra nếu ngày kết thúc nhỏ hơn hoặc bằng ngày bắt đầu
    if (endDateValue <= startDateValue) {
        spanError.style.display = 'block';
        spanError.innerText = 'Ngày kết thúc phải sau ngày bắt đầu!';
        return false;
    } else {
        spanError.style.display = 'none';
        spanError.innerText = '';
        return true;
    }
}

function checkNumberMaxQuantityInputVoucher(inputValue, spanError) {
    if(inputValue === '' || inputValue.length < 1) {
        spanError.style.display = 'block';
        spanError.innerText = 'Số lượng không được để trống!';
        return false;
    }else {
        var value = parseFloat(inputValue); // Chuyển đổi chuỗi thành số thực
        // Kiểm tra giá trị nhập có hợp lệ hay không
        if (isNaN(value) || inputValue === '' || value < 0) {
            spanError.style.display = 'block';
            spanError.innerText = 'Mời nhập số lượng hợp lệ!';
            return false;
        } else if (value > 100) {
            spanError.style.display = 'block';
            spanError.innerText = 'Số lượng không quá 100!';
            return false;
        } else {
            spanError.style.display = 'none';
            spanError.innerText = '';
            return true;
        }
    }
}

function formatDateVoucher(date) {
    let d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    let year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}


// Lấy ngày hiện tại
let today = new Date();
document.getElementById('startDate').value = formatDateVoucher(today);

let nexttoday = today.setDate(today.getDate() + 1);
document.getElementById('endDate').value = formatDateVoucher(nexttoday);
////////////////////

validateAllVoucher()
