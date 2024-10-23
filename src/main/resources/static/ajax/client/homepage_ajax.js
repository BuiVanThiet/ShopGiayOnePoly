function showAllProducts() {
    const hiddenProducts = document.querySelectorAll('.hidden-product');
    const button = document.getElementById('view-all-btn');

    if (hiddenProducts.length > 0) {
        // Hiển thị tất cả sản phẩm
        hiddenProducts.forEach(product => {
            product.classList.remove('hidden-product');
        });
        // Đổi nút thành "Thu gọn"
        button.textContent = 'Thu gọn';
    } else {
        // Ẩn lại tất cả sản phẩm ngoài 4 sản phẩm đầu
        const products = document.querySelectorAll('.col-3.position-relative');
        products.forEach((product, index) => {
            if (index >= 4) {
                product.classList.add('hidden-product');
            }
        });
        // Đổi nút lại thành "Xem tất cả"
        button.textContent = 'Xem tất cả';
    }
}
