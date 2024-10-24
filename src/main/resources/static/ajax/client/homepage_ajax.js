function showAllProductsHighest() {
    const hiddenProducts = document.querySelectorAll('.hidden-product');
    const button = document.getElementById('view-all-btn');
    const buttonText = document.getElementById('view-all-text');
    const icon = document.getElementById('view-all-icon');

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
    const products = document.querySelectorAll('.col-3.position-relative');
    const button = document.getElementById('view-all-btn-2');
    const hiddenProducts = document.querySelectorAll('.col-3.position-relative.hidden-product');

    if (hiddenProducts.length > 0) {
        // Hiển thị tất cả sản phẩm nếu có sản phẩm bị ẩn
        products.forEach(product => {
            product.classList.remove('hidden-product');
        });
        // Đổi text thành "Thu Gọn" và đổi icon sang mũi tên lên
        button.innerHTML = 'Thu Gọn <i id="view-all-icon-2" class="fas fa-chevron-up"></i>';
    } else {
        // Ẩn lại tất cả sản phẩm ngoài 4 sản phẩm đầu
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
    const products = document.querySelectorAll('.col-3.position-relative');
    const buttonText = document.getElementById('view-all-text-2');
    const hiddenProducts = document.querySelectorAll('.col-3.position-relative.hidden-product');

    if (hiddenProducts.length > 0) {
        // Hiển thị tất cả sản phẩm nếu có sản phẩm bị ẩn
        products.forEach(product => {
            product.classList.remove('hidden-product');
        });
        // Đổi text thành "Thu Gọn" và đổi icon sang mũi tên trái
        buttonText.innerHTML = 'Thu Gọn <i id="toggle-icon-2" class="fas fa-chevron-left"></i>';
    } else {
        // Ẩn lại tất cả sản phẩm ngoài 4 sản phẩm đầu
        products.forEach((product, index) => {
            if (index >= 4) {
                product.classList.add('hidden-product');
            } else {
                product.classList.remove('hidden-product');
            }
        });
        // Đổi lại text thành "Xem Tất Cả" và đổi icon sang mũi tên phải
        buttonText.innerHTML = 'Xem Tất Cả <i id="toggle-icon-2" class="fas fa-chevron-right"></i>';
    }
}


