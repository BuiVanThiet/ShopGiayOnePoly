const thumbnails = document.querySelectorAll('.img-thumbnail');

document.querySelector('#carouselExample').addEventListener('slid.bs.carousel', function (event) {
    // Xóa border xanh của tất cả các thumbnail
    thumbnails.forEach(function (thumbnail) {
        thumbnail.classList.remove('thumbnail-active');
    });

    thumbnails[event.to].classList.add('thumbnail-active');
});

thumbnails[0].classList.add('thumbnail-active');

function changeColor(color){
    document.getElementById("selected-color").innerText= color;
}
function changeSize(size){
    document.getElementById("selected-size").innerText= size;
}
$("#describeProductDetailLabel").on("click",function (){
    $("#describeProductDetailFilter").toggleClass("show");
});
$("#returnPolicyLabel").on("click",function (){
    $("#returnPolicyFilter").toggleClass("show");
});