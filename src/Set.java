public class Set extends Tupla {
    Token entidad, atributo, valor;

    public Set(Token entidad, Token atributo, Token valor, int sv, int sf) {
        super(sv, sf);
        this.entidad = entidad;
        this.atributo = atributo;
        this.valor = valor;
    }

    public String toString() {
        return "( " + super.toString() + ", [ SET, " + entidad.getNombre() + "." + atributo.getNombre() + " = " + valor.getNombre() + " ] )";
    }

    public int ejecutar(SymbolTable ts) {
        return saltoVerdadero;
    }
}
