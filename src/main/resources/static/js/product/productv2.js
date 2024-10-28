function toggleDropdownProduct(event, icon) {
    var menu = icon.nextElementSibling;

    // Kiểm tra và thay đổi trạng thái hiển thị menu
    menu.classList.toggle('show-product');

    // Ngăn chặn sự kiện click lan ra ngoài
    event.stopPropagation();
}

// Đóng menu khi hover ra khỏi khu vực
document.querySelectorAll('.dropdown-product').forEach(function(dropdown) {
    dropdown.addEventListener('mouseleave', function() {
        var menu = this.querySelector('.dropdown-menu-product');
        if (menu.classList.contains('show-product')) {
            menu.classList.remove('show-product');
        }
    });
});

// Đóng menu khi click ra ngoài khu vực menu
window.onclick = function(event) {
    if (!event.target.matches('.fa-ellipsis-v-product')) {
        var dropdowns = document.getElementsByClassName("dropdown-menu-product");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show-product')) {
                openDropdown.classList.remove('show-product');
            }
        }
    }
}
function showGridViewProduct() {
    document.querySelector('.form-group-product').style.display = 'flex';
    document.querySelector('.table-product').style.display = 'none';
    document.querySelector('.fa-th-product').style.color = 'blue';
    document.querySelector('.fa-list-product').style.color = 'black';
}

function showListViewProduct() {
    document.querySelector('.form-group-product').style.display = 'none';
    document.querySelector('.table-product').style.display = 'table';
    document.querySelector('.fa-th-product').style.color = 'black';
    document.querySelector('.fa-list-product').style.color = 'blue';
}
// Hàm chọn tất cả checkbox
function toggleSelectAllProduct(selectAllCheckbox) {
    const checkboxes = document.querySelectorAll('.select-row-product');
    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAllCheckbox.checked;
        toggleEditableRow(checkbox);
    });
    toggleSaveButton();
}

document.querySelectorAll('.select-row-product').forEach((checkbox) => {
    checkbox.addEventListener('change', function() {
        const allChecked = document.querySelectorAll('.select-row-product:checked').length === document.querySelectorAll('.select-row-product').length;
        document.getElementById('select-all-product').checked = allChecked;
        toggleEditableRow(checkbox);
        toggleSaveButton();
    });
});

// Bật/tắt khả năng chỉnh sửa các ô trong hàng dựa trên trạng thái checkbox
function toggleEditableRow(checkbox) {
    const row = checkbox.closest('tr');
    const cells = row.querySelectorAll('td:not(:first-child):not(:last-child)'); // Bỏ qua ô đầu tiên và cuối cùng (checkbox và các hành động)
    cells.forEach((cell) => {
        if (checkbox.checked) {
            cell.contentEditable = "true";
            cell.style.backgroundColor = "#eef2ff"; // Tuỳ chọn: làm nổi bật ô có thể chỉnh sửa
        } else {
            cell.contentEditable = "false";
            cell.style.backgroundColor = ""; // Đặt lại màu nền
        }
    });
}

// Hiển thị/ẩn nút "Lưu lại" và "Hủy" dựa trên checkbox được chọn
function toggleSaveButton() {
    const anyChecked = document.querySelectorAll('.select-row-product:checked').length > 0;
    document.getElementById('save-button').style.display = anyChecked ? "block" : "none";
    document.getElementById('cancel-button').style.display = anyChecked ? "block" : "none";
}

// Hủy bỏ chọn tất cả và đặt lại trạng thái các ô
function cancelChanges() {
    fetch('/api/productList') // Endpoint trả về JSON từ cơ sở dữ liệu
        .then(response => response.json())
        .then(data => {
            // Xóa các hàng hiện tại trong tbody
            const tbody = document.querySelector('.table-product tbody');
            tbody.innerHTML = '';

            // Duyệt qua dữ liệu JSON và tạo các hàng bảng
            data.forEach(product => {
                const row = document.createElement('tr');
                row.setAttribute('data-id', product.id);

                row.innerHTML = `
                    <td><input type="checkbox" class="select-row-product"/></td>
                    <td data-column="codeProduct">${product.codeProduct}</td>
                    <td data-column="nameProduct">
                        <div class="product-image-container">
                            <div class="image-slider">
                                <div class="slides">
                                    ${product.images.map(image => `
                                        <div class="slide">
                                            <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1728725582/${image.nameImage}" alt="Ảnh sản phẩm">
                                        </div>
                                    `).join('')}
                                </div>
                            </div>
                        </div>
                        <div>${product.nameProduct}</div>
                    </td>
                    <td data-column="material">${product.material.nameMaterial}</td>
                    <td data-column="manufacturer">${product.manufacturer.nameManufacturer}</td>
                    <td data-column="origin">${product.origin.nameOrigin}</td>
                    <td data-column="sole">${product.sole.nameSole}</td>
                    <td data-column="describe">${product.describe}</td>
                    <td data-column="status">${product.status === 1 ? 'Đang bán' : (product.status === 2 ? 'Ngừng bán' : '')}</td>
                    <td>
                        <div class="dropdown-product">
                            <i class="fa fa-ellipsis-v fa-ellipsis-v-product" aria-hidden="true" onclick="toggleDropdownProduct(event, this)"></i>
                            <div class="dropdown-menu-product">
                                <a href="#">Xem chi tiết</a>
                                <a href="#">Chỉnh sửa</a>
                                <a href="#">Xóa</a>
                            </div>
                        </div>
                    </td>
                `;

                tbody.appendChild(row);
            });
        })
        .catch(error => console.error('Error:', error));
}


// Lưu thay đổi (tuỳ chỉnh hàm này theo nhu cầu)
function saveChanges() {
    // Tạo mảng chứa dữ liệu từ các hàng được chọn
    const updatedData = [];

    // Duyệt qua các hàng có checkbox được chọn
    document.querySelectorAll('.select-row-product:checked').forEach((checkbox) => {
        const row = checkbox.closest('tr');
        const dataId = row.getAttribute('data-id');  // Lấy ID của sản phẩm (dùng để xác định bản ghi trong database)
        const updatedRow = {
            id: dataId,
            codeProduct: row.cells[1].innerText,
            nameProduct: row.cells[2].querySelector("div").innerText,
            material: row.cells[3].innerText,
            manufacturer: row.cells[4].innerText,
            origin: row.cells[5].innerText,
            sole: row.cells[6].innerText,
            describe: row.cells[7].innerText,
            status: row.cells[8].innerText === 'Đang bán' ? 1 : 2  // Chuyển trạng thái thành số để lưu vào database
        };
        updatedData.push(updatedRow);
    });

    // Gửi dữ liệu đến server qua AJAX
    fetch('/api/updateProducts', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedData), // Chuyển đổi dữ liệu sang JSON
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("Thay đổi đã được lưu vào database!");
                // Đặt lại trạng thái tất cả các checkbox và ô không chỉnh sửa
                document.querySelectorAll('.select-row-product:checked').forEach((checkbox) => {
                    checkbox.checked = false;
                    toggleEditableRow(checkbox);
                });
                document.getElementById('select-all-product').checked = false;
                toggleSaveButton();
            } else {
                alert("Có lỗi xảy ra khi lưu dữ liệu!");
            }
        })
        .catch((error) => {
            console.error("Lỗi khi gửi dữ liệu:", error);
            alert("Không thể kết nối tới server!");
        });
}

document.addEventListener("DOMContentLoaded", function() {
    // Tìm tất cả các phần tử có class 'form-control-product'
    const productControls = document.querySelectorAll('.form-control-product');

    productControls.forEach((control) => {
        const slides = control.querySelector('.slides');
        const slideElements = slides.children;
        let currentSlide = 0;

        setInterval(() => {
            currentSlide = (currentSlide + 1) % slideElements.length; // Chuyển tới slide tiếp theo
            slides.style.transform = `translateX(-${currentSlide * 100}%)`; // Trượt hình ảnh
        }, 7000); // Thay đổi sau mỗi 10 giây
    });
});
document.addEventListener("DOMContentLoaded", function() {
    // Chọn tất cả các image-slider trong bảng
    const sliders = document.querySelectorAll('.image-slider');

    sliders.forEach(slider => {
        const slides = slider.querySelector('.slides');
        const slideElements = slides.children;
        let currentSlide = 0;

        // Chạy một hàm để tự động chuyển đổi hình ảnh
        const interval = setInterval(() => {
            currentSlide = (currentSlide + 1) % slideElements.length; // Chuyển tới slide tiếp theo
            slides.style.transform = `translateX(-${currentSlide * 100}%)`; // Trượt hình ảnh
        }, 7000); // Thay đổi sau mỗi 10 giây

    });
});

