public class If extends Tupla {
    Token valor1, operador, valor2;

    public If(Token valor1, Token operador, Token valor2, int sv, int sf) {
        super(sv, sf);
        this.valor1 = valor1;
        this.operador = operador;
        this.valor2 = valor2;
    }

    public String toString() {
        return "( " + super.toString() + ", [ IF, " + valor1.getNombre() + " " + operador.getNombre() + " " + valor2.getNombre() + " ] )";
    }

    public int ejecutar(SymbolTable ts) {
        return saltoVerdadero;
    }
}
