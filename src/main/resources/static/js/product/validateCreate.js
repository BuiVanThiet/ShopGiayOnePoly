let codeProduct = document.getElementById('codeProduct');
let errorTextCodeProduct = document.getElementById('errorText-codeProduct');
let nameProduct = document.getElementById('myInput-nameProduct');
let errorTextNameProduct = document.getElementById('errorText-nameProduct');
let material = document.getElementById('myInput-material');
let errorTextMaterial = document.getElementById('errorText-material');
let manufacturer = document.getElementById('myInput-manufacturer');
let errorTextManufacturer = document.getElementById('errorText-manufacturer');
let origin = document.getElementById('myInput-origin');
let errorTextOrigin = document.getElementById('errorText-origin');
let sole = document.getElementById('myInput-sole');
let errorTextSole = document.getElementById('errorText-sole');
let errorTextCategory = document.getElementById('errorText-category');
let errorTextImage = document.getElementById('errorText-image');
let fileInputCreateProduct = document.getElementById('file-input-createProduct');
let buttonAdd = document.getElementById('create-btn-createProduct');
let arrayCodeProduct = [];
// Bắt sự kiện khi người dùng nhập vào ô tên sản phẩm
codeProduct.addEventListener('input', function () {
    validate();
});
nameProduct.addEventListener('input', function () {
    validate();
});

// Lắng nghe sự kiện thay đổi cho tất cả checkbox với class '.category-checkbox'
document.querySelectorAll(".category-checkbox").forEach(checkbox => {
    checkbox.addEventListener('input', function () {
        validate();  // Gọi hàm validate mỗi khi người dùng thay đổi trạng thái checkbox
    });
});

// Hàm validate xử lý tất cả các trường hợp, bao gồm checkbox và các input khác
async function validate(type) {
    let check = true;
    const response = await fetch('/product-api/findAllCodeProduct');
    if (response.ok) {
        arrayCodeProduct = await response.json(); // Đảm bảo đây là một mảng
    }
    // Kiểm tra trạng thái checkbox
    let categoryCheckboxes = document.querySelectorAll(".category-checkbox");
    let isChecked = Array.from(categoryCheckboxes).some(checkbox => checkbox.checked);  // Kiểm tra ít nhất một checkbox được chọn
    if (isChecked) {
        errorTextCategory.style.display = 'none';
    } else {
        errorTextCategory.style.display = 'block';
        check = false;
    }

    // Kiểm tra Mã sản phẩm
    if (codeProduct.value.length > 10) {
        errorTextCodeProduct.style.display = 'block';
        errorTextCodeProduct.innerText = '* Mã sản phẩm <= 10 kí tự';
        check = false;
    } else if (arrayCodeProduct.includes(codeProduct.value.trim())) {
        errorTextCodeProduct.style.display = 'block';
        errorTextCodeProduct.innerText = '* Mã sản phẩm đã tồn tại';
        check = false;
    } else if (codeProduct.value.length > 0) {
        errorTextCodeProduct.style.display = 'none';
    } else {
        errorTextCodeProduct.style.display = 'block';
        errorTextCodeProduct.innerText = '* Mã sản phẩm không được để trống';
        check = false;
    }

    // Kiểm tra tên sản phẩm
    if (nameProduct.value.length > 255) {
        errorTextNameProduct.style.display = 'block';
        errorTextNameProduct.innerText = '* Tên sản phẩm <= 255 kí tự';
        check = false;
    } else if (nameProduct.value.length > 0) {
        errorTextNameProduct.style.display = 'none';
    } else {
        errorTextNameProduct.style.display = 'block';
        errorTextNameProduct.innerText = '* Tên sản phẩm không được để trống';
        check = false;
    }

    // Kiểm tra ảnh
    if (fileInputCreateProduct.files.length > 0) {
        errorTextImage.style.display = 'none';
    } else {
        errorTextImage.style.display = 'block';
        check = false;
    }


    if (material.getAttribute('data-material-id')) {
        errorTextMaterial.style.display = 'none';
        if (type === 'material') {
            material.placeholder = 'Đang chọn ' + material.value.trim();
        }
    } else {
        errorTextMaterial.style.display = 'block';
        check = false;
    }

    if (manufacturer.getAttribute('data-manufacturer-id')) {
        errorTextManufacturer.style.display = 'none';
        if (type === 'manufacturer') {
            manufacturer.placeholder = 'Đang chọn ' + manufacturer.value.trim();
        }
    } else {
        errorTextManufacturer.style.display = 'block';
        check = false;
    }

    if (origin.getAttribute('data-origin-id')) {
        errorTextOrigin.style.display = 'none';
        if (type === 'origin') {
            origin.placeholder = 'Đang chọn ' + origin.value.trim();
        }
    } else {
        errorTextOrigin.style.display = 'block';
        check = false;
    }

    if (sole.getAttribute('data-sole-id')) {
        errorTextSole.style.display = 'none';
        if (type === 'sole') {
            sole.placeholder = 'Đang chọn ' + sole.value.trim();
        }
    } else {
        errorTextSole.style.display = 'block';
        check = false;
    }

    // Kiểm tra nếu tất cả đều hợp lệ, thì hiển thị nút thêm sản phẩm
    if (check === true) {
        buttonAdd.style.display = 'block';
    } else {
        buttonAdd.style.display = 'none';
    }
}
