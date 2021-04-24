package ink.rainbowbridge.arathoth2.moudle.base.manager;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseAttribute;
import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseCondition;
import ink.rainbowbridge.arathoth2.moudle.base.data.StatusData;
import ink.rainbowbridge.arathoth2.moudle.base.events.ArathothStatusUpdateEvent;
import ink.rainbowbridge.arathoth2.utils.ItemUtils;
import ink.rainbowbridge.arathoth2.utils.KetherUtils;
import ink.rainbowbridge.arathoth2.utils.NameUtils;
import io.izzel.taboolib.cronus.CronusUtils;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.nms.NMS;
import io.izzel.taboolib.module.nms.nbt.NBTCompound;
import io.izzel.taboolib.util.item.Items;
import lombok.Getter;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Author 寒雨
 * @Since 2021/3/7 10:31
 */
public class AttributeManager {
    @Getter
    public static File attributeConfigDir = new File(ArathothII.getInstance().getPlugin().getDataFolder(), "attributes");
    @Getter
    public static File conditionConfigDir = new File(ArathothII.getInstance().getPlugin().getDataFolder(), "conditions");
    public static List<BaseAttribute> attributeList = Collections.synchronizedList(new ArrayList<>());
    public static List<BaseCondition> conditionList = Collections.synchronizedList(new ArrayList<>());
    public static ConcurrentHashMap<String,BaseAttribute> attributeMap = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String,BaseCondition> conditionMap = new ConcurrentHashMap<>();
    public static HashMap<Integer, String> slotsMap = new HashMap<>();
    public static String mainHandKey = null;

    /**
     * 读取配置文件中的注册槽位
     */
    public static void loadSlots() {
        slotsMap.clear();
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
     */
    public static StatusData loadNBTAttribute(List<ItemStack> items, BaseAttribute attr, StatusData data) {
        for (ItemStack item : items) {
            NBTCompound compound = new NBTCompound();
            compound.put("min", 0.0D);
            compound.put("max", 0.0D);
            compound.put("percent", 0.0D);
            data.setMin(data.getMin() + NMS.handle().loadNBT(item).getOrDefault("Arathoth", new NBTCompound()).asCompound().getOrDefault("Attributes", new NBTCompound()).asCompound().getOrDefault(attr.getName(), compound).asCompound().get("min").asDouble());
            data.setMax(data.getMax() + NMS.handle().loadNBT(item).getOrDefault("Arathoth", new NBTCompound()).asCompound().getOrDefault("Attributes", new NBTCompound()).asCompound().getOrDefault(attr.getName(), compound).asCompound().get("max").asDouble());
            data.setPercent(data.getPercent() + NMS.handle().loadNBT(item).getOrDefault("Arathoth", new NBTCompound()).asCompound().getOrDefault("Attributes", new NBTCompound()).asCompound().getOrDefault(attr.getName(), compound).asCompound().get("percent").asDouble());
        }
        return data;
    }

    /**
     * 更新玩家属性数据
     * 支持PlayerSwapItem
     * @param p 对象玩家
     * @param item 主手物品
     */
    public static void updatePlayer(Player p,ItemStack item){
        long time = System.currentTimeMillis();
       Future<HashMap<String,StatusData>> future = Bukkit.getScheduler().callSyncMethod(ArathothII.getInstance().getPlugin(), ()->{
        List<ItemStack> items = new ArrayList<>();
        HashMap<String,StatusData> dataMap = new HashMap<>();
        if(ItemUtils.isType(item,mainHandKey)){
            boolean pass = true;
            for (var cond: conditionList) {
                if (!cond.pass(item,p)){
                    pass = false;
                    KetherUtils.eval(p,cond.getKetherScripts().stream().map(s -> s.replace("[item]", NameUtils.getItemName(p,item))).collect(Collectors.toList()));
                    break;
                }
            }
            if(pass) {
                items.add(item);
            }else{
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        moveOutItem(p,p.getInventory().getHeldItemSlot());
                    }
                }.runTask(ArathothII.getInstance().getPlugin());
            }
        }
        slotsMap.keySet().forEach((i)->{
            if (ItemUtils.isType(p.getInventory().getItem(i),slotsMap.get(i))){
                boolean pass = true;
                for (var cond: conditionList) {
                    if (!cond.pass(p.getInventory().getItem(i),p)){
                        pass = false;
                        KetherUtils.eval(p,cond.getKetherScripts().stream().map(s -> s.replace("[item]", NameUtils.getItemName(p,item))).collect(Collectors.toList()));
                        break;
                    }
                }
                if(pass) {
                    items.add(p.getInventory().getItem(i));
                }else{
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            moveOutItem(p,i);
                        }
                    }.runTask(ArathothII.getInstance().getPlugin());
                }
            }
        });
        attributeList.forEach(attr->{
          var data = attr.parseValue(p,items);
          dataMap.put(attr.getName(),data);
        });
        return dataMap;
        });
        try {
            var map = future.get();
            var eve = new ArathothStatusUpdateEvent(p,map);
            Bukkit.getPluginManager().callEvent(eve);
            p.setMetadata("StatusData",new FixedMetadataValue(ArathothII.getInstance().getPlugin(), eve.getData()));
            ArathothII.debug(3,TLocale.asString("Debug.ATTR_UPDATE",p.getName(),(System.currentTimeMillis()-time)));
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 为生物更新属性
     * @param e 目标生物
     */
    public static void updateEntity(LivingEntity e){
        long time = System.currentTimeMillis();
        Future<HashMap<String,StatusData>> future = Bukkit.getScheduler().callSyncMethod(ArathothII.getInstance().getPlugin(), ()-> {
            var items = new ArrayList<ItemStack>();
            var dataMap = new HashMap<String, StatusData>();
            if (ItemUtils.isApproveItem(e.getEquipment().getHelmet())) {
                items.add(e.getEquipment().getHelmet());
            }
            if (ItemUtils.isApproveItem(e.getEquipment().getChestplate())) {
                items.add(e.getEquipment().getChestplate());
            }
            if (ItemUtils.isApproveItem(e.getEquipment().getLeggings())) {
                items.add(e.getEquipment().getLeggings());
            }
            if (ItemUtils.isApproveItem(e.getEquipment().getBoots())) {
                items.add(e.getEquipment().getBoots());
            }
            if (ItemUtils.isApproveItem(e.getEquipment().getItemInMainHand())) {
                items.add(e.getEquipment().getItemInMainHand());
            }
            if (ItemUtils.isApproveItem(e.getEquipment().getItemInOffHand())) {
                items.add(e.getEquipment().getItemInOffHand());
            }
            attributeList.forEach((attr) -> dataMap.put(attr.getName(), attr.parseValue(e, items)));
            return dataMap;
        });
        try {
            var map = future.get();
            var eve = new ArathothStatusUpdateEvent(e,map);
            Bukkit.getPluginManager().callEvent(eve);
            e.setMetadata("StatusData",new FixedMetadataValue(ArathothII.getInstance().getPlugin(), eve.getData()));
            ArathothII.debug(3,TLocale.asString("Debug.ATTR_UPDATE",e.getName(),(System.currentTimeMillis()-time)));
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
    }

    public static void moveOutItem(Player p , Integer slot){
        ItemStack item = p.getInventory().getItem(slot).clone();
        p.getInventory().setItem(slot, new ItemStack(Material.STONE));
        CronusUtils.addItem(p,item);
        p.getInventory().setItem(slot,new ItemStack(Material.AIR));
    }
}