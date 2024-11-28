// Hàm áp dụng voucher và cập nhật giá trị giỏ hàng
function applyVoucher() {
    const selectedRadio = document.querySelector('input[name="radioVoucher"]:checked');
    if (selectedRadio) {
        const voucherId = selectedRadio.closest('.cart-voucher-item').querySelector('.voucher-id').textContent.trim();
        console.log("ID Voucher: " + voucherId);

        const totalPrice = parseFloat(document.getElementById('totalPriceCartItem').textContent.trim().replace(/₫|,/g, '')) || 0;
        console.log("Tổng tiền: " + totalPrice);

        // Gửi yêu cầu AJAX để lấy thông tin voucher
        $.ajax({
            url: `/api-client/selected-voucher/${voucherId}`,
            method: 'GET',
            success: function (data) {
                if (data) {
                    alert('Voucher đã được chọn thành công!');
                    const discountType = data.voucherType;
                    const discountValue = parseFloat(data.priceReduced) || 0;
                    let priceReduced = calculateDiscount(totalPrice, discountType, discountValue);

                    const finalPrice = Math.max(0, totalPrice - priceReduced);

                    updateCartDisplay(priceReduced, finalPrice);
                    sessionStorage.setItem('priceVoucherReduced', priceReduced.toLocaleString('en-US') + ' ₫');
                    setTimeout(function () {
                        sessionStorage.setItem('finalPrice', finalPrice.toLocaleString('en-US') + ' ₫');
                        sessionStorage.setItem('priceReduced', priceReduced.toLocaleString('en-US') + ' ₫');
                        window.location.reload();
                    }, 300);
                } else {
                    alert('Voucher không tồn tại hoặc đã hết hạn.');
                }
            },
            error: function (error) {
                console.error('Lỗi:', error);
                alert('Có lỗi xảy ra khi chọn voucher.');
            }
        });
    }
}

// Hàm tính toán giảm giá
function calculateDiscount(totalPrice, discountType, discountValue) {
    let priceReduced = 0;
    if (discountType === 1) {
        // Giảm theo %
        priceReduced = totalPrice * (discountValue / 100);
    } else if (discountType === 2) {
        // Giảm theo số tiền
        priceReduced = discountValue;
    }
    return Math.min(priceReduced, totalPrice); // Đảm bảo giá trị giảm không vượt quá tổng giá trị
}

// Cập nhật hiển thị giỏ hàng
function updateCartDisplay(priceReduced, finalPrice) {
    document.getElementById('priceVoucherReduced').textContent = priceReduced.toLocaleString('en-US') + ' ₫';
    document.getElementById('cart-spanTotalPriceCart').textContent = finalPrice.toLocaleString('en-US') + ' ₫';
    document.getElementById('price-Calculator').textContent = finalPrice.toLocaleString('en-US') + ' ₫';
}

// Hàm parse giá trị tiền tệ
function parseCurrency(value) {
    if (typeof value !== 'string') {
        console.error("Giá trị truyền vào không phải chuỗi:", value);
        return 0;
    }

    const parsedValue = parseFloat(value.replace(/[^\d.-]/g, ''));
    if (isNaN(parsedValue)) {
        console.error("Không thể chuyển đổi giá trị thành số:", value);
        return 0;
    }

    return parsedValue;
}

// Khi trang tải xong
window.addEventListener('load', function () {
    const totalPriceElem = document.getElementById('totalPriceCartItem');
    const totalPrice = parseCurrency(totalPriceElem.textContent);
    const priceVoucherReduced = parseCurrency(sessionStorage.getItem('priceVoucherReduced') || "0");

    if (!isNaN(priceVoucherReduced) && priceVoucherReduced > 0 && !isNaN(totalPrice)) {
        const finalPrice = Math.max(totalPrice - priceVoucherReduced, 0);
        updateCartDisplay(priceVoucherReduced, finalPrice);
    }

    setTimeout(function () {
        sessionStorage.removeItem('priceVoucherReduced');
    }, 2000);
});

// Hàm toggle hiển thị danh sách voucher
function toggleVoucherList() {
    const voucherList = document.getElementById('voucher-list');
    const voucherButtonText = document.getElementById('voucher-button-text');
    const voucherButtonImg = document.getElementById('voucher-button-img');

    if (voucherList.style.display === 'none') {
        voucherList.style.display = 'block';
        voucherButtonImg.style.display = 'none';
        voucherButtonText.textContent = "Đóng danh sách";
    } else {
        voucherList.style.display = 'none';
        voucherButtonImg.style.display = 'inline-block';
        voucherButtonText.textContent = "Áp dụng voucher";
    }
}

// Hàm xóa sản phẩm khỏi giỏ hàng
function removeProductDetailFromCart(btn) {
    const cartItem = btn.closest(".cart-item");
    const productId = btn.getAttribute('field');
    console.log("Sản phẩm có id:", productId);
    $.ajax({
        url: '/api-client/remove-from-cart/' + productId,
        type: 'POST',
        success: function (response) {
            console.log(response);
            cartItem.remove();
        },
        error: function (xhr, status, error) {
            alert('Có lỗi khi gửi yêu cầu');
        }
    });
}

// Tính tổng giỏ hàng và cập nhật giao diện
// Tính tổng giỏ hàng và cập nhật giao diện
document.addEventListener("DOMContentLoaded", function () {
    function calculateTotalPrice() {
        let totalPrice = 0;

        // Tính tổng giá trị các sản phẩm trong giỏ hàng
        document.querySelectorAll(".cart-item").forEach(item => {
            const priceElem = item.querySelector("#cart-spanPriceCartItem");
            const quantityElem = item.querySelector("#quantityProductFormCart");

            // Kiểm tra sự tồn tại của phần tử trước khi sử dụng
            if (!priceElem || !quantityElem) return;

            const price = parseFloat(priceElem.innerText.replace(/₫|,/g, "")) || 0;
            const quantity = parseInt(quantityElem.innerText) || 0;

            // Tính giá trị của mỗi sản phẩm
            const totalProductPrice = price * quantity;

            // Cập nhật giá trị tổng của mỗi sản phẩm
            const totalProductPriceElem = item.querySelector("#cart-item-total-price");
            if (totalProductPriceElem) {
                totalProductPriceElem.innerText = totalProductPrice.toLocaleString('en-US') + " ₫";
            }

            totalPrice += totalProductPrice;
        });

        // Lấy giá trị giảm giá từ sessionStorage hoặc mặc định
        const priceVoucherReduced = parseFloat(sessionStorage.getItem('priceVoucherReduced') || 0);
        const discountType = document.getElementById("voucher-type")?.innerText.trim();

        // Tính giá cuối cùng sau giảm giá
        let discountValue = 0;
        if (discountType == 1) {
            discountValue = totalPrice * (priceVoucherReduced / 100);  // Giảm theo phần trăm
        } else if (discountType == 2) {
            discountValue = priceVoucherReduced;  // Giảm theo giá trị cố định
        }

        const finalPrice = Math.max(0, totalPrice - discountValue);  // Đảm bảo giá không âm

        // Cập nhật tất cả các phần tử hiển thị trong HTML
        const totalPriceElem = document.getElementById("totalPriceCartItem");
        const priceVoucherElem = document.getElementById("priceVoucherReduced");
        const priceCalculatorElem = document.getElementById("price-Calculator");
        const cartTotalPriceElem = document.getElementById("cart-spanTotalPriceCart");

        if (totalPriceElem) totalPriceElem.innerText = totalPrice.toLocaleString('en-US') + " ₫";
        if (priceVoucherElem) priceVoucherElem.innerText = priceVoucherReduced.toLocaleString('en-US') + " ₫";
        if (priceCalculatorElem) priceCalculatorElem.innerText = finalPrice.toLocaleString('en-US') + " ₫";
        if (cartTotalPriceElem) cartTotalPriceElem.innerText = finalPrice.toLocaleString('en-US') + " ₫";
    }

// Cập nhật giỏ hàng khi thay đổi số lượng sản phẩm
    function updateQuantity(button, change) {
        const cartItem = button.closest(".cart-item");
        const quantityElem = cartItem.querySelector("#quantityProductFormCart");
        let quantityItem = parseInt(quantityElem.innerText) || 0;

        quantityItem = Math.max(1, Math.min(quantityItem + change, 10)); // Giới hạn số lượng từ 1 đến 10
        quantityElem.innerText = quantityItem;

        const productDetailId = button.getAttribute('field');
        updateQuantityInServer(productDetailId, quantityItem);
        calculateTotalPrice();  // Tính lại tổng giá trị giỏ hàng sau khi thay đổi số lượng
    }

// Cập nhật số lượng sản phẩm trên server
    function updateQuantityInServer(productDetailId, quantityItem) {
        $.ajax({
            type: "POST",
            url: "/api-client/update-from-cart/" + productDetailId,
            contentType: "application/json",
            data: JSON.stringify({quantityItem: quantityItem}),
            success: function () {
                console.log("Cập nhật thành công");
                updateCartTotal();
            },
            error: function (xhr) {
                console.error("Lỗi khi cập nhật:", xhr.responseText);
                alert("Cập nhật thất bại, vui lòng thử lại.");
            }
        });
    }

    // Cập nhật tổng giá trị giỏ hàng
    function updateCartTotal() {
        let totalPrice = 0;
        document.querySelectorAll(".cart-item").forEach(item => {
            const priceElem = item.querySelector("#cart-spanPriceCartItem");
            const quantityElem = item.querySelector("#quantityProductFormCart");

            if (!priceElem || !quantityElem) return;

            const price = parseCurrency(priceElem.innerText);
            const quantity = parseInt(quantityElem.innerText) || 0;

            totalPrice += price * quantity;
        });

        totalPrice = Math.max(0, totalPrice);
        document.getElementById("totalPriceCartItem").innerText = totalPrice.toLocaleString('en-US') + " ₫";

        const voucherTypeElem = document.getElementById("voucher-type");
        const priceReducedElem = document.getElementById("priceVoucherReduced");
        let discountType = parseInt(voucherTypeElem.innerText.trim()) || 1;
        let discountValue = parseCurrency(priceReducedElem.innerText.trim());

        let priceReduced = 0;
        if (discountType === 1) {
            priceReduced = totalPrice * (discountValue / 100);
        } else if (discountType === 2) {
            priceReduced = Math.min(discountValue, totalPrice);
        }

        priceReduced = Math.min(priceReduced, totalPrice);
        const finalPrice = Math.max(0, totalPrice - priceReduced);

        updateCartDisplay(priceReduced, finalPrice); // Cập nhật giao diện giỏ hàng
    }

    // Sự kiện thay đổi số lượng sản phẩm
    document.querySelectorAll(".cart-qtyminus").forEach(button => {
        button.addEventListener("click", function () {
            updateQuantity(this, -1);
            calculateTotalPrice();
        });
    });

    document.querySelectorAll(".cart-qtyplus").forEach(button => {
        button.addEventListener("click", function () {
            updateQuantity(this, 1);
            calculateTotalPrice();
        });
    });

    calculateTotalPrice();
});
