
// Hàm preview hình ảnh khi người dùng tải lên
function previewImages(event) {
    const files = event.target.files;
    const previewContainer = document.getElementById('image-preview-updateProduct');

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
    let dropdowns = document.getElementsByClassName("dropdown-content-updateProduct");
    for (let i = 0; i < dropdowns.length; i++) {
        dropdowns[i].classList.remove('show-updateProduct'); // Đóng tất cả dropdown
    }

    // Mở dropdown tương ứng với ô input hiện tại và đặt vị trí ngay bên dưới input
    if (ul) {
        ul.style.top = input.offsetHeight + 24 + "px"; // Đặt vị trí dropdown ngay dưới input
        ul.classList.add("show-updateProduct"); // Hiển thị dropdown
    }

    // Thêm sự kiện click để đóng dropdown khi bấm ra ngoài
    document.addEventListener('click', function closeDropdown(e) {
        if (!ul.contains(e.target) && !input.contains(e.target)) { // Kiểm tra nếu click xảy ra ngoài dropdown và input
            ul.classList.remove("show-updateProduct"); // Đóng dropdown
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
        input.setAttribute(`data-${dataType}-id`, item.getAttribute('value'));
    }
    closeAllDropdowns(); // Đóng tất cả dropdowns sau khi chọn
    validate(dataType);
}



function closeAllDropdowns() {
    let dropdowns = document.getElementsByClassName("dropdown-content-updateProduct");
    for (let i = 0; i < dropdowns.length; i++) {
        dropdowns[i].classList.remove('show-updateProduct'); // Đóng tất cả dropdown
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


async function updateProduct() {
    const formElement = document.getElementById('updateProductForm');
    const productId = formElement.getAttribute('data-product-id');

    const formData = new FormData(formElement);
    const originID = document.getElementById('myInput-origin').getAttribute('data-origin-id');
    const materialID = document.getElementById('myInput-material').getAttribute('data-material-id');
    const manufacturerID = document.getElementById('myInput-manufacturer').getAttribute('data-manufacturer-id');
    const soleID = document.getElementById('myInput-sole').getAttribute('data-sole-id');
    formData.append('origin', originID); // Thêm idOrigin vào phần dữ liệu sản phẩm
    formData.append('material', materialID); // Thêm idOrigin vào phần dữ liệu sản phẩm
    formData.append('manufacturer', manufacturerID); // Thêm idOrigin vào phần dữ liệu sản phẩm
    formData.append('sole', soleID); // Thêm idOrigin vào phần dữ liệu sản phẩm
    await fetch(`/staff/product/update-product/${productId}`, {
        method: 'POST',
        body: formData
    });
    window.location.href = `/staff/product/view-update/${productId}`;

}

function resetForm() {
    const formElement = document.getElementById('updateProductForm');
    const productId = formElement.getAttribute('data-product-id');
    window.location.href = "/staff/product/view-update/" + productId;
}


function toggleCheckbox(liElement, type) {
    const checkbox = liElement.querySelector(`input[data-type="${type}"]`);
    if (checkbox) {
        checkbox.checked = !checkbox.checked;
        validate();
    }
}