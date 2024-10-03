function editRow(index) {
    console.log('Editing row:', index);
    document.getElementById('code-input-' + index).style.display = 'inline-block';
    document.getElementById('name-input-' + index).style.display = 'inline-block';

    document.getElementById('code-text-' + index).style.display = 'none';
    document.getElementById('name-text-' + index).style.display = 'none';


    document.getElementById('edit-btn-' + index).style.display = 'none';
    document.getElementById('save-btn-' + index).style.display = 'inline-block';
}


function toggleDiv() {
    var content = document.getElementById("collapsibleDiv");
    var arrow = document.getElementById("arrow");

    if (content.style.display === "none" || content.style.display === "") {
        content.style.display = "block"; // Mở rộng nội dung
        arrow.style.transform = "rotate(180deg)"; // Xoay mũi tên hướng lên
    } else {
        content.style.display = "none"; // Thu gọn nội dung
        arrow.style.transform = "rotate(0deg)"; // Đặt lại mũi tên hướng xuống
    }
}


