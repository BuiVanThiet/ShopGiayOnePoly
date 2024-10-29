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
    validateNameCustomer(nameCustomerInput.value.trim(),errorNameCustomer);
    validateNumberPhone(numberPhoneInput.value.trim(),errorNumberPhone);
    validateEmail(emailInput.value.trim(),errorEmail);
    validateAddRessDetail(addResDetailInput.value.trim(),errorAddResDetail);
    validateProvince(provinceSelect);
    validateDistrict(districtSelect);
    validateWard(wardSelect);
    console.log(provinceSelect.value)
    if(validateNameCustomer(nameCustomerInput.value.trim(),errorNameCustomer) == true &&
        validateNumberPhone(numberPhoneInput.value.trim(),errorNumberPhone) == true &&
        validateEmail(emailInput.value.trim(),errorEmail) == true &&
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