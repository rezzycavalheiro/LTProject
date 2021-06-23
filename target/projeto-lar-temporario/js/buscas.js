function chamarServlet(servlet, cadastro, callback) {
    $.ajax({
        type: 'GET',
        url: servlet,
        data: cadastro,
        success: function(retorno) {
            callback(JSON.parse(retorno));
        },
        error: function(retorno) {
            var objectRetorno = JSON.parse(retorno.responseText);
            errorHandler(objectRetorno);
        }
    });
}

function errorHandler(erros) {
    limparFormularios();

    $('#error-form').removeClass("d-none");
    $('#error-form').append("<ul id='lista-erros'></ul>")
    $.each(erros, function (contador, erro) {
        $('#lista-erros').append("<li>" + erro.mensagem + "</li>")
    });

    $('#error-form')[0].scrollIntoView(true);
}

function limparFormularios() {
    if ($('#error-form').has("#lista-erros").length > 0) {
        $('#error-form').empty("#lista-erros");
        $('#error-form').addClass("d-none");
    }

    $('#resultados').empty();
}