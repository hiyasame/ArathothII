package ink.rainbowbridge.arathoth2.moudle.base.abstracts;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.moudle.base.events.ArathothPostConditionEvent;
import ink.rainbowbridge.arathoth2.moudle.base.manager.AttributeManager;
import io.izzel.taboolib.module.locale.TLocale;
import io.izzel.taboolib.module.nms.NMS;
import io.izzel.taboolib.module.nms.nbt.NBTCompound;
import io.izzel.taboolib.module.nms.nbt.NBTList;
import lombok.Getter;
import lombok.var;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 较上版本完全优化，现在注册条件比注册属性还方便
 * @Author 寒雨
 * @Since 2021/4/4 8:31
 */
public abstract class BaseCondition {
    @Getter
    private Plugin plugin;
    @Getter
    private FileConfiguration config;
    private final BaseCondition instance = this;
    public final void register(Plugin plugin){
        load();
        //优先级排序操作
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    if (!AttributeManager.conditionList.contains(instance)) {
                        for (BaseCondition cond : AttributeManager.conditionList) {
                            if (cond.getPriority() <= instance.getPriority()) {
                                int index = AttributeManager.attributeList.indexOf(cond);
                                AttributeManager.conditionList.add(index, instance);
                                AttributeManager.conditionMap.put(instance.getName(),instance);
                                break;
                            }
                        }
                        TLocale.sendToConsole("Plugin.COND_REGISTER", getName(), plugin.getName(), getPriority());
                    }
                }catch (Throwable ex){
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(ArathothII.getInstance().getPlugin());
        onRegistering();
    }

    /**
     * 获取条件名，默认返回属性class名
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
     * 在注册时执行的方法
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
     * 载入属性配置方法
     */
    public void load(){
        new BukkitRunnable() {
            @Override
            public void run() {
                File file = new File(AttributeManager.getConditionConfigDir(),getName()+".yml");
                if (!file.exists()) {
                    FileWriter fw = null;
                    PrintWriter out = null;
                    try {
                        fw = new FileWriter(file);
                        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)));
                        out.write("# ArathothII Conditions Configuration\n");
                        out.write("# @Author Freeze003(寒雨)\n");
                        out.write(getName() + ":\n");
                        out.write(" Enable: true\n");
                        out.write(" Priority: 0\n");
                        out.write(" DisplayName: '"+getName()+"'\n");
                        out.write(" Patterns:\n");
                        out.write(" - '[VALUE] " + getName() + "'\n");
                        out.write(" NotPass_KetherAction:\n");
                        out.write(" - 'send color *\"&7&l[&f&lRainbowBridge&7&l] &7物品 [item] &7未通过条件: &f"+getName()+"\""+"'\n");
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
     * 重写该抽象方法来指定条件判断规则
     * values为空list时不会执行该方法直接return true
     * 放心使用
     *  例子 OwnderRequest 实现，仅需3行代码:
     *   if(values.contains(p.getName()))
     *      return true;
     *    return false;
     * @param item 物品
     * @param p 玩家
     * @param values values
     * @return 是否通过
     */
    public abstract boolean onPass(ItemStack item, Player p, List<String> values);

    public List<String> getPatterns(){
        return getConfig().getStringList(getName()+".Patterns");
    }

    /**
     * 重写该方法来制定正则匹配规则
     * 默认: (\\S+)
     * @return regex
     */
    public String getRegexPlaceHolder(){ return "(\\S+)";}

    /**
     * 注: 应该在进行非空判断后再对物品执行该方法
     * 否则空指针
     * @param item 物品
     * @param p 玩家
     * @return 结果
     */
    public boolean pass(ItemStack item,Player p){
        var values = new ArrayList<String>();
        var lore = item.getItemMeta().getLore().stream().map(ChatColor::stripColor).collect(Collectors.toList());
        getPatterns().forEach(s -> {
            Pattern pt = Pattern.compile(s.replace("[VALUE]",getRegexPlaceHolder()));
            var m = pt.matcher(s);
            if (m.find()){
                values.add(m.group(1));
            }
        });
       var compound = NMS.handle().loadNBT(item);
       if (compound.containsKey("Arathoth")){
          var list = compound.getOrDefault("Arathoth",new NBTCompound()).asCompound().getOrDefault("Condition",new NBTCompound()).asCompound().getOrDefault(this.getName(),new NBTList()).asList();
          var trueList = new ArrayList<String>();
          list.forEach(nbtBase -> trueList.add(nbtBase.asString()));
          values.addAll(trueList);
       }
       var eve = new ArathothPostConditionEvent(p,values);
        Bukkit.getPluginManager().callEvent(eve);
        if (eve.getValues().isEmpty())
            return true;
        return onPass(item,p,eve.getValues());
    }

    /**
     * 注销条件方法
     */
    public final void unRegister(){
        AttributeManager.conditionMap.remove(this.getName());
        AttributeManager.conditionList.remove(this);
        TLocale.sendToConsole("Plugin.COND_UNREGISTER",getName());
    }

    public final List<String> getKetherScripts(){
        return config.getStringList(getName()+"NotPass_KetherAction");
    }
}
