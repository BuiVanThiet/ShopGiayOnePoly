function saveRow(index, event) { // hàm edit dữ liệu trên table
    event.preventDefault();
    let updatedData = {
        codeSole: document.getElementById('code-input-' + index).value,
        nameSole: document.getElementById('name-input-' + index).value,
        id: document.getElementById('row-' + index).getAttribute('data-id')  // Lấy ID của đối tượng từ hàng
    };

    // Thực hiện AJAX để cập nhật dữ liệu trong cơ sở dữ liệu
    $.ajax({
        url: '/attribute/update-sole',  // Thay bằng URL API của bạn
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(updatedData),  // Gửi dữ liệu JSON
        success: function (response) {
            fetchActiveSoles();
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
        url: '/attribute/sole/update-status',  // Đường dẫn API
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
        url: '/attribute/delete-sole',  // Đường dẫn API để xóa
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            status: 0  // Giả sử status 0 là trạng thái bị xóa
        }),
        success: function (response) {
            fetchActiveSoles();  // Xóa hàng với id là row-index
            createToast(response.check, response.message);
        },
        error: function (xhr, status, error) {
            console.error('Có lỗi xảy ra khi xóa:', error);
        }
    });
}


document.querySelector('.attribute-btn-listDelete').addEventListener('click', function () {
    fetch('/attribute/sole/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                fetchDeletedSoles();
                this.style.display = 'none';
                document.querySelector('.attribute-btn-listActive').style.display = 'inline-block';
            } else {
                createToast('1','Không có màu nào bị xóa')
            }
        });
});

document.querySelector('.attribute-btn-listActive').addEventListener('click', function () {
    fetchActiveSoles();
    this.style.display = 'none';
    document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
});

function fetchDeletedSoles() {
    // Thay URL dưới đây bằng endpoint của bạn để lấy danh sách màu đã xóa
    fetch('/attribute/sole/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Xóa các hàng cũ trong tbody
                const tbody = document.querySelector('#soleTable tbody');
                tbody.innerHTML = '';

                // Lặp qua các màu đang hoạt động và thêm các hàng vào bảng
                data.forEach((sole, index) => {
                    const createDate = new Date(sole.createDate).toLocaleString('vi-VN', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        second: '2-digit',
                        minute: '2-digit',
                        hour: '2-digit',
                    });
                    const updateDate = new Date(sole.updateDate).toLocaleString('vi-VN', {
                        day: '2-digit',
                        month: '2-digit',
                        year: 'numeric',
                        second: '2-digit',
                        minute: '2-digit',
                        hour: '2-digit',
                    });
                    const row = document.createElement('tr');
                    row.id = `row-${index}`;
                    row.setAttribute('data-id', sole.id);
                    row.innerHTML = `
                        <td>
                            <span id="code-text-${index}">${sole.codeSole}</span>
                            <input type="text" value="${sole.codeSole}" id="code-input-${index}" style="display:none;">
                        </td>
                        <td>
                            <span id="name-text-${index}">${sole.nameSole}</span>
                            <input type="text" value="${sole.nameSole}" id="name-input-${index}" style="display:none;">
                        </td>
                        <td>${createDate}</td>
                        <td>${updateDate}</td>
                        <td>
                            <i class="attribute-status-icon status-icon fas ${sole.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}"
                               data-status="${sole.status}"
                               data-index="${index}"
                               title="Toggle Status"></i>
                        </td>
                        <td>
                                <a data-index="${index}" data-id="${sole.id}"  onclick="restoreSole(this)">
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
            console.error('Error fetching active soles:', error);
        });
}


function fetchActiveSoles() {
    fetch('/attribute/sole/active')
        .then(response => response.json())
        .then(data => {

            const tbody = document.querySelector('#soleTable tbody');
            tbody.innerHTML = ''; // Xóa nội dung hiện tại của tbody

            data.forEach((sole, index) => {
                const createDate = new Date(sole.createDate).toLocaleString('vi-VN', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric',
                    second: '2-digit',
                    minute: '2-digit',
                    hour: '2-digit',
                });
                const updateDate = new Date(sole.updateDate).toLocaleString('vi-VN', {
                    day: '2-digit',
                    month: '2-digit',
                    year: 'numeric',
                    second: '2-digit',
                    minute: '2-digit',
                    hour: '2-digit',
                });
                const row = document.createElement('tr');
                row.id = `row-${index}`;
                row.setAttribute('data-id', sole.id);
                row.innerHTML = `
                        <td>
                            <span id="code-text-${index}">${sole.codeSole}</span>
                            <input class="inputUpdate-attribute" type="text" value="${sole.codeSole}" id="code-input-${index}" style="display:none;">
                        </td>
                        <td>
                            <span id="name-text-${index}">${sole.nameSole}</span>
                            <input class="inputUpdate-attribute" type="text" value="${sole.nameSole}" id="name-input-${index}" style="display:none;">
                        </td>
                        <td>${createDate}</td>
                        <td>${updateDate}</td>
                        <td>
                            <i class="attribute-status-icon status-icon fas ${sole.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}"
                               data-status="${sole.status}"
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
                               data-index="${index}" data-id="${sole.id}">
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
    if (await validateSole()) {
        const formElement = document.getElementById('createAttribute');
        const formData = new FormData(formElement);
        const response = await fetch('/attribute/sole/add', {
            method: 'POST',
            body: formData
        });
        if (response.ok) {
            const result = await response.json();
            codeSoleInput.value = '';
            nameSoleInput.value = '';
            document.querySelector('.attribute-btn-listActive').style.display = 'none';
            document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
            createToast(result.check, result.message);
            fetchActiveSoles();
        }
    } else {
        createToast('2', 'Dữ liệu không hợp lệ');
    }

}

fetchActiveSoles();

function restoreSole(element) {
    let index = element.getAttribute('data-index');
    let id = element.getAttribute('data-id');
    $.ajax({
        url: '/attribute/delete-sole',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            id: id,
            status: 1
        }),
        success: function (response) {
            fetchDeletedSoles();
            createToast(response.check, response.message)
        },
        error: function (xhr, status, error) {
            createToast('2', 'Khôi phục đế giày thất bại')
        }
    });

}

let codeSoleInput = document.getElementById("codeSoleInput");
let nameSoleInput = document.getElementById("nameSoleInput");
let soleError = document.getElementById("soleError");
codeSoleInput.addEventListener('input', function () {
    validateSole();
});
nameSoleInput.addEventListener('input', function () {
    validateSole();
});

let arrayCodeSole = [];
let arrayNameSole = [];


async function validateSole() {
    let codeSole = await fetch('/attribute/sole/get-code');
    if (codeSole.ok) {
        arrayCodeSole = await codeSole.json(); // Đảm bảo đây là một mảng
    }
    let nameSole = await fetch('/attribute/sole/get-name');
    if (nameSole.ok) {
        arrayNameSole = await nameSole.json(); // Đảm bảo đây là một mảng
    }
    if (codeSoleInput.value.trim() === "" && nameSoleInput.value.trim() === "") {
        soleError.textContent = "* Mã và tên không được để trống";
        return false;
    } else if (codeSoleInput.value.length > 10 && nameSoleInput.value.length > 50) {
        soleError.textContent = "* Mã <= 10 kí tự, Tên <= 50 kí tự";
        return false;
    } else if (arrayCodeSole.some(code => code.toLowerCase() === codeSoleInput.value.trim().toLowerCase())) {
        soleError.textContent = "* Mã đế giày đã tồn tại";
        return false;
    } else if (arrayNameSole.some(name => name.toLowerCase() === nameSoleInput.value.trim().toLowerCase())) {
        soleError.textContent = "* Tên đế giày đã tồn tại";
        return false;
    } else if (codeSoleInput.value.trim() === "") {
        soleError.textContent = "* Mã không được để trống";
        return false;
    } else if (nameSoleInput.value.trim() === "") {
        soleError.textContent = "* Tên không được để trống";
        return false;
    } else if (codeSoleInput.value.length > 10) {
        soleError.textContent = "* Mã <= 10 kí tự";
        return false;
    } else if (nameSoleInput.value.length > 50) {
        soleError.textContent = "* Tên <= 50 kí tự";
        return false;
    } else {
        soleError.textContent = "";
        return true;
    }
}

