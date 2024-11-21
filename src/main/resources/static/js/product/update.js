
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
}


function showDropdown(event) {
    var input = event.target;
    var listId = input.id.replace("myInput-", "dataList-"); // Tạo ID cho danh sách từ ID của input
    var ul = document.getElementById(listId);

    // Đóng tất cả dropdown khác
    var dropdowns = document.getElementsByClassName("dropdown-content-updateProduct");
    for (var i = 0; i < dropdowns.length; i++) {
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
    var input = event.target;
    var filter = input.value.toUpperCase();
    var listId = input.id.replace("myInput-", "dataList-"); // Tạo ID cho danh sách từ ID của input
    var ul = document.getElementById(listId);
    var li = ul.getElementsByTagName("li");

    // Lọc các mục dựa trên giá trị nhập vào
    for (var i = 0; i < li.length; i++) {
        var txtValue = li[i].textContent || li[i].innerText;

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
    var dropdowns = document.getElementsByClassName("dropdown-content-updateProduct");
    for (var i = 0; i < dropdowns.length; i++) {
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
        .then(response => {
            if (!response.ok) {
                throw new Error('Lỗi khi thêm thuộc tính');
            }
            createToast('1', 'Thêm thuộc tính thành công')
            closeQuickAddForm(); // Đóng form thêm thuộc tính
            reloadOptions(`${currentType}`); // Tải lại danh sách thuộc tính mới cho select
        })
        .catch(error => {
            console.error('Lỗi:', error);
            createToast('3', 'Thêm thuộc tính thất bại')
        });
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



function toggleCheckbox(liElement, type) {
    const checkbox = liElement.querySelector(`input[data-type="${type}"]`);
    if (checkbox) {
        checkbox.checked = !checkbox.checked;
        handleCheckboxChange(); // Gọi hàm kiểm tra sau khi thay đổi trạng thái
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
        await fetch(`/staff/product/update-product/${productId}`, {
            method: 'POST',
            body: formData
        });
        window.location.href = `/staff/product/view-update/${productId}`;
        createToast('1', 'Sửa sản phẩm thành công');

    } catch (error) {
        createToast('3', 'Thêm sản phẩm thất bại')
    }

}

function resetFormAndTable() {
    // Đặt lại tất cả các trường input trong form
    document.getElementById('updateProductForm').reset();

    // Làm trống phần preview ảnh nếu có
    document.getElementById('image-preview-updateProduct').innerHTML = '';

    // Làm trống các hàng trong bảng sản phẩm chi tiết
    const tableBody = document.getElementById('productDetailTable').querySelector('tbody');
    tableBody.innerHTML = '';
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


