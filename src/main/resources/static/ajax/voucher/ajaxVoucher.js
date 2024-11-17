function loadVoucher(page) {
    $.ajax({
        type: "GET",
        url: "/api_voucher/list/"+page,
        success: function (response) {
            console.log(response)
            var tableVoucher = $('#tableVoucher');
            var noDataVoucherContainer = $('#noDataVoucherContainer');
            tableVoucher.empty();
            if(response.length === 0) {
                noDataVoucherContainer.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có phiếu gia giá nào!</p>
                    `);
                tableVoucher.closest('table').hide();
                noDataVoucherContainer.show()
            }else {
                tableVoucher.closest('table').show();
                noDataVoucherContainer.hide()
                response.forEach(function (voucher,index) {
                    const start = formatDate(voucher[7]);
                    const end = formatDate(voucher[8]);
                    var action = '';
                    var endDate = new Date(end);
                    var today = new Date();
                    today.setHours(0, 0, 0, 0);
                    if(voucher[10] === 1) {
                        action = `
                    <td>
                        <a href="/voucher/delete/${voucher[0]}"
                           class="btn btn-danger btn-sm"
                           onclick="return confirm('Bạn có muốn xóa phiếu giảm giá này không?')">Xóa</a>
                    </td>
                    <td>
                        <a href="/voucher/edit/${voucher[0]}"
                           class="btn btn-warning btn-sm">Sửa</a>
                    </td>
                    <td>
                        <a href="/voucher/detail/${voucher[0]}"
                           class="btn btn-primary btn-sm">Chi tiết</a>
                    </td>
                    `;
                    }else if (voucher[10] === 2 || voucher[10] === 0) {
                        action = `
                    <td>
                        <a href="/voucher/restore/${voucher[0]}"
                           class="btn btn-success btn-sm"
                           onclick="return confirm('Bạn có muốn khôi phục phiếu giảm giá này không?')">Khôi
                            phục</a>
                    </td>
                    <td>
                        <a href="/voucher/detail/${voucher[0]}"
                           class="btn btn-primary btn-sm">Chi tiết</a>
                    </td>
                    
                    `;
                    }else if (voucher[10] === 3){
                        action = `
                     <td>
                        <a href="/voucher/delete/${voucher[0]}"
                           class="btn btn-danger btn-sm"
                           onclick="return confirm('Bạn có muốn xóa phiếu giảm giá này không?')">Xóa</a>
                    </td>
                    <td>
                        <a href="/voucher/extend/${voucher[0]}"
                           class="btn btn-success btn-sm"
                           onclick="return confirm('Bạn có muốn gia hạn phiếu giảm giá này không?')">Gia
                            hạn</a>
                    </td>
                    `;
                    }
                    tableVoucher.append(`
                <tr>
                    <td>${voucher[1]}</td>
                    <td>${voucher[2]}</td>
                    <td>${voucher[4]}</td>
                    <td>${voucher[9]}</td>
                    <td>${start}</td>
                    <td>${end}</td>
                    ${action}
                </tr>
                `);
                })
            }

        },
        error: function (xhr) {

            console.error('loi ' + xhr.responseText)
        }
    })
}

function formatDate(inputDate) {
    // Chuyển đổi chuỗi thành đối tượng Date
    const date = new Date(inputDate);

    // Sử dụng Intl.DateTimeFormat để định dạng ngày theo yêu cầu
    const formattedDate = new Intl.DateTimeFormat('vi-VN', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
    }).format(date);

    // Trả về giá trị đã định dạng
    return formattedDate;
}

function maxPageVoucher() {
    $.ajax({
        type: "GET",
        url: "/api_voucher/max-page-voucher",
        success: function (response) {
            createPagination('maxPageVoucher-manageVoucher',response,1)
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function searchVoucher() {
    var discountTypeValue = $('#searchDiscountTypeInput').val().trim();
    var discountTypeCheck = discountTypeValue === '' ? null : parseInt(discountTypeValue);

    var nameCheck = $('#inputSearchVoucher').val().trim();

    var statusCheckValue = $('#searchStatusVoucher').val().trim();
    var statusCheck = statusCheckValue === '' ? null : parseInt(statusCheckValue); // Tránh NaN nếu rỗng

    $.ajax({
        type: "POST",
        url: "/api_voucher/search-voucher",
        contentType: "application/json",
        data: JSON.stringify({
            discountTypeCheck: discountTypeCheck, // Gửi giá trị đã kiểm tra
            nameCheck: nameCheck, // Gửi tên tìm kiếm
            statusCheck: statusCheck // Gửi trạng thái
        }),
        success: function (response) {
            console.log(response)
            loadVoucher(1);
            maxPageVoucher();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}
function startLoadAjaxVoucher() {
    $.ajax({
        type: "Get",
        url: "/api_voucher/reset-filter-voucher",
        success: function (response) {
            loadVoucher(1);
            maxPageVoucher();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function resetFormSearch() {
    $('#searchDiscountTypeInput').val('');
    $('#searchStatusVoucher').val('1'); // Nếu trạng thái mặc định là "Hoạt động"

    // Reset trường input search về rỗng
    $('#inputSearchVoucher').val('');
    searchVoucher();
}

function addVoucherNew() {
    var selectedStatus = getSelectedStatus();
    $.ajax({
        type: "POST",
        url: "/api_voucher/add-new-voucher",
        contentType: "application/json",
        data: JSON.stringify({
            codeVoucher: $('#codeVoucher').val().trim(),
            nameVoucher: $('#nameVoucher').val().trim(),
            discountType: parseInt($('#discountType').val().trim()),
            priceReduced: $('#value').val().trim(),
            pricesApply: $('#applyValue').val().trim(),
            pricesMax: parseInt($('#discountType').val().trim()) === 2 ? $('#value').val().trim() : $('#maxDiscount').val().trim(),
            startDate: $('#startDate').val().trim(),
            endDate: $('#endDate').val().trim(),
            describe: $('#note').val().trim(),
            quantity: $('#quantity').val().trim(),
            status: selectedStatus
        }),
        success: function (response) {
            createToast(response.check, response.message);
            console.log(response)
            loadVoucher(1);
            maxPageVoucher();
            resetFormAddVoucher();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

// Lấy giá trị của radio button đã được chọn
function getSelectedStatus() {
    var selectedValue = $("input[name='statusVoucher']:checked").val();
    console.log(selectedValue); // In giá trị được chọn (1 hoặc 2)
    return selectedValue;
}

$(document).ready(function () {
    $('#searchForm').submit(function (event) {
        event.preventDefault();
        searchVoucher()
    })
    startLoadAjaxVoucher();

});