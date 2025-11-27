public class BuiltInTypeSymbol extends Symbol implements Type {
    public BuiltInTypeSymbol(String name) {
        super(name);
    }

    public String getName() {
        return name;
    }
}
