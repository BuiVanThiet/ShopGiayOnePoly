
function getAllBillClientByStatus(value) {
    $.ajax({
        type: "GET",
        url: "/api-client/list-bill-client/"+value,
        success: function (response) {
            var tableBillManage = $('#billClient');
            var nodataBillManage = $('#nodataBillClient');
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
                    // Lấy ngày hiện tại
                    const currentDate = new Date();
                    // Lấy ngày tạo hóa đơn và chuyển đổi sang đối tượng Date
                    const billCreateDate = new Date(bill.updateDate);
                    // Tính toán số ngày giữa ngày hiện tại và ngày tạo hóa đơn
                    const timeDiff = Math.abs(currentDate - billCreateDate); // Hiệu số thời gian (ms)
                    const dayDiff = Math.ceil(timeDiff / (1000 * 60 * 60 * 24)); // Chuyển đổi sang số ngày

                    // Kiểm tra điều kiện status
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
                            <th scope="row">${index+1}</th>
                            <td>${bill.codeBill}</td>
                            <td>${formattedDateTime}</td>
                            <td>
                                <a href="/onepoly/status-bill/${bill.id}" class="btn">Xem </a>
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

function getMaxPageBillClient() {
    $.ajax({
        type: "GET",
        url: "/api-client/list-bill-max-page",
        success: function (response) {
            console.log(response)
            createPagination('billClientPageMax', response, 1); // Phân trang 1
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

function clickStatusBillClient() {
    let statusSearchVal = $('#statusSearch').val();  // Lấy giá trị từ input

    // Kiểm tra nếu giá trị không rỗng và là chuỗi, sau đó xử lý
    let statusSearch = (typeof statusSearchVal === 'string' && statusSearchVal.trim()) ?
        statusSearchVal.trim().replace(/\s+/g, '').split(',').filter(Boolean).map(Number)
        : null; // Nếu không có giá trị thì gửi mảng rỗng

    $.ajax({
        type: "POST",
        url: "/api-client/status-bill-client",
        contentType: 'application/json',
        data: JSON.stringify({
            statusSearch: statusSearch
        }),
        success: function (response) {
            getMaxPageBillClient();
            getAllBillClientByStatus(1);
        },
        error: function (xhr) {
            console.error('loi chon trang thai ' + xhr.responseText);
        }
    })
}

function searchBillClient() {
    console.log('start: ' + $('#startDate-bill-client').val())
    console.log('end: ' + $('#endDate-bill-client').val())
    $.ajax({
        type: "POST",
        url: "/api-client/bill-client-search",
        contentType: "application/json",
        data: JSON.stringify({
            keywordBill: $('#keywordBillClient').val().trim(),
            startDate: $('#startDate-bill-client').val().trim(),
            endDate: $('#endDate-bill-client').val().trim()
        }),  // Gửi dữ liệu dạng JSON
        success: function(response) {
            getMaxPageBillClient();
            getAllBillClientByStatus(1);
            // clickStatusBillManager(999);
        },
        error: function(xhr) {
            console.error('Loi tim voucher ' + xhr.responseText);
        }
    });
}
function resetFillterBillClient() {
    document.getElementById('keywordBillClient').value='';
    // Lấy ngày hiện tại
    let today = new Date();
    document.getElementById('endDate-bill-client').value = formatDate(today);

    // Lấy ngày 7 ngày trước
    let sevenDaysAgo = new Date();
    sevenDaysAgo.setDate(today.getDate() - 7);
    document.getElementById('startDate-bill-client').value = formatDate(sevenDaysAgo);
    searchBillClient()
}

function formatDateTime(dateString) {
    const createDate = new Date(dateString);

    // Định dạng ngày theo "dd/MM/yyyy"
    const formattedDate = `${('0' + createDate.getDate()).slice(-2)}/${('0' + (createDate.getMonth() + 1)).slice(-2)}/${createDate.getFullYear()}`;

    // Định dạng thời gian theo "HH:mm"
    const formattedTime = `${('0' + createDate.getHours()).slice(-2)}:${('0' + createDate.getMinutes()).slice(-2)}`;

    // Kết hợp cả thời gian và ngày tháng
    return `${formattedTime} ${formattedDate}`;
}

$(document).ready(function () {
    $('#formSubmitFilterBill').submit(function (event) {
        event.preventDefault();
        searchBillClient();
    })

    resetFillterBillClient();

    clickStatusBillClient(999);
    getAllBilByStatus(1);
    getMaxPageBillClient();

});