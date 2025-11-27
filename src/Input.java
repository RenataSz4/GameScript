import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Input extends Tupla {
    Token entidad, atributo, mensaje;

    public Input(Token entidad, Token atributo, Token mensaje, int sv, int sf) {
        super(sv, sf);
        this.entidad = entidad;
        this.atributo = atributo;
        this.mensaje = mensaje;
    }

    public String toString() {
        return "( " + super.toString() + ", [ INPUT, " + entidad.getNombre() + "." + atributo.getNombre() + ", \"" + mensaje.getNombre() + "\" ] )";
    }

    public int ejecutar(SymbolTable ts) {
        String valor = "0";
        
        String textoSinComillas = mensaje.getNombre().replace("\"", "");
        System.out.print(textoSinComillas + " ");
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));

        try {
            valor = entrada.readLine();
        } catch (Exception ex) {
            System.out.println("Error al leer la entrada");
        }

        // Obtener la entidad y actualizar el atributo
        EntitySymbol entity = (EntitySymbol) ts.resolve(entidad.getNombre());

        if (entity != null) {
            entity.defineAttribute(atributo.getNombre(), valor);
        }

        return saltoVerdadero;
    }
}
