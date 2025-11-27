public class While extends Tupla {
    Token valor1, operador, valor2;

    public While(Token valor1, Token operador, Token valor2, int sv, int sf) {
        super(sv, sf);
        this.valor1 = valor1;
        this.operador = operador;
        this.valor2 = valor2;
    }

    public String toString() {
        return "( " + super.toString() + ", [ WHILE, " + valor1.getNombre() + " " + operador.getNombre() + " " + valor2.getNombre() + " ] )";
    }

    public int ejecutar(SymbolTable ts) {
        float operando1 = obtenerValor(valor1, ts);
        float operando2 = obtenerValor(valor2, ts);
        
        boolean resultado = false;
        
        switch (operador.getNombre()) {
            case ">":
                resultado = operando1 > operando2;
                break;
            case "<":
                resultado = operando1 < operando2;
                break;
            case ">=":
                resultado = operando1 >= operando2;
                break;
            case "<=":
                resultado = operando1 <= operando2;
                break;
            case "==":
                resultado = operando1 == operando2;
                break;
            case "!=":
                resultado = operando1 != operando2;
                break;
        }
        
        return resultado ? saltoVerdadero : saltoFalso;
    }
    
    private float obtenerValor(Token token, SymbolTable ts) {
        String nombreToken = token.getNombre();
        
        //si contiene punto, es referencia a atributo de entidad
        if (nombreToken.contains(".")) {
            String[] partes = nombreToken.split("\\.");
            if (partes.length == 2) {
                EntitySymbol entity = (EntitySymbol) ts.resolve(partes[0]);
                if (entity != null) {
                    String valor = entity.getAttributes().get(partes[1]);
                    if (valor != null) {
                        return Float.parseFloat(valor);
                    }
                }
            }
        } else {
            try {
                return Float.parseFloat(nombreToken);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        
        return 0;
    }
}
