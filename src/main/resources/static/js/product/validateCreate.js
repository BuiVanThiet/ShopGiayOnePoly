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
var buttonAdd = document.getElementById('create-btn-createProduct');

// Bắt sự kiện khi người dùng nhập vào ô tên sản phẩm
codeProduct.addEventListener('input', function () {
    validate();});
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
function validate() {
    let check = true;

    // Kiểm tra trạng thái checkbox
    const categoryCheckboxes = document.querySelectorAll(".category-checkbox");
    const isChecked = Array.from(categoryCheckboxes).some(checkbox => checkbox.checked);  // Kiểm tra ít nhất một checkbox được chọn
    const errorTextCategory = document.getElementById("errorText-category"); // Tham chiếu đến phần tử thông báo lỗi
    errorTextCategory.style.display = isChecked ? "none" : "block";  // Ẩn hoặc hiển thị thông báo lỗi

    // Kiểm tra các input khác
    const codeProduct = document.getElementById("codeProduct").value.trim();
    const errorTextCodeProduct = document.getElementById("errorText-codeProduct");
    const nameProduct = document.getElementById("nameProduct").value.trim();
    const errorTextNameProduct = document.getElementById("errorText-nameProduct");

    // Kiểm tra Mã sản phẩm
    if (codeProduct.value.length > 10) {
        errorTextCodeProduct.style.display = 'block';
        errorTextCodeProduct.innerText = '* Mã sản phẩm <= 10 kí tự';
        check = false;
    } else if (codeProduct.value.length > 0) {
        errorTextCodeProduct.style.display = 'none';
    } else {
        errorTextCodeProduct.style.display = 'block';
        errorTextCodeProduct.innerText = '* Mã sản phẩm không được để trống';
        check = false;
    }

    // Kiểm tra tên sản phẩm
    if (nameProduct.value.length > 0) {
        errorTextNameProduct.style.display = 'none';
    } else {
        errorTextNameProduct.style.display = 'block';
        errorTextNameProduct.innerText = '* Tên sản phẩm không được để trống';
        check = false;
    }

    // Kiểm tra chất liệu, nhà sản xuất, nguồn gốc, đế giày
    var material = document.getElementById("myInput-material");
    var manufacturer = document.getElementById("myInput-manufacturer");
    var origin = document.getElementById("myInput-origin");
    var sole = document.getElementById("myInput-sole");

    var errorTextMaterial = document.getElementById("errorText-material");
    var errorTextManufacturer = document.getElementById("errorText-manufacturer");
    var errorTextOrigin = document.getElementById("errorText-origin");
    var errorTextSole = document.getElementById("errorText-sole");

    if (material.getAttribute('data-material-id')) {
        errorTextMaterial.style.display = 'none';
        material.placeholder = 'Đang chọn ' + material.value.trim();
    } else {
        errorTextMaterial.style.display = 'block';
        check = false;
    }

    if (manufacturer.getAttribute('data-manufacturer-id')) {
        errorTextManufacturer.style.display = 'none';
        manufacturer.placeholder = 'Đang chọn ' + manufacturer.value.trim();
    } else {
        errorTextManufacturer.style.display = 'block';
        check = false;
    }

    if (origin.getAttribute('data-origin-id')) {
        errorTextOrigin.style.display = 'none';
        origin.placeholder = 'Đang chọn ' + origin.value.trim();
    } else {
        errorTextOrigin.style.display = 'block';
        check = false;
    }

    if (sole.getAttribute('data-sole-id')) {
        errorTextSole.style.display = 'none';
        sole.placeholder = 'Đang chọn ' + sole.value.trim();
    } else {
        errorTextSole.style.display = 'block';
        check = false;
    }

    // Kiểm tra nếu tất cả đều hợp lệ, thì hiển thị nút thêm sản phẩm
    var buttonAdd = document.getElementById("create-btn-createProduct");
    if (check === true) {
        buttonAdd.style.display = 'block';
    } else {
        buttonAdd.style.display = 'none';
    }
}
