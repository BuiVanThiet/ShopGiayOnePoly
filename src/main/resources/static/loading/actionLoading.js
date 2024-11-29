// Hàm để ẩn lớp phủ tải sau một khoảng thời gian
function hideLoadingOverlay(timeout) {
    setTimeout(() => {
        const loadingOverlay = document.getElementById('loadingOverlay');
        if (loadingOverlay) {
            loadingOverlay.classList.add('hidden'); // Ẩn lớp phủ tải
        } else {
            console.error('Element with id "loadingOverlay" not found');
        }
    }, timeout);
}

// Hàm để hiển thị lớp phủ tải
function showLoadingOverlay() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.classList.remove('hidden'); // Hiển thị lớp phủ tải
    } else {
        console.error('Element with id "loadingOverlay" not found');
    }
}

// Cài đặt sự kiện load cho cửa sổ
function setupLoadingOnWindowLoad(timeout) {
    window.addEventListener('load', () => {
        hideLoadingOverlay(timeout); // Ẩn lớp phủ tải sau khi tải xong
    });
}

// Cài đặt sự kiện click cho nút hoặc hành động khác
function setupLoadingOnButtonClick(timeout) {
    // console.log('buttonId(onload): ' + buttonId);

    // const button = document.getElementById(buttonId);
    // if (button) {
        // button.addEventListener('click', () => {
            showLoadingOverlay(); // Hiển thị lớp phủ tải khi nhấn nút
            hideLoadingOverlay(timeout); // Ẩn lớp phủ tải sau một khoảng thời gian
        // });
    // } else {
    //     console.error(`Button with id "${buttonId}" not found`);
    // }
}

// Gọi hàm để cài đặt sự kiện load với thời gian chờ 500 milliseconds
setupLoadingOnWindowLoad(500);

// Gọi hàm để cài đặt sự kiện click cho nút "loadButton" với thời gian chờ 3000 milliseconds
