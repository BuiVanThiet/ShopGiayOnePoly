document.addEventListener("DOMContentLoaded", function() {
    const applyDiscountBtn = document.getElementById("applyDiscountBtn");
    applyDiscountBtn.addEventListener("click", apply_discount);

    // Thêm sự kiện cho các nút chọn trong bảng sale
    const selectButtons = document.querySelectorAll('.select-sale-btn');
    selectButtons.forEach(button => {
        button.addEventListener('click', function() {
            // Xóa lớp 'selected-sale' khỏi tất cả các nút
            selectButtons.forEach(btn => btn.classList.remove('selected-sale'));
            // Thêm lớp 'selected-sale' cho nút được chọn
            this.classList.add('selected-sale');
            // Kiểm tra giá trị của các thuộc tính data-discount-value và data-discount-type
            console.log("Giá trị giảm:", this.dataset.discountValue);
            console.log("Loại giảm:", this.dataset.discountType);
            alert(this.dataset.discountType)
        });
    });
});

// Lấy giá trị giảm giá từ nút đã chọn
function getSelectedDiscountValue() {
    const selectedSale = document.querySelector('.selected-sale'); // Nút đã chọn
    const discountValue = selectedSale ? parseFloat(selectedSale.dataset.discountValue) : null;
    console.log("Giá trị giảm:", discountValue); // Kiểm tra giá trị trong console
    return discountValue; // Trả về giá trị
}

// Lấy loại giảm giá từ nút đã chọn
function getSelectedDiscountType() {
    const selectedSale = document.querySelector('.selected-sale'); // Nút đã chọn
    const discountType = selectedSale ? parseInt(selectedSale.dataset.discountType) : null;
    console.log("Loại giảm:", discountType); // Kiểm tra giá trị trong console
    return discountType; // Trả về loại
}

// Hàm áp dụng giảm giá
function apply_discount() {
    const selectedProductIds = Array.from(document.querySelectorAll('input[name="selectedProducts"]:checked'))
        .map(input => input.value); // Lấy danh sách ID của các sản phẩm đã chọn

    const discountValue = getSelectedDiscountValue(); // Lấy giá trị giảm từ nút đã chọn
    const discountType = getSelectedDiscountType(); // Lấy loại giảm giá từ nút đã chọn

    // Kiểm tra các tham số trước khi gửi request
    if (selectedProductIds.length === 0) {
        alert("Vui lòng chọn ít nhất một sản phẩm.");
        return;
    }

    if (discountValue === null || isNaN(discountValue) || discountValue <= 0) {
        alert("Vui lòng nhập giá trị giảm hợp lệ.");
        return;
    }

    if (discountType === null || isNaN(discountType)) {
        alert("Vui lòng chọn loại giảm hợp lệ.");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/sale-product/apply-discount", // Đường dẫn API cần đúng
        contentType: "application/json", // Đảm bảo định dạng JSON
        data: JSON.stringify({
            discountValue: discountValue, // Giá trị giảm
            discountType: discountType,   // Loại giảm
            productIds: selectedProductIds // Gửi danh sách productIds dưới dạng mảng
        }),
        success: function(response) {
            alert("Giảm giá đã được áp dụng thành công!");
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi áp dụng giảm giá:", error);
            alert("Đã xảy ra lỗi trong quá trình áp dụng giảm giá. Vui lòng thử lại.");
        }
    });
}
