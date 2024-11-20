function saveRow(index, event) { // hàm edit dữ liệu trên table
    event.preventDefault();
    var updatedData = {
        codeManufacturer: document.getElementById('code-input-' + index).value,
        nameManufacturer: document.getElementById('name-input-' + index).value,
        id: document.getElementById('row-' + index).getAttribute('data-id')  // Lấy ID của đối tượng từ hàng
    };

    // Thực hiện AJAX để cập nhật dữ liệu trong cơ sở dữ liệu
    $.ajax({
        url: '/attribute/update-manufacturer',  // Thay bằng URL API của bạn
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(updatedData),  // Gửi dữ liệu JSON
        success: function (response) {
            fetchActiveManufacturers();
            createToast(response.check, response.message);
        },
        error: function (xhr, status, error) {
            console.error('Có lỗi xảy ra khi cập nhật dữ liệu:', error);
            // Xử lý lỗi nếu cần thiết
        }
    });
}

function toggleStatus(element) { // hàm thay đổi trạng thái bằng button
    var index = element.getAttribute('data-index');  // Lấy index
    var status = element.getAttribute('data-status') === '1' ? 2 : 1;  // Lấy trạng thái mới

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
    var id = $('#row-' + index).data('id');  // Giả định bạn có thuộc tính id trong hàng

    // Thực hiện Ajax request để cập nhật trạng thái trong database
    $.ajax({
        url: '/attribute/manufacturer/update-status',  // Đường dẫn API
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
    var button = event.relatedTarget;  // Lấy nút kích hoạt modal
    var index = button.getAttribute('data-index');  // Lấy index từ nút kích hoạt
    var id = button.getAttribute('data-id');  // Lấy id từ nút kích hoạt

    // Gán index và id vào nút "Xóa" trong modal
    var deleteButton = document.querySelector('#confirm-create-bill-modal .btn-success');
    deleteButton.setAttribute('data-index', index);
    deleteButton.setAttribute('data-id', id);
});

function deleteByID(element) {
    var index = element.getAttribute('data-index');  // Lấy index từ nút "Xóa" trong modal
    var id = element.getAttribute('data-id');  // Lấy id từ nút "Xóa" trong modal

    $.ajax({
        url: '/attribute/delete-manufacturer',  // Đường dẫn API để xóa
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            status: 0  // Giả sử status 0 là trạng thái bị xóa
        }),
        success: function (response) {
            $('#row-' + index).remove();  // Xóa hàng với id là row-index
            createToast(response.check, response.message);
        },
        error: function (xhr, status, error) {
            console.error('Có lỗi xảy ra khi xóa:', error);
        }
    });
}


document.querySelector('.attribute-btn-listDelete').addEventListener('click', function () {
    fetch('/attribute/manufacturer/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                fetchDeletedManufacturers();
                this.style.display = 'none';
                document.querySelector('.attribute-btn-listActive').style.display = 'inline-block';
            } else {
                createToast('1','Không có màu nào bị xóa')
            }
        });
});

document.querySelector('.attribute-btn-listActive').addEventListener('click', function () {
    fetchActiveManufacturers();
    this.style.display = 'none';
    document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
});

function fetchDeletedManufacturers() {
    // Thay URL dưới đây bằng endpoint của bạn để lấy danh sách màu đã xóa
    fetch('/attribute/manufacturer/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Xóa các hàng cũ trong tbody
                const tbody = document.querySelector('#manufacturerTable tbody');
                tbody.innerHTML = '';

                // Lặp qua các màu đang hoạt động và thêm các hàng vào bảng
                data.forEach((manufacturer, index) => {
                    const createDate = new Date(manufacturer.createDate).toLocaleString('vi-VN', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        second: '2-digit',
                        minute: '2-digit',
                        hour: '2-digit',
                    });
                    const updateDate = new Date(manufacturer.updateDate).toLocaleString('vi-VN', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        second: '2-digit',
                        minute: '2-digit',
                        hour: '2-digit',
                    });
                    const row = document.createElement('tr');
                    row.id = `row-${index}`;
                    row.setAttribute('data-id', manufacturer.id);
                    row.innerHTML = `
                        <td>
                            <span id="code-text-${index}">${manufacturer.codeManufacturer}</span>
                            <input type="text" value="${manufacturer.codeManufacturer}" id="code-input-${index}" style="display:none;">
                        </td>
                        <td>
                            <span id="name-text-${index}">${manufacturer.nameManufacturer}</span>
                            <input type="text" value="${manufacturer.nameManufacturer}" id="name-input-${index}" style="display:none;">
                        </td>
                        <td>${createDate}</td>
                        <td>${updateDate}</td>
                        <td>
                            <i class="attribute-status-icon status-icon fas ${manufacturer.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}"
                               data-status="${manufacturer.status}"
                               data-index="${index}"
                               title="Toggle Status"></i>
                        </td>
                        <td>
                                <a data-index="${index}" data-id="${manufacturer.id}"  onclick="restoreManufacturer(this)">
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
            console.error('Error fetching active manufacturers:', error);
        });
}


function fetchActiveManufacturers() {
    fetch('/attribute/manufacturer/active')
        .then(response => response.json())
        .then(data => {

            const tbody = document.querySelector('#manufacturerTable tbody');
            tbody.innerHTML = ''; // Xóa nội dung hiện tại của tbody

            data.forEach((manufacturer, index) => {
                const createDate = new Date(manufacturer.createDate).toLocaleString('vi-VN', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric',
                    second: '2-digit',
                    minute: '2-digit',
                    hour: '2-digit',
                });
                const updateDate = new Date(manufacturer.updateDate).toLocaleString('vi-VN', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric',
                    second: '2-digit',
                    minute: '2-digit',
                    hour: '2-digit',
                });
                const row = document.createElement('tr');
                row.id = `row-${index}`;
                row.setAttribute('data-id', manufacturer.id);
                row.innerHTML = `
                        <td>
                            <span id="code-text-${index}">${manufacturer.codeManufacturer}</span>
                            <input class="inputUpdate-attribute" type="text" value="${manufacturer.codeManufacturer}" id="code-input-${index}" style="display:none;">
                        </td>
                        <td>
                            <span id="name-text-${index}">${manufacturer.nameManufacturer}</span>
                            <input class="inputUpdate-attribute" type="text" value="${manufacturer.nameManufacturer}" id="name-input-${index}" style="display:none;">
                        </td>
                        <td>${createDate}</td>
                        <td>${updateDate}</td>
                        <td>
                            <i class="attribute-status-icon status-icon fas ${manufacturer.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}"
                               data-status="${manufacturer.status}"
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
                               data-index="${index}" data-id="${manufacturer.id}">
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
    if (await validateManufacturer()) {
        const formElement = document.getElementById('createAttribute');
        const formData = new FormData(formElement);
        const response = await fetch('/attribute/manufacturer/add', {
            method: 'POST',
            body: formData
        });
        if (response.ok) {
            const result = await response.json();
            codeManufacturerInput.value = '';
            nameManufacturerInput.value = '';
            document.querySelector('.attribute-btn-listActive').style.display = 'none';
            document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
            createToast(result.check, result.message);
            fetchActiveManufacturers();
        }
    } else {
        createToast('2', 'Dữ liệu không hợp lệ');
    }

}

fetchActiveManufacturers();

function restoreManufacturer(element) {
    var index = element.getAttribute('data-index');
    var id = element.getAttribute('data-id');
    $.ajax({
        url: '/attribute/delete-manufacturer',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            status: 1
        }),
        success: function (response) {
            $('#row-' + index).remove();
            createToast(response.check, response.message)
        },
        error: function (xhr, status, error) {
            createToast('2', 'Khôi phục nhà sản xuất thất bại')
        }
    });

}

var codeManufacturerInput = document.getElementById("codeManufacturerInput");
var nameManufacturerInput = document.getElementById("nameManufacturerInput");
var manufacturerError = document.getElementById("manufacturerError");
codeManufacturerInput.addEventListener('input', function () {
    validateManufacturer();
});
nameManufacturerInput.addEventListener('input', function () {
    validateManufacturer();
});

var arrayCodeManufacturer = [];
var arrayNameManufacturer = [];


async function validateManufacturer() {
    var codeManufacturer = await fetch('/attribute/manufacturer/get-code');
    if (codeManufacturer.ok) {
        arrayCodeManufacturer = await codeManufacturer.json(); // Đảm bảo đây là một mảng
    }
    var nameManufacturer = await fetch('/attribute/manufacturer/get-name');
    if (nameManufacturer.ok) {
        arrayNameManufacturer = await nameManufacturer.json(); // Đảm bảo đây là một mảng
    }
    if (codeManufacturerInput.value.trim() === "" && nameManufacturerInput.value.trim() === "") {
        manufacturerError.textContent = "* Mã và tên không được để trống";
        return false;
    } else if (codeManufacturerInput.value.length > 10 && nameManufacturerInput.value.length > 50) {
        manufacturerError.textContent = "* Mã <= 10 kí tự, Tên <= 50 kí tự";
        return false;
    } else if (arrayCodeManufacturer.some(code => code.toLowerCase() === codeManufacturerInput.value.trim().toLowerCase())) {
        manufacturerError.textContent = "* Mã nhà sản xuất đã tồn tại";
        return false;
    } else if (arrayNameManufacturer.some(name => name.toLowerCase() === codeManufacturerInput.value.trim().toLowerCase())) {
        manufacturerError.textContent = "* Tên nhà sản xuất đã tồn tại";
        return false;
    } else if (codeManufacturerInput.value.trim() === "") {
        manufacturerError.textContent = "* Mã không được để trống";
        return false;
    } else if (nameManufacturerInput.value.trim() === "") {
        manufacturerError.textContent = "* Tên không được để trống";
        return false;
    } else if (codeManufacturerInput.value.length > 10) {
        manufacturerError.textContent = "* Mã <= 10 kí tự";
        return false;
    } else if (nameManufacturerInput.value.length > 50) {
        manufacturerError.textContent = "* Tên <= 50 kí tự";
        return false;
    } else {
        manufacturerError.textContent = "";
        return true;
    }
}

