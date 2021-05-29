package ink.rainbowbridge.arathoth2.module.script.javascript;

import ink.rainbowbridge.arathoth2.ArathothII;
import org.bukkit.plugin.Plugin;


/**
 * 为javascript玩家准备的工具类
 * @Author 寒雨
 * @Since 2021/4/11 9:37
 */
public class JsUtils {
    /**
     * 获取ArathothII插件实例
     * 一般用于运行BukkitRunnable
     * eg.
     * var r = new org.bukkit.scheduler.BukkitRunnable(){
     *     run: function(){
     *         print('fuck you');
     *     }
     * }
     * r.runTask(Utils.getPlugin());
     * @return 插件实例
     */
    public static Plugin getPlugin(){
        return ArathothII.getInstance().getPlugin();
    }
}
