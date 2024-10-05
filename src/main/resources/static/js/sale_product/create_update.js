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
