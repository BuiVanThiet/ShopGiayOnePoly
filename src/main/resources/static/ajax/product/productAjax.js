function deleteByID(element) {
    // Hiển thị modal tùy chỉnh
    const modal = document.getElementById('customConfirmModal');
    modal.style.display = "flex";
    var codeProduct = element.getAttribute('data-code-product');
    document.getElementById('confirmText-product').textContent = 'Bạn có chắc chắn muốn xóa sản phẩm "' + codeProduct + '" không?';

    // Gán hành động khi người dùng nhấn "Đồng ý"
    document.getElementById('confirmYes').onclick = function () {
    // Lấy index từ th:data-index
        var index = element.getAttribute('data-index');
        // Lấy id của phần tử từ th:data-id của hàng
        var id = $('#row-' + index).data('id');
        $.ajax({
            url: '/staff/product/delete',  // Đường dẫn API để xóa
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                id: id,
                status: 0  // Giả sử status 0 là trạng thái bị xóa
            }),
            success: function (response) {
                if (response.success) {
                    // Xóa hàng trong bảng mà không cần reload trang
                    $('#row-' + index).remove();  // Xóa hàng với id là row-index
                    console.log('Xóa thành công');
                } else {
                    alert('Xóa thất bại!');
                }
            },
            error: function (xhr, status, error) {
                console.error('Có lỗi xảy ra khi xóa:', error);
            }
        });

        // Ẩn modal sau khi thực hiện hành động
        modal.style.display = "none";
    };

    // Gán hành động khi người dùng nhấn "Hủy"
    document.getElementById('confirmNo').onclick = function () {
        modal.style.display = "none";  // Đóng modal mà không thực hiện hành động
    };
}
