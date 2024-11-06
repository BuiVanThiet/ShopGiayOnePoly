document.addEventListener("DOMContentLoaded", function () {
    const apiKey = '0fc88a8e-6633-11ef-8e53-0a00184fe694';
    const shopId = '195165';
    const fromDistrictId = 3440; // Mã huyện cố định của kho hàng
    let shippingFeeCalculated = false; // Cờ để kiểm soát việc tính lại phí vận chuyển

    function calculateShippingFee(serviceId, toDistrictId, toWardCode) {
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
                "insurance_value": 1000000,
                "to_district_id": parseInt(toDistrictId),
                "to_ward_code": toWardCode,
                "weight": 200,
                "length": 30,
                "width": 20,
                "height": 10,
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
                    shippingFeeCalculated = true; // Đánh dấu rằng phí vận chuyển đã được tính
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
            updateShippingFeeDisplay("Không thể lấy dịch vụ vận chuyển");
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
