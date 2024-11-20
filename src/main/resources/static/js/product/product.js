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

// Hàm chọn tất cả checkbox
function toggleSelectAllProduct(selectAllCheckbox) {
    // Chọn tất cả các checkbox trong phần tbody của bảng sản phẩm
    const checkboxes = document.querySelectorAll('#product-table-body .select-row-product');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAllCheckbox.checked; // Đánh dấu hoặc bỏ đánh dấu checkbox
    });

    toggleSaveButton(); // Cập nhật nút lưu nếu cần thiết

}



// Thêm sự kiện 'change' cho tất cả các checkbox có class 'select-row-product'
document.querySelectorAll('.select-row-product').forEach((checkbox) => {
    checkbox.addEventListener('change', function() {
        const allChecked = document.querySelectorAll('.select-row-product:checked').length === document.querySelectorAll('.select-row-product').length;
        document.getElementById('select-all-product').checked = allChecked;
        toggleSaveButton();
    });
});

// Hàm cập nhật hiển thị các nút dựa vào checkbox được chọn
function toggleSaveButton() {
    const anyChecked = document.querySelectorAll('.select-row-product:checked').length > 0;
    document.getElementById('btn-export-product').style.display = anyChecked ? "block" : "none";
    document.getElementById('btn-delete-product').style.display = anyChecked ? "block" : "none";
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
    fetch('/product-api/updateProducts', {
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

function initImageSlidersGridView() {
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
} // hàm chuyển ảnh grid view
initImageSlidersGridView();

function initImageSlidersTable() {
    // Chọn tất cả các image-slider trong bảng
    const sliders = document.querySelectorAll('.image-slider');

    sliders.forEach(slider => {
        const slides = slider.querySelector('.slides');
        const slideElements = slides.children;
        let currentSlide = 0;

        // Chạy một hàm để tự động chuyển đổi hình ảnh
        setInterval(() => {
            currentSlide = (currentSlide + 1) % slideElements.length; // Chuyển tới slide tiếp theo
            slides.style.transform = `translateX(-${currentSlide * 100}%)`; // Trượt hình ảnh
        }, 7000); // Thay đổi sau mỗi 7 giây
    });
}

initImageSlidersTable();

// Sự kiện khi người dùng thay đổi danh mục
document.querySelector('#search-select-product').addEventListener('change', function () {
    const idCategory = this.value;
    const searchTerm = document.querySelector('.search-input-product').value;
    fetchProductsByCategoryAndSearch(idCategory, searchTerm);
});

// Sự kiện khi người dùng nhập vào ô tìm kiếm
document.querySelector('.search-input-product').addEventListener('input', function () {
    const searchTerm = this.value;
    const idCategory = document.querySelector('#search-select-product').value;
    fetchProductsByCategoryAndSearch(idCategory, searchTerm);
});

// Hàm tìm kiếm sản phẩm theo danh mục và ô input
async function fetchProductsByCategoryAndSearch(idCategory, searchTerm, url = `/product-api/search`) {
    if (url === `/product-api/search`) {
        url += `?idCategory=${idCategory}&searchTerm=${searchTerm}`;
        document.getElementById('btn-findProduct-delete').style.display = 'block';
        document.getElementById('btn-findProduct-active').style.display = 'none';
    } else {
        document.getElementById('btn-findProduct-delete').style.display = 'none';
        document.getElementById('btn-findProduct-active').style.display = 'block';
    }
    await fetch(url)
        .then(response => response.json())
        .then(data => {
            products = data;
            currentPage = 1; // Đặt lại trang hiện tại là trang đầu tiên
            displayPage(currentPage); // Hiển thị lại trang với dữ liệu mới
        })
        .catch(error => console.error('Error:', error));
}


// Chuyển đổi giữa chế độ xem lưới và danh sách
function showGridViewProduct() {
    document.querySelector('.form-group-product').style.display = 'flex';
    document.querySelector('.table-product').style.display = 'none';
    document.querySelector('.fa-th-product').style.color = 'blue';
    document.querySelector('.fa-list-product').style.color = 'black';
    document.getElementById('items-per-page').style.display = 'none';
    isGridView = true;
    currentPage = 1;
    displayPage(currentPage); // Hiển thị lại trang hiện tại với chế độ grid
}

function showListViewProduct() {
    document.querySelector('.form-group-product').style.display = 'none';
    document.querySelector('.table-product').style.display = 'table';
    document.querySelector('.fa-th-product').style.color = 'black';
    document.querySelector('.fa-list-product').style.color = 'blue';
    document.getElementById('items-per-page').style.display = 'inline-block';
    isGridView = false;
    currentPage = 1;
    displayPage(currentPage); // Hiển thị lại trang hiện tại với chế độ list
}


// Hàm cập nhật các nút điều khiển phân trang
function updatePaginationControls(totalPages, page) {
    const pagination = document.getElementById('pagination-product');
    pagination.innerHTML = '';
    const itemsPerPage = isGridView ? itemsPerPageGrid : itemsPerPageList;
    // Các nút phân trang tiếp tục như trong code gốc
    // Tạo nút "Trang trước"
    const prevButton = document.createElement('button');
    prevButton.innerHTML = '<i class="fas fa-angle-left"></i>';
    prevButton.onclick = () => changePage(page - 1);
    prevButton.style.display = page === 1 ? 'none' : 'inline-block';
    pagination.appendChild(prevButton);
    let startPage, endPage;
    if (totalPages <= 3) {
        startPage = 1;
        endPage = totalPages;
    } else if (page <= 2) {
        startPage = 1;
        endPage = 3;
    } else if (page >= totalPages - 1) {
        startPage = totalPages - 2;
        endPage = totalPages;
    } else {
        startPage = page - 1;
        endPage = page + 1;
    }

    if (startPage > 1) {
        const firstPageButton = document.createElement('button');
        firstPageButton.innerText = '1';
        firstPageButton.onclick = () => changePage(1);
        pagination.appendChild(firstPageButton);

        const dots = document.createElement('span');
        dots.innerText = '...';
        pagination.appendChild(dots);
    }

    for (let i = startPage; i <= endPage; i++) {
        const pageButton = document.createElement('button');
        pageButton.innerText = i;
        pageButton.classList.add('number');
        if (i === page) {
            pageButton.style.backgroundColor = '#FAFAD2';
        }
        pageButton.onclick = () => changePage(i);
        pagination.appendChild(pageButton);
    }

    if (endPage < totalPages) {
        const dots = document.createElement('span');
        dots.innerText = '...';
        pagination.appendChild(dots);

        const lastPageButton = document.createElement('button');
        lastPageButton.innerText = totalPages;
        lastPageButton.onclick = () => changePage(totalPages);
        pagination.appendChild(lastPageButton);
    }

    const nextButton = document.createElement('button');
    nextButton.innerHTML = '<i class="fas fa-angle-right"></i>';

    nextButton.onclick = () => changePage(page + 1);
    nextButton.style.display = page === totalPages ? 'none' : 'inline-block';
    pagination.appendChild(nextButton);
}

var itemsPerPageList = 10; // Chế độ danh sách: 5 sản phẩm mỗi trang
var itemsPerPageGrid = 12; // Chế độ lưới: 12 sản phẩm mỗi trang
let currentPage = 1;
let products = []; // Mảng chứa các sản phẩm từ API
let isGridView = false; // Xác định chế độ hiện tại (false = danh sách, true = lưới)
function updateItemsPerPage(event) {
    if (parseInt(event.target.value) === 25){
        itemsPerPageList = products.length;
    } else {
        itemsPerPageList = parseInt(event.target.value);
    }
    displayPage(currentPage); // Hiển thị lại trang hiện tại với số lượng mới
    // Bỏ chọn tất cả các checkbox khi chuyển trang
    const selectAllCheckbox = document.getElementById('select-all-product');
    selectAllCheckbox.checked = false; // Bỏ chọn checkbox "tất cả"

    const checkboxes = document.querySelectorAll('.select-row-product');
    checkboxes.forEach((checkbox) => {
        checkbox.checked = false; // Bỏ chọn tất cả các checkbox của sản phẩm
    });
}
// Hàm chuyển trang
function changePage(newPage) {
    const itemsPerPage = isGridView ? itemsPerPageGrid : itemsPerPageList;
    const totalPages = Math.ceil(products.length / itemsPerPage);

    if (newPage >= 1 && newPage <= totalPages) {
        currentPage = newPage;
        displayPage(currentPage);

        // Bỏ chọn tất cả các checkbox khi chuyển trang
        const selectAllCheckbox = document.getElementById('select-all-product');
        selectAllCheckbox.checked = false; // Bỏ chọn checkbox "tất cả"

        const checkboxes = document.querySelectorAll('.select-row-product');
        checkboxes.forEach((checkbox) => {
            checkbox.checked = false; // Bỏ chọn tất cả các checkbox của sản phẩm
        });
    }
}
function displayPage(page) {
    if (document.getElementById('btn-export-product').style.display !== 'none'){
        document.getElementById('btn-export-product').style.display = 'none';
        document.getElementById('btn-delete-product').style.display = 'none';
    }
    const itemsPerPage = isGridView ? itemsPerPageGrid : itemsPerPageList;
    const totalPages = Math.ceil(products.length / itemsPerPage);
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;
    const tableBody = document.getElementById('product-table-body');
    tableBody.innerHTML = '';

    // Kiểm tra chế độ hiện tại để hiển thị đúng kiểu
    if (isGridView) {
        const gridContainer = document.querySelector('.form-group-product');
        gridContainer.style.display = 'flex'; // Hiển thị lại container grid khi vào chế độ grid
        gridContainer.innerHTML = ''; // Xóa nội dung trước đó để không bị trùng sản phẩm

        products.slice(start, end).forEach(product => {
            const productHtml = `
            <div class="form-control-product">
                <p>${product.nameProduct}</p>
                <div class="image-slider">
                    <div class="slides">
                        ${product.images.map(image => `
                            <div class="slide">
                                <img ondblclick="viewProductDetail(${product.id})" src="https://res.cloudinary.com/dfy4umpja/image/upload/v1728725582/${image.nameImage}" alt="Ảnh sản phẩm">
                            </div>
                        `).join('')}
                    </div>
                </div>
            </div>
        `;
            gridContainer.innerHTML += productHtml;
        });
    } else {
        products.slice(start, end).forEach(product => {
            const row = document.createElement('tr');
            row.id = `row-product-${product.id}`;
            row.dataset.id = product.id;
            row.ondblclick = () => viewProductDetail(product.id);

            // Xây dựng HTML cho slider ảnh
            const imageSliderHtml = product.images.map(image => `
                <div class="slide">
                    <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1728725582/${image.nameImage}" alt="Ảnh sản phẩm">
                </div>
            `).join('');

            row.innerHTML = `
                <td>
                        <input type="checkbox" class="select-row-product"/>
                </td>
                <td>
                    <div class="product-image-container">
                        <div class="image-slider">
                            <div class="slides">
                                ${imageSliderHtml}
                            </div>
                        </div>
                    </div>
                </td>
                <td data-column="codeProduct">${product.codeProduct}</td>
                <td data-column="nameProduct">${product.nameProduct}</td>
                <td data-column="material" data-material-id="${product.material.id}">${product.material.nameMaterial}</td>
                <td data-column="manufacturer">${product.manufacturer.nameManufacturer}</td>
                <td data-column="origin">${product.origin.nameOrigin}</td>
                <td data-column="sole">${product.sole.nameSole}</td>
                <td data-column="describe">${product.describe}</td>
                <td data-column="status">
                    ${product.status === 1 ? 'Đang bán' : (product.status === 2 ? 'Ngừng bán' : (product.status === 0 ? 'Đã xóa' : ''))}
                </td>
                <td>
                    ${product.status === 0 ? `
                        <!-- Icon Khôi phục khi status = 0 -->
                        <i id="restore-product" class="fa fa-undo fa-restore-icon" aria-hidden="true" onclick="updateStatus(${product.id}, 1)" title="Khôi phục"></i>
                    ` : `
                        <!-- Dropdown menu khi status khác 0 -->
                        <div class="dropdown-product">
                            <i class="fa fa-ellipsis-v fa-ellipsis-v-product" aria-hidden="true" onclick="toggleDropdownProduct(event, this)"></i>
                            <div class="dropdown-menu-product">
                                <a href="/staff/product/detail/${product.id}">Xem chi tiết</a>
                                <a href="/staff/product/view-update/${product.id}" >Chỉnh sửa</a>
                                <a onclick="getIdProduct(this)" class="delete-product" data-bs-toggle="modal" data-bs-target="#confirm-create-bill-modal" data-product-id="${product.id}">Xóa</a>
                            </div>
                        </div>
                    `}
                </td>


            `;

            tableBody.appendChild(row);
            // Thiết lập sự kiện cho checkbox
            const checkbox = row.querySelector('.select-row-product');
            checkbox.addEventListener('change', function() {
                const allChecked = document.querySelectorAll('.select-row-product:checked').length === document.querySelectorAll('.select-row-product').length;
                document.getElementById('select-all-product').checked = allChecked;
            });
        });
    }
    document.querySelectorAll('.select-row-product').forEach((checkbox) => {
        checkbox.addEventListener('change', function() {
            const allChecked = document.querySelectorAll('.select-row-product:checked').length === document.querySelectorAll('.select-row-product').length;
            document.getElementById('select-all-product').checked = allChecked;
            toggleSaveButton();
        });
    });
    initImageSlidersTable();
    initImageSlidersGridView();
    updatePaginationControls(totalPages, page);
}

// Khởi động hiển thị trang đầu tiên
fetchProductsByCategoryAndSearch(0, '');

function viewProductDetail(idProduct){
    window.location.href = "/staff/product/detail/" + idProduct
}


function updateProduct(idProduct) {
    const product = [];
    const rows = document.querySelectorAll('#productTable tbody tr');

    // Duyệt qua từng hàng trong bảng để lấy dữ liệu
    rows.forEach(row => {
        const codeProduct = row.cells[2].innerText.trim(); // Mô tả
        const nameProduct = row.cells[2].innerText.trim(); // Mô tả
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

// Đảm bảo thêm sự kiện cho tất cả các nút Xóa

function getIdProduct(element) {
    const productId = element.getAttribute('data-product-id');
    console.log(productId); // Kiểm tra ID có được lấy chính xác không

    const modal = document.getElementById('confirm-create-bill-modal');
    modal.setAttribute('data-product-id', productId);
}



async function updateStatus(id, status) {
    if (id === null){
        var modal = document.getElementById('confirm-create-bill-modal');
        id = modal.getAttribute('data-product-id');
    }

    // Gửi yêu cầu API để cập nhật trạng thái sản phẩm
    await fetch(`/product-api/restore?id=${id}&status=${status}`, {
        method: 'POST'
    });

    // Thực hiện các hành động tùy theo trạng thái
    if (status === 0) {
        createToast('1', 'Xóa sản phẩm thành công');
        fetchProductsByCategoryAndSearch(0, '');
    } else {
        createToast('1', 'Khôi phục sản phẩm thành công');
        fetchProductsByCategoryAndSearch(0, '', `/product-api/findProductDelete?idCategory=${0}&searchTerm=${''}`);
    }
}
