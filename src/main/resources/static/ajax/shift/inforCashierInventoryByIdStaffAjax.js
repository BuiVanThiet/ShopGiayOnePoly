function cashierInventoryListByIdStaff(page) {
    $.ajax({
        type: "GET",
        url: "/api-cashierInventory/infor-detail-cashierInventory/" + page,
        success: function (response) {
            console.log(response)
            var tbody = $('#tableCashierInventoryByIdBill');
            var noDataContainer = $('#noDataCashierInventoryByIdBill-manage');
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
                            <td>${cashierInventory[8]}</td>
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

function maxPageCashierInventoryByIdStaff() {
    $.ajax({
        type: "GET",
        url:"/api-cashierInventory/max-page-cashierInventory-by-idStaff",
        success: function (response) {
            createPagination('maxPageCashierInventoryByIdBill-manage', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function filterCashierInventoryByIdStaff() {
    $.ajax({
        type: "POST",
        url:"/api-cashierInventory/filter-cashierInventory-by-idStaff",
        contentType: 'application/json',
        data: JSON.stringify({
            startDate: $('#startDateCashierInventoryByIdBill').val(),
            endDate: $('#endDateCashierInventoryByIdBill').val(),
            startTime: $('#startTimeCashierInventoryByIdBill').val(),
            endTime: $('#endTimeCashierInventoryByIdBill').val()
        }),
        success: function (response) {
            cashierInventoryListByIdStaff(1);
            maxPageCashierInventoryByIdStaff();
            filterInvoiceStatusByStaffByIdStaff();

        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function resetFilterCashierInventoryByIdStaff() {
    // Reset các trường khác
    var today = new Date();

    // Định dạng ngày theo 'yyyy-MM-dd'
    var day = String(today.getDate()).padStart(2, '0');
    var month = String(today.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
    var year = today.getFullYear();
    var formattedDate = year + '-' + month + '-' + day;
    // Gán giá trị ngày hôm nay cho dateStart và dateEnd
    $('#startDateCashierInventoryByIdBill').val(formattedDate);
    $('#endDateCashierInventoryByIdBill').val(formattedDate);

    // Lấy giờ hiện tại
    var currentDate = new Date();

// Lấy giờ, phút và giây dưới dạng chuỗi 'HH:mm:ss'
    var hours = currentDate.getHours().toString().padStart(2, '0');
    var minutes = currentDate.getMinutes().toString().padStart(2, '0');
    var seconds = currentDate.getSeconds().toString().padStart(2, '0');

// Định dạng giờ hiện tại thành 'HH:mm:ss'
    var currentTime = hours + ':' + minutes + ':' + seconds;

// Đặt giá trị cho các trường input
    $('#startTimeCashierInventoryByIdBill').val(currentTime);
    $('#endTimeCashierInventoryByIdBill').val(currentTime);


    filterCashierInventoryByIdStaff();
}

function invoiceStatusByStaffByIdStaff(page) {
    $.ajax({
        type: "GET",
        url: "/bill-api/get-invoice-status-by-staff/" + page,
        success: function (response) {
            console.log(response)
            var tbody = $('#tableInvoiceStatusByStaff');
            var noDataContainer = $('#noDataInvoiceStatusByStaff-manage');
            tbody.empty(); // Xóa các dòng cũ
            if (response.length === 0) {
                // Nếu không có dữ liệu, hiển thị ảnh
                noDataContainer.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có hóa đơn nào!</p>
                    `);
                noDataContainer.show();
                tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            } else {
                noDataContainer.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
                tbody.closest('table').show(); // Hiển thị lại table nếu có dữ liệu
                response.forEach(function(invoiceStatus, index) {
                    var statusBill = ''; // Biến để lưu trạng thái hóa đơn

                    if (invoiceStatus[2] === 0) {
                        statusBill = 'Tạo hóa đơn';
                    } else if (invoiceStatus[2] === 1) {
                        statusBill = 'Chờ Xác Nhận';
                    } else if (invoiceStatus[2] === 2) {
                        statusBill = 'Đã xác nhận';
                    } else if (invoiceStatus[2] === 3) {
                        statusBill = 'Giao Hàng';
                    } else if (invoiceStatus[2] === 4) {
                        statusBill = 'Khách Đã Nhận Được Hàng';
                    } else if (invoiceStatus[2] === 5) {
                        statusBill = 'Đơn Hàng Đã Hoàn Thành';
                    } else if (invoiceStatus[2] === 6) {
                        statusBill = 'Đã Hủy';
                    } else if (invoiceStatus[2] === 101) {
                        statusBill = 'Đã Thanh Toán';
                    } else if (invoiceStatus[2] === 102) {
                        statusBill = 'Đã Thanh Toán(phiếu đổi-trả)';
                    } else if (invoiceStatus[2] === 201) {
                        statusBill = 'Chờ xác nhận đổi-trả hàng';
                    } else if (invoiceStatus[2] === 202) {
                        statusBill = 'Đồng ý đổi-trả hàng';
                    } else if (invoiceStatus[2] === 203) {
                        statusBill = 'Không đồng ý đổi-trả hàng';
                    }

                    tbody.append(`
                        <tr>
                            <th scope="row">${invoiceStatus[1]}</th>
                            <td>${statusBill}</td>
                            <td>${invoiceStatus[3]}</td>
                            <td>
                                <a href="/staff/bill/bill-status-index/${invoiceStatus[0]}" class="btn btn-outline-primary">Xem</a>
                            </td>
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

function maxPageInvoiceStatusByStaffIdStaff() {
    $.ajax({
        type: "GET",
        url:"/bill-api/get-max-page-invoice-status-by-staff",
        success: function (response) {
            createPagination('maxPageInvoiceStatusByStaff-manage', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function filterInvoiceStatusByStaffByIdStaff() {
    $.ajax({
        type: "POST",
        url:"/bill-api/filter-invoice-status-by-staff",
        contentType: 'application/json',
        data: JSON.stringify({
            startDate: $('#startDateCashierInventoryByIdBill').val(),
            endDate: $('#endDateCashierInventoryByIdBill').val(),
            startTime: $('#startTimeCashierInventoryByIdBill').val(),
            endTime: $('#endTimeCashierInventoryByIdBill').val()
        }),
        success: function (response) {
            invoiceStatusByStaffByIdStaff(1);
            maxPageInvoiceStatusByStaffIdStaff();
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

$(document).ready(function () {
    resetFilterCashierInventoryByIdStaff()
});