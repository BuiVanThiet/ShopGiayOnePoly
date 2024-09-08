function showToast(mess,check) {
    //create toast
    var toastElement = document.createElement('div');
    toastElement.classList.add('toast');
    toastElement.setAttribute('role','alert');
    toastElement.setAttribute('aria-live','assertive');
    toastElement.setAttribute('aria-atomic','true');

    //check css
    var bgcolor = '';
    if(check == '1'){
        this.bgcolor = 'bg-success';
    }else if(check == '2') {
        this.bgcolor = 'bg-warning';
    }else {
        this.bgcolor = 'bg-danger';
    }
    //create conternt toast
    toastElement.innerHTML = `
            <div class="row">
                <div class="col-1 ${this.bgcolor}">
                  
                </div>
                <div class="col-11">
                  <div class="toast-header">
                    <strong class="me-auto">
                      ${mess}
                    </strong>
                    <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
                  </div>
                </div>
            </div>
        `;
    //create toast by container
    document.querySelector('.position-fixed').appendChild(toastElement);
    var toast = new bootstrap.Toast(toastElement, { delay: 5000 });
    toast.show();
}
document.addEventListener('DOMContentLoaded',function () {
    //take value
    var message = document.getElementById('toastMessage').value;
    var checkBG = document.getElementById('toastCheck').value;
    if(message) {
        console.log(message+checkBG)
        showToast(message,checkBG);
    }
});