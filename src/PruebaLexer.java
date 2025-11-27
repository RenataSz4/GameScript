import java.io.FileReader;
import java.io.IOException;

public class PruebaLexer {

    public static void main(String[] arg) throws LexicalException {
        String entrada = leerPrograma("src/script.txt");
        GameScriptLexer lexer = new GameScriptLexer();
        lexer.analizar(entrada);

        System.out.println("*** Analisis léxico ***");

        for (Token t: lexer.getTokens()) {
            System.out.println(t);
        }
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
