package ink.rainbowbridge.arathoth2.moudle.command

import ink.rainbowbridge.arathoth2.ArathothII
import ink.rainbowbridge.arathoth2.api.ArathothAPI
import ink.rainbowbridge.arathoth2.moudle.base.enums.PlaceHolderType
import ink.rainbowbridge.arathoth2.utils.SendUtils
import io.izzel.taboolib.module.command.base.BaseCommand
import io.izzel.taboolib.module.command.base.BaseMainCommand
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.SubCommand
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * 想试着用刚学的半吊子kt写命令
 * @Author 寒雨
 * @Since 2021/4/17 15:00
 */
@BaseCommand(name = "arathoth",aliases = ["ara","arathoth2"],permission = "arathoth.admin",permissionMessage = "§f§lArathothII §7§lby.§8§l寒雨\n"
+ "§7Version: 2.0")
class CommandHandler : BaseMainCommand(){
    @SubCommand(description = "about author & plugin")
    val about:BaseSubCommand = object : BaseSubCommand(){
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f&lArathothII &7&lby.&8&l寒雨"))
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7Version: ${ArathothII.getInstance().plugin.description.version} "))
        }
    }
    @SubCommand(description = "list the player's status information",aliases = ["Online_Player"])
    val statusinfo = object : BaseSubCommand(){
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            val p = Bukkit.getPlayer(args[0])
            if(p == null){
                SendUtils.send(sender,"&4the player is not exist")
                return;
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7&l>&8&l&m-----&f&l[ &8${p.name} &fAttributeInfo &7&l]&8&l&m-----&7&l<"))
            ArathothAPI.getEntityAttrDataMap(p).forEach { (t, u) ->  sender.sendMessage("    &8&l> &7${ArathothAPI.getAttributeMap()[t]?.displayName} : &f${u.getPlaceHolder(PlaceHolderType.TOTAL)}")}
        }
    }
}