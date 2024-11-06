var nameCustomerInput = document.getElementById('nameCustomerShip');
var numberPhoneInput = document.getElementById('phoneCustomerShip');
var emailInput = document.getElementById('emailCustomerShip');
var addResDetailInput = document.getElementById('addResDetailCustomerShip');
var provinceSelect = document.getElementById("provinceSelect-transport-Ship");
var districtSelect = document.getElementById("districtSelect-transport-Ship");
var wardSelect = document.getElementById("wardSelect-transport-Ship");

var errorNameCustomer = document.getElementById('error-nameCustomer-short');
var errorNumberPhone = document.getElementById('error-numberPhoneCustomer-short');
var errorEmail = document.getElementById('error-emailCustomer-short');
var errorAddResDetail = document.getElementById('error-addResDetailCustomer-short');

var btnUpdateCustomerShip = document.getElementById('btnUpdateCustomerShip');

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
    validateAddRessDetail(addResDetailInput.value.trim(),errorAddResDetail);
    validateEmail(emailInput.value.trim(),errorEmail);
    validateProvince(provinceSelect);
    validateDistrict(districtSelect);
    validateWard(wardSelect);
    console.log(provinceSelect.value)
    if(validateNameCustomer(nameCustomerInput.value.trim(),errorNameCustomer) == true &&
        validateNumberPhone(numberPhoneInput.value.trim(),errorNumberPhone) == true &&
        validateAddRessDetail(addResDetailInput.value.trim(),errorAddResDetail) == true &&
        validateEmail(emailInput.value.trim(),errorEmail) == true &&
        validateProvince(provinceSelect) == true &&
        validateDistrict(districtSelect) == true &&
        validateWard(wardSelect) == true
    ) {
        btnUpdateCustomerShip.disabled = false;
    }else {
        btnUpdateCustomerShip.disabled = true;
    }
}