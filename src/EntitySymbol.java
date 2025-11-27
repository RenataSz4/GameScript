import java.util.HashMap;
import java.util.Map;

/**Represents an entity definition (name, attributes) in symbol table*/
public class EntitySymbol extends Symbol {
    Map<String, String> attributes = new HashMap<String, String>();

    public EntitySymbol(String name, Type type) {
        super(name, type);
    }

    public void defineAttribute(String attrName, String value) {
        attributes.put(attrName, value);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public String toString() {
        return '<' + getName() + ":" + type.getName() + ", attributes=" + attributes + '>';
    }
}
