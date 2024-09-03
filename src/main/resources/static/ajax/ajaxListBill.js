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
                        '<a class="nav-link text-dark" href="'+'/bill/bill-detail/'+url.id+'"'+' onclick="setActive(this)">'+url.codeBill+'</a>'
                        +'</li>'
                    );
                });
            },
            error: function (xhr) {
                    console.error("loi hien thi bill: " + xhr.responseText)
            }
        })

   }
    loadBillNew();
});