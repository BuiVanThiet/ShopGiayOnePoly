


function toggleDropdownproductDetail(event, icon) {
    let menu = icon.nextElementSibling;

    // Kiểm tra và thay đổi trạng thái hiển thị menu
    menu.classList.toggle('show-productDetail');

    // Ngăn chặn sự kiện click lan ra ngoài
    event.stopPropagation();
}
function cancelButton(){
    fetchProductDetails('', idProduct);
    document.getElementById('select-all-productDetail').checked = false;
    document.getElementById('btn-save-productDetail').style.display = "none";
    document.getElementById('btn-export-productDetail').style.display = "none";
    document.getElementById('btn-saveQR-productDetail').style.display = "none";
    document.getElementById('btn-cancel-productDetail').style.display = "none";
}
// Đóng menu khi hover ra khỏi khu vực
document.querySelectorAll('.dropdown-productDetail').forEach(function (dropdown) {
    dropdown.addEventListener('mouseleave', function () {
        let menu = this.querySelector('.dropdown-menu-productDetail');
        if (menu.classList.contains('show-productDetail')) {
            menu.classList.remove('show-productDetail');
        }
    });
});

// Đóng menu khi click ra ngoài khu vực menu
window.onclick = function (event) {
    if (!event.target.matches('.fa-ellipsis-v-productDetail')) {
        let dropdowns = document.getElementsByClassName("dropdown-menu-productDetail");
        for (let i = 0; i < dropdowns.length; i++) {
            let openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show-productDetail')) {
                openDropdown.classList.remove('show-productDetail');
            }
        }
    }
}

// Hàm chọn tất cả checkbox
function toggleSelectAllproductDetail(selectAllCheckbox) {
    // Chọn tất cả các checkbox trong phần tbody của bảng sản phẩm
    const checkboxes = document.querySelectorAll('#productDetail-table-body .select-row-productDetail');

    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAllCheckbox.checked; // Đánh dấu hoặc bỏ đánh dấu checkbox
        toggleEditableRow(checkbox); // Cập nhật hàng theo trạng thái checkbox
    });

    toggleSaveButton(); // Cập nhật nút lưu nếu cần thiết
}


document.querySelectorAll('.select-row-productDetail').forEach((checkbox) => {
    checkbox.addEventListener('change', function () {
        document.getElementById('select-all-productDetail').checked =
            document.querySelectorAll('.select-row-productDetail:checked').length === document.querySelectorAll('.select-row-productDetail').length;
        toggleEditableRow(checkbox);
        toggleSaveButton();
    });
});

function toggleEditableRow(checkbox) {
    const row = checkbox.closest('tr');
    const cells = row.querySelectorAll('td'); // Lấy tất cả các ô trong hàng

    cells.forEach((cell, index) => {
        // Chỉ áp dụng chỉnh sửa cho các cột 4, 5, 6, 7, và 8 (index bắt đầu từ 0)
        if (index >= 3 && index <= 7) {
            if (checkbox.checked) {
                if (!cell.hasAttribute('data-original-value')) {
                    cell.setAttribute('data-original-value', cell.innerHTML); // Lưu lại HTML ban đầu
                }
                cell.contentEditable = "true";
                cell.style.backgroundColor = "#eef2ff";
            } else {
                if (cell.hasAttribute('data-original-value')) {
                    cell.innerHTML = cell.getAttribute('data-original-value'); // Khôi phục HTML ban đầu
                    cell.removeAttribute('data-original-value');
                }
                cell.contentEditable = "false";
                cell.style.backgroundColor = "";
            }
        }
    });
}


// Hiển thị/ẩn nút "Lưu lại" và "Hủy" dựa trên checkbox được chọn
function toggleSaveButton() {
    const anyChecked = document.querySelectorAll('.select-row-productDetail:checked').length > 0;
    document.getElementById('btn-save-productDetail').style.display = anyChecked ? "block" : "none";
    document.getElementById('btn-export-productDetail').style.display = anyChecked ? "block" : "none";
    document.getElementById('btn-saveQR-productDetail').style.display = anyChecked ? "block" : "none";
    document.getElementById('btn-cancel-productDetail').style.display = anyChecked ? "block" : "none";
}

// Hủy bỏ chọn tất cả và đặt lại trạng thái các ô
function updateItemsPerPage(event) {
    if (parseInt(event.target.value) === 25) {
        itemsPerPage = productDetails.length;
    } else {
        itemsPerPage = parseInt(event.target.value);
    }
    displayPage(currentPage); // Hiển thị lại trang hiện tại với số lượng mới
    // Bỏ chọn tất cả các checkbox khi chuyển trang
    const selectAllCheckbox = document.getElementById('select-all-productDetail');
    selectAllCheckbox.checked = false; // Bỏ chọn checkbox "tất cả"

    const checkboxes = document.querySelectorAll('.select-row-productDetail');
    checkboxes.forEach((checkbox) => {
        checkbox.checked = false; // Bỏ chọn tất cả các checkbox của sản phẩm
    });
}


let itemsPerPage = 10;
let currentPage = 1;
let productDetails = [];
// Lấy thẻ p
let pElement = document.querySelector('p[data-value]');

// Lấy giá trị từ thuộc tính data-value
let idProduct = pElement.getAttribute('data-value');

document.querySelector('.search-input-productDetail').addEventListener('input', function () {
    const searchTerm = this.value;
    fetchProductDetails(searchTerm, idProduct);
});


// Function: Fetch and display data based on search term
function fetchProductDetails(searchTerm, idProduct) {
    fetch(`/product-api/detail/search?searchTerm=${searchTerm}&idProduct=${idProduct}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            productDetails = data; // Ensure productDetails is populated
            currentPage = 1; // Reset current page to the first
            displayPage(currentPage); // Display the new data
        })
        .catch(error => console.error('Error:', error));
}
function formatPrice(price) {
    // Đảm bảo giá là một số, nếu không trả về 0
    const parsedPrice = parseFloat(price);
    if (isNaN(parsedPrice)) return '0đ';

    // Định dạng giá theo kiểu tiền tệ Việt Nam
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(parsedPrice).replace('₫', 'đ');
}

function displayPage(page) {
    const totalPages = Math.ceil(productDetails.length / itemsPerPage);
    const start = (page - 1) * itemsPerPage;
    const end = start + itemsPerPage;

    const tableBody = document.getElementById('productDetail-table-body');
    tableBody.innerHTML = '';

    // Check if productDetails is an array and has content
    if (Array.isArray(productDetails) && productDetails.length > 0) {
        productDetails.slice(start, end).forEach(productDetail => {
            const row = document.createElement('tr');
            row.id = `row-product-${productDetail.id}`;
            row.dataset.id = productDetail.id;
            row.dataset.nameColor = productDetail.color.nameColor;
            row.dataset.nameSize = productDetail.size.nameSize;

            row.innerHTML = `
                <td>
                    <input type="checkbox" class="select-row-productDetail"/>
                </td>
                <td data-column="color">${productDetail.color.nameColor}</td>
                <td data-column="size">${productDetail.size.nameSize}</td>
                <td data-column="price">${formatPrice(productDetail.price)}</td>
                <td data-column="importPrice">${formatPrice(productDetail.import_price)}</td>
                <td data-column="quantity">${productDetail.quantity}</td>
                <td data-column="weight">${productDetail.weight + 'g'}</td>
                <td data-column="describe">${productDetail.describe}</td>
                <td data-column="status">${productDetail.status == 1 ? 'Đang bán' : (productDetail.status == 2 ? 'Ngừng bán' : '')}</td>
                <td>
                    <div class="dropdown-productDetail">
                        <i class="fa fa-ellipsis-v fa-ellipsis-v-productDetail" aria-hidden="true" onclick="toggleDropdownproductDetail(event, this)"></i>
                        <div class="dropdown-menu-productDetail">
                            <a href="#">Xóa</a>
                            <a href="#" onclick="downloadQRCode(${productDetail.id}, '${productDetail.color.nameColor}', '${productDetail.size.nameSize}')">Lưu QR</a>
                        </div>
                    </div>
                </td>
            `;

            tableBody.appendChild(row);

            // Set up event for checkbox
            const checkbox = row.querySelector('.select-row-productDetail');
            checkbox.addEventListener('change', function() {
                toggleEditableRow(checkbox);
                document.getElementById('select-all-productDetail').checked =
                    document.querySelectorAll('.select-row-productDetail:checked').length === document.querySelectorAll('.select-row-productDetail').length;
            });

        });
    } else {
        // Handle case when no product details are available
        tableBody.innerHTML = '<tr><td colspan="10">No products found.</td></tr>';
    }
    document.querySelectorAll('.select-row-productDetail').forEach((checkbox) => {
        checkbox.addEventListener('change', function() {
            toggleEditableRow(checkbox);
            toggleSaveButton();
            document.getElementById('select-all-productDetail').checked =
                document.querySelectorAll('.select-row-productDetail:checked').length === document.querySelectorAll('.select-row-productDetail').length;
        });
    });
    updatePaginationControls(totalPages, page);
}

// Function: Update pagination controls
function updatePaginationControls(totalPages, page) {
    const pagination = document.getElementById('pagination-productDetail');
    pagination.innerHTML = '';
    // Các nút phân trang tiếp tục như trong code gốc
    // Tạo nút "Trang trước"
    const prevButton = document.createElement('button');
    prevButton.innerText = '<';
    prevButton.onclick = () => changePage(page - 1);
    prevButton.disabled = page === 1;
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
            pageButton.style.backgroundColor = '#fceb97';
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
    nextButton.innerText = '>';
    nextButton.onclick = () => changePage(page + 1);
    nextButton.disabled = page === totalPages;
    pagination.appendChild(nextButton);
}

// Function: Change page
function changePage(newPage) {
    const itemsPerPage = 10;
    const totalPages = Math.ceil(productDetails.length / itemsPerPage);

    if (newPage >= 1 && newPage <= totalPages) {
        currentPage = newPage;
        displayPage(currentPage);

        // Bỏ chọn tất cả các checkbox khi chuyển trang
        const selectAllCheckbox = document.getElementById('select-all-productDetail');
        selectAllCheckbox.checked = false; // Bỏ chọn checkbox "tất cả"

        const checkboxes = document.querySelectorAll('.select-row-productDetail');
        checkboxes.forEach((checkbox) => {
            checkbox.checked = false; // Bỏ chọn tất cả các checkbox của sản phẩm
        });
    }
}

// Initialize display on page load
fetchProductDetails('', idProduct);


function downloadQRCode(productDetailId, nameColor, nameSize) {
    // Kiểm tra nếu ID không hợp lệ
    if (!productDetailId || typeof productDetailId !== 'number' && typeof productDetailId !== 'string') {
        console.error('Invalid product detail ID:', productDetailId);
        return;
    }

    // Đảm bảo ID là chuỗi
    const dataToEncode = String(productDetailId);

    // Tạo mã QR
    QRCode.toDataURL(`{"id":"${dataToEncode}"}`, { width: 400, height: 400 }, function (err, url) {
        if (err) {
            console.error('Error generating QR code:', err);
            return;
        }

        // Tạo liên kết để tải mã QR
        const a = document.createElement('a');
        a.href = url;
        a.download = `QRCode_${nameColor}_${nameSize}.png`;  // Đặt tên cho file tải về
        document.body.appendChild(a);
        a.click();  // Tự động nhấn vào liên kết để tải ảnh xuống
        document.body.removeChild(a);  // Xóa liên kết sau khi tải về
    });
}
function downloadAllQRCode() {
    // Lấy tất cả các checkbox đã được chọn
    const selectedRows = document.querySelectorAll('.select-row-productDetail:checked');

    // Duyệt qua từng checkbox và lấy ID sản phẩm chi tiết
    selectedRows.forEach(row => {
        // Tìm hàng cha (tr từ checkbox)
        const parentRow = row.closest('tr');
        if (!parentRow) return;

        // Lấy ID sản phẩm chi tiết từ data attribute hoặc một cột ẩn
        const productDetailId = parentRow.dataset.id;
        const nameColor = parentRow.dataset.nameColor;
        const nameSize = parentRow.dataset.nameSize;
        if (productDetailId) {
            // Gọi hàm tải mã QR cho từng sản phẩm
            downloadQRCode(productDetailId, nameColor, nameSize);
            fetchProductDetails('', idProduct);
            document.getElementById('btn-save-productDetail').style.display = "none";
            document.getElementById('btn-export-productDetail').style.display = "none";
            document.getElementById('btn-saveQR-productDetail').style.display = "none";
            document.getElementById('btn-cancel-productDetail').style.display = "none";
            document.getElementById('select-all-productDetail').checked = false;
        } else {
            console.error('Không tìm thấy ID sản phẩm chi tiết cho dòng:', parentRow);
        }
    });
}

async function updateSelectedProductDetails() {
    // Lấy tất cả các checkbox đã được chọn
    const selectedRows = document.querySelectorAll('.select-row-productDetail:checked');

    // Tạo danh sách productDetail cần cập nhật
    const productDetailsToUpdate = [];

    selectedRows.forEach(row => {
        // Tìm hàng cha (tr) của checkbox
        const parentRow = row.closest('tr');

        if (!parentRow) return;

        // Lấy dữ liệu từ các cột (nếu cần tùy chỉnh thêm, sửa lại phần này)
        const productDetail = {
            id: parseInt(parentRow.dataset.id, 10), // Đảm bảo id là kiểu số (Long)
            price: parseFloat(parentRow.querySelector('[data-column="price"]').textContent.trim().replace('đ', '').replaceAll('.', '')),
            import_price: parseFloat(parentRow.querySelector('[data-column="importPrice"]').textContent.trim().replace('đ', '').replaceAll('.', '')),
            quantity: parseInt(parentRow.querySelector('[data-column="quantity"]').textContent.trim(), 10),
            weight: parseFloat(parentRow.querySelector('[data-column="weight"]').textContent.trim().replace('g', '')),
            describe: parentRow.querySelector('[data-column="describe"]').textContent.trim(),
        };

        productDetailsToUpdate.push(productDetail);
    });

    // Gửi dữ liệu đến API
    const response = await fetch('/staff/product/update-multiple/product-detail', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(productDetailsToUpdate)
    });
    if (response.ok) {
        const result = await response.json();
        createToast(result.check, result.message);
        fetchProductDetails('', idProduct);
        document.getElementById('btn-save-productDetail').style.display = "none";
        document.getElementById('btn-export-productDetail').style.display = "none";
        document.getElementById('btn-saveQR-productDetail').style.display = "none";
        document.getElementById('btn-cancel-productDetail').style.display = "none";
        document.getElementById('select-all-productDetail').checked = false;
    }

}

function exportExcelProductDetail() {
    // Lấy các checkbox được chọn
    const selectedRows = document.querySelectorAll('.select-row-productDetail:checked');
    const data = [];

    selectedRows.forEach(row => {
        const tr = row.closest('tr');
        const rowData = {
            "Màu sắc": tr.querySelector('[data-column="color"]').textContent,
            "Kích cỡ": tr.querySelector('[data-column="size"]').textContent,
            "Giá bán": tr.querySelector('[data-column="price"]').textContent,
            "Giá nhập": tr.querySelector('[data-column="importPrice"]').textContent,
            "Số lượng": tr.querySelector('[data-column="quantity"]').textContent,
            "Trọng lượng": tr.querySelector('[data-column="weight"]').textContent,
            "Mô tả": tr.querySelector('[data-column="describe"]').textContent
        };
        data.push(rowData);
    });

    if (data.length > 0) {
        // Tạo bảng Excel từ dữ liệu
        const ws = XLSX.utils.json_to_sheet(data);

        // Tạo workbook
        const wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, 'Sản phẩm chi tiết');

        // Xuất file Excel
        XLSX.writeFile(wb, `Chi tiết ${productDetails[0].product.nameProduct}.xlsx`);
        createToast('1', 'Xuất dữ liệu đã chọn ra Excel thành công');
    } else {
        createToast('2', 'Vui lòng chọn ít nhất một sản phẩm để xuất Excel.');
    }
}
