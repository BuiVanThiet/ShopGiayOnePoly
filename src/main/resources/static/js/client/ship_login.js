document.addEventListener("DOMContentLoaded", function () {

    const apiKey = '0fc88a8e-6633-11ef-8e53-0a00184fe694';
    const shopId = '195165';
    const fromDistrictId = 3440;
    const weightText = document.getElementById("weightShip").value.trim();

    const weight = parseFloat(weightText.replace(/[^0-9.-]+/g, ''));
    if (isNaN(weight)) {
        alert("Cân nặng không hợp lệ.");
        return;
    }
    const totalPriceCartItemText = $('#spanTotalPriceCartItem').text().trim();
    if (totalPriceCartItemText === "") {
        alert("Không có thông tin toổng tiền.");
        return;
    }
    const totalPriceCartItem = parseFloat(totalPriceCartItemText.replace(/[^0-9.-]+/g, ''));
    if (isNaN(totalPriceCartItem)) {
        alert("Giá tổng tiền tại ship không hợp lệ.");
        return;
    }
    console.log("Weight ship: " + weight)
    console.log("Total amount: " + totalPriceCartItem)

    function calculateShippingFee(serviceId, toDistrictId, toWardCode) {
        // Kiểm tra các thông tin cần thiết
        if (!serviceId || !toDistrictId || !toWardCode) {
            console.error('Thiếu thông tin cần thiết để tính phí ship');
            document.getElementById("spanShippingFee").textContent = "Không thể tính phí ship";
            return;
        }

        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Token': apiKey
            }, body: JSON.stringify({
                "service_id": serviceId,
                "insurance_value": totalPriceCartItem,
                "to_district_id": parseInt(toDistrictId),
                "to_ward_code": toWardCode,
                "weight": weight,
                "length": 60,
                "width": 15,
                "height": 20,
                "from_district_id": fromDistrictId
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    // Cập nhật phí vận chuyển
                    const shippingFee = data.data.total;
                    document.getElementById("spanShippingFee").textContent = shippingFee.toLocaleString('en-US') + ' VNĐ';
                    console.log("Gia ship: " + shippingFee)

                    calculateTotalPrice();
                } else {
                    console.error('Lỗi tính phí ship:', data.message);
                    document.getElementById("spanShippingFee").textContent = "Không thể tính phí ship";
                }
            })
            .catch(error => console.error('Error:', error));
    }


    function getAvailableServices(toDistrictId, toWardCode) {
        if (!toDistrictId || !toWardCode) {
            console.error('Thiếu thông tin cần thiết để lấy dịch vụ');
            document.getElementById("spanShippingFee").textContent = "-";
            return;
        }

        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services', {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Token': apiKey
            }, body: JSON.stringify({
                "shop_id": parseInt(shopId, 10),
                "from_district": parseInt(fromDistrictId, 10),
                "to_district": parseInt(toDistrictId, 10)
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200 && data.data.length > 0) {
                    const serviceId = data.data[0].service_id;
                    calculateShippingFee(serviceId, toDistrictId, toWardCode);
                    console.log("Da den :getAvailableServices")
                } else {
                    console.error('Không có dịch vụ vận chuyển:', data.message);
                    document.getElementById("spanShippingFee").textContent = "Không có dịch vụ vận chuyển";
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById("spanShippingFee").textContent = "Lỗi kết nối hoặc lỗi máy chủ";
            });
    }


    function autoCalculateShippingFee() {
        const toDistrictId = document.getElementById("IdDistrict").value.trim();
        const toWardCode = document.getElementById("IdWard").value.trim();
        getAvailableServices(toDistrictId, toWardCode);
    }

    function calculateTotalPrice() {
        const totalPriceCartItemElement = document.getElementById("spanTotalPriceCartItem");
        const priceVoucherElement = document.getElementById("spanPriceVoucher");
        const shippingFeeElement = document.getElementById("spanShippingFee");
        const totalPriceBillElement = document.getElementById("spanTotalPriceBill");

        let totalPriceCartItem = parseFloat(totalPriceCartItemElement.textContent.replace(/[^\d.-]/g, '')) || 0;
        let priceVoucher = parseFloat(priceVoucherElement.textContent.replace(/[^\d.-]/g, '')) || 0;
        let shippingFee = parseFloat(shippingFeeElement.textContent.replace(/[^\d.-]/g, '')) || 0;

        const totalPriceBill = totalPriceCartItem - priceVoucher + shippingFee;

        totalPriceCartItemElement.textContent = totalPriceCartItem.toLocaleString('en-US') + ' VNĐ';
        priceVoucherElement.textContent = priceVoucher.toLocaleString('en-US') + ' VNĐ';
        shippingFeeElement.textContent = shippingFee.toLocaleString('en-US') + ' VNĐ';
        totalPriceBillElement.textContent = totalPriceBill.toLocaleString('en-US') + ' VNĐ';
    }

    autoCalculateShippingFee();
});
document.addEventListener("DOMContentLoaded", function () {
    calculateTotalPrice();
    const spanPriceVoucher = document.getElementById("spanPriceVoucher");
    const shipAddress = document.getElementById("addressShip");
    const fullAddressCustomerLogin = document.getElementById("fullAddressCustomerLogin").value;
    const voucherPriceText = spanPriceVoucher.textContent.trim();
    const priceVoucher = parseFloat(voucherPriceText.replace(/[^0-9.-]+/g, ''));
    if (fullAddressCustomerLogin != null) {
        console.log("fullAddressCustomerLogin: " + fullAddressCustomerLogin)
        shipAddress.value = fullAddressCustomerLogin;
    }
    if (isNaN(priceVoucher)) {
        spanPriceVoucher.textContent = "-";
    }

    const apiKey = '0fc88a8e-6633-11ef-8e53-0a00184fe694';
    const shopId = '195165';
    const fromDistrictId = 3440;
    let shippingFeeCalculated = false; // Cờ để kiểm soát việc tính lại phí vận chuyển
    const weightText = document.getElementById("weightShip").value.trim();

    const weight = parseFloat(weightText.replace(/[^0-9.-]+/g, ''));
    if (isNaN(weight)) {
        alert("Cân nặng không hợp lệ.");
        return;
    }

    const totalPriceCartItemText = $('#spanTotalPriceCartItem').text().trim();
    if (totalPriceCartItemText === "") {
        alert("Không có thông tin về giá tổng.");
        return;
    }
    const totalPriceCartItem = parseFloat(totalPriceCartItemText.replace(/[^0-9.-]+/g, ''));
    if (isNaN(totalPriceCartItem)) {
        alert("Giá tổng tiền không hợp lệ.");
        return;
    }

    // Hàm tính phí vận chuyển
    function calculateShippingFee(serviceId, toDistrictId, toWardCode) {
        console.log("Service ID:", serviceId);
        console.log("To District ID:", toDistrictId);
        console.log("To Ward Code:", toWardCode);

        // Kiểm tra tham số
        if (!serviceId || !toDistrictId || !toWardCode) {
            alert("Không thể tính phí ship: Tham số không hợp lệ.");
            console.error("Không thể tính phí ship: Thiếu tham số.");
            return;
        }

        // Đảm bảo serviceId là số nguyên (int)
        const serviceIdInt = parseInt(serviceId, 10);
        if (isNaN(serviceIdInt)) {
            console.error("service_id phải là một số nguyên.");
            alert("service_id phải là một số nguyên.");
            return;
        }

        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Token': apiKey // Đảm bảo rằng apiKey được khai báo đúng
            }, body: JSON.stringify({
                "service_id": serviceIdInt, // Đảm bảo service_id là số nguyên
                "insurance_value": totalPriceCartItem, // Đảm bảo totalPriceCartItem đã được khai báo
                "to_district_id": parseInt(toDistrictId), "to_ward_code": toWardCode, "weight": weight, // Đảm bảo weight đã được khai báo
                "length": 60, "width": 15, "height": 20, "from_district_id": fromDistrictId // Đảm bảo từ District ID được khai báo
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const shippingFee = data.data.total;
                    document.getElementById("spanShippingFee").textContent = `${shippingFee} VNĐ`;
                    calculateTotalPrice();
                    console.log("Phi ship sau khi doi: " + shippingFee)
                    shippingFeeCalculated = true;
                } else {
                    console.error('Lỗi tính phí ship:', data.message);
                    alert(`Lỗi tính phí ship: ${data.message}`);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert("Lỗi kết nối hoặc lỗi máy chủ, vui lòng thử lại sau.");
            });
    }


    function getAvailableServices(toDistrictId, toWardCode) {
        if (!toDistrictId || !toWardCode) {
            document.getElementById("spanShippingFee").textContent = "-";
            console.warn("Thông tin địa chỉ không đầy đủ để tính phí vận chuyển.");
            return;
        }

        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services', {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Token': apiKey
            }, body: JSON.stringify({
                shop_id: parseInt(shopId, 10),
                from_district: parseInt(fromDistrictId, 10),
                to_district: parseInt(toDistrictId, 10)
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200 && data.data?.length > 0) {
                    const serviceId = data.data[0].service_id;
                    console.log("Dịch vụ khả dụng:", data.data);
                    calculateShippingFee(serviceId, toDistrictId, toWardCode);
                } else {
                    console.warn("Không có dịch vụ vận chuyển khả dụng:", data.message || "Lỗi không xác định");
                    alert("Không có dịch vụ vận chuyển");
                }
            })
            .catch(error => {
                console.error('Lỗi khi kết nối đến API:', error);
                alert("Lỗi kết nối hoặc lỗi máy chủ");
            });
    }

    function autoCalculateShippingFee() {
        const selectedAddressElement = document.querySelector('.info-address-shipping input[type="radio"]:checked');
        if (!selectedAddressElement) {
            console.error("Không có địa chỉ nào được chọn.");
            return;
        }

        const addressTextElement = selectedAddressElement.closest('.info-address-shipping')?.querySelector('.original-address');
        if (!addressTextElement) {
            console.error("Không tìm thấy phần tử chứa địa chỉ.");
            return;
        }

        const fullAddress = addressTextElement.value?.trim();
        const {provinceId, districtId, wardCode} = extractAddressParts(fullAddress);

        if (!districtId || !wardCode) {
            console.error("Thiếu thông tin quận hoặc phường.");
            return;
        }

        // Cập nhật các thẻ DOM
        ['IdProvince', 'IdDistrict', 'IdWard'].forEach((id, idx) => {
            const value = [provinceId, districtId, wardCode][idx];
            document.getElementById(id).value = value;
        });

        getAvailableServices(districtId, wardCode);
    }

    function calculateTotalPrice() {
        const totalPriceCartItemElement = document.getElementById("spanTotalPriceCartItem");
        const priceVoucherElement = document.getElementById("spanPriceVoucher");
        const shippingFeeElement = document.getElementById("spanShippingFee");

        let totalPriceCartItem = parseFloat(totalPriceCartItemElement.textContent.replace(/[^\d.-]/g, '')) || 0;
        let priceVoucher = parseFloat(priceVoucherElement.textContent.replace(/[^\d.-]/g, '')) || 0;
        let shippingFee = parseFloat(shippingFeeElement.textContent.replace(/[^\d.-]/g, '')) || 0;

        // Định dạng lại các trường với toLocaleString và thêm ' đ'
        totalPriceCartItemElement.textContent = totalPriceCartItem.toLocaleString('en-US') + ' VNĐ';
        priceVoucherElement.textContent = priceVoucher.toLocaleString('en-US') + ' VNĐ';
        shippingFeeElement.textContent = shippingFee.toLocaleString('en-US') + ' VNĐ';

        // Tính tổng tiền
        const totalPriceBill = totalPriceCartItem - priceVoucher + shippingFee;

        // Hiển thị kết quả với định dạng số
        document.getElementById("spanTotalPriceBill").textContent = totalPriceBill.toLocaleString('en-US') + ' đ';
    }


    function extractAddressParts(fullAddress) {
        if (!fullAddress) {
            console.error("Địa chỉ trống.");
            return {provinceId: '', districtId: '', wardCode: ''};
        }

        const regex = /^(.*?),\s*(\d+),\s*(\d+),\s*.*$/;
        const match = fullAddress.match(regex);
        if (match) {
            const [, , districtId, wardCode] = match;
            return {provinceId: match[1], districtId, wardCode};
        } else {
            console.error("Địa chỉ không đúng định dạng:", fullAddress);
            return {provinceId: '', districtId: '', wardCode: ''};
        }
    }

    document.addEventListener('click', function (event) {
        if (event.target.closest('.change-address')) {
            // Lấy radio button được chọn
            const selectedAddressElement = document.querySelector('.info-address-shipping input[type="radio"]:checked');

            if (!selectedAddressElement) {
                console.error("Không có địa chỉ nào được chọn.");
                return;
            }

            // Tìm phần tử cha chứa thông tin địa chỉ từ radio button được chọn
            const parentElement = selectedAddressElement.closest('.info-address-shipping');
            if (!parentElement) {
                console.error("Không tìm thấy phần tử cha chứa địa chỉ.");
                return;
            }

            // Lấy thông tin tên và số điện thoại
            const nameAndPhoneNumber = parentElement.querySelector('.name-phoneNumber')?.textContent?.trim();
            const shortAddress = parentElement.querySelector('.short-address')?.textContent?.trim();

            if (!nameAndPhoneNumber || !shortAddress) {
                console.error("Thông tin không hợp lệ hoặc bị thiếu.");
                return;
            }

            // Hiển thị thông tin đã chọn
            document.getElementById('infoCustomer').textContent = 'Thông tin: ' + nameAndPhoneNumber + '.';
            document.getElementById('original-address').textContent = 'Địa chỉ: ' + shortAddress + '.';

            // Lấy địa chỉ đầy đủ
            const fullAddressInput = parentElement.querySelector('.full-address');
            if (fullAddressInput) {
                console.log('Địa chỉ đầy đủ:', fullAddressInput.value);
                document.getElementById('addressShip').value = fullAddressInput.value;
            } else {
                console.error('Không tìm thấy input chứa địa chỉ đầy đủ!');
            }

            // Đóng modal
            const modalElement = document.getElementById('changeAddressModal');
            if (modalElement) {
                modalElement.classList.remove('show');
                modalElement.style.display = 'none';
                document.body.classList.remove('modal-open');

                const backdrop = document.querySelector('.modal-backdrop');
                if (backdrop) backdrop.remove();
            }

            // Tính phí vận chuyển
            autoCalculateShippingFee();

            console.log("Cập nhật thành công: ", {nameAndPhoneNumber, shortAddress});
            createToast("1", "Cập nhật địa chỉ thành công");
        }
    });


});
document.addEventListener("DOMContentLoaded", function () {
    const apiKey = '0fc88a8e-6633-11ef-8e53-0a00184fe694';
    const provinceSelect = document.getElementById("province-create");
    const districtSelect = document.getElementById("district-create");
    const wardSelect = document.getElementById("ward-create");

    function fetchProvinces() {
        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province', {
            headers: {
                'Content-Type': 'application/json', 'Token': apiKey
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const provinces = data.data;
                    provinceSelect.innerHTML = '<option value="">Chọn Tỉnh</option>';
                    provinces.forEach(province => {
                        let option = document.createElement("option");
                        option.value = province.ProvinceID;
                        option.textContent = province.ProvinceName;
                        provinceSelect.appendChild(option);
                    });
                } else {
                    console.error('Lỗi lấy danh sách tỉnh:', data.message);
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function fetchDistricts(provinceId) {
        fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=${provinceId}`, {
            headers: {
                'Content-Type': 'application/json', 'Token': apiKey
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const districts = data.data;
                    districtSelect.innerHTML = '<option value="">Chọn Huyện</option>';
                    districts.forEach(district => {
                        let option = document.createElement("option");
                        option.value = district.DistrictID;
                        option.textContent = district.DistrictName;
                        districtSelect.appendChild(option);
                    });
                    wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>';
                } else {
                    console.error('Lỗi lấy danh sách huyện:', data.message);
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function fetchWards(districtId) {
        fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=${districtId}`, {
            headers: {
                'Content-Type': 'application/json', 'Token': apiKey
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const wards = data.data;
                    wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>';
                    wards.forEach(ward => {
                        let option = document.createElement("option");
                        option.value = ward.WardCode;
                        option.textContent = ward.WardName;
                        wardSelect.appendChild(option);
                    });
                } else {
                    console.error('Lỗi lấy danh sách xã/phường:', data.message);
                }
            })
            .catch(error => console.error('Error:', error));
    }

    provinceSelect.addEventListener("change", function () {
        const provinceId = provinceSelect.value;
        if (provinceId) {
            districtSelect.innerHTML = '<option value="">Đang tải...</option>';
            fetchDistricts(provinceId);
        } else {
            districtSelect.innerHTML = '<option value="">Chọn Huyện</option>';
            wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>';
        }
    });

    districtSelect.addEventListener("change", function () {
        const districtId = districtSelect.value;
        if (districtId) {
            wardSelect.innerHTML = '<option value="">Đang tải...</option>';
            fetchWards(districtId);
        } else {
            wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>';
        }
    });

    fetchProvinces();
});

document.addEventListener("DOMContentLoaded", function () {
    const accountLogin = document.getElementById("account-login").value;
    console.log("Account login: " + accountLogin);

    if (!accountLogin) {
        // Thêm
        const fullNameInput = document.getElementById("FullNameCreate");
        const emailInput = document.getElementById("MailCreate");
        const phoneInput = document.getElementById("PhoneCreate");
        const specificAddressInput = document.getElementById("specificAddressCreate");
        const provinceSelect = document.getElementById("province-create");
        const districtSelect = document.getElementById("district-create");
        const wardSelect = document.getElementById("ward-create");
        // Sửa
        const fullNameInputUpdate = document.getElementById("FullNameUpdate");
        const emailInputUpdate = document.getElementById("MailUpdate");
        const phoneInputUpdate = document.getElementById("PhoneUpdate");
        const specificAddressInputUpdate = document.getElementById("specificAddressUpdate");
        const provinceSelectUpdate = document.getElementById("province-update");
        const districtSelectUpdate = document.getElementById("district-update");
        const wardSelectUpdate = document.getElementById("ward-update");

        [fullNameInput, emailInput, phoneInput, specificAddressInput].forEach((input) => {
            input.addEventListener("input", function () {
                input.value = input.value.replace(/,/g, "");
            });
        });

        [fullNameInputUpdate, emailInputUpdate, phoneInputUpdate, specificAddressInputUpdate].forEach((input) => {
            input.addEventListener("input", function () {
                input.value = input.value.replace(/,/g, "");
            });
        });

        // Hàm validate Full Name
        function validateFullNameCreate() {
            const fullName = fullNameInput.value.trim();
            const errorFullName = document.getElementById("full-name-create-err");
            if (fullName === "") {
                errorFullName.textContent = "* Họ tên không được để trống.";
            } else if (fullName.length > 255) {
                errorFullName.textContent = "* Họ tên không được vượt quá 255 ký tự.";
            } else {
                errorFullName.textContent = "";
            }
        }

        // Hàm validate Email
        function validateEmailCreate() {
            const email = emailInput.value.trim();
            const errorEmail = document.getElementById("mail-create-err");
            const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (email === "") {
                errorEmail.textContent = "* Email không được để trống.";
            } else if (!emailPattern.test(email)) {
                errorEmail.textContent = "* Email không hợp lệ.";
            } else if (email.length > 100) {
                errorEmail.textContent = "* Email không được vượt quá 100 ký tự.";
            } else {
                errorEmail.textContent = ""; // Không có lỗi
            }
        }

        // Hàm validate Phone
        function validatePhoneCreate() {
            const phone = phoneInput.value.trim();
            const errorPhone = document.getElementById("phone-create-err");
            const phonePattern = /^[0-9]{10,11}$/;
            if (phone === "") {
                errorPhone.textContent = "* Số điện thoại không được để trống.";
            } else if (!phonePattern.test(phone)) {
                errorPhone.textContent = "* Số điện thoại không hợp lệ.";
            } else {
                errorPhone.textContent = "";
            }
        }

        // Hàm validate Specific Address
        function validateSpecificAddressCreate() {
            const specificAddress = specificAddressInput.value.trim();
            const errorAddress = document.getElementById("specific-address-create-err");
            if (specificAddress === "") {
                errorAddress.textContent = "* Địa chỉ cụ thể không được để trống.";
            } else if (specificAddress.length > 260) {
                errorAddress.textContent = "* Địa chỉ cụ thể không được vượt quá 260 ký tự.";
            } else {
                errorAddress.textContent = "";
            }
        }

        function validateAddressSelectionCreate() {
            const province = provinceSelect.value;
            const district = districtSelect.value;
            const ward = wardSelect.value;
            const errorSelectAddress = document.getElementById("selected-address-create-err");
            if (!province || !district || !ward) {
                errorSelectAddress.textContent = "* Bạn cần chọn đầy đủ Tỉnh, Huyện, và Xã/Phường";
            } else {
                errorSelectAddress.textContent = "";
            }
        }

        // Sửa
        // Hàm validate từng trường
        function validateFullNameUpdate() {
            const fullName = fullNameInputUpdate.value.trim();
            const errorFullName = document.getElementById("full-name-update-err");
            if (fullName === "") {
                errorFullName.textContent = "* Họ tên không được để trống.";
            } else if (fullName.length > 255) {
                errorFullName.textContent = "* Họ tên không được vượt quá 255 ký tự.";
            } else {
                errorFullName.textContent = "";
            }
        }

        function validateEmailUpdate() {
            const email = emailInputUpdate.value.trim();
            const errorEmail = document.getElementById("mail-update-err");
            const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (email === "") {
                errorEmail.textContent = "* Email không được để trống.";
            } else if (!emailPattern.test(email)) {
                errorEmail.textContent = "* Email không hợp lệ.";
            } else if (email.length > 100) {
                errorEmail.textContent = "* Email không được vượt quá 100 ký tự.";
            } else {
                errorEmail.textContent = "";
            }
        }

        function validatePhoneUpdate() {
            const phone = phoneInputUpdate.value.trim();
            const errorPhone = document.getElementById("phone-update-err");
            const phonePattern = /^[0-9]{10,11}$/;
            if (phone === "") {
                errorPhone.textContent = "* Số điện thoại không được để trống.";
            } else if (!phonePattern.test(phone)) {
                errorPhone.textContent = "* Số điện thoại không hợp lệ.";
            } else {
                errorPhone.textContent = "";
            }
        }

        function validateSpecificAddressUpdate() {
            const specificAddress = specificAddressInputUpdate.value.trim();
            const errorAddress = document.getElementById("specific-address-update-err");
            if (specificAddress === "") {
                errorAddress.textContent = "* Địa chỉ cụ thể không được để trống.";
            } else if (specificAddress.length > 260) {
                errorAddress.textContent = "* Địa chỉ cụ thể không được vượt quá 260 ký tự.";
            } else {
                errorAddress.textContent = "";
            }
        }

        function validateAddressSelectionUpdate() {
            const province = provinceSelectUpdate.value;
            const district = districtSelectUpdate.value;
            const ward = wardSelectUpdate.value;
            const errorSelectAddress = document.getElementById("selected-address-update-err");
            if (!province || !district || !ward) {
                errorSelectAddress.textContent = "* Bạn cần chọn đầy đủ Tỉnh, Huyện, và Xã/Phường.";
            } else {
                errorSelectAddress.textContent = "";
            }
        }

// Thêm sự kiện validate vào các trường
        fullNameInput.addEventListener("input", validateFullNameCreate);
        emailInput.addEventListener("input", validateEmailCreate);
        phoneInput.addEventListener("input", validatePhoneCreate);
        specificAddressInput.addEventListener("input", validateSpecificAddressCreate);
        provinceSelect.addEventListener("change", validateAddressSelectionCreate);
        districtSelect.addEventListener("change", validateAddressSelectionCreate);
        wardSelect.addEventListener("change", validateAddressSelectionCreate);

        fullNameInputUpdate.addEventListener("input", validateFullNameUpdate);
        emailInputUpdate.addEventListener("input", validateEmailUpdate);
        phoneInputUpdate.addEventListener("input", validatePhoneUpdate);
        specificAddressInputUpdate.addEventListener("input", validateSpecificAddressUpdate);
        provinceSelectUpdate.addEventListener("change", validateAddressSelectionUpdate);
        districtSelectUpdate.addEventListener("change", validateAddressSelectionUpdate);
        wardSelectUpdate.addEventListener("change", validateAddressSelectionUpdate);
    }
});

function createNewAddress() {
    let isValid = true;
    const fullNameInput = document.getElementById("FullNameCreate");
    const fullName = fullNameInput.value.trim();
    if (fullName === "") {
        document.getElementById("full-name-create-err").textContent = "* Họ tên không được để trống";
        isValid = false;
    } else if (fullName.includes(",")) {
        document.getElementById("full-name-create-err").textContent = "* Họ tên không được chứa dấu phẩy (,)";
        isValid = false;
    } else if (fullName.length > 250) {
        document.getElementById("full-name-create-err").textContent = "* Họ tên không được quá 250 ký tự";
        isValid = false;
    } else {
        document.getElementById("full-name-create-err").textContent = "";
    }

    // Validate email
    const emailInput = document.getElementById("MailCreate");
    const email = emailInput.value.trim();
    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (email === "") {
        document.getElementById("mail-create-err").textContent = "* Email không được để trống";
        isValid = false;
    } else if (!emailPattern.test(email)) {
        document.getElementById("mail-create-err").textContent = "* Email không hợp lệ";
        isValid = false;
    } else if (email.includes(",")) {
        document.getElementById("mail-create-err").textContent = "* Email không được chứa dấu phẩy (,)";
        isValid = false;
    } else {
        document.getElementById("mail-create-err").textContent = "";
    }

    const phoneInput = document.getElementById("PhoneCreate");
    const phone = phoneInput.value.trim();
    const phonePattern = /^(0[3|5|7|8|9])+([0-9]{8})$/;
    if (phone === "") {
        document.getElementById("phone-create-err").textContent = "* Số điện thoại không được để trống";
        isValid = false;
    } else if (!phonePattern.test(phone)) {
        document.getElementById("phone-create-err").textContent = "* Số điện thoại không hợp lệ";
        isValid = false;
    } else if (phone.includes(",")) {
        document.getElementById("phone-create-err").textContent = "* Số điện thoại không được chứa dấu phẩy (,)";
        isValid = false;
    } else {
        document.getElementById("phone-create-err").textContent = "";
    }

    // Validate địa chỉ cụ thể
    const specificAddressInput = document.getElementById("specificAddressCreate");
    const specificAddress = specificAddressInput.value.trim();
    if (specificAddress === "") {
        document.getElementById("specific-address-create-err").textContent = "* Vui lòng nhập địa chỉ nhận hàng cụ thể.";
        isValid = false;
    } else if (specificAddress.includes(",")) {
        document.getElementById("specific-address-create-err").textContent = "* Địa chỉ không được chứa dấu phẩy (,)";
        isValid = false;
    } else {
        document.getElementById("specific-address-create-err").textContent = "";
    }

    // Validate dropdown địa chỉ
    const provinceSelect = document.getElementById("province-create");
    const districtSelect = document.getElementById("district-create");
    const wardSelect = document.getElementById("ward-create");
    if (!provinceSelect.value || !districtSelect.value || !wardSelect.value) {
        document.getElementById("selected-address-create-err").textContent = "* Bạn cần chọn đầy đủ Tỉnh, Huyện, và Xã/Phường.";
        isValid = false;
    } else {
        document.getElementById("selected-address-create-err").textContent = "";
    }
    if (!isValid) {
        return;
    }
    Swal.fire({
        title: 'Bạn có chắc chắn muốn thêm địa chỉ này hay không này?',
        text: "Sau khi xác nhận, sẽ thêm mới địa chỉ này.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        customClass: {
            popup: 'swal-popup'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const fullName = document.getElementById("FullNameCreate").value;
            const phone = document.getElementById("PhoneCreate").value;
            const mail = document.getElementById("MailCreate").value;

            // Lấy giá trị từ các select box
            const provinceId = document.getElementById("province-create").value;
            const districtId = document.getElementById("district-create").value;
            const wardCode = document.getElementById("ward-create").value;

            // Lấy tên hiển thị của tỉnh, huyện, xã
            const province = document.getElementById("province-create").options[document.getElementById("province-create").selectedIndex].text;
            const district = document.getElementById("district-create").options[document.getElementById("district-create").selectedIndex].text;
            const ward = document.getElementById("ward-create").options[document.getElementById("ward-create").selectedIndex].text;

            // Lấy địa chỉ cụ thể
            const specificAddress = document.getElementById("specificAddressCreate").value;

            // Ghép địa chỉ đầy đủ
            const fullAddressText = `${specificAddress},${ward},${district},${province}`;
            const addressForCustomerText = `${fullName},${phone},${mail},${provinceId},${districtId},${wardCode},${fullAddressText}`


            // Gửi yêu cầu AJAX
            $.ajax({
                url: "/api-client/new-address-customer",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(addressForCustomerText),
                success: function (response) {
                    createToast(response.check, response.message);
                    listAddressForCustomer();
                    $("#addNewAddressModal").modal("hide");
                    $("#changeAddressModal").modal("show");
                },
                error: function (xhr) {
                    alert("Có lỗi xảy ra khi thêm địa chỉ: " + xhr.responseText);
                }
            });
        }
    });
}


document.addEventListener("DOMContentLoaded", function () {
    const apiKey = '0fc88a8e-6633-11ef-8e53-0a00184fe694';
    const provinceSelect = document.getElementById("province-update");
    const districtSelect = document.getElementById("district-update");
    const wardSelect = document.getElementById("ward-update");

    function fetchProvinces() {
        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province', {
            headers: {
                'Content-Type': 'application/json', 'Token': apiKey
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const provinces = data.data;
                    provinceSelect.innerHTML = '<option value="">Chọn Tỉnh</option>';
                    provinces.forEach(province => {
                        let option = document.createElement("option");
                        option.value = province.ProvinceID;
                        option.textContent = province.ProvinceName;
                        provinceSelect.appendChild(option);
                    });
                } else {
                    console.error('Lỗi lấy danh sách tỉnh:', data.message);
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function fetchDistricts(provinceId) {
        fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=${provinceId}`, {
            headers: {
                'Content-Type': 'application/json', 'Token': apiKey
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const districts = data.data;
                    districtSelect.innerHTML = '<option value="">Chọn Huyện</option>';
                    districts.forEach(district => {
                        let option = document.createElement("option");
                        option.value = district.DistrictID;
                        option.textContent = district.DistrictName;
                        districtSelect.appendChild(option);
                    });
                    wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>'; // Xóa danh sách xã khi chọn huyện mới
                    $(districtSelect).trigger('change'); // Kích hoạt sự kiện change nếu cần
                } else {
                    console.error('Lỗi lấy danh sách huyện:', data.message);
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function fetchWards(districtId) {
        fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=${districtId}`, {
            headers: {
                'Content-Type': 'application/json', 'Token': apiKey
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const wards = data.data;
                    wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>';
                    wards.forEach(ward => {
                        let option = document.createElement("option");
                        option.value = ward.WardCode;
                        option.textContent = ward.WardName;
                        wardSelect.appendChild(option);
                    });
                } else {
                    console.error('Lỗi lấy danh sách xã/phường:', data.message);
                }
            })
            .catch(error => console.error('Error:', error));
    }

    provinceSelect.addEventListener("change", function () {
        const provinceId = provinceSelect.value;
        if (provinceId) {
            districtSelect.innerHTML = '<option value="">Đang tải...</option>';
            fetchDistricts(provinceId);
        } else {
            districtSelect.innerHTML = '<option value="">Chọn Huyện</option>';
            wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>';
        }
    });

    districtSelect.addEventListener("change", function () {
        const districtId = districtSelect.value;
        if (districtId) {
            wardSelect.innerHTML = '<option value="">Đang tải...</option>';
            fetchWards(districtId);
        } else {
            wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>';
        }
    });

    // Gọi hàm lấy danh sách tỉnh khi tải trang
    fetchProvinces();


});

let selectedAddressId = null;
$(document).on('click', '.btn-update-address', function () {
    // Lấy phần tử cha gần nhất có class "info-address-shipping"
    const parentElement = $(this).closest('.info-address-shipping');
    if (parentElement.length) {
        // Gán giá trị ID vào biến toàn cục `selectedAddressId`
        selectedAddressId = parentElement.find('.id-address').val();
        console.log("ID địa chỉ được chọn:", selectedAddressId);
    } else {
        console.error("Không tìm thấy phần tử cha với class .info-address-shipping");
    }
});


function getAddressDetails(buttonElement) {
    var originalAddress = $(buttonElement).closest('.info-address-shipping').find('.full-address').val();
    console.log("Original Address: " + originalAddress);

    if (!originalAddress) {
        console.error("Original address không tồn tại hoặc bị null.");
        return;
    }

    // Tách chuỗi originalAddress thành các phần
    var addressParts = originalAddress.split(',');

    // Kiểm tra số lượng phần tử
    if (addressParts.length < 7) {
        console.error("Original address không đúng định dạng hoặc thiếu thông tin.");
        return;
    }

    // Lấy các giá trị từ mảng sau khi tách
    var fullName = addressParts[0]?.trim(); // Phần tử đầu tiên là họ tên
    var phoneNumber = addressParts[1]?.trim();
    var email = addressParts[2]?.trim();
    var provinceId = addressParts[3]?.trim(); // Lấy ID tỉnh
    var districtId = addressParts[4]?.trim(); // Lấy ID quận
    var wardCode = addressParts[5]?.trim();  // Lấy ID xã/phường
    var specificAddress = addressParts[6]?.trim();

    console.log("Full name:", fullName);
    console.log("Province ID:", provinceId);
    console.log("District ID:", districtId);
    console.log("Ward Code:", wardCode);

    // Gán các giá trị vào các input
    document.getElementById("FullNameUpdate").value = fullName || '';
    document.getElementById("PhoneUpdate").value = phoneNumber || '';
    document.getElementById("MailUpdate").value = email || '';
    document.getElementById("specificAddressUpdate").value = specificAddress || '';

    // Gán các giá trị vào các select dựa trên ID
    var provinceSelect = document.getElementById("province-update");
    var districtSelect = document.getElementById("district-update");
    var wardSelect = document.getElementById("ward-update");

    if (provinceSelect) {
        // Gán ID cho tỉnh
        provinceSelect.value = provinceId;
        $(provinceSelect).trigger('change'); // Kích hoạt sự kiện change để load danh sách huyện

        // Đợi sự kiện change tỉnh và sau đó cập nhật quận
        $(provinceSelect).on('change', function () {
            if (districtSelect) {
                // Gán ID cho quận
                districtSelect.value = districtId;
                $(districtSelect).trigger('change'); // Kích hoạt sự kiện change để load danh sách xã/phường
            }
        });
    }

    if (districtSelect) {
        // Đợi sự kiện change quận và sau đó cập nhật xã/phường
        $(districtSelect).on('change', function () {
            if (wardSelect) {
                // Gán ID cho xã/phường
                wardSelect.value = wardCode;
            }
        });
    }
}
// Hàm cập nhật địa chỉ khi ấn nút "Cập nhật"
function updateAddress() {
    let isValid = true;

// Validate họ tên
    const fullNameInputUpdate = document.getElementById("FullNameUpdate");
    const fullNameUpdate = fullNameInputUpdate.value.trim();
    if (fullNameUpdate === "") {
        document.getElementById("full-name-update-err").textContent = "* Họ tên không được để trống";
        isValid = false;
    } else if (fullNameUpdate.includes(",")) {
        document.getElementById("full-name-update-err").textContent = "* Họ tên không được chứa dấu phẩy (,)";
        isValid = false;
    } else if (fullNameUpdate.length > 250) {
        document.getElementById("full-name-update-err").textContent = "* Họ tên không được quá 250 ký tự";
        isValid = false;
    } else {
        document.getElementById("full-name-update-err").textContent = "";
    }

// Validate email
    const emailInputUpdate = document.getElementById("MailUpdate");
    const emailUpdate = emailInputUpdate.value.trim();
    const emailPatternUpdate = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (emailUpdate === "") {
        document.getElementById("mail-update-err").textContent = "* Email không được để trống";
        isValid = false;
    } else if (!emailPatternUpdate.test(emailUpdate)) {
        document.getElementById("mail-update-err").textContent = "* Email không hợp lệ";
        isValid = false;
    } else if (emailUpdate.includes(",")) {
        document.getElementById("mail-update-err").textContent = "* Email không được chứa dấu phẩy (,)";
        isValid = false;
    } else {
        document.getElementById("mail-update-err").textContent = "";
    }

// Validate số điện thoại
    const phoneInputUpdate = document.getElementById("PhoneUpdate");
    const phoneUpdate = phoneInputUpdate.value.trim();
    const phonePatternUpdate = /^(0[3|5|7|8|9])+([0-9]{8})$/;
    if (phoneUpdate === "") {
        document.getElementById("phone-update-err").textContent = "* Số điện thoại không được để trống";
        isValid = false;
    } else if (!phonePatternUpdate.test(phoneUpdate)) {
        document.getElementById("phone-update-err").textContent = "* Số điện thoại không hợp lệ";
        isValid = false;
    } else if (phoneUpdate.includes(",")) {
        document.getElementById("phone-update-err").textContent = "* Số điện thoại không được chứa dấu phẩy (,)";
        isValid = false;
    } else {
        document.getElementById("phone-update-err").textContent = "";
    }

// Validate địa chỉ cụ thể
    const specificAddressInputUpdate = document.getElementById("specificAddressUpdate");
    const specificAddressUpdate = specificAddressInputUpdate.value.trim();
    if (specificAddressUpdate === "") {
        document.getElementById("specific-address-update-err").textContent = "* Vui lòng nhập địa chỉ nhận hàng cụ thể.";
        isValid = false;
    } else if (specificAddressUpdate.includes(",")) {
        document.getElementById("specific-address-update-err").textContent = "* Địa chỉ không được chứa dấu phẩy (,)";
        isValid = false;
    } else {
        document.getElementById("specific-address-update-err").textContent = "";
    }

// Validate dropdown địa chỉ
    const provinceSelectUpdate = document.getElementById("province-update");
    const districtSelectUpdate = document.getElementById("district-update");
    const wardSelectUpdate = document.getElementById("ward-update");
    if (!provinceSelectUpdate.value || !districtSelectUpdate.value || !wardSelectUpdate.value) {
        document.getElementById("selected-address-update-err").textContent = "* Bạn cần chọn đầy đủ Tỉnh, Huyện, và Xã/Phường.";
        isValid = false;
    } else {
        document.getElementById("selected-address-update-err").textContent = "";
    }

    if (!isValid) {
        return;
    }

// Logic xử lý tiếp theo nếu tất cả validate đều hợp lệ

    Swal.fire({
        title: 'Bạn có chắc chắn muốn cập nhật địa chỉ này hay không?',
        text: "Sau khi xác nhận, địa chỉ của bạn sẽ được cập nhật.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        customClass: {
            popup: 'swal-popup'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const fullName = document.getElementById("FullNameUpdate").value;
            const phone = document.getElementById("PhoneUpdate").value;
            const mail = document.getElementById("MailUpdate").value;
            const provinceId = document.getElementById("province-update").value;
            const districtId = document.getElementById("district-update").value;
            const wardCode = document.getElementById("ward-update").value;

            const province = document.getElementById("province-update").options[document.getElementById("province-update").selectedIndex].text;
            const district = document.getElementById("district-update").options[document.getElementById("district-update").selectedIndex].text;
            const ward = document.getElementById("ward-update").options[document.getElementById("ward-update").selectedIndex].text;

            const specificAddress = document.getElementById("specificAddressUpdate").value;

            const fullAddressText = `${specificAddress}, ${ward}, ${district}, ${province}`;
            const addressForCustomerText = `${fullName},${phone},${mail},${provinceId},${districtId},${wardCode},${fullAddressText}`;
            console.log("ID dia chi khi update: " + selectedAddressId);
            if (!selectedAddressId) {
                alert("Bạn chưa chọn địa chỉ để cập nhật!");
                return;
            }

            // Chuẩn bị dữ liệu gửi đến server
            const addressForCustomerRequest = {
                addressCustomer: addressForCustomerText
            };

            // Gửi request AJAX
            $.ajax({
                url: '/api-client/update-address-customer/' + selectedAddressId,
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(addressForCustomerRequest),
                success: function (response) {
                    createToast(response.check, response.message);
                    listAddressForCustomer();
                    $("#updateAddressModal").modal("hide");
                    $("#changeAddressModal").modal("show");
                },
                error: function (xhr) {
                    console.error("Error status:", xhr.status);
                    console.error("Error response:", xhr.responseText);
                    alert("Có lỗi xảy ra khi cập nhật địa chỉ: " + xhr.responseText);
                }
            });
        }
    });
}


// AJAX để lấy danh sách địa chỉ
function listAddressForCustomer() {
    $.ajax({
        type: 'GET', url: '/api-client/list-address-for-customer', success: function (response) {
            var div = $('#info-address-for-customer');
            var noData = $('#noData-info-address-for-customer');
            div.empty();

            if (response.length === 0) {
                noData.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có địa chỉ nào!</p>
                    `);
                noData.show();
                div.hide();
            } else {
                noData.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
                div.show(); // Hiển thị lại table nếu có dữ liệu

                response.forEach(function (listAddress, index) {
                    div.append(`
                    <div class="list-address-customer">
                        <div class="info-address-shipping d-flex align-items-center border rounded p-2 mb-2 position-relative">
                            <!-- Radio button -->
                            <input class="form-check-input me-3" type="radio" name="selectedAddress"/>
                            <!-- Thông tin địa chỉ -->
                            <div class="address-info flex-grow-1">
                                <input type="hidden" class="id-address" value="${listAddress.id}"/>
                                <p class="name-phoneNumber mb-1">${listAddress.nameAndPhoneNumber}</p>
                                <p class="short-address mb-0 text-muted">${listAddress.shortAddress}</p>
                                <input type="hidden" class="original-address" value="${listAddress.originalAddress}"/>
                                <input type="hidden" id="fullAddressInput" value="${listAddress.fullAddress}" class="full-address"/>
                            </div>
                            <!-- Nút hành động -->
                            <div class="button-container d-flex gap-1">
                                <button class="btn-update-address btn btn-sm btn-outline-primary"
                                        type="button" data-bs-toggle="modal"
                                        data-bs-target="#updateAddressModal">
                                    <i class="fas fa-sync-alt"></i>
                                </button>
                                <button class="btn-delete-address btn btn-sm btn-outline-danger"
                                        type="button" onclick="deleteAddressForCustomer(${listAddress.id})">
                                    <i class="fas fa-trash-alt"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                `);
                });
            }
        }, error: function (xhr) {
            console.error('Lỗi: ' + xhr.responseText);
        }
    });
}

// Khi trang đã sẵn sàng
$(document).ready(function () {
    listAddressForCustomer();
});
