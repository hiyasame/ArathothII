package ink.rainbowbridge.arathoth2.utils

import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

/**
 * 试着用kt写工具类
 * @Author 寒雨
 * @Since 2021/4/17 15:37
 */
object SendUtils {
    @JvmStatic
    fun send(sender:CommandSender,msg:String){
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7&l[&f&lArathothII&7&l] &7$msg"))
    }
}