function toggleDropdownproductDetail(event, icon) {
    var menu = icon.nextElementSibling;

    // Kiểm tra và thay đổi trạng thái hiển thị menu
    menu.classList.toggle('show-productDetail');

    // Ngăn chặn sự kiện click lan ra ngoài
    event.stopPropagation();
}

// Đóng menu khi hover ra khỏi khu vực
document.querySelectorAll('.dropdown-productDetail').forEach(function (dropdown) {
    dropdown.addEventListener('mouseleave', function () {
        var menu = this.querySelector('.dropdown-menu-productDetail');
        if (menu.classList.contains('show-productDetail')) {
            menu.classList.remove('show-productDetail');
        }
    });
});

// Đóng menu khi click ra ngoài khu vực menu
window.onclick = function (event) {
    if (!event.target.matches('.fa-ellipsis-v-productDetail')) {
        var dropdowns = document.getElementsByClassName("dropdown-menu-productDetail");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
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
        const allChecked = document.querySelectorAll('.select-row-productDetail:checked').length === document.querySelectorAll('.select-row-productDetail').length;
        document.getElementById('select-all-productDetail').checked = allChecked;
        toggleEditableRow(checkbox);
        toggleSaveButton();
    });
});

function toggleEditableRow(checkbox) {
    const row = checkbox.closest('tr');
    const cells = row.querySelectorAll('td:not(:first-child):not(:last-child)');

    cells.forEach((cell) => {
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
    });
}


// Hiển thị/ẩn nút "Lưu lại" và "Hủy" dựa trên checkbox được chọn
function toggleSaveButton() {
    const anyChecked = document.querySelectorAll('.select-row-productDetail:checked').length > 0;
    document.getElementById('btn-save-productDetail').style.display = anyChecked ? "block" : "none";
    document.getElementById('btn-cancel-productDetail').style.display = anyChecked ? "block" : "none";
}

// Hủy bỏ chọn tất cả và đặt lại trạng thái các ô


const itemsPerPage = 10; // Display 5 products per page
let currentPage = 1;
let productDetails = [];
// Lấy thẻ p
var pElement = document.querySelector('p[data-value]');

// Lấy giá trị từ thuộc tính data-value
var idProduct = pElement.getAttribute('data-value');

console.log(idProduct)
document.querySelector('.search-input-productDetail').addEventListener('input', function () {
    const searchTerm = this.value;
    console.log('Search Term:', searchTerm); // Ghi lại từ khóa tìm kiếm
    console.log('ID Product:', idProduct); // Ghi lại ID sản phẩm

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

// Function: Display the current page
function displayPage(page) {
    const itemsPerPage = 10;
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

            row.innerHTML = `
                <td>
                    <input type="checkbox" class="select-row-productDetail"/>
                </td>
                <td data-column="codeproductDetail">${productDetail.color.nameColor}</td>
                <td data-column="nameproductDetail">${productDetail.size.nameSize}</td>
                <td data-column="material">${productDetail.price + 'đ'}</td>
                <td data-column="manufacturer">${productDetail.import_price + 'đ'}</td>
                <td data-column="origin">${productDetail.quantity}</td>
                <td data-column="sole">${productDetail.weight + 'g'}</td>
                <td data-column="describe">${productDetail.describe}</td>
                <td data-column="status">${productDetail.status == 1 ? 'Đang bán' : (productDetail.status == 2 ? 'Ngừng bán' : '')}</td>
                <td>
                    <div class="dropdown-productDetail">
                        <i class="fa fa-ellipsis-v fa-ellipsis-v-productDetail" aria-hidden="true" onclick="toggleDropdownproductDetail(event, this)"></i>
                        <div class="dropdown-menu-productDetail">
                            <a href="#">Xóa</a>
                        </div>
                    </div>
                </td>
            `;

            tableBody.appendChild(row);

            // Set up event for checkbox
            const checkbox = row.querySelector('.select-row-productDetail');
            checkbox.addEventListener('change', function () {
                toggleEditableRow(checkbox);
                const allChecked = document.querySelectorAll('.select-row-productDetail:checked').length === document.querySelectorAll('.select-row-productDetail').length;
                document.getElementById('select-all-productDetail').checked = allChecked;
            });
        });
    } else {
        // Handle case when no product details are available
        tableBody.innerHTML = '<tr><td colspan="10">No products found.</td></tr>';
    }

    updatePaginationControls(totalPages, page);
}

// Function: Update pagination controls
function updatePaginationControls(totalPages, page) {
    const pagination = document.getElementById('pagination-productDetail');
    pagination.innerHTML = '';
    const itemsPerPage =10;
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
