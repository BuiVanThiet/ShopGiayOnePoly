$(document).ready(function (){
    var idCity = $('#idCity-staff').val();
    var idDistrict = $('#idDistrict-staff').val();
    var idCommune = $('#idCommune-staff').val();

    initializeLocationDropdowns('city-staff','district-staff','ward-staff','districtSelectContainer-staff','wardSelectContainer-staff',idCity,idDistrict,idCommune)
})