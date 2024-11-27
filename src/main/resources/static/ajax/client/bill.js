document.addEventListener("DOMContentLoaded", function () {
    // Lấy các trường cần kiểm tra
    const fullNameInput = document.getElementById("FullName");
    const emailInput = document.getElementById("Mail");
    const phoneInput = document.getElementById("Phone");
    const noteBillInput = document.getElementById("noteBill");
    const provinceSelect = document.getElementById("province");
    const districtSelect = document.getElementById("district");
    const wardSelect = document.getElementById("ward");
    const specificAddressInput = document.getElementById("specificAddressNolog");
    const paymentMethodRadios = document.querySelectorAll("input[name='payment_method_id']");


    fullNameInput.addEventListener("input", validateFullName);
    emailInput.addEventListener("input", validateEmail);
    phoneInput.addEventListener("input", validatePhone);
    provinceSelect.addEventListener("change", validateAddressDropdowns);
    districtSelect.addEventListener("change", validateAddressDropdowns);
    wardSelect.addEventListener("change", validateAddressDropdowns);
    specificAddressInput.addEventListener("input", validateSpecificAddress);

    // Hàm kiểm tra họ tên
    function validateFullName() {
        const fullName = fullNameInput.value;
        if (fullName.trim() === "") {
            document.getElementById("error-fullname").textContent = "* Họ tên không được để trống";
        } else {
            document.getElementById("error-fullname").textContent = "";
        }
    }

    // Hàm kiểm tra email
    function validateEmail() {
        const email = emailInput.value;
        const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (email.trim() === "") {
            document.getElementById("error-email").textContent = "* Email không được để trống";
        } else if (!emailPattern.test(email)) {
            document.getElementById("error-email").textContent = "* Email không hợp lệ";
        } else {
            document.getElementById("error-email").textContent = "";
        }
    }

    // Hàm kiểm tra số điện thoại
    function validatePhone() {
        const phone = phoneInput.value;
        const phonePattern = /^[0-9]{10,11}$/;

        // Kiểm tra nếu số điện thoại trống
        if (phone.trim() === "") {
            document.getElementById("error-phone").textContent = "* Số điện thoại không được để trống";
        }
        // Kiểm tra nếu số điện thoại không hợp lệ
        else if (!phonePattern.test(phone.trim())) {
            document.getElementById("error-phone").textContent = "Số điện thoại không hợp lệ *";
        } else {
            document.getElementById("error-phone").textContent = "";
        }
    }

    // Hàm kiểm tra ghi chú
    function validateNoteBill() {
        const noteBill = noteBillInput.value;
        // Không bắt buộc, nhưng có thể kiểm tra nếu cần
        if (noteBill.length > 200) {
            document.getElementById("error-noteBill").textContent = "* Ghi chú không được quá 200 ký tự";
        } else {
            document.getElementById("error-noteBill").textContent = "";
        }
    }

    function validateAddressDropdowns() {
        const province = provinceSelect.value;
        const district = districtSelect.value;
        const ward = wardSelect.value;

        if (!province || !district || !ward) {
            document.getElementById("error-select-address").textContent = "* Bạn cần chọn đầy đủ Tỉnh, Huyện, và Xã/Phường.";
        } else {
            document.getElementById("error-select-address").textContent = "";
        }
    }

    // Hàm validate địa chỉ cụ thể
    function validateSpecificAddress() {
        const specificAddress = specificAddressInput.value.trim();
        if (specificAddress === "") {
            document.getElementById("error-address-specific").textContent = "* Vui lòng nhập địa chỉ nhận hàng cụ thể.";
        } else {
            document.getElementById("error-address-specific").textContent = "";
        }
    }

    function validatePaymentMethod() {
        let isChecked = false;

        paymentMethodRadios.forEach((radio) => {
            if (radio.checked) {
                isChecked = true;
            }
        });

        if (!isChecked) {
            document.getElementById("error-payment-method").textContent = "* Bạn cần chọn một phương thức thanh toán.";
        } else {
            document.getElementById("error-payment-method").textContent = "";
        }
    }

    paymentMethodRadios.forEach((radio) => {
        radio.addEventListener("change", validatePaymentMethod);
    });

    validateFullName();
    validateEmail();
    validatePhone();
    validateNoteBill();
    validateAddressDropdowns();
    validateSpecificAddress();
    validatePaymentMethod();

});

function payBill() {
    const addressShip = document.getElementById("addressShip").value;
    console.log("Dia chi thanh toan: " + addressShip)
    const shippingPriceText = $('#spanShippingFee').text().trim();
    const voucherPriceText = $('#spanPriceVoucher').text().trim();
    const totalAmountBillText = $('#spanTotalPriceCartItem').text().trim();
    const noteBill = $('#noteBill').val();
    const selectedRadioPaymentMethod = document.querySelector('input[name="payment_method_id"]:checked');
    if (selectedRadioPaymentMethod) {
        console.log('Selected payment method value: ' + selectedRadioPaymentMethod.value);
    } else {
        // Nếu không có radio button nào được chọn
        console.log('No payment method selected');
    }
    let shippingPrice = parseFloat(shippingPriceText.replace(/[^0-9.-]+/g, ''));
    let priceVoucher = parseFloat(voucherPriceText.replace(/[^0-9.-]+/g, ''));
    let totalAmountBill = parseFloat(totalAmountBillText.replace(/[^0-9.-]+/g, ''));
    shippingPrice = isNaN(shippingPrice) ? 0 : shippingPrice;
    priceVoucher = isNaN(priceVoucher) ? 0 : priceVoucher;
    totalAmountBill = isNaN(totalAmountBill) ? 0 : totalAmountBill;
    let payMethod = selectedRadioPaymentMethod.value;
    console.log("Địa chỉ giao hàng: " + addressShip);
    console.log("Giá vận chuyển: " + shippingPrice);
    console.log("Giá giảm: " + priceVoucher);
    console.log("Tổng số tiền hóa đơn: " + totalAmountBill);

    $.ajax({
        url: '/onepoly/payment',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            addressShip: addressShip,
            priceVoucher: priceVoucher,
            shippingPrice: shippingPrice,
            totalAmountBill: totalAmountBill,
            noteBill: noteBill,
            payMethod: payMethod
        }),
        success: function (response) {
            window.location.href = response;
        },
        error: function (error) {
            alert("Thanh toán thất bại. Vui lòng thử lại.");
            console.error(error);
        }
    });
}

