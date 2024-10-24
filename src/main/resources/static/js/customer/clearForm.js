function clearForm() {
    // Reset toàn bộ form
    document.getElementById("customerForm").reset();

    // Reset hình ảnh (input file) và hình ảnh xem trước
    document.getElementById("file-upload").value = ""; // Xóa file đã chọn
    document.getElementById("image-preview").src = "path_to_default_image.png"; // Đặt lại hình ảnh mặc định
}

