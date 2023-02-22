package ECS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
public class World {

    public final List<Entity> entities;
    public final List<System> systems;

    public boolean gameOver = false;
    private final List<Entity> marked = new ArrayList<>();

    public World(List<Entity> entities, List<System> systems) {
        this.entities = entities;
        this.systems = systems;
    }

    public void startSystems() {

        // link systems to world state
        systems.forEach(sys -> sys.world = this);

        while (!gameOver) {
            // system iterations
            systems.forEach(sys -> {
                entities.stream()
                        .map(e -> new Tuple<>(e, e.components().stream().map(Object::getClass).toList()))
                        .filter(e -> new HashSet<>(e.v2()).containsAll(sys.requires))
                        .map(Tuple::v1).toList()
                        .forEach(sys::update);
            });

            // collect and destroy marked entities from the systems
            marked.forEach(entities::remove);
            marked.clear();
        }
    }

    public void markEntity(Entity ent) {
        marked.add(ent);
    }
}