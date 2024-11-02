document.addEventListener("DOMContentLoaded", function () {
    const apiKey = '0fc88a8e-6633-11ef-8e53-0a00184fe694';
    const shopId = '195165';

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

    document.getElementById("province").addEventListener("change", function () {
        let provinceId = this.value;
        if (provinceId) {
            fetchDistricts(provinceId);
        }
    });

    document.getElementById("district").addEventListener("change", function () {
        let districtId = this.value;
        if (districtId) {
            fetchWards(districtId);
        }
    });

    fetchProvinces();

    function calculateShippingFee(serviceId) {
        const fromDistrictId = 3440;
        const toDistrictId = document.getElementById("district").value;
        const toWardCode = document.getElementById("ward").value;
        const weight = 200;
        const length = 30;
        const width = 20;
        const height = 10;

        if (!fromDistrictId || !toDistrictId || !toWardCode || !serviceId || !shopId) {
            console.error('Thiếu thông tin cần thiết');
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
                "insurance_value": 1000000,
                "to_district_id": parseInt(toDistrictId),
                "to_ward_code": toWardCode,
                "weight": weight,
                "length": length,
                "width": width,
                "height": height,
                "from_district_id": fromDistrictId
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.code === 200) {
                    document.getElementById("spanShippingFee").textContent = `${data.data.total} VND`;
                } else {
                    console.error('Lỗi tính phí ship:', data.message);
                    document.getElementById("spanShippingFee").textContent = "Không thể tính phí ship";
                }
            })
            .catch(error => console.error('Error:', error));
    }

    function getAvailableServices() {
        const fromDistrictId = 1582;
        const toDistrictId = document.getElementById("district").value;

        if (!fromDistrictId || !toDistrictId || !shopId) {
            console.error('Thiếu thông tin cần thiết để lấy dịch vụ');
            document.getElementById("spanShippingFee").textContent = "Không thể lấy dịch vụ vận chuyển";
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
                if (data.code === 200 && data.data) {
                    if (data.data.length > 0) {
                        const serviceId = data.data[0].service_id;
                        calculateShippingFee(serviceId);
                    } else {
                        document.getElementById("spanShippingFee").textContent = "Không có dịch vụ vận chuyển";
                    }
                } else {
                    console.error('Lỗi:', data.message);
                    document.getElementById("spanShippingFee").textContent = "Không thể lấy dịch vụ vận chuyển";
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById("spanShippingFee").textContent = "Lỗi kết nối hoặc lỗi máy chủ";
            });
    }

    document.getElementById("ward").addEventListener("change", function () {
        getAvailableServices();
    });

    function updateAddress() {
        const province = document.getElementById("province").options[document.getElementById("province").selectedIndex].text;
        const district = document.getElementById("district").options[document.getElementById("district").selectedIndex].text;
        const ward = document.getElementById("ward").options[document.getElementById("ward").selectedIndex].text;

        const fullAddress = `${ward}, ${district}, ${province}`;
        document.getElementById("addressShip").value = fullAddress;
    }

    document.getElementById("province").addEventListener("change", function () {
        let provinceId = this.value;
        if (provinceId) {
            fetchDistricts(provinceId);
            updateAddress(); // Cập nhật địa chỉ khi thay đổi tỉnh
        }
    });

    document.getElementById("district").addEventListener("change", function () {
        let districtId = this.value;
        if (districtId) {
            fetchWards(districtId);
            updateAddress(); // Cập nhật địa chỉ khi thay đổi huyện
        }
    });

    document.getElementById("ward").addEventListener("change", function () {
        getAvailableServices();
        updateAddress(); // Cập nhật địa chỉ khi thay đổi xã
    });

    fetchProvinces();
});

function calculatorTotalPrice() {
    // Lấy giá trị từ các phần tử HTML và chuyển đổi sang kiểu số
    const totalPriceCart = parseFloat(document.getElementById("spanTotalPriceCartItem").textContent.replace(/[^\d.-]/g, '')) || 0;
    const priceVoucher = parseFloat(document.getElementById("spanPriceVoucher").textContent.replace(/[^\d.-]/g, '')) || 0;
    const shippingFree = parseFloat(document.getElementById("spanShippingFee").textContent.replace(/[^\d.-]/g, '')) || 0;

    // Tính toán giá trị của totalPriceBill
    const totalPriceBill = totalPriceCart - priceVoucher + shippingFree;

    document.getElementById("spanTotalPriceBill").textContent = totalPriceBill.toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });


}

const observer = new MutationObserver(calculatorTotalPrice);

// Cấu hình cho observer
const config = {
    childList: true, // Theo dõi sự thay đổi của các nút con
    characterData: true, // Theo dõi sự thay đổi của dữ liệu ký tự
    subtree: true // Theo dõi toàn bộ cây con
};

// Các phần tử cần theo dõi
const totalPriceCartItem = document.getElementById("spanTotalPriceCartItem");
const priceVoucher = document.getElementById("spanPriceVoucher");
const shippingFee = document.getElementById("spanShippingFee");

// Bắt đầu theo dõi
if (totalPriceCartItem) {
    observer.observe(totalPriceCartItem, config);
}
if (priceVoucher) {
    observer.observe(priceVoucher, config);
}
if (shippingFee) {
    observer.observe(shippingFee, config);
}
calculatorTotalPrice();
