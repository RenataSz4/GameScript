public class Print extends Tupla {
    Token mensaje;

    public Print(Token mensaje, int sv, int sf) {
        super(sv, sf);
        this.mensaje = mensaje;
    }

    public String toString() {
        return "( " + super.toString() + ", [ PRINT, \"" + mensaje.getNombre() + "\" ] )";
    }

    public int ejecutar(SymbolTable ts) {
        return saltoVerdadero;
    }
}
