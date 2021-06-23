$(document).ready(function(){
    $("#loginONG").click(function (event) {
        event.preventDefault();
    });
});

$(document).ready(function(){
    $("#loginONG").click(function(){
        var cadastro = $("form").serializeArray();
        chamarServlet("LoginONGController", cadastro);
    });
});