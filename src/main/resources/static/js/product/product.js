function toggleDropdownProduct(event, icon) {
    let menu = icon.nextElementSibling;

    // Kiểm tra và thay đổi trạng thái hiển thị menu
    menu.classList.toggle('show-product');

    // Ngăn chặn sự kiện click lan ra ngoài
    event.stopPropagation();
}
// Đóng menu khi hover ra khỏi khu vực
document.querySelectorAll('.dropdown-product').forEach(function(dropdown) {
    dropdown.addEventListener('mouseleave', function() {
        let menu = this.querySelector('.dropdown-menu-product');
        if (menu.classList.contains('show-product')) {
            menu.classList.remove('show-product');
        }
    });
});
// Đóng menu khi click ra ngoài khu vực menu
window.onclick = function(event) {
    if (!event.target.matches('.fa-ellipsis-v-product')) {
        let dropdowns = document.getElementsByClassName("dropdown-menu-product");
        for (let i = 0; i < dropdowns.length; i++) {
            let openDropdown = dropdowns[i];
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
        document.getElementById('select-all-product').checked =
            document.querySelectorAll('.select-row-product:checked').length === document.querySelectorAll('.select-row-product').length;
        toggleSaveButton();
    });
});
// Hàm cập nhật hiển thị các nút dựa vào checkbox được chọn
function toggleSaveButton() {
    const anyChecked = document.querySelectorAll('.select-row-product:checked').length > 0;
    if (document.getElementById('btn-findProduct-delete').style.display !== 'none'){
        document.getElementById('btn-export-product').style.display = anyChecked ? "block" : "none";
        document.getElementById('btn-delete-product').style.display = anyChecked ? "block" : "none";
        document.getElementById('btn-cancel-product').style.display = anyChecked ? "block" : "none";
    } else {
        document.getElementById('btn-export-product').style.display = anyChecked ? "block" : "none";
        document.getElementById('btn-restore-product').style.display = anyChecked ? "block" : "none";
        document.getElementById('btn-cancel-product').style.display = anyChecked ? "block" : "none";
    }

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
        }, 5000); // Thay đổi sau mỗi 10 giây
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
        }, 5000); // Thay đổi sau mỗi 7 giây
    });
}
initImageSlidersTable();
// Sự kiện khi người dùng thay đổi danh mục
document.querySelector('#search-select-product').addEventListener('change', async function () {
    const idCategory = this.value;
    const searchTerm = document.querySelector('.search-input-product').value;
    await fetchProductsByCategoryAndSearch(idCategory, searchTerm);
});
// Sự kiện khi người dùng nhập vào ô tìm kiếm
document.querySelector('.search-input-product').addEventListener('input', async function () {
    const searchTerm = this.value;
    const idCategory = document.querySelector('#search-select-product').value;
    await fetchProductsByCategoryAndSearch(idCategory, searchTerm);
});
// Hàm tìm kiếm sản phẩm theo danh mục và ô input
async function fetchProductsByCategoryAndSearch(idCategory, searchTerm, url = `/product-api/search`) {
    document.getElementById('select-all-product').checked
        ? document.getElementById('select-all-product').checked = false
        : null;
    if (url === `/product-api/search`) {
        if (searchTerm === '' || idCategory === 0){
            searchTerm = document.querySelector('.search-input-product').value;
            idCategory = document.querySelector('#search-select-product').value;
        }
        url += `?idCategory=${idCategory}&searchTerm=${searchTerm}`;
        document.getElementById('btn-findProduct-delete').style.display = 'block';
        document.getElementById('btn-findProduct-active').style.display = 'none';
    } else {
        if (searchTerm === '' || idCategory === 0){
            searchTerm = document.querySelector('.search-input-product').value;
            idCategory = document.querySelector('#search-select-product').value;
        }
        url = `/product-api/findProductDelete?idCategory=${idCategory}&searchTerm=${searchTerm}`
        document.getElementById('btn-findProduct-delete').style.display = 'none';
        document.getElementById('btn-findProduct-active').style.display = 'block';
    }
    await fetch(url)
        .then(response => response.json())
        .then(data => {
            if (data === null) {

            } else {
                products = data;
                currentPage = 1; // Đặt lại trang hiện tại là trang đầu tiên
                displayPage(currentPage); // Hiển thị lại trang với dữ liệu mới
            }

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
let nextButton = document.createElement('button');
let prevButton = document.createElement('button');
function updatePaginationControls(totalPages, page) {
    const pagination = document.getElementById('pagination-product');
    pagination.innerHTML = '';
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

    nextButton.innerHTML = '<i class="fas fa-angle-right"></i>';

    nextButton.onclick = () => changePage(page + 1);
    nextButton.style.display = page === totalPages ? 'none' : 'inline-block';
    pagination.appendChild(nextButton);
    if (products.length === 0) {
        nextButton.style.display = 'none';
        prevButton.style.display = 'none';
    }
}
let itemsPerPageList = 10; // Chế độ danh sách: 5 sản phẩm mỗi trang
let itemsPerPageGrid = 12; // Chế độ lưới: 12 sản phẩm mỗi trang
let currentPage = 1;
let products = []; // Mảng chứa các sản phẩm từ API
let isGridView = false; // Xác định chế độ hiện tại (false = danh sách, true = lưới)
function updateItemsPerPage(event) {
    if (parseInt(event.target.value) === 25) {
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
        document.getElementById('btn-restore-product').style.display = 'none';
        document.getElementById('btn-cancel-product').style.display = 'none';
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
        if (products.length === 0) {
            gridContainer.innerHTML += `<h3>Không tìm thấy sản phẩm nào</h3>`
        } else {
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
        }
    } else {
        if (products.length === 0) {
            document.getElementById('nullProduct').style.display = 'block';
        } else {
            document.getElementById('nullProduct').style.display = 'none';
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
                        <i onclick="getIdProductRestore(this)" id="restore-product" class="fa fa-undo fa-restore-icon" aria-hidden="true" data-bs-toggle="modal" data-bs-target="#confirm-restoreOne-product-modal" data-product-id="${product.id}" title="Khôi phục"></i>
                    ` : `
                        <!-- Dropdown menu khi status khác 0 -->
                        <div class="dropdown-product">
                            <i class="fa fa-ellipsis-v fa-ellipsis-v-product" aria-hidden="true" onclick="toggleDropdownProduct(event, this)"></i>
                            <div class="dropdown-menu-product">
                                <a href="/staff/product/detail/${product.id}">Xem chi tiết</a>
                                <a href="/staff/product/view-update/${product.id}" >Chỉnh sửa</a>
                                <a onclick="getIdProductDelete(this)" class="delete-product" data-bs-toggle="modal" data-bs-target="#confirm-create-bill-modal" data-product-id="${product.id}">Xóa</a>
                            </div>
                        </div>
                    `}
                </td>


            `;

                tableBody.appendChild(row);
                // Thiết lập sự kiện cho checkbox
                const checkbox = row.querySelector('.select-row-product');
                checkbox.addEventListener('change', function () {
                    document.getElementById('select-all-product').checked =
                        document.querySelectorAll('.select-row-product:checked').length === document.querySelectorAll('.select-row-product').length;
                });
            });
        }
    }
    document.querySelectorAll('.select-row-product').forEach((checkbox) => {
        checkbox.addEventListener('change', function() {
            document.getElementById('select-all-product').checked =
                document.querySelectorAll('.select-row-product:checked').length === document.querySelectorAll('.select-row-product').length;
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
function getIdProductDelete(element) {
    const productId = element.getAttribute('data-product-id');
    const modal = document.getElementById('confirm-create-bill-modal');
    modal.setAttribute('data-product-id', productId);
}
function getIdProductRestore(element) {
    const productId = element.getAttribute('data-product-id');
    const modal = document.getElementById('confirm-restoreOne-product-modal');
    modal.setAttribute('data-product-id', productId);
}
async function updateStatus(id, status) {
    if (id === null){
        let modal = document.getElementById('confirm-create-bill-modal');
        id = modal.getAttribute('data-product-id');
    } else {
        let modal = document.getElementById('confirm-restoreOne-product-modal');
        id = modal.getAttribute('data-product-id');
    }

    // Gửi yêu cầu API để cập nhật trạng thái sản phẩm
    await fetch(`/product-api/restore?id=${id}&status=${status}`, {
        method: 'POST'
    });

    // Thực hiện các hành động tùy theo trạng thái
    if (status === 0) {
        createToast('1', 'Xóa sản phẩm thành công');
        await fetchProductsByCategoryAndSearch(0, '');
    } else {
        createToast('1', 'Khôi phục sản phẩm thành công');
        await fetchProductsByCategoryAndSearch(0, '', `/product-api/findProductDelete?idCategory=${0}&searchTerm=${''}`);
    }
}
async function deleteMultipleProduct() {
    // Lấy các checkbox được chọn
    const selectedRows = document.querySelectorAll('.select-row-product:checked');
    const productIds = Array.from(selectedRows).map(row => {
        const tr = row.closest('tr');
        return parseInt(tr.dataset.id, 10);
    });

    if (productIds.length > 0) {
        try {
            // Gửi yêu cầu xóa sản phẩm
            await fetch('/product-api/delete-multiple', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(productIds),
            });

            // Tải lại danh sách sản phẩm sau khi xóa
            await fetchProductsByCategoryAndSearch(0, '');

            // Thông báo thành công
            createToast('1', 'Xóa sản phẩm thành công');
        } catch (error) {
            // Thông báo lỗi nếu có
            createToast('2', 'Đã xảy ra lỗi khi xóa sản phẩm.');
            console.error('Lỗi khi xóa sản phẩm:', error);
        }
    } else {
        createToast('2', 'Vui lòng chọn ít nhất một sản phẩm để xóa.');
    }
}
async function restoreMultipleProduct() {
    // Lấy các checkbox được chọn
    const selectedRows = document.querySelectorAll('.select-row-product:checked');
    const productIds = Array.from(selectedRows).map(row => {
        const tr = row.closest('tr');
        return parseInt(tr.dataset.id, 10);
    });

    if (productIds.length > 0) {
        try {
            // Gửi yêu cầu xóa sản phẩm
            await fetch('/product-api/restore-multiple', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(productIds),
            });

            // Tải lại danh sách sản phẩm sau khi xóa
            await fetchProductsByCategoryAndSearch(0, '');

            // Thông báo thành công
            createToast('1', 'Khôi phục sản phẩm thành công');
        } catch (error) {
            // Thông báo lỗi nếu có
            createToast('2', 'Đã xảy ra lỗi khi khôi phục sản phẩm.');
        }
    } else {
        createToast('2', 'Vui lòng chọn ít nhất một sản phẩm để khôi phục.');
    }
}
function cancelButton(){
    const selectedRows = document.querySelectorAll('.select-row-product:checked');
    selectedRows.forEach(row => {
        row.checked = false;
    });
    document.getElementById('select-all-product').checked = false;
    document.getElementById('btn-export-product').style.display = "none";
    document.getElementById('btn-delete-product').style.display = "none";
    document.getElementById('btn-cancel-product').style.display = "none";
    document.getElementById('btn-restore-product').style.display = "none";
}

function exportExcelProduct() {
    // Lấy các checkbox được chọn
    const selectedRows = document.querySelectorAll('.select-row-product:checked');
    const data = [];

    selectedRows.forEach(row => {
        const tr = row.closest('tr');
        const rowData = {
            "Mã sản phẩm": tr.querySelector('[data-column="codeProduct"]').textContent,
            "Tên sản phẩm": tr.querySelector('[data-column="nameProduct"]').textContent,
            "Chất liệu": tr.querySelector('[data-column="material"]').textContent,
            "Nhà sản xuất": tr.querySelector('[data-column="manufacturer"]').textContent,
            "Xuất xứ": tr.querySelector('[data-column="origin"]').textContent,
            "Loại đế": tr.querySelector('[data-column="sole"]').textContent,
            "Mô tả": tr.querySelector('[data-column="describe"]').textContent,
            "Trạng thái": tr.querySelector('[data-column="status"]').textContent,
        };
        data.push(rowData);
    });

    if (data.length > 0) {
        // Tạo bảng Excel từ dữ liệu
        const ws = XLSX.utils.json_to_sheet(data);

        // Tạo workbook
        const wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, 'Sản phẩm');

        // Xuất file Excel
        XLSX.writeFile(wb, 'products.xlsx');
        createToast('1', 'Xuất dữ liệu đã chọn ra Excel thành công');
    } else {
        createToast('2', 'Vui lòng chọn ít nhất một sản phẩm để xuất Excel.');
    }
}
