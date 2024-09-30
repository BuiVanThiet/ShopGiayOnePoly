$(document).ready(function (){
    var idCity = $('#idCity-customer').val();
    var idDistrict = $('#idDistrict-customer').val();
    var idCommune = $('#idCommune-customer').val();

    initializeLocationDropdowns('city-customer','district-customer','ward-customer','districtSelectContainer-customer','wardSelectContainer-customer',idCity,idDistrict,idCommune)
})