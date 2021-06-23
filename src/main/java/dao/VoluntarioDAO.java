
package dao;

import model.Animal;
import model.Filtro;
import model.Voluntario;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class VoluntarioDAO {
    private Conexao con;

    private final String INSERT_VOLUNTARIO = "INSERT INTO voluntario (nome, telefone, endereco, cidade, casaTelada, email, senha) VALUES (?,?,?,?,?,?,?)";
    private final String INSERT_VOLUNTARIOS_ANIMAIS = "INSERT INTO voluntarios_animais (voluntario_id, animais_id) VALUES (?,?)";
    private final String SELECT_VOLUNTARIOS = "SELECT voluntario.id, voluntario.nome, voluntario.telefone, voluntario.endereco, voluntario.cidade, voluntario.email, voluntario.casaTelada FROM voluntario INNER JOIN voluntarios_animais oa on voluntario.id = oa.voluntario_id INNER JOIN animal a on oa.animais_id = a.id";
    private final String LOGIN = "SELECT nome, telefone, endereco, cidade, casaTelada, email FROM voluntario WHERE email = ? AND senha = ?";

    public VoluntarioDAO(){
        con = Conexao.getInstance();
    }
    
    public void insere(Voluntario voluntario){
        try{
            con.getConexao().setAutoCommit(false);
            PreparedStatement insertVoluntario = con.getConexao().prepareStatement(INSERT_VOLUNTARIO, Statement.RETURN_GENERATED_KEYS);

            insertVoluntario.setString(1, voluntario.getNome());
            insertVoluntario.setString(2, voluntario.getTelefone());
            insertVoluntario.setString(3, voluntario.getEndereco());
            insertVoluntario.setString(4, voluntario.getCidade());
            insertVoluntario.setBoolean(5, voluntario.getCasaTelada());
            insertVoluntario.setString(6, voluntario.getEmail());
            insertVoluntario.setString(7, voluntario.getSenha());
            insertVoluntario.execute();

            ResultSet generatedKeys = insertVoluntario.getGeneratedKeys();
            generatedKeys.next();
            for (Animal animal : voluntario.getAnimais()) {
                PreparedStatement insertVoluntariosAnimais = con.getConexao().prepareStatement(INSERT_VOLUNTARIOS_ANIMAIS);
                insertVoluntariosAnimais.setInt(1, generatedKeys.getInt(1));
                insertVoluntariosAnimais.setInt(2, animal.getId());
                insertVoluntariosAnimais.execute();
            }

            con.getConexao().commit();
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Voluntario> buscar(Filtro filtro){
        try {
            String query = montarQuery(filtro);
            PreparedStatement selectVoluntarios = con.getConexao().prepareStatement(query);

            int contador = 1;
            if (filtro.getCidade() != null) {
                selectVoluntarios.setString(contador++, filtro.getCidade());
            }

            if (filtro.getAnimalId() != null) {
                selectVoluntarios.setInt(contador,
                        Integer.parseInt(filtro.getAnimalId()));
            }

            ResultSet resultSet = selectVoluntarios.executeQuery();

            List<Voluntario> voluntarios = new ArrayList<>();

            while (resultSet.next()) {
                Voluntario voluntario = new Voluntario();
                voluntario.setId(resultSet.getInt("id"));
                voluntario.setNome(resultSet.getString("nome"));
                voluntario.setTelefone(resultSet.getString("telefone"));
                voluntario.setEndereco(resultSet.getString("endereco"));
                voluntario.setCidade(resultSet.getString("cidade"));
                voluntario.setEmail(resultSet.getString("email"));
                voluntario.setCasaTelada(resultSet.getBoolean("casaTelada"));

                voluntarios.add(voluntario);
            }

            return voluntarios;
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public Voluntario login(Voluntario voluntario) {
        try {
            PreparedStatement selectVoluntarios = con.getConexao().prepareStatement(LOGIN);
            selectVoluntarios.setString(1, voluntario.getEmail());
            selectVoluntarios.setString(2, voluntario.getSenha());

            ResultSet resultSet = selectVoluntarios.executeQuery();

            Voluntario voluntarioLogado = null;

            if (resultSet.next()) {
                voluntarioLogado = new Voluntario();
                voluntarioLogado.setNome(resultSet.getString("nome"));
                voluntarioLogado.setTelefone(resultSet.getString("telefone"));
                voluntarioLogado.setEndereco(resultSet.getString("endereco"));
                voluntarioLogado.setCidade(resultSet.getString("cidade"));
                voluntarioLogado.setEmail(resultSet.getString("email"));
                voluntarioLogado.setCasaTelada(resultSet.getBoolean("casaTelada"));
            }

            return voluntarioLogado;
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String montarQuery(Filtro filtro) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder(SELECT_VOLUNTARIOS);
        if (filtro.getCidade() != null || filtro.getAnimalId() != null) {
            stringBuilder.append(" WHERE ");

            if (filtro.getCidade() != null) {
                stringBuilder.append(" voluntario.cidade = ? ");
            }

            if (filtro.getCidade() != null && filtro.getAnimalId() != null) {
                stringBuilder.append(" AND ");
            }

            if (filtro.getAnimalId() != null) {
                stringBuilder.append(" oa.animais_id = ? ");
            }
        }

        stringBuilder.append(" GROUP BY 1, 2, 3, 4, 5, 6");

        return stringBuilder.toString();
    }


}
