document.addEventListener("DOMContentLoaded", function() {
    const applyDiscountBtn = document.getElementById("applyDiscountBtn");

    // Thêm sự kiện cho các nút chọn trong bảng sale
    const selectButtons = document.querySelectorAll('.select-sale-btn');
    selectButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Xóa lớp 'selected-sale' khỏi tất cả các nút
            selectButtons.forEach(btn => btn.classList.remove('selected-sale'));
            // Thêm lớp 'selected-sale' cho nút được chọn
            this.classList.add('selected-sale');
        });
    });

    // Lấy giá trị giảm giá từ nút đã chọn
    function getSelectedDiscountValue() {
        const selectedSale = document.querySelector('.selected-sale');
        return selectedSale ? parseFloat(selectedSale.dataset.discountValue) : null;
    }

    // Lấy loại giảm giá từ nút đã chọn
    function getSelectedDiscountType() {
        const selectedSale = document.querySelector('.selected-sale');
        return selectedSale ? parseInt(selectedSale.dataset.discountType) : null;
    }

    // Hàm áp dụng giảm giá
    function apply_discount() {
        const selectedProductIds = Array.from(document.querySelectorAll('input[name="selectedProducts"]:checked'))
            .map(input => input.value);

        const discountValue = getSelectedDiscountValue();
        const discountType = getSelectedDiscountType();

        // Lấy saleProductId từ nút đã chọn
        const selectedSale = document.querySelector('.selected-sale');
        const saleProductId = selectedSale ? parseInt(selectedSale.dataset.saleId) : null;

        if (selectedProductIds.length === 0) {
            alert("Vui lòng chọn ít nhất một sản phẩm.");
            return;
        }

        if (discountValue === null || isNaN(discountValue) || discountValue <= 0) {
            alert("Vui lòng chọn giá trị giảm hợp lệ.");
            return;
        }

        if (discountType === null || isNaN(discountType)) {
            alert("Vui lòng chọn loại giảm hợp lệ.");
            return;
        }

        // Kiểm tra saleProductId
        if (saleProductId === null) {
            alert("Vui lòng chọn một đợt giảm giá.");
            return;
        }

        // Gửi dữ liệu qua AJAX đến server
        $.ajax({
            type: "POST",
            url: "/sale-product/apply-discount",
            contentType: "application/json", // Đặt Content-Type thành JSON
            data: JSON.stringify({
                productIds: selectedProductIds,
                discountValue: discountValue,
                discountType: discountType,
                saleProductId: saleProductId // Gửi các giá trị trong chuỗi JSON
            }),
            success: function(response) {alert("Giảm giá đã được áp dụng thành công!");
            },
            error: function(xhr, status, error) {
                console.error("Lỗi khi áp dụng giảm giá:", xhr.responseText || error);
            }
        });
    }

    applyDiscountBtn.addEventListener('click', apply_discount);

});