
    document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("searchStartDate").addEventListener("change", function() {
        autoSubmitForm();
    });
    document.getElementById("searchEndDate").addEventListener("change", function() {
    autoSubmitForm();
});

    function autoSubmitForm() {
    var startDate = document.getElementById("searchStartDate").value;
    var endDate = document.getElementById("searchEndDate").value;

    var url = "/voucher/search-date?startDate=" + startDate + "&endDate=" + endDate;

    window.location.href = url;
}
});
