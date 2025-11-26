import java.io.FileReader;
import java.io.IOException;

public class PruebaParser {
    public static void main(String[] args) throws LexicalException, SyntaxException {
        String entrada = leerPrograma("src/script.txt");
        GameScriptLexer lexer = new GameScriptLexer();
        lexer.analizar(entrada);

        System.out.println("*** Análisis léxico ***\n");

        for (Token t: lexer.getTokens()) {
            System.out.println(t);
        }

        System.out.println("\n*** Análisis sintáctico ***\n");

        GameScriptParser parser = new GameScriptParser();
        parser.analizar(lexer);
    }

    private static String leerPrograma(String nombre) {
        String entrada = "";

        try {
            FileReader reader = new FileReader(nombre);
            int caracter;

            while ((caracter = reader.read()) != -1)
                entrada += (char) caracter;

            reader.close();
            return entrada;
        } catch (IOException e) {
            return "";
        }
    }
}
