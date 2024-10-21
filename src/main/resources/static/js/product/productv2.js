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
    });
}

// Hàm cập nhật trạng thái checkbox "chọn tất cả" nếu tất cả các checkbox hàng đều được chọn
document.querySelectorAll('.select-row-product').forEach((checkbox) => {
    checkbox.addEventListener('change', function() {
        const allChecked = document.querySelectorAll('.select-row-product:checked').length === document.querySelectorAll('.select-row-product').length;
        document.getElementById('select-all-product').checked = allChecked;
    });
});
