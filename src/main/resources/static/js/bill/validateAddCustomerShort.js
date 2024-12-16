var nameCustomerInput = document.getElementById('nameCustomer');
var numberPhoneInput = document.getElementById('numberPhone');
var emailInput = document.getElementById('email');
var addResDetailInput = document.getElementById('addResDetail');
var provinceSelect = document.getElementById("provinceSelect-add-customer");
var districtSelect = document.getElementById("districtSelect-add-customer");
var wardSelect = document.getElementById("wardSelect-add-customer");

var errorNameCustomer = document.getElementById('error-nameCustomer-short');
var errorNumberPhone = document.getElementById('error-numberPhoneCustomer-short');
var errorEmail = document.getElementById('error-emailCustomer-short');
var errorAddResDetail = document.getElementById('error-addResDetailCustomer-short');

var btnAddCustomerShort = document.getElementById('btnAddCustomerShort');
var checkSelectAddRes = 0;
function loadAPIAddRess() {
    initializeLocationDropdowns('provinceSelect-add-customer','districtSelect-add-customer','wardSelect-add-customer','districtSelectContainer-add-customer','wardSelectContainer-add-customer',0,0,0)
}

nameCustomerInput.addEventListener('input', function () {
    validateAllFormAddCustomerShort();
});

numberPhoneInput.addEventListener('input', function () {
    validateAllFormAddCustomerShort();
});

emailInput.addEventListener('input', function () {
    validateAllFormAddCustomerShort();
});

addResDetailInput.addEventListener('input', function () {
    validateAllFormAddCustomerShort();
});

provinceSelect.addEventListener("change", function() {
    districtSelect.value = '';
    wardSelect.value = '';
    validateAllFormAddCustomerShort();
});

districtSelect.addEventListener("change", function() {
    wardSelect.value = '';
    validateAllFormAddCustomerShort();
});


wardSelect.addEventListener("change", function() {
    validateAllFormAddCustomerShort();
});


function validateAllFormAddCustomerShort() {
    validateNameCustomerIsText(nameCustomerInput.value.trim(),errorNameCustomer);
    validateNumberPhone(numberPhoneInput.value.trim(),errorNumberPhone);
    validateEmail(emailInput.value.trim(),errorEmail);
    validateAddRessDetail(addResDetailInput.value.trim(),errorAddResDetail);
    validateProvince(provinceSelect);
    validateDistrict(districtSelect);
    validateWard(wardSelect);
    if(validateNameCustomerIsText(nameCustomerInput.value.trim(),errorNameCustomer) == true &&
        validateNumberPhone(numberPhoneInput.value.trim(),errorNumberPhone) == true &&
        validateEmail(emailInput.value.trim(),errorEmail) == true &&
        isEmailUnique(emailInput.value.trim(),errorEmail) == true &&
        validateAddRessDetail(addResDetailInput.value.trim(),errorAddResDetail) == true &&
        validateProvince(provinceSelect) == true &&
        validateDistrict(districtSelect) == true &&
        validateWard(wardSelect) == true
    ) {
        btnAddCustomerShort.disabled = false;
    }else {
        btnAddCustomerShort.disabled = true;
    }
}

function validateNameCustomerIsText(value, inputError) {
    var nameCheck = value; // Loại bỏ khoảng trắng ở đầu và cuối
    const regex = /^[A-Za-z\s\u00C0-\u00FF\u0100-\u017F\u0180-\u024F\u1E00-\u1EFF]*$/;

    if (nameCheck === '' || nameCheck.length < 1) {
        inputError.style.display = 'block';
        inputError.innerText = '*Mời nhập tên khách hàng!';
        return false;
    } else if (!regex.test(nameCheck)) {
        // Biểu thức chính quy kiểm tra chỉ bao gồm chữ cái (có dấu hoặc không dấu) và khoảng trắng
        inputError.style.display = 'block';
        inputError.innerText = '*Tên khách hàng không hợp lệ!';
        return false;
    }else if (nameCheck.length > 255) {
        inputError.style.display = 'block';
        inputError.innerText = '*Tên khách hàng không được quá 255 ký tự!';
        return false;
    }else {
        inputError.style.display = 'none';
        inputError.innerText = '';
        return true;
    }
}



var listEmailCheck = [];
function isEmailUnique(emailInput,inputError) {
    // Kiểm tra nếu email đã tồn tại trong danh sách
    var exists = listEmailCheck.includes(emailInput);

    if (exists) {
        inputError.style.display = 'block';
        inputError.innerText = '*Email đã tồn tại, mời chọn email khác!';
        return false; // Email đã tồn tại
    } else {
        inputError.style.display = 'none';
        inputError.innerText = '';
        return true; // Email không trùng
    }
}

function getAllEmailCheckSame() {
    $.ajax({
        type: "GET",
        url: "/bill-api/check-same-email-customer",
        success: function (response) {
            listEmailCheck = response;
        },
        error: function (xhr) {
            console.log(xhr.responseText)
        }
    })
}

getAllEmailCheckSame()