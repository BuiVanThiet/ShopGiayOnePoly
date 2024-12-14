var totalAmount = document.getElementById('totalCash');
var cashClient = document.getElementById('cashClient');
var btnCreateBill = document.getElementById('createBill');
var erorrCash = document.getElementById('erorrCash');
var formErorrCash = document.getElementById('formErorrCash');
var payMethod = document.getElementById('payMethod');
var payMethodChecked ;
if(payMethod) {
    payMethodChecked = parseInt(payMethod.value);
}
var formPayMethod = document.getElementById('formPay');

var surplusMoney = document.getElementById('surplusMoney');
var surplusMoneySpan = document.getElementById('surplusMoneySpan');
var textSurplusMoney = document.getElementById('textSurplusMoney');
//
var cashClientText = document.getElementById('cashClientText');
var cashAccount = document.getElementById('cashAccount');


var provinceTransport;
var districtTransport;
var wardTransport;
var totalBill = 0;
var totalWeight = 0;
var shipPrice = 0;

var totalAmountBillCheck = 0;

const shipSpan = document.getElementById('shipSpan'); // Xác định thẻ div cần ẩn/hiện

if(shipSpan != null) {
    shipSpan.style.display = 'none'; // Ẩn thẻ div khi checkbox được chọn
}
var provinceID;
var districtID;
var wardID;
var nameCustomer='';
var emailCustomer = '';
var numberPhoneCustomer = '';
var addRessDetailCustomer = '';
var checkFormBill = document.getElementById('checkFormBill');
var shipMoneyBillWait = 0;
var checkUpdateCustomer = false;

var checkQuantityOrder = false;

var checkSwitch = false;

// ep kieu ngay
function formatDateTime(dateString) {
    const createDate = new Date(dateString);

    // Định dạng ngày theo "dd/MM/yyyy"
    const formattedDate = `${('0' + createDate.getDate()).slice(-2)}/${('0' + (createDate.getMonth() + 1)).slice(-2)}/${createDate.getFullYear()}`;

    // Định dạng thời gian theo "HH:mm"
    const formattedTime = `${('0' + createDate.getHours()).slice(-2)}:${('0' + createDate.getMinutes()).slice(-2)}`;

    // Kết hợp cả thời gian và ngày tháng
    return `${formattedTime} ${formattedDate}`;
}

//xuat hoa don
function createBillPDF(id) {
    if(id == null) {
        id = parseInt($('#idBillCreatePDF').val());
        console.log('id de tao oa don la ' + id)
    }
    $.ajax({
        type: "GET",
        url: "/bill-api/bill-pdf/"+id,
        xhrFields: {
            responseType: 'blob'  // Nhận PDF dưới dạng blob
        },
        success: function (response) {
            // Tạo URL cho blob PDF
            const pdfUrl = URL.createObjectURL(response);

            // Mở tab mới và hiển thị PDF
            const newTab = window.open();
            newTab.document.write(`<iframe src="${pdfUrl}" width="100%" height="100%" style="border:none;"></iframe>`);

        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseType);
        }
    })
}

function createBillPDFReturnExchange(id) {
    if(id == null) {
        id = parseInt($('#idBillCreatePDF').val());
        console.log('id de tao oa don la ' + id)
    }
    $.ajax({
        type: "GET",
        url: "/bill-api/bill-return-exchange-pdf/"+id,
        xhrFields: {
            responseType: 'blob'  // Nhận PDF dưới dạng blob
        },
        success: function (response) {
            // Tạo URL cho blob PDF
            const pdfUrl = URL.createObjectURL(response);

            // Mở tab mới và hiển thị PDF
            const newTab = window.open();
            newTab.document.write(`<iframe src="${pdfUrl}" width="100%" height="100%" style="border:none;"></iframe>`);

        },
        error: function (xhr) {
            console.error('loi ' + xhr.responseType);
        }
    })
}

function isValidString(str) {
    const regex = /^[A-Za-z0-9\s\u00C0-\u00FF\u0100-\u017F\u0180-\u024F\u1E00-\u1EFF]*$/;
    if (regex.test(str)) {
        return true;
    } else {
        return false;
    }
}

//validate nameCustomer
function validateNameCustomer(value,inputError) {
    var nameCheck = value;
    if(nameCheck === '' || nameCheck.length < 1){
        console.log('rong ne')
        inputError.style.display = 'block';
        inputError.innerText = '*Mời nhập tên khách hàng!';
        return false;
    } else if (!isValidString(nameCheck)) {
        inputError.style.display = 'block';
        inputError.innerText = '*Tên khách hàng không hợp lệ!';
        return false;
    }else if (nameCheck.length > 255) {
        inputError.style.display = 'block';
        inputError.innerText = '*Tên khách hàng không được quá 255 ký tự!';
        return false;
    } else {
        inputError.style.display = 'none';
        inputError.innerText = '';
        return true;
    }
}
//validate NumberPhone
function validateNumberPhone(value,inputError) {
    var numberPhoneCheck = value.trim();
    var phoneRegex = /^(0[3|5|7|8|9])+([0-9]{8})$/;
    if(numberPhoneCheck === '' || numberPhoneCheck.length < 1){
        console.log('rong ne')
        inputError.style.display = 'block';
        inputError.innerText = '*Mời nhập SĐT khách hàng!';
        return false;
    } else if (!phoneRegex.test(numberPhoneCheck)) {
        inputError.style.display = 'block';
        inputError.innerText = '*SĐT khách hàng không hợp lệ!';
        return false;
    } else {
        inputError.style.display = 'none';
        inputError.innerText = '';
        return true;
    }
}
//validate email
function validateEmail(value,inputError) {
    var emailCheck = value.trim();
    var emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if(emailCheck === '' || emailCheck.length < 1){
        console.log('rong ne')
        inputError.style.display = 'block';
        inputError.innerText = '*Mời nhập email khách hàng!';
        return false;
    } else if (!emailRegex.test(emailCheck)) {
        inputError.style.display = 'block';
        inputError.innerText = '*Email khách hàng không hợp lệ!';
        return false;
    }else if (emailRegex.length > 100) {
        inputError.style.display = 'block';
        inputError.innerText = '*Email khách hàng không được quá 100 ký tự!';
        return false;
    } else {
        inputError.style.display = 'none';
        inputError.innerText = '';
        return true;
    }
}

//validateAddress deatil
function validateAddRessDetail(value,inputError) {
    var addRessCheck = value.trim();
    if(addRessCheck === '' || addRessCheck.length < 1){
        console.log('rong ne')
        inputError.style.display = 'block';
        inputError.innerText = '*Mời nhập địa chỉ chi tiết khách hàng!';
        return false;
    }else if (addRessCheck.length > 260) {
        inputError.style.display = 'block';
        inputError.innerText = '*Địa chỉ chi tiết khách hàng không được quá 260 ký tự!';
        return false;
    } else {
        inputError.style.display = 'none';
        inputError.innerText = '';
        return true;
    }
}
//validate Emial

// validate select api
function validateProvince(select) {
    return select.value.trim() !== "";
}
function validateDistrict(select) {
    return select.value.trim()!== "";
}
function validateWard(select) {
    return select.value.trim() !== "";
}

//checkGia
function getPriceAfterDiscount(productDetail) {
    let priceBuy = productDetail.price;

    // Kiểm tra nếu SaleProduct không phải null
    if (productDetail.saleProduct != null) {

        // Tạo đối tượng Date cho ngày bắt đầu, kết thúc và ngày hiện tại
        const startDate = new Date(productDetail.saleProduct.startDate);
        const endDate = new Date(productDetail.saleProduct.endDate);
        const today = new Date();

        // // Đảm bảo các đối tượng Date không chứa giờ, phút, giây
        // startDate.setHours(0, 0, 0, 0);
        // endDate.setHours(0, 0, 0, 0);
        // today.setHours(0, 0, 0, 0);

        console.log('Ngày bắt đầu:', formatDateCompare(startDate));
        console.log('Ngày kết thúc:', formatDateCompare(endDate));
        console.log('Ngày hiện tại:', formatDateCompare(endDate));

        if (productDetail.saleProduct.status === 1) {
            console.log('dot giam gia dang con on');
            // Kiểm tra nếu ngày hiện tại nằm trong khoảng startDate và endDate
            if (formatDateCompare(today) >= formatDateCompare(startDate) && formatDateCompare(today) <= formatDateCompare(endDate)) {
                console.log('dot giam gia dang con thoi gian');

                const discountValue = productDetail.saleProduct.discountValue;

                if (productDetail.saleProduct.discountType === 1) {
                    // Tính phần trăm giảm (discountValue là %)
                    const percent = discountValue / 100;
                    const pricePercent = productDetail.price * percent;
                    priceBuy = productDetail.price - pricePercent;
                } else {
                    // Giảm trực tiếp theo giá trị cụ thể
                    priceBuy = productDetail.price - discountValue;
                }
                // Đảm bảo giá sau khi giảm không âm
                priceBuy = Math.max(priceBuy, 0);

                console.log('Giá sau khi giảm: ', priceBuy);
            } else {
                console.log('Đợt giảm giá không còn thời gian hiệu lực');
            }
        }
    }

    // Trả về giá sau khi giảm hoặc giá gốc nếu không có khuyến mãi
    return priceBuy;
}

function formatDateCompareYYYYMMDD(date) {
    return date.toISOString().split('T')[0]; // Định dạng thành YYYY-MM-DD
}

function formatDateCompare(date) {
    const day = ("0" + date.getDate()).slice(-2); // Lấy ngày (2 chữ số)
    const month = ("0" + (date.getMonth() + 1)).slice(-2); // Lấy tháng (2 chữ số)
    const year = date.getFullYear(); // Lấy năm

    return `${day}-${month}-${year}`;
}


