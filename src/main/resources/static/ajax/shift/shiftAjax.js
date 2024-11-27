var idShiftUpdate = null;
var selectedStaffIds = [];
localStorage.setItem('selectedStaffs', JSON.stringify([]));
var pageCheckedFastStaff = 0;
var checkStaff = 1;

function loadShift(page) {
    $.ajax({
        type: "GET",
        url: "/shift-api/list/" + page,
        success: function (response) {
            var tbody = $('#taleShift');
            var noDataContainer = $('#noDataShiftContainer');
            tbody.empty(); // Xóa các dòng cũ
            if (response.length === 0) {
                // Nếu không có dữ liệu, hiển thị ảnh
                noDataContainer.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có ca làm nào!</p>
                    `);
                noDataContainer.show();
                tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            } else {
                noDataContainer.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
                tbody.closest('table').show(); // Hiển thị lại table nếu có dữ liệu
                response.forEach(function(shift, index) {
                    var status_shift = ''
                    var status = ''
                    var btn = `
                        <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#updateShiftModal" 
                          onclick="setIdShift(${shift[0]},'${shift[1]}','${shift[2]}',${shift[6]},${shift[3]})"
                        >Chi tiết</button>
                    `;
                    if(shift[3] == 1) {
                        status_shift = 'Chưa bắt đầu';
                    }else if (shift[3] == 2) {
                        status_shift = 'Đang bắt đầu';
                    }else if (shift[3] == 3) {
                        status_shift = 'Đã kết thúc';
                    }else {
                        status_shift = 'Không xác định được';
                    }

                    if(shift[6] == 0) {
                        status = 'Đã xóa';
                        btn = `
                        <button type="button" class="btn btn-outline-success" onclick="restoreShift(${shift[0]})">Khôi phục</button>
                        `;
                    }else if (shift[6] == 1) {
                        status = 'Hoạt động';
                    }else {
                        status = 'Ngừng hoạt động';
                    }

                    if(shift[6] == 1) {
                        btn += `
                        <button type="button" class="btn btn-outline-danger" onclick="deleteShift(${shift[0]})">Xóa</button>
                            <button type="button" class="btn btn-outline-success" data-bs-toggle="modal" data-bs-target="#listStaffModal" 
                          onclick="setIdShift(${shift[0]},'${shift[1]}','${shift[2]}',${shift[6]},${shift[3]})"
                        >Thêm nhân viên</button>
                        `;
                    }

                    tbody.append(`
                    <tr>
                        <th scope="row">${shift[0]}</th>
                        <td>${shift[1]}</td>
                        <td>${shift[2]}</td>
                        <td>${status_shift}</td>
                        <td>${status}</td>
                        <td>${btn}</td>
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

function maxPageShift() {
    $.ajax({
        type: "GET",
        url:"/shift-api/max-page",
        success: function (response) {
            createPagination('shiftPageMax-manage', response, 1); // Phân trang 1
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}

function filterShift() {
    $.ajax({
        type: 'POST',
        url: '/shift-api/filter-shift',  // Endpoint xử lý
        contentType: 'application/json',
        data: JSON.stringify({
            startTimeBegin: convertTo24HourFormat($('#startTimeBegin').val()),  // Convert to 24-hour format
            startTimeEnd: convertTo24HourFormat($('#startTimeEnd').val()),
            endTimeBegin: convertTo24HourFormat($('#endTimeBegin').val()),
            endTimeEnd: convertTo24HourFormat($('#endTimeEnd').val()),
            statusShift: $('#statusShiftFilter').val() === '' ? null : parseInt($('#statusShiftFilter').val()),
            status: $('#statusFilter').val() === '' ? null : parseInt($('#statusFilter').val())
        }),
        success: function (response) {
            // console.log(response)
            loadShift(1);
            maxPageShift();

        },
        error: function (xhr) {
            console.log('Lỗi filter: ' + xhr.responseText);
        }
    });
}

function deleteShift(id) {
    $.ajax({
        type: "GET",
        url:"/shift-api/delete-shift/"+id,
        success: function (response) {
            createToast(response.check, response.message);
            loadShift(1);
            maxPageShift();
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}
function restoreShift(id) {
    $.ajax({
        type: "GET",
        url:"/shift-api/restore-shift/"+id,
        success: function (response) {
            createToast(response.check, response.message);
            loadShift(1);
            maxPageShift();
        },
        error: function (xhr) {
            console.error('loi phan trang cho bill deatil' + xhr.responseText)
        }
    })
}
function addShift(idStart,idEnd) {
    var selectedStatus = getSelectedStatus();

    // Chuyển đổi thời gian bắt đầu và kết thúc sang định dạng 24 giờ
    var startTime = convertTo24HourFormat($('#' + idStart).val());
    var endTime = convertTo24HourFormat($('#' + idEnd).val());
    console.log('startTime ' + startTime + '-'+'endTime '+endTime)
    $.ajax({
        type: "POST",
        url: "/shift-api/add-or-update-shift",
        contentType: "application/json",
        data: JSON.stringify({
            id: idShiftUpdate,
            startTime: startTime,  // Sử dụng thời gian đã chuyển sang định dạng 24h
            endTime: endTime,  // Sử dụng thời gian đã chuyển sang định dạng 24h
            status: selectedStatus
        }),
        success: function (response) {
            idShiftUpdate = null;
            resetFormAddShift();
            createToast(response.check, response.message);
            loadShift(1);
            maxPageShift();
        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseText)
        }
    })
}

function setIdShift(id,start,end,status,statusShift) {
    console.log(start, end);
    // Hiển thị thời gian trong định dạng AM/PM vào input
    $('#startTimeUpdate').val(start);
    $('#endTimeUpdate').val(end);
    validateAddShift('#startTimeUpdate', '#endTimeUpdate', '#statusShiftUpdate1', '#statusShiftUpdate2', '#errorStartTimeUpdate', '#errorEndTimeUpdate', '#errorStatusShiftUpdate', '#buttonUpdateShift','#errorSameShiftUpdate');

    $('#errorStatusShiftUpdate').hide()
    idShiftUpdate = id;
    console.log(id, start, end, status); // Kiểm tra giá trị nhận được

    if(statusShift != 2 || status == 2) {
        checkShiftStaffWorking(idShiftUpdate)
            .then((isShiftClear) => {
                if (isShiftClear === true) {
                    $('#btnUpdateShift').html(`<button id="buttonUpdateShift" class="btn btn-outline-success" data-bs-target="#updateShiftModal2" data-bs-toggle="modal">Sửa</button>`);
                } else {
                    $('#btnUpdateShift').html(``);
                }
            })
            .catch((error) => {
                console.error("Error checking shift status: ", error);
                $('#btnUpdateShift').html(``);
            });
    }else {
        $('#btnUpdateShift').html(``)
    }


    // Xử lý trạng thái
    if (status == 1) {
        document.getElementById('statusShiftUpdate2').checked = false;
        document.getElementById('statusShiftUpdate1').checked = true;
    } else if (status == 2) {
        document.getElementById('statusShiftUpdate1').checked = false;
        document.getElementById('statusShiftUpdate2').checked = true;
    }

    resetFilterStaffShift()
}

function resetFormAddShift() {
    $('#startTimeAdd').val('00:00');
    $('#endTimeAdd').val('23:59');
}

function getSelectedStatus() {
    var selectedValue = $("input[name='statusShiftAdd']:checked").val();
    console.log(selectedValue); // In giá trị được chọn (1 hoặc 2)
    return selectedValue;
}

function resetFilter() {
    // Reset các input time về mặc định (00:00 - 23:59)
    $('#startTimeBegin').val('00:00');
    $('#startTimeEnd').val('23:59');
    $('#endTimeBegin').val('00:00');
    $('#endTimeEnd').val('23:59');

    // Reset các dropdown (nếu có) về giá trị mặc định
    $('#statusShiftFilter').val('');  // Trường hợp nếu bạn sử dụng <select>
    $('#statusFilter').val('');  // Trường hợp nếu bạn sử dụng <select>
    filterShift();
}
function convertTo24HourFormat(time) {
    // Kiểm tra nếu thời gian trống hoặc không hợp lệ
    if (!time) return null;

    let [hours, minutes] = time.split(':');
    let period = minutes.slice(-2); // AM or PM
    minutes = minutes.slice(0, 2); // Extract minute part

    hours = parseInt(hours);
    if (period === 'PM' && hours < 12) {
        hours += 12;  // Convert PM to 24-hour format
    }
    if (period === 'AM' && hours === 12) {
        hours = 0;  // Convert 12 AM to 00:xx in 24-hour format
    }

    return (hours < 10 ? '0' : '') + hours + ':' + minutes;  // Return in 24-hour format
}
///danh sach nhan vien

function listStaff(page) {
    $.ajax({
        type: "GET",
        url: "/shift-api/list-staff/"+page,
        success: function (response) {
            pageCheckedFastStaff = page;

            var tbody = $('#tableStaff');
            var noDataContainer = $('#noDataStaffShiftContainer');
            tbody.empty(); // Xóa các dòng cũ
            if (response.length === 0) {
                // Nếu không có dữ liệu, hiển thị ảnh
                noDataContainer.html(`
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1725477250/jw3etgwdqqxtkevcxisq.png"
                             alt="Lỗi ảnh" style="width: auto; height: 100px;">
                             <p class="text-center">Không có nhân viên nào!</p>
                    `);
                noDataContainer.show();
                tbody.closest('table').hide(); // Ẩn table nếu không có dữ liệu
            } else {
                noDataContainer.hide(); // Ẩn phần chứa ảnh nếu có dữ liệu
                tbody.closest('table').show(); // Hiển thị lại table nếu có dữ liệu
                response.forEach(function(shift, index) {

                    var status_shift = '';
                    var checkBox = `<input type="checkbox" value="${shift[0]}" name="selectedStaffs" id="staff_apply${shift[0]}" />`;
                    var timeShift = `${shift[5]} -> ${shift[6]}`;
                    if(shift[9] == 1) {
                        status_shift = 'Chưa bắt đầu';
                    }else if (shift[9] == 2) {
                        status_shift = 'Đang bắt đầu';

                    }else if (shift[9] == 3) {
                        status_shift = 'Đã kết thúc';
                    }else {
                        status_shift = 'Không có ca làm';
                        timeShift = 'Không có ca làm';
                    }

                    if(shift[11] === 'Đã hoàn tất') {

                    }else if (shift[11] === 'Đã điểm danh vào') {
                        checkBox = '';
                    }else if (shift[11] === 'Đang chờ') {
                        status_shift = 'Đã kết thúc';
                    }

                    tbody.append(`
                    <tr onclick="toggleCheckboxProduct(${shift[0]},${page})">
                        <th scope="row">${shift[1]}</th>
                        <td>${shift[2]}</td>
                        <td>${timeShift}</td>
                        <td>${shift[11]}</td>
                        <td>${shift[7]} -> ${shift[8]}</td>
                        <td class="text-center align-middle">
                            ${checkBox}
                        </td>
                    </tr>
                    `)
                    restoreCheckboxState(page);
                })

            }

            console.log(response)
        },
        error: function (xhr) {
            console.error('loi '+xhr.responseText)
        }
    })
}

function toggleCheckboxProduct(productId, pageNumber) {
    const checkbox = document.getElementById(`staff_apply${productId}`);
    checkbox.checked = !checkbox.checked;

    let selectedProducts = JSON.parse(localStorage.getItem('selectedStaffs')) || {};
    console.log('Before toggle:', selectedProducts);

    let productsOnCurrentPage = selectedProducts[pageNumber] || [];

    if (checkbox.checked) {
        // Nếu checkbox được chọn và sản phẩm chưa có trong danh sách
        if (!productsOnCurrentPage.includes(productId)) {
            document.getElementById('clickFastStaff').checked = true;
            productsOnCurrentPage.push(productId);
            selectedStaffIds.push(productId); // Thêm vào danh sách đã chọn
        }
    } else {
        // Nếu checkbox bị bỏ chọn và sản phẩm đã có trong danh sách
        productsOnCurrentPage = productsOnCurrentPage.filter(id => id !== productId);
        selectedStaffIds = selectedStaffIds.filter(id => id !== productId); // Cập nhật lại danh sách
    }
    selectedProducts[pageNumber] = productsOnCurrentPage;

    localStorage.setItem('selectedStaffs', JSON.stringify(selectedProducts));
    console.log('du lieu de giam ' + selectedStaffIds)
    console.log('After toggle:', selectedProducts);

    // Kiểm tra trạng thái của tất cả các checkbox trên trang
    const checkboxesOnCurrentPage = document.querySelectorAll(`#tableStaff input[type="checkbox"]`);
    const someChecked = Array.from(checkboxesOnCurrentPage).some(checkbox => checkbox.checked);

// Nếu có ít nhất một sản phẩm được chọn thì checkbox "Chọn tất cả" sẽ được checked
    if (someChecked) {
        document.getElementById('clickFastStaff').checked = true;
    } else {
        document.getElementById('clickFastStaff').checked = false;
    }
}
//ham chon tat ca
function selectAllStaffs(pageNumber) {
    const checkboxes = document.querySelectorAll(`#tableStaff input[type="checkbox"]`);
    document.getElementById('clickFastStaff').checked = true;
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = true;
        const productId = parseInt(checkbox.value);
        toggleCheckboxProduct(productId, pageNumber); // Cập nhật trạng thái vào localStorage
    });
}

// Hàm xóa nhanh (bỏ chọn tất cả sản phẩm)
function clearAllSelection(pageNumber) {
    const checkboxes = document.querySelectorAll(`#tableStaff input[type="checkbox"]`);
    document.getElementById('clickFastStaff').checked = false;
    checkboxes.forEach(function(checkbox) {
        checkbox.checked = false;
        const productId = parseInt(checkbox.value);
        toggleCheckboxProduct(productId, pageNumber); // Cập nhật trạng thái vào localStorage
    });
}

document.getElementById('clickFastStaff').addEventListener('change', function() {
    const pageNumber = pageCheckedFastStaff; // Xác định trang hiện tại (có thể thay đổi theo yêu cầu)
    console.log('hi')
    if (!this.checked) {
        selectAllStaffs(pageNumber); // Nếu checked, gọi hàm chọn tất cả
    } else {
        clearAllSelection(pageNumber); // Nếu không checked, gọi hàm xóa nhanh
    }
});

function restoreCheckboxState(pageNumber) {
    let selectedProducts = JSON.parse(localStorage.getItem('selectedStaffs')) || {};
    console.log('Restore data:', selectedProducts);

    let productsOnCurrentPage = selectedProducts[pageNumber] || [];

    console.log(productsOnCurrentPage)

    document.getElementById('clickFastStaff').checked = false;
    $('input[name="selectedStaffs"]').each(function() {
        let productId = $(this).val();
        if (productsOnCurrentPage.includes(Number(productId))) {
            document.getElementById('clickFastStaff').checked = true;
            $(this).prop('checked', true);
        } else {
            $(this).prop('checked', false);
        }
    });
}

function maxPageListStaff() {
    $.ajax({
        type: "GET",
        url: "/shift-api/max-page-list-staff",
        success: function (response) {
            console.log(response)
            createPagination('maxStaff-manageShift', response, 1); // Phân trang 1

        },
        error: function (xhr) {
            console.error('loi '+xhr.responseText)
        }
    })
}

function filterListStaff(checkShift) {
    $.ajax({
        type: "POST",
        url: "/shift-api/filter-list-staff",
        contentType: 'application/json',
        data: JSON.stringify({
            idShiftData: idShiftUpdate,
            keySearchData: $('#keySearchStaffShift').val(),
            checkShiftData: checkShift
        }),
        success: function (response) {
            listStaff(1);
            maxPageListStaff();
        },
        error: function (xhr) {
            console.error('loi '+xhr.responseText)
        }
    })
}

function resetFilterStaffShift() {
    $('#keySearchStaffShift').val('')
    filterListStaff(checkStaff);
}

//them ca lam cho nhan vien
function addOrUpdateShiftInStaff() {
    $.ajax({
        url: '/shift-api/save-or-update-shift-in-staff',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            staffIds: selectedStaffIds, // Danh sách ID sản phẩm
            idShift: idShiftUpdate
        }),
        success: function(response) {
            createToast(response.check, response.message);
            filterListStaff(2);
            localStorage.setItem('selectedStaffs', JSON.stringify([]));
            selectedStaffIds = [];
            if(response.check == '1') {
                document.getElementById('staffNotIdShift').classList.remove('active');
                document.getElementById('staffYesIdShift').classList.add('active');
            }


            var btn = `
                 <button type="button" class="btn btn-outline-danger" onclick="removeShiftStaffInStaff()">Xóa đợt ca làm</button>
                 <button type="button" class="btn btn-outline-success" onclick="addOrUpdateShiftInStaff()" >Thêm-sửa ca làm</button>
            `;
            checkStaff = 2;
            $('#methodAddAndRemoverStaffShift').html(btn)
            // resetDataSale();
        },
        error: function(xhr, status, error) {
            console.error('Lỗi:', error);
        }
    });
}

function removeShiftStaffInStaff() {
    $.ajax({
        url: '/shift-api/remove-shift-in-staff',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            staffIds: selectedStaffIds, // Danh sách ID sản phẩm
            idShift: idShiftUpdate
        }),
        success: function(response) {
            createToast(response.check, response.message);
            filterListStaff(1);
            localStorage.setItem('selectedStaffs', JSON.stringify([]));
            selectedStaffIds = [];
            if(response.check == '1') {
                document.getElementById('staffNotIdShift').classList.add('active');
                document.getElementById('staffYesIdShift').classList.remove('active');
                $('#methodAddAndRemoverStaffShift').html(`<button type="button" class="btn btn-outline-success" onclick="addOrUpdateShiftInStaff()" >Thêm-sửa ca làm</button>`)
            }
            checkStaff = 1;
            // resetDataSale();
        },
        error: function(xhr, status, error) {
            console.error('Lỗi:', error);
        }
    });
}

//check ca lam co nhan vien khong
function checkShiftStaffWorking(idShift) {
    console.log(idShift);
    return new Promise((resolve, reject) => {
        $.ajax({
            type: "GET",
            url: "/shift-api/check-shift-staff-working/" + idShift,
            success: function (response) {
                console.log('mess ben js(check shift): ' + response);
                if (response == 'Vẫn còn người chưa điểm danh ra') {
                    resolve(false); // Trả về false nếu còn người chưa điểm danh ra
                } else if (response == 'Không còn người chưa điểm danh ra') {
                    resolve(true); // Trả về true nếu không còn ai chưa điểm danh
                } else {
                    resolve(null); // Xử lý trường hợp không xác định
                }
            },
            error: function (xhr) {
                console.error(xhr.responseText);
                reject(xhr.responseText); // Trả về lỗi nếu AJAX thất bại
            }
        });
    });
}


$(document).ready(function () {
    $('#formFilterStaff').submit(function (event) {
        event.preventDefault();
        localStorage.setItem('selectedStaffs', JSON.stringify([]));
        selectedStaffIds = [];
        filterListStaff(checkStaff);
    })
    resetFilter();
});