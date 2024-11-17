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

function removeProductDetailFromCart(btn) {
    const cartItem = btn.closest(".cart-item");
    const productId = btn.getAttribute('field');
    // Sử dụng productId trong xử lý của bạn
    console.log("Sản phẩm có id:", productId);
    // const productDetailId = document.getElementById("idProductDetailFromCart").innerText;
    // console.log("ID productDetail delete text: " + productDetailId)
    //
    // let productDetailIdINT = parseInt(productDetailId.value);
    //
    $.ajax({
        url: '/api-client/remove-from-cart/' + productId,  // Gửi ID sản phẩm lên server
        type: 'POST',
        success: function (response) {
            console.log(response);
            cartItem.remove(); // Xóa sản phẩm khỏi giỏ hàng
        },
        error: function (xhr, status, error) {
            alert('Có lỗi khi gửi yêu cầu');
        }
    });
}

document.addEventListener("DOMContentLoaded", function () {
    function updateQuantity(button, change) {
        const cartItem = button.closest(".cart-item");
        const quantityElem = cartItem.querySelector("#quantityProductFormCart");
        const productDetailIdElem = cartItem.querySelector("#idProductDetailFromCart");
        let quantityItem = parseInt(quantityElem.innerText);
        const productDetailId = productDetailIdElem.value;

        if (isNaN(quantityItem)) {
            alert("Số lượng không hợp lệ.");
            return;
        }

        quantityItem += change;
        if (quantityItem < 1) quantityItem = 1;
        else if (quantityItem > 10) {
            alert("Chỉ được mua tối đa 10 sản phẩm cùng loại.");
            quantityItem = 10;
        }

        const productId = button.getAttribute('field');

        quantityElem.innerText = quantityItem;
        updateQuantityInServer(productId, quantityItem);
    }


    function updateQuantityInServer(productDetailId, quantityItem) {
        console.log('vao phuong thuc cong tru ' + productDetailId)
        console.log('so luong mua ' + quantityItem)

        $.ajax({
            type: "POST",
            url: "/api-client/update-from-cart/"+productDetailId,
            contentType: "application/json",
            data: JSON.stringify({quantityItem: quantityItem}),
            success:function (response) {
                console.log('done')
                updateCartTotal();
            },
            error:function (xhr) {
                console.log('loi' + xhr.responseText)
            }
        })
        // $.ajax({
        //     url: `/api-client/update-from-cart/${productDetailId}`,
        //     type: "POST",
        //     contentType: "application/json",
        //     data: JSON.stringify({quantityItem: quantityItem}),
        //     success: function (response) {
        //         console.log("Cập nhật số lượng thành công:", response);
        //         if (response.messages) {
        //             console.log(response.messages);
        //         }
        //         updateCartTotal();
        //     },
        //     error: function (xhr) {
        //         console.error("Lỗi khi cập nhật giỏ hàng:", xhr.responseText);
        //         alert("Có lỗi xảy ra khi cập nhật giỏ hàng. Vui lòng thử lại.");
        //     },
        // });
    }

    function updateCartTotal() {
        let totalPrice = 0;

        document.querySelectorAll(".cart-item").forEach(item => {
            const priceElem = item.querySelector("#cart-spanPriceCartItem");
            const quantityElem = item.querySelector("#quantityProductFormCart");

            const price = parseFloat(priceElem.innerText.replace(/₫|,/g, "")) || 0;
            const quantity = parseInt(quantityElem.innerText) || 0;

            totalPrice += price * quantity;
        });

        document.getElementById("totalPriceCartItem").innerText = totalPrice.toLocaleString("vi-VN") + " ₫";

        const voucherTypeElem = document.getElementById("voucher-type");
        const priceReducedElem = document.getElementById("priceVoucherReduced");
        let discountType = 1;
        let discountValue = 0;

        if (voucherTypeElem && priceReducedElem) {
            discountType = parseInt(voucherTypeElem.innerText.trim()) || 1;
            discountValue = parseFloat(priceReducedElem.innerText.trim().replace(/₫|,/g, "")) || 0;
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


