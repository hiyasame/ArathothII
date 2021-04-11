package ink.rainbowbridge.arathoth2.moudle.base.interfaces;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseAttribute;
import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseCondition;
import ink.rainbowbridge.arathoth2.utils.FileUtils;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;


import java.util.HashMap;
import java.util.List;

/**
 * @Author 寒雨
 * @Since 2021/4/11 0:41
 */
public class ToActiveHandler {
    private static HashMap<Plugin,BaseAttribute> attrInstances = new HashMap<>();
    private static HashMap<Plugin, BaseCondition> condInstances = new HashMap<>();

    /**
     * 为所有插件注册使用 @ToActive 标注的属性/条件实例
     */
    public static void setup(){
        for(Plugin plugin : Bukkit.getPluginManager().getPlugins()){
            try{
                setupPlugin(plugin);
            }catch(Throwable t){
                t.printStackTrace();
            }
        }
    }

    /**
     * 读取插件中所有class中的toActive注解并且将上了注解的属性/条件new instance后放入map待用
     * 只支持依赖于ArathothII的插件使用
     * @param pl 插件
     */
    public static void setupPlugin(Plugin pl){
        if(ArathothII.isDependArathothII(pl) || pl.getName().equals("ArathothII")){
            List<Class> classes = FileUtils.getClasses(pl);
            for (Class<?> pluginClass : classes) {
                if (BaseAttribute.class.isAssignableFrom(pluginClass) && pluginClass.isAnnotationPresent(ToActive.class)) {
                    try {
                        BaseAttribute attr = pl.getClass().equals(pluginClass) ? (BaseAttribute) pl : (BaseAttribute) pluginClass.newInstance();
                        attrInstances.put(pl,attr);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (BaseCondition.class.isAssignableFrom(pluginClass) && pluginClass.isAnnotationPresent(ToActive.class)) {
                    try {
                        BaseCondition cond = pl.getClass().equals(pluginClass) ? (BaseCondition) pl : (BaseCondition) pluginClass.newInstance();
                        condInstances.put(pl,cond);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 注册所有使用 @ToActive 注册的属性/条件
     */
    public static void registerAll(){
        for(var pl : attrInstances.keySet()){
            attrInstances.get(pl).register(pl);
        }
        for (var pl : condInstances.keySet()){
            condInstances.get(pl).register(pl);
        }
    }
}
