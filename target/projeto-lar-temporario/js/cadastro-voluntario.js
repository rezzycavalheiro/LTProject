$(document).ready(function(){
    $("#submitVoluntario").click(function (event) {
        event.preventDefault();
    });
});

$(document).ready(function(){
    $("#submitVoluntario").click(function(){
        var cadastro = $("form").serializeArray();
        chamarServlet("CadastroVoluntarioController", cadastro, "voluntarioSuccess.html");
        var url = $(this).data('target');
        location.replace(url);
    });
});