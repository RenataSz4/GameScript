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

    public void crearTuplaSet(int indiceEntidad, int indiceAtributo, int indiceValor) {
        tuplas.add(new Set(
            tokens.get(indiceEntidad),
            tokens.get(indiceAtributo),
            tokens.get(indiceValor),
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

    public void crearTuplaWhile(int indiceValor1, int indiceOp, int indiceValor2) {
        tuplas.add(new While(
            tokens.get(indiceValor1),
            tokens.get(indiceOp),
            tokens.get(indiceValor2),
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
