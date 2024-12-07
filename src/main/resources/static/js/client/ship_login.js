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
                    document.getElementById("spanShippingFee").textContent = shippingFee.toLocaleString('en-US') + ' đ';
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

        // Tính tổng tiền
        const totalPriceBill = totalPriceCartItem - priceVoucher + shippingFee;

        // Định dạng lại các giá trị và hiển thị
        totalPriceCartItemElement.textContent = totalPriceCartItem.toLocaleString('en-US') + ' đ';
        priceVoucherElement.textContent = priceVoucher.toLocaleString('en-US') + ' đ';
        shippingFeeElement.textContent = shippingFee.toLocaleString('en-US') + ' đ';
        totalPriceBillElement.textContent = totalPriceBill.toLocaleString('en-US') + ' đ';
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
                    document.getElementById("spanShippingFee").textContent = `${shippingFee} ₫`;
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
        // Lấy giá trị từ textContent, loại bỏ ký tự không phải số
        const totalPriceCartItemElement = document.getElementById("spanTotalPriceCartItem");
        const priceVoucherElement = document.getElementById("spanPriceVoucher");
        const shippingFeeElement = document.getElementById("spanShippingFee");

        let totalPriceCartItem = parseFloat(totalPriceCartItemElement.textContent.replace(/[^\d.-]/g, '')) || 0;
        let priceVoucher = parseFloat(priceVoucherElement.textContent.replace(/[^\d.-]/g, '')) || 0;
        let shippingFee = parseFloat(shippingFeeElement.textContent.replace(/[^\d.-]/g, '')) || 0;

        // Định dạng lại các trường với toLocaleString và thêm ' đ'
        totalPriceCartItemElement.textContent = totalPriceCartItem.toLocaleString('en-US') + ' đ';
        priceVoucherElement.textContent = priceVoucher.toLocaleString('en-US') + ' đ';
        shippingFeeElement.textContent = shippingFee.toLocaleString('en-US') + ' đ';

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

    // Gọi hàm lấy danh sách tỉnh khi tải trang
    fetchProvinces();
});

function createNewAddress() {
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


// Hàm cập nhật địa chỉ khi ấn nút "Cập nhật"
function updateAddress() {

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
        type: 'GET',
        url: '/api-client/list-address-for-customer',
        success: function (response) {
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
        },
        error: function (xhr) {
            console.error('Lỗi: ' + xhr.responseText);
        }
    });
}

// Khi trang đã sẵn sàng
$(document).ready(function () {
    listAddressForCustomer();
});
