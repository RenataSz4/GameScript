import java.io.FileReader;
import java.io.IOException;

public class PruebaEnsamblador {

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
            System.out.println("ERROR: No se pudo leer el archivo '" + nombre + "'");
            System.out.println("Mensaje: " + e.getMessage());
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
            int contador = 0;
            for (Tupla t : parser.getTuplas()) {
                System.out.println(contador + ": " + t);
                contador++;
            }

            System.out.println("\n*** Generación de código ensamblador ***\n");

            GameScriptEnsamblador ensamblador = new GameScriptEnsamblador(parser.getTuplas());
            ensamblador.generarCodigo();
            ensamblador.imprimir();

            String archivoSalida = "src/output.asm";
            ensamblador.guardarEnArchivo(archivoSalida);

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
