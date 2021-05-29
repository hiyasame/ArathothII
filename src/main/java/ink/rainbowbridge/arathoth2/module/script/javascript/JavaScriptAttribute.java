package ink.rainbowbridge.arathoth2.module.script.javascript;

import ink.rainbowbridge.arathoth2.module.base.abstracts.BaseAttribute;
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;

/**
 * js属性
 * @Author 寒雨
 * @Since 2021/4/11 9:11
 */
public class JavaScriptAttribute extends BaseAttribute {
    private File jsFile;
    private ScriptEngine engine;
    public JavaScriptAttribute(File jsFile, ScriptEngine engine){
        this.jsFile = jsFile;
        this.engine = engine;
    }

    @Override
    public String getName() {
        return jsFile.getName().replace(".js","");
    }

    @Override
    public boolean isPercentAttribute() {
        if (engine.get("isPercentAttribute") == null){
            return false;
        }
        return (boolean)engine.get("isPercentAttribute");
    }

    @Override
    public boolean isZeroExecute() {
        if (engine.get("isZeroExecute") == null){
            return false;
        }
        return (boolean)engine.get("isZeroExecute");
    }

    @Override
    public int getPriority() {
        return (int)engine.get("priority");
    }

    @Override
    public String getDescription() {
        if (engine.get("description") == null){ return super.getDescription(); }
        return (String)engine.get("description");
    }

    @Override
    public StatusType getType() {
        return StatusType.valueOf(((String)engine.get("type")).toUpperCase());
    }

    @Override
    public void onExecute(EventData eventData) {
        Invocable inv = (Invocable)engine;
        try {
            inv.invokeFunction("onExecute",eventData);
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
