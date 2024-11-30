function saveRow(index, event) { // hàm edit dữ liệu trên table
    event.preventDefault();
    let updatedData = {
        codeSize: document.getElementById('code-input-' + index).value,
        nameSize: document.getElementById('name-input-' + index).value,
        id: document.getElementById('row-' + index).getAttribute('data-id')  // Lấy ID của đối tượng từ hàng
    };

    // Thực hiện AJAX để cập nhật dữ liệu trong cơ sở dữ liệu
    $.ajax({
        url: '/attribute/update-size',  // Thay bằng URL API của bạn
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(updatedData),  // Gửi dữ liệu JSON
        success: function (response) {
            fetchActiveSizes();
            createToast(response.check, response.message);
        },
        error: function (xhr, status, error) {
            console.error('Có lỗi xảy ra khi cập nhật dữ liệu:', error);
            // Xử lý lỗi nếu cần thiết
        }
    });
}

function toggleStatus(element) { // hàm thay đổi trạng thái bằng button
    let index = element.getAttribute('data-index');  // Lấy index
    let status = element.getAttribute('data-status') === '1' ? 2 : 1;  // Lấy trạng thái mới

    // Thay đổi biểu tượng toggle
    if (status === 1) {
        element.classList.remove('fa-toggle-off');
        element.classList.add('fa-toggle-on');
    } else {
        element.classList.remove('fa-toggle-on');
        element.classList.add('fa-toggle-off');
    }

    // Cập nhật trạng thái trong data attribute
    element.setAttribute('data-status', status);

    // Lấy ID thực tế của đối tượng thay vì index
    let id = $('#row-' + index).data('id');  // Giả định bạn có thuộc tính id trong hàng

    // Thực hiện Ajax request để cập nhật trạng thái trong database
    $.ajax({
        url: '/attribute/size/update-status',  // Đường dẫn API
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            status: status
        }),
        success: function (response) {
            console.log('Trạng thái đã được cập nhật thành công');
        },
        error: function (xhr, status, error) {
            console.error('Có lỗi xảy ra khi cập nhật trạng thái:', error);
            // Xử lý lỗi nếu cần
        }
    });
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
    let index = element.getAttribute('data-index');  // Lấy index từ nút "Xóa" trong modal
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


document.querySelector('.attribute-btn-listDelete').addEventListener('click', function () {
    fetch('/attribute/size/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                fetchDeletedSizes();
                this.style.display = 'none';
                document.querySelector('.attribute-btn-listActive').style.display = 'inline-block';
            } else {
                createToast('1','Không có màu nào bị xóa')
            }
        });
});

document.querySelector('.attribute-btn-listActive').addEventListener('click', function () {
    fetchActiveSizes();
    this.style.display = 'none';
    document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
});

function fetchDeletedSizes() {
    // Thay URL dưới đây bằng endpoint của bạn để lấy danh sách màu đã xóa
    fetch('/attribute/size/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Xóa các hàng cũ trong tbody
                const tbody = document.querySelector('#sizeTable tbody');
                tbody.innerHTML = '';

                // Lặp qua các màu đang hoạt động và thêm các hàng vào bảng
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
                                <a data-index="${index}" data-id="${size.id}"  onclick="restoreSize(this)">
                                    <i class="attribute-icon-restore fas fa-undo" title="Khôi phục"></i>
                                </a>
                        </td>
                    `;
                    tbody.appendChild(row);
                });
            } else {
                createToast('1','Không có màu nào bị xóa')
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
            console.error("Có lỗi xảy ra khi lấy danh sách màu:", error);
        });
}

async function add() {
    if (await validateSize()) {
        const formElement = document.getElementById('createAttribute');
        const formData = new FormData(formElement);
        const response = await fetch('/attribute/size/add', {
            method: 'POST',
            body: formData
        });
        if (response.ok) {
            const result = await response.json();
            codeSizeInput.value = '';
            nameSizeInput.value = '';
            document.querySelector('.attribute-btn-listActive').style.display = 'none';
            document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
            createToast(result.check, result.message);
            fetchActiveSizes();
        }
    } else {
        createToast('2', 'Dữ liệu không hợp lệ');
    }

}

fetchActiveSizes();

function restoreSize(element) {
    let index = element.getAttribute('data-index');
    let id = element.getAttribute('data-id');
    $.ajax({
        url: '/attribute/delete-size',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            status: 1
        }),
        success: function (response) {
            fetchDeletedSizes();
            createToast(response.check, response.message)
        },
        error: function (xhr, status, error) {
            createToast('2', 'Khôi phục kích cỡ thất bại')
        }
    });

}

let codeSizeInput = document.getElementById("codeSizeInput");
let nameSizeInput = document.getElementById("nameSizeInput");
let sizeError = document.getElementById("sizeError");
codeSizeInput.addEventListener('input', function () {
    validateSize();
});
nameSizeInput.addEventListener('input', function () {
    validateSize();
});

let arrayCodeSize = [];
let arrayNameSize = [];


async function validateSize() {
    let codeSize = await fetch('/attribute/size/get-code');
    if (codeSize.ok) {
        arrayCodeSize = await codeSize.json(); // Đảm bảo đây là một mảng
    }
    let nameSize = await fetch('/attribute/size/get-name');
    if (nameSize.ok) {
        arrayNameSize = await nameSize.json(); // Đảm bảo đây là một mảng
    }
    if (codeSizeInput.value.trim() === "" && nameSizeInput.value.trim() === "") {
        sizeError.textContent = "* Mã và tên không được để trống";
        return false;
    } else if (codeSizeInput.value.length > 10 && nameSizeInput.value.length > 50) {
        sizeError.textContent = "* Mã <= 10 kí tự, Tên <= 50 kí tự";
        return false;
    } else if (arrayCodeSize.some(code => code.toLowerCase() === codeSizeInput.value.trim().toLowerCase())) {
        sizeError.textContent = "* Mã kích cỡ đã tồn tại";
        return false;
    } else if (arrayNameSize.some(name => name.toLowerCase() === nameSizeInput.value.trim().toLowerCase())) {
        sizeError.textContent = "* Tên kích cỡ đã tồn tại";
        return false;
    } else if (codeSizeInput.value.trim() === "") {
        sizeError.textContent = "* Mã không được để trống";
        return false;
    } else if (nameSizeInput.value.trim() === "") {
        sizeError.textContent = "* Tên không được để trống";
        return false;
    } else if (codeSizeInput.value.length > 10) {
        sizeError.textContent = "* Mã <= 10 kí tự";
        return false;
    } else if (nameSizeInput.value.length > 50) {
        sizeError.textContent = "* Tên <= 50 kí tự";
        return false;
    } else {
        sizeError.textContent = "";
        return true;
    }
}

