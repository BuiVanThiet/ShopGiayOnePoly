$(document).ready(function () {
   function loadBillNew() {
        $.ajax({
            type: "GET",
            url:"/bill-api/all-new",
            success:function (response) {
                var ul =$('#billBody');
                ul.empty();
                response.forEach(function (url) {
                    ul.append(
                        '<li class="nav-item">'+
                        '<a class="nav-link text-dark" href="'+'/bill/bill-detail/'+url.id+'"'+'>'+url.codeBill+'</a>'
                        +'</li>'
                    );
                });
            },
            error: function (xhr) {
                    console.error("loi hien thi bill: " + xhr.responseText)
            }
        })

   }
    function loadBillDetail() {
        $.ajax({
            type: "GET",
            url:"/bill-api/bill-detail-by-id-bill",
            success:function (response) {
                var ul =$('#tableBillDetail');
                ul.empty();
                response.forEach(function (url) {
                    ul.append(
                        '<tr>'+
                            '<th scope="row">1</th>'+
                            '<td>'+
                                '<img src="https://res.cloudinary.com/dfy4umpja/image/upload/v1724526670/Screenshot%202024-07-24%20160719.png"  alt="Lỗi ảnh" style="width: auto; height: 100px;">'+
                            '</td>'+
                            '<td>'+
                                url.productDetail.id+
                            '</td>'+
                            '<td>'+
                                url.quantity+
                            '</td>'+
                        '<td>'+
                                url.price+
                            '</td>'+

                            '<td>'+
                                '<a href="" class="btn btn-danger">Xóa bỏ</a>'+
                            '</td>'+
                        '</tr>'
                    );
                });
            },
            error: function (xhr) {
                console.error("loi hien thi bill: " + xhr.responseText)
            }
        })

    }
    loadBillNew();
    loadBillDetail();
});