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
document.querySelectorAll('.search-label').forEach(label => {
    label.addEventListener('click', function () {
        const target = document.querySelector(this.getAttribute('data-target'));
        if (target.style.display === 'none' || target.style.display === '') {
            target.style.display = 'block';
        } else {
            target.style.display = 'none';
        }
    });
});
document.querySelectorAll('.search-label').forEach(label => {
    label.addEventListener('click', function () {
        const target = document.querySelector(this.getAttribute('data-target'));
        target.style.display = (target.style.display === 'none' || target.style.display === '') ? 'block' : 'none';
    });
});

$(function() {
    $("#slider-range").slider({
        range: true,
        min: 0,
        max: 10000000,
        values: [0, 10000000],
        slide: function(event, ui) {
            $("#minAmount").val(ui.values[0] + " ₫");
            $("#maxAmount").val(ui.values[1] + " ₫");
        }
    });

    $("#minAmount").val($("#slider-range").slider("values", 0) + " ₫");
    $("#maxAmount").val($("#slider-range").slider("values", 1) + " ₫");
});

