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
 * 支持热加载依赖时读入属性
 * @Author 寒雨
 * @Since 2021/4/11 0:41
 */
public class ToActiveHandler {
    private static HashMap<Plugin,List<BaseAttribute>> attrInstances = new HashMap<>();
    private static HashMap<Plugin, List<BaseCondition>> condInstances = new HashMap<>();


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
                       var list = attrInstances.get(pl);
                       list.add(attr);
                       attrInstances.put(pl,list);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (BaseCondition.class.isAssignableFrom(pluginClass) && pluginClass.isAnnotationPresent(ToActive.class)) {
                    try {
                        BaseCondition cond = pl.getClass().equals(pluginClass) ? (BaseCondition) pl : (BaseCondition) pluginClass.newInstance();
                        var list = condInstances.get(pl);
                        list.add(cond);
                        condInstances.put(pl,list);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void register(Plugin plugin){
        attrInstances.get(plugin).forEach(attr -> attr.register(plugin));
        condInstances.get(plugin).forEach(cond -> cond.register(plugin));
    }
}
