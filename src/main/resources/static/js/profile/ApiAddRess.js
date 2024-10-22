document.addEventListener("DOMContentLoaded", function () {
    const apiKey = '0fc88a8e-6633-11ef-8e53-0a00184fe694';
    const shopId = '194418';

    // Lấy các giá trị ID ẩn từ trang
    const provinceHiden = document.getElementById('provinceHiden').value;
    const districtHiden = document.getElementById('districtHiden').value;
    const wardHiden = document.getElementById('wardHiden').value;

    // Hàm chọn giá trị trong dropdown
    function selectOptionByValue(selectElement, value) {
        const options = selectElement.options;
        for (let i = 0; i < options.length; i++) {
            if (options[i].value == value) {
                selectElement.selectedIndex = i;
                return;
            }
        }
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

                    // Tự động chọn tỉnh nếu đã có ID sẵn
                    if (provinceHiden) {
                        selectOptionByValue(provinceSelect, provinceHiden);
                        fetchDistricts(provinceHiden); // Gọi hàm lấy huyện sau khi chọn tỉnh
                    }
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

                    // Tự động chọn huyện nếu đã có ID sẵn
                    if (districtHiden) {
                        selectOptionByValue(districtSelect, districtHiden);
                        fetchWards(districtHiden); // Gọi hàm lấy xã sau khi chọn huyện
                    }
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

                    // Tự động chọn xã/phường nếu đã có ID sẵn
                    if (wardHiden) {
                        selectOptionByValue(wardSelect, wardHiden);
                    }
                } else {
                    console.error('Lỗi lấy danh sách xã/phường:', data.message);
                }
            })
            .catch(error => console.error('Error:', error));
    }

    // Bắt sự kiện thay đổi cho dropdown
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

    // Gọi hàm để lấy danh sách tỉnh khi trang tải
    fetchProvinces();
});