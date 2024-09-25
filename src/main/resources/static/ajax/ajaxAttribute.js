function saveRow(index) {
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
            console.log('Cập nhật thành công:', response);
            // Hiển thị thông báo thành công hoặc xử lý kết quả trả về từ server
        },
        error: function (xhr, status, error) {
            console.error('Có lỗi xảy ra khi cập nhật dữ liệu:', error);
            // Xử lý lỗi nếu cần thiết
        }
    });
}


function toggleStatus(element) {
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
        url: '/attribute/update-status',  // Đường dẫn API
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
            url: '/attribute/delete-color',  // Đường dẫn API để xóa
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
    fetchDeletedColors();

    // Ẩn nút "Danh sách đã xóa"
    this.style.display = 'none';

    // Hiển thị nút "Danh sách hoạt động"
    document.querySelector('.attribute-btn-listActive').style.display = 'inline-block';
});

document.querySelector('.attribute-btn-listActive').addEventListener('click', function () {
    fetchActiveColors();  // Bạn có thể tạo hàm này để lấy danh sách màu hoạt động

    // Ẩn nút "Danh sách hoạt động"
    this.style.display = 'none';

    // Hiển thị lại nút "Danh sách đã xóa"
    document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
});

function fetchDeletedColors() {
    // Thay URL dưới đây bằng endpoint của bạn để lấy danh sách màu đã xóa
    fetch('http://localhost:8080/attribute/color/delete')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Xóa các hàng cũ trong tbody
                const tbody = document.querySelector('#colorTable tbody');
                tbody.innerHTML = '';

                // Lặp qua các màu đã xóa và thêm các hàng vào bảng
                data.forEach((color, index) => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                            <td>${color.codeColor}</td>
                            <td>${color.nameColor}</td>
                            <td>${color.createDate}</td>
                            <td>${color.updateDate}</td>
                            <td>
                                <i class="attribute-status-icon status-icon fas ${color.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}" 
                                   data-status="${color.status}"></i>
                            </td>
                            <td>
                                <a href="#" onclick="restoreColor(${color.id})">
                                    <i class="attribute-icon-restore fas fa-undo" title="Khôi phục"></i>
                                </a>
                            </td>
                        `;
                    tbody.appendChild(row);
                });
            } else {
                alert("Không có màu nào đã bị xóa.");
            }
        })
        .catch(error => {
            console.error('Error fetching deleted colors:', error);
        });
}
document.querySelector('.attribute-btn-listActive').addEventListener('click', function () {
    fetchActiveColors();

    // Ẩn nút "Danh sách hoạt động"
    this.style.display = 'none';

    // Hiển thị lại nút "Danh sách đã xóa"
    document.querySelector('.attribute-btn-listDelete').style.display = 'inline-block';
});

function fetchActiveColors() {
    // Thay URL dưới đây bằng endpoint của bạn để lấy danh sách màu đang hoạt động
    fetch('http://localhost:8080/attribute/color/active')
        .then(response => response.json())
        .then(data => {
            if (data.length > 0) {
                // Xóa các hàng cũ trong tbody
                const tbody = document.querySelector('#colorTable tbody');
                tbody.innerHTML = '';

                // Lặp qua các màu đang hoạt động và thêm các hàng vào bảng
                data.forEach((color, index) => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                            <td>${color.codeColor}</td>
                            <td>${color.nameColor}</td>
                            <td>${color.createDate}</td>
                            <td>${color.updateDate}</td>
                            <td>
                                <i class="attribute-status-icon status-icon fas ${color.status == 1 ? 'fa-toggle-on' : 'fa-toggle-off'}" 
                                   data-status="${color.status}"></i>
                            </td>
                            <td>
                                <a href="#" onclick="editRow(${index})" id="edit-btn-${index}">
                                    <i class="attribute-icon-edit fas fa-edit" title="Edit"></i>
                                </a>
                                <a href="#" onclick="saveRow(${index})" style="display:none;" id="save-btn-${index}">
                                    <i class="attribute-icon-save fas fa-save" title="Save"></i>
                                </a>
                                <a href="#" onclick="deleteByID(this)" data-index="${index}">
                                    <i class="attribute-icon-delete fas fa-trash" title="Delete"></i>
                                </a>
                            </td>
                        `;
                    tbody.appendChild(row);
                });
            } else {
                alert("Không có màu nào đang hoạt động.");
            }
        })
        .catch(error => {
            console.error('Error fetching active colors:', error);
        });
}



function restoreColor(colorId) {
    // Logic để khôi phục màu bị xóa (thực hiện khi người dùng bấm vào icon khôi phục)
    alert('Khôi phục màu có ID: ' + colorId);
}

