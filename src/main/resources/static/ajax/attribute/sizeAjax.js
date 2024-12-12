let currentRowIndex = null; // Biến lưu chỉ số hàng hiện tại

function saveRow(index, event) {
    event.preventDefault();
    currentRowIndex = index; // Lưu index vào biến toàn cục

    // Hiển thị modal xác nhận
    const modal = new bootstrap.Modal(document.getElementById('confirm-save-modal'));
    modal.show();
}
function confirmSave() {
    if (currentRowIndex !== null) {
        let updatedData = {
            codeSize: document.getElementById('code-input-' + currentRowIndex).value,
            nameSize: document.getElementById('name-input-' + currentRowIndex).value,
            id: document.getElementById('row-' + currentRowIndex).getAttribute('data-id')
        };

        // AJAX để cập nhật dữ liệu
        $.ajax({
            url: '/attribute/update-size',  // Thay bằng URL API của bạn
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(updatedData),
            success: function (response) {
                fetchActiveSizes();
                createToast(response.check, response.message);
                // Đóng modal
                const modal = bootstrap.Modal.getInstance(document.getElementById('confirm-save-modal'));
                modal.hide();
            },
            error: function (xhr, status, error) {
                console.error('Có lỗi xảy ra khi cập nhật dữ liệu:', error);
            }
        });

        // Reset giá trị index
        currentRowIndex = null;
    }
}

document.addEventListener('show.bs.modal', function (event) {
    let button = event.relatedTarget;  // Lấy nút kích hoạt modal
    let index = button.getAttribute('data-index');  // Lấy index từ nút kích hoạt
    let id = button.getAttribute('data-id');  // Lấy id từ nút kích hoạt

    // Gán index và id vào nút "Xóa" trong modal
    let deleteButton = document.querySelector('#confirm-create-bill-modal .btn-success');
    deleteButton.setAttribute('data-index', index);
    deleteButton.setAttribute('data-id', id);
});

function deleteByID(element) {
    let id = element.getAttribute('data-id');  // Lấy id từ nút "Xóa" trong modal

    $.ajax({
        url: '/attribute/delete-size',  // Đường dẫn API để xóa
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            status: 0  // Giả sử status 0 là trạng thái bị xóa
        }),
        success: function (response) {
            fetchActiveSizes();  // Xóa hàng với id là row-index
            createToast(response.check, response.message);
        },
        error: function (xhr, status, error) {
            console.error('Có lỗi xảy ra khi xóa:', error);
        }
    });
}
document.addEventListener('show.bs.modal', function (event) {
    let button = event.relatedTarget;  // Lấy nút kích hoạt modal
    let index = button.getAttribute('data-index');  // Lấy index từ nút kích hoạt
    let id = button.getAttribute('data-id');  // Lấy id từ nút kích hoạt

    // Gán index và id vào nút "Xóa" trong modal
    let deleteButton = document.querySelector('#confirm-restore-size-modal .btn-success');
    deleteButton.setAttribute('data-index', index);
    deleteButton.setAttribute('data-id', id);
});
function restoreSize(element) {
    let id = element.getAttribute('data-id');  // Lấy id từ nút "Xóa" trong modal

    $.ajax({
        url: '/attribute/delete-size',  // Đường dẫn API để xóa
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            status: 1  // Giả sử status 0 là trạng thái bị xóa
        }),
        success: function (response) {
            fetchActiveSizes();  // Xóa hàng với id là row-index
            createToast(response.check, response.message);
        },
        error: function (xhr, status, error) {
            console.error('Có lỗi xảy ra khi xóa:', error);
        }
    });
}

document.querySelector('.attribute-btn-listDelete').addEventListener('click', function () {
    fetch('/attribute/size/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                fetchDeletedSizes();
                this.style.display = 'none';
                document.querySelector('.attribute-btn-listActive').style.display = 'inline-block';
            } else {
                createToast('1','Không có kích cỡ nào bị xóa')
            }
        });
});

document.querySelector('.attribute-btn-listActive').addEventListener('click', function () {
    fetchActiveSizes();
    this.style.display = 'none';
    document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
});

function fetchDeletedSizes() {
    // Thay URL dưới đây bằng endpoint của bạn để lấy danh sách kích cỡ đã xóa
    fetch('/attribute/size/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Xóa các hàng cũ trong tbody
                const tbody = document.querySelector('#sizeTable tbody');
                tbody.innerHTML = '';

                // Lặp qua các kích cỡ đang hoạt động và thêm các hàng vào bảng
                data.forEach((size, index) => {
                    const createDate = new Date(size.createDate).toLocaleString('vi-VN', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        second: '2-digit',
                        minute: '2-digit',
                        hour: '2-digit',
                    });
                    const updateDate = new Date(size.updateDate).toLocaleString('vi-VN', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        second: '2-digit',
                        minute: '2-digit',
                        hour: '2-digit',
                    });
                    const row = document.createElement('tr');
                    row.id = `row-${index}`;
                    row.setAttribute('data-id', size.id);
                    row.innerHTML = `
                        <td>
                            <span id="code-text-${index}">${size.codeSize}</span>
                            <input type="text" value="${size.codeSize}" id="code-input-${index}" style="display:none;">
                        </td>
                        <td>
                            <span id="name-text-${index}">${size.nameSize}</span>
                            <input type="text" value="${size.nameSize}" id="name-input-${index}" style="display:none;">
                        </td>
                        <td>${createDate}</td>
                        <td>${updateDate}</td>
                        <td>
                            <i class="attribute-status-icon status-icon fas ${size.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}"
                               data-status="${size.status}"
                               data-index="${index}"
                               title="Toggle Status"></i>
                        </td>
                        <td>
                                <a href="#" data-bs-toggle="modal" data-bs-target="#confirm-restore-size-modal"
                               data-index="${index}" data-id="${size.id}">
                                    <i class="attribute-icon-restore fas fa-undo" title="Khôi phục"></i>
                                </a>
                        </td>
                    `;
                    tbody.appendChild(row);
                });
            } else {
                createToast('1','Không có kích cỡ nào bị xóa')
            }
        })
        .catch(error => {
            console.error('Error fetching active sizes:', error);
        });
}

function fetchActiveSizes() {
    fetch('/attribute/size/active')
        .then(response => response.json())
        .then(data => {

            const tbody = document.querySelector('#sizeTable tbody');
            tbody.innerHTML = ''; // Xóa nội dung hiện tại của tbody

            data.forEach((size, index) => {
                const createDate = new Date(size.createDate).toLocaleString('vi-VN', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric',
                    second: '2-digit',
                    minute: '2-digit',
                    hour: '2-digit',
                });
                const updateDate = new Date(size.updateDate).toLocaleString('vi-VN', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric',
                    second: '2-digit',
                    minute: '2-digit',
                    hour: '2-digit',
                });
                const row = document.createElement('tr');
                row.id = `row-${index}`;
                row.setAttribute('data-id', size.id);
                row.innerHTML = `
                        <td>
                            <span id="code-text-${index}">${size.codeSize}</span>
                            <input class="inputUpdate-attribute" type="text" value="${size.codeSize}" id="code-input-${index}" style="display:none;">
                        </td>
                        <td>
                            <span id="name-text-${index}">${size.nameSize}</span>
                            <input class="inputUpdate-attribute" type="text" value="${size.nameSize}" id="name-input-${index}" style="display:none;">
                        </td>
                        <td>${createDate}</td>
                        <td>${updateDate}</td>
                        <td>
                            <i class="attribute-status-icon status-icon fas ${size.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}"
                               data-status="${size.status}"
                               data-index="${index}"
                               title="Toggle Status"></i>
                        </td>
                        <td>
                            <a href="#" onclick="editRow(${index}, event)" id="edit-btn-${index}">
                                <i class="attribute-icon-edit icon-edit fas fa-edit" title="Edit"></i>
                            </a>
                            <a href="#" onclick="saveRow(${index}, event)" id="save-btn-${index}" style="display:none;">
                                <i class="attribute-icon-save icon-save fas fa-save" title="Save"></i>
                            </a>
                            <a href="#" data-bs-toggle="modal" data-bs-target="#confirm-create-bill-modal"
                               data-index="${index}" data-id="${size.id}">
                                <i class="attribute-icon-delete icon-delete fas fa-trash" title="Delete"></i>
                            </a>
                        </td>
                    `;
                tbody.appendChild(row);
            });
        })
        .catch(error => {
            console.error("Có lỗi xảy ra khi lấy danh sách kích cỡ:", error);
        });
}

async function add() {

    const formElement = document.getElementById('createAttribute');
    const formData = new FormData(formElement);
    const response = await fetch('/attribute/size/add', {
        method: 'POST',
        body: formData
    });
    if (response.ok) {
        const result = await response.json();

        document.querySelector('.attribute-btn-listActive').style.display = 'none';
        document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
        createToast(result.check, result.message);
        if (result.check === '1'){
            codeSizeInput.value = '';
            nameSizeInput.value = '';
        }
        fetchActiveSizes();
    }


}

fetchActiveSizes();