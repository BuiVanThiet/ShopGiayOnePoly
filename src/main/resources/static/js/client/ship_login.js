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
    console.log("Weight ship: "+weight)
    console.log("Total amount ship: "+totalPriceCartItem)

    function calculateShippingFee(serviceId, toDistrictId, toWardCode) {
        console.log("Service ID :"+serviceId)
        console.log("toDistrictId ID :"+toDistrictId)
        console.log("toWardCode ID :"+toWardCode)
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
document.addEventListener("DOMContentLoaded", function() {
    const spanPriceVoucher = document.getElementById("spanPriceVoucher");
    const voucherPriceText = spanPriceVoucher.textContent.trim();
    const priceVoucher = parseFloat(voucherPriceText.replace(/[^0-9.-]+/g, ''));

    // Nếu giá trị không hợp lệ, đặt giá trị mặc định là 0
    if (isNaN(priceVoucher)) {
        spanPriceVoucher.textContent = "-";
    }
});