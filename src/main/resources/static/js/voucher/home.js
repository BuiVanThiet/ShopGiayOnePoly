document.addEventListener("DOMContentLoaded", function () {
    // document.getElementById("button1").addEventListener("click", function () {
    //     changeTable(1);
    // });
    // document.getElementById("button2").addEventListener("click", function () {
    //     changeTable(2);
    // });

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

    var toastEl = document.querySelector('.custom-toast');
    if (toastEl) {
        var toast = new bootstrap.Toast(toastEl, {
            delay: 5000
        });
        toast.show();
    }

    document.querySelectorAll('.search-label').forEach(label => {
        label.addEventListener('click', function () {
            const targetSelector = this.getAttribute('data-target');
            const targetElement = document.querySelector(targetSelector);

            if (targetElement) {
                targetElement.style.display = (targetElement.style.display === 'none' || targetElement.style.display === '') ? 'block' : 'none';
            } else {
                console.error('Element not found:', targetSelector);
            }
        });
    });
    document.getElementById("discountType").addEventListener("change", function () {
        const selectedVoucherType = this.value;
        const maxiumPrice = document.getElementById("maxDiscount");
        const boxMaxiumPrice = document.getElementById("boxOfMaxiumDiscount");
        const valueVoucher = document.getElementById("value");
        if (selectedVoucherType === "2") {
            boxMaxiumPrice.style.display = 'none';
            maxiumPrice.value = valueVoucher.value;
        } else if (selectedVoucherType === '1') {
            boxMaxiumPrice.style.display = 'block';
        }
    })
});
