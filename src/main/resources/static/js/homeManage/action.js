function toggleSubmenu(event) {
    event.preventDefault(); // Ngăn chặn chuyển hướng

    // Lấy tất cả các menu con
    const allSubmenus = document.querySelectorAll('.submenu');

    // Đóng tất cả các menu con khác
    allSubmenus.forEach(submenu => {
        if (submenu !== event.currentTarget.nextElementSibling) {
            submenu.classList.remove("open");
        }
    });

    // Chuyển trạng thái của menu con được nhấn
    const submenu = event.currentTarget.nextElementSibling;
    submenu.classList.toggle("open"); // Thêm hoặc gỡ lớp "open" để hiển thị
}
