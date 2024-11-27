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
    console.log("Total amount ship: " + totalPriceCartItem)

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
                    document.getElementById("spanShippingFee").textContent = `${shippingFee} VND`;
                    console.log("Gia ship: " + shippingFee)

                    // Tính lại tổng tiền sau khi cập nhật phí vận chuyển
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
        const totalPriceCartItem = parseFloat(document.getElementById("spanTotalPriceCartItem").textContent.replace(/[^0-9.]/g, '').replace(',', '.')) || 0;
        const priceVoucher = parseFloat(document.getElementById("spanPriceVoucher").textContent.replace(/[^0-9.]/g, '').replace(',', '.')) || 0;
        const shippingFee = parseFloat(document.getElementById("spanShippingFee").textContent.replace(/[^0-9.]/g, '').replace(',', '.')) || 0;

        console.log("Ship: " + shippingFee);
        const totalPriceBill = totalPriceCartItem - priceVoucher + shippingFee;
        console.log("totalPriceBill: " + totalPriceBill);

        document.getElementById("spanTotalPriceBill").textContent = totalPriceBill.toFixed(2);
    }

    autoCalculateShippingFee();
});
// Channge address
document.addEventListener("DOMContentLoaded", function () {
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
                    document.getElementById("spanShippingFee").textContent = `${shippingFee} VND`;
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
        const totalPriceCartItem = parseFloat(document.getElementById("spanTotalPriceCartItem").textContent.replace(/[^0-9.]/g, '').replace(',', '.')) || 0;
        const priceVoucher = parseFloat(document.getElementById("spanPriceVoucher").textContent.replace(/[^0-9.]/g, '').replace(',', '.')) || 0;
        const shippingFee = parseFloat(document.getElementById("spanShippingFee").textContent.replace(/[^0-9.]/g, '').replace(',', '.')) || 0;
        const totalPriceBill = totalPriceCartItem - priceVoucher + shippingFee;
        document.getElementById("spanTotalPriceBill").textContent = totalPriceBill.toFixed(2);
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

            // Lấy phần tử cha chứa thông tin địa chỉ
            const parentElement = selectedAddressElement.closest('.info-address-shipping');
            if (!parentElement) {
                console.error("Không tìm thấy phần tử cha chứa địa chỉ.");
                return;
            }

            // Truy xuất dữ liệu từ các trường
            const nameAndPhoneNumber = parentElement.querySelector('.name-phoneNumber')?.textContent?.trim();
            const shortAddress = parentElement.querySelector('.short-address')?.textContent?.trim();

            if (!nameAndPhoneNumber || !shortAddress) {
                console.error("Thông tin không hợp lệ hoặc bị thiếu.");
                return;
            }

            document.getElementById('infoCustomer').textContent = nameAndPhoneNumber + '.';
            document.getElementById('original-address').textContent = shortAddress + '.';

            const addressFull = document.getElementById('fullAddressInput');
            if (addressFull) {
                console.log('Địa chỉ đầy đủ:', addressFull.value);
                document.getElementById('addressShip').value = addressFull.value;
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
            autoCalculateShippingFee()
            console.log("Cập nhật thành công: ", {nameAndPhoneNumber, shortAddress});
        }
    });
// Thêm địa chỉ mới
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
                    wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>'; // Xóa danh sách xã khi chọn huyện mới
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

function createNewAddress() {
    // Lấy giá trị từ các trường nhập
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
            alert(response);
            $("#addNewAddressModal").modal("hide");
        },
        error: function (xhr) {
            alert("Có lỗi xảy ra khi thêm địa chỉ: " + xhr.responseText);
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

function getAddressDetails(buttonElement) {
    // Lấy giá trị original address từ input hidden
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

document.querySelectorAll('.btn-update-address').forEach(button => {
    button.addEventListener('click', function () {
        // Lấy ID của địa chỉ từ hidden input trong phần tử cha
        selectedAddressId = this.closest('.info-address-shipping').querySelector('.id-address').value;
        console.log("ID địa chỉ được chọn:", selectedAddressId);
    });
});

// Hàm cập nhật địa chỉ khi ấn nút "Cập nhật"
function updateAddress() {
    // Lấy thông tin từ các trường nhập liệu
    const fullName = document.getElementById("FullNameUpdate").value;
    const phone = document.getElementById("PhoneUpdate").value;
    const mail = document.getElementById("MailUpdate").value;
    const provinceId = document.getElementById("province-update").value;
    const districtId = document.getElementById("district-update").value;
    const wardCode = document.getElementById("ward-update").value;

    // Lấy tên tỉnh, huyện, xã
    const province = document.getElementById("province-update").options[document.getElementById("province-update").selectedIndex].text;
    const district = document.getElementById("district-update").options[document.getElementById("district-update").selectedIndex].text;
    const ward = document.getElementById("ward-update").options[document.getElementById("ward-update").selectedIndex].text;

    const specificAddress = document.getElementById("specificAddressUpdate").value;

    // Ghép thành địa chỉ đầy đủ
    const fullAddressText = `${specificAddress}, ${ward}, ${district}, ${province}`;
    const addressForCustomerText = `${fullName},${phone},${mail},${provinceId},${districtId},${wardCode},${fullAddressText}`;

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
            console.log("Response từ server:", response);
            alert("Cập nhật thành công!");
            $("#updateAddressModal").modal("hide");
        },
        error: function (xhr) {
            console.error("Error status:", xhr.status);
            console.error("Error response:", xhr.responseText);
            alert("Có lỗi xảy ra khi cập nhật địa chỉ: " + xhr.responseText);
        }
    });
}

