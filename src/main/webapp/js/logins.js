function errorHandler(idForm, erros) {
    limparFormularios();

    $('#error-form').removeClass("d-none");
    $('#error-form').append("<ul id='lista-erros'></ul>")
    $.each(erros, function (contador, erro) {
        $('#lista-erros').append("<li>" + erro.mensagem + "</li>")
    });

    $('#error-form')[0].scrollIntoView(true);
}

function successHandler(usuario) {
    limparFormularios();

    $('#success-form').removeClass("d-none");
    $('#success-form').append("<ul id='sucesso'></ul>");
    $('#sucesso').append("<li id='sucesso'>Bem-vindo: " + usuario.nome + "</li>");
}

function limparFormularios() {
    if ($('#success-form').has("#sucesso").length > 0) {
        $('#success-form').empty("#sucesso");
    }

    $('#success-form').addClass("d-none");

    if ($('#error-form').has("#lista-erros").length > 0) {
        $('#error-form').empty("#lista-erros");
    }

    $('#error-form').addClass("d-none");
}

function chamarServlet(servlet, cadastro) {
    $.ajax({
        type: 'POST',
        url: servlet,
        data: cadastro,
        dataType: 'json',
        success: function(retorno) {
            console.log(retorno);
            successHandler(retorno);
        },
        error: function(retorno) {
            errorHandler('', JSON.parse(retorno.responseText));
        }
    });
}