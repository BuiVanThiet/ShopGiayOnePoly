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
codeProduct.addEventListener('input', async function () {
    await validate();
});
nameProduct.addEventListener('input', async function () {
    await validate();
});

// Lắng nghe sự kiện thay đổi cho tất cả checkbox với class '.category-checkbox'
document.querySelectorAll(".category-checkbox").forEach(checkbox => {
    checkbox.addEventListener('input', async function () {
        await validate();  // Gọi hàm validate mỗi khi người dùng thay đổi trạng thái checkbox
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
        errorTextCategory.style.visibility = 'hidden';
    } else {
        errorTextCategory.style.visibility = 'visible';
        check = false;
    }

    // Kiểm tra Mã sản phẩm
    if (codeProduct.value.length > 10) {
        errorTextCodeProduct.style.visibility = 'visible';
        errorTextCodeProduct.innerText = '* Mã sản phẩm <= 10 kí tự';
        check = false;
    } else if (arrayCodeProduct.includes(codeProduct.value.trim())) {
        errorTextCodeProduct.style.visibility = 'visible';
        errorTextCodeProduct.innerText = '* Mã sản phẩm đã tồn tại';
        check = false;
    } else if (codeProduct.value.length > 0) {
        errorTextCodeProduct.style.visibility = 'hidden';
    } else {
        errorTextCodeProduct.style.visibility = 'visible';
        errorTextCodeProduct.innerText = '* Mã sản phẩm không được để trống';
        check = false;
    }

    // Kiểm tra tên sản phẩm
    if (nameProduct.value.length > 255) {
        errorTextNameProduct.style.visibility = 'visible';
        errorTextNameProduct.innerText = '* Tên sản phẩm <= 255 kí tự';
        check = false;
    } else if (nameProduct.value.length > 0) {
        errorTextNameProduct.style.visibility = 'hidden';
    } else {
        errorTextNameProduct.style.visibility = 'visible';
        errorTextNameProduct.innerText = '* Tên sản phẩm không được để trống';
        check = false;
    }

    // Kiểm tra ảnh
    if (fileInputCreateProduct.files.length > 10) {
        errorTextImage.style.visibility = 'visible';
        errorTextImage.innerText = '* Tối đa 10 ảnh';
        check = false;
    } else if (fileInputCreateProduct.files.length > 0) {
        errorTextImage.style.visibility = 'hidden';
    } else {
        errorTextImage.style.visibility = 'visible';
        check = false;
    }


    if (material.getAttribute('data-material-id')) {
        errorTextMaterial.style.visibility = 'hidden';
        if (type === 'material') {
            material.placeholder = 'Đang chọn ' + material.value.trim();
        }
    } else {
        errorTextMaterial.style.visibility = 'visible';
        check = false;
    }

    if (manufacturer.getAttribute('data-manufacturer-id')) {
        errorTextManufacturer.style.visibility = 'hidden';
        if (type === 'manufacturer') {
            manufacturer.placeholder = 'Đang chọn ' + manufacturer.value.trim();
        }
    } else {
        errorTextManufacturer.style.visibility = 'visible';
        check = false;
    }

    if (origin.getAttribute('data-origin-id')) {
        errorTextOrigin.style.visibility = 'hidden';
        if (type === 'origin') {
            origin.placeholder = 'Đang chọn ' + origin.value.trim();
        }
    } else {
        errorTextOrigin.style.visibility = 'visible';
        check = false;
    }

    if (sole.getAttribute('data-sole-id')) {
        errorTextSole.style.visibility = 'hidden';
        if (type === 'sole') {
            sole.placeholder = 'Đang chọn ' + sole.value.trim();
        }
    } else {
        errorTextSole.style.visibility = 'visible';
        check = false;
    }

    // Kiểm tra nếu tất cả đều hợp lệ, thì hiển thị nút thêm sản phẩm
    if (check) {
        buttonAdd.style.visibility = 'visible';
        validateAndFormatCells();
    } else {
        buttonAdd.style.visibility = 'hidden';
    }

}

function formatNumber(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.');
}

function validateAndFormatCells() {
    const errorText = document.getElementById('errorTextProductDetail');
    const numericColumns = {
        3: { name: "Giá bán", min: 1000, max: Infinity }, // Giá bán: >= 1000
        4: { name: "Giá nhập", min: 1000, max: Infinity }, // Giá nhập: >= 1000
        5: { name: "Số lượng", min: 1, max: Infinity },    // Số lượng: > 0
        6: { name: "Trọng lượng", min: 1, max: 5000 }      // Trọng lượng: <= 5000
    };

    let hasError = false; // Biến đánh dấu nếu có lỗi

    document.querySelectorAll('#productDetailTable tbody tr').forEach(row => {
        Object.keys(numericColumns).forEach(colIndex => {
            const cell = row.cells[colIndex];
            const rawValue = cell.textContent.trim(); // Giá trị chưa xử lý
            const value = rawValue.replace(/\./g, ''); // Loại bỏ dấu chấm để kiểm tra
            const columnRules = numericColumns[colIndex];

            // Lưu trạng thái con trỏ trước khi sửa đổi
            const selection = window.getSelection();
            const range = selection.rangeCount > 0 ? selection.getRangeAt(0) : null;
            const cursorOffset = range ? range.startOffset : null;

            // Kiểm tra điều kiện hợp lệ
            if (isNaN(value) || value === "" || Number(value) < columnRules.min || Number(value) > columnRules.max) {
                // Không hợp lệ
                cell.style.backgroundColor = '#d3d3d3';
                hasError = true;
            } else {
                // Hợp lệ
                cell.style.backgroundColor = '';
                if (colIndex !== '5' && colIndex !== '6') { // Không định dạng cột "Số lượng" và "Trọng lượng"
                    const formattedValue = formatNumber(Number(value));
                    if (rawValue !== formattedValue) {
                        // Cập nhật chỉ khi cần định dạng
                        cell.textContent = formattedValue;
                    }
                }
            }

            // Khôi phục vị trí con trỏ
            if (cursorOffset !== null && cell.contains(selection.anchorNode)) {
                const newRange = document.createRange();
                const textNode = cell.childNodes[0];
                const newCursorPosition = Math.min(cursorOffset, textNode.length); // Đảm bảo vị trí không vượt quá độ dài nội dung
                newRange.setStart(textNode, newCursorPosition);
                newRange.collapse(true);
                selection.removeAllRanges();
                selection.addRange(newRange);
            }
        });
    });

    // Hiển thị hoặc ẩn thông báo lỗi
    if (hasError) {
        buttonAdd.style.visibility = 'hidden';
        errorText.style.visibility = 'visible';
        errorText.innerText = "Giá bán, Giá nhập > 1.000. Trọng lượng <= 5.000";
    } else {
        buttonAdd.style.visibility = 'visible';
        errorText.style.visibility = 'hidden';
    }
}

function addValidationListeners() {
    const numericColumns = [3, 4, 5, 6]; // Các cột cần kiểm tra

    document.querySelectorAll('#productDetailTable tbody tr').forEach(row => {
        numericColumns.forEach(colIndex => {
            const cell = row.cells[colIndex];
            if (cell && cell.contentEditable === "true") {
                cell.addEventListener('input', validateAndFormatCells);
            }
        });
    });
}
