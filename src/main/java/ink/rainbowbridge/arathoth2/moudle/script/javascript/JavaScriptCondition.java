package ink.rainbowbridge.arathoth2.moudle.script.javascript;

import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseCondition;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.util.List;

/**
 * @Author 寒雨
 * @Since 2021/4/11 10:01
 */
public class JavaScriptCondition extends BaseCondition {
    private File jsFile;
    private ScriptEngine engine;
    public JavaScriptCondition(File jsFile, ScriptEngine engine){
        this.jsFile = jsFile;
        this.engine = engine;
    }
    @Override
    public boolean onPass(ItemStack item, Player p, List<String> values) {
        try {
            return (boolean) ((Invocable)engine).invokeFunction("onPass",item,p,values);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String getName() {
        return jsFile.getName().replace(".js","");
    }


    @Override
    public int getPriority() {
        return (int)engine.get("priority");
    }

}
