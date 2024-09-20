window.onload = function() {
    document.getElementById('notifyBtn').addEventListener('click', function() {
        var notification = document.getElementById('notification');
        notification.classList.remove('hidden');
        notification.classList.add('visible');

        setTimeout(function() {
            notification.classList.remove('visible');
            setTimeout(function() {
                notification.classList.add('hidden');
            }, 500);
        }, 7000); // Hiển thị thông báo trong 5 giây
    });
}