package template;

import arc.*;
import arc.util.*;

import mindustry.game.EventType.*;
import mindustry.mod.*;

import mindustry.ui.dialogs.*;
import template.annotations.Annotations.*;
import template.gen.*;

import static mindustry.Vars.*;

/**
 * The mod's main mod class. Contains static references to other modules.
 * @author Avant Team
 */
@LoadRegs("error") // Need this temporarily, so the class gets generated.
public class Template extends Mod{
    /** Whether the mod is in an asset-processing context. */
    public static boolean tools = false;

    /** Default constructor for Mindustry mod loader to instantiate. */
    public Template(){
        this(false);
    }

    /**
     * Constructs the mod, and binds some functionality to the game under certain circumstances.
     * @param tools Whether the mod is in an asset-processing context.
     */
    public Template(boolean tools){
        Template.tools = tools;

        if(!headless){
            // Load assets once they're added into `Vars.tree`.
            Events.on(FileTreeInitEvent.class, e -> Core.app.post(TemplateSounds::load));

            Events.on(ClientLoadEvent.class, e -> {
                //show dialog upon startup
                Time.runTask(10f, () -> {
                    BaseDialog dialog = new BaseDialog("A Cat");
                    dialog.cont.add("BEHOLD!").row();
                    //mod sprites are prefixed with the mod name (this mod is called 'example' in its config)
                    dialog.cont.image(Core.atlas.find("template-cat")).pad(20f).row();
                    dialog.cont.button("Cool, I ques?", dialog::hide).size(100f, 50f);
                    dialog.show();
                });
            });

        }

        Events.on(ContentInitEvent.class, e -> {
            if(!headless){
                Regions.load();
            }
        });
    }

    @Override
    public void init(){
    }

    @Override
    public void loadContent(){
        //below has to be done after all things are loaded.
        TemplateEntityMapping.init();
    }
}
