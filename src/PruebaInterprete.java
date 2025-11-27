import java.io.FileReader;
import java.io.IOException;

public class PruebaInterprete {

    public static String leerPrograma(String nombre) {
        String entrada = "";
        try {
            FileReader reader = new FileReader(nombre);
            int caracter;
            while ((caracter = reader.read()) != -1)
                entrada += (char) caracter;
            reader.close();
            return entrada;
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
            System.exit(1);
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            String entrada = leerPrograma("src/script.txt");

            GameScriptLexer lexer = new GameScriptLexer();
            lexer.analizar(entrada);

            System.out.println("*** Análisis léxico ***");
            for (Token t : lexer.getTokens()) {
                System.out.println(t);
            }

            System.out.println("\n*** Análisis sintáctico ***");

            GameScriptParser parser = new GameScriptParser();
            parser.analizar(lexer);

            System.out.println("\n*** Tuplas generadas ***");
            int i = 0;
            for (Tupla t : parser.getTuplas()) {
                System.out.println(i + ": " + t);
                i++;
            }

            System.out.println("\n*** Ejecución del programa ***\n");
            System.out.println("*** Intérprete ***");
            
            GameScriptInterprete interprete = new GameScriptInterprete(parser.getSymbolTable());
            interprete.interpretar(parser.getTuplas());

        } catch (LexicalException e) {
            System.err.println("ERROR LEXICO: " + e.getMessage());
        } catch (SyntaxException e) {
            System.err.println("ERROR SINTACTICO: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
