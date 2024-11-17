document.getElementById('discountType').addEventListener('change',function () {
    console.log(document.getElementById('discountType').value)
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

// Lấy ngày hiện tại
let today = new Date();
document.getElementById('startDate').value = formatDateVoucher(today);

let nexttoday = today.setDate(today.getDate() + 1);
document.getElementById('endDate').value = formatDateVoucher(nexttoday);


function setActiveProductSaleAndNotSale(element, value) {
    // Xóa lớp active khỏi tất cả các liên kết
    var links = document.querySelectorAll('.nav-link-custom');
    links.forEach(function(link) {
        link.classList.remove('active');
    });
    var btn = '';
    if(value == 1) {
        btn = `
             <button type="button" class="btn btn-outline-success" onclick="addOrUpdateSaleProductInProduct()" >Thêm đợt giảm giá</button>
        `;
    }else {
        btn = `
             <button type="button" class="btn btn-outline-danger" onclick="removeSaleProductInProduct()">Xóa đợt giảm giá</button>
             <button type="button" class="btn btn-outline-success" onclick="addOrUpdateSaleProductInProduct()" >Thêm đợt giảm giá</button>
        `
    }
    $('#methodAddAndRemoverSaleProduct').html(btn)
    checkProduct = value;
    filterProduct(checkProduct)
    element.classList.add('active');
}

/////////////validate

function validateAllSaleProduct() {
    var codeSaleProduct = document.getElementById('codeSaleProduct');
    var nameSaleProduct = document.getElementById('nameSaleProduct');
    var discountType = document.getElementById('discountType');
    var value = document.getElementById('value');
    var startDate = document.getElementById('startDate');
    var endDate = document.getElementById('endDate');
    //span error
    var codeSaleProductError = document.getElementById('codeSaleProductError');
    var nameSaleProductError = document.getElementById('nameSaleProductError');
    var discountTypeError = document.getElementById('discountTypeError');
    var valueError = document.getElementById('valueError');
    var startDateError = document.getElementById('startDateError');
    var endDateError = document.getElementById('endDateError');



    var checkCodeSaleProduct = checkNullInputSaleProduct(codeSaleProduct.value.trim(),codeSaleProductError,'Mời nhập mã đợt giảm giá');
    var checkNameSaleProduct = checkNullInputSaleProduct(nameSaleProduct.value.trim(),nameSaleProductError,'Mời nhập tên đợt giảm giá');
    var checkDiscountType = true;
    var checkValue = discountType.value === '2' ? checkNumberLimitInputSaleProductCash(value.value,valueError) : checkNumberLimitInputSaleProductPercent(value.value,valueError);
    var checkStartDate = checkStartDateSaleProduct(startDate.value,startDateError);
    var checkEndDate = checkEndDateSaleProduct(endDate.value,startDate.value,endDateError);
// In kết quả ra console
    // Kiểm tra tất cả các biến, tất cả phải là true
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
    document.getElementById('value').value = '';
    today = new Date();
    document.getElementById('startDate').value = formatDateVoucher(today);
    nexttoday = today.setDate(today.getDate() + 1);
    document.getElementById('endDate').value = formatDateVoucher(nexttoday);
    validateAllSaleProduct()
}
document.getElementById('codeSaleProduct').addEventListener('input',function () {
    validateAllSaleProduct();
})
document.getElementById('nameSaleProduct').addEventListener('input',function () {
    validateAllSaleProduct();
})
document.getElementById('discountType').addEventListener('change',function () {
    validateAllSaleProduct();
})
document.getElementById('value').addEventListener('input',function () {
    validateAllSaleProduct();
})
document.getElementById('startDate').addEventListener('input',function () {
    validateAllSaleProduct();
})
document.getElementById('endDate').addEventListener('input',function () {
    validateAllSaleProduct();
})

//check trong
function checkNullInputSaleProduct(inputValue,spanError,mess) {
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
function checkNumberLimitInputSaleProductCash(inputValue, spanError) {
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
function checkNumberLimitInputSaleProductPercent(inputValue, spanError) {
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

//check ngay bat dau
function checkStartDateSaleProduct(inputDate,spanError) {
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
function checkEndDateSaleProduct(inputDateEnd, inputDateStart, spanError) {
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

validateAllSaleProduct()