package ECS;

import java.util.List;

public record Entity(String name, List<Component> components) {
    public <T extends Component>T getC(Class<T> cClass) {
        return components
                .stream()
                .filter(c ->cClass.isAssignableFrom(c.getClass()))
                .findFirst()
                .map(cClass::cast)
                .orElse(null);
    }
}
