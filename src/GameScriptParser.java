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
        if (match("GAME"))
            if (match("IDENTIFICADOR"))
                if (SeccionEntidades())
                    if (SeccionReglas())
                        return true;

        return false;
    }

    private boolean SeccionEntidades() {
        int indiceAux = indiceToken;

        if (Entidad()) {
            while (Entidad()) {
            }
            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Entidad() {
        int indiceAux = indiceToken;

        if (match("ENTITY"))
            if (match("IDENTIFICADOR"))
                if (match("LLAVEIZQ"))
                    if (Atributos())
                        if (match("LLAVEDER"))
                            return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Atributos() {
        int indiceAux = indiceToken;

        if (Atributo()) {
            while (match("COMA") && Atributo()) {
            }
            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Atributo() {
        int indiceAux = indiceToken;

        if (match("IDENTIFICADOR"))
            if (match("DOSPUNTOS"))
                if (Valor())
                    return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean SeccionReglas() {
        int indiceAux = indiceToken;

        if (Regla()) {
            while (Regla()) {
            }
            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Regla() {
        int indiceAux = indiceToken;

        if (match("RULE"))
            if (TipoRegla())
                if (match("LLAVEIZQ"))
                    if (Instrucciones())
                        if (match("LLAVEDER"))
                            return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean TipoRegla() {
        return match("ONSTART") || match("ONUPDATE") ||
                match("ONWIN") || match("ONLOSE");
    }

    private boolean Instrucciones() {
        while (Instruccion()) {
        }
        return true;
    }

    private boolean Instruccion() {
        int indiceAux = indiceToken;

        if (indiceToken >= tokens.size())
            return false;

        String tipoToken = tokens.get(indiceToken).getTipo().getNombre();

        if (tipoToken.equals("MOVE"))
            if (InstruccionMove())
                return true;

        indiceToken = indiceAux;
        if (tipoToken.equals("PRINT"))
            if (InstruccionPrint())
                return true;

        indiceToken = indiceAux;
        if (tipoToken.equals("IF"))
            if (InstruccionIf())
                return true;

        indiceToken = indiceAux;
        if (tipoToken.equals("WHILE"))
            if (InstruccionWhile())
                return true;

        indiceToken = indiceAux;
        if (tipoToken.equals("SET"))
            if (InstruccionSet())
                return true;

        indiceToken = indiceAux;
        if (tipoToken.equals("ENDGAME"))
            if (InstruccionEndGame())
                return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionMove() {
        int indiceAux = indiceToken;

        if (match("MOVE"))
            if (match("IDENTIFICADOR"))
                if (Expresion())
                    if (Expresion())
                        if (match("PUNTOYCOMA"))
                            return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionPrint() {
        int indiceAux = indiceToken;

        if (match("PRINT"))
            if (match("CADENA"))
                if (match("PUNTOYCOMA"))
                    return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionIf() {
        int indiceAux = indiceToken;

        if (match("IF"))
            if (match("PARENTESISIZQ"))
                if (Condicion())
                    if (match("PARENTESISDER"))
                        if (match("LLAVEIZQ"))
                            if (Instrucciones())
                                if (match("LLAVEDER")) {
                                    int auxElse = indiceToken;
                                    if (match("ELSE")) {
                                        if (match("LLAVEIZQ"))
                                            if (Instrucciones())
                                                if (match("LLAVEDER"))
                                                    return true;
                                        indiceToken = auxElse;
                                    }
                                    return true;
                                }

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionWhile() {
        int indiceAux = indiceToken;

        if (match("WHILE"))
            if (match("PARENTESISIZQ"))
                if (Condicion())
                    if (match("PARENTESISDER"))
                        if (match("LLAVEIZQ"))
                            if (Instrucciones())
                                if (match("LLAVEDER"))
                                    return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionSet() {
        int indiceAux = indiceToken;

        if (match("SET"))
            if (match("IDENTIFICADOR"))
                if (match("PUNTO"))
                    if (match("IDENTIFICADOR"))
                        if (match("IGUAL"))
                            if (Expresion())
                                if (match("PUNTOYCOMA"))
                                    return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionEndGame() {
        int indiceAux = indiceToken;

        if (match("ENDGAME"))
            if (match("PUNTOYCOMA"))
                return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Condicion() {
        int indiceAux = indiceToken;

        if (Expresion())
            if (match("OPRELACIONAL"))
                if (Expresion())
                    return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Expresion() {
        int indiceAux = indiceToken;

        if (Termino()) {
            while (indiceToken < tokens.size() &&
                    tokens.get(indiceToken).getTipo().getNombre().equals("OPARITMETICO") &&
                    (tokens.get(indiceToken).getNombre().equals("+") ||
                            tokens.get(indiceToken).getNombre().equals("-"))) {
                match("OPARITMETICO");
                if (!Termino()) {
                    indiceToken = indiceAux;
                    return false;
                }
            }
            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Termino() {
        int indiceAux = indiceToken;

        if (Factor()) {
            while (indiceToken < tokens.size() &&
                    tokens.get(indiceToken).getTipo().getNombre().equals("OPARITMETICO") &&
                    (tokens.get(indiceToken).getNombre().equals("*") ||
                            tokens.get(indiceToken).getNombre().equals("/"))) {
                match("OPARITMETICO");
                if (!Factor()) {
                    indiceToken = indiceAux;
                    return false;
                }
            }
            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Factor() {
        int indiceAux = indiceToken;

        if (match("NUMERO"))
            return true;

        indiceToken = indiceAux;

        if (match("IDENTIFICADOR")) {
            int auxPunto = indiceToken;
            if (match("PUNTO")) {
                if (match("IDENTIFICADOR"))
                    return true;
                indiceToken = auxPunto;
            }
            return true;
        }

        indiceToken = indiceAux;

        if (match("PARENTESISIZQ"))
            if (Expresion())
                if (match("PARENTESISDER"))
                    return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean Valor() {
        int indiceAux = indiceToken;

        if (match("NUMERO"))
            return true;

        indiceToken = indiceAux;

        if (match("CADENA"))
            return true;

        indiceToken = indiceAux;

        if (Expresion())
            return true;

        indiceToken = indiceAux;
        return false;
    }

    private boolean match(String nombre) {
        if (indiceToken >= tokens.size()) {
            if (ex == null)
                ex = new SyntaxException(nombre, "FIN DE ARCHIVO");
            return false;
        }

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
