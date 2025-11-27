public class EndGame extends Tupla {
    
    public EndGame() {
        super(-1, -1);
    }

    public String toString() {
        return "( " + super.toString() + ", [ END_GAME ] )";
    }

    public int ejecutar(SymbolTable ts) {
        return -1;
    }
}
