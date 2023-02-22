package ECS;

import java.util.List;

public abstract class System {
    public List<Class<? extends Component>> requires;
    public World world;
    public System(List<Class<? extends  Component>> requires) {
        this.requires = requires;
    }

    public abstract void update(Entity entity);
}
