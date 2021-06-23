$(document).ready(function(){
    $("#submitONG").click(function (event) {
        event.preventDefault();
    });
});

$(document).ready(function(){
    $("#submitONG").click(function(){
        var cadastro = $("form").serializeArray();
        chamarServlet("CadastroONGController", cadastro, "ongSuccess.html");
        var url = $(this).data('target');
        location.replace(url);
    });
});