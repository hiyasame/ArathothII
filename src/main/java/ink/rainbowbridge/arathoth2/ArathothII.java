package ink.rainbowbridge.arathoth2;

import ink.rainbowbridge.arathoth2.api.ArathothAPI;
import ink.rainbowbridge.arathoth2.bstats.Metrics;
import ink.rainbowbridge.arathoth2.module.base.abstracts.BaseAttribute;
import ink.rainbowbridge.arathoth2.module.base.abstracts.BaseCondition;
import ink.rainbowbridge.arathoth2.module.base.manager.AttributeManager;
import ink.rainbowbridge.arathoth2.module.hook.PlaceHolderAPIHook;
import ink.rainbowbridge.arathoth2.module.script.javascript.JSLoader;
import io.izzel.taboolib.loader.Plugin;
import io.izzel.taboolib.module.config.TConfig;
import io.izzel.taboolib.module.inject.TInject;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.locale.logger.TLogger;
import lombok.Getter;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * @Author 寒雨
 * @Since 2021/3/7 10:06
 */
public class ArathothII extends Plugin {
    @TInject(value = "config.yml",locale = "Options.language",migrate = true)
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
        AttributeManager.loadSlots();
        //为ArathothII本体注册 @ToActive 注解标注的属性/条件
        ArathothAPI.registerToActive(getPlugin());
        Metrics metrics = new Metrics(this.getPlugin(), 9838);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
        boolean success = new PlaceHolderAPIHook().register();
        if (success) {
            TLocale.sendToConsole("PAPI_REGISTER_SUCCESS");
        } else {
            TLocale.sendToConsole("PAPI_REGISTER_FAIL");
        }
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

    /**
     * 插件是否依赖于 ArathothII（依赖或软兼容）
     *
     * @param plugin 插件
     * @return boolean
     */
    public static boolean isDependArathothII(org.bukkit.plugin.Plugin plugin) {
        return plugin.getDescription().getDepend().contains("ArathothII") || plugin.getDescription().getSoftDepend().contains("ArathothII");
    }

    public static void loadPlugin(){
        if (!AttributeManager.getAttributeConfigDir().exists()){
            AttributeManager.getAttributeConfigDir().mkdir();
        }
        if (!AttributeManager.getConditionConfigDir().exists()){
            AttributeManager.getConditionConfigDir().mkdir();
        }
        File dataDir = new File(getInstance().getPlugin().getDataFolder(),"Data");
        if(!dataDir.exists()){
            dataDir.mkdir();
        }
        AttributeManager.loadSlots();
        JSLoader.reloadJs();
        AttributeManager.attributeList.forEach(BaseAttribute::load);
        AttributeManager.conditionList.forEach(BaseCondition::load);
    }

}
