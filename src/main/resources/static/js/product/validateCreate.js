var codeProduct = document.getElementById('codeProduct');

var addResDetailInput = document.getElementById('addResDetail');
var provinceSelect = document.getElementById("provinceSelect-add-customer");
var districtSelect = document.getElementById("districtSelect-add-customer");
var wardSelect = document.getElementById("wardSelect-add-customer");
var nameProduct = document.getElementById('myInput-nameProduct');
var errorTextCodeProduct = document.getElementById('errorText-codeProduct');

// Bắt sự kiện khi người dùng nhập vào ô tên sản phẩm
codeProduct.addEventListener('input', function () {
    // Kiểm tra nếu người dùng để trống hoặc chỉ nhập khoảng trắng
    if (!codeProduct.value.trim()) {
        errorTextNameProduct.style.display = "block";  // Hiển thị thông báo lỗi
    } else {
        errorTextNameProduct.style.display = "none";  // Ẩn thông báo lỗi
    }
});


function validateCategory() {
    // Lấy danh sách các checkbox trong danh mục
    const categoryCheckboxes = document.querySelectorAll("input[name='categories']:checked");
    // Kiểm tra nếu không có checkbox nào được chọn
    if (categoryCheckboxes.length === 0) {
        alert("Vui lòng chọn ít nhất một danh mục");
        return false;
    }
}

function validateMaterial() {
    // Lấy giá trị của select chất liệu
    const materialSelect = document.getElementById("materialSelect");
    const selectedValue = materialSelect.value;

    // Kiểm tra nếu giá trị là 0 (tức là chưa chọn chất liệu)
    if (selectedValue === "0") {
        alert("Vui lòng chọn chất liệu.");
        return false;
    }
    return true;
}
