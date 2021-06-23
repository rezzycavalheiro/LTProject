
package controller;

import com.google.gson.Gson;
import dao.VoluntarioDAO;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Animal;
import model.Erro;
import model.ONG;
import model.Voluntario;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CadastroVoluntarioController extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        try {
            Voluntario v = validarEntrada(request);

            VoluntarioDAO vdao = new VoluntarioDAO();
            vdao.insere(v);

            response.setStatus(HttpServletResponse.SC_OK);
        } catch(ValidationException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new Gson().toJson(ex.getErros()));
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new Gson().toJson(Arrays.asList(new Erro("Falha ao salvar voluntário."))));
        }
    }

    private Voluntario validarEntrada(HttpServletRequest request) {
        List<Erro> erros = new ArrayList<>();

        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        String endereco = request.getParameter("endereco");
        String cidade = request.getParameter("cidade");
        String casaTelada = request.getParameter("casaTelada");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String[] animais = request.getParameterValues("animais");

        if (StringUtils.isEmpty(nome)) {
            erros.add(new Erro("Nome é obrigatório."));
        }

        if (StringUtils.isEmpty(telefone)) {
            erros.add(new Erro("Telefone é obrigatório."));
        }

        if (StringUtils.isEmpty(endereco)) {
            erros.add(new Erro("Endereço é obrigatório."));
        }

        if (StringUtils.isEmpty(cidade)) {
            erros.add(new Erro("Cidade é obrigatório."));
        }

        if (StringUtils.isEmpty(email)) {
            erros.add(new Erro("E-mail é obrigatório."));
        }

        if (StringUtils.isEmpty(senha)) {
            erros.add(new Erro("Senha é obrigatório."));
        }

        if (StringUtils.isEmpty(casaTelada)) {
            erros.add(new Erro("Informar se a casa é telada ou não."));
        }

        if (animais == null) {
            erros.add(new Erro("Selecione pelo menos um tipo de animal."));
        }

        if (!erros.isEmpty()) {
            throw new ValidationException(erros);
        }

        Voluntario voluntario = new Voluntario();
        voluntario.setNome(nome);
        voluntario.setTelefone(telefone);
        voluntario.setEndereco(endereco);
        voluntario.setCidade(cidade);
        voluntario.setCasaTelada(Boolean.valueOf(casaTelada));
        voluntario.setEmail(email);
        voluntario.setSenha(senha);

        List<Animal> animals = new ArrayList<>();
        for (String animalId : animais) {
            animals.add(new Animal(Integer.parseInt(animalId)));
        }
        voluntario.setAnimais(animals);
        return voluntario;
    }
}

