package ink.rainbowbridge.arathoth2.module.script.javascript;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.module.base.manager.AttributeManager;
import jdk.internal.dynalink.beans.StaticClass;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static ink.rainbowbridge.arathoth2.ArathothII.config;
import static ink.rainbowbridge.arathoth2.ArathothII.logger;

/**
 * @Author 寒雨
 * @Since 2021/4/17 14:50
 */
public class JSLoader {
    /**
     * 载入所有js属性/条件
     */
    public static void loadJavaScripts(){
        File attrFile = new File(ArathothII.getInstance().getPlugin().getDataFolder(),"JavaScript"+File.separatorChar+"Attributes");
        File condFile = new File(ArathothII.getInstance().getPlugin().getDataFolder(),"JavaScript"+File.separatorChar+"Conditions");
        if(!attrFile.exists()){ attrFile.mkdir(); ArathothII.getInstance().getPlugin().saveResource("JavaScript/Attributes/exampleattr.js",false);}
        if(!condFile.exists()){ condFile.mkdir(); }
        for (String s : attrFile.list()) {
            if (s.endsWith(".js")){
                File jsFile = new File(attrFile,s);
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
                //注入静态类依赖
                config.getConfigurationSection("JavaScript.Building").getKeys(false).forEach(str -> {
                            String path = config.getString("JavaScript.Building." + str);
                            try {
                                StaticClass staticClass = StaticClass.forClass(Class.forName(str));
                                engine.put(str,staticClass);
                            } catch (ClassNotFoundException e) {
                                logger.warn("can not found class while load javascript building: "+path);
                                e.printStackTrace();
                            }
                        }
                );
                try {
                    engine.eval(new InputStreamReader(new FileInputStream(jsFile), StandardCharsets.UTF_8));
                } catch (ScriptException | FileNotFoundException e) {
                    logger.warn("there is a exception happened while eval javascript :");
                    e.printStackTrace();
                    break;
                }
                new JavaScriptAttribute(jsFile,engine).register(ArathothII.getInstance().getPlugin());
            }
        }
        //重复代码，丢死人了
        for (String s : condFile.list()) {
            if (s.endsWith(".js")){
                File jsFile = new File(attrFile,s);
                ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
                //注入静态类依赖
                config.getConfigurationSection("JavaScript.Building").getKeys(false).forEach(str -> {
                            String path = config.getString("JavaScript.Building." + str);
                            try {
                                StaticClass staticClass = StaticClass.forClass(Class.forName(str));
                                engine.put(str,staticClass);
                            } catch (ClassNotFoundException e) {
                                logger.warn("can not found class while load javascript building: "+path);
                                e.printStackTrace();
                            }
                        }
                );
                try {
                    engine.eval(new InputStreamReader(new FileInputStream(jsFile), StandardCharsets.UTF_8));
                } catch (ScriptException | FileNotFoundException e) {
                    logger.warn("there is a exception happened while eval javascript :");
                    e.printStackTrace();
                    break;
                }
                new JavaScriptCondition(jsFile,engine).register(ArathothII.getInstance().getPlugin());
            }
        }
    }

    /**
     * 注销全部js属性/条件并重新加载
     */
    public static void reloadJs(){
        AttributeManager.attributeList.forEach(attr -> {
            if(attr instanceof JavaScriptAttribute){
                attr.unRegister();
            }
        });
        AttributeManager.conditionList.forEach(cond -> {
            if (cond instanceof JavaScriptCondition){
                cond.unRegister();
            }
        });
        loadJavaScripts();
    }
}
