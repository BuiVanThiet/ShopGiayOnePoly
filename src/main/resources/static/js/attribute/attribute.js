function editRow(index) {
    console.log('Editing row:', index);
    document.getElementById('code-input-' + index).style.display = 'inline-block';
    document.getElementById('name-input-' + index).style.display = 'inline-block';

    document.getElementById('code-text-' + index).style.display = 'none';
    document.getElementById('name-text-' + index).style.display = 'none';


    document.getElementById('edit-btn-' + index).style.display = 'none';
    document.getElementById('save-btn-' + index).style.display = 'inline-block';
}





