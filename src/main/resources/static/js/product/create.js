
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
    validate();
}


function showDropdown(event) {
    let input = event.target;
    let listId = input.id.replace("myInput-", "dataList-"); // Tạo ID cho danh sách từ ID của input
    let ul = document.getElementById(listId);

    // Đóng tất cả dropdown khác
    let dropdowns = document.getElementsByClassName("dropdown-content-createProduct");
    for (let i = 0; i < dropdowns.length; i++) {
        dropdowns[i].classList.remove('show-createProduct'); // Đóng tất cả dropdown
    }

    // Mở dropdown tương ứng với ô input hiện tại và đặt vị trí ngay bên dưới input
    if (ul) {
        ul.style.top = input.offsetHeight + 24 + "px"; // Đặt vị trí dropdown ngay dưới input
        ul.classList.add("show-createProduct"); // Hiển thị dropdown
    }

    // Thêm sự kiện click để đóng dropdown khi bấm ra ngoài
    document.addEventListener('click', function closeDropdown(e) {
        if (!ul.contains(e.target) && !input.contains(e.target)) { // Kiểm tra nếu click xảy ra ngoài dropdown và input
            ul.classList.remove("show-createProduct"); // Đóng dropdown
            document.removeEventListener('click', closeDropdown); // Gỡ sự kiện để tránh lặp lại
        }
    });
}



function filterFunction(event) {
    let input = event.target;
    let filter = input.value.toUpperCase();
    let listId = input.id.replace("myInput-", "dataList-"); // Tạo ID cho danh sách từ ID của input
    let ul = document.getElementById(listId);
    let li = ul.getElementsByTagName("li");

    // Lọc các mục dựa trên giá trị nhập vào
    for (let i = 0; i < li.length; i++) {
        let txtValue = li[i].textContent || li[i].innerText;

        // Nếu có kết quả phù hợp, hiển thị, nếu không thì ẩn
        li[i].style.display = txtValue.toUpperCase().indexOf(filter) > -1 ? "" : "none";
    }
}

function selectAttribute(item, inputId, dataType) {
    const input = document.getElementById(inputId); // Lấy input dựa trên inputId
    if (input) {
        input.value = item.textContent; // Đặt giá trị của ô input thành giá trị được chọn
        input.setAttribute(`data-${dataType}-id`, item.getAttribute('value')); // Cập nhật data-* dựa trên dataType
    }
    closeAllDropdowns(); // Đóng tất cả dropdowns sau khi chọn
    validate(dataType);
}



function closeAllDropdowns() {
    let dropdowns = document.getElementsByClassName("dropdown-content-createProduct");
    for (let i = 0; i < dropdowns.length; i++) {
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

async function submitQuickAdd() {
    const codeInput = document.getElementById("input1").value.trim();
    const nameInput = document.getElementById("input2").value.trim();

    if (!codeInput || !nameInput) {
        createToast('2', 'Nhập đầy đủ mã và tên thuộc tính');
        return;
    }

    if (codeInput.length > 10) {
        createToast('2', 'Mã thuộc tính <= 10 kí tự');
        return;
    }

    if (nameInput.length > 50) {
        createToast('2', 'Tên thuộc tính <= 50 kí tự');
        return;
    }

    const [codeResponse, nameResponse] = await Promise.all([
        fetch(`/attribute/${currentType}/get-code`),
        fetch(`/attribute/${currentType}/get-name`)
    ]);

    const arrayCodeAttribute = codeResponse.ok ? await codeResponse.json() : [];
    const arrayNameAttribute = nameResponse.ok ? await nameResponse.json() : [];

    if (arrayCodeAttribute.some(code => code.toLowerCase() === codeInput.toLowerCase())) {
        createToast('2', 'Mã thuộc tính đã tồn tại');
        return;
    }

    if (arrayNameAttribute.some(name => name.toLowerCase() === nameInput.toLowerCase())) {
        createToast('2', 'Tên thuộc tính đã tồn tại');
        return;
    }

    const data = {
        code: codeInput,
        name: nameInput,
        type: currentType
    };

    const response = await fetch('/product-api/attribute/quickly-add', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    });

    if (response.ok) {
        const result = await response.json();
        createToast(result.check, result.message);
        closeQuickAddForm(); // Đóng form thêm thuộc tính
        reloadOptions(currentType); // Tải lại danh sách thuộc tính mới cho select
    }
}
function reloadOptions(type) {
    // URL API dựa trên loại thuộc tính (type)
    const url = `/product-api/attribute/list?type=${type}`;

    fetch(url)
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
            return response.json();
        })
        .then(data => {
            if (!Array.isArray(data)) {
                console.error("Dữ liệu không hợp lệ:", data);
                return;
            }

            const mapping = {
                category: "nameCategory",
                color: "nameColor",
                size: "nameSize",
                sole: "nameSole",
                material: "nameMaterial",
                manufacturer: "nameManufacturer",
                origin: "nameOrigin"
            };

            const textKey = mapping[type];
            const elementId = `dataList-${type}`;

            if (!textKey) {
                console.error("Loại thuộc tính không hợp lệ:", type);
                return;
            }

            updateOptions(elementId, data, textKey, type);
        })
        .catch(error => console.error("Lỗi xảy ra:", error));
}
function updateOptions(elementId, data, textKey, type) {
    const element = document.getElementById(elementId);
    if (!element) {
        console.error(`Không tìm thấy phần tử với ID: ${elementId}`);
        return;
    }

    element.innerHTML = ''; // Xóa nội dung cũ

    data.forEach(item => {
        const li = document.createElement("li");
        li.style.display = "flex";
        li.style.justifyContent = "space-between";

        // Thêm nội dung chính
        const span = document.createElement("span");
        span.textContent = item[textKey];
        li.appendChild(span);

        // Tùy chỉnh theo loại thuộc tính
        if (["category", "color", "size"].includes(type)) {
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.value = item.id;
            checkbox.style.width = "20px";
            checkbox.setAttribute("data-type", type);
            checkbox.classList.add(`${type}-checkbox`); // Thêm class cho checkbox

            // Thêm checkbox vào li
            li.appendChild(checkbox);

            // Thiết lập sự kiện click để toggle checkbox
            li.onclick = () => toggleCheckbox(li, type);
        } else {
            li.setAttribute("value", item.id);
            li.onclick = () => selectAttribute(li, `myInput-${type}`, type);
        }

        // Thêm class cho li
        li.classList.add(`${type}-item`);

        // Thêm id cho li (nếu cần thiết, ví dụ cho mỗi loại thuộc tính)
        li.id = `${type}-item-${item.id}`;

        // Thêm phần tử vào danh sách
        element.appendChild(li);
    });
}




function insertTableProductDetail() {

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
    document.getElementById('table-productDetail-createProduct').style.display = 'block';
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
            // Chỉ xử lý nếu hàng đang được chỉnh sửa có checkbox được chọn
            const row = cell.closest('tr');
            const checkbox = row.querySelector('.row-selector');

            if (!checkbox.checked) return; // Nếu checkbox không được chọn, không làm gì

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
    addValidationListeners(); // Thêm sự kiện validate

}


function toggleCheckbox(liElement, type) {
    const checkbox = liElement.querySelector(`input[data-type="${type}"]`);
    if (checkbox) {
        checkbox.checked = !checkbox.checked;
        handleCheckboxChange(); // Gọi hàm kiểm tra sau khi thay đổi trạng thái
        validate();
    }
}

function handleCheckboxChange() {
    const categoryInput = document.getElementById('myInput-category');
    const selectedCheckboxes = document.querySelectorAll('#dataList-category input[type="checkbox"]:checked');
    const selectedNames = Array.from(selectedCheckboxes).map(checkbox => {
        // Lấy tên từ phần tử `span` chứa text bên cạnh checkbox
        const liElement = checkbox.closest('li');
        return liElement.querySelector('span').innerText;
    });

    // Cập nhật giá trị vào input, nối bằng dấu phẩy nếu có nhiều hơn một
    categoryInput.value = selectedNames.join(', ');

    // Xử lý logic khác nếu cần (ví dụ, kiểm tra màu và kích cỡ)
    const selectedColors = document.querySelectorAll('#dataList-color input[type="checkbox"]:checked').length;
    const selectedSizes = document.querySelectorAll('#dataList-size input[type="checkbox"]:checked').length;

    if (selectedColors > 0 && selectedSizes > 0) {
        insertTableProductDetail();
    }
}

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
    const originID = document.getElementById('myInput-origin').getAttribute('data-origin-id');
    const materialID = document.getElementById('myInput-material').getAttribute('data-material-id');
    const manufacturerID = document.getElementById('myInput-manufacturer').getAttribute('data-manufacturer-id');
    const soleID = document.getElementById('myInput-sole').getAttribute('data-sole-id');
    formData.append('origin', originID); // Thêm idOrigin vào phần dữ liệu sản phẩm
    formData.append('material', materialID); // Thêm idOrigin vào phần dữ liệu sản phẩm
    formData.append('manufacturer', manufacturerID); // Thêm idOrigin vào phần dữ liệu sản phẩm
    formData.append('sole', soleID); // Thêm idOrigin vào phần dữ liệu sản phẩm
    // Thu thập dữ liệu chi tiết sản phẩm từ bảng và thêm vào FormData
    const rows = document.querySelectorAll('#productDetailTable tbody tr');
    const productDetails = [];

    rows.forEach(row => {
        const detail = {
            color: {id: row.cells[1].getAttribute('data-color-id')}, // Truyền đối tượng color
            size: {id: row.cells[2].getAttribute('data-size-id')}, // Truyền đối tượng size
            price: row.cells[3].innerText.trim(),
            import_price: row.cells[4].innerText.trim(),
            quantity: row.cells[5].innerText.trim(),
            weight: row.cells[6].innerText.trim(),
            describe: row.cells[7].innerText.trim(),
            status: 1
        };
        productDetails.push(detail);
    });

    if (productDetails.length > 0) {
        formData.append("productDetails", JSON.stringify(productDetails));
    }
    try {
        const response = await fetch('/staff/product/add-product-with-details', {
            method: 'POST',
            body: formData
        });
        if (await response.text() === 'Thêm sản phẩm thành công') {
            window.location.href = 'http://localhost:8080/staff/product';
        } else {
            window.location.href = 'http://localhost:8080/staff/product/create';
        }
    } catch (error) {
        createToast('3', 'Thêm sản phẩm thất bại')
    }

}

function resetFormAndTable() {
    // Đặt lại tất cả các trường input trong form
    document.getElementById('createProductForm').reset();
    errorTextCodeProduct.style.display = 'block'
    errorTextImage.style.display = 'block'
    errorTextCategory.style.display = 'block'
    errorTextMaterial.style.display = 'block'
    errorTextManufacturer.style.display = 'block'
    errorTextSole.style.display = 'block'
    errorTextOrigin.style.display = 'block'
    errorTextNameProduct.style.display = 'block'
    buttonAdd.style.display = 'none';
    material.placeholder = '';
    manufacturer.placeholder = '';
    origin.placeholder = '';
    sole.placeholder = '';
    material.setAttribute('data-material-id', '');
    manufacturer.setAttribute('data-material-id', '');
    origin.setAttribute('data-material-id', '');
    sole.setAttribute('data-material-id', '');

    // Làm trống phần preview ảnh nếu có
    document.getElementById('image-preview-createProduct').innerHTML = '';

    // Làm trống các hàng trong bảng sản phẩm chi tiết
    const tableBody = document.getElementById('productDetailTable').querySelector('tbody');
    tableBody.innerHTML = '';
    validate();
}


function toggleSelectAllproductDetail(selectAllCheckbox) {
    // Chọn tất cả các checkbox trong phần tbody của bảng sản phẩm
    const checkboxes = document.querySelectorAll('#productDetail-table-body .row-selector');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAllCheckbox.checked; // Đánh dấu hoặc bỏ đánh dấu checkbox
    });
}

document.querySelectorAll('.row-selector').forEach((checkbox) => {
    checkbox.addEventListener('change', function () {
        const allChecked = document.querySelectorAll('.row-selector:checked').length === document.querySelectorAll('.row-selector').length;
        document.getElementById('select-all-productDetail').checked = allChecked;
    });
});


