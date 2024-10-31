const buttons = document.querySelectorAll('.custom-btn');

// Thêm sự kiện click cho mỗi nút
buttons.forEach(button => {
    button.addEventListener('click', function() {
        // Xóa lớp 'active' khỏi tất cả các nút
        buttons.forEach(btn => btn.classList.remove('active'));
        // Thêm lớp 'active' vào nút được nhấn
        this.classList.add('active');
    });
});

