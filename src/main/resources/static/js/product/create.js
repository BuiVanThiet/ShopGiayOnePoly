// Hàm chọn tất cả các dòng trong bảng
function toggleSelectAll(selectAllCheckbox) {
    const checkboxes = document.querySelectorAll('.select-row-createProduct');
    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAllCheckbox.checked;
    });
}

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

function toggleDropdownCreateProduct() {
    const dropdown = document.getElementById('categoryDropdown-createProduct');
    dropdown.classList.toggle('active');
}

// Close the dropdown when clicking outside
window.onclick = function(event) {
    if (!event.target.matches('.custom-dropdown-createProduct')) {
        const dropdowns = document.getElementsByClassName('custom-dropdown-createProduct');
        for (let i = 0; i < dropdowns.length; i++) {
            dropdowns[i].classList.remove('active');
        }
    }
}
