/**A generic programming language symbol*/
public class Symbol {
    String name; // All symbols at least have a name
    Type type;

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        if (type != null) return '<' + getName() + ":" + type + '>';
        return getName();
    }
}
