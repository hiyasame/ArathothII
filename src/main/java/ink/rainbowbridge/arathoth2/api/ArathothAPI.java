package ink.rainbowbridge.arathoth2.api;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseAttribute;
import ink.rainbowbridge.arathoth2.moudle.base.data.StatusData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;

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
                return ((HashMap<String, StatusData>) ArathothII.getMetadata(entity, "AttributeData", ArathothII.getInstance().getPlugin())).get(attr.getName());
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
            return ((HashMap<String, StatusData>) ArathothII.getMetadata(entity, "AttributeData", ArathothII.getInstance().getPlugin()));
        }catch (NullPointerException ignored) {
            return new HashMap<>();
        }
    }

}
