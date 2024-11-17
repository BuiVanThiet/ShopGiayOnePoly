const notifications = document.querySelector(".notifications-toast-custom");
const buttons = document.querySelectorAll(".buttons-toast-custom .btn");


const toastDetails = {
    timer: 5000,
    success: {
        icon: 'fa-circle-check',
        text: 'Success: This is a success toast.',
    },
    error: {
        icon: 'fa-circle-xmark',
        text: 'Error: This is an error toast.',
    },
    warning: {
        icon: 'fa-triangle-exclamation',
        text: 'Warning: This is a warning toast.',
    },
    info: {
        icon: 'fa-circle-info',
        text: 'Info: This is an information toast.',
    }
}

const removeToast = (toast) => {
    toast.classList.add("hide-toast-custom");
    if(toast.timeoutId) clearTimeout(toast.timeoutId); // Clearing the timeout for the toast
    setTimeout(() => toast.remove(), 500); // Removing the toast after 500ms
}
const removeAllToasts = () => {
    Array.from(notifications.children).forEach(toast => removeToast(toast));
}

const createToast = (check, mess) => {
    // Lấy chi tiết từ toastDetails dựa trên giá trị check
    removeAllToasts();
    if (check === '1') {
        check = 'success';
    }else if (check === '3') {
        check = 'error';
    }else if (check === '2') {
        check = 'warning';
    }else {
        check = 'info';
    }

    const { icon } = toastDetails[check];
    const toast = document.createElement("li");
    toast.className = `toast-toast-custom ${check}`;
    toast.innerHTML = `<div class="column-toast-custom">
                         <i class="fa-solid ${icon}"></i>
                         <span>${mess}</span>
                      </div>
                      <i class="fa-solid fa-xmark" onclick="removeToast(this.parentElement)"></i>`;

    notifications.appendChild(toast);
    toast.timeoutId = setTimeout(() => removeToast(toast), toastDetails.timer);
};

// Adding a click event listener to each button to create a toast when clicked
buttons.forEach(btn => {
    btn.addEventListener("click", () => createToast(btn.id));
});

document.addEventListener('DOMContentLoaded', function () {
    // Lấy giá trị message và check
    var messageElement = document.getElementById('toastMessage');
    var checkBGElement = document.getElementById('toastCheck');

    var message = messageElement ? messageElement.value : '';
    var checkBG = checkBGElement ? checkBGElement.value : '';
    console.log('du lieu cua mess: '+message + checkBG);

    // Hiển thị toast nếu có message
    if (message != '') {
        console.log('du lieu cua mess da loc: '+message + checkBG);
        // showToast(message, checkBG);
        createToast(checkBG, message);
    }
});

// createToast('success', 'Đây là thông báo thành công');
