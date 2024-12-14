// const shipSpan = document.getElementById('shipSpan'); // Xác định thẻ div cần ẩn/hiện
//
// shipSpan.style.display = 'none'; // Ẩn thẻ div khi checkbox được chọn
// var provinceID;
// var districtID;
// var wardID;
// var nameCustomer='';
// var numberPhoneCustomer = '';
// var addRessDetailCustomer = '';
var flexSwitchCheckDefaultCheck = document.getElementById('flexSwitchCheckDefault');
if(flexSwitchCheckDefaultCheck) {
    flexSwitchCheckDefaultCheck.addEventListener('change', function() {
        const dynamicContent = document.getElementById('dynamic-content');
        const shipSpan = document.getElementById('shipSpan'); // Xác định thẻ div cần ẩn/hiện

        if (this.checked) {
            checkSwitch = true;
            getUpdateTypeBill('2');
            // document.getElementById('moneyTransport').value = totalBill;
            formErorrCash.style.display = 'none';
            erorrCash.innerText = '';
            btnCreateBill.disabled = false;
            document.getElementById('formMoney').style.display = 'none';
            shipSpan.style.display = 'block'; // Ẩn thẻ div khi checkbox được chọn
            document.getElementById('form-payMethod-bill').style.display = 'none';
            // Gọi AJAX để lấy dữ liệu khách hàng
            $.ajax({
                url: '/bill-api/client-bill-information', // URL của endpoint
                method: 'GET',
                success: function(client) {
                    provinceID  = parseInt(client.city);
                    districtID = parseInt(client.district);
                    wardID = parseInt(client.commune);
                    nameCustomer = client.name;
                    emailCustomer = client.email;
                    numberPhoneCustomer = client.numberPhone;
                    addRessDetailCustomer = client.addressDetail;
                    console.log(provinceID + '-' + districtID + '-' + wardID)
                    selectCheckProvince = client.city;
                    selectCheckDistrict = client.district;
                    selectCheckWard = client.commune;
                    setClientShip(nameCustomer,numberPhoneCustomer,emailCustomer,provinceID,districtID,wardID,addRessDetailCustomer)
                    dynamicContent.innerHTML = `
            <div class="">
                <div class="row">
                    <div class="col-12">
                        <label class="form-label">Tên khách hàng</label>
                        <input type="text" class="form-control" value="${client.name}" id="nameCustomer">
                        <span class="text-danger" id="error-name-customer-ship" style="display: none"></span>
                    </div>
                    <div class="col-12">
                        <label class="form-label">Số điện thoại</label>
                        <input type="text" class="form-control" value="${client.numberPhone}" id="phoneCustomer">
                        <span class="text-danger" id="error-numberPhone-customer-ship" style="display: none"></span>
                    </div>
                      <div class="col-12">
                        <label class="form-label">Email</label>
                        <input type="text" class="form-control" value="${client.email}" id="emailCustomer">
                        <span class="text-danger" id="error-email-customer-ship" style="display: none"></span>
                    </div>
                    <!-- Các phần khác của form -->
                    <div class="col-4">
                        <label class="form-label">Tỉnh/Thành phố</label>
                        <select class="form-select" id="provinceSelect-transport">
                            <option value="" selected>Chọn tỉnh/thành phố</option>
                        </select>
                    </div>
                    <div class="col-4" id="districtSelectContainer-transport" style="display: none;">
                        <label class="form-label">Quận/Huyện</label>
                        <select class="form-select" id="districtSelect-transport">
                            <option value="" selected>Chọn quận/huyện</option>
                        </select>
                    </div>
                    <div class="col-4" id="wardSelectContainer-transport" style="display: none;">
                        <label class="form-label">Xã/Phường/Thị Trấn</label>
                        <select class="form-select" id="wardSelect-transport">
                            <option value="" selected>Chọn xã/phường/thị trấn</option>
                        </select>
                    </div>
                    <div class="mb-12">
                        <p>Địa chỉ cụ thể: </p>
                        <textarea class="form-control" id="addRessDetailCustomer">${client.addressDetail}</textarea>
                        <span class="text-danger" id="error-addResDetail-customer-ship" style="display: none"></span>
                    </div>
                </div>
                <div style="float: right;">
                    <img style="height: 100px" src="https://res.cloudinary.com/dfy4umpja/image/upload/v1734029128/phenkbeww9wjn9zyskdq.png">
                </div>
            </div>
        `;
                    console.log('Thong tin sau khi chon api ' + provinceTransport + '-' + districtTransport + '-' + wardTransport)
                    attachInputListeners();
                    initializeLocationDropdowns('provinceSelect-transport','districtSelect-transport','wardSelect-transport','districtSelectContainer-transport','wardSelectContainer-transport',provinceID,districtID,wardID)
                    validateInformationShip();

                },
                error: function() {
                    alert('Lỗi khi lấy thông tin khách hàng.');
                }
            });
            checkTypeBill = false;
        } else {
            checkSwitch = false;
            checkTypeBill = true;
            document.getElementById('form-payMethod-bill').style.display = 'flex';
            getUpdateTypeBill('1');
            formErorrCash.style.display = 'block';
            erorrCash.innerText = 'Mời nhập đủ giá!';
            $('#customerShip').val('Không có');
            if(payMethodChecked === 1 || payMethodChecked === 3){
                btnCreateBill.disabled = true;
            }
            document.getElementById('formMoney').style.display = 'block';
            document.getElementById('moneyTransport').value = 0;
            shipSpan.style.display = 'none'; // Hiển thị lại thẻ div khi checkbox không được chọn
            $('#moneyTransport').val(0)
            shipPrice = 0;
            paymentInformation();
            dynamicContent.innerHTML = `
            <div class="d-flex justify-content-center align-items-center position-relative">
                <div class="d-flex position-relative" style="width: 90%; padding-top: 90%;">
                    <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1732951076/4720aef9-53ba-41c9-ba1d-ca39c1637051.webp"
                        alt="Lỗi ảnh" class="img-fluid position-absolute top-0 start-0 w-100 h-100" style="object-fit: cover;">
                </div>
            </div>
        `;
        }
    });
}

function setClientShip(name,numberPhone,email,province,district,ward,addressDetail) {
    if(name) {
        console.log(name)
    }
    if(numberPhone) {
        console.log(numberPhone)
    }
    if(email) {
        console.log(email)
    }
    if(province) {
        console.log(province)
    }
    if(district) {
        console.log(district)
    }
    if(ward) {
        console.log(ward)
    }
    if(addressDetail) {
        console.log(addressDetail)
        $('#customerShip').val(name+','+numberPhone+','+email+','+province+','+district+','+ward+','+addressDetail);
    }
    // $('#idCity-staff').val(province)
    // $('#idDistrict-staff').val(district)
    // $('#idCommune-staff').val(ward)
}

function validateInformationShip() {
    // Khai báo các phần tử DOM sử dụng trong mã
    var nameInputCheckSwitch = document.getElementById('nameCustomer');
    var phoneInputCheckSwitch = document.getElementById('phoneCustomer');
    var emailInputCheckSwitch = document.getElementById('emailCustomer');
    var addressTextareaCheckSwitch = document.getElementById('addRessDetailCustomer');

    var provinceCheckSwitch = document.getElementById('provinceSelect-transport');
    var districtCheckSwitch = document.getElementById('wardSelect-transport');
    var wardCheckSwitch = document.getElementById('wardSelect-transport');

// Khai báo các phần tử hiển thị lỗi
    var errorNameCustomerShipCheckSwitch = document.getElementById('error-name-customer-ship');
    var errorNumberPhoneCustomerShipCheckSwitch = document.getElementById('error-numberPhone-customer-ship');
    var errorEmailCustomerShipCheckSwitch = document.getElementById('error-email-customer-ship');
    var errorAddResDetailCustomerShipCheckSwitch = document.getElementById('error-addResDetail-customer-ship');

    console.log(phoneInputCheckSwitch.value.trim())
    validateNameCustomer(nameInputCheckSwitch.value.trim(),errorNameCustomerShipCheckSwitch);
    validateNumberPhone(phoneInputCheckSwitch.value.trim(),errorNumberPhoneCustomerShipCheckSwitch);
    validateAddRessDetail(addressTextareaCheckSwitch.value.trim(),errorAddResDetailCustomerShipCheckSwitch);
    validateEmail(emailInputCheckSwitch.value.trim(),errorEmailCustomerShipCheckSwitch)

    validateProvinceCheckSwitch(selectCheckProvince);
    validateDistrictCheckSwitch(selectCheckDistrict);
    validateWardCheckSwitch(selectCheckWard);
    console.log('da vao day')
    console.log('provinceCheckSwitch ' + selectCheckProvince)
    console.log('districtCheckSwitch ' + selectCheckDistrict)
    console.log('wardCheckSwitch ' + selectCheckWard)

    if(validateNameCustomer(nameInputCheckSwitch.value.trim(),errorNameCustomerShipCheckSwitch) == true &&
        validateNumberPhone(phoneInputCheckSwitch.value.trim(),errorNumberPhoneCustomerShipCheckSwitch) == true &&
        validateEmail(emailInputCheckSwitch.value.trim(),errorEmailCustomerShipCheckSwitch) == true &&
        validateAddRessDetail(addressTextareaCheckSwitch.value.trim(),errorAddResDetailCustomerShipCheckSwitch) == true &&
        validateProvinceCheckSwitch(selectCheckProvince) == true &&
        validateDistrictCheckSwitch(selectCheckDistrict) == true &&
        validateWardCheckSwitch(selectCheckWard) == true
    ) {
        if(totalAmountBillCheck > 100000000000) {
            if (btnCreateBill) {
                document.getElementById('errorTotalAmount').style.display = 'block';
                btnCreateBill.disabled = true;
            }
        }else {
            if (btnCreateBill) {
                document.getElementById('errorTotalAmount').style.display = 'none';
                btnCreateBill.disabled = false;
            }
        }
    }else {
        btnCreateBill.disabled = true;
    }
}

function attachInputListeners() {
    // Khai báo các phần tử DOM sử dụng trong mã
    var nameInputCheckSwitch = document.getElementById('nameCustomer');
    var phoneInputCheckSwitch = document.getElementById('phoneCustomer');
    var emailInputCheckSwitch = document.getElementById('emailCustomer');
    var addressTextareaCheckSwitch = document.getElementById('addRessDetailCustomer');

    var provinceCheckSwitch = document.getElementById('provinceSelect-transport');
    var districtCheckSwitch = document.getElementById('districtSelect-transport');
    var wardCheckSwitch = document.getElementById('wardSelect-transport');

    if (nameInputCheckSwitch) {
        nameInputCheckSwitch.addEventListener('input', function() {
            validateInformationShip();
            nameCustomer = this.value.trim();
            setClientShip(nameInputCheckSwitch.value.trim(),phoneInputCheckSwitch.value.trim(),emailInputCheckSwitch.value.trim(),provinceID,districtID,wardID,addressTextareaCheckSwitch.value.trim());
            console.log('Tên khách hàng đã thay đổi: ' + this.value);
        });
    }

    if (phoneInputCheckSwitch) {
        phoneInputCheckSwitch.addEventListener('input', function() {
            validateInformationShip();
            numberPhoneCustomer = this.value.trim();
            setClientShip(nameCustomer,numberPhoneCustomer,emailCustomer,provinceID,districtID,wardID,addRessDetailCustomer);
            console.log('Số điện thoại đã thay đổi: ' + this.value);
        });
    }

    if (emailInputCheckSwitch) {
        emailInputCheckSwitch.addEventListener('input', function() {
            validateInformationShip();
            emailCustomer = this.value.trim();
            setClientShip(nameCustomer,numberPhoneCustomer,emailCustomer,provinceID,districtID,wardID,addRessDetailCustomer);
            console.log('email khách hàng đã thay đổi: ' + this.value);
        });
    }

    if (addressTextareaCheckSwitch) {
        addressTextareaCheckSwitch.addEventListener('input', function() {
            validateInformationShip();
            addRessDetailCustomer = this.value.trim();
            setClientShip(nameCustomer,numberPhoneCustomer,emailCustomer,provinceID,districtID,wardID,addRessDetailCustomer)
            console.log('Địa chỉ cụ thể đã thay đổi: ' + this.value);
        });
    }

    if (provinceCheckSwitch) {
        provinceCheckSwitch.addEventListener("change", function() {
            selectCheckProvince = this.value.trim();
            selectCheckDistrict = '';
            selectCheckWard='';
            validateInformationShip();
            console.log('Địa chỉ cụ thể đã thay đổi 1: ' + this.value);
        });
    }
    //
    if (districtCheckSwitch) {
        districtCheckSwitch.addEventListener("change", function() {
            selectCheckDistrict = this.value.trim();
            selectCheckWard='';
            validateInformationShip();
            console.log('Địa chỉ cụ thể đã thay đổi 2: ' + this.value);
        });
    }
    //
    if (wardCheckSwitch) {
        wardCheckSwitch.addEventListener("change", function() {
            selectCheckWard=this.value.trim();
            validateInformationShip();
            console.log('Địa chỉ cụ thể đã thay đổi 3: ' + this.value);
        });
    }



}

var selectCheckProvince = '';
var selectCheckDistrict = '';
var selectCheckWard = '';


function validateProvinceCheckSwitch(select) {
    return select.trim() !== "";
}
function validateDistrictCheckSwitch(select) {
    return select.trim()!== "";
}
function validateWardCheckSwitch(select) {
    return select.trim() !== "";
}




