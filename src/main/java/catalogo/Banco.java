package catalogo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Banco {

    public static final String URL = "jdbc:h2:mem:catalogo;DB_CLOSE_DELAY=-1";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, "sa", "");
    }

    public static void criarEsquemaInicial(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.execute("CREATE TABLE itens ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "titulo VARCHAR(1000), "
                    + "autor VARCHAR(1000), "
                    + "ano VARCHAR(50), "
                    + "genero VARCHAR(1000), "
                    + "detalhes VARCHAR(4000))");

            st.execute("CREATE TABLE usuarios_demo ("
                    + "id INT PRIMARY KEY, login VARCHAR(100), senha VARCHAR(100))");

            st.execute("INSERT INTO itens (titulo, autor, ano, genero, detalhes) VALUES "
                    + "('Dom Casmurro', 'Machado de Assis', '1899', 'Romance', 'Clássico brasileiro'), "
                    + "('Cidade de Deus', 'Fernando Meirelles', '2002', 'Drama', 'Filme brasileiro'), "
                    + "('O Senhor dos Anéis', 'J. R. R. Tolkien', '1954', 'Fantasia', 'Trilogia')");

            st.execute("INSERT INTO usuarios_demo VALUES "
                    + "(1, 'admin', 'senha-secreta-123'), "
                    + "(2, 'maria', 'abc12345')");
        }
    }

    public static void criarEsquemaMelhorado(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            st.execute("CREATE TABLE item_midia ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "titulo VARCHAR(255) NOT NULL, "
                    + "autor_diretor VARCHAR(255), "
                    + "ano_lancamento INT CHECK (ano_lancamento BETWEEN 1400 AND 2100), "
                    + "genero VARCHAR(100), "
                    + "sinopse VARCHAR(4000), "
                    + "tipo_midia VARCHAR(50) NOT NULL CHECK (tipo_midia IN ('Livro', 'Filme', 'Série')))");
            st.execute("CREATE INDEX idx_item_titulo ON item_midia(titulo)");
            st.execute("CREATE INDEX idx_item_autor ON item_midia(autor_diretor)");
        }
    }
}
