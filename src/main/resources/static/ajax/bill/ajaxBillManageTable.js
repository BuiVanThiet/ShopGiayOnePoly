
function getAllBilByStatus(value) {
    $.ajax({
        type: "GET",
        url: "/bill-api/manage-bill/"+value,
        success: function (response) {
            var tableBillManage = $('#billManage');
            var nodataBillManage = $('#nodataBillManage');
            tableBillManage.empty()
            if (response.length === 0) {
                // Nếu không có dữ liệu, hiển thị ảnh
                nodataBillManage.html(`
                <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                     alt="Lỗi ảnh" style="width: auto; height: 100px;">
                     <p class="text-center">Không có hóa đơn nào!</p>
            `);
                nodataBillManage.show();
                tableBillManage.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            } else {
                nodataBillManage.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
                tableBillManage.closest('table').show();
                response.forEach(function (bill,index) {

                    const formattedDateTime = formatDateTime(bill.updateDate);
                    var btnDrop = '';
                    // Lấy ngày hiện tại
                    const currentDate = new Date();
                    // Lấy ngày tạo hóa đơn và chuyển đổi sang đối tượng Date
                    const billCreateDate = new Date(bill.updateDate);
                    // Tính toán số ngày giữa ngày hiện tại và ngày tạo hóa đơn
                    const timeDiff = Math.abs(currentDate - billCreateDate); // Hiệu số thời gian (ms)
                    const dayDiff = Math.ceil(timeDiff / (1000 * 60 * 60 * 24)); // Chuyển đổi sang số ngày

                    // Kiểm tra điều kiện status
                    if (bill.status == 5) {
                        btnDrop = `
                            <li><a href="/staff/bill/bill-status-index/${bill.id}" class="dropdown-item">Xem chi tiết</a></li>
                            <li><button class="dropdown-item" onclick="createBillPDF(${bill.id});">Xuất hóa đơn</button></li>
                        `;

                        // Nếu hóa đơn được tạo trong vòng 3 ngày, hiển thị thêm 2 nút Đổi hàng và Trả hàng
                        if (dayDiff <= 3) {
                            btnDrop += `
                            <li><a class="dropdown-item" href="/staff/return-bill/bill/${bill.id}">Đổi-Trả hàng</a></li>
                        `;
                        }
                    } else if (bill.status == 6) {
                        btnDrop = `
                            <li><a href="/staff/bill/bill-status-index/${bill.id}" class="dropdown-item">Xem chi tiết</a></li>
                            <li><a class="dropdown-item" href="#">Xuất hóa đơn</a></li>
                        `;
                    }else {
                        btnDrop = `
                            <li><a href="/staff/bill/bill-status-index/${bill.id}" class="dropdown-item">Xem chi tiết</a></li>
                            <li><button class="dropdown-item" onclick="createBillPDF(${bill.id});">Xuất hóa đơn</button></li>
                        `;
                    }
                    if(bill.status == 6) {
                        btnDrop = `
                            <li><a href="/staff/bill/bill-status-index/${bill.id}" class="dropdown-item">Xem chi tiết</a></li>
                        `;
                    }
                    var nameCustomer = '';
                    var numberPhone = '';
                    if(bill.addRess === 'Không có') {
                        nameCustomer = 'Không có thông tin!'
                        numberPhone = 'Không có thông tin!'
                    }else {
                        var partsInfo = bill.addRess.split(',');
                        var nameCustomer = partsInfo[0];  // "Bùi Thiết"
                        var numberPhone = partsInfo[1];   // "0987654321"
                    }
                    tableBillManage.append(`
                     <tr>
                            <td scope="row">${index+1}</td>
                            <td>${bill.codeBill}</td>
                            <td>${bill.customer == null && bill.addRess == 'Không có' ? 'Khách lẻ' : (bill.customer == null ? nameCustomer : bill.customer.fullName)}</td>
                            <td>${bill.customer == null && bill.addRess == 'Không có' ? 'Không có' : (bill.customer == null ? numberPhone : bill.customer.numberPhone)}</td>
                            <td>${Math.trunc(bill.finalAmount).toLocaleString('en-US') + ' VNĐ'}</td>
                            <td>${bill.billType == 1 ? 'Tại quầy' : 'Giao hàng'}</td>
                            <td>${formattedDateTime}</td>
                            <td>
                               <div class="btn-group dropstart">
                                  <button type="button" class="btn btn-outline-primary dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                        Chức năng
                                  </button>
                                  <ul class="dropdown-menu">
                                        ${btnDrop}
                                  </ul>
                                </div>
                            </td>
                        </tr>
                    `);
                })
            }
        },
        error: function (xhr) {
            console.error('Hien thi loi phan chon bill ' + xhr.responseText);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(getAllBilByStatus, 5000);  // Gửi lại sau 5 giây
        // }
    })
}

function getMaxPageBillManage() {
    $.ajax({
        type: "GET",
        url: "/bill-api/manage-bill-max-page",
        success: function (response) {
            createPagination('billManagePageMax', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi max page bill manage' + xhr.responseText)
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(getMaxPageBillManage, 5000);  // Gửi lại sau 5 giây
        // }
    })
}

function clickStatusBillManager() {
    let statusSearchVal = $('#statusSearch').val();  // Lấy giá trị từ input

    // Kiểm tra nếu giá trị không rỗng và là chuỗi, sau đó xử lý
    let statusSearch = (typeof statusSearchVal === 'string' && statusSearchVal.trim()) ?
        statusSearchVal.trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number)
        : null; // Nếu không có giá trị thì gửi mảng rỗng

    $.ajax({
        type: "POST",
        url: "/bill-api/status-bill-manage",
        contentType: 'application/json',
        data: JSON.stringify({
            statusSearch: statusSearch
        }),
        success: function (response) {
            getMaxPageBillManage();
            getAllBilByStatus(1);
        },
        error: function (xhr) {
            console.error('loi chon trang thai ' + xhr.responseText);
        }
    })
}

function searchBillManage() {
    console.log('start: ' + $('#startDate-bill-manage').val())
    console.log('end: ' + $('#endDate-bill-manage').val())
    $.ajax({
        type: "POST",
        url: "/bill-api/bill-manage-search",
        contentType: "application/json",
        data: JSON.stringify({
            keywordBill: $('#keywordBillManage').val().trim(),
            startDate: $('#startDate-bill-manage').val().trim(),
            endDate: $('#endDate-bill-manage').val().trim()
        }),  // Gửi dữ liệu dạng JSON
        success: function(response) {
            getMaxPageBillManage();
            getAllBilByStatus(1);
            // clickStatusBillManager(999);
        },
        error: function(xhr) {
            console.error('Loi tim voucher ' + xhr.responseText);
        }
    });
}
function resetFillterBillManage() {
    document.getElementById('keywordBillManage').value='';
    // Lấy ngày hiện tại
    let today = new Date();
    document.getElementById('endDate-bill-manage').value = formatDate(today);

    // Lấy ngày 7 ngày trước
    let sevenDaysAgo = new Date();
    sevenDaysAgo.setDate(today.getDate() - 7);
    document.getElementById('startDate-bill-manage').value = formatDate(sevenDaysAgo);
    searchBillManage()
}
$(document).ready(function () {
    $('#formSubmitFilterBill').submit(function (event) {
        event.preventDefault();
        searchBillManage();
    })

    resetFillterBillManage();

    clickStatusBillManager(999);
    getAllBilByStatus(1);
    getMaxPageBillManage();

});