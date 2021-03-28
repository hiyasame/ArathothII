package ink.rainbowbridge.arathoth2;

import ink.rainbowbridge.arathoth2.moudle.base.manager.AttributeManager;
import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.logger.TLogger;
import lombok.Getter;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * @Author 寒雨
 * @Since 2021/3/7 10:06
 */
public class ArathothII extends Plugin {
    @TInject(value = "config.yml",locale = "Options.language")
    public static TConfig config = null;
    @Getter
    public static TLogger logger = TLogger.getUnformatted("ArathothII");
    public static DecimalFormat DecimalFormat = new DecimalFormat("0.0");
    public static Random random = new Random();

    @Override
    public void onEnable() {
        if (!AttributeManager.getAttributeConfigDir().exists()){
            AttributeManager.getAttributeConfigDir().mkdir();
        }
        if (!AttributeManager.getConditionConfigDir().exists()){
            AttributeManager.getConditionConfigDir().mkdir();
        }
        if (!AttributeManager.getItemFeatureConfigDir().exists()){
            AttributeManager.getItemFeatureConfigDir().mkdir();
        }
        AttributeManager.loadSlots();
    }

    public static Object getMetadata(Metadatable object, String key, org.bukkit.plugin.Plugin plugin) {
        List<MetadataValue> values = object.getMetadata(key);
        for (MetadataValue value : values) {
            // Plugins are singleton objects, so using == is safe here
            if (value.getOwningPlugin() == plugin) {
                return value.value();
            }
        }
        return null;
    }

    public static int getDebugLevel(){
        return config.getInt("Options.DebugLevel",0);
    }

    /**
     * debug方法
     * @param level 等级
     * @param message 消息
     */
    public static void debug(int level,String message){
        if (level < getDebugLevel()){
            getLogger().info("&7&l[&8&lDEBUG: &f"+level+"&7&l] "+message);
        }
    }
}
