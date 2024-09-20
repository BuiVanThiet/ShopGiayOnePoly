// function showToast(mess,check) {
//
//     //create toast
//     var toastElement = document.createElement('div');
//     toastElement.classList.add('toast');
//     toastElement.setAttribute('role','alert');
//     toastElement.setAttribute('aria-live','assertive');
//     toastElement.setAttribute('aria-atomic','true');
//
//     //check css
//     var bgcolor = '';
//     if(check == '1'){
//         this.bgcolor = 'bg-success';
//     }else if(check == '2') {
//         this.bgcolor = 'bg-warning';
//     }else {
//         this.bgcolor = 'bg-danger';
//     }
//     //create conternt toast
//     toastElement.innerHTML = `
//             <div class="row">
//                 <div class="col-1 ${this.bgcolor}">
//
//                 </div>
//                 <div class="col-11">
//                   <div class="toast-header">
//                     <strong class="me-auto">
//                       ${mess}
//                     </strong>
//                     <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
//                   </div>
//                 </div>
//             </div>
//         `;
//     //create toast by container
//     document.querySelector('.position-fixed').appendChild(toastElement);
//     var toast = new bootstrap.Toast(toastElement, { delay: 5000 });
//     toast.show();
// }
// document.addEventListener('DOMContentLoaded',function () {
//     //take value
//     var message = document.getElementById('toastMessage').value;
//     var checkBG = document.getElementById('toastCheck').value;
//     if(message) {
//         console.log(message+checkBG)
//         showToast(message,checkBG);
//     }
// });

function showToast(mess, check) {
    // Lấy container chứa các toast
    var toastContainer = document.querySelector('.position-fixed');

    // Kiểm tra nếu số lượng toast đã lớn hơn 5, xóa cái cũ nhất
    var toasts = toastContainer.querySelectorAll('.toast');
    if (toasts.length >= 5) {
        toastContainer.removeChild(toasts[0]);  // Xóa toast đầu tiên (cũ nhất)
    }

    // Tạo toast mới
    var toastElement = document.createElement('div');
    toastElement.classList.add('toast');
    toastElement.setAttribute('role', 'alert');
    toastElement.setAttribute('aria-live', 'assertive');
    toastElement.setAttribute('aria-atomic', 'true');

    // Kiểm tra màu sắc theo giá trị của check
    var bgcolor = '';
    if (check == '1') {
        bgcolor = 'bg-success';
    } else if (check == '2') {
        bgcolor = 'bg-warning';
    } else {
        bgcolor = 'bg-danger';
    }

    // Tạo nội dung toast
    toastElement.innerHTML = `
        <div class="row">
            <div class="col-1 ${bgcolor}"></div>
            <div class="col-11">
              <div class="toast-header">
                <strong class="me-auto">${mess}</strong>
                <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
              </div>
            </div>
        </div>
    `;

    // Thêm toast vào container
    toastContainer.appendChild(toastElement);

    // Hiển thị toast
    var toast = new bootstrap.Toast(toastElement, { delay: 5000 });
    toast.show();
}

document.addEventListener('DOMContentLoaded', function () {
    // Lấy giá trị message và check
    var message = document.getElementById('toastMessage').value;
    var checkBG = document.getElementById('toastCheck').value;

    // Hiển thị toast nếu có message
    if (message) {
        console.log(message + checkBG);
        showToast(message, checkBG);

    }
});



