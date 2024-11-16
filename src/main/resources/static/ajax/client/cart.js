// Khi áp dụng voucher và cập nhật giá trị trực tiếp
function applyVoucher() {
    const selectedRadio = document.querySelector('input[name="radioVoucher"]:checked');
    if (selectedRadio) {
        const voucherId = selectedRadio.closest('.cart-voucher-item').querySelector('.voucher-id').textContent.trim();
        console.log("ID Voucher: " + voucherId);

        const totalPrice = parseFloat(document.getElementById('totalPriceCartItem').textContent.trim().replace(/₫|,/g, ''));

        // Gửi yêu cầu AJAX để lấy thông tin voucher
        $.ajax({
            url: `/api-client/selected-voucher/${voucherId}`,
            method: 'GET',
            success: function (data) {
                if (data) {
                    alert('Voucher đã được chọn thành công!');

                    const discountType = data.voucherType; // Loại giảm giá (1: theo % / 2: theo số tiền)
                    const discountValue = parseFloat(data.priceReduced); // Giá trị giảm

                    let priceReduced = 0;
                    if (discountType === 1) {  // Giảm theo %
                        priceReduced = totalPrice * (discountValue / 100);
                    } else if (discountType === 2) {  // Giảm theo số tiền cố định
                        priceReduced = discountValue;
                    }

                    const finalPrice = totalPrice - priceReduced;

                    // Cập nhật giao diện ngay lập tức
                    document.getElementById('priceVoucherReduced').textContent = priceReduced.toLocaleString("vi-VN") + ' ₫';
                    document.getElementById('cart-spanTotalPriceCart').textContent = finalPrice.toLocaleString("vi-VN") + ' ₫';
                    document.getElementById('price-Calculator').textContent = finalPrice.toLocaleString("vi-VN") + ' ₫';

                    // Lưu dữ liệu voucher vào sessionStorage
                    sessionStorage.setItem('priceVoucherReduced', priceReduced.toFixed(2));

                    // Tải lại trang sau khi lưu vào sessionStorage (nếu cần thiết)
                    setTimeout(function () {
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


window.addEventListener('load', function () {
    const priceVoucherReduced = parseFloat(sessionStorage.getItem('priceVoucherReduced'));
    const totalPrice = parseFloat(document.getElementById('totalPriceCartItem').textContent.trim());

    if (priceVoucherReduced) {
        const finalPrice = totalPrice - priceVoucherReduced;

        document.getElementById('priceVoucherReduced').textContent = priceVoucherReduced.toFixed(2) + ' đ';
        document.getElementById('cart-spanTotalPriceCart').textContent = finalPrice.toFixed(2) + ' ₫';
        document.getElementById('price-Calculator').textContent = finalPrice.toFixed(2) + ' ₫';

        setTimeout(function () {
            sessionStorage.removeItem('priceVoucherReduced');
        }, 2000);
    }
});

function toggleVoucherList() {
    const voucherList = document.getElementById('voucher-list');
    const voucherButtonText = document.getElementById('voucher-button-text');
    const voucherButtonImg = document.getElementById('voucher-button-img');

    if (voucherList.style.display === 'none') {
        voucherList.style.display = 'block';
        voucherButtonImg.style.display = 'none';  // Ẩn ảnh
        voucherButtonText.textContent = "Đóng danh sách";  // Thay đổi text
    } else {
        voucherList.style.display = 'none';
        voucherButtonImg.style.display = 'inline-block';  // Hiển thị ảnh
        voucherButtonText.textContent = "Áp dụng voucher";  // Thay đổi text về ban đầu
    }
}

function removeFromCart(idProductDetailFromCart) {
    fetch(`/onepoly/remove-from-cart/${idProductDetailFromCart}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => response.json())
        .then(data => {
            const totalPrice = data.totalPrice;
            const priceReduced = data.priceReduced;
            const cartItems = data.cartItems;

            // Cập nhật tổng giá trị vào giao diện
            document.getElementById('totalPriceCartItem').textContent = totalPrice + ' ₫';

            // Cập nhật lại giá trị giảm giá (nếu có)
            if (priceReduced !== '0') {
                document.getElementById('priceVoucherReduced').textContent = priceReduced + ' ₫';
            } else {
                document.getElementById('priceVoucherReduced').textContent = '0 ₫';
            }

            // Cập nhật lại danh sách sản phẩm trong giỏ hàng
            const cartItemsContainer = document.getElementById('voucher-list');
            cartItemsContainer.innerHTML = ''; // Xóa danh sách cũ

            cartItems.forEach(item => {
                const productDiv = document.createElement('div');
                productDiv.textContent = `${item.productDetail.name} - ${item.quantity} x ${item.productDetail.price} ₫`;
                cartItemsContainer.appendChild(productDiv);
            });

            if (cartItems.length === 0) {
                cartItemsContainer.innerHTML = '<p>Giỏ hàng của bạn đang trống.</p>';
            }
        })
        .catch(error => console.error('Error removing product from cart:', error));
}

function removeProductDetailFromCart(button) {
    // Tìm đến phần tử <input> có id là 'idProductDetailFromCart' trong cùng một div với nút xóa
    const cartItemDiv = button.closest('.cart-item');  // Tìm div chứa sản phẩm
    const productDetailId = cartItemDiv.querySelector('#idProductDetailFromCart').value; // Lấy giá trị của hidden input

    // In ra giá trị của ID sản phẩm trong giỏ
    console.log("ID sản phẩm trong giỏ hàng: " + productDetailId);

    // Gọi AJAX để xóa sản phẩm khỏi giỏ
    removeFromCart(productDetailId);
    console.log("delete nxong")
}

document.addEventListener("DOMContentLoaded", function () {
    function updateQuantity(button, change) {
        const cartItem = button.closest(".cart-item");
        const quantityElem = cartItem.querySelector("#quantityProductFormCart");
        let quantity = parseInt(quantityElem.innerText);
        const productDetailId = cartItem.querySelector("#idProductDetailFromCart").value;

        quantity += change;
        console.log("productDetail ID+" + productDetailId);
        // Giới hạn số lượng sản phẩm
        if (quantity < 1) quantity = 1;
        else if (quantity > 10) {
            alert("Chỉ được mua tối đa 10 sản phẩm cùng loại.");
            quantity = 10;
        }

        // Cập nhật số lượng hiển thị
        quantityElem.innerText = quantity;

        // Gửi yêu cầu AJAX để cập nhật số lượng
        updateQuantityInServer(productDetailId, quantity);
    }

    function updateQuantityInServer(productId, quantity) {
        $.ajax({
            
        })
    //         method: "POST",
    //         headers: {
    //             "Content-Type": "application/x-www-form-urlencoded",
    //         },
    //         body: new URLSearchParams({quantityItem: quantity}),
    //     })
    //         .then(response => response.json())
    //         .then(data => {
    //             alert("HHHHHHH");
    //             if (data.messages) {
    //                 alert(data.messages);
    //             }
    //
    //             updateCartTotal();
    //         })
    //         .catch(error => console.error("Lỗi khi cập nhật giỏ hàng:", error));
    }

    function updateCartTotal() {
        let totalPrice = 0;

        document.querySelectorAll(".cart-item").forEach(item => {
            const priceElem = item.querySelector("#cart-spanPriceCartItem");
            const quantityElem = item.querySelector("#quantityProductFormCart");
            const price = parseFloat(priceElem.innerText.replace(/₫|,/g, ""));
            const quantity = parseInt(quantityElem.innerText);
            totalPrice += price * quantity;
        });

        document.getElementById("totalPriceCartItem").innerText = totalPrice.toLocaleString("vi-VN") + " ₫";

        const voucherTypeElem = document.getElementById("voucher-type");
        const priceReducedElem = document.getElementById("price-reduced-voucher");
        let discountType = 1;
        let discountValue = 0;

        if (voucherTypeElem && priceReducedElem) {
            discountType = parseInt(voucherTypeElem.innerText.trim());
            discountValue = parseFloat(priceReducedElem.innerText.trim().replace(/₫|,/g, ""));
        }

        let priceReduced = 0;
        if (discountType === 1) priceReduced = totalPrice * (discountValue / 100);
        else if (discountType === 2) priceReduced = Math.min(discountValue, totalPrice);

        const finalPrice = totalPrice - priceReduced;
        document.getElementById("priceVoucherReduced").innerText = priceReduced.toLocaleString("vi-VN") + " ₫";
        document.getElementById("price-Calculator").innerText = finalPrice.toLocaleString("vi-VN") + " ₫";
        document.getElementById("cart-spanTotalPriceCart").innerText = finalPrice.toLocaleString("vi-VN") + " ₫";
    }

    // Thêm sự kiện cho các nút tăng/giảm số lượng
    document.querySelectorAll(".cart-qtyminus").forEach(button => {
        button.addEventListener("click", function () {
            updateQuantity(this, -1);
        });
    });

    document.querySelectorAll(".cart-qtyplus").forEach(button => {
        button.addEventListener("click", function () {
            updateQuantity(this, 1);
        });
    });
});

