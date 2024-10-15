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
            document.getElementById("codeProduct").value = data.codeProduct;
            document.getElementById("nameProduct").value = data.nameProduct;
            document.getElementById("material").value = data.material.id;
            document.getElementById("manufacturer").value = data.manufacturer.id;
            document.getElementById("origin").value = data.origin.id;
            document.getElementById("sole").value = data.sole.id;
            document.getElementById("describeProduct").value = data.describe;

            // Tự động chọn các ô checkbox
            const selectedCategories = data.categories; // Giả sử đây là mảng chứa ID của các danh mục
            const checkboxes = document.querySelectorAll('input[name="categories"]');

            checkboxes.forEach(checkbox => {
                if (selectedCategories.includes(parseInt(checkbox.value))) {
                    checkbox.checked = true;
                } else {
                    checkbox.checked = false;
                }
            });

            // Tạo event input để trigger các sự kiện khác nếu cần
            var event = new Event('input', { bubbles: true });
            document.getElementById("codeProduct").dispatchEvent(event);

            // Cuộn lên đầu trang
            window.scrollTo({
                top: 0,
                left: 0,
                behavior: 'smooth'
            });
        })
        .catch(error => {
            console.error('Lỗi khi tải dữ liệu sản phẩm:', error);
        });
}



document.getElementById('codeProduct').addEventListener('input', function() {
    var codeProduct = this.value;

    if (codeProduct.trim() !== "") {  // Kiểm tra nếu codeProduct không rỗng
        // Gửi yêu cầu kiểm tra mã sản phẩm tồn tại
        var btnAdd = document.getElementById('product-btn-add');
        var btnEdit = document.getElementById('product-btn-edit');
        fetch('http://localhost:8080/staff/product/check-code/' + codeProduct)
            .then(response => {
                if (!response.ok) {
                    btnEdit.style.display = 'none'
                    btnAdd.style.display = 'block'
                }
                return response.json();
            })
            .then(data => {
                btnEdit.style.display = 'block'
                btnAdd.style.display = 'none'
            });
    }
});

document.getElementById('product-btn-add').addEventListener('click', function(event) {
    event.preventDefault();
    // Hiển thị modal tùy chỉnh
    const modal = document.getElementById('customConfirmModal');
    modal.style.display = "flex";
    var codeProduct = document.getElementById("codeProduct").value;
    document.getElementById('confirmText-product').textContent = 'Bạn có chắc chắn muốn thêm sản phẩm "' + codeProduct + '" không?';

    document.getElementById('confirmYes').onclick = function () {
        document.getElementById('product-form').submit();
        modal.style.display = "none";
    };
    // Gán hành động khi người dùng nhấn "Hủy"
    document.getElementById('confirmNo').onclick = function () {
        modal.style.display = "none";  // Đóng modal mà không thực hiện hành động
        event.preventDefault(); // Ngăn không cho biểu mẫu được gửi
    };
});

document.getElementById('product-btn-edit').addEventListener('click', function(event) {
    event.preventDefault();
    // Hiển thị modal tùy chỉnh
    const modal = document.getElementById('customConfirmModal');
    modal.style.display = "flex";
    var codeProduct = document.getElementById("codeProduct").value;
    document.getElementById('confirmText-product').textContent = 'Bạn có chắc chắn muốn sửa sản phẩm "' + codeProduct + '" không?';

    document.getElementById('confirmYes').onclick = function () {
        document.getElementById('product-form').submit();
        modal.style.display = "none";
    };
    // Gán hành động khi người dùng nhấn "Hủy"
    document.getElementById('confirmNo').onclick = function () {
        modal.style.display = "none";  // Đóng modal mà không thực hiện hành động
        event.preventDefault(); // Ngăn không cho biểu mẫu được gửi
    };
});

document.getElementById("dropdown-btn").onclick = function(event) {
    event.stopPropagation(); // Ngăn chặn sự kiện bấm lan ra ngoài
    var dropdownContent = document.getElementById("dropdown-content");
    var arrow = document.querySelector(".arrow");

    dropdownContent.classList.toggle("show");
    arrow.classList.toggle("rotate");
};

// Đóng dropdown nếu người dùng bấm ra ngoài
window.onclick = function(event) {
    if (!event.target.matches('.dropdown-btn') && !event.target.closest('.dropdown-content')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
        // Xoay lại mũi tên về vị trí ban đầu
        var arrow = document.querySelector(".arrow");
        arrow.classList.remove("rotate");
    }
};


// xem ảnh tạm thời
function previewImages(event) {
    var imagePreviewContainer = document.getElementById('imagePreview');
    imagePreviewContainer.innerHTML = ''; // Xóa các ảnh đã preview trước đó

    var files = event.target.files;
    var fileArray = Array.from(files); // Chuyển FileList thành mảng

    // Duyệt qua từng file và hiển thị preview
    fileArray.forEach((file, index) => {
        var reader = new FileReader();
        reader.onload = function(e) {
            var imagePreview = document.createElement('div');
            imagePreview.classList.add('image-preview-container');

            var img = document.createElement('img');
            img.src = e.target.result;

            var removeButton = document.createElement('button');
            removeButton.classList.add('remove-btn');
            removeButton.innerHTML = 'x';
            removeButton.onclick = function() {
                // Xóa hình ảnh khi bấm vào nút 'x'
                imagePreview.remove();

                // Loại bỏ tệp đã xoá từ fileArray
                fileArray.splice(index, 1);

                // Cập nhật lại input với fileArray mới
                updateFileInput(fileArray);
            };

            imagePreview.appendChild(img);
            imagePreview.appendChild(removeButton);
            imagePreviewContainer.appendChild(imagePreview);
        };
        reader.readAsDataURL(file);
    });
}

function updateFileInput(fileArray) {
    var dataTransfer = new DataTransfer();

    // Nếu còn file, thêm chúng vào DataTransfer
    fileArray.forEach(file => {
        dataTransfer.items.add(file);
    });

    // Cập nhật lại input với danh sách file còn lại
    document.getElementById('imageUpload').files = dataTransfer.files;

    // Nếu không còn file, đặt lại giá trị input
    if (fileArray.length === 0) {
        document.getElementById('imageUpload').value = ""; // Reset input nếu không còn ảnh
    }
}






