var selectBankVNPay;
function loadBankVietNam() {
    $.ajax({
        type: "GET",
        url: "https://api.vietqr.io/v2/banks",
        // url: "/bill-api/filter-size",
        success: function (response) {
            const selectElement = $('#banksVNPAY');
            selectElement.empty(); // Xóa các option cũ nếu có
            // Kiểm tra xem response đã là mảng chưa, nếu chưa thì chuyển nó thành mảng
            const dataArray = response.data;
            // const dataArray = Array.isArray(response) ? response : Object.values(response);

            // Kiểm tra nếu dataArray là mảng
            if (Array.isArray(dataArray)) {
                // Duyệt qua các ngân hàng trong mảng
                dataArray.forEach(bank => {
                    const option = $('<option>', {
                        value: bank.code,  // giá trị của option
                        text: bank.name + ' - ' + bank.code  // tên hiển thị trong option
                    });
                    selectElement.append(option);  // Thêm option vào select
                });
            } else {
                console.error("Dữ liệu không phải là mảng.");
            }
            // dataArray.forEach(size => {
            //     const option = $('<option>', {
            //         value: size.id, // giá trị của option
            //         text: size.nameSize // tên hiển thị trong option
            //     });
            //     selectElement.append(option);
            // });
            // Khởi tạo MultiSelectTag sau khi thêm các option vào select
                selectBankVNPay = new MultiSelectTag('banksVNPAY', {
                    rounded: true,
                    shadow: false,
                    placeholder: 'Search',
                    tagColor: {
                        textColor: '#327b2c',
                        borderColor: '#92e681',
                        bgColor: '#eaffe6',
                    },
                    onChange: function(values) {
                        let selectedValues = values.map(item => item.value);
                        document.getElementById('banksVNPAYSearch').value = selectedValues;
                    }
                });

            // Clear all selected values (if needed)
            selectBankVNPay.clearAll();
        },
        error: function (xhr) {
            console.log('Lỗi: ' + xhr.responseText);
        }
    });
}

function loadAllTransactionVNPay(page) {
    $.ajax({
        type: "GET",
        url: "/transactionVNPay-api/list/"+page,
        // url: "/bill-api/filter-size",
        success: function (response) {
            var tableTransactionVNPay = $('#tableTransactionVNPay');
            var nodataTransactionVNPay = $('#nodataTransactionVNPay');
            var transactionVNPayPageMax = $('#transactionVNPayPageMax');
            var allTransaction = $('#allTransaction');
            // var moneyTransaction = $('#moneyTransaction');
            tableTransactionVNPay.empty();
            sumTotal()
            if(response.length === 0) {
                nodataTransactionVNPay.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có hóa đơn thanh toán nào!</p>
                    `);
                // allTransaction.text('0')
                // moneyTransaction.text('0 VNĐ')
                tableTransactionVNPay.closest('table').hide();
                nodataTransactionVNPay.show();
                transactionVNPayPageMax.hide();
            }else {
                tableTransactionVNPay.closest('table').show();
                nodataTransactionVNPay.hide();
                transactionVNPayPageMax.show();
                // allTransaction.text(response.length); // Gán số phần tử tối đa vào allTransaction
                // moneyTransaction.text('0 VNĐ')
                let totalMoney = response.reduce((sum, transaction) => sum + Math.trunc(transaction[8]), 0);
                // moneyTransaction.text(totalMoney.toLocaleString('en-US') + ' VNĐ')
                response.forEach(function (transaction,index) {
                    var dateCreate = formatDate(transaction[6]);
                    tableTransactionVNPay.append(`
                    <tr>
                        <td scope="row">${index+1}</td>
                        <td>${transaction[0]}</td>
                        <td>${transaction[1]}</td>
                        <td>${transaction[2]}</td>
                        <td>${transaction[3]}</td>
                        <td>${transaction[4]}</td>
                        <td>${transaction[5]}</td>
                        <td>${dateCreate}</td>
                        <td>
                        <a href="/staff/transactionVNPay/transaction-by-id/${transaction[7]}" class="btn btn-outline-primary">
                            Xem
                        </a>
                        </td>
                    </tr>
                `);
                })
            }
        },
        error: function (xhr) {
            console.log('Lỗi: ' + xhr.responseText);
        }
    });
}

function sumTotal() {
    $.ajax({
        type: 'GET',
        url: '/transactionVNPay-api/sum-total',  // Endpoint xử lý
        success: function (response) {
            var moneyTransaction = $('#moneyTransaction');
            moneyTransaction.text(Math.trunc(response.total).toLocaleString('en-US') + ' VNĐ');
            var allTransaction = $('#allTransaction');
            allTransaction.text(Math.trunc(response.quantity).toLocaleString('en-US'));

        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        }
    });
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

function filterTransactionVNPay() {
    $.ajax({
        type: 'POST',
        url: '/transactionVNPay-api/search-transaction',  // Endpoint xử lý
        contentType: 'application/json',
        data: JSON.stringify({
            bankCodeList: $('#banksVNPAYSearch').val().trim() === "" ? null : $('#banksVNPAYSearch').val().trim().split(','),
            codeVNPay: $('#codeVNPayFilter').val().trim(),
            starDate: $('#starDateFilter').val().trim(),
            endDate: $('#endDateFilter').val().trim(),
            numberBill: $('#numberBillFilter').val().trim(),
            transactionStatus: $('#transactionStatusFilter').val().trim(),
            notePay: $('#notePayFilter').val().trim()
        }),
        success: function (response) {
            loadAllTransactionVNPay(1);
            maxPageTransaction();
        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        }
    });
}

function resetFilterTransactionVNPay() {
    $.ajax({
        type: 'GET',
        url: '/transactionVNPay-api/reset-search-transaction',  // Endpoint xử lý
        success: function (response) {
            loadBankVietNam();
            loadAllTransactionVNPay(1);
            maxPageTransaction();
        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        }
    });
}

function exportExcelTransaction() {
    $.ajax({
        type: 'GET',
        url: '/transactionVNPay-api/excel-export-transaction',  // Endpoint xử lý
        success: function (response) {
            createToast(response.check, response.message)
        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        }
    });
}
function maxPageTransaction() {
    $.ajax({
        type: 'GET',
        url: '/transactionVNPay-api/max-page-transaction',  // Endpoint xử lý
        success: function (response) {
            createPagination('transactionVNPayPageMax', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        }
    });
}


function resetFilter() {
    document.getElementById('banksVNPAYSearch').value='';
    selectBankVNPay.clearAll();
    $('#codeVNPayFilter').val('')
    document.getElementById('endDateFilter').value = formatDate(today);
    document.getElementById('starDateFilter').value = formatDate(today);
    $('#numberBillFilter').val('')
    $('#transactionStatusFilter').prop('selectedIndex', 0);
    $('#notePayFilter').val('')

    filterTransactionVNPay();
}

function clearToToday(inputId) {
    const inputField = document.getElementById(inputId);

    // Lấy ngày hôm nay theo múi giờ Hà Nội (UTC+7)
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Đặt lại giờ thành 00:00:00

    // Lấy ngày theo định dạng yyyy-mm-dd mà không có giờ
    const todayString = today.getFullYear() + '-'
        + String(today.getMonth() + 1).padStart(2, '0') + '-'
        + String(today.getDate()).padStart(2, '0');

    // Nếu người dùng xóa ngày, đặt lại giá trị về ngày hôm nay
    if (!inputField.value) {
        inputField.value = todayString;
    }
}
function formatDate(date) {
    let d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    let year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}
// Lấy ngày hiện tại
let today = new Date();
document.getElementById('endDateFilter').value = formatDate(today);
document.getElementById('starDateFilter').value = formatDate(today);


$(document).ready(function () {
    $('#formFilterTransaction').submit(function (event) {
        event.preventDefault();
        filterTransactionVNPay();
    })
    resetFilterTransactionVNPay();
});