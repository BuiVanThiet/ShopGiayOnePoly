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

function checkOutStaff() {
    $.ajax({
        type: 'POST',
        url: '/api-timekeeping/check-out-staff',  // Endpoint xử lý
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

            // if(response.buttonCheckInAndCheckOut === 'checkIn') {
            //     $('#buttonCheckInAndCheckOut').html(`<button type="button" class="btn btn-outline-success" onclick="checkInStaff()">Điểm danh vào làm</button>`)
            // }else {
            //     $('#buttonCheckInAndCheckOut').html(`<button type="button" class="btn btn-outline-danger" onclick="checkOutStaff()">Điểm danh ra về</button>`)
            // }
        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        }
    });
}