document.addEventListener('DOMContentLoaded', function () {
    const filterButton = document.getElementById('filterButton');
    const filterSidebar = document.getElementById('filterSidebar');
    const overlay = document.getElementById('overlay');
    const closeFilter = document.getElementById('closeFilter');

    // Hiển thị sidebar
    filterButton.addEventListener('click', () => {
        filterSidebar.classList.add('active');
        overlay.classList.add('active');
    });

    // Ẩn sidebar
    closeFilter.addEventListener('click', () => {
        filterSidebar.classList.remove('active');
        overlay.classList.remove('active');
    });

    overlay.addEventListener('click', () => {
        filterSidebar.classList.remove('active');
        overlay.classList.remove('active');
    });
});

function collectFilters() {
    let categories = Array.from(document.querySelectorAll('input[name="categories"]:checked')).map(cb => parseInt(cb.value));
    let manufacturers = Array.from(document.querySelectorAll('input[name="manufacturer"]:checked')).map(cb => parseInt(cb.value));
    let materials = Array.from(document.querySelectorAll('input[name="material"]:checked')).map(cb => parseInt(cb.value));
    let origins = Array.from(document.querySelectorAll('input[name="origin"]:checked')).map(cb => parseInt(cb.value));
    let minPrice = document.getElementById('minPrice').value ? parseInt(document.getElementById('minPrice').value) : null;
    let maxPrice = document.getElementById('maxPrice').value ? parseInt(document.getElementById('maxPrice').value) : null;
    let priceSort = document.querySelector('input[name="priceSort"]:checked')?.value || null;

    let filters = {
        categories: categories.length ? categories : null,
        manufacturers: manufacturers.length ? manufacturers : null,
        materials: materials.length ? materials : null,
        origins: origins.length ? origins : null,
        minPrice: minPrice || null,
        maxPrice: maxPrice || null,
        priceSort: priceSort || null
    };

    console.log(filters)

    // Gửi dữ liệu qua AJAX
    fetch('/onepoly/filter', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(filters)
    })
        .then(response => response.json())
        .then(data => {
            updateProductList(data); // Hàm cập nhật danh sách sản phẩm
        })
        .catch(error => {
            console.error('Error fetching filtered products:', error);
        });
}

// Hàm cập nhật danh sách sản phẩm trong giao diện
function updateProductList(products) {
    const productContainer = document.querySelector('.row.justify-content-between');
    productContainer.innerHTML = ''; // Xóa danh sách cũ

    products.forEach(product => {
        const productHTML = `
            <div class="col-3">
                <div class="product-item" style="position: relative;">
                    <img id="imageProductClient"
                        src="https://res.cloudinary.com/dfy4umpja/image/upload/v1728725582/${product.images}" 
                        alt="Product Image">
                    <div class="info-product mt-2 d-flex justify-content-between">
                        <p>${product.nameProduct}</p>
                        <p class="price-min fw-bold" style="margin-left: auto; color: #2f2a2a">${product.priceProduct}</p>
                    </div>
                    <div class="product-icons">
                        <a href="#" class="cart-icon"><i class="fas fa-shopping-cart"></i></a>
                        <a class="view-icon" href="/onepoly/product-detail/${product.id}">
                            <i class="fas fa-eye"></i>
                        </a>
                    </div>
                </div>
            </div>
        `;
        productContainer.insertAdjacentHTML('beforeend', productHTML);
    });
}

async function searchProduct() {
    const keyword = document.getElementById("searchInput").value.trim();
    window.location.href = `/onepoly/search?keyword=${encodeURIComponent(keyword)}`;
}

document.querySelectorAll('.price-min').forEach(el => {
    const price = parseFloat(el.getAttribute('data-price'));
    el.textContent = Math.floor(price).toLocaleString('en-US') + ' đ';
});