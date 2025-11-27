import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameScriptInterprete {
    SymbolTable ts;
    Map<String, Map<String, String>> estadoInicial;

    public GameScriptInterprete(SymbolTable ts) {
        this.ts = ts;
        this.estadoInicial = new HashMap<>();
    }

    public void interpretar(ArrayList<Tupla> tuplas) {
        if (tuplas == null || tuplas.isEmpty()) {
            System.out.println("No hay tuplas para ejecutar");
            return;
        }

        int indiceTupla = 0;
        Tupla t = tuplas.get(0);
        int contadorTuplas = 0;

        do {
            indiceTupla = t.ejecutar(ts);
            contadorTuplas++;
            
            //guarda estado inicial despues de INPUTs
            if (contadorTuplas == 2) {
                guardarEstadoInicial();
            }
            
            if (indiceTupla < 0 || indiceTupla >= tuplas.size()) {
                break;
            }
            
            t = tuplas.get(indiceTupla);
        } while (!(t instanceof EndGame));

        System.out.println();
        System.out.println("Resumen de ejecución:");
        System.out.println();
        mostrarComparacion();
    }
    
    private void guardarEstadoInicial() {
        String[] entidades = {"jugador", "enemigo", "item"};
        for (String nombre : entidades) {
            EntitySymbol entity = (EntitySymbol) ts.resolve(nombre);
            if (entity != null) {
                Map<String, String> attrs = new HashMap<>(entity.getAttributes());
                estadoInicial.put(nombre, attrs);
            }
        }
    }
    
    private void mostrarComparacion() {
        EntitySymbol jugador = (EntitySymbol) ts.resolve("jugador");
        EntitySymbol enemigo = (EntitySymbol) ts.resolve("enemigo");
        EntitySymbol item = (EntitySymbol) ts.resolve("item");
        
        if (jugador != null && estadoInicial.containsKey("jugador")) {
            Map<String, String> ini = estadoInicial.get("jugador");
            System.out.println("JUGADOR:");
            System.out.println("  Posicion: (" + ini.get("posX") + "," + ini.get("posY") + ") -> (" + jugador.getAttributes().get("posX") + "," + jugador.getAttributes().get("posY") + ")");
            System.out.println("  Vida:     " + ini.get("vida") + " -> " + jugador.getAttributes().get("vida"));
            System.out.println("  Puntos:   " + ini.get("puntos") + " -> " + jugador.getAttributes().get("puntos"));
        }
        
        if (enemigo != null && estadoInicial.containsKey("enemigo")) {
            Map<String, String> ini = estadoInicial.get("enemigo");
            System.out.println("ENEMIGO:");
            System.out.println("  Posicion: (" + ini.get("posX") + "," + ini.get("posY") + ") -> (" + enemigo.getAttributes().get("posX") + "," + enemigo.getAttributes().get("posY") + ")");
            System.out.println("  Vida:     " + ini.get("vida") + " -> " + enemigo.getAttributes().get("vida"));
        }
        
        if (item != null && estadoInicial.containsKey("item")) {
            Map<String, String> ini = estadoInicial.get("item");
            System.out.println("ITEM:");
            System.out.println("  Posicion: (" + ini.get("posX") + "," + ini.get("posY") + ") -> (" + item.getAttributes().get("posX") + "," + item.getAttributes().get("posY") + ")");
            System.out.println("  Valor:    " + ini.get("valor") + " -> " + item.getAttributes().get("valor"));
        }
    }
}
