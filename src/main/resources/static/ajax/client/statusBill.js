function loadBillStatusByBillId_webClient() {
    $.ajax({
        type: "GET",
        url: "/api-client/show-status-bill",
        success: function (response) {
            var formStatus = $('#formStatusBill');
            formStatus.empty();  // Xóa sạch nội dung cũ
            let htmlContent = '';  // Biến lưu trữ nội dung HTML
            var stepWidth = 310;  // Chiều rộng của mỗi bước (bước + khoảng cách)
            var totalSteps = response.length;  // Số lượng bước

            response.forEach(function (invoiceBill, index) {
                // Tùy thuộc vào trạng thái, ta sẽ thêm icon và nội dung tương ứng
                var iconClass = '';
                var stepTitle = '';
                switch (invoiceBill.status) {
                    case 0:
                        iconClass = 'bi-cart-plus';
                        stepTitle = 'Tạo hóa đơn';
                        break;
                    case 1:
                        iconClass = 'bi-receipt';
                        stepTitle = 'Chờ Xác Nhận';
                        break;
                    case 2:
                        iconClass = 'bi-cart-check';
                        stepTitle = 'Đã xác nhận';
                        break;
                    case 3:
                        iconClass = 'bi-truck';
                        stepTitle = 'Giao Hàng';
                        break;
                    case 4:
                        iconClass = 'bi-box-seam';
                        stepTitle = 'Khách Đã Nhận Được Hàng';
                        break;
                    case 5:
                        iconClass = 'bi-star';
                        stepTitle = 'Đơn Hàng Đã Hoàn Thành';
                        break;
                    case 6:
                        iconClass = 'bi-arrow-counterclockwise';
                        stepTitle = 'Đã Hủy';
                        break;
                    case 101:
                        iconClass = 'bi-cash';
                        stepTitle = 'Đã Thanh Toán';
                        break;
                    case 102:
                        iconClass = 'bi-cash';
                        stepTitle = 'Đã Thanh Toán(phiếu đổi-trả)';
                        break;
                    case 201:
                        iconClass = 'bi-arrow-counterclockwise';
                        stepTitle = 'Chờ xác nhận đổi-trả hàng';
                        break;
                    case 202:
                        iconClass = 'bi-file-earmark-check';
                        stepTitle = 'Đồng ý đổi-trả hàng';
                        break;
                    case 203:
                        iconClass = 'bi-file-earmark-excel';
                        stepTitle = 'Không đồng ý đổi-trả hàng';
                        break;
                }
                const formattedDateTime = formatDateTime(invoiceBill.createDate);
                // Nếu đây không phải là trạng thái đầu tiên, thì thêm <div class="progress-bar-line"></div>
                if (index > 0) {
                    formStatus.append(`<div class="progress-bar-line"></div>`);
                }

                formStatus.append(`
                    <div class="step">
                        <div class="circle">
                            <i class="bi ${iconClass}"></i>
                        </div>
                        <div class="step-info">
                            <div class="step-title">${stepTitle}</div>
                            <div class="step-time">${formattedDateTime}</div>
                        </div>
                    </div>
                `);
                var newContainerWidth = totalSteps * stepWidth;
                $('.progress-container').width(newContainerWidth);  // Cập nhật chiều rộng
            });
        },
        error: function (xhr) {
            console.error('Lỗi hiển thị trạng thái: ' + xhr.responseText);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadBillStatusByBillId_webClient, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

function loadProductBuy(page) {
    $.ajax({
        type: "GET",
        url: "/api-client/show-product-buy-status-bill/"+page,
        success: function (response) {
            var tbody = $('#product-buy-client-bill-status');
            tbody.empty();
            response.forEach(function(billReturn, index) {
                var priceBuy = '';
                if(billReturn[6] === billReturn[5]) {
                    priceBuy = `
                        <span class="text-danger" style="font-size: 19px">${Math.trunc(billReturn[6]).toLocaleString('en-US')} VNĐ</span>
                    `;
                }else {
                    priceBuy = `
                        <span class="text-decoration-line-through" style="font-size: 15px;color: #626262">${Math.trunc(billReturn[5]).toLocaleString('en-US')}</span>
                        <span class="text-danger" style="font-size: 19px">${Math.trunc(billReturn[6]).toLocaleString('en-US')}</span>
                    `;
                }
                tbody.append(`
                    <div class="row">
                            <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1724526670/${billReturn[0]}" class="rounded float-start" style="height: 100px; width: auto;">
                            <div class="col-7">
                                <span>Tên sản phẩm: </span>
                                <span id="nameProductBillClient">${billReturn[1]}</span>
                                <br>
                                <span>Màu sắc: </span>
                                <span id="colorProductBillClient">${billReturn[2]}</span>
                                <br>
                                <span>Kích cỡ: </span>
                                <span id="sizeProductBillClient">${billReturn[3]}</span>
                                <br>
                                <span>Số lượng mua: </span>
                                <span id="quantityBuyProductBillClient">${billReturn[4]}</span>
                            </div>
                            <div class="col-3">
                                <div id="priceBuyProductClient">
                                    <span>Giá mua: </span>
                                    ${priceBuy}
                                </div>
                            </div>
                        </div>
                        <hr size="5px" align="center" color="#626262" />
                `);
            });
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function loadMaxPageProductBuy() {
    $.ajax({
        type: "GET",
        url: "/api-client/max-page-bill-status",
        success: function (response) {
            console.log('so trang la ' + response)
            createPagination('billClientPageMax-billStatus', response, 1);
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function loadTotalBillStatus() {
    $.ajax({
        type: "GET",
        url: "/api-client/show-total-status-bill",
        success: function (response) {
            console.log('so trang la ' + response)
            $('#totalAmountBillClient').text(Math.trunc(response[0]).toLocaleString('en-US') + ' VNĐ')
            $('#priceShipBillClient').text(Math.trunc(response[1]).toLocaleString('en-US') + ' VNĐ')
            $('#discountBillClient').text(Math.trunc(response[2]).toLocaleString('en-US') + ' VNĐ')
            $('#sumMoneyBillClient').text(Math.trunc(response[3]).toLocaleString('en-US') + ' VNĐ')
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}
function loadInformationBillStatus() {
    $.ajax({
        type: "GET",
        url: "/api-client/show-information-status-bill",
        success: function (response) {
            $('#codeBillStatusClient').text(response[0])
            $('#nameShipBillStatus').text('Họ và tên: '+response[1])
            $('#numberPhoneBillStatus').text('Số điện thoại: '+response[2])
            $('#emailBillStatus').text('Email: '+response[3])
            $('#addResShipDetailBillStatus').text('Địa chỉ chi tiết: '+response[4])
            $('#noteBillClient').text(response[5])

            console.log('so trang la ' + response)
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
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
function formSendEmailRequestBill() {
    $('#button-clone').html(``)
    var emailSend = $('#requestEmailBill').val().trim();
    console.log(emailSend)
    $.ajax({
        type: "POST",
        url: "/api-client/send-mail-request-bill",
        contentType: 'application/json',
        data: JSON.stringify({
            emailSend: emailSend
        }),
        success: function (response) {
            $('#button-clone').html(`
        <i class="bi bi-x-lg fs-5"  data-bs-dismiss="modal" aria-label="Close"></i>
`)
           $('#confirmSendEmail').text(response);
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function openBill(id) {
    $.ajax({
        type: "GET",
        url: "/api-client/bill-pdf/"+id,
        xhrFields: {
            responseType: 'blob'  // Nhận PDF dưới dạng blob
        },
        success: function (response) {
            const pdfUrl = URL.createObjectURL(response);
            const newTab = window.open();
            newTab.document.write(`<iframe src="${pdfUrl}" width="100%" height="100%" style="border:none;"></iframe>`);
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseType);
        }
    })
}

$(document).ready(function () {
    $('#button-clone').html(`
        <i class="bi bi-x-lg fs-5"  data-bs-dismiss="modal" aria-label="Close"></i>
`)
    $('#formSendEmailRequestBill').submit(function (event) {
        event.preventDefault();
        formSendEmailRequestBill();
    })
    loadBillStatusByBillId_webClient();
    loadProductBuy(1);
    loadMaxPageProductBuy()
    loadTotalBillStatus();
    loadInformationBillStatus();
});
