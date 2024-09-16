// Ẩn overlay sau 3 giây
window.addEventListener('load', () => {
    setTimeout(() => {
        document.getElementById('loadingOverlay').classList.add('hidden');
    }, 500); // 3000 milliseconds = 3 seconds
});