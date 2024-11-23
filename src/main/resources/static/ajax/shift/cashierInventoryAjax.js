function cashierInventoryList(page) {
    $.ajax({
        type: "GET",
        url: "/api-cashierInventory/list/" + page,
        success: function (response) {
            console.log(response)
            var tbody = $('#tableCashierInventory');
            var noDataContainer = $('#noDataCashierInventory-manage');
            tbody.empty(); // Xóa các dòng cũ
            if (response.length === 0) {
                // Nếu không có dữ liệu, hiển thị ảnh
                noDataContainer.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có danh sách chốt đơn nào!</p>
                    `);
                noDataContainer.show();
                tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            } else {
                noDataContainer.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
                tbody.closest('table').show(); // Hiển thị lại table nếu có dữ liệu
                response.forEach(function(cashierInventory, index) {
                    tbody.append(`
                        <tr>
                            <th scope="row">${cashierInventory[2]}</th>
                            <td>${cashierInventory[3]} -> ${cashierInventory[4]}</td>
                            <td>${Math.trunc(cashierInventory[5]).toLocaleString('en-US')} VNĐ</td>
                            <td>${Math.trunc(cashierInventory[6]).toLocaleString('en-US')} VNĐ</td>
                            <td>${Math.trunc(cashierInventory[7]).toLocaleString('en-US')} VNĐ</td>
                            <td>${Math.trunc(cashierInventory[5] - (cashierInventory[6] - cashierInventory[7])).toLocaleString('en-US')} VNĐ</td>

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

function maxPageCashierInventory() {
    $.ajax({
        type: "GET",
        url:"/api-cashierInventory/max-page-cashierInventory",
        success: function (response) {
            createPagination('maxPageCashierInventory-manage', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function filterCashierInventory() {
    $.ajax({
        type: "POST",
        url:"/api-cashierInventory/filter-cashierInventory",
        contentType: 'application/json',
        data: JSON.stringify({
            keyStaff: $('#keySearchCashierInventory').val(),  // Convert to 24-hour format
            startDate: $('#startDateCashierInventory').val(),
            endDate: $('#endDateCashierInventory').val()
        }),
        success: function (response) {
            cashierInventoryList(1);
            maxPageCashierInventory();
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function resetFilterCashierInventory() {
    // Reset các trường khác
    $('#keySearchCashierInventory').val('');
    // Lấy ngày hôm nay
    var today = new Date();

    // Định dạng ngày theo 'yyyy-MM-dd'
    var day = String(today.getDate()).padStart(2, '0');
    var month = String(today.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
    var year = today.getFullYear();
    var formattedDate = year + '-' + month + '-' + day;
    // Gán giá trị ngày hôm nay cho dateStart và dateEnd
    $('#startDateCashierInventory').val(formattedDate);
    $('#endDateCashierInventory').val(formattedDate);
    filterCashierInventory();
}

$(document).ready(function () {
    resetFilterCashierInventory()
});