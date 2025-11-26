import java.util.ArrayList;

public class GameScriptParser {
    private ArrayList<Token> tokens;
    private int indiceToken = 0;
    private SyntaxException ex;

    public void analizar(GameScriptLexer lexer) throws SyntaxException {
        tokens = lexer.getTokens();

        if (Programa()) {
            if (indiceToken == tokens.size()) {
                System.out.println("\nLa sintaxis del programa es correcta");
                return;
            }
        }

        throw ex;
    }

    private boolean Programa() {
        if (match("INICIOPROGRAMA"))
            if (Enunciados())
                if (match("FINPROGRAMA"))
                    return true;

        return false;
    }

    private boolean Enunciados() {
        int indiceAux = indiceToken;

        if (Enunciado()) {
            while (Enunciado());
            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Enunciado() {
        int indiceAux = indiceToken;

        if (tokens.get(indiceToken).getTipo().getNombre().equals("VARIABLE"))
            if (Asignacion())
                return true;

        indiceToken = indiceAux;
        if (tokens.get(indiceToken).getTipo().getNombre().equals("LEER"))
            if (Leer())
                return true;

        indiceToken = indiceAux;
        if (tokens.get(indiceToken).getTipo().getNombre().equals("ESCRIBIR"))
            if (Escribir())
                return true;

        indiceToken = indiceAux;
        if (tokens.get(indiceToken).getTipo().getNombre().equals("SI"))
            if (Si())
                return true;

        indiceToken = indiceAux;
        if (tokens.get(indiceToken).getTipo().getNombre().equals("MIENTRAS"))
            if (Mientras())
                return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Asignacion() {
        int indiceAux = indiceToken;

        if (match("VARIABLE"))
            if (match("IGUAL"))
                if (Expresion())
                    return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Expresion() {
        int indiceAux = indiceToken;

        if (Valor())
            if (match("OPARITMETICO"))
                if (Valor())
                    return true;

        indiceToken = indiceAux;

        if (Valor())
            return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Valor() {
        if (match("VARIABLE") || match("NUMERO"))
            return true;

        return false;
    }

    private boolean Leer() {
        int indiceAux = indiceToken;

        if (match("LEER"))
            if (match("VARIABLE"))
                return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Escribir() {
        int indiceAux = indiceToken;

        if (match("ESCRIBIR"))
            if (match("CADENA"))
                if (match("COMA"))
                    if (match("VARIABLE"))
                        return true;

        indiceToken = indiceAux;

        if (match("ESCRIBIR"))
            if (match("CADENA"))
                return true;

        indiceToken = indiceAux;

        if (match("ESCRIBIR"))
            if (match("VARIABLE"))
                return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Si() {
        int indiceAux = indiceToken;

        if (match("SI"))
            if (Comparacion())
                if (match("ENTONCES"))
                    if (Enunciados())
                        if (match("FINSI"))
                            return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Mientras() {
        int indiceAux = indiceToken;

        if (match("MIENTRAS"))
            if (Comparacion())
                if (Enunciados())
                    if (match("FINMIENTRAS"))
                        return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Comparacion() {
        int indiceAux = indiceToken;

        if (match("PARENTESISIZQ"))
            if (Valor())
                if (match("OPRELACIONAL"))
                    if (Valor())
                        if (match("PARENTESISDER"))
                            return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean match(String nombre) {
        if (tokens.get(indiceToken).getTipo().getNombre().equals(nombre)) {
            System.out.println(nombre + ": " + tokens.get(indiceToken).getNombre());

            indiceToken++;
            return true;
        }

        if (ex == null)
            ex = new SyntaxException(nombre, tokens.get(indiceToken).getTipo().getNombre());

        return false;
    }
}
