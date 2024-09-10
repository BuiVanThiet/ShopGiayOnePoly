const thumbnails = document.querySelectorAll('.img-thumbnail');

document.querySelector('#carouselExample').addEventListener('slid.bs.carousel', function (event) {
    // Xóa border xanh của tất cả các thumbnail
    thumbnails.forEach(function (thumbnail) {
        thumbnail.classList.remove('thumbnail-active');
    });

    thumbnails[event.to].classList.add('thumbnail-active');
});

thumbnails[0].classList.add('thumbnail-active');