window.onload = function() {
    //cai nay la cua bill
    // Lấy phần tử nút theo ID và gán sự kiện click
    document.getElementById('cash').addEventListener('click', function() {
        payMethodUpLoad = 1;
        uploadPayMethod()
    });

    document.getElementById('accountMoney').addEventListener('click', function() {
        payMethodUpLoad = 2;
        uploadPayMethod()
    });

    document.getElementById('accountMoneyAndCash').addEventListener('click', function() {
        payMethodUpLoad = 3;
        uploadPayMethod()
    });

    //cai nay la cua atribute
    showNotification();
};