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
function filterFunction() {
    var input, filter, ul, li, i;
    input = document.getElementById("myInput-codeProduct");
    filter = input.value.toUpperCase();
    ul = document.getElementById("dataList-codeProduct");
    li = ul.getElementsByTagName("li");

    // Hiển thị dropdown khi có nhập liệu
    if (input.value) {
        ul.classList.add("show");
    } else {
        ul.classList.remove("show");
    }

    // Lọc các sản phẩm dựa trên giá trị nhập vào
    for (i = 0; i < li.length; i++) {
        txtValue = li[i].textContent || li[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";
        } else {
            li[i].style.display = "none";
        }
    }
}

// Đóng dropdown khi click ngoài
window.onclick = function(event) {
    if (!event.target.matches('#myInput-codeProduct')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}
