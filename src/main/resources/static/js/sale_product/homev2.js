document.getElementById('discountType').addEventListener('change',function () {
    console.log(document.getElementById('discountType').value)
    var value = document.getElementById('discountType').value;
    if(value == '2') {
        document.getElementById('discountText').textContent = '₫';
    }else {
        document.getElementById('discountText').textContent = '%';
    }
})

function formatDateVoucher(date) {
    let d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    let year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}


// Lấy ngày hiện tại
let today = new Date();
document.getElementById('startDate').value = formatDateVoucher(today);

let nexttoday = today.setDate(today.getDate() + 1);
document.getElementById('endDate').value = formatDateVoucher(nexttoday);


function setActiveProductSaleAndNotSale(element, value) {
    // Xóa lớp active khỏi tất cả các liên kết
    var links = document.querySelectorAll('.nav-link-custom');
    links.forEach(function(link) {
        link.classList.remove('active');
    });
    checkProduct = value;
    filterProduct(checkProduct)
    element.classList.add('active');
}