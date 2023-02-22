
import ECS.Component;
import ECS.Entity;
import ECS.World;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        new World(
                new ArrayList<>(List.of(
                        new Entity("Entity1", new ArrayList<>(List.of(new MortalComponent()))),
                        new Entity("Entity2", new ArrayList<>(List.of(new MortalComponent(), new MoodComponent())))
                )),
                new ArrayList<>(List.of(
                        new LifeSystem(),
                        new MoodSystem(),
                        new RenderMoodyMortalsSystem(),
                        new RenderMortals()
                ))
        ).startSystems();
    }
}


class MoodComponent extends Component {
    public boolean isHappy = false;
}

class MortalComponent extends Component {
    public long timeAlive = -1;
    public long birthTime = -1;
    public boolean isAlive = false;
    public boolean lifeStarted = false;
}

class RenderMortals extends ECS.System {
    public RenderMortals() {
        super(new ArrayList<>(List.of(MortalComponent.class)));
    }

    @Override
    public void update(Entity entity) {
        MortalComponent life = entity.getC(MortalComponent.class);

        assert life != null;

        if(!life.isAlive) {
            System.out.printf("%s is dead! :C\nGame over!", entity.name());
            this.world.gameOver = true;
        }
    }
}

class RenderMoodyMortalsSystem extends ECS.System {
    public RenderMoodyMortalsSystem() {
        super(new ArrayList<>(List.of(MoodComponent.class, MortalComponent.class)));
    }

    @Override
    public void update(Entity entity) {
        MoodComponent mood = entity.getC(MoodComponent.class);
        MortalComponent life = entity.getC(MortalComponent.class);

        assert life != null;
        assert mood != null;

        System.out.printf("%s is %s, lived for %s seconds\n", entity.name(), mood.isHappy ? "happy" : "sad", life.timeAlive);
    }
}

class MoodSystem extends  ECS.System {
    public MoodSystem() {
        super(new ArrayList<>(List.of(MoodComponent.class, MortalComponent.class)));
    }

    @Override
    public void update(Entity entity) {
        MoodComponent mood = entity.getC(MoodComponent.class);
        MortalComponent life = entity.getC(MortalComponent.class);

        assert life != null;
        assert mood != null;

        if(life.timeAlive < 5) {
            mood.isHappy = true;
            return;
        }
        if(life.timeAlive < 10) {
            mood.isHappy = false;
            return;
        }
        life.isAlive = false;

        this.world.markEntity(entity);
    }
}

class LifeSystem extends ECS.System {
    public LifeSystem() {
        super(new ArrayList<>(List.of(MortalComponent.class)));
    }

    @Override
    public void update(Entity entity) {
        MortalComponent life = entity.getC(MortalComponent.class);

        assert life != null;

        if(!life.lifeStarted) {
            life.lifeStarted = true;
            life.timeAlive = 0;
            life.isAlive = true;
            life.birthTime = Instant.now().getEpochSecond();
            return;
        }
        life.timeAlive = Instant.now().getEpochSecond() - life.birthTime;
    }
}