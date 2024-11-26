function showDropdown(event) {
    let input = event.target;
    let listId = input.id.replace("myInput-", "dataList-"); // Tạo ID cho danh sách từ ID của input
    let ul = document.getElementById(listId);

    // Đóng tất cả dropdown khác
    let dropdowns = document.getElementsByClassName("dropdown-content-createProductDetail");
    for (let i = 0; i < dropdowns.length; i++) {
        dropdowns[i].classList.remove('show-createProductDetail'); // Đóng tất cả dropdown
    }

    // Mở dropdown tương ứng với ô input hiện tại
    if (ul) {
        ul.style.top = input.offsetHeight + 24 + "px"; // Đặt vị trí dropdown ngay dưới input
        ul.classList.add("show-createProductDetail"); // Hiển thị dropdown
    }

    document.addEventListener('click', function closeDropdown(e) {
        if (!ul.contains(e.target) && !input.contains(e.target)) { // Kiểm tra nếu click xảy ra ngoài dropdown và input
            ul.classList.remove("show-createProductDetail"); // Đóng dropdown
            document.removeEventListener('click', closeDropdown); // Gỡ sự kiện để tránh lặp lại
        }
    });
}

function filterFunctionCheckBox(event) {
    let input = event.target;
    let filter = input.value.toUpperCase();
    let ul = input.closest('ul');  // Lấy phần tử <ul> chứa input

    // Kiểm tra nếu ul tồn tại
    if (ul) {
        let li = ul.getElementsByTagName("li");  // Lấy tất cả các mục li trong danh sách

        // Bắt đầu từ mục thứ hai để bỏ qua ô tìm kiếm đầu tiên
        for (let i = 1; i < li.length; i++) {
            let span = li[i].getElementsByTagName("span")[0];  // Lấy thẻ span chứa tên danh mục
            if (span) {
                let txtValue = span.textContent || span.innerText;
                // Nếu có kết quả phù hợp, hiển thị, nếu không thì ẩn
                li[i].style.display = txtValue.toUpperCase().indexOf(filter) > -1 ? "" : "none";
            }
        }
    } else {
        console.error("Không tìm thấy phần tử ul chứa input tìm kiếm");
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
        headers: {'Content-Type': 'application/json'},
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

    // Xóa nội dung cũ
    element.innerHTML = '';

    // Thêm thẻ li chứa input tìm kiếm
    const searchLi = document.createElement("li");
    searchLi.style.display = "flex";
    searchLi.style.justifyContent = "space-between";

    const searchInput = document.createElement("input");
    searchInput.type = "text";
    searchInput.style.height = "30px";
    searchInput.placeholder = "Tìm kiếm";
    searchInput.onkeyup = (event) => filterFunctionCheckBox(event); // Gắn sự kiện tìm kiếm

    searchLi.appendChild(searchInput);
    element.appendChild(searchLi);

    // Duyệt qua dữ liệu và tạo các mục danh sách
    data.forEach(item => {
        const li = document.createElement("li");
        li.style.display = "flex";
        li.style.justifyContent = "space-between";

        // Tạo phần hiển thị tên
        const span = document.createElement("span");
        span.textContent = item[textKey];
        li.appendChild(span);

        // Nếu là category, color hoặc size, thêm checkbox
        if (type === "category" || type === "color" || type === "size") {
            const checkbox = document.createElement("input");
            checkbox.type = "checkbox";
            checkbox.value = item.id;
            checkbox.name = `${type}s`;
            checkbox.style.width = "20px";
            checkbox.setAttribute("data-type", type);
            checkbox.classList.add(`${type}-checkbox`);

            li.appendChild(checkbox);

            // Gắn sự kiện click cho li
            li.onclick = () => toggleCheckbox(li, type);
        }

        // Thêm class và ID cho li
        li.classList.add(`${type}-item`);
        li.id = `${type}-item-${item.id}`;

        // Thêm li vào danh sách
        element.appendChild(li);
    });
}


function insertTableProductDetail() {
    if (document.getElementById('select-all-productDetail').checked) {
        document.getElementById('select-all-productDetail').checked = false;
    }
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
    addValidationListeners();
}
function toggleCheckbox(liElement, type) {
    const checkbox = liElement.querySelector(`input[data-type="${type}"]`);
    if (checkbox) {
        checkbox.checked = !checkbox.checked;
        handleCheckboxChange(); // Gọi hàm kiểm tra sau khi thay đổi trạng thái

    }
}

// Gọi hàm updateTableRows khi chọn checkbox
function handleCheckboxChange() {

    const colorInput = document.getElementById('myInput-color');
    const selectedCheckboxesColor = document.querySelectorAll('#dataList-color input[type="checkbox"]:checked');
    const selectedNamesColor = Array.from(selectedCheckboxesColor).map(checkbox => {
        const liElement = checkbox.closest('li');
        return liElement.querySelector('span').innerText;
    });

    const sizeInput = document.getElementById('myInput-size');
    const selectedCheckboxesSize = document.querySelectorAll('#dataList-size input[type="checkbox"]:checked');
    const selectedNamesSize = Array.from(selectedCheckboxesSize).map(checkbox => {
        const liElement = checkbox.closest('li');
        return liElement.querySelector('span').innerText;
    });

    colorInput.value = selectedNamesColor.join(', ');
    sizeInput.value = selectedNamesSize.join(', ');

    // Xử lý xóa hàng khỏi bảng khi checkbox màu hoặc kích cỡ bị bỏ chọn
    const uncheckedColors = Array.from(document.querySelectorAll('#dataList-color input[type="checkbox"]:not(:checked)'));
    const uncheckedSizes = Array.from(document.querySelectorAll('#dataList-size input[type="checkbox"]:not(:checked)'));

    const tableBody = document.querySelector('#productDetailTable tbody');

    // Xóa các hàng liên quan đến màu bị bỏ chọn
    uncheckedColors.forEach(checkbox => {
        const colorId = checkbox.value;
        Array.from(tableBody.querySelectorAll(`tr[data-color-id="${colorId}"]`)).forEach(row => row.remove());
    });

    // Xóa các hàng liên quan đến kích cỡ bị bỏ chọn
    uncheckedSizes.forEach(checkbox => {
        const sizeId = checkbox.value;
        Array.from(tableBody.querySelectorAll(`tr[data-size-id="${sizeId}"]`)).forEach(row => row.remove());
    });

    // Kiểm tra nếu không còn màu hoặc kích cỡ được chọn thì ẩn bảng
    if (selectedCheckboxesColor.length === 0 || selectedCheckboxesSize.length === 0) {
        tableBody.innerHTML = ''; // Xóa toàn bộ hàng trong bảng
    }

    // Nếu không phải thay đổi danh mục, gọi `insertTableProductDetail`
    if (selectedCheckboxesColor.length > 0 && selectedCheckboxesSize.length > 0) {
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
        handleCheckboxChange();
    }

}


async function addProductDetail(idProduct) {
    const productDetails = [];
    const rows = document.querySelectorAll('#productDetailTable tbody tr');
    // Duyệt qua từng hàng trong bảng để lấy dữ liệu
    rows.forEach(row => {
        const detail = {
            color: {id: row.cells[1].getAttribute('data-color-id')}, // Truyền đối tượng color
            size: {id: row.cells[2].getAttribute('data-size-id')}, // Truyền đối tượng size
            price: row.cells[3].innerText.trim().replace(/\./g, ''), // Loại bỏ dấu chấm trong giá bán
            import_price: row.cells[4].innerText.trim().replace(/\./g, ''), // Loại bỏ dấu chấm trong giá nhập
            quantity: row.cells[5].innerText.trim().replace(/\./g, ''), // Loại bỏ dấu chấm trong số lượng
            weight: row.cells[6].innerText.trim().replace(/\./g, ''), // Loại bỏ dấu chấm trong trọng lượng
            describe: row.cells[7].innerText.trim(),
            status: 1
        };
        productDetails.push(detail);
    })
    if (validateAndFormatCells()){
        const response = await fetch(`/staff/product/add-productDetail/${idProduct}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(productDetails)
        })

        if (await response.text() === 'Thêm chi tiết sản phẩm thành công') {
            window.location.href = `/staff/product/detail/${idProduct}`;
        } else {
            createToast('2', 'Dữ liệu không hợp lệ')
        }
    }  else {
        createToast('2', 'Dữ liệu không hợp lệ')
    }


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
