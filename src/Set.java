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
        EntitySymbol entity = (EntitySymbol) ts.resolve(entidad.getNombre());
        
        if (entity != null) {
            String nuevoValor = valor.getNombre();
            
            //si valor es expresion como "jugador.puntos + item.valor"
            //intentar evaluarla
            if (nuevoValor.contains(".")) {
                // Parsear expresiones del tipo "entidad.atributo [operador entidad.atributo]"
                String[] partes = nuevoValor.split("\\s+");
                
                if (partes.length == 1) {
                    String[] ref = partes[0].split("\\.");
                    if (ref.length == 2) {
                        EntitySymbol refEntity = (EntitySymbol) ts.resolve(ref[0]);
                        if (refEntity != null) {
                            nuevoValor = refEntity.getAttributes().get(ref[1]);
                        }
                    }
                } else if (partes.length == 3) {
                    String[] ref1 = partes[0].split("\\.");
                    String operador = partes[1];
                    String operando2Str = partes[2];
                    
                    float operando1 = 0;
                    float operando2 = 0;
                    
                    if (ref1.length == 2) {
                        EntitySymbol refEntity = (EntitySymbol) ts.resolve(ref1[0]);
                        if (refEntity != null) {
                            String val = refEntity.getAttributes().get(ref1[1]);
                            operando1 = Float.parseFloat(val);
                        }
                    }
                    
                    if (operando2Str.contains(".")) {
                        String[] ref2 = operando2Str.split("\\.");
                        if (ref2.length == 2) {
                            EntitySymbol refEntity = (EntitySymbol) ts.resolve(ref2[0]);
                            if (refEntity != null) {
                                String val = refEntity.getAttributes().get(ref2[1]);
                                operando2 = Float.parseFloat(val);
                            }
                        }
                    } else {
                        operando2 = Float.parseFloat(operando2Str);
                    }
                    
                    //operacion
                    float resultado = 0;
                    switch (operador) {
                        case "+":
                            resultado = operando1 + operando2;
                            break;
                        case "-":
                            resultado = operando1 - operando2;
                            break;
                        case "*":
                            resultado = operando1 * operando2;
                            break;
                        case "/":
                            if (operando2 != 0) {
                                resultado = operando1 / operando2;
                            } else {
                                System.out.println("Error: División entre cero");
                            }
                            break;
                    }
                    
                    nuevoValor = String.valueOf((int) resultado);
                }
            }
            
            entity.defineAttribute(atributo.getNombre(), nuevoValor);
        }
        
        return saltoVerdadero;
    }
}
