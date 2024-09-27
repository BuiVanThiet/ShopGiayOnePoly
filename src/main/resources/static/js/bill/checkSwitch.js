// const shipSpan = document.getElementById('shipSpan'); // Xác định thẻ div cần ẩn/hiện
//
// shipSpan.style.display = 'none'; // Ẩn thẻ div khi checkbox được chọn
// var provinceID;
// var districtID;
// var wardID;
// var nameCustomer='';
// var numberPhoneCustomer = '';
// var addRessDetailCustomer = '';
document.getElementById('flexSwitchCheckDefault').addEventListener('change', function() {
    const dynamicContent = document.getElementById('dynamic-content');
    const shipSpan = document.getElementById('shipSpan'); // Xác định thẻ div cần ẩn/hiện

    if (this.checked) {
        getUpdateTypeBill('2');
        // document.getElementById('moneyTransport').value = totalBill;
        formErorrCash.style.display = 'none';
        erorrCash.innerText = '';
        btnCreateBill.disabled = false;
        document.getElementById('formMoney').style.display = 'none';
        shipSpan.style.display = 'block'; // Ẩn thẻ div khi checkbox được chọn

        // Gọi AJAX để lấy dữ liệu khách hàng
        $.ajax({
            url: '/bill-api/client-bill-information', // URL của endpoint
            method: 'GET',
            success: function(client) {
                provinceID  = parseInt(client.city);
                districtID = parseInt(client.district);
                wardID = parseInt(client.commune);
                nameCustomer = client.name;
                numberPhoneCustomer = client.numberPhone;
                addRessDetailCustomer = client.addressDetail;
                console.log(provinceID + '-' + districtID + '-' + wardID)
                setClientShip(nameCustomer,numberPhoneCustomer,provinceID,districtID,wardID,addRessDetailCustomer)
                dynamicContent.innerHTML = `
            <div class="">
                <div class="row">
                    <div class="col-12">
                        <label class="form-label">Tên khách hàng</label>
                        <input type="text" class="form-control" value="${client.name}" id="nameCustomer">
                    </div>
                    <div class="col-12">
                        <label class="form-label">Số điện thoại</label>
                        <input type="text" class="form-control" value="${client.numberPhone}" id="phoneCustomer">
                    </div>
                    <!-- Các phần khác của form -->
                    <div class="col-4">
                        <label class="form-label">Tỉnh/Thành phố</label>
                        <select class="form-select" id="provinceSelect-transport">
                            <option selected>Chọn tỉnh/thành phố</option>
                        </select>
                    </div>
                    <div class="col-4" id="districtSelectContainer-transport" style="display: none;">
                        <label class="form-label">Quận/Huyện</label>
                        <select class="form-select" id="districtSelect-transport">
                            <option selected>Chọn quận/huyện</option>
                        </select>
                    </div>
                    <div class="col-4" id="wardSelectContainer-transport" style="display: none;">
                        <label class="form-label">Xã/Phường/Thị Trấn</label>
                        <select class="form-select" id="wardSelect-transport">
                            <option selected>Chọn xã/phường/thị trấn</option>
                        </select>
                    </div>
                    <div class="mb-12">
                        <p>Địa chỉ cụ thể: </p>
                        <textarea class="form-control" id="addRessDetailCustomer">${client.addressDetail}</textarea>
                    </div>
                </div>
            </div>
        `;
                attachInputListeners();
                initializeLocationDropdowns('provinceSelect-transport','districtSelect-transport','wardSelect-transport','districtSelectContainer-transport','wardSelectContainer-transport',provinceID,districtID,wardID)
                console.log('Thong tin sau khi chon api ' + provinceTransport + '-' + districtTransport + '-' + wardTransport)

            },
            error: function() {
                alert('Lỗi khi lấy thông tin khách hàng.');
            }
        });
    } else {
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
                    <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1724526670/Screenshot%202024-07-24%20160719.png"
                        alt="Lỗi ảnh" class="img-fluid position-absolute top-0 start-0 w-100 h-100" style="object-fit: cover;">
                </div>
            </div>
        `;
    }
});

function setClientShip(name,numberPhone,province,district,ward,addressDetail) {
    $('#customerShip').val(name+','+numberPhone+','+province+','+district+','+ward+','+addressDetail);
}

function attachInputListeners() {
    const nameInput = document.getElementById('nameCustomer');
    const phoneInput = document.getElementById('phoneCustomer');
    const addressTextarea = document.getElementById('addRessDetailCustomer');

    if (nameInput) {
        nameInput.addEventListener('input', function() {
            var nameCheck = this.value.trim();
            nameCustomer = this.value.trim();
            if(nameCheck === '' || nameCheck.length < 1){
                console.log('rong ne')
                btnCreateBill.disabled = true;
            }else {
                console.log('ko rong ne')
                btnCreateBill.disabled = false;
            }
            setClientShip(nameCustomer,numberPhoneCustomer,provinceID,districtID,wardID,addRessDetailCustomer)
            console.log('Tên khách hàng đã thay đổi: ' + this.value);
        });
    }

    if (phoneInput) {
        phoneInput.addEventListener('input', function() {
            numberPhoneCustomer = this.value.trim();
            var numberPhoneCheck = this.value.trim();

            // Biểu thức chính quy cho số điện thoại:
            // Số điện thoại bắt đầu bằng số, có thể có 10 đến 12 chữ số
            var phoneRegex = /^[0-9]{10,12}$/;

            // Kiểm tra không rỗng và phải là số INT hợp lệ
            if (numberPhoneCheck === '' || numberPhoneCheck.length === 0) {
                console.log('Số điện thoại không được để trống');
                btnCreateBill.disabled = true;
            } else if (!phoneRegex.test(numberPhoneCheck)) {
                console.log('Số điện thoại không hợp lệ. Phải là số và có từ 10 đến 12 chữ số.');
                btnCreateBill.disabled = true;
            } else {
                console.log('Số điện thoại hợp lệ');
                btnCreateBill.disabled = false;
            }
            setClientShip(nameCustomer,numberPhoneCustomer,provinceID,districtID,wardID,addRessDetailCustomer);
            console.log('Số điện thoại đã thay đổi: ' + this.value);
        });
    }

    if (addressTextarea) {
        addressTextarea.addEventListener('input', function() {
            addRessDetailCustomer = this.value.trim();
            var addRessDetailCheck = this.value.trim();
            if(addRessDetailCheck === '' || addRessDetailCheck.length < 1){
                console.log('rong ne')
                btnCreateBill.disabled = true;
            }else {
                console.log('ko rong ne')
                btnCreateBill.disabled = false;
            }
            setClientShip(nameCustomer,numberPhoneCustomer,provinceID,districtID,wardID,addRessDetailCustomer)
            console.log('Địa chỉ cụ thể đã thay đổi: ' + this.value);
        });
    }
}



