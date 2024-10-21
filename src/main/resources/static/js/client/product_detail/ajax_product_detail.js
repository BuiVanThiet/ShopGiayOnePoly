function fectchProductDetails(productID){
    $.ajax({
        url:'/one-poly/product-detail/'+productID,
        type:'GET',
        dataType:'json',
        success: function(response) {
            // Xử lý dữ liệu trả về từ server (danh sách sản phẩm)
            if (response && response.length > 0) {
                $('#productDetailsContainer').empty();  // Xóa dữ liệu cũ nếu có

                // Lặp qua danh sách sản phẩm và hiển thị từng sản phẩm
                response.forEach(function(productDetail) {
                    var productHtml = `
                        <div class="product-item">
                            <h2>${productDetail.productName}</h2>
                            <p>Code: ${productDetail.productCode}</p>
                            <p>Price: ${productDetail.price}</p>
                            <p>Description: ${productDetail.productDescription}</p>
                        </div>
                    `;
                    $('#productDetailsContainer').append(productHtml);
                });
            } else {
                // Nếu không có sản phẩm nào, hiển thị thông báo
                $('#productDetailsContainer').html('<p>No products found</p>');
            }
        },
        error: function(xhr, status, error) {
            // Xử lý lỗi nếu có
            console.error('Error:', error);
            alert('Failed to fetch product details');
        }
    });
}