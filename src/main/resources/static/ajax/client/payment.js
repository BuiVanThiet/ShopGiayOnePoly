document.addEventListener("DOMContentLoaded", function () {
    const quantityCart = parseInt(document.getElementById("totalQuantity").innerText);
    const btnDatHang = document.getElementById("btnDatHang");

    if (isNaN(quantityCart) || quantityCart <= 0) {
        btnDatHang.disabled = true; // Vô hiệu hóa nút thanh toán
    } else {
        btnDatHang.disabled = false; // Kích hoạt nút thanh toán
    }

    const accountLogin = document.getElementById("account-login").value;
    console.log("Account login: " + accountLogin);

    if (!accountLogin) {
        const fullNameInput = document.getElementById("FullName");
        const emailInput = document.getElementById("Mail");
        const phoneInput = document.getElementById("Phone");
        const specificAddressInput = document.getElementById("specificAddressNolog");
        const provinceSelect = document.getElementById("province");
        const districtSelect = document.getElementById("district");
        const wardSelect = document.getElementById("ward");
        if (!fullNameInput || !emailInput || !phoneInput || !specificAddressInput) {
            console.error("One or more input elements not found");
            return;
        }

        console.log("Validate Unlogin");

        // Loại bỏ dấu phẩy khi nhập
        [fullNameInput, emailInput, phoneInput, specificAddressInput].forEach((input) => {
            input.addEventListener("input", function () {
                input.value = input.value.replace(/,/g, ""); // Loại bỏ dấu phẩy
            });
        });

        // Hàm validate Full Name
        function validateFullName() {
            const fullName = fullNameInput.value.trim();
            const errorFullName = document.getElementById("error-fullname");
            if (fullName === "") {
                errorFullName.textContent = "* Họ tên không được để trống.";
            } else if (fullName.length > 255) {
                errorFullName.textContent = "* Họ tên không được vượt quá 255 ký tự.";
            } else {
                errorFullName.textContent = "";
            }
        }

        // Hàm validate Email
        function validateEmail() {
            const email = emailInput.value.trim();
            const errorEmail = document.getElementById("error-email");
            const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (email === "") {
                errorEmail.textContent = "* Email không được để trống.";
            } else if (!emailPattern.test(email)) {
                errorEmail.textContent = "* Email không hợp lệ.";
            } else if (email.length > 100) {
                errorEmail.textContent = "* Email không được vượt quá 100 ký tự.";
            } else {
                errorEmail.textContent = ""; // Không có lỗi
            }
        }

        // Hàm validate Phone
        function validatePhone() {
            const phone = phoneInput.value.trim();
            const errorPhone = document.getElementById("error-phone");
            const phonePattern = /^[0-9]{10,11}$/; // Chỉ chấp nhận số có 10-11 chữ số
            if (phone === "") {
                errorPhone.textContent = "* Số điện thoại không được để trống.";
            } else if (!phonePattern.test(phone)) {
                errorPhone.textContent = "* Số điện thoại không hợp lệ.";
            } else {
                errorPhone.textContent = ""; // Không có lỗi
            }
        }

        function validateAddressSelection() {
            const province = provinceSelect.value;
            const district = districtSelect.value;
            const ward = wardSelect.value;
            const errorSelectAddress = document.getElementById("error-select-address");
            if (!province || !district || !ward) {
                errorSelectAddress.textContent = "* Bạn cần chọn đầy đủ Tỉnh, Huyện, và Xã/Phường";
            } else {
                errorSelectAddress.textContent = "";
            }
        }

        // Hàm validate Specific Address
        function validateSpecificAddress() {
            const specificAddress = specificAddressInput.value.trim();
            const errorAddress = document.getElementById("error-address-specific");
            if (specificAddress === "") {
                errorAddress.textContent = "* Địa chỉ cụ thể không được để trống.";
            } else if (specificAddress.length > 260) {
                errorAddress.textContent = "* Địa chỉ cụ thể không được vượt quá 260 ký tự.";
            } else {
                errorAddress.textContent = "";
            }
        }

        fullNameInput.addEventListener("input", validateFullName);
        emailInput.addEventListener("input", validateEmail);
        phoneInput.addEventListener("input", validatePhone);
        specificAddressInput.addEventListener("input", validateSpecificAddress);
        provinceSelect.addEventListener("change", validateAddressSelection);
        districtSelect.addEventListener("change", validateAddressSelection);
        wardSelect.addEventListener("change", validateAddressSelection);
    }
});

function payBill() {
    const accountLogin = document.getElementById("account-login").value;
    console.log("Account login: " + accountLogin);
    if (!accountLogin) {
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
            document.getElementById("error-fullname").textContent = "* Họ tên không được quá 250 ký tự";
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

        const phoneInput = document.getElementById("Phone");
        const phone = phoneInput.value.trim();
        const phonePattern = /^(0[3|5|7|8|9])+([0-9]{8})$/;
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
    } else {
        const infoCustomer = document.getElementById("infoCustomer");
        const originalAddress = document.getElementById("original-address");
        const addressShip = document.getElementById("addressShip");

        let isValid = true;
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
        if (!infoCustomer || infoCustomer.textContent.trim() === "") {
            document.getElementById("error-address-for-bill").textContent = "* Thông tin khách hàng không được để trống";
            isValid = false;
        } else {
            document.getElementById("error-address-for-bill").textContent = "";
        }

        if (!originalAddress || originalAddress.textContent.trim() === "") {
            document.getElementById("error-address-for-bill").textContent = "* Địa chỉ gốc không được để trống";
            isValid = false;
        } else {
            document.getElementById("error-address-for-bill").textContent = "";
        }

        // Kiểm tra địa chỉ giao hàng
        if (!addressShip || addressShip.value.trim() === "") {
            document.getElementById("error-address-for-bill").textContent = "* Địa chỉ giao hàng không được để trống";
            isValid = false;
        } else {
            document.getElementById("error-address-for-bill").textContent = "";
        }

        if (!isValid) {
            return;
        }
    }
    Swal.fire({
        title: 'Bạn có chắc chắn muốn đặt hàng với đơn hàng này?',
        text: "Sau khi xác nhận, bạn sẽ không thể thay đổi đơn hàng.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        customClass: {
            popup: 'swal-popup'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const addressShip = document.getElementById("addressShip").value;
            const shippingPriceText = $('#spanShippingFee').text().trim();
            const voucherPriceText = $('#spanPriceVoucher').text().trim();
            const totalAmountBillText = $('#spanTotalPriceCartItem').text().trim();
            const noteBill = $('#noteBill').val();
            const selectedRadioPaymentMethod = document.querySelector('input[name="payment_method_id"]:checked');
            let shippingPrice = parseFloat(shippingPriceText.replace(/[^0-9.-]+/g, ''));
            let priceVoucher = parseFloat(voucherPriceText.replace(/[^0-9.-]+/g, ''));
            let totalAmountBill = parseFloat(totalAmountBillText.replace(/[^0-9.-]+/g, ''));

            shippingPrice = isNaN(shippingPrice) ? 0 : shippingPrice;
            priceVoucher = isNaN(priceVoucher) ? 0 : priceVoucher;
            totalAmountBill = isNaN(totalAmountBill) ? 0 : totalAmountBill;

            let payMethod = selectedRadioPaymentMethod.value;
            console.log("Pay method value: " + payMethod);

            const errorElement = document.getElementById("error-total-amount-bill");

            if (parseInt(payMethod) === 1) {
                if (totalAmountBill > 100000000) {
                    errorElement.textContent = "Tổng tiền phải nhỏ hơn 100 triệu cho phương thức COD.";
                    return false;
                } else {
                    errorElement.textContent = "";
                }
            } else if (parseInt(payMethod) === 2) {
                if (totalAmountBill > 20000000) {
                    errorElement.textContent = "Tổng tiền phải nhỏ hơn 20 triệu cho phương thức VNPAY.";
                    return false;
                } else {
                    errorElement.textContent = "";
                }
            }

            $.ajax({
                url: '/onepoly/payment', type: 'POST', contentType: 'application/json', data: JSON.stringify({
                    addressShip: addressShip,
                    shippingPrice: shippingPrice,
                    voucherPrice: priceVoucher,
                    totalAmountBill: totalAmountBill,
                    noteBill: noteBill,
                    payMethod: payMethod
                }), success: function (response) {
                    console.log(response);
                    window.location.href = response;
                }, error: function (error) {
                    console.log(error);
                }
            });
        }
    });
}

