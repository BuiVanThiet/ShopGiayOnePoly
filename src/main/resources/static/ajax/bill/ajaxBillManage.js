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
                    case 1:
                        iconClass = 'bi-receipt';
                        stepTitle = 'Chờ Xác Nhận';
                        $('#btnDeleteBill').show();
                        $('#btnConfirmBill').show();
                        break;
                    case 2:
                        iconClass = 'bi-truck';
                        stepTitle = 'Đã xác nhận';
                        $('#btnDeleteBill').show();
                        $('#btnConfirmBill').show();
                        break;
                    case 3:
                        iconClass = 'bi-truck';
                        stepTitle = 'Chuẩn Bị Giao Hàng';
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
                }

                // Chuyển đổi chuỗi createDate thành đối tượng Date
                const createDate = new Date(invoiceBill.createDate);

                // Định dạng ngày theo "dd/MM/yyyy"
                const formattedDate = `${('0' + createDate.getDate()).slice(-2)}/${('0' + (createDate.getMonth() + 1)).slice(-2)}/${createDate.getFullYear()}`;

                // Định dạng thời gian theo "HH:mm"
                const formattedTime = `${('0' + createDate.getHours()).slice(-2)}:${('0' + createDate.getMinutes()).slice(-2)}`;

                // Kết hợp cả thời gian và ngày tháng
                const formattedDateTime = `${formattedTime} ${formattedDate}`;

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
        }
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
            if (response.status === 1) {
                statusBill = 'Chờ Xác Nhận';
            } else if (response.statuss === 2) {
                statusBill = 'Đang Giao Hàng';
            } else if (response.status === 3) {
                statusBill = 'Khách Đã Nhận Được Hàng';
            } else if (response.status === 4) {
                statusBill = 'Đơn Hàng Đã Hoàn Thành';
            } else if (response.status === 5) {
                statusBill = 'Đã Hủy';
            }

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
            $('#informationStatusBill').text(statusBill)
            $('#informationCodeBill').text(response.codeBill);
            $('#informationTypeBill').text(typeBill)
            $('#informationShipPriceBill').text(response.shipPrice.toLocaleString('en-US') + ' VNĐ')
            $('#informationPriceProductBill').text(response.totalPriceProduct.toLocaleString('en-US') + ' VNĐ')
            $('#informationPriceAllBill').text((response.totalPriceProduct+response.shipPrice-response.maximumReduction).toLocaleString('en-US') + ' VNĐ')
            $('#nameVoucherApply').val(voucher);
            $('#informationPriceReduction').text(response.maximumReduction.toLocaleString('en-US') + ' VNĐ');
            totalBill = response.totalPriceProduct-response.maximumReduction;
            },
        error: function (xhr) {
            console.error('Loi ne' + xhr.responseText);
        }
    })
}

function loadCustomerShipInBill() {
    $.ajax({
        type: "GET",
        url: "/bill-api/show-customer-in-bill",
        success: function (response) {
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

        },
        error: function (xhr) {
            console.error('loi' + xhr.responseText);
            $('#customerNotSystem').show();
            $('#customerSystem').hide();
        }
    })
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
            loadBillStatusByBillId();
            loadInformationBillByIdBill();
            loadCustomerShipInBill();
            showToast(response.message,response.check)
        },
        error: function (xhr) {
            console.error('loi' + xhr.responseText);
        }

    })
}
$(document).ready(function () {
    $('#updateAddressShip').submit(function (event){
        event.preventDefault();
        updateAddressShip();
    })
    loadBillStatusByBillId();
    loadInformationBillByIdBill();
    loadCustomerShipInBill();
});
