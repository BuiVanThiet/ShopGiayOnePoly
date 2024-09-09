function enableEditing(editIcon) {
    const row = editIcon.closest('tr');
    const spans = row.querySelectorAll('span');
    const inputs = row.querySelectorAll('input, textarea');
    const saveIcon = row.querySelector('.save-icon');

    // Ẩn các span và hiển thị input/textarea
    spans.forEach(span => {
        span.style.display = 'none';
    });

    inputs.forEach(input => {
        input.style.display = 'block';
    });

    // Ẩn nút Edit và hiển thị nút Save
    editIcon.style.display = 'none';
    saveIcon.style.display = 'inline';
}

function saveChanges(saveButton) {
    const row = saveButton.closest('tr');
    const inputs = row.querySelectorAll('input, textarea');
    const spans = row.querySelectorAll('span');
    const editIcon = row.querySelector('.edit-icon');

    // Lưu nội dung từ input/textarea vào span và hiển thị lại span
    inputs.forEach(input => {
        const span = input.previousElementSibling;
        span.textContent = input.value;
        span.style.display = 'inline';
        input.style.display = 'none';
    });

    // Ẩn nút Save và hiển thị nút Edit
    saveButton.style.display = 'none';
    editIcon.style.display = 'inline';
}
