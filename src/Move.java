public class Move extends Tupla {
    Token entidad, x, y;

    public Move(Token entidad, Token x, Token y, int sv, int sf) {
        super(sv, sf);
        this.entidad = entidad;
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "( " + super.toString() + ", [ MOVE, " + entidad.getNombre() + ", " + x.getNombre() + ", " + y.getNombre() + " ] )";
    }

    public int ejecutar(SymbolTable ts) {
        EntitySymbol entity = (EntitySymbol) ts.resolve(entidad.getNombre());
        
        if (entity != null) {
            String nuevoX = x.getNombre();
            String nuevoY = y.getNombre();
            
            entity.defineAttribute("posX", nuevoX);
            entity.defineAttribute("posY", nuevoY);
        }
        
        return saltoVerdadero;
    }
}
