document.getElementById('flexSwitchCheckDefault').addEventListener('change', function() {
    const dynamicContent = document.getElementById('dynamic-content');

    if (this.checked) {
        // Nếu switch đang bật (on)
        dynamicContent.innerHTML = `
                <div class="">
                    <div class="row">
                        <div class="col-12">
                            <label class="form-label">Tên khách hàng</label>
                            <input type="text" class="form-control">
                        </div>
                        <div class="col-12">
                            <label class="form-label">Số điện thoại</label>
                            <input type="text" class="form-control">
                        </div>
                        <div class="col-4">
                            <label class="form-label">Tỉnh/Thành phố</label>
                            <select class="form-select">
                                <option selected>Open this select menu</option>
                                <option value="1">One</option>
                                <option value="2">Two</option>
                                <option value="3">Three</option>
                            </select>
                        </div>
                        <div class="col-4">
                            <label class="form-label">Quận/Huyện</label>
                            <select class="form-select">
                                <option selected>Open this select menu</option>
                                <option value="1">One</option>
                                <option value="2">Two</option>
                                <option value="3">Three</option>
                            </select>
                        </div>
                        <div class="col-4">
                            <label class="form-label">Xã/Phường/Thị Trấn</label>
                            <select class="form-select">
                                <option selected>Open this select menu</option>
                                <option value="1">One</option>
                                <option value="2">Two</option>
                                <option value="3">Three</option>
                            </select>
                        </div>
                        <div class="mb-12">
                            <p>Địa chỉ cụ thể: </p>
                            <textarea class="form-control"></textarea>
                        </div>
                    </div>
                </div>
            `;
    } else {
        // Nếu switch đang tắt (off)
        dynamicContent.innerHTML = `
                <div class="d-flex justify-content-center align-items-center position-relative">
                    <div class="d-flex position-relative" style="width: 90%; padding-top: 90%;">
                        <img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1724526670/Screenshot%202024-07-24%20160719.png"
                            alt="Lỗi ảnh" class="img-fluid position-absolute top-0 start-0 w-100 h-100" style="object-fit: cover;">
                    </div>
                </div>
            `;
    }
});