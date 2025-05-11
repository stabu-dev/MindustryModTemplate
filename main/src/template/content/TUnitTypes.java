package template.content;

import arc.graphics.Color;
import arc.struct.Seq;
import mindustry.ai.types.BuilderAI;
import mindustry.content.Items;
import mindustry.gen.UnitEntity;
import mindustry.type.UnitType;
import template.annotations.Annotations.*;

public final class TUnitTypes{
    public static @EntityPoint(UnitEntity.class) UnitType discovery;

    private TUnitTypes(){
        throw new AssertionError();
    }

    public static void load(){
        discovery = new UnitType("discovery"){{
            controller = u -> new BuilderAI(true, 500f);
            outlineColor = Color.valueOf("2f2f36");
            isEnemy = hittable = false;

            lowAltitude = true;
            flying = true;
            mineSpeed = 4.5f;
            mineTier = 2;
            mineItems = Seq.with(Items.beryllium);
            buildSpeed = 0.3f;
            drag = 0.03f;
            speed = 2f;
            rotateSpeed = 13f;
            accel = 0.1f;
            itemCapacity = 20;
            health = 110f;
            engineOffset = 5f;
            hitSize = 8f;
            alwaysUnlocked = true;
        }};
    }
}