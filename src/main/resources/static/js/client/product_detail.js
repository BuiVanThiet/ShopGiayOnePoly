document.addEventListener("DOMContentLoaded", function () {
    const toggles = document.querySelectorAll(".toggle-content");
    toggles.forEach(toggle => {
        toggle.addEventListener("click", function () {
            const content = this.nextElementSibling;
            const icon = this.querySelector(".toggle-icon");

            if (content.classList.contains("content-hidden")) {
                content.classList.remove("content-hidden");
                icon.classList.add("rotate");
            } else {
                content.classList.add("content-hidden");
                icon.classList.remove("rotate");
            }
        });
    });
    const voucherValue= document.getElementById("voucher-value-new");
    voucherValue.innerText =
});
