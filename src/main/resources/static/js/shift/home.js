$('#methodAddAndRemoverStaffShift').html(`<button type="button" class="btn btn-outline-success" onclick="addOrUpdateShiftInStaff()" >Thêm-sửa ca làm</button>`)
function setActiveStaffShiftAndNotShift(element, value) {
    // Xóa lớp active khỏi tất cả các liên kết
    var links = document.querySelectorAll('.nav-link-custom');
    links.forEach(function(link) {
        link.classList.remove('active');
    });
    var btn = '';
    if(value == 1) {
        btn = `
             <button type="button" class="btn btn-outline-success" onclick="addOrUpdateShiftInStaff()" >Thêm-sửa ca làm</button>
        `;
    }else {
        btn = `
            <button type="button" class="btn btn-outline-danger" onclick="removeShiftStaffInStaff()">Xóa ca làm giá</button>
            <button type="button" class="btn btn-outline-success" onclick="addOrUpdateShiftInStaff()" >Thêm-sửa ca làm</button>
        `;
    }
    $('#methodAddAndRemoverStaffShift').html(btn)
    checkStaff = value;
    // filterProduct(checkProduct)
    filterListStaff(checkStaff);
    element.classList.add('active');



    /// validate add and update

}