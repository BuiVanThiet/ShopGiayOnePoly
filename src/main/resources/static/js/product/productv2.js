function toggleDropdown(event, icon) {
    var menu = icon.nextElementSibling;

    // Kiểm tra và thay đổi trạng thái hiển thị menu
    menu.classList.toggle('show');

    // Ngăn chặn sự kiện click lan ra ngoài
    event.stopPropagation();
}

// Đóng menu khi hover ra khỏi khu vực
document.querySelectorAll('.dropdown').forEach(function(dropdown) {
    dropdown.addEventListener('mouseleave', function() {
        var menu = this.querySelector('.dropdown-menu');
        if (menu.classList.contains('show')) {
            menu.classList.remove('show');
        }
    });
});


// Đóng menu khi click ra ngoài khu vực menu
window.onclick = function(event) {
    if (!event.target.matches('.fa-ellipsis-v')) {
        var dropdowns = document.getElementsByClassName("dropdown-menu");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
}
function showGridView() {
    document.querySelector('.form-group').style.display = 'flex';
    document.querySelector('.table').style.display = 'none';
    document.querySelector('.fa-th').style.color = 'blue';
    document.querySelector('.fa-list').style.color = 'black';
}

function showListView() {
    document.querySelector('.form-group').style.display = 'none';
    document.querySelector('.table').style.display = 'table';
    document.querySelector('.fa-th').style.color = 'black';
    document.querySelector('.fa-list').style.color = 'blue';
}
// Hàm chọn tất cả checkbox
function toggleSelectAll(selectAllCheckbox) {
    const checkboxes = document.querySelectorAll('.select-row');
    checkboxes.forEach((checkbox) => {
        checkbox.checked = selectAllCheckbox.checked;
    });
}

// Hàm cập nhật trạng thái checkbox "chọn tất cả" nếu tất cả các checkbox hàng đều được chọn
document.querySelectorAll('.select-row').forEach((checkbox) => {
    checkbox.addEventListener('change', function() {
        const allChecked = document.querySelectorAll('.select-row:checked').length === document.querySelectorAll('.select-row').length;
        document.getElementById('select-all').checked = allChecked;
    });
});
