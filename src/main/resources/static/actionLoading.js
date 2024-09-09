// Ẩn overlay sau 3 giây
window.addEventListener('load', () => {
    setTimeout(() => {
        document.getElementById('loadingOverlay').classList.add('hidden');
    }, 1000); // 3000 milliseconds = 3 seconds
});