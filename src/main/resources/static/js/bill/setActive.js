function setActive(element, value) {
    // Xóa lớp active khỏi tất cả các liên kết
    var links = document.querySelectorAll('.nav-link-custom');
    links.forEach(function(link) {
        link.classList.remove('active');
    });

    // Thêm lớp active vào liên kết được nhấp vào
    element.classList.add('active');
    clickStatusBillManager(parseInt(value));
    // Xử lý giá trị được truyền vào (value)
    console.log("Giá trị được truyền vào:", value);

    // Bạn có thể làm gì đó với giá trị 'value' ở đây, ví dụ: gọi API hoặc thay đổi nội dung trang
}
