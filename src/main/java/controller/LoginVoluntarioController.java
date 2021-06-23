
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
import model.Filtro;
import model.Voluntario;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginVoluntarioController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            Voluntario voluntario = validarEntrada(request);
            VoluntarioDAO voluntarioDAO = new VoluntarioDAO();
            Voluntario voluntarioLogado = voluntarioDAO.login(voluntario);

            if (voluntarioLogado == null) {
                throw new ValidationException(Arrays.asList(new Erro("E-mail ou senha incorretos")));
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(new Gson().toJson(voluntarioLogado));
        } catch (ValidationException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new Gson().toJson(ex.getErros()));
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new Gson().toJson(Arrays.asList(new Erro("Falha ao buscar Ongs"))));
        }
    }

    private Voluntario validarEntrada(HttpServletRequest request) {
        List<Erro> erros = new ArrayList<>();

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        if (StringUtils.isEmpty(email)) {
            erros.add(new Erro("E-mail é obrigatório"));
        }

        if (StringUtils.isEmpty(senha)) {
            erros.add(new Erro("Senha é obrigatório"));
        }

        if (!erros.isEmpty()) {
            throw new ValidationException(erros);
        }

        Voluntario voluntario = new Voluntario();
        voluntario.setEmail(email);
        voluntario.setSenha(senha);

        return voluntario;
    }
}

