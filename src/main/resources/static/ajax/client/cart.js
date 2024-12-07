// Hàm áp dụng voucher và cập nhật giá trị giỏ hàng
function applyVoucher() {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn áp dụng phiếu giảm giá này không?',
        text: "Sau khi xác nhận, bạn sẽ áp dụng đợt giảm giá cho đơn hàng của bạn.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            const selectedRadio = document.querySelector('input[name="radioVoucher"]:checked');
            if (selectedRadio) {
                const voucherId = selectedRadio.closest('.cart-voucher-item').querySelector('.voucher-id').textContent.trim();
                console.log("ID Voucher: " + voucherId);
                const totalPrice = parseFloat(document.getElementById('totalPriceCartItem').textContent.trim().replace(/₫|,/g, '')) || 0;
                console.log("Tổng tiền: " + totalPrice);
                $.ajax({
                    url: `/api-client/selected-voucher/${voucherId}`,
                    method: 'GET',
                    success: function (data) {
                        if (data.check === '1') {
                            createToast(data.check, data.message);
                            const discountType = data.voucherType; // Lấy kiểu giảm giá
                            const discountValue = parseFloat(data.priceReduced) || 0; // Lấy giá trị giảm giá

                            // Kiểm tra giá trị hợp lệ của voucherType và priceReduced
                            if (isNaN(discountType) || isNaN(discountValue)) {
                                alert("Dữ liệu giảm giá không hợp lệ.");
                                return;
                            }

                            const voucherTypeElem = document.getElementById('type-voucher-apply');
                            if (voucherTypeElem) {
                                voucherTypeElem.innerText = discountType === 1 ? 'Giảm theo phần trăm' : 'Giảm theo giá trị';
                            }

                            console.log("Voucher Type: " + discountType);

                            // Tính toán giá giảm
                            let priceReduced = calculateDiscount(totalPrice, discountType, discountValue);
                            if (isNaN(priceReduced)) {
                                console.warn("Giá giảm tính toán không hợp lệ.");
                                priceReduced = 0;
                            }

                            const finalPrice = Math.max(0, totalPrice - priceReduced);
                            updateCartDisplay(priceReduced, finalPrice);

                            // Lưu trữ thông tin giảm giá vào sessionStorage
                            sessionStorage.setItem('priceVoucherReduced', priceReduced.toLocaleString('en-US') + ' ₫');
                            setTimeout(function () {
                                sessionStorage.setItem('finalPrice', finalPrice.toLocaleString('en-US') + ' ₫');
                                sessionStorage.setItem('priceReduced', priceReduced.toLocaleString('en-US') + ' ₫');
                                window.location.reload(); // reload trang sau khi cập nhật giá trị
                            }, 3500); // Đợi một chút để cập nhật sessionStorage trước khi reload
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
    // document.getElementById('price-Calculator').textContent = finalPrice.toLocaleString('en-US') + ' ₫';
}

// Hàm parse giá trị tiền tệ
function parseCurrency(value) {
    if (!value) return 0;
    return parseFloat(value.replace(/₫|,/g, "").trim());
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

function calculateTotalPrice() {
    let totalPrice = 0;

    document.querySelectorAll(".cart-item").forEach(item => {
        const priceElem = item.querySelector("#cart-spanPriceCartItem");
        const quantityElem = item.querySelector("#quantityProductFormCart");

        if (!priceElem || !quantityElem) {
            console.warn("Dữ liệu sản phẩm không hợp lệ.");
            return;
        }

        const price = parseFloat(priceElem.innerText.replace(/₫|,/g, "")) || 0;
        const quantity = parseInt(quantityElem.innerText) || 0;

        if (isNaN(price) || isNaN(quantity)) {
            console.warn("Giá hoặc số lượng không hợp lệ:", {price, quantity});
            return;
        }

        const totalProductPrice = price * quantity;

        totalPrice += totalProductPrice;

        const totalProductPriceElem = item.querySelector("#cart-item-total-price");
        if (totalProductPriceElem) {
            totalProductPriceElem.innerText = totalProductPrice.toLocaleString('en-US') + " ₫";
        }
    });

    const priceVoucherReducedElement = document.getElementById('priceVoucherReduced');
    const priceVoucherReduced = parseFloat(
        priceVoucherReducedElement ? priceVoucherReducedElement.innerText.replace(/₫|,/g, "") : 0
    ) || 0;

    if (isNaN(priceVoucherReduced)) {
        console.warn("Giá trị giảm giá không hợp lệ:", priceVoucherReduced);
    }

    const finalPrice = Math.max(0, totalPrice - priceVoucherReduced);

    console.log("Tổng tiền:", totalPrice);
    console.log("Giảm được:", priceVoucherReduced);
    console.log("Giá cuối cùng:", finalPrice);

    // Cập nhật giá trị trên giao diện
    document.getElementById("totalPriceCartItem").innerText = totalPrice.toLocaleString('en-US') + " ₫";
    document.getElementById("priceVoucherReduced").innerText = priceVoucherReduced.toLocaleString('en-US') + " ₫";
    // document.getElementById("price-Calculator").innerText = finalPrice.toLocaleString('en-US') + " ₫";
    document.getElementById("cart-spanTotalPriceCart").innerText = finalPrice.toLocaleString('en-US') + " ₫";
}

// Hàm xóa sản phẩm khỏi giỏ hàng
function removeProductDetailFromCart(btn) {
    const cartItem = btn.closest(".cart-list-cart");
    const productId = btn.getAttribute('field');
    console.log("Sản phẩm có id:", productId);

    $.ajax({
        url: '/api-client/remove-from-cart/' + productId,
        type: 'POST',
        success: function (response) {
            console.log(response);

            // Xóa sản phẩm khỏi giao diện
            cartItem.remove();

            const cartCountElement = document.querySelector("#cart-count");
            let currentCount = parseInt(cartCountElement.textContent, 10);
            calculateTotalPrice();
            if (currentCount > 1) {
                cartCountElement.textContent = currentCount - 1;
            } else {
                // Nếu số lượng bằng 0, hiển thị giao diện giỏ hàng trống
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
        },
        error: function (xhr, status, error) {
            alert('Có lỗi khi gửi yêu cầu');
        }
    });
}


// Tính tổng giỏ hàng và cập nhật giao diện
document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll("#cart-spanPriceCartItem").forEach(function (discountedPriceElem) {
        // Lấy giá trị giảm giá và giá gốc
        const discountedPrice = parseFloat(discountedPriceElem.getAttribute("data-price")) || 0;
        const originalPriceElem = discountedPriceElem.closest(".cart-item")
            .querySelector("#cart-spanPriceCartItemOriginal");
        const originalPrice = parseFloat(originalPriceElem.getAttribute("data-price")) || 0;

        // Kiểm tra nếu giá trị bằng nhau, ẩn giá gốc
        if (discountedPrice === originalPrice) {
            originalPriceElem.style.display = "none";
        }
    });


// Cập nhật giỏ hàng khi thay đổi số lượng sản phẩm
    // Cập nhật số lượng sản phẩm
    function updateQuantity(button, change) {
        const cartItem = button.closest(".cart-item");
        const quantityElem = cartItem.querySelector("#quantityProductFormCart");
        let quantityItem = parseInt(quantityElem.innerText) || 0;

        quantityItem = Math.max(1, Math.min(quantityItem + change, 10));
        quantityElem.innerText = quantityItem;

        const productDetailId = button.getAttribute('field');
        updateQuantityInServer(productDetailId, quantityItem);
        calculateTotalPrice();
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

    function updateCartTotal() {
        let totalPrice = 0;
        document.querySelectorAll(".cart-item").forEach(item => {
            const priceElem = item.querySelector("#cart-spanPriceCartItem");
            const quantityElem = item.querySelector("#quantityProductFormCart");

            if (!priceElem || !quantityElem) return;

            const price = parseCurrency(priceElem.innerText);
            const quantity = parseInt(quantityElem.innerText) || 0;

            if (isNaN(price)) return; // Kiểm tra nếu giá trị không hợp lệ
            totalPrice += price * quantity;
        });

        totalPrice = Math.max(0, totalPrice);  // Đảm bảo tổng tiền không âm
        document.getElementById("totalPriceCartItem").innerText = totalPrice.toLocaleString('en-US') + " ₫";

        const priceReducedElem = document.getElementById("priceVoucherReduced");


        let discountValue = parseCurrency(priceReducedElem.innerText.trim()) || 0;

        discountValue = Math.min(discountValue, totalPrice);  // Đảm bảo giá giảm không vượt quá tổng tiền
        const finalPrice = Math.max(0, totalPrice - discountValue);
        updateCartDisplay(discountValue, finalPrice);

        // Tính lại tổng giá sau khi cập nhật
        calculateTotalPrice();  // Cần gọi lại hàm này sau khi cập nhật giỏ hàng
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

function UnApplyVoucherForCart() {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn hủy áp dụng phiếu giảm giá này không?',
        text: "Sau khi xác nhận, bạn sẽ hủy áp dụng.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xác nhận',
        cancelButtonText: 'Hủy'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: '/api-client/un-apply-voucher',
                type: 'GET',
                success: function (response) {
                    createToast(response.check, response.message);
                    if (response.check === '1') {
                        setTimeout(function () {
                            location.reload();
                        }, 3500);
                        createToast(response.check, response.message);
                    }
                },
                error: function (error) {
                    console.log("Xóa thất bại: " + error.responseText);
                }
            })
        }
    });
}


document.querySelectorAll('.cart-price-item').forEach(el => {
    const price = parseFloat(el.getAttribute('data-price'));
    el.textContent = Math.floor(price).toLocaleString('en-US') + " ₫";
});
document.querySelectorAll('.original-price').forEach(el => {
    const price = parseFloat(el.getAttribute('data-price'));
    el.textContent = Math.floor(price).toLocaleString('en-US') + " ₫";
});