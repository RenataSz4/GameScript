public class TipoToken {
    private String nombre;
    private String patron;

    public TipoToken(String nombre, String patron) {
        this.nombre = nombre;
        this.patron = patron;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPatron() {
        return patron;
    }

    public static String GAME = "GAME";
    public static String ENTITY = "ENTITY";
    public static String RULE = "RULE";
    public static String ONSTART = "ONSTART";
    public static String ONUPDATE = "ONUPDATE";
    public static String ONWIN = "ONWIN";
    public static String ONLOSE = "ONLOSE";
    public static String MOVE = "MOVE";
    public static String PRINT = "PRINT";
    public static String INPUT = "INPUT";
    public static String IF = "IF";
    public static String ELSE = "ELSE";
    public static String WHILE = "WHILE";
    public static String ENDGAME = "ENDGAME";
    public static String SET = "SET";
    public static String DOSPUNTOS = "DOSPUNTOS";
    public static String PUNTOYCOMA = "PUNTOYCOMA";
    public static String COMA = "COMA";
    public static String PUNTO = "PUNTO";
    public static String IGUAL = "IGUAL";
    public static String LLAVEIZQ = "LLAVEIZQ";
    public static String LLAVEDER = "LLAVEDER";
    public static String PARENTESISIZQ = "PARENTESISIZQ";
    public static String PARENTESISDER = "PARENTESISDER";
    public static String OPRELACIONAL = "OPRELACIONAL";
    public static String OPARITMETICO = "OPARITMETICO";
    public static String NUMERO = "NUMERO";
    public static String CADENA = "CADENA";
    public static String IDENTIFICADOR = "IDENTIFICADOR";
    public static String ESPACIO = "ESPACIO";
    public static String ERROR = "ERROR";

}