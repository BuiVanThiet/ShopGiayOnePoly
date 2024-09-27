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

shipSpan.style.display = 'none'; // Ẩn thẻ div khi checkbox được chọn
var provinceID;
var districtID;
var wardID;
var nameCustomer='';
var numberPhoneCustomer = '';
var addRessDetailCustomer = '';