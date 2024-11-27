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
var isRequestInProgress = false
function checkEndTime90() {
    if (isRequestInProgress) return; // Nếu đang có yêu cầu Ajax, không gửi yêu cầu mới

    isRequestInProgress = true; // Đánh dấu là đang gọi Ajax

    $.ajax({
        type: 'GET',
        url: '/api-timekeeping/check-time-out-90',  // Endpoint xử lý
        success: function (response) {
            // createToast('3','Đã đến giới hạn điểm danh ra về, hệ thống đã tự động điểm danh ra về!')
            console.log(response)
            if(response == 'Khẩn cấp') {
                createToast('3','Đã đến giới hạn điểm danh ra về, hệ thống đã tự động điểm danh ra về!')
                checkOutStaff(2);
            }
        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        },
        complete: function() {
            isRequestInProgress = false; // Khi hoàn thành, đánh dấu yêu cầu Ajax đã xong
            setTimeout(checkEndTime90, 10000); // Gửi lại sau 10 giây
        }
    });
}

$(document).ready(function () {
    checkEndTime90()
});