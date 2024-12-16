function timekeepingList(page) {
    $.ajax({
        type: "GET",
        url: "/api-timekeeping/list/" + page,
        success: function (response) {
            var tbody = $('#tableTimekeeping');
            var noDataContainer = $('#noDataTimekeeping-manage');
            tbody.empty(); // Xóa các dòng cũ
            if (response.length === 0) {
                // Nếu không có dữ liệu, hiển thị ảnh
                noDataContainer.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có đợt điểm danh nào!</p>
                    `);
                noDataContainer.show();
                tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            } else {
                noDataContainer.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
                tbody.closest('table').show(); // Hiển thị lại table nếu có dữ liệu
                response.forEach(function(timekeeping, index) {
                    var timeShift = '';
                    if(timekeeping[3] == null) {
                        timeShift = `Không có ca làm`;
                    }else {
                        timeShift = `${timekeeping[3]} -> ${timekeeping[4]}`;
                    }
                    tbody.append(`
                    <tr>
                        <td scope="row">${timekeeping[2]}</td>
                        <td> ${timeShift}</td>
                        <td>${timekeeping[8]}</td>
                        <td>${timekeeping[6]}</td>
                        <td>${timekeeping[11]}</td>
                        <td>${timekeeping[7]}</td>
                    </tr>
                    `)
                })

            }
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(maxPageBillDetailByIdBill, 5000);  // Gửi lại sau 5 giây
        // }
    })
}

function maxPageTimekeeping() {
    $.ajax({
        type: "GET",
        url:"/api-timekeeping/max-page-timekeeping",
        success: function (response) {
            createPagination('maxPageTimekeeping-manage', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function filterTimekeeping() {
    $.ajax({
        type: "POST",
        url:"/api-timekeeping/filter-timekeeping",
        contentType: 'application/json',
        data: JSON.stringify({
            searchTerm: $('#keySearchStaffShiftTimekeeping').val(),  // Convert to 24-hour format
            startDate: $('#dateStart').val(),
            endDate: $('#dateEnd').val(),
            startTime: convertTo24HourFormat($('#timeStart').val()),
            endTime: convertTo24HourFormat($('#timeEnd').val()),
            timekeeping_typeCheck: $('#timekeepingType').val() === '' ? null : parseInt($('#timekeepingType').val())
        }),
        success: function (response) {
            timekeepingList(1);
            maxPageTimekeeping();
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function resetFilterTimekeeping() {
    // Reset các trường khác
    $('#keySearchStaffShiftTimekeeping').val('');
    $('#timeStart').val('00:00');
    $('#timeEnd').val('23:59');
    $('#timekeepingType').val('');

    // Lấy ngày hôm nay
    var today = new Date();

    // Định dạng ngày theo 'yyyy-MM-dd'
    var day = String(today.getDate()).padStart(2, '0');
    var month = String(today.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
    var year = today.getFullYear();
    var formattedDate = year + '-' + month + '-' + day;

    // Gán giá trị ngày hôm nay cho dateStart và dateEnd
    $('#dateStart').val(formattedDate);
    $('#dateEnd').val(formattedDate);

    filterTimekeeping();
}

$(document).ready(function () {
    resetFilterTimekeeping();
});