import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameScriptLexer {
    private ArrayList<TipoToken> tipos = new ArrayList<>();
    private ArrayList<Token> tokens = new ArrayList<>();

    public GameScriptLexer() {
        tipos.add(new TipoToken(TipoToken.GAME, "GAME"));
        tipos.add(new TipoToken(TipoToken.ENTITY, "ENTITY"));
        tipos.add(new TipoToken(TipoToken.RULE, "RULE"));
        tipos.add(new TipoToken(TipoToken.ONSTART, "ON_START"));
        tipos.add(new TipoToken(TipoToken.ONUPDATE, "ON_UPDATE"));
        tipos.add(new TipoToken(TipoToken.ONWIN, "ON_WIN"));
        tipos.add(new TipoToken(TipoToken.ONLOSE, "ON_LOSE"));
        tipos.add(new TipoToken(TipoToken.MOVE, "MOVE"));
        tipos.add(new TipoToken(TipoToken.PRINT, "PRINT"));
        tipos.add(new TipoToken(TipoToken.INPUT, "INPUT"));
        tipos.add(new TipoToken(TipoToken.IF, "IF"));
        tipos.add(new TipoToken(TipoToken.ELSE, "ELSE"));
        tipos.add(new TipoToken(TipoToken.WHILE, "WHILE"));
        tipos.add(new TipoToken(TipoToken.ENDGAME, "END_GAME"));
        tipos.add(new TipoToken(TipoToken.SET, "SET"));
        tipos.add(new TipoToken(TipoToken.DOSPUNTOS, ":"));
        tipos.add(new TipoToken(TipoToken.PUNTOYCOMA, ";"));
        tipos.add(new TipoToken(TipoToken.COMA, ","));
        tipos.add(new TipoToken(TipoToken.PUNTO, "\\."));
        tipos.add(new TipoToken(TipoToken.IGUAL, "="));
        tipos.add(new TipoToken(TipoToken.LLAVEIZQ, "\\{"));
        tipos.add(new TipoToken(TipoToken.LLAVEDER, "\\}"));
        tipos.add(new TipoToken(TipoToken.PARENTESISIZQ, "\\("));
        tipos.add(new TipoToken(TipoToken.PARENTESISDER, "\\)"));
        tipos.add(new TipoToken(TipoToken.OPRELACIONAL, "<=|>=|!=|<|>"));
        tipos.add(new TipoToken(TipoToken.OPARITMETICO, "[+\\-*/]"));
        tipos.add(new TipoToken(TipoToken.NUMERO, "-?[0-9]+(\\.[0-9]+)?"));
        tipos.add(new TipoToken(TipoToken.CADENA, "\"[^\"]*\""));
        tipos.add(new TipoToken(TipoToken.IDENTIFICADOR, "[a-zA-Z_][a-zA-Z0-9_]*"));
        tipos.add(new TipoToken(TipoToken.ESPACIO, "[ \t\f\r\n]+"));
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void analizar(String entrada) throws LexicalException {
        StringBuffer er = new StringBuffer();

        for (TipoToken tt : tipos)
            er.append(String.format("|(?<%s>%s)", tt.getNombre(), tt.getPatron()));

        Pattern p = Pattern.compile(new String(er.substring(1)));
        Matcher m = p.matcher(entrada);

        while (m.find()) {
            for (TipoToken tt : tipos) {
                if (m.group(TipoToken.ESPACIO) != null)
                    continue;
                else if (m.group(tt.getNombre()) != null) {
                    if (tt.getNombre().equals(TipoToken.ERROR)) {
                        LexicalException ex = new LexicalException(m.group(tt.getNombre()));
                        throw ex;
                    }

                    String nombre = m.group(tt.getNombre());

                    if (tt.getNombre().equals(TipoToken.CADENA)) {
                        nombre = nombre.substring(1, nombre.length() - 1);
                    }

                    tokens.add(new Token(tt, nombre));
                    break;
                }
            }
        }
    }
}
