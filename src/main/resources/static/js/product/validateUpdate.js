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
let fileInputCreateProduct = document.getElementById('file-input-updateProduct');
let buttonUpdate = document.getElementById('update-btn-updateProduct');
let arrayCodeProduct = [];
let initialCodeProduct = '';

window.addEventListener('load', () => {
    initialCodeProduct = codeProduct.value.trim();
});
// Fetch all product codes initially
async function fetchProductCodes() {
    const response = await fetch('/product-api/findAllCodeProduct');
    if (response.ok) {
        arrayCodeProduct = await response.json();
    }
}

fetchProductCodes(); // Fetch product codes when the page loads

// Lắng nghe sự kiện thay đổi input và checkbox
codeProduct.addEventListener('input', validate);
nameProduct.addEventListener('input', validate);

document.querySelectorAll(".category-checkbox").forEach(checkbox => {
    checkbox.addEventListener('input', validate);
});

// Hàm kiểm tra trạng thái hợp lệ của form
async function validate(type) {
    let check = true;

    if (arrayCodeProduct.length === 0) {
        await fetchProductCodes();
    }

    // Kiểm tra checkbox danh mục
    const categoryCheckboxes = document.querySelectorAll(".category-checkbox");
    const isChecked = Array.from(categoryCheckboxes).some(checkbox => checkbox.checked);

    if (isChecked) {
        errorTextCategory.style.display = 'none';
    } else {
        errorTextCategory.style.display = 'block';
        check = false;
    }

    const trimmedCodeProduct = codeProduct.value.trim();

    // Kiểm tra Mã sản phẩm
    if (trimmedCodeProduct === initialCodeProduct) {
        hideError(errorTextCodeProduct); // Nếu mã chưa thay đổi, không kiểm tra
    } else if (trimmedCodeProduct.length === 0) {
        showError(errorTextCodeProduct, '* Mã sản phẩm không được để trống');
        check = false;
    } else if (trimmedCodeProduct.length > 10) {
        showError(errorTextCodeProduct, '* Mã sản phẩm <= 10 kí tự');
        check = false;
    } else if (arrayCodeProduct.includes(trimmedCodeProduct)) {
        showError(errorTextCodeProduct, '* Mã sản phẩm đã tồn tại');
        check = false;
    } else {
        hideError(errorTextCodeProduct);
    }

    // Kiểm tra Tên sản phẩm
    const trimmedNameProduct = nameProduct.value.trim();
    if (trimmedNameProduct.length === 0) {
        showError(errorTextNameProduct, '* Tên sản phẩm không được để trống');
        check = false;
    } else if (trimmedNameProduct.length > 255) {
        showError(errorTextNameProduct, '* Tên sản phẩm <= 255 kí tự');
        check = false;
    } else {
        hideError(errorTextNameProduct);
    }

    const imagePreviewDiv = document.getElementById('image-preview-updateProduct');

    // Kiểm tra nếu trong div không có thẻ <img>
    if (imagePreviewDiv && imagePreviewDiv.querySelectorAll('img').length === 0) {
        if (fileInputCreateProduct.files.length > 10) {
            showError(errorTextImage, '* Tối đa 10 ảnh');
            check = false;
        } else if (fileInputCreateProduct.files.length > 0) {
            hideError(errorTextImage);
        } else {
            showError(errorTextImage, '* Vui lòng chọn ít nhất 1 ảnh');
            check = false;
        }
    }



    // Kiểm tra các thuộc tính khác
    check = validateField(material, errorTextMaterial, 'material', type) && check;
    check = validateField(manufacturer, errorTextManufacturer, 'manufacturer', type) && check;
    check = validateField(origin, errorTextOrigin, 'origin', type) && check;
    check = validateField(sole, errorTextSole, 'sole', type) && check;

    // Hiển thị nút thêm sản phẩm nếu hợp lệ
    if (check){
        buttonUpdate.style.display = 'block';
    } else {
        buttonUpdate.style.display = 'none';
    }
}

// Hàm kiểm tra từng trường input
function validateField(field, errorText, type, currentType) {
    const fieldId = field.getAttribute(`data-${type}-id`);
    if (fieldId) {
        hideError(errorText);
        if (currentType === type) {
            field.placeholder = `Đang chọn ${field.value.trim()}`;
        }
        return true;
    } else {
        showError(errorText, `* Vui lòng chọn ${type}`);
        return false;
    }
}

// Hàm hiển thị lỗi
function showError(element, message) {
    element.style.display = 'block';
    element.innerText = message;
}

// Hàm ẩn lỗi
function hideError(element) {
    element.style.display = 'none';
}
