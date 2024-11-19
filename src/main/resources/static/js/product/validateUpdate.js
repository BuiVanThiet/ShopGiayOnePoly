var codeProduct = document.getElementById('codeProduct');
var errorTextCodeProduct = document.getElementById('errorText-codeProduct');
var nameProduct = document.getElementById('myInput-nameProduct');
var errorTextNameProduct = document.getElementById('errorText-nameProduct');
var material = document.getElementById('myInput-material');
var errorTextMaterial = document.getElementById('errorText-material');
var manufacturer = document.getElementById('myInput-manufacturer');
var errorTextManufacturer = document.getElementById('errorText-manufacturer');
var origin = document.getElementById('myInput-origin');
var errorTextOrigin = document.getElementById('errorText-origin');
var sole = document.getElementById('myInput-sole');
var errorTextSole = document.getElementById('errorText-sole');
var errorTextCategory = document.getElementById('errorText-category');
var buttonAdd = document.getElementById('update-btn-updateProduct');
var arrayCodeProduct = [];
var initialCodeProduct = '';

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

    // Ensure that the product codes are fetched before validation
    if (arrayCodeProduct.length === 0) {
        await fetchProductCodes(); // Fetch the codes if not already done
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
        showError(errorTextCodeProduct, '* Mã sản phẩm đã tồn tại trong cơ sở dữ liệu');
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

    // Kiểm tra các thuộc tính khác
    check = validateField(material, errorTextMaterial, 'material', type) && check;
    check = validateField(manufacturer, errorTextManufacturer, 'manufacturer', type) && check;
    check = validateField(origin, errorTextOrigin, 'origin', type) && check;
    check = validateField(sole, errorTextSole, 'sole', type) && check;

    // Hiển thị nút thêm sản phẩm nếu hợp lệ
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
