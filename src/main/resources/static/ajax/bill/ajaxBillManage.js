
function loadBillStatusByBillId() {
    $.ajax({
        type: "GET",
        url: "/bill-api/show-status-bill",
        success: function (response) {
            response.forEach(function (invoiceBill) {
                console.log('xem co bao nhieu trang thai ' + invoiceBill.status)
            })
        },
        error: function (xhr) {
            console.error('loi hien thi trnag thai' + xhr.responseText);
        }
    })
}


$(document).ready(function () {
    loadBillStatusByBillId();
});