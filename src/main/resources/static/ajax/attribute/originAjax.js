function saveRow(index) { // hàm edit dữ liệu trên table
    console.log('Saving row:', index);
    var updatedData = {
        codeOrigin: document.getElementById('code-input-' + index).value,
        nameOrigin: document.getElementById('name-input-' + index).value,
        id: document.getElementById('row-' + index).getAttribute('data-id')  // Lấy ID của đối tượng từ hàng
    };

    // Hiển thị lại các giá trị đã chỉnh sửa trên trang
    document.getElementById('code-text-' + index).innerText = updatedData.codeOrigin;
    document.getElementById('name-text-' + index).innerText = updatedData.nameOrigin;

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
        url: '/attribute/update-origin',  // Thay bằng URL API của bạn
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(updatedData),  // Gửi dữ liệu JSON
        success: function (response) {
            console.log('Cập nhật thành công:', response);
            // Hiển thị thông báo thành công hoặc xử lý kết quả trả về từ server
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
        url: '/attribute//origin/update-status',  // Đường dẫn API
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

function deleteByID(element) {
    if (confirm('Bạn có chắc chắn muốn xóa mục này không?')) {
        var index = element.getAttribute('data-index');  // Lấy index từ th:data-index
        var id = $('#row-' + index).data('id');  // Lấy id của phần tử từ th:data-id của hàng

        $.ajax({
            url: '/attribute/delete-origin',  // Đường dẫn API để xóa
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                id: id,
                status: 0  // Giả sử status 0 là trạng thái bị xóa
            }),
            success: function (response) {
                if (response.success) {
                    // Xóa hàng trong bảng mà không cần reload trang
                    $('#row-' + index).remove();  // Xóa hàng với id là row-index
                    console.log('Xóa thành công');
                } else {
                    alert('Xóa thất bại!');
                }
            },
            error: function (xhr, status, error) {
                console.error('Có lỗi xảy ra khi xóa:', error);
            }
        });
    }
}

document.querySelector('.attribute-btn-listDelete').addEventListener('click', function () {
    fetch('http://localhost:8080/attribute/origin/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                fetchDeletedOrigins();
                this.style.display = 'none';
                document.querySelector('.attribute-btn-listActive').style.display = 'inline-block';
            } else {
                alert("Không có mục nào bị xóa.");
            }
        });
});

document.querySelector('.attribute-btn-listActive').addEventListener('click', function () {
    fetchActiveOrigins();
    this.style.display = 'none';
    document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
});

function fetchDeletedOrigins() {
    // Thay URL dưới đây bằng endpoint của bạn để lấy danh sách màu đã xóa
    fetch('http://localhost:8080/attribute/origin/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Xóa các hàng cũ trong tbody
                const tbody = document.querySelector('#originTable tbody');
                tbody.innerHTML = '';

                // Lặp qua các màu đang hoạt động và thêm các hàng vào bảng
                data.forEach((origin, index) => {
                    const row = document.createElement('tr');
                    row.id = `row-${index}`;
                    row.setAttribute('data-id', origin.id);
                    row.innerHTML = `
                        <td>
                            <span id="code-text-${index}">${origin.codeOrigin}</span>
                            <input type="text" value="${origin.codeOrigin}" id="code-input-${index}" style="display:none;">
                        </td>
                        <td>
                            <span id="name-text-${index}">${origin.nameOrigin}</span>
                            <input type="text" value="${origin.nameOrigin}" id="name-input-${index}" style="display:none;">
                        </td>
                        <td>${origin.createDate}</td>
                        <td>${origin.updateDate}</td>
                        <td>
                            <i class="attribute-status-icon status-icon fas ${origin.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}"
                               data-status="${origin.status}"
                               data-index="${index}"
                               title="Toggle Status"></i>
                        </td>
                        <td>
                                <a data-index="${index}"  onclick="restoreOrigin(this)">
                                    <i class="attribute-icon-restore fas fa-undo" title="Khôi phục"></i>
                                </a>
                        </td>
                    `;
                    tbody.appendChild(row);
                });
            } else {
                alert("Không có mục nào bị xóa.");
            }
        })
        .catch(error => {
            console.error('Error fetching active origins:', error);
        });
}


function fetchActiveOrigins() {
    fetch('http://localhost:8080/attribute/origin/active')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {

                const tbody = document.querySelector('#originTable tbody');
                tbody.innerHTML = '';

                data.forEach((origin, index) => {
                    const row = document.createElement('tr');
                    row.id = `row-${index}`;
                    row.setAttribute('data-id', origin.id);
                    row.innerHTML = `
                        <td>
                            <span id="code-text-${index}">${origin.codeOrigin}</span>
                            <input class="inputUpdate-attribute" type="text" value="${origin.codeOrigin}" id="code-input-${index}" style="display:none;">
                        </td>
                        <td>
                            <span id="name-text-${index}">${origin.nameOrigin}</span>
                            <input class="inputUpdate-attribute" type="text" value="${origin.nameOrigin}" id="name-input-${index}" style="display:none;">
                        </td>
                        <td>${origin.createDate}</td>
                        <td>${origin.updateDate}</td>
                        <td>
                            <i class="attribute-status-icon status-icon fas ${origin.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}"
                               data-status="${origin.status}"
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
                            <a href="#" data-index="${index}" onclick="deleteByID(this)">
                                <i class="attribute-icon-delete icon-delete fas fa-trash" title="Delete"></i>
                            </a>
                        </td>
                    `;
                    tbody.appendChild(row);
                });
            } else {
                alert("Không có mục nào đang hoạt động.");
            }
        })
}


function restoreOrigin(element) {
    if (confirm('Bạn có chắc chắn muốn khôi phục mục này không?')) {
        var index = element.getAttribute('data-index');
        var id = $('#row-' + index).data('id');

        $.ajax({
            url: '/attribute/delete-origin',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                id: id,
                status: 1
            }),
            success: function (response) {
                if (response.success) {
                    $('#row-' + index).remove();
                    console.log('Khôi phục thành công');
                } else {
                    alert('Khôi phục thất bại!');
                }
            },
            error: function (xhr, status, error) {
                console.error('Có lỗi xảy ra khi khôi phục:', error);
            }
        });
    }
}

