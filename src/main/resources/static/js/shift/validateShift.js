$('#startTimeAdd, #endTimeAdd, #statusShiftAdd1, #statusShiftAdd2').on('change', function () {
    validateAddShift('#startTimeAdd', '#endTimeAdd', '#statusShiftAdd1', '#statusShiftAdd2', '#errorStartTimeAdd', '#errorEndTimeAdd', '#errorStatusShiftAdd', '#buttonAddShift');
});

$('#startTimeUpdate, #endTimeUpdate, #statusShiftUpdate1, #statusShiftUpdate2').on('change', function () {
    validateAddShift('#startTimeUpdate', '#endTimeUpdate', '#statusShiftUpdate1', '#statusShiftUpdate2', '#errorStartTimeUpdate', '#errorEndTimeUpdate', '#errorStatusShiftUpdate', '#buttonUpdateShift');
});


function validateAddShift(startTimeId, endTimeId, statusId1, statusId2, errorStartId, errorEndId, errorStatusId, buttonId) {
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

    // Kiểm tra nếu tất cả các trường hợp hợp lệ, enable hoặc disable button
    if (isValidStartTime && isValidEndTime && isValidStatus && isValidTime) {
        $(buttonId).prop('disabled', false);
    } else {
        $(buttonId).prop('disabled', true);
    }
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