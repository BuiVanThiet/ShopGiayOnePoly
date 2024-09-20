// Ẩn overlay sau 3 giây
// Hàm để cài đặt sự kiện load và ẩn lớp phủ tải sau một khoảng thời gian
// Hàm để ẩn lớp phủ tải
function hideLoadingOverlay(timeout) {
    setTimeout(() => {
        const loadingOverlay = document.getElementById('loadingOverlay');
        if (loadingOverlay) {
            loadingOverlay.classList.add('hidden');
        } else {
            console.error('Element with id "loadingOverlay" not found');
        }
    }, timeout);
}

// Cài đặt sự kiện load cho cửa sổ
function setupLoadingOnWindowLoad(timeout) {
    window.addEventListener('load', () => {
        hideLoadingOverlay(timeout);
    });
}

// Gọi hàm để cài đặt sự kiện load với thời gian chờ 500 milliseconds
setupLoadingOnWindowLoad(500);
