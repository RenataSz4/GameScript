import java.util.ArrayList;

public class GameScriptParser {
    private ArrayList<Token> tokens;
    private int indiceToken = 0;
    private SyntaxException ex;
    private SymbolTable symbolTable;
    private GameScriptGenerador generador;

    public void analizar(GameScriptLexer lexer) throws SyntaxException {
        tokens = lexer.getTokens();
        symbolTable = new SymbolTable();
        generador = new GameScriptGenerador(tokens);

        if (Programa()) {
            if (indiceToken == tokens.size()) {
                System.out.println("\nLa sintaxis del programa es correcta");
                System.out.println("\n*** Tabla de símbolos ***");
                System.out.println(symbolTable);
                return;
            }
        }

        throw ex;
    }

    public ArrayList<Tupla> getTuplas() {
        if (generador != null) {
            return generador.getTuplas();
        }
        return new ArrayList<Tupla>();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
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

        if (match("ENTITY")) {
            if (match("IDENTIFICADOR")) {
                String nombreEntidad = tokens.get(indiceToken - 1).getNombre();
                Type entityType = (Type) symbolTable.resolve("entity");
                EntitySymbol entity = new EntitySymbol(nombreEntidad, entityType);
                symbolTable.define(entity);

                if (match("LLAVEIZQ"))
                    if (Atributos(entity))
                        if (match("LLAVEDER"))
                            return true;
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Atributos(EntitySymbol entity) {
        int indiceAux = indiceToken;

        if (Atributo(entity)) {
            while (match("COMA") && Atributo(entity)) {
            }
            return true;
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Atributo(EntitySymbol entity) {
        int indiceAux = indiceToken;

        if (match("IDENTIFICADOR")) {
            String nombreAtributo = tokens.get(indiceToken - 1).getNombre();
            if (match("DOSPUNTOS")) {
                if (Valor()) {
                    String valorAtributo = tokens.get(indiceToken - 1).getNombre();
                    entity.defineAttribute(nombreAtributo, valorAtributo);
                    return true;
                }
            }
        }

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
        if (tipoToken.equals("INPUT"))
            if (InstruccionInput())
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

        if (match("MOVE")) {
            if (match("IDENTIFICADOR")) {
                int indiceEntidad = indiceToken - 1;
                String entidad = tokens.get(indiceToken - 1).getNombre();
                if (symbolTable.resolve(entidad) == null) {
                    ex = new SyntaxException("Entidad '" + entidad + "' no declarada");
                    indiceToken = indiceAux;
                    return false;
                }
                int indiceX = indiceToken;
                if (Expresion()) {
                    int indiceY = indiceToken;
                    if (Expresion()) {
                        if (match("PUNTOYCOMA")) {
                            generador.crearTuplaMove(indiceEntidad, indiceX, indiceY);
                            return true;
                        }
                    }
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionPrint() {
        int indiceAux = indiceToken;

        if (match("PRINT")) {
            if (match("CADENA")) {
                int indiceMensaje = indiceToken - 1;
                if (match("PUNTOYCOMA")) {
                    generador.crearTuplaPrint(indiceMensaje);
                    return true;
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionInput() {
        int indiceAux = indiceToken;

        if (match("INPUT")) {
            if (match("IDENTIFICADOR")) {
                int indiceEntidad = indiceToken - 1;
                if (match("PUNTO")) {
                    if (match("IDENTIFICADOR")) {
                        int indiceAtributo = indiceToken - 1;
                        if (match("CADENA")) {
                            int indiceMensaje = indiceToken - 1;
                            if (match("PUNTOYCOMA")) {
                                generador.crearTuplaInput(indiceEntidad, indiceAtributo, indiceMensaje);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionIf() {
        int indiceAux = indiceToken;

        if (match("IF")) {
            if (match("PARENTESISIZQ")) {
                int tuplaInicial = generador.getTuplas().size();
                if (Condicion(true)) {
                    if (match("PARENTESISDER")) {
                        if (match("LLAVEIZQ")) {
                            if (Instrucciones()) {
                                if (match("LLAVEDER")) {
                                    generador.conectarIf(tuplaInicial);
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
                            }
                        }
                    }
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionWhile() {
        int indiceAux = indiceToken;

        if (match("WHILE")) {
            if (match("PARENTESISIZQ")) {
                int tuplaInicial = generador.getTuplas().size();
                if (Condicion(false)) {
                    if (match("PARENTESISDER")) {
                        if (match("LLAVEIZQ")) {
                            if (Instrucciones()) {
                                if (match("LLAVEDER")) {
                                    generador.conectarWhile(tuplaInicial);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionSet() {
        int indiceAux = indiceToken;

        if (match("SET")) {
            if (match("IDENTIFICADOR")) {
                int indiceEntidad = indiceToken - 1;  // Capturar DESPUÉS del match
                String entidad = tokens.get(indiceEntidad).getNombre();
                Symbol symbol = symbolTable.resolve(entidad);
                if (symbol == null) {
                    ex = new SyntaxException("Entidad '" + entidad + "' no declarada");
                    indiceToken = indiceAux;
                    return false;
                }
                if (match("PUNTO")) {
                    if (match("IDENTIFICADOR")) {
                        int indiceAtributo = indiceToken - 1;  // Capturar DESPUÉS del match
                        String atributo = tokens.get(indiceAtributo).getNombre();
                        if (symbol instanceof EntitySymbol) {
                            EntitySymbol entitySym = (EntitySymbol) symbol;
                            if (!entitySym.getAttributes().containsKey(atributo)) {
                                ex = new SyntaxException("Atributo '" + atributo + "' no existe en entidad '" + entidad + "'");
                                indiceToken = indiceAux;
                                return false;
                            }
                        }
                        if (match("IGUAL")) {
                            int indiceValorInicio = indiceToken;
                            if (Expresion()) {
                                int indiceValorFin = indiceToken - 1;
                                if (match("PUNTOYCOMA")) {
                                    generador.crearTuplaSetConRangos(indiceEntidad, indiceAtributo, indiceValorInicio, indiceValorFin);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean InstruccionEndGame() {
        int indiceAux = indiceToken;

        if (match("ENDGAME")) {
            if (match("PUNTOYCOMA")) {
                generador.crearTuplaEndGame();
                return true;
            }
        }

        indiceToken = indiceAux;
        return false;
    }

    private boolean Condicion(boolean esIf) {
        int indiceAux = indiceToken;
        int indiceValor1Inicio = indiceToken;

        if (Expresion()) {
            int indiceValor1Fin = indiceToken - 1;
            if (match("OPRELACIONAL")) {
                int indiceOp = indiceToken - 1;
                int indiceValor2Inicio = indiceToken;
                if (Expresion()) {
                    int indiceValor2Fin = indiceToken - 1;
                    if (esIf) {
                        generador.crearTuplaIfConRangos(indiceValor1Inicio, indiceValor1Fin, indiceOp, indiceValor2Inicio, indiceValor2Fin);
                    } else {
                        generador.crearTuplaWhileConRangos(indiceValor1Inicio, indiceValor1Fin, indiceOp, indiceValor2Inicio, indiceValor2Fin);
                    }
                    return true;
                }
            }
        }

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
            String entidad = tokens.get(indiceToken - 1).getNombre();
            int auxPunto = indiceToken;
            if (match("PUNTO")) {
                if (match("IDENTIFICADOR")) {
                    String atributo = tokens.get(indiceToken - 1).getNombre();
                    Symbol symbol = symbolTable.resolve(entidad);
                    if (symbol == null) {
                        ex = new SyntaxException("Entidad '" + entidad + "' no declarada");
                        indiceToken = indiceAux;
                        return false;
                    }
                    if (symbol instanceof EntitySymbol) {
                        EntitySymbol entitySym = (EntitySymbol) symbol;
                        if (!entitySym.getAttributes().containsKey(atributo)) {
                            ex = new SyntaxException("Atributo '" + atributo + "' no existe en entidad '" + entidad + "'");
                            indiceToken = indiceAux;
                            return false;
                        }
                    }
                    return true;
                }
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
