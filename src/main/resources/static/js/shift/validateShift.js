$('#startTimeAdd, #endTimeAdd, #statusShiftAdd1, #statusShiftAdd2').on('change', function () {
    validateAddShift('#startTimeAdd', '#endTimeAdd', '#statusShiftAdd1', '#statusShiftAdd2', '#errorStartTimeAdd', '#errorEndTimeAdd', '#errorStatusShiftAdd', '#buttonAddShift','#errorSameShiftAdd');
});

$('#startTimeUpdate, #endTimeUpdate, #statusShiftUpdate1, #statusShiftUpdate2').on('change', function () {
    validateAddShift('#startTimeUpdate', '#endTimeUpdate', '#statusShiftUpdate1', '#statusShiftUpdate2', '#errorStartTimeUpdate', '#errorEndTimeUpdate', '#errorStatusShiftUpdate', '#buttonUpdateShift','#errorSameShiftUpdate');
});


function validateAddShift(startTimeId, endTimeId, statusId1, statusId2, errorStartId, errorEndId, errorStatusId, buttonId,errorSameShift) {
    $(errorSameShift).hide()
    // Lấy giá trị của các trường input từ các ID được truyền vào
    var startTime = $(startTimeId).val();
    var endTime = $(endTimeId).val();
    var statusShiftAdd1Checked = $(statusId1).is(':checked');
    var statusShiftAdd2Checked = $(statusId2).is(':checked');

    // Validate các trường
    var isValidStartTime = validateNotEmpty(startTime, errorStartId);
    var isValidEndTime = validateNotEmpty(endTime, errorEndId);
    var isValidStatus = validateStatus(statusShiftAdd1Checked || statusShiftAdd2Checked, errorStatusId);
    var isValidTime = validateStartEndTime(startTime, endTime, errorStartId, errorEndId);
    var isValidDuration = validateShiftDuration(startTime, endTime, errorSameShift); // Kiểm tra 8 tiếng

    // Kiểm tra nếu tất cả các trường hợp hợp lệ, enable hoặc disable button
    if (isValidStartTime && isValidEndTime && isValidStatus && isValidTime && isValidDuration) {
        checkShift(startTimeId, function () {
            console.log('Shifts data: ', shiftsData);

            var isShiftExists = shiftsData.some(function (shift) {
                return shift.startTime === convertTo24HourFormat(startTime) && shift.endTime === convertTo24HourFormat(endTime);
            });

            if (isShiftExists) {
                $(errorSameShift).text("Ca làm đã tồn tại!");
                $(errorSameShift).show();
                $(buttonId).prop('disabled', true);
            } else {
                $(errorSameShift).hide();
                $(buttonId).prop('disabled', false);
            }
        });
    } else {
        $(buttonId).prop('disabled', true);
    }
}

//kiem tra 1 ngày không quá 8h
// Hàm kiểm tra thời gian không quá 8 tiếng
function validateShiftDuration(startTime, endTime, errorId) {
    // Chuyển đổi thời gian sang phút để tính toán
    var start = convertToMinutes(startTime);
    var end = convertToMinutes(endTime);

    // Kiểm tra thời gian
    if (end - start > 480) { // 480 phút = 8 tiếng
        $(errorId).text("Khoảng thời gian không được vượt quá 8 tiếng").show();
        return false;
    } else {
        $(errorId).hide();
        return true;
    }
}

// Hàm hỗ trợ chuyển đổi thời gian từ HH:mm sang phút
function convertToMinutes(time) {
    var parts = time.split(':'); // Tách giờ và phút
    return parseInt(parts[0]) * 60 + parseInt(parts[1]); // Chuyển đổi thành phút
}


// Hàm kiểm tra trường hợp không được để trống
function validateNotEmpty(value, errorId) {
    if (value === '') {
        $(errorId).show();
        return false;
    } else {
        $(errorId).hide();
        return true;
    }
}

// Hàm kiểm tra trạng thái radio
function validateStatus(isStatusSelected, errorId) {
    if (!isStatusSelected) {
        $(errorId).show();
        return false;
    } else {
        $(errorId).hide();
        return true;
    }
}

// Hàm kiểm tra giờ bắt đầu phải trước giờ kết thúc
function validateStartEndTime(startTime, endTime, errorStartId, errorEndId) {
    if (startTime >= endTime) {
        $(errorStartId).text('Giờ bắt đầu phải trước giờ kết thúc').show();
        $(errorEndId).text('Giờ kết thúc phải sau giờ bắt đầu').show();
        return false;
    } else {
        $(errorStartId).hide();
        $(errorEndId).hide();
        return true;
    }
}

//check trung
let shiftsData = [];
function checkShift(startTimeId, callback) {
    shiftsData = [];
    $.ajax({
        url: '/shift-api/check-shift-same', // API kiểm tra ca làm
        type: 'GET',
        success: function (exists) {
            shiftsData = exists;
            if (startTimeId === '#startTimeUpdate') {
                console.log('Đã vào đây vì là update');
                shiftsData = shiftsData.filter(function (shift) {
                    return shift.id !== idShiftUpdate; // Loại bỏ các mã trùng
                });
            }
            console.log('Ngoài này là add');
            console.log(shiftsData);

            // Gọi callback sau khi dữ liệu được cập nhật
            if (callback) callback();
        },
        error: function () {
            console.error("Không thể kiểm tra ca làm việc!");
        }
    });
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

    return (hours < 10 ? '0' : '') + hours + ':' + minutes + ':' + '00';  // Return in 24-hour format
}

checkShift('')