package template.tools.proc;

import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.noise.*;

import mindustry.gen.*;
import mindustry.type.*;

import template.*;
import template.tools.*;
import template.tools.GenAtlas.*;

import java.util.concurrent.*;

import static mindustry.Vars.*;
import static template.tools.Tools.*;

/**
 * A processor to generate unit sprites such as:
 * <ul>
 *     <li> {@code -full} icons. </li>
 *     <li> Wreck regions. </li>
 * </ul>
 * @author GlennFolker
 * @author Drullkus
 * @author Anuke
 */
public class UnitProcessor implements Processor{
    @Override
    @SuppressWarnings("SuspiciousNameCombination") // sus :flushed:
    public void process(ExecutorService exec){
        content.units().each(Template::isTemplate, (UnitType type) -> submit(exec, type.name, () -> {
            init(type);
            load(type);

            float scl = Draw.scl / 4f;

            Func3<GenRegion, String, Pixmap, GenRegion> add = (relative, name, pixmap) -> {
                if(!relative.found()) throw new IllegalArgumentException("Cannot use a non-existent region as a relative point: " + relative);

                GenRegion reg = new GenRegion(name, pixmap);
                reg.relativePath = relative.relativePath;
                reg.save();
                return reg;
            };

            Unit unit = type.constructor.get();

            Pixmap compositeIcon = conv(type.region).pixmap().copy();

            if(unit instanceof Mechc){
                GraphicUtils.drawCenter(compositeIcon, conv(type.baseRegion).pixmap());
                GraphicUtils.drawCenter(compositeIcon, conv(type.legRegion).pixmap());

                Pixmap flip = conv(type.legRegion).pixmap().flipX();
                GraphicUtils.drawCenter(compositeIcon, flip);
                flip.dispose();

                compositeIcon.draw(conv(type.region).pixmap(), true);
            }

            type.weapons.sort(w -> w.layerOffset);
            for(var weapon : type.weapons){
                GenRegion reg = conv(weapon.region);
                Pixmap pix = weapon.name.isEmpty() ? null : reg.pixmap();
                Pixmap tempFlippedPix = null;

                if(pix != null && weapon.flipSprite){
                    tempFlippedPix = pix.flipX();
                    pix = tempFlippedPix;
                }

                if(pix != null){
                    compositeIcon.draw(pix,
                            (int)(weapon.x / scl + compositeIcon.width / 2f - pix.width / 2f),
                            (int)(-weapon.y / scl + compositeIcon.height / 2f - pix.height / 2f),
                            true
                    );
                }

                if(tempFlippedPix != null) tempFlippedPix.dispose();

                weapon.load();
            }

            compositeIcon.draw(conv(type.region).pixmap(), true);
            int baseColor = Color.valueOf("ffa665").rgba();

            Pixmap baseCell = conv(type.cellRegion).pixmap();
            Pixmap cell = new Pixmap(type.cellRegion.width, type.cellRegion.height);
            cell.each((x, y) -> cell.setRaw(x, y, Color.muli(baseCell.getRaw(x, y), baseColor)));

            compositeIcon.draw(cell, compositeIcon.width / 2 - cell.width / 2, compositeIcon.height / 2 - cell.height / 2, true);

            for(var weapon : type.weapons){
                if(weapon.layerOffset < 0f) continue;

                Pixmap pix = weapon.name.isEmpty() ? null : conv(weapon.region).pixmap();
                Pixmap tempFlippedPix = null;

                if(pix != null && weapon.flipSprite){
                    tempFlippedPix = pix.flipX();
                    pix = tempFlippedPix;
                }

                if(pix != null){
                    compositeIcon.draw(pix,
                            (int)(weapon.x / scl + compositeIcon.width / 2f - pix.width / 2f),
                            (int)(-weapon.y / scl + compositeIcon.height / 2f - pix.height / 2f),
                            true
                    );
                }

                if(tempFlippedPix != null) tempFlippedPix.dispose();
            }

            Pixmap fullIcon = Pixmaps.outline(new PixmapRegion(compositeIcon), type.outlineColor, type.outlineRadius);
            fullIcon.draw(compositeIcon, true);

            GenRegion baseReg = conv(type.region);
            add.get(baseReg, type.name + "-full", fullIcon);

            if(!baseReg.found()) throw new IllegalArgumentException("Cannot use a non-existent region as a relative point: " + baseReg);

            Rand rand = new Rand();
            rand.setSeed(type.name.hashCode());

            int splits = 3;
            float degrees = rand.random(360f);
            float offsetRange = Math.max(fullIcon.width, fullIcon.height) * 0.15f;
            Vec2 offset = new Vec2(1, 1).rotate(rand.random(360f)).setLength(rand.random(0, offsetRange)).add(fullIcon.width / 2f, fullIcon.height / 2f);

            Pixmap[] wrecks = new Pixmap[splits];
            for(int i = 0; i < wrecks.length; i++){
                wrecks[i] = new Pixmap(fullIcon.width, fullIcon.height);
            }

            VoronoiNoise voronoi = new VoronoiNoise(type.id, true);
            fullIcon.each((x, y) -> {
                if(voronoi.noise(x, y, 1f / (14f + fullIcon.width/40f)) <= 0.47d){
                    boolean rValue = Math.max(Ridged.noise2d(1, x, y, 3, 1f / (20f + fullIcon.width / 8f)), 0) > 0.16f;

                    float dst =  offset.dst(x, y);
                    float noise = (float)Noise.rawNoise(dst / (9f + fullIcon.width / 70f)) * (60 + fullIcon.width / 30f);
                    wrecks[(int)Mathf.clamp(Mathf.mod(offset.angleTo(x, y) + noise + degrees, 360f) / 360f * splits, 0, splits - 1)].setRaw(x, y, Color.muli(fullIcon.getRaw(x, y), rValue ? 0.7f : 1f));
                }
            });

            String wreckPath = "rubble/";
            for(int i = 0; i < wrecks.length; i++){
                GenRegion reg = new GenRegion(type.name + "-wreck" + i, wrecks[i]);
                reg.relativePath = wreckPath;
                reg.save();
                wrecks[i].dispose();
            }

            compositeIcon.dispose();
            cell.dispose();
            fullIcon.dispose();
        }));
    }
}