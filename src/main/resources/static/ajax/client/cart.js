function applyVoucher() {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn áp dụng phiếu giảm giá này không?',
        text: "Sau khi xác nhận, bạn sẽ áp dụng đợt giảm giá cho đơn hàng của bạn.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        customClass: {
            popup: 'swal-popup'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            const selectedRadio = document.querySelector('input[name="radioVoucher"]:checked');
            if (selectedRadio) {
                const voucherId = selectedRadio.closest('.cart-voucher-item').querySelector('.voucher-id').textContent.trim();
                const totalPrice = parseFloat(document.getElementById('totalPriceCartItem').textContent.trim().replace(/VNĐ|,/g, '')) || 0;

                $.ajax({
                    url: `/api-client/selected-voucher/${voucherId}`,
                    method: 'GET',
                    success: function (data) {
                        if (data.check === '1') {
                            const discountType = data.voucherType;
                            const discountValue = parseFloat(data.priceReduced) || 0;

                            if (isNaN(discountType) || isNaN(discountValue)) {
                                createToast("2", "Dữ liệu giảm giá không hợp lệ.");
                                return;
                            }

                            const voucherTypeElem = document.getElementById('type-voucher-apply');
                            if (voucherTypeElem) {
                                voucherTypeElem.innerText = discountType === 1 ? 'Giảm theo phần trăm' : 'Giảm theo giá trị';
                            }

                            let priceReduced = calculateDiscount(totalPrice, discountType, discountValue);
                            if (isNaN(priceReduced)) {
                                console.warn("Giá giảm tính toán không hợp lệ.");
                                priceReduced = 0;
                            }

                            priceReduced = Math.min(priceReduced, totalPrice);
                            const finalPrice = Math.max(0, totalPrice - priceReduced);

                            updateCartDisplay(priceReduced, finalPrice);

                            sessionStorage.setItem('priceVoucherReduced', priceReduced.toLocaleString('en-US') + ' VNĐ');
                            sessionStorage.setItem('finalPrice', finalPrice.toLocaleString('en-US') + ' VNĐ');
                            sessionStorage.setItem('priceReduced', priceReduced.toLocaleString('en-US') + ' VNĐ');
                            sessionStorage.setItem('toastCheck', data.check);
                            sessionStorage.setItem('toastMessage', data.message);

                            window.location.reload();
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
    });
}


function UnApplyVoucherForCart() {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn hủy áp dụng phiếu giảm giá này không?',
        text: "Sau khi xác nhận, bạn sẽ hủy áp dụng.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy',
        customClass: {
            popup: 'swal-popup'
        }
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/api-client/un-apply-voucher',
                type: 'GET',
                success: function (response) {
                    sessionStorage.setItem('toastCheckUnApplyVoucher', response.check);
                    sessionStorage.setItem('toastMessageUnApplyVoucher', response.message);
                    window.location.reload();
                },
                error: function (error) {
                    createToast("3", "Xóa thất bại");
                }
            })
        }
    });
}

function calculateDiscount(totalPrice, discountType, discountValue) {
    let priceReduced = 0;
    if (discountType === 1) {
        priceReduced = totalPrice * (discountValue / 100);
    } else if (discountType === 2) {
        priceReduced = discountValue;
    }
    return Math.min(priceReduced, totalPrice);
}

function updateCartDisplay(priceReduced, finalPrice) {
    document.getElementById('priceVoucherReduced').textContent = priceReduced.toLocaleString('en-US') + ' VNĐ';
    document.getElementById('cart-spanTotalPriceCart').textContent = finalPrice.toLocaleString('en-US') + ' VNĐ';
}

function parseCurrency(value) {
    if (!value) return 0;
    return parseFloat(value.replace(/VNĐ|,/g, "").trim());
}

window.addEventListener('load', function () {
    const totalPriceElem = document.getElementById('totalPriceCartItem');
    const totalPrice = parseCurrency(totalPriceElem.textContent);
    const priceVoucherReduced = parseCurrency(sessionStorage.getItem('priceVoucherReduced') || "0");

    if (!isNaN(priceVoucherReduced) && priceVoucherReduced > 0 && !isNaN(totalPrice)) {
        const finalPrice = Math.max(totalPrice - priceVoucherReduced, 0);
        updateCartDisplay(priceVoucherReduced, finalPrice);
    }

    const toastCheck = sessionStorage.getItem('toastCheck');
    const toastMessage = sessionStorage.getItem('toastMessage');

    if (toastCheck && toastMessage) {
        createToast(toastCheck, toastMessage);
        sessionStorage.removeItem('toastCheck');
        sessionStorage.removeItem('toastMessage');
    }

    const toastCheckUnApplyVoucher = sessionStorage.getItem('toastCheckUnApplyVoucher');
    const toastMessageUnApplyVoucher = sessionStorage.getItem('toastMessageUnApplyVoucher');
    if (toastCheckUnApplyVoucher && toastMessageUnApplyVoucher) {
        createToast(toastCheckUnApplyVoucher, toastMessageUnApplyVoucher);
        sessionStorage.removeItem('toastCheckUnApplyVoucher');
        sessionStorage.removeItem('toastMessageUnApplyVoucher');
    }

    setTimeout(function () {
        sessionStorage.removeItem('priceVoucherReduced');
    }, 2000);
});

function calculateTotalPrice() {
    let totalPrice = 0;

    document.querySelectorAll(".cart-item").forEach(item => {
        const priceElem = item.querySelector("#cart-spanPriceCartItem");
        const quantityElem = item.querySelector("#quantityProductFormCart");

        if (!priceElem || !quantityElem) {
            console.warn("Dữ liệu sản phẩm không hợp lệ.");
            return;
        }

        const price = parseFloat(priceElem.innerText.replace(/VNĐ|,/g, "")) || 0;
        const quantity = parseInt(quantityElem.innerText) || 0;

        if (isNaN(price) || isNaN(quantity)) {
            console.warn("Giá hoặc số lượng không hợp lệ:", {price, quantity});
            return;
        }

        const totalProductPrice = price * quantity;

        totalPrice += totalProductPrice;

        const totalProductPriceElem = item.querySelector("#cart-item-total-price");
        if (totalProductPriceElem) {
            totalProductPriceElem.innerText = Math.max(0, totalProductPrice).toLocaleString('en-US') + " VNĐ";
        }
    });

    const priceVoucherReducedElement = document.getElementById('priceVoucherReduced');
    const priceVoucherReduced = parseFloat(
        priceVoucherReducedElement ? priceVoucherReducedElement.innerText.replace(/VNĐ|,/g, "") : 0
    ) || 0;

    if (isNaN(priceVoucherReduced)) {
        console.warn("Giá trị giảm giá không hợp lệ:", priceVoucherReduced);
    }

    const finalPrice = Math.max(0, totalPrice - priceVoucherReduced);

    document.getElementById("totalPriceCartItem").innerText = Math.max(0, totalPrice).toLocaleString('en-US') + " VNĐ";
    document.getElementById("priceVoucherReduced").innerText = Math.max(0, priceVoucherReduced).toLocaleString('en-US') + " VNĐ";
    document.getElementById("cart-spanTotalPriceCart").innerText = finalPrice.toLocaleString('en-US') + " VNĐ";
}

function removeProductDetailFromCart(btn) {
    const cartItem = btn.closest(".cart-list-cart");
    const productId = btn.getAttribute('field');

    $.ajax({
        url: '/api-client/remove-from-cart/' + productId,
        type: 'POST',
        success: function (response) {
            console.log(response);
            cartItem.remove();

            const cartCountElement = document.querySelector("#cart-count");
            let currentCount = parseInt(cartCountElement.textContent, 10);
            calculateTotalPrice();

            if (currentCount > 1) {
                cartCountElement.textContent = currentCount - 1;
            } else {
                const cartContainer = document.querySelector("#cart-total-container");
                cartContainer.innerHTML = `
                    <div style="text-align: center; margin-top: 100px; border: 1px solid grey; border-radius: 8px; padding: 20px; background-color: #f9f9f9;">
                        <img src="/img/client/category/null-cart.png" alt="Giỏ hàng trống" style="width: 500px; margin: auto; display: block;" />
                        <p style="margin-top: 20px; font-size: 18px; color: #555;">Không có sản phẩm nào trong giỏ hàng của bạn!</p>
                        <a href="/onepoly/home" class="btn-go-home" style="display: inline-block; width: 400px; margin-top: 20px; padding: 10px 20px; background-color: #06519b; color: #fff; text-decoration: none; border-radius: 4px;">Tiếp tục mua sắm</a>
                    </div>
                `;
                cartCountElement.textContent = 0;
            }

            const totalPrice = parseFloat(document.getElementById("totalPriceCartItem").textContent.replace('₫', '').trim());
            const applyPrice = parseFloat(document.getElementById("applyPriceVoucherHidden").value);

            if (totalPrice < applyPrice) {
                location.reload();
            }
        },
        error: function (xhr, status, error) {
            alert('Có lỗi khi gửi yêu cầu');
        }
    });
}

document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll("#cart-spanPriceCartItem").forEach(function (discountedPriceElem) {
        const discountedPrice = parseFloat(discountedPriceElem.getAttribute("data-price")) || 0;
        const originalPriceElem = discountedPriceElem.closest(".cart-item")
            .querySelector("#cart-spanPriceCartItemOriginal");
        const originalPrice = parseFloat(originalPriceElem.getAttribute("data-price")) || 0;

        if (discountedPrice === originalPrice) {
            originalPriceElem.style.display = "none";
        }
    });

    function updateQuantity(button, change) {
        const cartItem = button.closest(".cart-item");
        const quantityElem = cartItem.querySelector("#quantityProductFormCart");
        let currentQuantity = parseInt(quantityElem.innerText) || 0;

        let newQuantity = Math.max(1, Math.min(currentQuantity + change, 10));

        quantityElem.innerText = newQuantity;

        const productDetailId = button.getAttribute('field');

        updateQuantityInServer(productDetailId, newQuantity, currentQuantity, quantityElem);
    }

    function updateQuantityInServer(productDetailId, quantityItem, currentQuantity, quantityElem) {
        $.ajax({
            type: "POST",
            url: "/api-client/update-from-cart/" + productDetailId,
            contentType: "application/json",
            data: JSON.stringify({quantityItem: quantityItem}),
            success: function (response) {
                if (response.check === "1") {
                    updateCartTotal();
                } else {
                    createToast(response.check, response.message);
                    quantityElem.innerText = currentQuantity;
                }
            },
            error: function (xhr) {
                const response = JSON.parse(xhr.responseText);
                createToast(response.check, response.message);

                quantityElem.innerText = currentQuantity;
            }
        });
    }

    function updateCartTotal() {
        let totalPrice = 0;
        document.querySelectorAll(".cart-item").forEach(item => {
            const priceElem = item.querySelector("#cart-spanPriceCartItem");
            const quantityElem = item.querySelector("#quantityProductFormCart");

            if (!priceElem || !quantityElem) return;

            const price = parseCurrency(priceElem.innerText);
            const quantity = parseInt(quantityElem.innerText) || 0;

            if (isNaN(price)) return;
            totalPrice += price * quantity;
        });

        totalPrice = Math.max(0, totalPrice);
        document.getElementById("totalPriceCartItem").innerText = totalPrice.toLocaleString('en-US') + " VNĐ";

        const priceReducedElem = document.getElementById("priceVoucherReduced");


        let discountValue = parseCurrency(priceReducedElem.innerText.trim()) || 0;

        discountValue = Math.min(discountValue, totalPrice);
        const finalPrice = Math.max(0, totalPrice - discountValue);
        updateCartDisplay(discountValue, finalPrice);

        calculateTotalPrice();
    }

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
document.querySelectorAll('.cart-price-item').forEach(el => {
    let price = parseFloat(el.getAttribute('data-price'));
    price = Math.max(0, price);
    el.textContent = Math.floor(price).toLocaleString('en-US') + " VNĐ";
});

document.querySelectorAll('.original-price').forEach(el => {
    let price = parseFloat(el.getAttribute('data-price'));
    price = Math.max(0, price); // Đảm bảo giá trị không âm
    el.textContent = Math.floor(price).toLocaleString('en-US') + " VNĐ";
});


document.querySelectorAll('.voucher-discount-voucher-apply').forEach(el => {
    // Lấy nội dung giảm giá từ textContent
    const text = el.textContent;
    const match = text.match(/(\d+([.,]\d+)?)/);
    if (match) {
        const price = parseFloat(match[1].replace(',', '')); // Chuyển đổi sang số
        el.textContent = text.replace(match[1], price.toLocaleString('en-US'));
    } else {
        el.textContent = 'N/A';
    }
});
document.querySelectorAll('.voucher-apply-success').forEach(el => {
    const price = parseFloat(el.getAttribute('data-price'));
    const typeVoucherApplyText = document.getElementById("typeVoucherApply").value;
    if (!isNaN(price)) {
        let displayText;
        if (typeVoucherApplyText === "2") {
            displayText = "Giá trị giảm: " + price.toLocaleString('en-US') + " VNĐ";
        } else if (typeVoucherApplyText === "1") {
            displayText = "Giá trị giảm: " + price.toLocaleString('en-US') + " %";
        } else {
            displayText = "Giá trị giảm: N/A";
        }
        el.textContent = displayText;
    } else {
        el.textContent = "Giá trị giảm: N/A";
    }
});


