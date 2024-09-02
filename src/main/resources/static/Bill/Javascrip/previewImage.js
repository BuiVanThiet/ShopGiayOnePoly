function previewImage(event) {
    const reader = new FileReader();
    reader.onload = function () {
        const output = document.getElementById('image-preview');
        output.innerHTML = '<img src="' + reader.result + '" class="img-fluid" alt="Ảnh đã chọn" style="max-height: 150px;">';
    };
    reader.readAsDataURL(event.target.files[0]);
}