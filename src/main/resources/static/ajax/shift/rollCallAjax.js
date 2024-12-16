function checkInStaff() {
    $.ajax({
        type: 'POST',
        url: '/api-timekeeping/check-in-staff',  // Endpoint xử lý
        contentType: 'application/json',
        data: JSON.stringify({
            noteData: $('#noteDataCheckInEndCheckOut').val()
        }),
        success: function (response) {
            createToast(response.check, response.message);

            var formCheckShift = document.getElementById('formCheckShift');
            if(formCheckShift) {
                resetFilterTimekeeping();
            }
        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        }
    });
}

function checkOutStaff(check) {
    $.ajax({
        type: 'POST',
        url: '/api-timekeeping/check-out-staff',  // Endpoint xử lý
        contentType: 'application/json',
        data: JSON.stringify({
            noteData: $('#noteDataCheckInEndCheckOut').val()
        }),
        success: function (response) {
            if(check == 1) {
                createToast(response.check, response.message);
            }
            var formCheckShift = document.getElementById('formCheckShift');
            if(formCheckShift) {
                resetFilterTimekeeping();
            }

        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        }
    });
}
var isRequestInProgress = false


$(document).ready(function () {
    // checkEndTime90()
});