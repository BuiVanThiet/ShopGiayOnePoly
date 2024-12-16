document.addEventListener("DOMContentLoaded", function () {
    const apiKey = '4ad62142-6630-11ef-8e53-0a00184fe694';
    const shopId = '194419';
    const fromDistrictId = 3440;
    const weightText = document.getElementById("weightShip").value.trim();
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
                    let provinceSelect = document.getElementById("province");
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
                'Content-Type': 'application/json',
                'Token': apiKey
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    const districts = data.data;
                    let districtSelect = document.getElementById("district");
                    districtSelect.innerHTML = '<option value="">Chọn Huyện</option>';
                    districts.forEach(district => {
                        let option = document.createElement("option");
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
                    let wardSelect = document.getElementById("ward");
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

    function calculateShippingFee(serviceId, toDistrictId, toWardCode) {
        // Kiểm tra các thông tin cần thiết
        if (!serviceId || !toDistrictId || !toWardCode) {
            console.error('Thiếu thông tin cần thiết để tính phí ship');
            document.getElementById("spanShippingFee").textContent = "Không thể tính phí ship";
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
                    document.getElementById("spanShippingFee").textContent = `${shippingFee} VNĐ`;

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
                    document.getElementById("spanShippingFee").textContent = "Không có dịch vụ vận chuyển";
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById("spanShippingFee").textContent = "Lỗi kết nối hoặc lỗi máy chủ";
            });
    }

    function updateAddress() {
        const fullName = document.getElementById("FullName").value;
        const phone = document.getElementById("Phone").value;
        const mail = document.getElementById("Mail").value;

        const provinceId = document.getElementById("province").value;
        const districtId = document.getElementById("district").value;
        const wardCode = document.getElementById("ward").value;

        const province = document.getElementById("province").options[document.getElementById("province").selectedIndex].text;
        const district = document.getElementById("district").options[document.getElementById("district").selectedIndex].text;
        const ward = document.getElementById("ward").options[document.getElementById("ward").selectedIndex].text;
        const specificAddress = document.getElementById("specificAddressNolog").value;
        const fullAddress = `${ward}, ${district}, ${province}`;

        const address = `${fullName},${phone},${mail},${provinceId},${districtId},${wardCode},${fullAddress},${specificAddress}`;
        document.getElementById("addressShip").value = address;

    }

    function calculateTotalPrice() {
        // Lấy các phần tử DOM
        const totalPriceCartItemElement = document.getElementById("spanTotalPriceCartItem");
        const priceVoucherElement = document.getElementById("spanPriceVoucher");
        const shippingFeeElement = document.getElementById("spanShippingFee");
        const totalPriceBillElement = document.getElementById("spanTotalPriceBill");

        // Chuyển đổi giá trị textContent sang số, xử lý các ký tự không hợp lệ
        let totalPriceCartItem = parseFloat(totalPriceCartItemElement.textContent.replace(/[^\d.-]/g, '')) || 0;
        let priceVoucher = parseFloat(priceVoucherElement.textContent.replace(/[^\d.-]/g, '')) || 0;
        let shippingFee = parseFloat(shippingFeeElement.textContent.replace(/[^\d.-]/g, '')) || 0;

        let totalPriceBill = totalPriceCartItem - priceVoucher + shippingFee;

        totalPriceCartItemElement.textContent = Math.max(totalPriceCartItem, 0).toLocaleString('en-US') + ' VNĐ';
        priceVoucherElement.textContent = Math.max(priceVoucher, 0).toLocaleString('en-US') + ' VNĐ';
        shippingFeeElement.textContent = Math.max(shippingFee, 0).toLocaleString('en-US') + ' VNĐ';
        totalPriceBillElement.textContent = Math.max(totalPriceBill, 0).toLocaleString('en-US') + ' VNĐ';

    }


    document.getElementById("FullName").addEventListener("input", updateAddress);
    document.getElementById("Phone").addEventListener("input", updateAddress);
    document.getElementById("Mail").addEventListener("input", updateAddress);
    document.getElementById("province").addEventListener("change", function () {
        const provinceId = this.value;
        if (provinceId) fetchDistricts(provinceId);
        updateAddress();
    });

    document.getElementById("district").addEventListener("change", function () {
        const districtId = this.value;
        if (districtId) fetchWards(districtId);
        updateAddress();
        calculateTotalPrice();
    });

    document.getElementById("ward").addEventListener("change", function () {
        const districtId = document.getElementById("district").value;
        const wardCode = document.getElementById("ward").value;
        getAvailableServices(districtId, wardCode);
        updateAddress(); // Gọi hàm cập nhật địa chỉ khi chọn xã/phường
        calculateTotalPrice();
    });
    document.getElementById("specificAddressNolog").addEventListener("input", updateAddress);

    fetchProvinces();
});

function calculateTotalPrice() {
    let totalPrice = 0;

    // Lặp qua tất cả các item trong giỏ hàng
    document.querySelectorAll("#cart-list .line-item").forEach(item => {
        const priceElem = item.querySelector("#spanPriceCartItem");
        const quantityElem = item.querySelector(".line-item-qty");

        if (!priceElem || !quantityElem) {
            console.warn("Dữ liệu sản phẩm không hợp lệ.");
            return;
        }

        // Lấy giá và số lượng
        const price = parseFloat(priceElem.getAttribute("data-price")) || 0;
        const quantity = parseInt(quantityElem.innerText) || 0;

        if (isNaN(price) || isNaN(quantity) || price < 0 || quantity < 0) {
            console.warn("Giá hoặc số lượng không hợp lệ:", {price, quantity});
            return;
        }

        const totalProductPrice = price * quantity;

        totalPrice += totalProductPrice;

        const totalProductPriceElem = item.querySelector(".item-total-price");
        if (totalProductPriceElem) {
            totalProductPriceElem.innerText = Math.max(0, totalProductPrice).toLocaleString('en-US') + " VNĐ";
        }
    });

    // Giảm giá voucher
    const priceVoucherReducedElement = document.getElementById("spanPriceVoucher");
    const priceVoucherReduced = parseFloat(
        priceVoucherReducedElement ? priceVoucherReducedElement.innerText.replace(/đ|,/g, "") : 0
    ) || 0;

    if (isNaN(priceVoucherReduced) || priceVoucherReduced < 0) {
        console.warn("Giá trị giảm giá không hợp lệ:", priceVoucherReduced);
    }

    const finalPrice = Math.max(0, totalPrice - priceVoucherReduced);

    // Cập nhật các thẻ HTML
    document.getElementById("spanTotalPriceCartItem").innerText = totalPrice.toLocaleString('en-US') + " VNĐ";
    document.getElementById("spanPriceVoucher").innerText = Math.max(0, priceVoucherReduced).toLocaleString('en-US') + " VNĐ";
    document.getElementById("spanTotalPriceBill").innerText = Math.max(0, finalPrice).toLocaleString('en-US') + " VNĐ";

}


window.addEventListener('load', function () {
    const spanPriceVoucher = document.getElementById("spanPriceVoucher");
    if (spanPriceVoucher) {
        const voucherPriceText = spanPriceVoucher.textContent.trim();
        const priceVoucher = parseFloat(voucherPriceText.replace(/[^0-9.-]+/g, ''));

        if (isNaN(priceVoucher)) {
            spanPriceVoucher.textContent = "-";
        }
    }
    calculateTotalPrice();

});
document.querySelectorAll('.item-price span').forEach(el => {
    // Lấy giá trị từ data-price
    const price = parseFloat(el.getAttribute('data-price'));

    // Đảm bảo giá trị không âm
    const validPrice = Math.max(0, price);

    if (!isNaN(validPrice)) {
        el.textContent = validPrice.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
    }
});

