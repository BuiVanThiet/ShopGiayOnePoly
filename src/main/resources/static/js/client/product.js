$(function() {
    $("#slider-range").slider({
        range: true,
        min: 0,
        max: 10000000,
        values: [0, 10000000],
        slide: function(event, ui) {
            $("#minAmount").val(ui.values[0] + " ₫");A
            $("#maxAmount").val(ui.values[1] + " ₫");
        }
    });

    $("#minAmount").val($("#slider-range").slider("values", 0) + " ₫");
    $("#maxAmount").val($("#slider-range").slider("values", 1) + " ₫");

    $("#filterButton").on("click", function() {
        $("#filterMenu").toggle();
    });

    $("#closeButton").on("click", function() {
        $("#filterMenu").hide();
    });

    $("#priceLabel").on("click", function() {
        $("#priceFilter").toggleClass("show");
        $("#brandFilter, #sizeFilter, #colorFilter").removeClass("show");
    });

    $("#brandLabel").on("click", function() {
        $("#brandFilter").toggleClass("show");
        $("#priceFilter, #sizeFilter, #colorFilter").removeClass("show");
    });

    $("#sizeLabel").on("click", function() {
        $("#sizeFilter").toggleClass("show");
        $("#priceFilter, #brandFilter, #colorFilter").removeClass("show");
    });

    $("#colorLabel").on("click", function() {
        $("#colorFilter").toggleClass("show");
        $("#priceFilter, #brandFilter, #sizeFilter").removeClass("show");
    });
});
