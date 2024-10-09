document.getElementById('file-upload').onchange = function (event) {
    const [file] = event.target.files;
    if (file) {
    const imagePreview = document.getElementById('image-preview');
    imagePreview.src = URL.createObjectURL(file);
    }
};
