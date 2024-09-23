const shipSpan = document.getElementById('shipSpan'); // Xác định thẻ div cần ẩn/hiện

shipSpan.style.display = 'none'; // Ẩn thẻ div khi checkbox được chọn
var provinceID;
var districtID;
var wardID;
document.getElementById('flexSwitchCheckDefault').addEventListener('change', function() {
    const dynamicContent = document.getElementById('dynamic-content');
    const shipSpan = document.getElementById('shipSpan'); // Xác định thẻ div cần ẩn/hiện

    if (this.checked) {
        document.getElementById('moneyTransport').value = 1000;
        formErorrCash.style.display = 'none';
        erorrCash.innerText = '';
        btnCreateBill.disabled = false;
        document.getElementById('formMoney').style.display = 'none';
        shipSpan.style.display = 'block'; // Ẩn thẻ div khi checkbox được chọn

        // Gọi AJAX để lấy dữ liệu khách hàng
        $.ajax({
            url: '/bill-api/client-bill-information', // URL của endpoint
            method: 'GET',
            success: function(client) {

                provinceID  = parseInt(client.city);
                districtID = parseInt(client.district);
                wardID = parseInt(client.commune);

                console.log(provinceID + '-' + districtID + '-' + wardID)

                dynamicContent.innerHTML = `
            <div class="">
                <div class="row">
                    <div class="col-12">
                        <label class="form-label">Tên khách hàng</label>
                        <input type="text" class="form-control" value="${client.name}">
                    </div>
                    <div class="col-12">
                        <label class="form-label">Số điện thoại</label>
                        <input type="text" class="form-control" value="${client.numberPhone}">
                    </div>
                    <!-- Các phần khác của form -->
                    <div class="col-4">
                        <label class="form-label">Tỉnh/Thành phố</label>
                        <select class="form-select" id="provinceSelect-transport">
                            <option selected>Chọn tỉnh/thành phố</option>
                        </select>
                    </div>
                    <div class="col-4" id="districtSelectContainer-transport" style="display: none;">
                        <label class="form-label">Quận/Huyện</label>
                        <select class="form-select" id="districtSelect-transport">
                            <option selected>Chọn quận/huyện</option>
                        </select>
                    </div>
                    <div class="col-4" id="wardSelectContainer-transport" style="display: none;">
                        <label class="form-label">Xã/Phường/Thị Trấn</label>
                        <select class="form-select" id="wardSelect-transport">
                            <option selected>Chọn xã/phường/thị trấn</option>
                        </select>
                    </div>
                    <div class="mb-12">
                        <p>Địa chỉ cụ thể: </p>
                        <textarea class="form-control">${client.addressDetail}</textarea>
                    </div>
                </div>
            </div>
        `;
                initializeLocationDropdowns('provinceSelect-transport','districtSelect-transport','wardSelect-transport','districtSelectContainer-transport','wardSelectContainer-transport',provinceID,districtID,wardID)

                console.log('Thong tin sau khi chon api ' + provinceTransport + '-' + districtTransport + '-' + wardTransport)

            },
            error: function() {
                alert('Lỗi khi lấy thông tin khách hàng.');
            }
        });
    } else {
        formErorrCash.style.display = 'block';
        erorrCash.innerText = 'Mời nhập đủ giá!';
        if(payMethodChecked === 1 || payMethodChecked === 3){
            btnCreateBill.disabled = true;
        }
        document.getElementById('formMoney').style.display = 'block';
        document.getElementById('moneyTransport').value = 0.00;
        shipSpan.style.display = 'none'; // Hiển thị lại thẻ div khi checkbox không được chọn

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



