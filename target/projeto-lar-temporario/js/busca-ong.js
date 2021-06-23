$(document).ready(function(){
    $("#searchONG").click(function (event) {
        event.preventDefault();
    });
});

$(document).ready(function(){
    $("#searchONG").click(function(){
        var busca = $("form").serializeArray();
        chamarServlet("ListarONGController", busca, successHandler);
    });
});

function successHandler(ongs) {
    limparFormularios();

    $.each(ongs, function (contador, ong) {

        $('#resultados').append("<tr id='resultado-" + contador + "'</tr>");
        $('#resultado-' + contador).append("<th id='resultado-" + contador + "'" + "scope='row'>" + ong.id + "</th>");
        $('#resultado-' + contador).append("<td>" + ong.nome + "</td>");
        $('#resultado-' + contador).append("<td>" + ong.telefone + "</td>");
        $('#resultado-' + contador).append("<td>" + ong.endereco + "</td>");
        $('#resultado-' + contador).append("<td>" + ong.cidade + "</td>");
        $('#resultado-' + contador).append("<td>" + ong.email + "</td>");
    });
}