function loadTableProductDetail(status) {
    var url = ''
    if (status == 1) {
        url = '/sale/list-un-apply'
        console.log(url)
    } else {
        url = '/sale/list-apply'
        console.log(url)
    }
    $.ajax({
        type: "GET",
        url: url,
        success: function (response) {
            var table = $('#send')
            table.empty()
            if (response.length > 0) {
            } else {
            }
            response.forEach(function (product) {
                table.append(`<tr>
                                    <td>${product.id}</td>
                                    <td>${product.product.nameProduct}</td>
                                    <td>${product.price}</td>
                                    <td>
                                        <input type="checkbox"  name="selectedProducts"/>
                                    </td>
                                    </tr>`)
            })
        },
        error: function (xhr) {
            console.log("Error: " + xhr.responseText)
        }
    })
    // }
}

$(document).ready(function () {
    loadTableProductDetail(1);
})

