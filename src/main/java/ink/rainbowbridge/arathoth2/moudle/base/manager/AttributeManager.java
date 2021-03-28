package ink.rainbowbridge.arathoth2.moudle.base.manager;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseAttribute;
import ink.rainbowbridge.arathoth2.moudle.base.data.StatusData;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.nms.NMS;
import io.izzel.taboolib.module.nms.nbt.NBTCompound;
import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;

/**
 * @Author 寒雨
 * @Since 2021/3/7 10:31
 */
public class AttributeManager {
    @Getter
    public static File attributeConfigDir = new File(ArathothII.getInstance().getPlugin().getDataFolder(), "attributes");
    @Getter
    public static File conditionConfigDir = new File(ArathothII.getInstance().getPlugin().getDataFolder(), "conditions");
    @Getter
    public static File itemFeatureConfigDir = new File(ArathothII.getInstance().getPlugin().getDataFolder(), "ItemFeatures");
    public static List<BaseAttribute> attributeList = Collections.synchronizedList(new ArrayList<>());
    public static HashMap<Integer, String> slotsMap = new HashMap<>();
    public static String mainHandKey = null;

    /**
     * 读取配置文件中的注册槽位
     */
    public static void loadSlots() {
        for (String i : ArathothII.config.getConfigurationSection("Slots.Register").getKeys(false)) {
            slotsMap.put(Integer.parseInt(i), ArathothII.config.getString("Slots.Register." + i));
            TLocale.sendToConsole("Plugin.SLOT_REGISTER", i, ArathothII.config.getString("Slots.Register." + i));
        }
        mainHandKey = ArathothII.config.getString("Slots.MainHand");
        TLocale.sendToConsole("Plugin.SLOT_REGISTER", "MainHand", mainHandKey);
    }

    /**
     * 读取nbtData
     *
     * @param items  物品
     * @param attr   属性
     * @param data   data
     * @param entity 生物
     */
    public static void loadNBTAttribute(List<ItemStack> items, BaseAttribute attr, StatusData data, LivingEntity entity) {
        for (ItemStack item : items) {
            NBTCompound compound = new NBTCompound();
            compound.put("min", 0.0D);
            compound.put("max", 0.0D);
            compound.put("percent", 0.0D);
            data.setMin(data.getMin() + NMS.handle().loadNBT(item).getOrDefault("Arathoth", new NBTCompound()).asCompound().getOrDefault("Attributes", new NBTCompound()).asCompound().getOrDefault(attr.getName(), compound).asCompound().get("min").asDouble());
            data.setMax(data.getMax() + NMS.handle().loadNBT(item).getOrDefault("Arathoth", new NBTCompound()).asCompound().getOrDefault("Attributes", new NBTCompound()).asCompound().getOrDefault(attr.getName(), compound).asCompound().get("max").asDouble());
            data.setPercent(data.getPercent() + NMS.handle().loadNBT(item).getOrDefault("Arathoth", new NBTCompound()).asCompound().getOrDefault("Attributes", new NBTCompound()).asCompound().getOrDefault(attr.getName(), compound).asCompound().get("percent").asDouble());
        }
    }

    public static void updatePlayer(Player p,ItemStack item){
        List<ItemStack> items = new ArrayList<>();
        items.
    }
}