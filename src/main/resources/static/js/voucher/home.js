    function changeTextTypeVoucher() {
        var selectElement = document.getElementById("discountType");
        var selectedValue = selectElement.options[selectElement.selectedIndex].value;

        var valueReduce = "";

        if (selectedValue === "1") {
            valueReduce = "%";
        } else {
            valueReduce = "₫";
        }
        document.getElementById("discountText").innerText = valueReduce;
    }

    window.onload = function () {
        var discountTypeSelect = document.getElementById("discountType");
        if (discountTypeSelect) {
                discountTypeSelect.addEventListener("change", changeTextTypeVoucher);
            changeTextTypeVoucher();
        }
    };

    function changeTable(tableNumber) {
        var tableVoucher = document.getElementById("table-voucher");
        var tableVoucherDelete = document.getElementById("table-voucher-delete");

        if (tableNumber === 1) {
            tableVoucher.style.display = "block";
            tableVoucherDelete.style.display = "none";
            document.getElementById("button1").classList.add("btn-primary");
            document.getElementById("button1").classList.remove("btn-secondary");
            document.getElementById("button2").classList.add("btn-secondary");
            document.getElementById("button2").classList.remove("btn-primary");
        } else if (tableNumber === 2) {
            tableVoucher.style.display = "none";
            tableVoucherDelete.style.display = "block";
            document.getElementById("button2").classList.add("btn-primary");
            document.getElementById("button2").classList.remove("btn-secondary");
            document.getElementById("button1").classList.add("btn-secondary");
            document.getElementById("button1").classList.remove("btn-primary");
        }
    }

    window.onload = function () {
        changeTable(1);
    };
