document.addEventListener("DOMContentLoaded", function () {
    const fullNameInput = document.getElementById("FullName");
    const emailInput = document.getElementById("Mail");
    const phoneInput = document.getElementById("Phone");
    const specificAddressInput = document.getElementById("specificAddressNolog");

    // Loại bỏ dấu phẩy trong khi nhập
    [fullNameInput, emailInput, phoneInput, specificAddressInput].forEach((input) => {
        input.addEventListener("input", function () {
            input.value = input.value.replace(/,/g, ""); // Loại bỏ dấu phẩy
        });
    });

    // Hàm validate
    function validateFullName() {
        const fullName = fullNameInput.value.trim();
        const errorElement = document.getElementById("error-fullname");
        if (fullName === "") {
            errorElement.textContent = "* Họ tên không được để trống";
            return false;
        }
        if (fullName.includes(",")) {
            errorElement.textContent = "* Họ tên không được chứa dấu phẩy (,)";
            return false;
        }
        if (fullName.length > 255) {
            errorElement.textContent = "* Họ tên không được vượt quá 50 ký tự";
            return false;
        }
        errorElement.textContent = "";
        return true;
    }

    function validateEmail() {
        const email = emailInput.value.trim();
        const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        const errorElement = document.getElementById("error-email");
        if (email === "") {
            errorElement.textContent = "* Email không được để trống";
            return false;
        }
        if (!emailPattern.test(email)) {
            errorElement.textContent = "* Email không hợp lệ";
            return false;
        }
        if (email.includes(",")) {
            errorElement.textContent = "* Email không được chứa dấu phẩy (,)";
            return false;
        }
        if (email.length > 50) {
            errorElement.textContent = "* Email không được vượt quá 100 ký tự";
            return false;
        }
        errorElement.textContent = "";
        return true;
    }

    function validatePhone() {
        const phone = phoneInput.value.trim();
        const phonePattern = /^[0-9]{10,11}$/;
        const errorElement = document.getElementById("error-phone");
        if (phone === "") {
            errorElement.textContent = "* Số điện thoại không được để trống";
            return false;
        }
        if (!phonePattern.test(phone)) {
            errorElement.textContent = "* Số điện thoại không hợp lệ";
            return false;
        }
        if (phone.includes(",")) {
            errorElement.textContent = "* Số điện thoại không được chứa dấu phẩy (,)";
            return false;
        }
        errorElement.textContent = "";
        return true;
    }

    function validateSpecificAddress() {
        const specificAddress = specificAddressInput.value.trim();
        const errorElement = document.getElementById("error-address-specific");
        if (specificAddress === "") {
            errorElement.textContent = "* Vui lòng nhập địa chỉ nhận hàng cụ thể.";
            return false;
        }
        if (specificAddress.includes(",")) {
            errorElement.textContent = "* Địa chỉ không được chứa dấu phẩy (,)";
            return false;
        }
        if (specificAddress.length > 260) {
            errorElement.textContent = "* Địa chỉ cụ thể không được vượt quá 260 ký tự";
            return false;
        }
        errorElement.textContent = "";
        return true;
    }

    // // Khi nhấn nút thanh toán
    // window.payBill = function () {
    //     const isFullNameValid = validateFullName();
    //     const isEmailValid = validateEmail();
    //     const isPhoneValid = validatePhone();
    //     const isSpecificAddressValid = validateSpecificAddress();
    //
    //     if (!isFullNameValid || !isEmailValid || !isPhoneValid || !isSpecificAddressValid) {
    //         return;
    //     }
    //
    //     // Xử lý thanh toán nếu không có lỗi
    //     console.log("Tất cả dữ liệu hợp lệ, tiếp tục xử lý thanh toán.");
    // };
});


function payBill() {
    let isValid = true;

    // Validate họ tên
    const fullNameInput = document.getElementById("FullName");
    const fullName = fullNameInput.value.trim();
    if (fullName === "") {
        document.getElementById("error-fullname").textContent = "* Họ tên không được để trống";
        isValid = false;
    } else if (fullName.includes(",")) {
        document.getElementById("error-fullname").textContent = "* Họ tên không được chứa dấu phẩy (,)";
        isValid = false;
    } else if (fullName.length > 250) {
        document.getElementById("error-fullname").textContent = "* Họ tên không được quá 255 ký tự";
        isValid = false;
    } else {
        document.getElementById("error-fullname").textContent = "";
    }

    // Validate email
    const emailInput = document.getElementById("Mail");
    const email = emailInput.value.trim();
    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (email === "") {
        document.getElementById("error-email").textContent = "* Email không được để trống";
        isValid = false;
    } else if (!emailPattern.test(email)) {
        document.getElementById("error-email").textContent = "* Email không hợp lệ";
        isValid = false;
    } else if (email.includes(",")) {
        document.getElementById("error-email").textContent = "* Email không được chứa dấu phẩy (,)";
        isValid = false;
    } else {
        document.getElementById("error-email").textContent = "";
    }

    // Validate số điện thoại
    const phoneInput = document.getElementById("Phone");
    const phone = phoneInput.value.trim();
    const phonePattern = /^[0-9]{10,11}$/;
    if (phone === "") {
        document.getElementById("error-phone").textContent = "* Số điện thoại không được để trống";
        isValid = false;
    } else if (!phonePattern.test(phone)) {
        document.getElementById("error-phone").textContent = "* Số điện thoại không hợp lệ";
        isValid = false;
    } else if (phone.includes(",")) {
        document.getElementById("error-phone").textContent = "* Số điện thoại không được chứa dấu phẩy (,)";
        isValid = false;
    } else {
        document.getElementById("error-phone").textContent = "";
    }

    // Validate địa chỉ cụ thể
    const specificAddressInput = document.getElementById("specificAddressNolog");
    const specificAddress = specificAddressInput.value.trim();
    if (specificAddress === "") {
        document.getElementById("error-address-specific").textContent = "* Vui lòng nhập địa chỉ nhận hàng cụ thể.";
        isValid = false;
    } else if (specificAddress.includes(",")) {
        document.getElementById("error-address-specific").textContent = "* Địa chỉ không được chứa dấu phẩy (,)";
        isValid = false;
    } else {
        document.getElementById("error-address-specific").textContent = "";
    }

    // Validate dropdown địa chỉ
    const provinceSelect = document.getElementById("province");
    const districtSelect = document.getElementById("district");
    const wardSelect = document.getElementById("ward");
    if (!provinceSelect.value || !districtSelect.value || !wardSelect.value) {
        document.getElementById("error-select-address").textContent = "* Bạn cần chọn đầy đủ Tỉnh, Huyện, và Xã/Phường.";
        isValid = false;
    } else {
        document.getElementById("error-select-address").textContent = "";
    }

    // Validate phương thức thanh toán
    const paymentMethodRadios = document.querySelectorAll("input[name='payment_method_id']");
    let isPaymentMethodSelected = false;
    paymentMethodRadios.forEach((radio) => {
        if (radio.checked) isPaymentMethodSelected = true;
    });
    if (!isPaymentMethodSelected) {
        document.getElementById("error-payment-method").textContent = "* Bạn cần chọn một phương thức thanh toán.";
        isValid = false;
    } else {
        document.getElementById("error-payment-method").textContent = "";
    }

    // Nếu có lỗi, dừng việc gửi AJAX
    if (!isValid) {
        return;
    }

    // Tiếp tục xử lý khi không có lỗi
    const addressShip = document.getElementById("addressShip").value;
    const shippingPriceText = $('#spanShippingFee').text().trim();
    const voucherPriceText = $('#spanPriceVoucher').text().trim();
    const totalAmountBillText = $('#spanTotalPriceCartItem').text().trim();
    const noteBill = $('#noteBill').val().trim();

    const selectedRadioPaymentMethod = document.querySelector('input[name="payment_method_id"]:checked');

    let shippingPrice = parseFloat(shippingPriceText.replace(/[^0-9.-]+/g, ''));
    let priceVoucher = parseFloat(voucherPriceText.replace(/[^0-9.-]+/g, ''));
    let totalAmountBill = parseFloat(totalAmountBillText.replace(/[^0-9.-]+/g, ''));

    shippingPrice = isNaN(shippingPrice) ? 0 : shippingPrice;
    priceVoucher = isNaN(priceVoucher) ? 0 : priceVoucher;
    totalAmountBill = isNaN(totalAmountBill) ? 0 : totalAmountBill;

    let payMethod = selectedRadioPaymentMethod.value;
    console.log("Pay method value: " + payMethod)
    // Lấy giá trị phương thức thanh toán
    const errorElement = document.getElementById("error-total-amount-bill");

    if (parseInt(payMethod) === 1) {
        // Nếu là phương thức COD
        if (totalAmountBill > 100000000) {
            errorElement.textContent = "Tổng tiền phải nhỏ hơn 100 triệu cho phương thức COD.";
            return false;
        } else {
            errorElement.textContent = "";
        }
    } else if (parseInt(payMethod) === 2) {
        // Nếu là phương thức VNPAY
        if (totalAmountBill > 20000000) {
            errorElement.textContent = "Tổng tiền phải nhỏ hơn 20 triệu cho phương thức VNPAY.";
            return false;
        } else {
            errorElement.textContent = "";
        }
    }
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

