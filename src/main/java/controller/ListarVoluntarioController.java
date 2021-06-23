
package controller;

import com.google.gson.Gson;
import dao.VoluntarioDAO;
import exception.ValidationException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Erro;
import model.Filtro;
import model.Voluntario;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ListarVoluntarioController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Filtro filtro = validarEntrada(request);
            VoluntarioDAO voluntarioDAO = new VoluntarioDAO();
            List<Voluntario> voluntarios = voluntarioDAO.buscar(filtro);

            if (voluntarios.isEmpty()) {
                throw new ValidationException(Arrays.asList(new Erro("Não foram encontradas voluntarios com o filtro solicitado")));
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(new Gson().toJson(voluntarios));
        } catch (ValidationException ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new Gson().toJson(ex.getErros()));
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write(new Gson().toJson(Arrays.asList(new Erro("Falha ao buscar Ongs"))));
        }
    }

    private Filtro validarEntrada(HttpServletRequest request) {
        String cidade = request.getParameter("cidade");
        String animalId = request.getParameter("animais");

        Filtro filtro = new Filtro();
        filtro.setCidade(StringUtils.isEmpty(cidade) ? null : cidade);
        filtro.setAnimalId(StringUtils.isEmpty(animalId) ? null : animalId);

        return filtro;
    }
}

