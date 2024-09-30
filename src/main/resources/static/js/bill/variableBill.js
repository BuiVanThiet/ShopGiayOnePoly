var totalAmount = document.getElementById('totalCash');
var cashClient = document.getElementById('cashClient');
var btnCreateBill = document.getElementById('createBill');
var erorrCash = document.getElementById('erorrCash');
var formErorrCash = document.getElementById('formErorrCash');
var payMethod = document.getElementById('payMethod');
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
var totalBill;
var shipPrice = 0;

const shipSpan = document.getElementById('shipSpan'); // Xác định thẻ div cần ẩn/hiện

if(shipSpan != null) {
    shipSpan.style.display = 'none'; // Ẩn thẻ div khi checkbox được chọn
}
var provinceID;
var districtID;
var wardID;
var nameCustomer='';
var numberPhoneCustomer = '';
var addRessDetailCustomer = '';
var checkFormBill = document.getElementById('checkFormBill');
var shipMoneyBillWait = 0;
var checkUpdateCustomer = false;

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