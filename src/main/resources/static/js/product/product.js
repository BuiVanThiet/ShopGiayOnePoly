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

function editRow(index) {
    // Mở rộng div nếu đang thu gọn
    var content = document.getElementById("collapsibleDiv");
    if (content.style.display === "none" || content.style.display === "") {
        content.style.display = "block";
        document.getElementById("arrow").style.transform = "rotate(180deg)";
    }

    // Lấy id của sản phẩm từ data-id
    var id = $('#row-' + index).data('id');

    fetch('http://localhost:8080/staff/product/get-one/' + id)
        .then(response => {
            if (!response.ok) {
                throw new Error('Sản phẩm không tìm thấy');
            }
            return response.json();
        })
        .then(data => {
            // Cập nhật dữ liệu vào các ô input và select
            document.getElementById("codeProductInput").value = data.codeProduct;
            document.getElementById("nameProductInput").value = data.nameProduct;
            document.getElementById("material").value = data.material.id;
            document.getElementById("manufacturer").value = data.manufacturer.id;
            document.getElementById("origin").value = data.origin.id;
            document.getElementById("sole").value = data.sole.id;
        })
        .catch(error => {
            console.error('Lỗi khi lấy dữ liệu sản phẩm:', error);
        });
}

// Lắng nghe sự kiện "input" khi người dùng nhập vào ô input codeProduct
// Lắng nghe sự kiện "input" khi người dùng nhập vào ô input codeProduct
document.getElementById('codeProductInput').addEventListener('input', function() {
    var codeProduct = this.value;

    if (codeProduct.trim() !== "") {  // Kiểm tra nếu codeProduct không rỗng
        // Gửi yêu cầu kiểm tra mã sản phẩm tồn tại
        fetch('http://localhost:8080/staff/product/check-code/' + codeProduct)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Sản phẩm không tìm thấy');
                }
                return response.json();
            })
            .then(data => {
                var btnAdd = document.getElementById('product-btn-add');

                if (data === true) {
                    // Nếu mã sản phẩm đã tồn tại, thay đổi nút "Thêm sản phẩm" thành "Sửa sản phẩm"
                    btnAdd.textContent = 'Sửa sản phẩm';
                    btnAdd.classList.remove('product-btn-add');
                    btnAdd.classList.add('product-btn-edit');
                } else {
                    // Nếu mã sản phẩm không tồn tại, thay đổi lại nút thành "Thêm sản phẩm"
                    btnAdd.textContent = 'Thêm sản phẩm';
                    btnAdd.classList.remove('product-btn-edit');
                    btnAdd.classList.add('product-btn-add');
                }
            })
            .catch(error => {
                console.error('Lỗi khi lấy dữ liệu sản phẩm:', error);
            });
    }
});

