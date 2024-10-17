function toggleDiv() {
    const content = document.getElementById("collapsibleDiv");
    const arrow = document.getElementById("arrow");

    if (!content.style.display || content.style.display === "none") {
        content.style.display = "block";
        arrow.style.transform = "rotate(180deg)";
    } else {
        content.style.display = "none";
        arrow.style.transform = "rotate(0deg)";
    }
}

document.getElementById('codeProduct').addEventListener('input', function () {
    const codeProduct = this.value.trim();
    const btnAdd = document.getElementById('product-btn-add');
    const btnEdit = document.getElementById('product-btn-edit');

    if (codeProduct !== "") {
        fetch(`http://localhost:8080/staff/product/check-code/${codeProduct}`)
            .then(response => {
                btnEdit.style.display = response.ok ? 'block' : 'none';
                btnAdd.style.display = response.ok ? 'none' : 'block';
                return response.json();
            })
            .catch(err => console.error('Error checking product code:', err));
    }
});

function updateCodeProductValue(newCodeProduct) {
    const codeProductInput = document.getElementById('codeProduct');
    codeProductInput.value = newCodeProduct;
    codeProductInput.dispatchEvent(new Event('input', { bubbles: true }));
}

function editRow(index) {
    // Mở rộng div nếu đang thu gọn
    const content = document.getElementById("collapsibleDiv");
    const arrow = document.getElementById("arrow");
    const id = document.getElementById(`row-${index}`).dataset.id;

    if (!content.style.display || content.style.display === "none") {
        content.style.display = "block";
        arrow.style.transform = "rotate(180deg)";
    }

    // Lấy thông tin sản phẩm
    fetch(`http://localhost:8080/staff/product/get-one/${id}`)
        .then(response => response.json())
        .then(data => {
            // Cập nhật dữ liệu vào các input và select
            updateCodeProductValue(data.codeProduct); // Kích hoạt sự kiện input khi cập nhật
            document.getElementById("nameProduct").value = data.nameProduct;
            document.getElementById("material").value = data.material.id;
            document.getElementById("manufacturer").value = data.manufacturer.id;
            document.getElementById("origin").value = data.origin.id;
            document.getElementById("sole").value = data.sole.id;
            document.getElementById("describeProduct").value = data.describe;

            // Lấy hình ảnh
            return fetch(`http://localhost:8080/staff/product/getImage/${id}`);
        })
        .then(response => response.json())
        .then(images => {
            const imagePreview = document.getElementById('imagePreview');
            imagePreview.innerHTML = ''; // Xóa hình ảnh cũ trước khi thêm mới
            images.forEach(image => {
                const img = document.createElement('img');
                img.src = `https://res.cloudinary.com/dfy4umpja/image/upload/v1728725582/${image.nameImage}`;

                const container = document.createElement('div');
                container.classList.add('image-preview-container');
                container.appendChild(img);
                imagePreview.appendChild(container);
            });
        })
        .then(() => {
            // Lấy danh sách category và đánh dấu các checkbox
            return fetch(`http://localhost:8080/staff/product/getCategory/${id}`);
        })
        .then(response => response.json())
        .then(idCategories => {
            const allCheckboxes = document.querySelectorAll('[id^="category_"]');
            allCheckboxes.forEach(checkbox => {
                const checkboxId = checkbox.id.split('_')[1]; // Lấy ID của checkbox
                if (idCategories.includes(parseInt(checkboxId))) {
                    checkbox.checked = true;  // Đánh dấu nếu có trong database
                } else {
                    checkbox.checked = false; // Bỏ đánh dấu nếu không có trong database
                }
            });
        })
        .catch(error => console.error('Lỗi khi chỉnh sửa dòng:', error));

    // Cuộn lên đầu trang
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}


function handleFormSubmit(event, actionType) {
    event.preventDefault();
    const modal = document.getElementById('customConfirmModal');
    modal.style.display = "flex";
    const codeProduct = document.getElementById("codeProduct").value;
    document.getElementById('confirmText-product').textContent = `Bạn có chắc chắn muốn ${actionType} sản phẩm "${codeProduct}" không?`;

    document.getElementById('confirmYes').onclick = function () {
        document.getElementById('product-form').submit();
        modal.style.display = "none";
    };

    document.getElementById('confirmNo').onclick = function () {
        modal.style.display = "none";
    };
}

document.getElementById('product-btn-add').addEventListener('click', function (event) {
    handleFormSubmit(event, 'thêm');
});

document.getElementById('product-btn-edit').addEventListener('click', function (event) {
    handleFormSubmit(event, 'sửa');
});

document.getElementById("dropdown-btn").onclick = function (event) {
    event.stopPropagation();
    const dropdownContent = document.getElementById("dropdown-content");
    const arrow = document.querySelector(".arrow");
    dropdownContent.classList.toggle("show-product");
    arrow.classList.toggle("rotate-product");
};

window.onclick = function (event) {
    if (!event.target.matches('.dropdown-btn') && !event.target.closest('.dropdown-content')) {
        document.querySelectorAll(".dropdown-content").forEach(dropdown => dropdown.classList.remove('show'));
        document.querySelector(".arrow").classList.remove("rotate");
    }
};

function previewImages(event) {
    const files = Array.from(event.target.files);
    const imagePreviewContainer = document.getElementById('imagePreview');
    imagePreviewContainer.innerHTML = '';

    files.forEach(file => {
        const reader = new FileReader();
        reader.onload = function (e) {
            const imagePreview = document.createElement('div');
            imagePreview.classList.add('image-preview-container');

            const img = document.createElement('img');
            img.src = e.target.result;
            img.classList.add('image-preview');

            imagePreview.appendChild(img);
            imagePreviewContainer.appendChild(imagePreview);
        };
        reader.readAsDataURL(file);
    });
}
