
package dao;

import model.Animal;
import model.Filtro;
import model.ONG;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ONGDAO {
    private Conexao con;

    private final String INSERT_ONG = "INSERT INTO ong (nome, telefone, endereco, cidade, email, senha) VALUES (?,?,?,?,?,?)";
    private final String INSERT_ONGS_ANIMAIS = "INSERT INTO ongs_animais (ong_id, animais_id) VALUES (?,?)";
    private final String SELECT_ONGS = "SELECT ong.id, ong.nome, ong.telefone, ong.endereco, ong.cidade, ong.email FROM ong INNER JOIN ongs_animais oa on ong.id = oa.ong_id INNER JOIN animal a on oa.animais_id = a.id";
    private final String LOGIN = "SELECT nome, telefone, endereco, cidade, email FROM ong WHERE email = ? AND senha = ?";

    public ONGDAO(){
        con = Conexao.getInstance();
    }
    
    public void insere(ONG ong){
        try {
            con.getConexao().setAutoCommit(false);
            PreparedStatement insertOng = con.getConexao().prepareStatement(INSERT_ONG, Statement.RETURN_GENERATED_KEYS);
            insertOng.setString(1, ong.getNome());
            insertOng.setString(2, ong.getTelefone());
            insertOng.setString(3, ong.getEndereco());
            insertOng.setString(4, ong.getCidade());
            insertOng.setString(5, ong.getEmail());
            insertOng.setString(6, ong.getSenha());
            insertOng.execute();

            ResultSet generatedKeys = insertOng.getGeneratedKeys();
            generatedKeys.next();
            for (Animal animal : ong.getAnimais()) {
                PreparedStatement insertOngAnimais = con.getConexao().prepareStatement(INSERT_ONGS_ANIMAIS);
                insertOngAnimais.setInt(1, generatedKeys.getInt(1));
                insertOngAnimais.setInt(2, animal.getId());
                insertOngAnimais.execute();
            }

            con.getConexao().commit();
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<ONG> buscar(Filtro filtro){
        try {
            String query = montarQuery(filtro);
            PreparedStatement selectOngs = con.getConexao().prepareStatement(query);

            int contador = 1;
            if (filtro.getCidade() != null) {
                selectOngs.setString(contador++, filtro.getCidade());
            }

            if (filtro.getAnimalId() != null) {
                selectOngs.setInt(contador,
                        Integer.parseInt(filtro.getAnimalId()));
            }

            ResultSet resultSet = selectOngs.executeQuery();

            List<ONG> ongs = new ArrayList<>();

            while (resultSet.next()) {
                ONG ong = new ONG();
                ong.setId(resultSet.getInt("id"));
                ong.setNome(resultSet.getString("nome"));
                ong.setTelefone(resultSet.getString("telefone"));
                ong.setEndereco(resultSet.getString("endereco"));
                ong.setCidade(resultSet.getString("cidade"));
                ong.setEmail(resultSet.getString("email"));

                ongs.add(ong);
            }

            return ongs;
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ONG login(ONG ong) {
        try {
            PreparedStatement selectOng = con.getConexao().prepareStatement(LOGIN);
            selectOng.setString(1, ong.getEmail());
            selectOng.setString(2, ong.getSenha());

            ResultSet resultSet = selectOng.executeQuery();

            ONG ongLogada = null;

            if (resultSet.next()) {
                ongLogada = new ONG();
                ongLogada.setNome(resultSet.getString("nome"));
                ongLogada.setTelefone(resultSet.getString("telefone"));
                ongLogada.setEndereco(resultSet.getString("endereco"));
                ongLogada.setCidade(resultSet.getString("cidade"));
                ongLogada.setEmail(resultSet.getString("email"));
            }

            return ongLogada;
        }
        catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String montarQuery(Filtro filtro) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder(SELECT_ONGS);
        if (filtro.getCidade() != null || filtro.getAnimalId() != null) {
            stringBuilder.append(" WHERE ");

            if (filtro.getCidade() != null) {
                stringBuilder.append(" ong.cidade = ? ");
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
