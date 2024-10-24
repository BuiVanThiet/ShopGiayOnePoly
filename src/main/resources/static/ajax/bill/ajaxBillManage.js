function loadBillStatusByBillId() {
    $.ajax({
        type: "GET",
        url: "/bill-api/show-status-bill",
        success: function (response) {
            var formStatus = $('#formStatusBill');
            formStatus.empty();  // Xóa sạch nội dung cũ
            response.forEach(function (invoiceBill, index) {
                // Tùy thuộc vào trạng thái, ta sẽ thêm icon và nội dung tương ứng
                var iconClass = '';
                var stepTitle = '';
                switch (invoiceBill.status) {
                    case 0:
                        iconClass = 'bi-cart-plus';
                        stepTitle = 'Tạo hóa đơn';
                        $('#btnDeleteBill').show();
                        $('#btnConfirmBill').show();
                        break;
                    case 1:
                        iconClass = 'bi-receipt';
                        stepTitle = 'Chờ Xác Nhận';
                        $('#btnDeleteBill').show();
                        $('#btnConfirmBill').show();
                        break;
                    case 2:
                        iconClass = 'bi-cart-check';
                        stepTitle = 'Đã xác nhận';
                        $('#btnDeleteBill').show();
                        $('#btnConfirmBill').show();
                        break;
                    case 3:
                        iconClass = 'bi-truck';
                        stepTitle = 'Giao Hàng';
                        $('#btnDeleteBill').show();
                        $('#btnConfirmBill').show();
                        break;
                    case 4:
                        iconClass = 'bi-box-seam';
                        stepTitle = 'Khách Đã Nhận Được Hàng';
                        $('#btnDeleteBill').hide();
                        $('#btnConfirmBill').show();
                        break;
                    case 5:
                        iconClass = 'bi-star';
                        stepTitle = 'Đơn Hàng Đã Hoàn Thành';
                        $('#btnDeleteBill').hide();
                        $('#btnConfirmBill').hide();
                        break;
                    case 6:
                        iconClass = 'bi-arrow-counterclockwise';
                        stepTitle = 'Đã Hủy';
                        $('#btnDeleteBill').hide();
                        $('#btnConfirmBill').hide();
                        break;
                    case 101:
                        iconClass = 'bi-cash';
                        stepTitle = 'Đã Thanh Toán';
                        $('#btnDeleteBill').hide();
                        $('#btnConfirmBill').hide();

                        break;
                    case 201:
                        iconClass = 'bi-arrow-counterclockwise';
                        stepTitle = 'Chờ xác nhận đổi-trả hàng';
                        $('#btnDeleteBill').hide();
                        $('#btnConfirmBill').hide();
                        break;
                    case 202:
                        iconClass = 'bi-file-earmark-check';
                        stepTitle = 'Đồng ý đổi-trả hàng';
                        $('#btnDeleteBill').hide();
                        $('#btnConfirmBill').hide();
                        break;
                    case 203:
                        iconClass = 'bi-file-earmark-excel';
                        stepTitle = 'Không đồng ý đổi-trả hàng';
                        $('#btnDeleteBill').hide();
                        $('#btnConfirmBill').hide();
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
            });
        },
        error: function (xhr) {
            console.error('Lỗi hiển thị trạng thái: ' + xhr.responseText);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadBillStatusByBillId, 5000);  // Gửi lại sau 5 giây
        // }
    });
}

function loadInformationBillByIdBill() {
    $.ajax({
        type: "GET",
        url: "/bill-api/show-invoice-status-bill",
        success: function (response) {
            var statusBill = '';
            var typeBill ='';
            var voucher = '';

            if(response.billMethod === 1) {
                typeBill = 'Tại quầy'
            }else {
                typeBill = 'Giao hàng'
            }

            if(response.voucher == null) {
                $('#btnRemoveVoucher').hide();
                voucher = 'Không có';
            }else {
                $('#btnRemoveVoucher').show();
                voucher = response.voucher.codeVoucher+'-'+response.voucher.nameVoucher;
            }
            if(response.status == 1) {
                statusBill = 'Chờ xác nhận';
                $('#cancel-button').show();
                $('#confirm-button').show();
                $('#form-action-voucher').show();
                $('#form-final-voucher').hide();
                $('#btn-modal-customer').show();
                $('#btn-buy-product').show();
                $('#startCamera').show();
                if(checkQuantityOrder == false) {
                    $('#confirm-button').hide();
                }else {
                    $('#confirm-button').show() ;
                }
            }else if (response.status == 2) {
                statusBill = 'Đã xác nhận';
                $('#cancel-button').show();
                $('#confirm-button').show();
                $('#form-action-voucher').hide();
                $('#form-final-voucher').show();
                $('#title-voucher').text(voucher);
                $('#btn-modal-customer').hide();
                $('#btn-buy-product').hide();
                $('#startCamera').hide();
                $('#table-product-buy th:last-child').hide();
            }else if (response.status == 3) {
                statusBill = 'Giao hàng';
                $('#cancel-button').show();
                $('#confirm-button').show();
                $('#form-action-voucher').hide();
                $('#form-final-voucher').show();
                $('#title-voucher').text(voucher);
                $('#btn-modal-customer').hide();
                $('#btn-buy-product').hide();
                $('#startCamera').hide();
                $('#table-product-buy th:last-child').hide();
            }else if (response.status == 4) {
                statusBill = 'Khách đã nhận được hàng';
                $('#cancel-button').show();
                $('#confirm-button').show();
                $('#form-action-voucher').hide();
                $('#form-final-voucher').show();
                $('#title-voucher').text(voucher);
                $('#btn-modal-customer').hide();
                $('#btn-buy-product').hide();
                $('#startCamera').hide();
                $('#table-product-buy th:last-child').hide();
            }else if (response.status == 5) {
                statusBill = 'Hoàn thành';
                $('#cancel-button').hide();
                $('#confirm-button').hide();
                $('#form-action-voucher').hide();
                $('#form-final-voucher').show();
                $('#title-voucher').text(voucher);
                $('#btn-modal-customer').hide();
                $('#btn-buy-product').hide();
                $('#startCamera').hide();
                $('#table-product-buy th:last-child').hide();
            }else if (response.status == 6){
                statusBill = 'Đã hủy';
                $('#cancel-button').hide();
                $('#confirm-button').hide();
                $('#form-action-voucher').hide();
                $('#form-final-voucher').show();
                $('#title-voucher').text(voucher);
                $('#btn-modal-customer').hide();
                $('#btn-buy-product').hide();
                $('#startCamera').hide();
                $('#table-product-buy th:last-child').hide();
            }else if (response.status == 7){
                statusBill = 'Chờ xác nhận đổi-trả hàng';
                $('#cancel-button').hide();
                $('#confirm-button').hide();
                $('#form-action-voucher').hide();
                $('#form-final-voucher').show();
                $('#title-voucher').text(voucher);
                $('#btn-modal-customer').hide();
                $('#btn-buy-product').hide();
                $('#startCamera').hide();
                $('#table-product-buy th:last-child').hide();

                loadInfomationReturnBillFromBillManage();
                var buttons = '';
                buttons = `
                        <button class="btn btn-outline-danger me-2"
                            type="button" id="cancel-button-return-bill"
                            data-action="cancel-return-bill"
                            data-bs-target="#infoBill"
                            data-bs-toggle="modal">
                        <i class="bi bi-bag-x"></i> Hủy đơn
                        </button>
                        <button class="btn btn-outline-success"
                                type="button" id="confirm-button-return-bill"
                                data-action="confirm-return-bill"
                                data-bs-target="#infoBill"
                                data-bs-toggle="modal">
                            <i class="bi bi-bag-check"></i> Xác nhận
                        </button>`;
                // Đẩy nút vào trong div có id là "block-confirm"
                $('#block-confirm').html(buttons);
                actionModal();
            }else if (response.status == 8){
                statusBill = 'Đồng ý đổi-trả hàng';
                $('#cancel-button').hide();
                $('#confirm-button').hide();
                $('#form-action-voucher').hide();
                $('#form-final-voucher').show();
                $('#title-voucher').text(voucher);
                $('#btn-modal-customer').hide();
                $('#btn-buy-product').hide();
                $('#startCamera').hide();
                $('#table-product-buy th:last-child').hide();

                loadInfomationReturnBillFromBillManage();
                var buttons = '';
                buttons = `
                        `;
                // Đẩy nút vào trong div có id là "block-confirm"
                $('#block-confirm').html(buttons);
                actionModal();
            }else if (response.status == 9){
                statusBill = 'Không đồng ý đổi-trả hàng';
                $('#cancel-button').hide();
                $('#confirm-button').hide();
                $('#form-action-voucher').hide();
                $('#form-final-voucher').show();
                $('#title-voucher').text(voucher);
                $('#btn-modal-customer').hide();
                $('#btn-buy-product').hide();
                $('#startCamera').hide();
                $('#table-product-buy th:last-child').hide();

                loadInfomationReturnBillFromBillManage();
                var buttons = '';
                buttons = `
                        `;
                // Đẩy nút vào trong div có id là "block-confirm"
                $('#block-confirm').html(buttons);
                actionModal();
            }
            // if(response.status == 1) {
            //     $('#form-action-voucher').show();
            //     $('#form-voucher-final').hide();
            //     $('#infoVoucher').hide();
            // }else if (response.status == 2) {
            //     $('#form-action-voucher').hide();
            //     $('#form-voucher-final').show();
            //     $('#infoVoucher').show();
            //     $('#infoVoucher').val(voucher);
            // }else if (response.status == 3) {
            //     $('#form-action-voucher').hide();
            // }else if (response.status == 4) {
            //     $('#form-action-voucher').hide();
            // }else if (response.status == 5) {
            //     $('#form-action-voucher').hide();
            // }else if (response.status == 6) {
            //     $('#form-action-voucher').hide();
            // }

            if(response.paymentStatus == 0) {
                if(response.status = 6) {
                    $('#btn-payment-confirm').hide();
                }
                $('#btn-payment-confirm').show();
            }else {
                $('#btn-payment-confirm').hide();
            }

            console.log(checkQuantityOrder)

            $('#informationStatusBill').text(statusBill)
            $('#informationCodeBill').text(response.codeBill);
            $('#informationTypeBill').text(typeBill)
            $('#informationShipPriceBill').text(response.shipPrice.toLocaleString('en-US') + ' VNĐ')
            $('#informationPriceProductBill').text(response.totalPriceProduct.toLocaleString('en-US') + ' VNĐ')
            $('#informationPriceAllBill').text((response.totalPriceProduct+response.shipPrice-response.maximumReduction).toLocaleString('en-US') + ' VNĐ')
            $('#nameVoucherApply').val(voucher);
            $('#voucher-chosen').text(voucher)
            $('#informationPriceReduction').text(response.maximumReduction.toLocaleString('en-US') + ' VNĐ');
            $('#noteByBill').text(response.note);
            totalBill = response.totalPriceProduct-response.maximumReduction;

            //phan nay de truyen du lieu con validate

            $('#cashBillPay').val((response.totalPriceProduct+response.shipPrice-response.maximumReduction))

        },
        error: function (xhr) {
            console.error('Loi ne' + xhr.responseText);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadInformationBillByIdBill, 5000);  // Gửi lại sau 5 giây
        // }
    })
}

function loadCustomerShipInBill() {
    $.ajax({
        type: "GET",
        url: "/bill-api/show-customer-in-bill-ship",
        success: function (response) {
            var provinceSelectTransportShip = document.getElementById('provinceSelect-transport-Ship');
            // var districtSelectTransportShip = document.getElementById('districtSelect-transport-Ship');
            // var wardSelectTransportShip = document.getElementById('wardSelect-transport-Ship');
            if(!response || Object.keys(response).length === 0) {
                $.ajax({
                    type: "GET",
                    url: "/bill-api/show-customer-in-bill-not-ship",
                    success: function (response) {
                        if(!response || Object.keys(response).length === 0) {
                            console.log('Khong co khach hang thi vao day')
                            $('#customerNotSystem').show();
                            $('#customerSystem').hide();
                        }else {
                            console.log('co thi vao day')
                            $('#customerNotSystem').hide();
                            $('#customerSystem').show();
                            $('#nameCustomerNotModal').text(response.fullName);
                            $('#numberPhoneNotModal').text(response.numberPhone);
                            $('#addResDetailNotModal').text(response.addRessDetail);
                            $('#emailNotModal').text(response.email);
                        }
                    },
                    error: function (xhr) {
                        console.error('loi' + xhr.responseText);
                        $('#customerNotSystem').show();
                        $('#customerSystem').hide();
                    }
                })
                // console.log('Khong co khach hang thi vao day')
                // $('#customerNotSystem').show();
                // $('#customerSystem').remove();
            }else {
                $('#customerNotSystem').hide();
                $('#customerSystem').show();
                $('#nameCustomerShip').val(response.name);
                $('#phoneCustomerShip').val(response.numberPhone);
                $('#addResDetailCustomerShip').val(response.addressDetail);
                $('#nameCustomerNotModal').text(response.name);
                $('#numberPhoneNotModal').text(response.numberPhone);
                $('#emailNotModal').text(response.email);
                $('#addResDetailNotModal').text(response.addressDetail);
                initializeLocationDropdowns('provinceSelect-transport-Ship','districtSelect-transport-Ship','wardSelect-transport-Ship','districtSelectContainer-transport-Ship','wardSelectContainer-transport-Ship',parseInt(response.city),parseInt(response.district),parseInt(response.commune));
            }
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadCustomerShipInBill, 5000);  // Gửi lại sau 5 giây
        // }
    })
}

function getBillStatus(response) {
    var statusBill = '';

    switch (response) {
        case 0:
            statusBill = 'Tạo đơn hàng';
            break;
        case 1:
            statusBill = 'Chờ xác nhận';
            break;
        case 2:
            statusBill = 'Đã xác nhận';
            break;
        case 3:
            statusBill = 'Giao hàng';
            break;
        case 4:
            statusBill = 'Khách đã nhận được hàng';
            break;
        case 5:
            statusBill = 'Hoàn thành';
            break;
        case 6:
            statusBill = 'Đã hủy';
            break;
        case 101:
            statusBill = 'Đã thanh toán';
            break;
        case 201:
            statusBill = 'Chờ xác nhận đổi-trả hàng';
            break;
        case 202:
            statusBill = 'Đồng ý đổi-trả hàng';
            break;
        case 203:
            statusBill = 'Không đồng ý đổi-trả hàng';
            break;
        default:
            statusBill = 'Không xác định';
            break;
    }
    return statusBill;
}


function updateAddressShip() {
    $.ajax({
        type: "POST",
        url: "/bill-api/update-customer-ship",
        contentType: 'application/json',
        data: JSON.stringify({
            name: $('#nameCustomerShip').val().trim(),
            numberPhone: $('#phoneCustomerShip').val().trim() || null, // Sử dụng || null để xử lý trường hợp không có giá trị
            city: parseInt($('#provinceSelect-transport-Ship').val().trim()) || null,
            district: parseInt($('#districtSelect-transport-Ship').val().trim()) || null,
            commune: parseInt($('#wardSelect-transport-Ship').val().trim()) || null,
            addressDetail: $('#addResDetailCustomerShip').val().trim() || null
        }),
        success: function (response) {
            checkUpdateCustomer = true;
            loadInformationBillByIdBill();
            showToast(response.message,response.check);
        },
        error: function (xhr) {
            console.error('loi' + xhr.responseText);
        }

    })
}

function updateMoneyShipWait(moneyShipWait) {
    console.log('data cua cap nhat gia ship ' + moneyShipWait)
    $.ajax({
        type: "GET",
        url: "/bill-api/update-ship-money",
        data: {
            money: moneyShipWait
        },
        success: function (response) {
            console.log('data cua cap nhat gia ship ' + response.data)
            loadInformationBillByIdBill();
            loadBillStatusByBillId();
            // loadCustomerShipInBill();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText);
        }
    })
}

function confirmBill(content) {
    $.ajax({
        type: "GET",
        url: "/bill-api/confirm-bill/"+content,
        success: function (response) {
            showToast(response.message,response.check);
            loadInformationBillByIdBill();
            loadInfomationHistoryByBillId();
            loadBillDetail(1);
            loadBillStatusByBillId();
            // loadCustomerShipInBill();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText);
        }
    })
}

function actionModal() {
    var modalTitle = document.getElementById('modal-title');
    var modalBody = document.getElementById('modal-body');
    var confirmButton = document.getElementById('confirm-action-button');

    document.querySelectorAll('[data-bs-toggle="modal"]').forEach(function (button) {
        button.addEventListener('click', function () {
            var action = button.getAttribute('data-action');

            if (action === 'cancel') {
                modalTitle.textContent = 'Xác nhận hủy đơn';
                modalBody.textContent = 'Bạn có chắc muốn hủy đơn hàng này không?';
                confirmButton.setAttribute('onclick', "confirmBill('cancel')");
            } else if (action === 'confirm') {
                modalTitle.textContent = 'Xác nhận đơn hàng';
                modalBody.textContent = 'Bạn có chắc muốn xác nhận đơn hàng này không?';
                confirmButton.setAttribute('onclick', "confirmBill('agree')");
            }else if (action === 'confirm-return-bill') {
                modalTitle.textContent = 'Xác nhận đổi-trả hàng';
                modalBody.textContent = 'Bạn muốn xác nhận đơn đổi-trả hàng này sao?';
                confirmButton.setAttribute('onclick', "confirmBill('agreeReturnBill')");
            }else if (action === 'cancel-return-bill') {
                modalTitle.textContent = 'Xác nhận đổi-trả hàng';
                modalBody.textContent = 'Bạn hủy đơn đổi-trả hàng này sao?';
                confirmButton.setAttribute('onclick', "confirmBill('cancelReturnBill')");
            }
        });
    });
}

document.addEventListener('DOMContentLoaded', function () {
    actionModal();
});

function paymentBill() {
    var cashPay = $('#cashPay').val();
    var cashAcountPay = $('#cashAcountPay').val();
    var cashBillPay = $('#cashBillPay').val();
    var notePay = $('#notePay').val();
    var payStatus = $('#payStatus').val();
    var surplusMoneyPay = $('#surplusMoneyPay').val();
    var payMethod = $('#payMethod').val();

    $.ajax({
        type: "POST",
        url: "/bill-api/payment-for-ship",
        contentType: 'application/json',
        data: JSON.stringify({
            cashPay: cashPay,
            cashAcountPay: cashAcountPay,
            cashBillPay: cashBillPay,
            notePay: notePay,
            payStatus: payStatus,
            surplusMoneyPay: surplusMoneyPay,
            payMethod: payMethod
        }),
        success: function(response) {
            if (response.vnpayUrl) {
                window.location.href = response.vnpayUrl;
            } else {
                loadInformationBillByIdBill();
                loadCustomerShipInBill();
                setUpPayment();
                $('#payMethod').val('1');
                loadInfomationPaymentByBillId();
                loadInfomationHistoryByBillId();
                loadBillStatusByBillId();
                $('#cancel-button').remove();
                showToast(response.message,response.check);
            }
        },
        error: function(error) {
            // Xử lý lỗi
            console.log(error);
        }
    })
}

function loadInfomationPaymentByBillId() {
    $.ajax({
        type: "GET",
        url: "/bill-api/infomation-payment-bill",
        success: function (response) {
            var tableInfomationPayment = $('#tableInfomationPayment');
            var notPaymentBill = $('#notPaymentBill');
            tableInfomationPayment.empty(); // Xóa các dòng cũ
            if(response && response.length > 0) {
                notPaymentBill.hide();
                tableInfomationPayment.closest('table').show();
                response.forEach(function (item,index) {
                    const formattedDateTime = formatDateTime(item[1]);
                    tableInfomationPayment.append(`
                    <tr>
                        <th scope="row">${index+1}</th>
                        <td>${item[2].toLocaleString('en-US')} VNĐ</td>
                        <td>${formattedDateTime}</td>
                        <td>${item[3]}</td>
                        <td>${item[4]}</td>
                    </tr>
                    `);
                })
            }else {
                notPaymentBill.html(`
                  <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                     alt="Lỗi ảnh" style="width: auto; height: 100px;">
                     <p class="text-center">Không có giao dịch nào!</p>
            `);
                notPaymentBill.show();
                tableInfomationPayment.closest('table').hide();
            }
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadInfomationPaymentByBillId, 5000);  // Gửi lại sau 5 giây
        // }
    })
}

function loadInfomationHistoryByBillId() {
    $.ajax({
        type: "GET",
        url: "/bill-api/infomation-history-bill",
        success: function (response) {
            var tableHistoryBill = $('#tableHistoryBill');
            var notHistoryBill = $('#notHistoryBill');
            tableHistoryBill.empty(); // Xóa các dòng cũ
            if(response && response.length > 0) {
                notHistoryBill.hide();
                tableHistoryBill.closest('table').show();
                response.forEach(function (item,index) {
                    const formattedDateTime = formatDateTime(item[1]);
                    var status = getBillStatus(item[0])
                    tableHistoryBill.append(`
                    <tr>
                        <th scope="row">${index+1}</th>
                        <td>${status}</td>
                        <td>${formattedDateTime}</td>
                        <td>${item[2]}</td>
                        <td>${item[3]}</td>
                    </tr>
                    `);
                })
            }else {
                notHistoryBill.html(`
                  <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                     alt="Lỗi ảnh" style="width: auto; height: 100px;">
                     <p class="text-center">Không có giao dịch nào!</p>
            `);
                notHistoryBill.show();
                tableHistoryBill.closest('table').hide();
            }
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText);
        },
        // complete: function() {
        //     // Sau khi hoàn thành, lại tiếp tục gửi yêu cầu để giữ kết nối liên tục
        //     setTimeout(loadInfomationPaymentByBillId, 5000);  // Gửi lại sau 5 giây
        // }
    })
}

//hien thong tin return bill
function loadInfomationReturnBillFromBillManage() {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/infomation-return-bill-from-bill-manage",
        success: function (response) {
            $('#code-bill').text(response.codeBill)
            $('#customer-buy-product').text(response.nameCustomer)
            $('#discount-voucher').text(response.discount.toLocaleString('en-US') + ' VNĐ')
            $('#divide-equally-product').text(response.discountRatioPercentage+ ' %')
            $('#total-return').text((response.totalReturn-response.exchangeAndReturnFee+response.discountedAmount).toLocaleString('en-US') + ' VNĐ')
            $('#node-return').val(response.noteReturn);
            $('#node-return').attr('disabled', true);
            $('#total-exchange').text(response.totalExchange.toLocaleString('en-US') + ' VNĐ')

            var totalReturnCustomer = 0;
            if(((response.totalReturn-response.exchangeAndReturnFee+response.discountedAmount)-response.totalExchange) <= 0) {
                totalReturnCustomer = 0;
            }else {
                totalReturnCustomer = (response.totalReturn-response.exchangeAndReturnFee+response.discountedAmount) -response.totalExchange;
            }
            $('#total-return-customer').text(totalReturnCustomer.toLocaleString('en-US') + ' VNĐ')
            var totalExchangeCustomer = 0;
            if((response.totalExchange-(response.totalReturn-response.exchangeAndReturnFee+response.discountedAmount)) <= 0) {
                totalExchangeCustomer = 0;
            }else {
                totalExchangeCustomer = response.totalExchange-(response.totalReturn-response.exchangeAndReturnFee+response.discountedAmount);
            }
            $('#total-exchange-customer').text(totalExchangeCustomer.toLocaleString('en-US') + ' VNĐ')
            loadReturnBillFromBillManage(1);
            maxPageReturnBillFromBillManage();
            loadExchangeBillFromBillManage(1);
            maxPageExchangeBillFromBillManage();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText);
        }
    })
}
function loadReturnBillFromBillManage(page) {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/infomation-return-bill-detail-from-bill-manage/"+page,
        success: function (response) {
            var tbody = $('#tableReturnBill');
            var noDataContainer = $('#noDataReturnBill');
            tbody.empty();
            $('#btnCreateReturnBill').remove();
            $('#createReturnBillModal').remove();
            $('#table-returnBill th:last-child, #table-returnBill td:last-child').hide();

            if(response.length === 0) {
                console.log('khong co san pham tra')
                noDataContainer.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có sản phẩm nào!</p>
                    `);

                noDataContainer.show();
                tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            }else {
                document.getElementById('errorReturn').style.display = 'none';
                console.log('co san pham tra')
                noDataContainer.hide();
                tbody.closest('table').show(); // Ẩn table nếu không có dữ liệu
                response.forEach(function(billReturn, index) {
                    var imagesHtml = '';

                    billReturn.productDetail.product.images.forEach(function(image, imgIndex) {
                        imagesHtml += `
                            <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" data-bs-interval="30000">
                                <img style="height: auto; width: 100px;" src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh">
                            </div>`;
                    });
                    tbody.append(`
                        <tr>
                            <th scope="row" class="text-center align-middle">${index + 1}</th>
                            <td class="text-center align-middle">
                                <div class="carousel slide" data-bs-ride="carousel">
                                    <div class="carousel-inner carousel-inner-bill-custom">
                                        ${imagesHtml}
                                    </div>
                                </div>
                            </td>
                            <td class="" style="max-height: 100px; overflow-y: auto; width: 150px;">
                                <div class="fs-4">
                                    ${billReturn.productDetail.product.nameProduct}
                                </div>
                                <div class="fs-6">
                                    Tên màu: ${billReturn.productDetail.color.nameColor}
                                    <br>
                                    Tên size: ${billReturn.productDetail.size.nameSize}
                                </div>
                            </td>

                            <td class="text-center align-middle">
                                ${billReturn.priceBuy.toLocaleString('en-US') + ' VNĐ'}
                            </td>
                            <td class="text-center align-middle">
                                 ${billReturn.quantityReturn}
                            </td>
                            <td class="text-center align-middle">
                                ${billReturn.totalReturn.toLocaleString('en-US') + ' VNĐ'}
                            </td>
                        </tr>`);
                });
                // Khởi tạo lại tất cả các carousel sau khi cập nhật DOM
                $('.carousel').each(function() {
                    $(this).carousel(); // Khởi tạo carousel cho từng phần tử
                });
            }
        },
        error: function (xhr) {
            console.error('loi' + xhr.responseText);
        }
    })
}

function maxPageReturnBillFromBillManage() {
    $.ajax({
        type: "GET",
        url:"/return-exchange-bill-api/max-page-return-bill-detail-from-bill-manage",
        success: function (response) {
            createPagination('billReturnPageMax-returnBill', response, 1);
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }

    })
}

function loadExchangeBillFromBillManage(page) {
    $.ajax({
        type: "GET",
        url: "/return-exchange-bill-api/infomation-exchange-bill-detail-from-bill-manage/"+page,
        success: function (response) {
            var tbody = $('#tableExchangeBill');
            var noDataContainer = $('#noDataExchangeBill');
            tbody.empty(); // Xóa các dòng cũ
            $('#table-exchangeBill th:last-child, #table-exchangeBill td:last-child').hide();
            $('#btn-group-exchange-product').remove();
            if(response.length == 0) {
                // Nếu không có dữ liệu, hiển thị ảnh
                noDataContainer.html(`
                <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                     alt="Lỗi ảnh" style="width: auto; height: 100px;">
                     <p class="text-center">Không có sản phẩm nào!</p>
                `);
                noDataContainer.show();
                tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            }else {
                noDataContainer.hide();
                tbody.closest('table').show();
                response.forEach(function (exchange,index) {
                    var imagesHtml = '';
                    exchange.productDetail.product.images.forEach(function(image, imgIndex) {
                        imagesHtml += `
                            <div class="carousel-item ${imgIndex === 0 ? 'active' : ''}" data-bs-interval="30000">
                                <img style="height: auto; width: 100px;" src="https://res.cloudinary.com/dfy4umpja/image/upload/f_auto,q_auto/${image.nameImage}" class="d-block w-100" alt="Lỗi ảnh">
                            </div>`;
                    });
                    var price = '';
                    if(exchange.priceAtTheTimeOfExchange == exchange.priceRootAtTime) {
                        price = `<div>
                                    <span>${exchange.priceAtTheTimeOfExchange.toLocaleString('en-US')} VNĐ</span>
                                </div>`;
                    }else {
                        price = ` <div>
                                    <span class="text-decoration-line-through">${ exchange.priceRootAtTime.toLocaleString('en-US')} VNĐ</span>
                                    <br>
                                    <span class="text-danger fs-5">${exchange.priceAtTheTimeOfExchange.toLocaleString('en-US')} VNĐ</span>
                                </div>`;
                    }
                    tbody.append(`
                        <tr>
                            <th scope="row" class="text-center align-middle">${index + 1}</th>
                            <td class="text-center align-middle">
                                <div class="carousel slide" data-bs-ride="carousel">
                                    <div class="carousel-inner carousel-inner-bill-custom">
                                        ${imagesHtml}
                                    </div>
                                </div>
                            </td>
                            <td class="" style="max-height: 100px; overflow-y: auto; width: 150px;">
                                <div class="fs-4">
                                    ${exchange.productDetail.product.nameProduct}
                                </div>
                                <div class="fs-6">
                                    Tên màu: ${exchange.productDetail.color.nameColor}
                                    <br>
                                    Tên size: ${exchange.productDetail.size.nameSize}
                                </div>
                            </td>

                            <td class="text-center align-middle">
                                ${price}
                            </td>
                            <td class="text-center align-middle">
                                 <div class="pagination mb-3 custom-number-input" style="width: 130px;" data-id="${exchange.productDetail.id}">
                                    ${exchange.quantityExchange}
                                 </div>
                            </td>
                            <td class="text-center align-middle">
                                ${exchange.totalExchange.toLocaleString('en-US') + ' VNĐ'}
                            </td>
                           
                        </tr>`);
                })
                // Khởi tạo lại tất cả các carousel sau khi cập nhật DOM
                $('.carousel').each(function() {
                    $(this).carousel(); // Khởi tạo carousel cho từng phần tử
                });
            }
        },
        error: function (xhr) {
            console.error('Loi ' + xhr.responseText)
        }
    })
}

function maxPageExchangeBillFromBillManage() {
    $.ajax({
        type: "GET",
        url:"/return-exchange-bill-api/max-page-exchange-bill-detail-from-bill-manage",
        success: function (response) {
            console.log('so trang tra ' + response)
            createPagination('billExchangePageMax-exchangeBill', response, 1);
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }

    })
}

$(document).ready(function () {
    checkUpdateCustomer = true;
    $('#updateAddressShip').submit(function (event){
        event.preventDefault();
        checkUpdateCustomer = true;
        updateAddressShip();
    })
    $('#payMoney').submit(function (event){
        event.preventDefault();
        paymentBill();
    })
    loadInformationBillByIdBill();
    loadCustomerShipInBill();
    loadInfomationPaymentByBillId();
    loadInfomationHistoryByBillId();
    loadBillStatusByBillId();
});
