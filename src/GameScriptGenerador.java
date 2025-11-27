import java.util.ArrayList;

public class GameScriptGenerador {
    private ArrayList<Tupla> tuplas = new ArrayList<>();
    private ArrayList<Token> tokens;

    public GameScriptGenerador(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public void crearTuplaMove(int indiceEntidad, int indiceX, int indiceY) {
        tuplas.add(new Move(
            tokens.get(indiceEntidad),
            tokens.get(indiceX),
            tokens.get(indiceY),
            tuplas.size() + 1,
            tuplas.size() + 1
        ));
    }

    public void crearTuplaPrint(int indiceMensaje) {
        tuplas.add(new Print(
            tokens.get(indiceMensaje),
            tuplas.size() + 1,
            tuplas.size() + 1
        ));
    }

    public void crearTuplaInput(int indiceEntidad, int indiceAtributo, int indiceMensaje) {
        tuplas.add(new Input(
            tokens.get(indiceEntidad),
            tokens.get(indiceAtributo),
            tokens.get(indiceMensaje),
            tuplas.size() + 1,
            tuplas.size() + 1
        ));
    }

    public void crearTuplaSet(int indiceEntidad, int indiceAtributo, int indiceValor) {
        tuplas.add(new Set(
            tokens.get(indiceEntidad),
            tokens.get(indiceAtributo),
            tokens.get(indiceValor),
            tuplas.size() + 1,
            tuplas.size() + 1
        ));
    }

    public void crearTuplaSetConRangos(int indiceEntidad, int indiceAtributo, int indiceValorInicio, int indiceValorFin) {
        //token combinado para valor de expresion
        StringBuilder valorStr = new StringBuilder();
        for (int i = indiceValorInicio; i <= indiceValorFin; i++) {
            String tokenActual = tokens.get(i).getNombre();
            //agregar espacio solo si no es punto y anterior no era punto
            if (i > indiceValorInicio) {
                String tokenAnterior = tokens.get(i - 1).getNombre();
                if (!tokenActual.equals(".") && !tokenAnterior.equals(".")) {
                    valorStr.append(" ");
                }
            }
            valorStr.append(tokenActual);
        }
        Token tokenValor = new Token(tokens.get(indiceValorInicio).getTipo(), valorStr.toString());
        
        tuplas.add(new Set(
            tokens.get(indiceEntidad),
            tokens.get(indiceAtributo),
            tokenValor,
            tuplas.size() + 1,
            tuplas.size() + 1
        ));
    }

    public void crearTuplaIf(int indiceValor1, int indiceOp, int indiceValor2) {
        tuplas.add(new If(
            tokens.get(indiceValor1),
            tokens.get(indiceOp),
            tokens.get(indiceValor2),
            tuplas.size() + 1,
            tuplas.size() + 1
        ));
    }

    public void crearTuplaIfConRangos(int indiceValor1Inicio, int indiceValor1Fin, int indiceOp, int indiceValor2Inicio, int indiceValor2Fin) {
        //token combinado para valor1
        StringBuilder valor1Str = new StringBuilder();
        for (int i = indiceValor1Inicio; i <= indiceValor1Fin; i++) {
            valor1Str.append(tokens.get(i).getNombre());
        }
        Token tokenValor1 = new Token(tokens.get(indiceValor1Inicio).getTipo(), valor1Str.toString());
        
        //token combinado para valor2
        StringBuilder valor2Str = new StringBuilder();
        for (int i = indiceValor2Inicio; i <= indiceValor2Fin; i++) {
            valor2Str.append(tokens.get(i).getNombre());
        }
        Token tokenValor2 = new Token(tokens.get(indiceValor2Inicio).getTipo(), valor2Str.toString());
        
        tuplas.add(new If(
            tokenValor1,
            tokens.get(indiceOp),
            tokenValor2,
            tuplas.size() + 1,
            tuplas.size() + 1
        ));
    }

    public void crearTuplaWhile(int indiceValor1, int indiceOp, int indiceValor2) {
        tuplas.add(new While(
            tokens.get(indiceValor1),
            tokens.get(indiceOp),
            tokens.get(indiceValor2),
            tuplas.size() + 1,
            tuplas.size() + 1
        ));
    }

    public void crearTuplaWhileConRangos(int indiceValor1Inicio, int indiceValor1Fin, int indiceOp, int indiceValor2Inicio, int indiceValor2Fin) {
        //token combinado para valor1
        StringBuilder valor1Str = new StringBuilder();
        for (int i = indiceValor1Inicio; i <= indiceValor1Fin; i++) {
            valor1Str.append(tokens.get(i).getNombre());
        }
        Token tokenValor1 = new Token(tokens.get(indiceValor1Inicio).getTipo(), valor1Str.toString());
        
        //token combinado para valor2
        StringBuilder valor2Str = new StringBuilder();
        for (int i = indiceValor2Inicio; i <= indiceValor2Fin; i++) {
            valor2Str.append(tokens.get(i).getNombre());
        }
        Token tokenValor2 = new Token(tokens.get(indiceValor2Inicio).getTipo(), valor2Str.toString());
        
        tuplas.add(new While(
            tokenValor1,
            tokens.get(indiceOp),
            tokenValor2,
            tuplas.size() + 1,
            tuplas.size() + 1
        ));
    }

    public void crearTuplaEndGame() {
        tuplas.add(new EndGame());
    }

    public void conectarIf(int tuplaInicial) {
        int tuplaFinal = tuplas.size() - 1;
        if (tuplaInicial >= tuplas.size() || tuplaInicial >= tuplaFinal)
            return;
        tuplas.get(tuplaInicial).setSaltoFalso(tuplaFinal + 1);
    }

    public void conectarWhile(int tuplaInicial) {
        int tuplaFinal = tuplas.size() - 1;
        if (tuplaInicial >= tuplas.size() || tuplaInicial >= tuplaFinal)
            return;
        
        tuplas.get(tuplaInicial).setSaltoFalso(tuplaFinal + 1);
        tuplas.get(tuplaFinal).setSaltoVerdadero(tuplaInicial);
        tuplas.get(tuplaFinal).setSaltoFalso(tuplaInicial);
    }

    public ArrayList<Tupla> getTuplas() {
        return tuplas;
    }
}
