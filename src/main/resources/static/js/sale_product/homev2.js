var listCodeSaleProducts = [];
document.getElementById('discountType').addEventListener('change',function () {
    var value = document.getElementById('discountType').value;
    if(value == '2') {
        document.getElementById('discountText').textContent = '₫';
    }else {
        document.getElementById('discountText').textContent = '%';
    }
})

function formatDateVoucher(date) {
    let d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    let year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}

$('#methodAddAndRemoverSaleProduct').html(`<button type="button" class="btn btn-outline-success" onclick="addOrUpdateSaleProductInProduct()">Thêm-sửa đợt giảm giá</button>`)
var codeUpdate = document.getElementById('formUpdateSaleProduct');
if(!codeUpdate) {
    // Lấy ngày hiện tại
    let today = new Date();
    document.getElementById('startDate').value = formatDateVoucher(today);

    let nexttoday = today.setDate(today.getDate() + 1);
    document.getElementById('endDate').value = formatDateVoucher(nexttoday);
}


function setActiveProductSaleAndNotSale(element, value) {
    // Xóa lớp active khỏi tất cả các liên kết
    var links = document.querySelectorAll('.nav-link-custom');
    links.forEach(function(link) {
        link.classList.remove('active');
    });
    var btn = '';
    if(value == 1) {
        btn = `
             <button type="button" class="btn btn-outline-success" onclick="addOrUpdateSaleProductInProduct()" >Thêm-sửa đợt giảm giá</button>
        `;
    }else {
        btn = `
             <button type="button" class="btn btn-outline-danger" onclick="removeSaleProductInProduct()">Xóa đợt giảm giá</button>
             <button type="button" class="btn btn-outline-success" onclick="addOrUpdateSaleProductInProduct()" >Thêm-sửa đợt giảm giá</button>
        `
    }
    $('#methodAddAndRemoverSaleProduct').html(btn)
    checkProduct = value;
    filterProduct(checkProduct)
    element.classList.add('active');
}

/////////////validate

function validateAllSaleProduct() {
    // Format lại giá trị của trường value
    formatInputWithThousandSeparator('value');

    var codeSaleProduct = document.getElementById('codeSaleProduct');
    var nameSaleProduct = document.getElementById('nameSaleProduct');
    var discountType = document.getElementById('discountType');
    var value = document.getElementById('value');
    var startDate = document.getElementById('startDate');
    var endDate = document.getElementById('endDate');

    // span error
    var codeSaleProductError = document.getElementById('codeSaleProductError');
    var nameSaleProductError = document.getElementById('nameSaleProductError');
    var discountTypeError = document.getElementById('discountTypeError');
    var valueError = document.getElementById('valueError');
    var startDateError = document.getElementById('startDateError');
    var endDateError = document.getElementById('endDateError');

    var checkCodeSaleProduct = checkNullInputSaleProduct(codeSaleProduct.value.trim(), codeSaleProductError, 100, '*Mời nhập mã đợt giảm giá(tối đa 100 ký tự)');
    if (checkCodeSaleProduct) {
        checkCodeSaleProduct = checkSame(codeSaleProduct.value.trim(), codeSaleProductError, '*Mã đợt giảm giá đã tồn tại!');
    }
    var checkNameSaleProduct = checkNullInputSaleProduct(nameSaleProduct.value.trim(), nameSaleProductError, 255, '*Mời nhập tên đợt giảm giá(tối đa 255 ký tự)');

    // Kiểm tra giá trị của discountType để xác định loại giảm giá và thực hiện validate tương ứng
    var checkValue = discountType.value === '2' ? checkNumberLimitInputSaleProductCash(removeThousandSeparator('value'), valueError) : checkNumberLimitInputSaleProductPercent(removeThousandSeparator('value'), valueError);

    var checkStartDate = checkStartDateSaleProduct(startDate.value, startDateError);
    var checkEndDate = checkEndDateSaleProduct(endDate.value, startDate.value, endDateError);
    // In kết quả ra console
    formatInputWithThousandSeparator('value');
    if (checkCodeSaleProduct && checkNameSaleProduct && checkValue && checkStartDate && checkEndDate) {
        document.getElementById('btnOpenModalConfirmAddSaleProduct').disabled = false;
    } else {
        document.getElementById('btnOpenModalConfirmAddSaleProduct').disabled = true;
    }
}
function resetFormAddSaleProduct() {
    document.getElementById('codeSaleProduct').value = '';
    document.getElementById('nameSaleProduct').value = '';
    document.getElementById('discountType').value = '2';
    document.getElementById('value').value = '0';

    if (!codeUpdate) {
        today = new Date();
        document.getElementById('startDate').value = formatDateVoucher(today);
        nexttoday = today.setDate(today.getDate() + 1);
        document.getElementById('endDate').value = formatDateVoucher(nexttoday);
    }
    formatInputWithThousandSeparator('value');
    // Gọi validate sau khi reset form
    validateAllSaleProduct();
}
document.getElementById('codeSaleProduct').addEventListener('input',function () {
    validateAllSaleProduct();
})
document.getElementById('nameSaleProduct').addEventListener('input',function () {
    validateAllSaleProduct();
})
// Lắng nghe sự kiện thay đổi discountType
document.getElementById('discountType').addEventListener('change', function () {
    formatInputWithThousandSeparator('value');
    validateAllSaleProduct();
});
document.getElementById('value').addEventListener('input',function () {
    validateAllSaleProduct();
})
document.getElementById('startDate').addEventListener('input',function () {
    validateAllSaleProduct();
})
document.getElementById('endDate').addEventListener('input',function () {
    validateAllSaleProduct();
})

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
    // Kiểm tra nếu giá trị là số và không có phần dư khi chia 1
    return Number.isFinite(value) && value % 1 === 0;
}
//kiem tra trung ma
function checkSameCodeSaleProduct() {
   $.ajax({
       type: "GET",
       url: "/api-sale-product/check-code-sale-product-same",
       success: function (response) {
           listCodeSaleProducts = response
            // Kiểm tra và xóa mã trong list nếu giống với mã cần cập nhật
           if (codeUpdate) {
               // Sử dụng filter để loại bỏ các mã trùng với codeUpdate
               listCodeSaleProducts = listCodeSaleProducts.filter(function(code) {
                   return code !== codeUpdate.value.trim();  // Giữ lại các mã không trùng
               });
           }
       },
       error: function (xhr) {
           console.error('loi ' + xhr.responseText)
       }
   })
}
checkSameCodeSaleProduct()

function checkSame(inputValue,spanError,mess) {
    if (listCodeSaleProducts.includes(inputValue)) {
        spanError.style.display = 'block';
        spanError.innerText = mess;
        return false;
    }else {
        spanError.style.display = 'none';
        spanError.innerText = '';
        return true;
    }
}

//check trong
function checkNullInputSaleProduct(inputValue,spanError,lengthValue,mess) {
    var nameCheck = inputValue;
    if(nameCheck === '' || nameCheck.length < 1){
        spanError.style.display = 'block';
        spanError.innerText = mess;
        return false;
    }if (nameCheck.length > lengthValue) {
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
function checkNumberLimitInputSaleProductCash(inputValue, spanError) {
    if(inputValue === '' || inputValue.length < 1) {
        spanError.style.display = 'block';
        spanError.innerText = '*Giá trị không được để trống!';
        return false;
    }else {
        var value = parseFloat(inputValue);
        if(isIntegerValue(value) == true) {
            // Kiểm tra giá trị nhập có hợp lệ hay không
            if (isNaN(value) || inputValue === '' || value < 10000) {
                spanError.style.display = 'block';
                spanError.innerText = '*Mời nhập giá trị hợp lệ!(it nhat la phải 10 nghìn)';
                return false;
            } else if (value > 10000000) {
                spanError.style.display = 'block';
                spanError.innerText = '*Giá trị không được vượt quá 10 triệu!';
                return false;
            } else {
                spanError.style.display = 'none';
                spanError.innerText = '';
                return true;
            }
        }else {
            spanError.style.display = 'block';
            spanError.innerText = '*Giá trị giảm phải là số nguyên!';
            return false;
        }
    }
}

//check Giá trị phai la so duong lon hon hon 90%
function checkNumberLimitInputSaleProductPercent(inputValue, spanError) {
    if(inputValue === '' || inputValue.length < 1) {
        spanError.style.display = 'block';
        spanError.innerText = '*Giá trị không được để trống!';
        return false;
    }else {
        var value = parseFloat(inputValue);
        if(isIntegerValue(value) == true) {
            if (isNaN(value) || inputValue === '' || value <= 0) {
                spanError.style.display = 'block';
                spanError.innerText = '*Mời nhập giá trị hợp lệ!(ít nhất phải 1%)';
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
            spanError.innerText = '*Giá trị giảm phải là số nguyên!';
            return false;
        }
    }
}

//check ngay bat dau
function checkStartDateSaleProduct(inputDate,spanError) {
    var dateValue = new Date(inputDate);  // Chuyển đổi giá trị ngày nhập thành đối tượng Date
    var currentDate = new Date();
    currentDate.setHours(0, 0, 0, 0); // Đặt thời gian hiện tại về 00:00 để chỉ so sánh ngày

    if (dateValue < currentDate) {
        spanError.style.display = 'block';
        spanError.innerText = '*Ngày bắt đầu phải là ngày hôm nay hoặc sau đó!';
        return false;
    } else {
        spanError.style.display = 'none';
        spanError.innerText = '';
        return true;
    }
}

//check ngay ket thuc
function checkEndDateSaleProduct(inputDateEnd, inputDateStart, spanError) {
    // Chuyển đổi các giá trị ngày vào thành đối tượng Date
    var endDateValue = new Date(inputDateEnd);
    var startDateValue = new Date(inputDateStart);

    // Kiểm tra nếu ngày kết thúc nhỏ hơn hoặc bằng ngày bắt đầu
    if (endDateValue <= startDateValue) {
        spanError.style.display = 'block';
        spanError.innerText = '*Ngày kết thúc phải sau ngày bắt đầu!';
        return false;
    } else {
        spanError.style.display = 'none';
        spanError.innerText = '';
        return true;
    }
}

validateAllSaleProduct()
formatInputWithThousandSeparator('value');
