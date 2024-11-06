
// Khi trang được tải xong, kiểm tra các giá trị input để hiển thị bảng chi tiết sản phẩm
document.addEventListener("DOMContentLoaded", function () {
    const colorInput = document.getElementById("colorInput-createProduct");
    const sizeInput = document.getElementById("sizeInput-createProduct");

    function checkInputs() {
        // Kiểm tra nếu cả hai trường 'color' và 'size' đều có giá trị thì hiển thị bảng
        if (colorInput.value && sizeInput.value) {
            document.getElementById("table-createProductDetail").style.display = "block";
        }
    }

    colorInput.addEventListener("input", checkInputs);
    sizeInput.addEventListener("input", checkInputs);
});

// Hàm cho combobox chọn màu sắc
document.addEventListener("DOMContentLoaded", function () {
    const comboboxToggle = document.getElementById("comboboxToggle-createProduct");
    const comboboxOptions = document.getElementById("comboboxOptions-createProduct");

    // Hiển thị hoặc ẩn danh sách tùy chọn khi nhấn vào toggle
    comboboxToggle.addEventListener("click", function () {
        const isHidden = comboboxOptions.style.display === "none" || comboboxOptions.style.display === "";
        comboboxOptions.style.display = isHidden ? "block" : "none";
    });

    // Ẩn combobox khi click ra ngoài vùng combobox
    document.addEventListener("click", function (event) {
        if (!comboboxToggle.contains(event.target) && !comboboxOptions.contains(event.target)) {
            comboboxOptions.style.display = "none";
        }
    });

    // Cập nhật giá trị hiển thị trên combobox khi có thay đổi lựa chọn
    comboboxOptions.addEventListener("change", function () {
        const selectedOptions = Array.from(document.querySelectorAll('input[name="color-createProduct"]:checked'))
            .map(option => option.value);
        comboboxToggle.innerText = selectedOptions.length > 0 ? selectedOptions.join(", ") : "Chọn màu sắc";
    });
});

// Hàm preview hình ảnh khi người dùng tải lên
function previewImages(event) {
    const files = event.target.files;
    const previewContainer = document.getElementById('image-preview-createProduct');

    // Xóa các ảnh đã preview trước đó
    previewContainer.innerHTML = '';

    // Duyệt qua từng file để hiển thị preview
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        reader.onload = function (e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            previewContainer.appendChild(img);
        };
        reader.readAsDataURL(file);
    }
}
// Đóng dropdown khi click ngoài
window.onclick = function(event) {
    var dropdowns = document.getElementsByClassName("dropdown-content-createProduct");

    // Kiểm tra xem click không phải trên ô input hoặc bên trong dropdown
    if (!event.target.matches('.form-control-createProduct input')) {
        for (var i = 0; i < dropdowns.length; i++) {
            dropdowns[i].classList.remove('show-createProduct'); // Ẩn tất cả dropdown
        }
    }
}

function showDropdown(event) {
    var input = event.target;
    var listId = input.id.replace("myInput-", "dataList-"); // Tạo ID cho danh sách từ ID của input
    var ul = document.getElementById(listId);

    // Đóng tất cả dropdown khác
    var dropdowns = document.getElementsByClassName("dropdown-content-createProduct");
    for (var i = 0; i < dropdowns.length; i++) {
        dropdowns[i].classList.remove('show-createProduct'); // Đóng tất cả dropdown
    }

    // Mở dropdown tương ứng với ô input hiện tại
    if (ul) {
        ul.classList.add("show-createProduct"); // Hiển thị dropdown
    }
}


function showDropdownCategory(event) {
    var input = event.target;
    var listId = input.id.replace("myInput-", "dataList-"); // Tạo ID cho danh sách từ ID của input
    var ul = document.getElementById(listId);

    // Đóng tất cả dropdown khác
    var dropdowns = document.getElementsByClassName("dropdown-content-createProduct");
    for (var i = 0; i < dropdowns.length; i++) {
        dropdowns[i].classList.remove('show-createProduct'); // Đóng tất cả dropdown
    }

    // Mở dropdown tương ứng với ô input hiện tại
    if (ul) {
        ul.classList.add("show-createProduct"); // Hiển thị dropdown
    }
}

function filterFunction(event) {
    var input = event.target;
    var filter = input.value.toUpperCase();
    var listId = input.id.replace("myInput-", "dataList-"); // Tạo ID cho danh sách từ ID của input
    var ul = document.getElementById(listId);
    var li = ul.getElementsByTagName("li");

    // Lọc các sản phẩm dựa trên giá trị nhập vào
    for (var i = 0; i < li.length; i++) {
        var txtValue = li[i].textContent || li[i].innerText;
        li[i].style.display = txtValue.toUpperCase().indexOf(filter) > -1 ? "" : "none";
    }
}
function selectNameProduct(item) {
    var input = document.getElementById("myInput-nameProduct");
    input.value = item.textContent; // Đặt giá trị của ô input thành giá trị được chọn
    closeAllDropdowns(); // Đóng tất cả dropdowns sau khi chọn
}

function closeAllDropdowns() {
    var dropdowns = document.getElementsByClassName("dropdown-content-createProduct");
    for (var i = 0; i < dropdowns.length; i++) {
        dropdowns[i].classList.remove('show-createProduct'); // Đóng tất cả dropdown
    }
}


let currentType = ''; // Biến để lưu loại thuộc tính hiện tại

function openQuickAddForm(title, codeLabel, nameLabel, codePlaceholder, namePlaceholder, type) {
    currentType = type; // Lưu loại thuộc tính hiện tại
    document.getElementById("formTitle").innerText = title;

    // Cập nhật các nhãn và placeholder
    document.getElementById("label1").innerText = codeLabel;
    document.getElementById("label2").innerText = nameLabel;
    document.getElementById("input1").placeholder = codePlaceholder;
    document.getElementById("input2").placeholder = namePlaceholder;

    // Hiển thị form thêm thuộc tính
    document.getElementById("quickAddForm").style.display = "flex"; // Đảm bảo form hiển thị
}

function closeQuickAddForm() {
    document.getElementById("quickAddForm").style.display = "none";
    document.getElementById("input1").value = ""// Ẩn form
    document.getElementById("input2").value = ""// Ẩn form
}

function submitQuickAdd() {
    const code = document.getElementById("input1").value;
    const name = document.getElementById("input2").value;

    // Dữ liệu JSON để gửi
    const data = {
        code: code,
        name: name,
        type: currentType // Gửi loại thuộc tính (danh mục, màu sắc, kích thước)
    };

    // Gửi yêu cầu POST đến API
    fetch('/product-api/attribute/quickly-add', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    alert("thêm thuộc tính thành công");
    closeQuickAddForm();
    reloadOptions();
}

function reloadOptions() {
    const url = `/product-api/attribute/list?type=${currentType}`; // URL API với tham số `type`

    // Gửi yêu cầu GET đến API để lấy danh sách thuộc tính mới
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Cập nhật `select` dựa trên `currentType`
            let selectElement;
            const placeholderOption = document.createElement("option");
            placeholderOption.value = "0";

            // Xác định select element tương ứng với `currentType`
            switch (currentType) {
                case 'color':
                    selectElement = document.getElementById("colorSelect");
                    placeholderOption.textContent = "Chọn màu sắc";
                    break;
                case 'size':
                    selectElement = document.getElementById("sizeSelect");
                    placeholderOption.textContent = "Chọn kích cỡ";
                    break;
                default:
                    console.error('Loại thuộc tính không hợp lệ');
                    return;
            }

            // Xóa các option cũ
            selectElement.innerHTML = '';

            // Thêm option mặc định


            selectElement.appendChild(placeholderOption);

            // Thêm các option mới từ dữ liệu nhận được
            data.forEach(item => {
                const option = document.createElement("option");
                option.value = item.id;

                // Sử dụng các trường tương ứng để đặt tên hiển thị của từng loại
                if (currentType === 'color') {
                    option.textContent = item.nameColor;
                } else if (currentType === 'size') {
                    option.textContent = item.nameSize;
                }

                selectElement.appendChild(option);
            });
        })
        .catch((error) => {
            console.error('Lỗi:', error);
        });
}


// Close the dropdown when clicking outside
// Lắng nghe sự kiện click trên toàn bộ cửa sổ
window.onclick = function (event) {
    // Kiểm tra nếu phần tử được bấm không phải là dropdown hoặc là một phần tử con của dropdown
    if (!event.target.closest('.custom-dropdown-createProductDetail')) {
        const dropdowns = document.getElementsByClassName('custom-dropdown-createProductDetail');

        // Duyệt qua tất cả dropdowns và loại bỏ lớp 'active' để ẩn dropdown
        for (let i = 0; i < dropdowns.length; i++) {
            dropdowns[i].classList.remove('active');
        }
    }
}


function updateTableRows() {
    const selectedColors = Array.from(document.querySelectorAll('#dataList-color input[type="checkbox"]:checked'))
        .map(checkbox => {
            const colorName = checkbox.closest('li').querySelector('span').innerText;
            const colorId = checkbox.value; // Lấy ID màu
            return {name: colorName, id: colorId};
        });

    const selectedSizes = Array.from(document.querySelectorAll('#dataList-size input[type="checkbox"]:checked'))
        .map(checkbox => {
            const sizeName = checkbox.closest('li').querySelector('span').innerText;
            const sizeId = checkbox.value; // Lấy ID kích cỡ
            return {name: sizeName, id: sizeId};
        });
    const tableBody = document.querySelector('#productDetailTable tbody');
    tableBody.innerHTML = '';

    selectedColors.forEach(color => {
        selectedSizes.forEach(size => {
            const row = document.createElement('tr');

            row.innerHTML = `
        <td><input type="checkbox" class="row-selector"></td>
        <td class="editable-cell" data-color-id="${color.id}">${color.name}</td>
        <td class="editable-cell" data-size-id="${size.id}">${size.name}</td>
        <td contenteditable="true" class="editable-cell"></td> <!-- Giá bán -->
        <td contenteditable="true" class="editable-cell"></td> <!-- Giá nhập -->
        <td contenteditable="true" class="editable-cell"></td> <!-- Số lượng -->
        <td contenteditable="true" class="editable-cell"></td> <!-- Trọng lượng -->
        <td contenteditable="true" class="editable-cell"></td> <!-- Mô tả -->
        <td><span class="material-symbols-outlined" onclick="deleteRow(this)">delete_forever</span></td>
      `;

            tableBody.appendChild(row);
        });
    });

    // Lắng nghe sự kiện thay đổi trên các ô có thể chỉnh sửa
    document.querySelectorAll('.editable-cell').forEach(cell => {
        cell.addEventListener('input', function (event) {
            const colIndex = cell.cellIndex;
            const newValue = cell.innerText;
            const selection = window.getSelection();
            const range = selection.getRangeAt(0);
            const cursorPosition = range.startOffset; // Lưu vị trí con trỏ

            // Cập nhật các ô tương ứng trong các hàng đã chọn
            document.querySelectorAll('.row-selector:checked').forEach(checkbox => {
                const selectedRow = checkbox.closest('tr');
                const targetCell = selectedRow.cells[colIndex];
                targetCell.innerText = newValue;
            });

            // Khôi phục vị trí con trỏ
            range.setStart(cell.childNodes[0], cursorPosition);
            range.collapse(true);
            selection.removeAllRanges();
            selection.addRange(range);
        });
    });
}

// Gọi hàm updateTableRows khi chọn checkbox
document.querySelectorAll('#dataList-color input[type="checkbox"], #dataList-size input[type="checkbox"]').forEach(checkbox => {
    checkbox.addEventListener('change', updateTableRows);
});

function deleteRow(deleteButton) {
    const row = deleteButton.closest('tr');
    const color = row.cells[1].innerText; // Cột màu sắc
    const size = row.cells[2].innerText;  // Cột kích cỡ

    row.remove(); // Xóa hàng hiện tại

    // Kiểm tra nếu màu hoặc kích cỡ không còn trong bảng thì bỏ chọn checkbox tương ứng
    checkAndUncheckOption('#dataList-color', color);
    checkAndUncheckOption('#dataList-size', size);
}

function checkAndUncheckOption(listSelector, value) {
    // Kiểm tra nếu không còn hàng nào chứa giá trị cần kiểm tra
    const isValueInTable = Array.from(document.querySelectorAll('#productDetailTable tbody tr')).some(row => {
        return row.cells[1].innerText === value || row.cells[2].innerText === value;
    });

    // Nếu không còn hàng nào chứa giá trị này thì bỏ chọn checkbox tương ứng
    if (!isValueInTable) {
        document.querySelectorAll(`${listSelector} input[type="checkbox"]`).forEach(checkbox => {
            const itemText = checkbox.closest('li').querySelector('span').innerText;
            if (itemText === value) {
                checkbox.checked = false;
            }
        });
    }
}


async function addProductWithDetails() {
    const formElement = document.getElementById('createProductForm');
    const formData = new FormData(formElement);

    // Thu thập dữ liệu chi tiết sản phẩm từ bảng và thêm vào FormData
    const rows = document.querySelectorAll('#productDetailTable tbody tr');
    const productDetails = [];

    rows.forEach(row => {
        const detail = {
            color: { id: row.cells[1].getAttribute('data-color-id') }, // Truyền đối tượng color
            size: { id: row.cells[2].getAttribute('data-size-id') }, // Truyền đối tượng size
            price: row.cells[3].innerText.trim(),
            import_price: row.cells[4].innerText.trim(),
            quantity: row.cells[5].innerText.trim(),
            weight: row.cells[6].innerText.trim(),
            describe: row.cells[7].innerText.trim(),
            status: 1
        };
        productDetails.push(detail);
    });

    // Thêm mảng productDetails vào FormData
    formData.append("productDetails", JSON.stringify(productDetails));

    try {
        const response = await fetch('/staff/product/add-product-with-details', {
            method: 'POST',
            body: formData
        });

        if (!response.ok) throw new Error('Thêm sản phẩm thất bại');

        alert("Sản phẩm và chi tiết sản phẩm đã được thêm thành công");
        window.location.href = 'http://localhost:8080/staff/product';
    } catch (error) {
        console.error('Lỗi:', error);
        alert("Có lỗi xảy ra khi thêm sản phẩm hoặc chi tiết sản phẩm.");
    }
}




