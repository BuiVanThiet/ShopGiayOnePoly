function showAllProductsHighest() {
    const hiddenProducts = document.querySelectorAll('.hidden-product');
    const button = document.getElementById('view-all-btn');
    const buttonText = document.getElementById('view-all-text');

    if (hiddenProducts.length > 0) {
        // Hiển thị tất cả sản phẩm
        hiddenProducts.forEach(product => {
            product.classList.remove('hidden-product');
        });
        // Đổi text thành "Thu Gọn" và đổi icon sang mũi tên lên
        button.innerHTML = 'Thu Gọn <i id="view-all-icon" class="fa fa-chevron-up"></i>';
        buttonText.innerHTML = 'Thu Gọn <i id="view-all-icon" class="fa fa-chevron-up"></i>';
    } else {
        // Ẩn lại tất cả sản phẩm ngoài 4 sản phẩm đầu
        const products = document.querySelectorAll('.col-3.position-relative');
        products.forEach((product, index) => {
            if (index >= 4) {
                product.classList.add('hidden-product');
            }
        });
        // Đổi lại text thành "Xem Tất Cả" và đổi icon sang mũi tên xuống
        button.innerHTML = 'Xem Tất Cả <i id="view-all-icon" class="fa fa-chevron-down"></i>';
        buttonText.innerHTML = 'Xem Tất Cả <i id="view-all-icon" class="fa fa-chevron-down"></i>';
    }
}

function showAllProductsByTextHighest() {
    const hiddenProducts = document.querySelectorAll('.hidden-product');
    const button = document.getElementById('view-all-btn');
    const buttonText = document.getElementById('view-all-text');
    const icon = document.getElementById('view-all-icon');

    if (hiddenProducts.length > 0) {
        // Hiển thị tất cả sản phẩm
        hiddenProducts.forEach(product => {
            product.classList.remove('hidden-product');
        });
        // Đổi text thành "Thu Gọn" và đổi icon sang mũi tên trái
        button.innerHTML = 'Thu Gọn <i id="view-all-icon" class="fa fa-chevron-left"></i>';
        buttonText.innerHTML = 'Thu Gọn <i id="view-all-icon" class="fa fa-chevron-left"></i>';
    } else {
        // Ẩn lại tất cả sản phẩm ngoài 4 sản phẩm đầu
        const products = document.querySelectorAll('.col-3.position-relative');
        products.forEach((product, index) => {
            if (index >= 4) {
                product.classList.add('hidden-product');
            }
        });
        // Đổi lại text thành "Xem Tất Cả" và đổi icon sang mũi tên phải
        button.innerHTML = 'Xem Tất Cả <i id="view-all-icon" class="fa fa-chevron-right"></i>';
        buttonText.innerHTML = 'Xem Tất Cả <i id="view-all-icon" class="fa fa-chevron-right"></i>';
    }
}
// Lowest
function showAllProductsLowest() {
    const hiddenProducts = document.querySelectorAll('#product-container-2 .hidden-product');
    const button = document.getElementById('view-all-btn-2');
    const products = document.querySelectorAll('#product-container-2 .col-3.position-relative');

    if (hiddenProducts.length > 0) {
        // Hiển thị tất cả sản phẩm
        hiddenProducts.forEach(product => {
            product.classList.remove('hidden-product');
        });
        // Đổi text thành "Thu Gọn" và đổi icon sang mũi tên lên
        button.innerHTML = 'Thu Gọn <i id="view-all-icon-2" class="fas fa-chevron-up"></i>';
    } else {
        // Ẩn các sản phẩm ngoài 4 sản phẩm đầu
        products.forEach((product, index) => {
            if (index >= 4) {
                product.classList.add('hidden-product');
            } else {
                product.classList.remove('hidden-product');
            }
        });
        // Đổi lại text thành "Xem Tất Cả" và đổi icon sang mũi tên phải
        button.innerHTML = 'Xem Tất Cả <i id="view-all-icon-2" class="fas fa-chevron-right"></i>';
    }
}
function showAllProductsByTextLowest() {
    const products = document.querySelectorAll('#product-container-2 .col-3.position-relative');
    const buttonText = document.getElementById('view-all-text-2');
    const hiddenProducts = document.querySelectorAll('#product-container-2 .hidden-product');

    if (hiddenProducts.length > 0) {
        // Hiển thị tất cả sản phẩm
        products.forEach(product => product.classList.remove('hidden-product'));
        // Đổi text thành "Thu Gọn" và icon mũi tên trái
        buttonText.innerHTML = 'Thu Gọn <i class="fas fa-chevron-left"></i>';
    } else {
        // Ẩn lại các sản phẩm ngoài 4 sản phẩm đầu
        products.forEach((product, index) => {
            if (index >= 4) {
                product.classList.add('hidden-product');
            } else {
                product.classList.remove('hidden-product');
            }
        });
        // Đổi lại text thành "Xem Tất Cả" và icon mũi tên phải
        buttonText.innerHTML = 'Xem Tất Cả <i class="fas fa-chevron-right"></i>';
    }
}
document.querySelectorAll('.price-min').forEach(el => {
    // Lấy giá trị từ thuộc tính data-price
    const price = parseFloat(el.getAttribute('data-price'));
    if (!isNaN(price)) {
        // Định dạng giá theo VNĐ
        el.textContent = Math.floor(price).toLocaleString('vi-VN') + " VNĐ";
    }
});

document.querySelectorAll('.price-min').forEach(el => {
    // Lấy giá trị từ thuộc tính data-price
    const price = parseFloat(el.getAttribute('data-price'));
    if (!isNaN(price)) {
        // Định dạng giá theo VNĐ
        el.textContent = Math.floor(price).toLocaleString('vi-VN') + " VNĐ";
    }
});




