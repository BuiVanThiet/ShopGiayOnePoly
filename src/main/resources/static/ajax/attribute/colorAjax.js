function saveRow(index) { // hàm edit dữ liệu trên table
    console.log('Saving row:', index);
    var updatedData = {
        codeColor: document.getElementById('code-input-' + index).value,
        nameColor: document.getElementById('name-input-' + index).value,
        id: document.getElementById('row-' + index).getAttribute('data-id')  // Lấy ID của đối tượng từ hàng
    };

    // Hiển thị lại các giá trị đã chỉnh sửa trên trang
    document.getElementById('code-text-' + index).innerText = updatedData.codeColor;
    document.getElementById('name-text-' + index).innerText = updatedData.nameColor;

    // Ẩn input và hiển thị lại text
    document.getElementById('code-input-' + index).style.display = 'none';
    document.getElementById('name-input-' + index).style.display = 'none';
    document.getElementById('code-text-' + index).style.display = 'inline-block';
    document.getElementById('name-text-' + index).style.display = 'inline-block';

    // Hiển thị lại nút "Edit" và ẩn nút "Save"
    document.getElementById('edit-btn-' + index).style.display = 'inline-block';
    document.getElementById('save-btn-' + index).style.display = 'none';

    console.log('Sending updated data to server:', updatedData);

    // Thực hiện AJAX để cập nhật dữ liệu trong cơ sở dữ liệu
    $.ajax({
        url: '/attribute/update-color',  // Thay bằng URL API của bạn
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(updatedData),  // Gửi dữ liệu JSON
        success: function (response) {
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
        url: '/attribute/color/update-status',  // Đường dẫn API
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
        url: '/attribute/delete-color',  // Đường dẫn API để xóa
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
    fetch('/attribute/color/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                fetchDeletedColors();
                this.style.display = 'none';
                document.querySelector('.attribute-btn-listActive').style.display = 'inline-block';
            } else {
                createToast('1','Không có màu nào bị xóa')
            }
        });
});

document.querySelector('.attribute-btn-listActive').addEventListener('click', function () {
    fetchActiveColors();
    this.style.display = 'none';
    document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
});

function fetchDeletedColors() {
    // Thay URL dưới đây bằng endpoint của bạn để lấy danh sách màu đã xóa
    fetch('/attribute/color/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Xóa các hàng cũ trong tbody
                const tbody = document.querySelector('#colorTable tbody');
                tbody.innerHTML = '';

                // Lặp qua các màu đang hoạt động và thêm các hàng vào bảng
                data.forEach((color, index) => {
                    const row = document.createElement('tr');
                    row.id = `row-${index}`;
                    row.setAttribute('data-id', color.id);
                    row.innerHTML = `
                        <td>
                            <span id="code-text-${index}">${color.codeColor}</span>
                            <input type="text" value="${color.codeColor}" id="code-input-${index}" style="display:none;">
                        </td>
                        <td>
                            <span id="name-text-${index}">${color.nameColor}</span>
                            <input type="text" value="${color.nameColor}" id="name-input-${index}" style="display:none;">
                        </td>
                        <td>${color.createDate}</td>
                        <td>${color.updateDate}</td>
                        <td>
                            <i class="attribute-status-icon status-icon fas ${color.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}"
                               data-status="${color.status}"
                               data-index="${index}"
                               title="Toggle Status"></i>
                        </td>
                        <td>
                                <a data-index="${index}" data-id="${color.id}"  onclick="restoreColor(this)">
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
            console.error('Error fetching active colors:', error);
        });
}


function fetchActiveColors() {
    fetch('/attribute/color/active')
        .then(response => response.json())
        .then(data => {

                const tbody = document.querySelector('#colorTable tbody');
                tbody.innerHTML = ''; // Xóa nội dung hiện tại của tbody

                data.forEach((color, index) => {
                    const row = document.createElement('tr');
                    row.id = `row-${index}`;
                    row.setAttribute('data-id', color.id);
                    row.innerHTML = `
                        <td>
                            <span id="code-text-${index}">${color.codeColor}</span>
                            <input class="inputUpdate-attribute" type="text" value="${color.codeColor}" id="code-input-${index}" style="display:none;">
                        </td>
                        <td>
                            <span id="name-text-${index}">${color.nameColor}</span>
                            <input class="inputUpdate-attribute" type="text" value="${color.nameColor}" id="name-input-${index}" style="display:none;">
                        </td>
                        <td>${color.createDate}</td>
                        <td>${color.updateDate}</td>
                        <td>
                            <i class="attribute-status-icon status-icon fas ${color.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}"
                               data-status="${color.status}"
                               data-index="${index}"
                               onclick="toggleStatus(this)"
                               title="Toggle Status"></i>
                        </td>
                        <td>
                            <a href="#" onclick="editRow(${index})" id="edit-btn-${index}">
                                <i class="attribute-icon-edit icon-edit fas fa-edit" title="Edit"></i>
                            </a>
                            <a href="#" onclick="saveRow(${index})" id="save-btn-${index}" style="display:none;">
                                <i class="attribute-icon-save icon-save fas fa-save" title="Save"></i>
                            </a>
                            <a href="#" data-bs-toggle="modal" data-bs-target="#confirm-create-bill-modal"
                               data-index="${index}" data-id="${color.id}">
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
    const formElement = document.getElementById('createAttribute');
    const formData = new FormData(formElement);
    const response = await fetch('/attribute/color/add', {
        method: 'POST',
        body: formData
    });
    if (response.ok) {
        const result = await response.json(); 
        createToast(result.check, result.message);
        fetchActiveColors();
    }
}


function restoreColor(element) {
    var index = element.getAttribute('data-index');
    var id = element.getAttribute('data-id');
    $.ajax({
        url: '/attribute/delete-color',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            status: 1
        }),
        success: function (response) {
            $('#row-' + index).remove();
            createToast('1', 'Khôi phục màu sắc thành công')
        },
        error: function (xhr, status, error) {
            createToast('2', 'Khôi phục màu sắc thất bại')
        }
    });

}

var codeColorInput = document.getElementById("codeColorInput");
var nameColorInput = document.getElementById("nameColorInput");
var colorError = document.getElementById("colorError");
codeColorInput.addEventListener('input', function () {
    validateAttribute();
});
nameColorInput.addEventListener('input', function () {
    validateAttribute();
});

function validateAttribute() {
    if (codeColorInput.value.trim() === "" && nameColorInput.value.trim() === "") {
        colorError.textContent = "* Mã và tên không được để trống";
        return false;
    } else if (codeColorInput.value.length > 10 && nameColorInput.value.length > 50) {
        colorError.textContent = "* Mã <= 10 kí tự, Tên <= 50 kí tự";
        return false;
    } else if (codeColorInput.value.trim() === "") {
        colorError.textContent = "* Mã không được để trống";
        return false;
    } else if (nameColorInput.value.trim() === "") {
        colorError.textContent = "* Tên không được để trống";
        return false;
    } else if (codeColorInput.value.length > 10) {
        colorError.textContent = "* Mã <= 10 kí tự";
        return false;
    } else if (nameColorInput.value.length > 50) {
        colorError.textContent = "* Tên <= 50 kí tự";
        return false;
    } else {
        colorError.textContent = "";
        return true;
    }
}


function checkValidate() {
    const codeColorInput = document.getElementById("codeColorInput");
    const nameColorInput = document.getElementById("nameColorInput");
    if (codeColorInput.value.length > 10 || codeColorInput.value.trim() === '' || nameColorInput.value.length > 50 || nameColorInput.value.trim() === '') {
        return false;
    } else {
        return true;
    }
}
