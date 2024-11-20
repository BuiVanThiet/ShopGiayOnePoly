document.addEventListener("DOMContentLoaded", function () {
    const apiKey = '0fc88a8e-6633-11ef-8e53-0a00184fe694';
    const shopId = '195165';
    const fromDistrictId = 3440;
    let shippingFeeCalculated = false; // Cờ để kiểm soát việc tính lại phí vận chuyển
    const weightText = $('#weightShip').text().trim();
    if (weightText === "") {
        alert("Không có thông tin về cân nặng.");
        return;
    }

    const weight = parseFloat(weightText.replace(/[^0-9.-]+/g, ''));
    if (isNaN(weight)) {
        alert("Cân nặng không hợp lệ.");
        return;
    }
    const totalPriceCartItemText = $('#spanTotalPriceCartItem').text().trim();
    if (totalPriceCartItemText === "") {
        alert("Không có thông tin về cân nặng.");
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
        console.log("Service ID :" + serviceId)
        console.log("toDistrictId ID :" + toDistrictId)
        console.log("toWardCode ID :" + toWardCode)
        if (!serviceId || !toDistrictId || !toWardCode) {
            updateShippingFeeDisplay("Không thể tính phí ship");
            return;
        }

        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Token': apiKey
            },
            body: JSON.stringify({
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
                    updateShippingFeeDisplay(`${shippingFee} VND`);
                    calculateTotalPrice();
                    shippingFeeCalculated = true;
                } else {
                    console.error('Lỗi tính phí ship:', data.message);
                    updateShippingFeeDisplay("Không thể tính phí ship");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                updateShippingFeeDisplay("Lỗi kết nối hoặc lỗi máy chủ");
            });
    }

    function getAvailableServices(toDistrictId, toWardCode) {
        if (!toDistrictId || !toWardCode) {
            updateShippingFeeDisplay("-");
            return;
        }

        if (shippingFeeCalculated) {
            console.log('Phí vận chuyển đã được tính, không gọi lại hàm');
            return;
        }

        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Token': apiKey
            },
            body: JSON.stringify({
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
                } else {
                    console.error('Không có dịch vụ vận chuyển:', data.message);
                    updateShippingFeeDisplay("Không có dịch vụ vận chuyển");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                updateShippingFeeDisplay("Lỗi kết nối hoặc lỗi máy chủ");
            });
    }

    function autoCalculateShippingFee() {
        const toDistrictId = document.getElementById("IdDistrict").textContent.trim();
        const toWardCode = document.getElementById("IdWard").textContent.trim();
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

    function updateShippingFeeDisplay(message) {
        document.getElementById("spanShippingFee").textContent = message;
    }

    autoCalculateShippingFee();
});
document.addEventListener("DOMContentLoaded", function () {
    const spanPriceVoucher = document.getElementById("spanPriceVoucher");
    const voucherPriceText = spanPriceVoucher.textContent.trim();
    const priceVoucher = parseFloat(voucherPriceText.replace(/[^0-9.-]+/g, ''));

    if (isNaN(priceVoucher)) {
        spanPriceVoucher.textContent = "-";
    }
});

// Channge address
document.addEventListener("DOMContentLoaded", function () {
    const apiKey = '0fc88a8e-6633-11ef-8e53-0a00184fe694';
    const shopId = '195165';
    const fromDistrictId = 3440;
    let shippingFeeCalculated = false; // Cờ để kiểm soát việc tính lại phí vận chuyển
    const weightText = $('#weightShip').text().trim();
    if (weightText === "") {
        alert("Không có thông tin về cân nặng.");
        return;
    }

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

    function calculateShippingFee(serviceId, toDistrictId, toWardCode) {
        console.log("Service ID:", serviceId);
        console.log("To District ID:", toDistrictId);
        console.log("To Ward Code:", toWardCode);

        if (!serviceId || !toDistrictId || !toWardCode) {
            updateShippingFeeDisplay("Không thể tính phí ship");
            return;
        }

        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Token': apiKey
            },
            body: JSON.stringify({
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
                    const shippingFee = data.data.total;
                    updateShippingFeeDisplay(`${shippingFee} VND`);
                    calculateTotalPrice();
                    shippingFeeCalculated = true;
                } else {
                    console.error('Lỗi tính phí ship:', data.message);
                    updateShippingFeeDisplay("Không thể tính phí ship");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                updateShippingFeeDisplay("Lỗi kết nối hoặc lỗi máy chủ");
            });
    }

    function getAvailableServices(toDistrictId, toWardCode) {
        if (!toDistrictId || !toWardCode) {
            updateShippingFeeDisplay("-");
            return;
        }

        if (shippingFeeCalculated) {
            console.log('Phí vận chuyển đã được tính, không gọi lại hàm');
            return;
        }

        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Token': apiKey
            },
            body: JSON.stringify({
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
                } else {
                    console.error('Không có dịch vụ vận chuyển:', data.message);
                    updateShippingFeeDisplay("Không có dịch vụ vận chuyển");
                }
            })
            .catch(error => {
                console.error('Error:', error);
                updateShippingFeeDisplay("Lỗi kết nối hoặc lỗi máy chủ");
            });
    }

    function autoCalculateShippingFee() {
        const toDistrictId = document.getElementById("IdDistrict").textContent.trim();
        const toWardCode = document.getElementById("IdWard").textContent.trim();
        getAvailableServices(toDistrictId, toWardCode);
    }

    function calculateTotalPrice() {
        const totalPriceCartItem = parseFloat(document.getElementById("spanTotalPriceCartItem").textContent.replace(/[^0-9.]/g, '').replace(',', '.')) || 0;
        const priceVoucher = parseFloat(document.getElementById("spanPriceVoucher").textContent.replace(/[^0-9.]/g, '').replace(',', '.')) || 0;
        const shippingFee = parseFloat(document.getElementById("spanShippingFee").textContent.replace(/[^0-9.]/g, '').replace(',', '.')) || 0;

        const totalPriceBill = totalPriceCartItem - priceVoucher + shippingFee;
        document.getElementById("spanTotalPriceBill").textContent = totalPriceBill.toFixed(2);
    }

    function updateShippingFeeDisplay(message) {
        document.getElementById("spanShippingFee").textContent = message;
    }

    function changeAddress() {
        const saveButton = document.querySelector('.change-address');
        const addressElements = document.querySelectorAll('.info-address-shipping');
        const idWardElement = document.getElementById('IdWard');
        const idDistrictElement = document.getElementById('IdDistrict');
        const idProvinceElement = document.getElementById('IdProvince');
        const originalAddressElement = document.getElementById('original-address');
        const modalElement = document.getElementById('changeAddressModal');

        if (!saveButton || !addressElements || !idWardElement || !idDistrictElement || !idProvinceElement || !originalAddressElement) {
            console.error('Các phần tử cần thiết không tồn tại trong DOM.');
            return;
        }

        saveButton.addEventListener('click', function () {
            const selectedAddress = Array.from(addressElements).find(address =>
                address.querySelector('input[type="radio"]').checked
            );

            if (selectedAddress) {
                const addressTextElement = selectedAddress.querySelector('.original-address');
                if (!addressTextElement) {
                    console.error('Không tìm thấy phần tử chứa địa chỉ.');
                    return;
                }

                const fullAddress = addressTextElement.textContent.trim();
                const addressParts = fullAddress.split(',');

                // Lấy từng phần của địa chỉ
                idWardElement.textContent = addressParts[0] ? addressParts[0].trim() : '';
                idDistrictElement.textContent = addressParts[1] ? addressParts[1].trim() : '';
                idProvinceElement.textContent = addressParts[2] ? addressParts[2].trim() : '';

                // Phần còn lại của địa chỉ sau khi cắt
                const remainingAddress = addressParts.slice(3).join(',').trim();
                originalAddressElement.textContent = remainingAddress;

                shippingFeeCalculated = false; // Reset trạng thái đã tính phí
                autoCalculateShippingFee(); // Tính lại phí vận chuyển

                if (modalElement) {
                    modalElement.classList.remove('show');
                    modalElement.style.display = 'none';
                    document.body.classList.remove('modal-open');
                    const backdrop = document.querySelector('.modal-backdrop');
                    if (backdrop) backdrop.remove(); // Loại bỏ lớp nền mờ
                }
            } else {
                alert('Vui lòng chọn một địa chỉ.');
            }
        });
    }

    changeAddress();
    autoCalculateShippingFee();
});
document.addEventListener("DOMContentLoaded", function () {
    const apiKey = '0fc88a8e-6633-11ef-8e53-0a00184fe694';

    // Các phần tử `select`
    const provinceSelect = document.getElementById("province-create");
    const districtSelect = document.getElementById("district-create");
    const wardSelect = document.getElementById("ward-create");

    // Lấy danh sách tỉnh
    function fetchProvinces() {
        fetch('https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province', {
            headers: {
                'Content-Type': 'application/json',
                'Token': apiKey
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const provinces = data.data;
                    provinceSelect.innerHTML = '<option value="">Chọn Tỉnh</option>';
                    provinces.forEach(province => {
                        const option = document.createElement("option");
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

    // Lấy danh sách huyện theo tỉnh
    function fetchDistricts(provinceId) {
        fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district?province_id=${provinceId}`, {
            headers: {
                'Content-Type': 'application/json',
                'Token': apiKey
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const districts = data.data;
                    districtSelect.innerHTML = '<option value="">Chọn Huyện</option>';
                    districts.forEach(district => {
                        const option = document.createElement("option");
                        option.value = district.DistrictID;
                        option.textContent = district.DistrictName;
                        districtSelect.appendChild(option);
                    });
                } else {
                    console.error('Lỗi lấy danh sách huyện:', data.message);
                }
            })
            .catch(error => console.error('Error:', error));
    }

    // Lấy danh sách xã/phường theo huyện
    function fetchWards(districtId) {
        fetch(`https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=${districtId}`, {
            headers: {
                'Content-Type': 'application/json',
                'Token': apiKey
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const wards = data.data;
                    wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>';
                    wards.forEach(ward => {
                        const option = document.createElement("option");
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

    // Sự kiện thay đổi tỉnh -> Lấy huyện
    provinceSelect.addEventListener("change", function () {
        const provinceId = provinceSelect.value;
        if (provinceId) {
            fetchDistricts(provinceId);
        } else {
            districtSelect.innerHTML = '<option value="">Chọn Huyện</option>';
            wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>';
        }
    });

    // Sự kiện thay đổi huyện -> Lấy xã/phường
    districtSelect.addEventListener("change", function () {
        const districtId = districtSelect.value;
        if (districtId) {
            fetchWards(districtId);
        } else {
            wardSelect.innerHTML = '<option value="">Chọn Xã, Phường</option>';
        }
    });

    // Gọi hàm khởi tạo để tải danh sách tỉnh ban đầu
    fetchProvinces();
})
function createNewAddress() {
    // Lấy dữ liệu từ các trường nhập
    let fullName = $("#FullNameCreate").val();
    let phone = $("#PhoneCreate").val();
    let email = $("#MailCreate").val();
    let province = $("#province-create").val();
    let district = $("#district-create").val();
    let ward = $("#ward-create").val();
    let specificAddress = $("#specificAddress").val();

    // Tạo request payload
    let addressRequest = {
        nameCustomer: fullName,
        phoneNumber: phone,
        emailCustomer: email,
        addressCustomer: `${ward},${district},${province},${specificAddress}`
    };

    // Gửi yêu cầu AJAX
    $.ajax({
        url: "/api-client/new-address-customer",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(addressRequest),
        success: function (response) {
            alert(response); // Hiển thị thông báo thành công
            $("#addNewAddressModal").modal("hide"); // Ẩn modal
        },
        error: function (xhr) {
            alert("Có lỗi xảy ra khi thêm địa chỉ: " + xhr.responseText);
        }
    });
}
