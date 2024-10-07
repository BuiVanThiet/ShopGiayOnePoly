document.getElementById("discountType").addEventListener("change", function () {
    const selectValueType = this.value;
    const discountTextDola = document.getElementById("discountTextDola");
    const discountTextCash = document.getElementById("discountTextCash");

    if (selectValueType === "1") {
        discountTextDola.style.display = "inline";
        discountTextCash.style.display = "none";
    } else if (selectValueType === "2") {
        discountTextCash.style.display = "inline";
        discountTextDola.style.display = "none";
    } else {
        discountTextDola.style.display = "none";
        discountTextCash.style.display = "none";
    }
});
document.addEventListener("DOMContentLoaded", function () {
    var toastEl = document.querySelector('.custom-toast');
    if (toastEl) {
        var toast = new bootstrap.Toast(toastEl, {
            delay: 5000
        });
        toast.show();
    }
    document.querySelector('.custom-btn-close').addEventListener('click', function () {
        toast.hide();
    });
});
