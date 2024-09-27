var provinceTransport;
var districtTransport;
var wardTransport;
var totalBill;
var shipPrice = 0;
function initializeLocationDropdowns(provinceSelectId, districtSelectId, wardSelectId,districtSelectContainerID,wardSelectContainerID, provinceID, districtID, wardID) {
    const provinceSelect = document.getElementById(provinceSelectId);
    const districtSelect = document.getElementById(districtSelectId);
    const wardSelect = document.getElementById(wardSelectId);
    const districtSelectContainer = document.getElementById(districtSelectContainerID);
    const wardSelectContainer = document.getElementById(wardSelectContainerID);
    console.log(totalBill)
    $.ajax({
        url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province',
        method: 'GET',
        headers: {
            'Token': '4ad62142-6630-11ef-8e53-0a00184fe694' // Token từ GHN
        },
        success: function(response) {
            const provinces = response.data;
            provinceSelect.innerHTML = '<option value="">Chọn tỉnh/thành phố</option>'; // Reset options

            provinces.forEach(province => {
                const isSelected = province.ProvinceID === provinceID ? 'selected' : '';
                provinceSelect.innerHTML += `<option value="${province.ProvinceID}" ${isSelected}>${province.ProvinceName}</option>`;
            });

            // Khi chọn tỉnh/thành phố, gọi API để lấy danh sách quận/huyện
            provinceSelect.addEventListener('change', function() {
                const selectedProvinceID = this.value;
                $('#shipMoney').text('0' + ' VNĐ');
                $('#moneyTransport').val(0)
                $('#moneyShipUpdate').val(0)
                shipPrice = 0;
                if(btnCreateBill != null) {
                    btnCreateBill.disabled = true;
                }
                paymentInformation();

                provinceTransport = this.value;

                wardSelectContainer.style.display = 'none';
                wardSelect.innerHTML = '<option value="">Chọn xã/phường/thị trấn</option>';

                if (selectedProvinceID) {
                    districtSelectContainer.style.display = 'block';
                    $.ajax({
                        url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district',
                        method: 'GET',
                        headers: {
                            'Token': '4ad62142-6630-11ef-8e53-0a00184fe694'
                        },
                        data: {
                            province_id: selectedProvinceID
                        },
                        success: function(response) {
                            const districts = response.data;
                            districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';

                            districts.forEach(district => {
                                const isSelected = district.DistrictID === districtID ? 'selected' : '';
                                districtSelect.innerHTML += `<option value="${district.DistrictID}" ${isSelected}>${district.DistrictName}</option>`;
                            });

                            // Khi chọn quận/huyện, gọi API để lấy danh sách xã/phường
                            districtSelect.addEventListener('change', function() {
                                const selectedDistrictID = this.value;
                                $('#shipMoney').text('0' + ' VNĐ');
                                $('#moneyTransport').val(0)
                                $('#moneyShipUpdate').val(0)
                                if(btnCreateBill != null) {
                                    btnCreateBill.disabled = true;
                                }
                                shipPrice = 0;
                                paymentInformation();

                                districtTransport = this.value;

                                if (selectedDistrictID) {
                                    wardSelectContainer.style.display = 'block';
                                    $.ajax({
                                        url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward',
                                        method: 'GET',
                                        headers: {
                                            'Token': '4ad62142-6630-11ef-8e53-0a00184fe694'
                                        },
                                        data: {
                                            district_id: selectedDistrictID
                                        },
                                        success: function(response) {
                                            const wards = response.data;
                                            wardSelect.innerHTML = '<option value="">Chọn xã/phường/thị trấn</option>';

                                            wards.forEach(ward => {
                                                const isSelected = ward.WardCode === wardID ? 'selected' : '';
                                                wardSelect.innerHTML += `<option value="${ward.WardCode}" ${isSelected}>${ward.WardName}</option>`;
                                            });

                                            wardSelect.addEventListener('change',function () {
                                                wardTransport = this.value;
                                                console.log('Thong tin sau khi chon api (danh cho doi tuong sau khi chon xong)' + provinceTransport + '-' + districtTransport + '-' + wardTransport)
                                                totalShip(provinceTransport,districtTransport,wardTransport);
                                            })

                                            // Tự động chọn xã/phường nếu có
                                            if (wardID) {
                                                wardSelect.value = wardID;
                                                wardTransport = wardID;
                                            }
                                        },
                                        error: function() {
                                            alert('Lỗi khi lấy danh sách xã/phường.');
                                        }
                                    });
                                } else {
                                    $('#shipMoney').text(0 + ' VNĐ');
                                    $('#moneyTransport').val(0)
                                    $('#moneyShipUpdate').val(0)
                                    if(btnCreateBill != null) {
                                        btnCreateBill.disabled = true;
                                    }
                                    shipPrice = 0;

                                    wardTransport = null;
                                    wardSelectContainer.style.display = 'none';
                                }
                            });

                            // Tự động chọn xã/phường nếu quận/huyện đã được chọn
                            const selectedDistrict = districtSelect.querySelector('option[selected]');
                            if (selectedDistrict) {
                                const selectedDistrictID = selectedDistrict.value;

                                districtTransport = selectedDistrict.value;

                                if (selectedDistrictID) {
                                    wardSelectContainer.style.display = 'block';
                                    $.ajax({
                                        url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward',
                                        method: 'GET',
                                        headers: {
                                            'Token': '4ad62142-6630-11ef-8e53-0a00184fe694'
                                        },
                                        data: {
                                            district_id: selectedDistrictID
                                        },
                                        success: function(response) {
                                            const wards = response.data;
                                            wardSelect.innerHTML = '<option value="">Chọn xã/phường/thị trấn</option>';

                                            wards.forEach(ward => {
                                                const isSelected = ward.WardCode === wardID ? 'selected' : '';
                                                wardSelect.innerHTML += `<option value="${ward.WardCode}" ${isSelected}>${ward.WardName}</option>`;
                                            });

                                            // Tự động chọn xã/phường nếu có
                                            if (wardID) {
                                                wardSelect.value = wardID;
                                                wardTransport = wardID;
                                            }
                                            console.log('Tu dong chon ne' + + provinceTransport + '-' + districtTransport + '-' + wardTransport)
                                            totalShip(provinceTransport,districtTransport,wardTransport);
                                        },
                                        error: function() {
                                            alert('Lỗi khi lấy danh sách xã/phường.');
                                        }
                                    });
                                }
                            }
                        },
                        error: function() {
                            alert('Lỗi khi lấy danh sách quận/huyện.');
                        }
                    });
                } else {
                    $('#shipMoney').text('0' + ' VNĐ');
                    $('#moneyTransport').val(0)

                    districtTransport = null;
                    wardTransport = null;
                    districtSelectContainer.style.display = 'none';
                    wardSelectContainer.style.display = 'none';
                }
            });

            // Tự động chọn quận/huyện và xã/phường nếu tỉnh đã được chọn
            const selectedProvince = provinceSelect.querySelector('option[selected]');
            if (selectedProvince) {
                const selectedProvinceID = selectedProvince.value;

                provinceTransport = selectedProvince.value;

                if (selectedProvinceID) {
                    districtSelectContainer.style.display = 'block';
                    $.ajax({
                        url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district',
                        method: 'GET',
                        headers: {
                            'Token': '4ad62142-6630-11ef-8e53-0a00184fe694'
                        },
                        data: {
                            province_id: selectedProvinceID
                        },
                        success: function(response) {
                            const districts = response.data;
                            districtSelect.innerHTML = '<option value="">Chọn quận/huyện</option>';

                            districts.forEach(district => {
                                const isSelected = district.DistrictID === districtID ? 'selected' : '';
                                districtSelect.innerHTML += `<option value="${district.DistrictID}" ${isSelected}>${district.DistrictName}</option>`;
                            });

                            // Tự động chọn xã/phường nếu quận/huyện đã được chọn
                            const selectedDistrict = districtSelect.querySelector('option[selected]');
                            if (selectedDistrict) {
                                const selectedDistrictID = selectedDistrict.value;

                                districtTransport = selectedDistrict.value;

                                if (selectedDistrictID) {
                                    wardSelectContainer.style.display = 'block';
                                    $.ajax({
                                        url: 'https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward',
                                        method: 'GET',
                                        headers: {
                                            'Token': '4ad62142-6630-11ef-8e53-0a00184fe694'
                                        },
                                        data: {
                                            district_id: selectedDistrictID
                                        },
                                        success: function(response) {
                                            const wards = response.data;
                                            wardSelect.innerHTML = '<option value="">Chọn xã/phường/thị trấn</option>';

                                            wards.forEach(ward => {
                                                const isSelected = ward.WardCode === wardID ? 'selected' : '';
                                                wardSelect.innerHTML += `<option value="${ward.WardCode}" ${isSelected}>${ward.WardName}</option>`;
                                            });

                                            // Tự động chọn xã/phường nếu có
                                            if (wardID) {
                                                wardSelect.value = wardID;
                                                wardTransport = wardID;
                                            }
                                            console.log('Thong tin sau khi chon api (danh cho doi tuong dia chi manc dinh)' + provinceTransport + '-' + districtTransport + '-' + wardTransport)
                                            totalShip(provinceTransport,districtTransport,wardTransport);
                                        },
                                        error: function() {
                                            alert('Lỗi khi lấy danh sách xã/phường.');
                                        }
                                    });
                                }
                            }
                        },
                        error: function() {
                            alert('Lỗi khi lấy danh sách quận/huyện.');
                        }
                    });
                }
            }
        },
        error: function() {
            alert('Lỗi khi lấy danh sách tỉnh/thành phố.');
        }
    });
}

function totalShip(province, district, ward) {
    console.log("district check " + district);
    $.ajax({
        type: "GET",
        url: "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services",
        headers: {
            'Token': '4ad62142-6630-11ef-8e53-0a00184fe694'
        },
        data: {
            shop_id: 194419,
            from_district: 1482,
            to_district: district
        },
        success: function (response) {
            var services = response.data;
            var service = services.find(s => s.short_name === "Hàng nhẹ");
            if (service) {
                console.log(service);
                let serviceId = service.service_id; // Lưu service_id

                function calculateFee(serviceId) {
                    $.ajax({
                        type: "GET",
                        url: "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee",
                        headers: {
                            'Token': '4ad62142-6630-11ef-8e53-0a00184fe694',
                            'shop_id': '194419'
                        },
                        data: {
                            service_id: serviceId,
                            insurance_value: totalBill,
                            coupon: null,
                            to_district_id: district,
                            to_ward_code: ward,
                            height: 20,
                            length: 60,
                            weight: 3000,
                            width: 15
                        },
                        success: function (response) {
                            console.log(response.data);
                            $('#shipMoney').text(response.data.total.toLocaleString('en-US') + ' VNĐ');
                            $('#moneyTransport').val(response.data.total)
                            $('#moneyShipUpdate').val(response.data.total)
                            shipPrice = response.data.total;
                            if(btnCreateBill != null) {
                                btnCreateBill.disabled = false;
                            }
                            setClientShip(nameCustomer,numberPhoneCustomer,provinceTransport,districtTransport,wardTransport,addRessDetailCustomer)
                            paymentInformation();
                        },
                        error: function (xhr) {
                            console.log('Lỗi tính tiền ship: ' + xhr.responseText);
                            // Giảm service_id và thực hiện lại yêu cầu nếu có lỗi
                            if (serviceId > 1) { // Đảm bảo service_id không âm
                                calculateFee(serviceId - 1); // Gọi lại với service_id giảm 1
                            } else {
                                console.log("Không còn dịch vụ nào để thử.");
                            }
                        }
                    });
                }

                // Gọi hàm tính phí với service_id ban đầu
                calculateFee(serviceId);
            } else {
                console.log("Không tìm thấy dịch vụ Hàng nhẹ");
            }
        },
        error: function (xhr) {
            console.log("Lỗi: " + xhr.responseText);
        }
    });
}


