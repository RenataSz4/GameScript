import java.util.HashMap;
import java.util.Map;

public class SymbolTable implements Scope {
    Map<String, Symbol> symbols = new HashMap<String, Symbol>();

    public SymbolTable() {
        initTypeSystem();
    }

    protected void initTypeSystem() {
        define(new BuiltInTypeSymbol("entity"));
    }

    // Satisfy Scope interface
    public String getScopeName() {
        return "global";
    }

    public Scope getEnclosingScope() {
        return null;
    }

    public void define(Symbol sym) {
        symbols.put(sym.getName(), sym);
    }

    public Symbol resolve(String name) {
        return symbols.get(name);
    }

    public String toString() {
        return getScopeName() + ":" + symbols;
    }
}
