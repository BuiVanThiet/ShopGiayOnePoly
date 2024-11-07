function showDropdown(event) {
    var input = event.target;
    var listId = input.id.replace("myInput-", "dataList-"); // Tạo ID cho danh sách từ ID của input
    var ul = document.getElementById(listId);

    // Đóng tất cả dropdown khác
    var dropdowns = document.getElementsByClassName("dropdown-content-createProductDetail");
    for (var i = 0; i < dropdowns.length; i++) {
        dropdowns[i].classList.remove('show-createProductDetail'); // Đóng tất cả dropdown
    }

    // Mở dropdown tương ứng với ô input hiện tại
    if (ul) {
        ul.classList.add("show-createProductDetail"); // Hiển thị dropdown
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


function closeAllDropdowns() {
    var dropdowns = document.getElementsByClassName("dropdown-content-createProduct");
    for (var i = 0; i < dropdowns.length; i++) {
        dropdowns[i].classList.remove('show-createProduct'); // Đóng tất cả dropdown
    }
}

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
                case 'category':
                    selectElement = document.getElementById("categorySelect");
                    placeholderOption.textContent = "Chọn danh mục";
                    break;
                case 'origin':
                    selectElement = document.getElementById("originSelect");
                    placeholderOption.textContent = "Chọn xuất xứ";
                    break;
                case 'material':
                    selectElement = document.getElementById("materialSelect");
                    placeholderOption.textContent = "Chọn chất liệu";
                    break;
                case 'manufacturer':
                    selectElement = document.getElementById("manufacturerSelect");
                    placeholderOption.textContent = "Chọn hãng";
                    break;
                case 'color':
                    selectElement = document.getElementById("colorSelect");
                    placeholderOption.textContent = "Chọn màu sắc";
                    break;
                case 'size':
                    selectElement = document.getElementById("sizeSelect");
                    placeholderOption.textContent = "Chọn kích cỡ";
                    break;
                case 'sole':
                    selectElement = document.getElementById("soleSelect");
                    placeholderOption.textContent = "Chọn loại đế";
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
                if (currentType === 'category') {
                    option.textContent = item.nameCategory;
                } else if (currentType === 'origin') {
                    option.textContent = item.nameOrigin;
                } else if (currentType === 'material') {
                    option.textContent = item.nameMaterial;
                } else if (currentType === 'manufacturer') {
                    option.textContent = item.nameManufacturer;
                } else if (currentType === 'color') {
                    option.textContent = item.nameColor;
                } else if (currentType === 'size') {
                    option.textContent = item.nameSize;
                } else if (currentType === 'sole') {
                    option.textContent = item.nameSole;
                }

                selectElement.appendChild(option);
            });
        })
        .catch((error) => {
            console.error('Lỗi:', error);
        });
}

// Close the dropdown when clicking outside
window.onclick = function (event) {
    if (!event.target.matches('.custom-dropdown-createProductDetail')) {
        const dropdowns = document.getElementsByClassName('custom-dropdown-createProductDetail');
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


function addProductDetail(idProduct) {
    const productDetails = [];
    const rows = document.querySelectorAll('#productDetailTable tbody tr');

    // Duyệt qua từng hàng trong bảng để lấy dữ liệu
    rows.forEach(row => {
        const colorId = parseInt(row.cells[1].getAttribute('data-color-id')); // ID màu
        const sizeId = parseInt(row.cells[2].getAttribute('data-size-id'));
        const price = parseFloat(row.cells[3].innerText.trim()) || 0; // Giá bán
        const importPrice = parseFloat(row.cells[4].innerText.trim()) || 0; // Giá nhập
        const quantity = parseInt(row.cells[5].innerText.trim(), 10) || 0; // Số lượng
        const weight = parseFloat(row.cells[6].innerText.trim()) || 0; // Trọng lượng
        const description = row.cells[7].innerText.trim(); // Mô tả
        const status = 1;

        // Tạo đối tượng chi tiết sản phẩm
        const productDetail = {
            product: {id: idProduct}, // Thay thế với cấu trúc thực tế trong Java backend
            color: {id: colorId}, // ID màu
            size: {id: sizeId}, // ID kích thước
            price: price,
            import_price: importPrice,
            quantity: quantity,
            describe: description,
            weight: weight,
            status: status // Trạng thái
        };

        productDetails.push(productDetail);
    })
    fetch('/staff/product/add-productDetail', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(productDetails)
    })
        .then(response => response.json())
    window.location.href = 'http://localhost:8080/staff/product/detail/' + idProduct;
}


