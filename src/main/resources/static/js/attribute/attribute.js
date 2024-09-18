function editRow(index) {
    console.log('Editing row:', index);
    document.getElementById('code-input-' + index).style.display = 'inline-block';
    document.getElementById('name-input-' + index).style.display = 'inline-block';

    document.getElementById('code-text-' + index).style.display = 'none';
    document.getElementById('name-text-' + index).style.display = 'none';


    document.getElementById('edit-btn-' + index).style.display = 'none';
    document.getElementById('save-btn-' + index).style.display = 'inline-block';
}

function saveRow(index) {
    console.log('Saving row:', index);
    var updatedData = {
        codeColor: document.getElementById('code-input-' + index).value,
        nameColor: document.getElementById('name-input-' + index).value,

    };

    document.getElementById('code-text-' + index).innerText = updatedData.codeColor;
    document.getElementById('name-text-' + index).innerText = updatedData.nameColor;


    document.getElementById('code-input-' + index).style.display = 'none';
    document.getElementById('name-input-' + index).style.display = 'none';


    document.getElementById('code-text-' + index).style.display = 'inline-block';
    document.getElementById('name-text-' + index).style.display = 'inline-block';


    document.getElementById('edit-btn-' + index).style.display = 'inline-block';
    document.getElementById('save-btn-' + index).style.display = 'none';

    console.log(updatedData);
}

function toggleStatus(icon) {
    var currentStatus = parseInt(icon.getAttribute('data-status'));
    var newStatus = currentStatus === 1 ? 0 : 1;
    var index = icon.getAttribute('data-index');

    // Cập nhật biểu tượng và trạng thái
    icon.classList.toggle('fa-toggle-on', newStatus === 1);
    icon.classList.toggle('fa-toggle-off', newStatus === 0);
    icon.setAttribute('data-status', newStatus);

    // Nếu cần, gửi dữ liệu mới tới server bằng AJAX
    var updatedData = {
        id: index,
        status: newStatus
    };

    console.log(updatedData);
    // Ví dụ sử dụng fetch để gửi dữ liệu đến server
    // fetch('/update-status', {
    //     method: 'POST',
    //     headers: {
    //         'Content-Type': 'application/json'
    //     },
    //     body: JSON.stringify(updatedData)
    // }).then(response => response.json())
    //   .then(data => console.log('Success:', data))
    //   .catch(error => console.error('Error:', error));
}
