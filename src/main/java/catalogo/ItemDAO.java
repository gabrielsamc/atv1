package catalogo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ItemDAO {

    public static List<String> buscarVulneravel(Connection c, String termo) throws SQLException {
        String sql = "SELECT id, titulo, autor FROM itens "
                + "WHERE CONCAT(titulo, ' ', autor) LIKE '%" + termo + "%'";
        System.out.println("   SQL montado: " + sql);
        try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            return lerLinhas(rs);
        }
    }

    public static List<String> buscarSeguro(Connection c, String termo) throws SQLException {
        String sql = "SELECT id, titulo, autor FROM itens "
                + "WHERE CONCAT(titulo, ' ', autor) LIKE ?";
        System.out.println("   SQL enviado: " + sql);
        System.out.println("   Parâmetro 1: %" + termo + "%");
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return lerLinhas(rs);
            }
        }
    }

    public static void inserir(Connection c, String titulo, String autorDiretor,
                               Integer ano, String genero, String sinopse, String tipo)
            throws SQLException {
        String sql = "INSERT INTO item_midia "
                + "(titulo, autor_diretor, ano_lancamento, genero, sinopse, tipo_midia) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setString(2, autorDiretor);
            if (ano == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, ano);
            }
            ps.setString(4, genero);
            ps.setString(5, sinopse);
            ps.setString(6, tipo);
            ps.executeUpdate();
        }
    }

    public static List<String> buscarItemMidia(Connection c, String termo) throws SQLException {
        String sql = "SELECT id, titulo, autor_diretor FROM item_midia "
                + "WHERE titulo LIKE ? OR autor_diretor LIKE ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + termo + "%");
            ps.setString(2, "%" + termo + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return lerLinhas(rs);
            }
        }
    }

    private static List<String> lerLinhas(ResultSet rs) throws SQLException {
        List<String> linhas = new ArrayList<>();
        while (rs.next()) {
            linhas.add(rs.getString(1) + " | " + rs.getString(2) + " | " + rs.getString(3));
        }
        return linhas;
    }
}
