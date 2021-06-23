function errorHandler(idForm, erros) {
    if ($('#error-form').has("#lista-erros").length > 0) {
        $('#error-form').empty("#lista-erros");
    }

    $('#error-form').removeClass("d-none");
    $('#error-form').append("<ul id='lista-erros'></ul>")
    $.each(erros, function (contador, erro) {
        $('#lista-erros').append("<li>" + erro.mensagem + "</li>")
    });

    $('#error-form')[0].scrollIntoView(true);
}

function chamarServlet(servlet, cadastro, paginaSucesso) {
    $.ajax({
        type: 'POST',
        url: servlet,
        data: cadastro,
        success: function() {
            window.location.href=paginaSucesso;
        },
        error: function(retorno) {
            var objectRetorno = JSON.parse(retorno.responseText);
            errorHandler('', objectRetorno);
        }
    });
}