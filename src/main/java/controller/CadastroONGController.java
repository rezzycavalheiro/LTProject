
package controller;

import com.google.gson.Gson;
import dao.ONGDAO;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Animal;
import model.Erro;
import model.ONG;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CadastroONGController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ONG ong = validarEntrada(request);
            ONGDAO ongdao = new ONGDAO();
            ongdao.insere(ong);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch(ValidationException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new Gson().toJson(ex.getErros()));
        } catch (Exception ex) {
            Erro erro = new Erro("Falha ao salvar Ong no sistema");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new Gson().toJson(Arrays.asList(erro)));
        }
    }

    private ONG validarEntrada(HttpServletRequest request) {
        List<Erro> erros = new ArrayList<>();

        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        String[] animais = request.getParameterValues("animais");
        String endereco = request.getParameter("endereco");
        String cidade = request.getParameter("cidade");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (StringUtils.isEmpty(nome)) {
            erros.add(new Erro("Nome é obrigatório"));
        }

        if (StringUtils.isEmpty(telefone)) {
            erros.add(new Erro("Telefone é obrigatório"));
        }

        if (StringUtils.isEmpty(endereco)) {
            erros.add(new Erro("Endereço é obrigatório"));
        }

        if (StringUtils.isEmpty(cidade)) {
            erros.add(new Erro("Cidade é obrigatório"));
        }

        if (StringUtils.isEmpty(email)) {
            erros.add(new Erro("E-mail é obrigatório"));
        }

        if (StringUtils.isEmpty(senha)) {
            erros.add(new Erro("Senha é obrigatório"));
        }

        if (animais == null) {
            erros.add(new Erro("Selecione pelo menos um tipo de animal"));
        }

        if (!erros.isEmpty()) {
            throw new ValidationException(erros);
        }

        ONG ong = new ONG();
        ong.setNome(nome);
        ong.setTelefone(telefone);
        ong.setEndereco(endereco);
        ong.setCidade(cidade);
        ong.setEmail(email);
        ong.setSenha(senha);
        List<Animal> animals = new ArrayList<>();
        for (String animalId : animais) {
            animals.add(new Animal(Integer.parseInt(animalId)));
        }
        ong.setAnimais(animals);

        return ong;
    }

}
