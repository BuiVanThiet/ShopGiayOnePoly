function autoSubmitForm() {
    var startDate = document.getElementById("searchStartDate").value;
    var endDate = document.getElementById("searchEndDate").value;

    if (startDate && endDate) {
        // Đảm bảo định dạng ngày là YYYY-MM-DD
        var formattedStartDate = new Date(startDate).toISOString().split('T')[0];
        var formattedEndDate = new Date(endDate).toISOString().split('T')[0];

        var url = "/voucher/search-date?startDate=" + formattedStartDate + "&endDate=" + formattedEndDate;
        window.location.href = url;
    }
}
