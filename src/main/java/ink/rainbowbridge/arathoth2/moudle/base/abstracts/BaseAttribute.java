package ink.rainbowbridge.arathoth2.moudle.base.abstracts;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.api.ArathothAPI;
import ink.rainbowbridge.arathoth2.moudle.base.data.EventData;
import ink.rainbowbridge.arathoth2.moudle.base.data.StatusData;
import ink.rainbowbridge.arathoth2.moudle.base.enums.PlaceHolderType;
import ink.rainbowbridge.arathoth2.moudle.base.enums.StatusType;
import ink.rainbowbridge.arathoth2.moudle.base.events.ArathothStatusExecuteEvent;
import ink.rainbowbridge.arathoth2.moudle.base.manager.AttributeManager;
import ink.rainbowbridge.arathoth2.utils.ItemUtils;
import io.izzel.taboolib.module.locale.TLocale;
import lombok.Getter;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 属性抽象类
 * @Author 寒雨
 * @Since 2021/3/7 10:29
 */
public abstract class BaseAttribute {
    @Getter
    private Plugin plugin;
    @Getter
    private FileConfiguration config;
    private final BaseAttribute instance = this;
    public final void register(Plugin plugin){
        load();
        //优先级排序操作
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (!AttributeManager.attributeList.contains(instance)) {
                        for (BaseAttribute attr : AttributeManager.attributeList) {
                            if (attr.getPriority() <= instance.getPriority()) {
                                int index = AttributeManager.attributeList.indexOf(attr);
                                AttributeManager.attributeList.add(index, instance);
                                break;
                            }
                        }
                        TLocale.sendToConsole("Plugin.ATTR_REGISTER", getName(), plugin.getName(), getPriority());
                    }
                }catch (Throwable ex){
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(ArathothII.getInstance().getPlugin());
        onRegistering();
    }

    /**
     * 获取属性名，默认返回属性class名
     * @return name
     */
    public String getName(){
        return this.getClass().getSimpleName();
    }

    /**
     * 获取优先级，默认返回属性config中的Priority
     * @return 优先级
     */
    public int getPriority(){
        return config.getInt(getName()+".Priority");
    }

    /**
     * 通过重写该方法来操作默认config
     * @param config 操作前
     * @return 操作后
     */
    public FileConfiguration setDefaultConfig(FileConfiguration config){
        return config;
    }

    /**
     * 在属性注册时执行的方法
     */
    public void onRegistering(){
    }

    /**
     * 获取展示名，在buff之类的模块可能用到就是了
     * @return name
     */
    public String getDisplayName(){
        return config.getString(getName()+".DisplayName");
    }

    /**
     * 如果为是，获取的placeholder会多一个%
     * @return 默认false
     */
    public boolean isPercentAttribute(){
        return false;
    }

    /**
     * 是否在属性值为0时仍然运行属性，默认true
     * 在部分属性关闭它有助于提升性能
     * 不过请注意，关闭了它之后开发者监听ArathothStatusExecuteEvent在某些情况不会生效
     * 自行体会 :)
     * @return boolean
     */
    public boolean isZeroExecute(){return true;}

    /**
     * 是否在执行属性前修正负值
     * 可以杜绝一些鬼畜的属性玩法
     * @return 默认false
     */
    public boolean isFixValue(){return false;}

    /**
     * 属性类别
     * @return type
     */
    public abstract StatusType getType();

    public boolean isEnable(){
       return config.getBoolean(getName()+".Enable",true);
    }

    /**
     * 执行属性抽象方法
     * @param eventData data
     */
    public abstract void onExecute(EventData eventData);

    /**
     * 执行属性运算方法
     * @param eventData data
     */
    public void execute(EventData eventData){
        if (eventData.getType() == getType()){
            var eve = new ArathothStatusExecuteEvent(eventData.getExecutor(),this,eventData.getExecutorData(),eventData);
            Bukkit.getPluginManager().callEvent(eve);
            if (eve.isCancelled() || !isEnable()){
                return;
            }
            if (eventData.getExecutorData().isZeroData() && !isZeroExecute()){
                return;
            }
            if(isFixValue()){eventData.getExecutorData().FixZeroValue();}
            onExecute(eventData);
            ArathothII.debug(3,TLocale.asString("Debug.ATTR_EXECUTE",getName(),eventData.getExecutorData().getMin(),eventData.getExecutorData().getMax(),eventData.getExecutorData().getPercent(),eventData.getExecutor().getName()));
        }
    }

    /**
     * 载入属性配置方法
     */
    public void load(){
        new BukkitRunnable() {
            @Override
            public void run() {
                File file = new File(AttributeManager.getAttributeConfigDir(),getName()+".yml");
                if (!file.exists()) {
                    FileWriter fw = null;
                    PrintWriter out = null;
                    try {
                        fw = new FileWriter(file);
                        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)));
                        out.write("# ArathothII Attributes Configuration\n");
                        out.write("# @Author Freeze003(寒雨)\n");
                        out.write(getName() + ":\n");
                        out.write(" Enable: true\n");
                        out.write(" Priority: 0\n");
                        out.write(" DisplayName: '"+getName()+"'\n");
                        out.write(" Patterns:\n");
                        out.write(" - '[VALUE] " + getName() + "'");
                        out.flush();
                        out.close();
                        fw.close();
                        FileConfiguration config1 = YamlConfiguration.loadConfiguration(file);
                        FileConfiguration edited = setDefaultConfig(config1);
                        edited.save(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                instance.config = YamlConfiguration.loadConfiguration(file);
            }
        }.runTask(ArathothII.getInstance().getPlugin());
    }

    /**
     * 获取指定玩家的属性papi变量
     * @param type type
     * @param player 玩家
     * @return papi
     */
    public String getPlaceHolder(PlaceHolderType type, Player player){
        String papi = null;
        var data = ArathothAPI.getEntityAttrData(player,this);
        papi = data.getPlaceHolder(type);
        if(isPercentAttribute()){
            papi += "%";
        }
        return papi;
    }

    /**
     * 返回属性描述，默认无描述
     * @return str
     */
    public String getDescription(){
        return TLocale.asString("Plugin.ATTR_DEFAULTDESCRIPTION");
    }

    /**
     * 获取pattern
     * @return pattern
     */
    public List<String> getPatterns(){
        return getConfig().getStringList(getName()+".Patterns");
    }

    /**
     * lore匹配属性方法，可以选择重写
     * @param uncoloredLore lore
     * @return data
     */
    public StatusData onParseValue(List<String> uncoloredLore){
        StatusData data = new StatusData();
        for (String str : getPatterns()) {
            Pattern min = Pattern.compile(str.replace("[VALUE]", "((?:\\-|\\+)?(\\d+(?:\\.\\d+)?))"));
            Pattern max = Pattern.compile(str.replace("[VALUE]", "((?:\\-|\\+)?(\\d+(?:\\.\\d+)?))(\\-)(\\d+(?:\\.\\d+)?)"));
            Pattern pct = Pattern.compile(str.replace("[VALUE]", "((?:\\-|\\+)?(\\d+(?:\\.\\d+)?))\\%"));
            for (String lore : uncoloredLore) {
                Matcher m1 = min.matcher(lore);
                Matcher m2 = max.matcher(lore);
                Matcher m3 = pct.matcher(lore);
                if (m2.find()) {
                    data.setMin(data.getMin() + Double.valueOf(m2.group(1)));
                    data.setMax(data.getMax() + Double.valueOf(m2.group(4)));
                } else {
                    if (m1.find()) {
                        data.setMin(data.getMin() + Double.valueOf(m1.group(1)));
                        data.setMax(data.getMax() + Double.valueOf(m1.group(1)));
                    }
                }
                if (m3.find()) {
                    data.setPercent(data.getPercent() + Double.valueOf(m3.group(1)));
                }
            }
        }
        return data;
    }

    /**
     * 读取玩家data
     * @param target 目标
     * @return data
     */
    public final StatusData parseValue(LivingEntity target) {
            var data = new StatusData();
            if (target instanceof Player) {
                var p = (Player) target;
                List<ItemStack> items = new ArrayList<>();
                for (Integer i : AttributeManager.slotsMap.keySet()) {
                    if (ItemUtils.isApproveItem(p.getInventory().getItem(i))) {
                        if (ItemUtils.isType(p.getInventory().getItem(i), AttributeManager.slotsMap.get(i))) {
                            items.add(p.getInventory().getItem(i));
                        }
                    }
                }
                if (ItemUtils.isApproveItem(p.getInventory().getItemInMainHand())) {
                    if (ItemUtils.isType(p.getInventory().getItemInMainHand(), AttributeManager.mainHandKey)) {
                        items.add(p.getInventory().getItemInMainHand());
                    }
                }
                List<String> lores = new ArrayList<>();
                items.forEach(s -> {
                    lores.addAll(s.getItemMeta().getLore());
                });
                lores.forEach(s -> {
                    s = ChatColor.stripColor(s);
                });
                data = onParseValue(lores);
                //读取nbt属性，附加值属性等
            }else{
                List<ItemStack> items = new ArrayList<>();
                items.add(target.getEquipment().getBoots());
                items.add(target.getEquipment().getLeggings());
                items.add(target.getEquipment().getChestplate());
                items.add(target.getEquipment().getHelmet());
                items.add(target.getEquipment().getItemInMainHand());
                items.add(target.getEquipment().getItemInOffHand());
                List<String> lores = new ArrayList<>();
                items.forEach(s -> {
                    if(ItemUtils.isApproveItem(s)) {
                        lores.addAll(s.getItemMeta().getLore());
                    }
                });
                lores.forEach(s -> {
                    s = ChatColor.stripColor(s);
                });
                data = onParseValue(lores);
            }
            return data;
    }
}
