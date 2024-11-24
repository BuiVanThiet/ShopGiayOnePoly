
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
    let arrayNameAttribute = [];
    let arrayCodeAttribute = [];

    let codeAttribute = await fetch(`/attribute/${currentType}/get-code`);
    if (codeAttribute.ok) {
        arrayCodeAttribute = await codeAttribute.json(); // Đảm bảo đây là một mảng
    }
    let nameAttribute = await fetch(`/attribute/${currentType}/get-name`);
    if (nameAttribute.ok) {
        arrayNameAttribute = await nameAttribute.json(); // Đảm bảo đây là một mảng
    }
    if (codeInput.length > 0 && nameInput.length > 0) {
        if (arrayCodeAttribute.some(code => code.toLowerCase() === codeInput.toLowerCase())) {
            createToast('2', 'Mã thuộc tính đã tồn tại')
        } else if (arrayNameAttribute.some(name => name.toLowerCase() === nameInput.toLowerCase())) {
            createToast('2', 'Tên thuộc tính đã tồn tại')
        } else {
            const data = {
                code: codeInput,
                name: nameInput,
                type: currentType
            };
            const response = await fetch('/product-api/attribute/quickly-add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            if (response.ok){
                const result = await response.json();
                createToast(result.check, result.message);
                closeQuickAddForm(); // Đóng form thêm thuộc tính
                reloadOptions(`${currentType}`); // Tải lại danh sách thuộc tính mới cho select
            }

        }

    } else {
        createToast('2', 'Nhập đầy đủ mã và tên thuộc tính')
    }


}

function reloadOptions(type) {
    // URL API dựa trên loại thuộc tính (type)
    const url = `/product-api/attribute/list?type=${type}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Dữ liệu API trả về:", data); // Kiểm tra dữ liệu từ API

            // Kiểm tra nếu dữ liệu không hợp lệ hoặc không phải là mảng
            if (!Array.isArray(data)) {
                console.error("Dữ liệu không hợp lệ:", data);
                return;
            }

            // Cấu hình phần tử cần cập nhật
            const mapping = {
                category: { elementId: "dataList-category", type: "ul", textKey: "nameCategory" },
                color: { elementId: "dataList-color", type: "ul", textKey: "nameColor" },
                size: { elementId: "dataList-size", type: "ul", textKey: "nameSize" },
                sole: { elementId: "dataList-sole", type: "ul", textKey: "nameSole" },
                material: { elementId: "dataList-material", type: "ul", textKey: "nameMaterial" },
                manufacturer: { elementId: "dataList-manufacturer", type: "ul", textKey: "nameManufacturer" },
                origin: { elementId: "dataList-origin", type: "ul", textKey: "nameOrigin" }
            };

            const config = mapping[type];

            if (!config) {
                console.error("Loại thuộc tính không hợp lệ:", type);
                return;
            }

            updateOptions(config.elementId, data, config.type, config.textKey, type);
        })
        .catch(error => {
            console.error("Lỗi xảy ra:", error);
        });
}


function updateOptions(elementId, data, elementType, textKey, type) {
    const element = document.getElementById(elementId);

    if (!element) {
        console.error(`Không tìm thấy phần tử với ID: ${elementId}`);
        return;
    }

    element.innerHTML = ''; // Xóa nội dung cũ

    if (elementType === "ul") {
        data.forEach(item => {
            const li = document.createElement("li");

            // Tùy chỉnh giao diện cho checkbox hoặc chọn
            if (type === "category" || type === "color" || type === "size") {
                li.style.display = "flex";
                li.style.justifyContent = "space-between";

                const span = document.createElement("span");
                span.textContent = item[textKey];
                li.appendChild(span);

                const checkbox = document.createElement("input");
                checkbox.type = "checkbox";
                checkbox.value = item.id;
                checkbox.style.width = "20px";
                checkbox.setAttribute("data-type", type);
                li.appendChild(checkbox);

                li.onclick = () => toggleCheckbox(li, type);
            } else {
                li.textContent = item[textKey];
                li.setAttribute("value", item.id);
                li.onclick = () => selectAttribute(li, `myInput-${type}`, type);
            }

            element.appendChild(li);
        });
    } else {
        console.error("Hiện tại chỉ hỗ trợ phần tử <ul>.");
    }
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
    const selectedColors = document.querySelectorAll('#dataList-color input[type="checkbox"]:checked').length;
    const selectedSizes = document.querySelectorAll('#dataList-size input[type="checkbox"]:checked').length;

    // Kiểm tra nếu có ít nhất 1 màu và 1 kích cỡ được chọn
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


