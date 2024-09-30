document.addEventListener("DOMContentLoaded", function () {
    // Hàm chuyển đổi giữa các bảng
    window.changeTable = function (tableNumber) {
        const tableVoucher = document.getElementById("table-voucher");
        const tableVoucherDelete = document.getElementById("table-voucher-delete");
        const tableVoucherExpired = document.getElementById("table-voucher-expired");

        // Ẩn tất cả bảng trước
        tableVoucher.style.display = "none";
        tableVoucherDelete.style.display = "none";
        tableVoucherExpired.style.display = "none";

        // Reset tất cả các nút về trạng thái "btn-secondary"
        document.getElementById("button1").classList.remove("btn-primary");
        document.getElementById("button1").classList.add("btn-secondary");
        document.getElementById("button2").classList.remove("btn-primary");
        document.getElementById("button2").classList.add("btn-secondary");
        document.getElementById("button3").classList.remove("btn-primary");
        document.getElementById("button3").classList.add("btn-secondary");

        if (tableNumber === 1) {
            // Hiển thị bảng 1 và thay đổi trạng thái nút
            tableVoucher.style.display = "block";
            document.getElementById("button1").classList.add("btn-primary");
            document.getElementById("button1").classList.remove("btn-secondary");
        } else if (tableNumber === 2) {
            // Hiển thị bảng 2 và thay đổi trạng thái nút
            tableVoucherDelete.style.display = "block";
            document.getElementById("button2").classList.add("btn-primary");
            document.getElementById("button2").classList.remove("btn-secondary");
        } else if (tableNumber === 3) {
            // Hiển thị bảng 3 và thay đổi trạng thái nút
            tableVoucherExpired.style.display = "block";
            document.getElementById("button3").classList.add("btn-primary");
            document.getElementById("button3").classList.remove("btn-secondary");
        }
    };

    changeTable(1);


    // Thêm sự kiện click cho các nút
    document.getElementById("button1").addEventListener("click", function () {
        changeTable(1);
    });

    document.getElementById("button2").addEventListener("click", function () {
        changeTable(2);
    });

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

    var toastEl = document.querySelector('.toast');
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
});
