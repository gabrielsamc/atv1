package catalogo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class Principal {

    private static final String ATAQUE_TAUTOLOGIA = "' OR 1=1 --";
    private static final String ATAQUE_UNION =
            "zzz%' UNION SELECT id, login, senha FROM usuarios_demo --";

    public static void main(String[] args) throws SQLException {
        try (Connection c = Banco.conectar()) {
            Banco.criarEsquemaInicial(c);

            titulo("ETAPA 1 - Busca vulnerável com uso normal");
            mostrar(ItemDAO.buscarVulneravel(c, "Dom"));

            titulo("ETAPA 2 - Ataque: condição sempre verdadeira (retorna tudo)");
            System.out.println("   Entrada do usuário: " + ATAQUE_TAUTOLOGIA);
            mostrar(ItemDAO.buscarVulneravel(c, ATAQUE_TAUTOLOGIA));

            titulo("ETAPA 2 - Ataque: UNION vaza dados de OUTRA tabela");
            System.out.println("   Entrada do usuário: " + ATAQUE_UNION);
            mostrar(ItemDAO.buscarVulneravel(c, ATAQUE_UNION));

            titulo("ETAPA 3 - Correção com PreparedStatement: uso normal");
            mostrar(ItemDAO.buscarSeguro(c, "Dom"));

            titulo("ETAPA 3 - Mesmos ataques contra a busca segura");
            System.out.println("   Entrada do usuário: " + ATAQUE_TAUTOLOGIA);
            mostrar(ItemDAO.buscarSeguro(c, ATAQUE_TAUTOLOGIA));
            System.out.println("   Entrada do usuário: " + ATAQUE_UNION);
            mostrar(ItemDAO.buscarSeguro(c, ATAQUE_UNION));

            titulo("ETAPA 4 - Modelagem melhorada (tipos, restrições, índices)");
            Banco.criarEsquemaMelhorado(c);
            tentarInserir(c, "Dom Casmurro", "Machado de Assis", 1899, "Romance", "Ciúme e dúvida.", "Livro");
            tentarInserir(c, "Cidade de Deus", "Fernando Meirelles", 2002, "Drama", "Crime no Rio.", "Filme");
            System.out.println("   Tentativas inválidas (o próprio banco recusa):");
            tentarInserir(c, "Livro do futuro", "Autor", 3000, "Ficção", "Ano fora do limite.", "Livro");
            tentarInserir(c, "Sem tipo", "Autor", 2000, "Ficção", "Tipo inexistente.", "Jogo");
            tentarInserir(c, null, "Autor", 2000, "Ficção", "Sem título.", "Livro");
            System.out.println("   Busca por 'Dom' na tabela nova:");
            mostrar(ItemDAO.buscarItemMidia(c, "Dom"));
        }
    }

    private static void tentarInserir(Connection c, String titulo, String autor, Integer ano,
                                      String genero, String sinopse, String tipo) {
        try {
            ItemDAO.inserir(c, titulo, autor, ano, genero, sinopse, tipo);
            System.out.println("   OK: inserido '" + titulo + "' (" + tipo + ", " + ano + ")");
        } catch (SQLException e) {
            System.out.println("   RECUSADO: '" + titulo + "' (" + tipo + ", " + ano
                    + ") -> código SQL " + e.getSQLState());
        }
    }

    private static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }

    private static void mostrar(List<String> linhas) {
        System.out.println("   Resultados: " + linhas.size());
        for (String l : linhas) {
            System.out.println("     " + l);
        }
    }
}
