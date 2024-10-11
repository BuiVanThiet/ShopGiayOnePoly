document.getElementById("cancelApplySale").addEventListener('click', function() {
    const selectedProductIds = Array.from(document.querySelectorAll('input[name="selectedProducts"]:checked'))
        .map(input => input.value);

    if (selectedProductIds.length === 0) {
        alert("Vui lòng chọn ít nhất một sản phẩm.");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/sale-product/cancel-discount",
        data: {
            productIds: selectedProductIds
        },
        success: function(response) {
            alert("Giảm giá đã được ngừng và giá gốc đã được khôi phục!");
            location.reload();
        },
        error: function(xhr, status, error) {
            console.error("Lỗi khi ngừng giảm giá:", xhr.responseText || error);
        }
    });
});