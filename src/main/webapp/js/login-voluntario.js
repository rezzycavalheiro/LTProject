$(document).ready(function(){
    $("#loginVoluntario").click(function (event) {
        event.preventDefault();
    });
});

$(document).ready(function(){
    $("#loginVoluntario").click(function(){
        var cadastro = $("form").serializeArray();
        chamarServlet("LoginVoluntarioController", cadastro);
    });
});