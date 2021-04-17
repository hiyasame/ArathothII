package ink.rainbowbridge.arathoth2.api;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseAttribute;
import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseCondition;
import ink.rainbowbridge.arathoth2.moudle.base.data.StatusData;
import ink.rainbowbridge.arathoth2.moudle.base.interfaces.ToActiveHandler;
import ink.rainbowbridge.arathoth2.moudle.base.manager.AttributeManager;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author 寒雨
 * @Since 2021/3/7 10:16
 */
public class ArathothAPI {
    /**
     * 取随机数方法
     * 继承自Arathoth-Zero
     *
     * @param value1 primary
     * @param value2 regular
     * @return 浮动值
     */
    public static Double getRandom(Double value1, Double value2) {
        Double random = Math.floor(value1 + ArathothII.random.nextDouble() * (value2 - value1));
        return random;
    }
    /**
     * 概率判断方法
     *
     * @param rate 概率
     * @return 是否通过
     */
    public static boolean Chance(Double rate){
        if (ArathothII.random.nextDouble() < (rate/100)){
            return true;
        }
        return false;
    }

    /**
     * 获取 抛射物/生物 指定属性data
     * 之后需要做1.16版本兼容的改进
     * @param entity 实体
     * @param attr 属性
     * @return data,没有则返回一个空data;
     */
    public static StatusData getEntityAttrData(Entity entity, BaseAttribute attr){
            try {
                return ((HashMap<String, StatusData>) ArathothII.getMetadata(entity, "StatusData", ArathothII.getInstance().getPlugin())).get(attr.getName());
            }catch (NullPointerException ignored) {
                return new StatusData();
            }
    }

    /**
     * 为弓箭设置元数据data
     *
     * @param e 弓箭
     * @param data 属性data
     */
    public static void setArrowData(Entity e, HashMap<String,StatusData> data){
        e.setMetadata("AttributeData",new FixedMetadataValue(ArathothII.getInstance().getPlugin(),data));
    }

    public static HashMap<String, StatusData> getEntityAttrDataMap(Entity entity){
        try {
            return ((HashMap<String, StatusData>) ArathothII.getMetadata(entity, "StatusData", ArathothII.getInstance().getPlugin()));
        }catch (NullPointerException ignored) {
            return new HashMap<>();
        }
    }

    /**
     * 获取属性实例list，顺序按照优先级排列
     * @return list
     */
    public static List<BaseAttribute> getAttributeInstances(){
        return AttributeManager.attributeList;
    }

    /**
     * 获取条件实例list
     * @return list
     */
    public static List<BaseCondition> getConditionInstances(){
        return AttributeManager.conditionList;
    }

    /**
     * 获得属性map，获取属性实例的话直接从这里面get就好
     * @return map
     */
    public static ConcurrentHashMap<String, BaseAttribute> getAttributeMap(){
        return AttributeManager.attributeMap;
    }

    public static ConcurrentHashMap<String, BaseCondition> getConditionMap(){
        return AttributeManager.conditionMap;
    }

    /**
     * 注册插件中所有使用@ToActive快捷注册的属性/条件
     * @param plugin 插件
     */
    public static void registerToActive(Plugin plugin){
        ToActiveHandler.setupPlugin(plugin);
        ToActiveHandler.register(plugin);
    }

    /**
     * 注销某个插件的全部属性
     * @param plugin 插件
     */
    public static void unRegisterAll(Plugin plugin){
        AttributeManager.attributeList.forEach(attr -> {
            if (attr.getPlugin() == plugin){
                attr.unRegister();
            }
        });
        AttributeManager.conditionList.forEach(cond -> {
            if (cond.getPlugin() == plugin){
                cond.unRegister();
            }
        });
    }
}
