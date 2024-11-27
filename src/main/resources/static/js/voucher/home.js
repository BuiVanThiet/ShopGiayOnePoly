var listCodeVouchers = [];
var codeVoucherUpdate = document.getElementById('formUpdateVoucher');

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
    formatInputWithThousandSeparator('value')
    formatInputWithThousandSeparator('applyValue')
    formatInputWithThousandSeparator('maxDiscount')
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
    if (checkCodeVoucher) {
        checkCodeVoucher = checkSame(codeVoucher.value.trim(), codeVoucherError, 'Mã phiếu giảm giá đã tồn tại!');
    }
    var checkNameVoucher = checkNullInputVoucher(nameVoucher.value.trim(),nameVoucherError,'Mời nhập tên phiếu giảm giá');
    var checkDiscountType = true;
    var checkValue = discountType.value === '2' ? checkNumberLimitInputVoucherCash(removeThousandSeparator('value'),valueError) : checkNumberLimitInputVoucherPercent(removeThousandSeparator('value'),valueError);
    var checkApplyValue = checkNumberApplyLimitInputVoucherCash(removeThousandSeparator('applyValue'),applyValueError);
    var checkMaxDiscount = discountType.value === '2' ? true : checkNumberMaxLimitInputVoucherCash(removeThousandSeparator('maxDiscount'),maxDiscountError);
    var checkNote = checkNullInputVoucher(note.value,noteError,'Mời nhập ghi chú');
    var checkStartDate = checkStartDateVoucher(startDate.value,startDateError);
    var checkEndDate = checkEndDateVoucher(endDate.value,startDate.value,endDateError);
    var checkQuantity = checkNumberMaxQuantityInputVoucher(quantity.value,quantityError);

    var checkVoucherLogic = checkVoucherValues(value.value, applyValue.value,discountType.value === '1' ? maxDiscount.value : value.value);

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
    console.log("checkVoucherLogic: ", checkVoucherLogic);
    formatInputWithThousandSeparator('value')
    formatInputWithThousandSeparator('applyValue')
    formatInputWithThousandSeparator('maxDiscount')
    // Kiểm tra tất cả các biến, tất cả phải là true
    if (checkVoucherLogic && checkCodeVoucher && checkNameVoucher && checkValue && checkApplyValue && checkMaxDiscount && checkNote && checkStartDate && checkEndDate && checkQuantity) {
        document.getElementById('btnOpenModalConfirmAddVoucher').disabled = false;
    } else {
        document.getElementById('btnOpenModalConfirmAddVoucher').disabled = true;
    }
}


//kiem tra trung ma
function checkSameCodeVoucher() {
    $.ajax({
        type: "GET",
        url: "/api_voucher/check-same-code-voucher",
        success: function (response) {
            listCodeVouchers = response
            // Kiểm tra và xóa mã trong list nếu giống với mã cần cập nhật
            if (codeVoucherUpdate) {
                console.log(codeVoucherUpdate)
                // Sử dụng filter để loại bỏ các mã trùng với codeUpdate
                listCodeVouchers = listCodeVouchers.filter(function(code) {
                    return code !== codeVoucherUpdate.value.trim();  // Giữ lại các mã không trùng
                });
            }
            console.log(listCodeVouchers)
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function checkSame(inputValue,spanError,mess) {
    console.log('da vao check some')
    if (listCodeVouchers.includes(inputValue)) {
        spanError.style.display = 'block';
        spanError.innerText = mess;
        return false;
    }else {
        spanError.style.display = 'none';
        spanError.innerText = '';
        return true;
    }
}
//ep kieu so
function formatInputWithThousandSeparator(inputId) {
    const inputElement = document.getElementById(inputId);

    if (!inputElement) {
        console.error(`Không tìm thấy phần tử với ID: ${inputId}`);
        return;
    }

    // Định dạng giá trị sẵn có khi trang tải
    if (inputElement.value) {
        let rawValue2 = inputElement.value.replace(/,/g, '');
        if (!isNaN(rawValue2) && rawValue2 !== '') {
            inputElement.value = Number(rawValue2).toLocaleString('en-US');
        }
    }

    // Thêm sự kiện khi người dùng nhập
    inputElement.addEventListener("input", function () {
        // Lấy giá trị gốc, loại bỏ dấu phân cách (nếu có)
        let rawValue = this.value.replace(/,/g, '');
        // Kiểm tra nếu giá trị là số
        if (!isNaN(rawValue) && rawValue !== '') {
            // Thêm dấu phân cách hàng nghìn
            this.value = Number(rawValue).toLocaleString('en-US');
        } else {
            // Nếu không phải số, xóa giá trị
            this.value = '';
        }
    });
}
function removeThousandSeparator(inputId) {
    const inputElement = document.getElementById(inputId);

    if (!inputElement) {
        console.error(`Không tìm thấy phần tử với ID: ${inputId}`);
        return;
    }

    // Lấy giá trị hiện tại của input và loại bỏ dấu phẩy
    const rawValue = inputElement.value.replace(/,/g, '');

    // Gán lại giá trị không có dấu phẩy
    inputElement.value = rawValue;

    return rawValue; // Trả về giá trị dạng số nguyên (chuỗi)
}


//phai la so nguyen
function isIntegerValue(value) {
    console.log(value)
    // Kiểm tra nếu giá trị là số và không có phần dư khi chia 1
    return Number.isFinite(value) && value % 1 === 0;
}

//
// Check business logic for voucher values
// Giá trị giảm không được lớn hơn giảm tối đa.
// 5. Giá trị giảm kh đc lớn hơn giá trị áp dụng.
// 4. Giá trị áp dụng phải lớn hon hoac bang giảm tối đa.
function checkVoucherValues(value, applyValue, maxDiscount) {
    var errorMessage = '';
    var valueError = document.getElementById('valueError2'); // For value field error
    var applyValueError = document.getElementById('applyValueError2'); // For apply value field error

    // Clear previous error messages
    valueError.innerText = '';
    applyValueError.innerText = '';

    // Check if value exceeds max discount
    if (parseFloat(value) > parseFloat(maxDiscount)) {
        errorMessage = 'Giá trị giảm không được lớn hơn giá trị giảm tối đa!';
        valueError.innerText = errorMessage; // Show error in value field
    }
    // Check if value exceeds apply value
    else if (parseFloat(value) > parseFloat(applyValue)) {
        errorMessage = 'Giá trị giảm không được lớn hơn giá trị áp dụng!';
        applyValueError.innerText = errorMessage; // Show error in apply value field
    }
    // Check if apply value is less than max discount
    else if (parseFloat(applyValue) < parseFloat(maxDiscount)) {
        errorMessage = 'Giá trị áp dụng phải lớn hơn hoặc bằng giá trị giảm tối đa!';
        applyValueError.innerText = errorMessage; // Show error in apply value field
    }

    // If there's any error message, log it and return false
    if (errorMessage) {
        console.log(errorMessage); // Log the error message for debugging
        return false; // Return false if there's a business rule violation
    }
    return true; // Return true if all checks pass
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
        if(isIntegerValue(value) == true) {
            // Kiểm tra giá trị nhập có hợp lệ hay không
            if (isNaN(value) || inputValue === '' || value < 10000) {
                spanError.style.display = 'block';
                spanError.innerText = 'Mời nhập giá trị hợp lệ!(thấp nhất là 10 nghìn)';
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
        }else {
            spanError.style.display = 'block';
            spanError.innerText = 'Giá trị giảm phải là số nguyên!';
            return false;
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
        if(isIntegerValue(value) == true) {
            // Kiểm tra giá trị nhập có hợp lệ hay không
            if (isNaN(value) || inputValue === '' || value <= 0) {
                spanError.style.display = 'block';
                spanError.innerText = 'Mời nhập giá trị hợp lệ!(thấp nhất là 1%)';
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
        }else {
            spanError.style.display = 'block';
            spanError.innerText = 'Giá trị giảm phải là số nguyên!';
            return false;
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
        if(isIntegerValue(value) == true) {
            // Kiểm tra giá trị nhập có hợp lệ hay không
            if (isNaN(value) || inputValue === '' || value < 10000) {
                spanError.style.display = 'block';
                spanError.innerText = 'Mời nhập giá trị áp dụng hợp lệ!(thấp nhất là 10 nghìn)';
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
        }else {
            spanError.style.display = 'block';
            spanError.innerText = 'Giá trị áp dụng phải là số nguyên!';
            return false;
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
        if(isIntegerValue(value) == true) {
            // Kiểm tra giá trị nhập có hợp lệ hay không
            if (isNaN(value) || inputValue === '' || value < 10000) {
                spanError.style.display = 'block';
                spanError.innerText = 'Mời nhập giá trị tối đa hợp lệ!(thấp nhất là 10 nghìn)';
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
        }else {
            spanError.style.display = 'block';
            spanError.innerText = 'Giá trị giảm tối đa phải là số nguyên!';
            return false;
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
        if(isIntegerValue(value) == true) {
            // Kiểm tra giá trị nhập có hợp lệ hay không
            if (isNaN(value) || inputValue === '' || value <= 0) {
                spanError.style.display = 'block';
                spanError.innerText = 'Mời nhập số lượng hợp lệ!(ít nhất phải có 1)';
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
        }else {
            spanError.style.display = 'block';
            spanError.innerText = 'Số lượng phải là số nguyên!';
            return false;
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
checkSameCodeVoucher()
// formatInputWithThousandSeparator('value')
// formatInputWithThousandSeparator('applyValue')
// formatInputWithThousandSeparator('maxDiscount')