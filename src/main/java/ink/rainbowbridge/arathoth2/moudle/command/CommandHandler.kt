package ink.rainbowbridge.arathoth2.moudle.command

import ink.rainbowbridge.arathoth2.ArathothII
import ink.rainbowbridge.arathoth2.api.ArathothAPI
import ink.rainbowbridge.arathoth2.moudle.base.enums.PlaceHolderType
import ink.rainbowbridge.arathoth2.moudle.base.manager.AttributeManager
import ink.rainbowbridge.arathoth2.utils.SendUtils
import io.izzel.taboolib.module.command.base.BaseCommand
import io.izzel.taboolib.module.command.base.BaseMainCommand
import io.izzel.taboolib.module.command.base.BaseSubCommand
import io.izzel.taboolib.module.command.base.SubCommand
import io.izzel.taboolib.module.locale.TLocale
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
    @SubCommand
    val about:BaseSubCommand = object : BaseSubCommand(){
        override fun getDescription(): String {
            return TLocale.asString("Command.description.about")
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&f&lArathothII &7&lby.&8&l寒雨"))
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7Version: ${ArathothII.getInstance().plugin.description.version} "))
        }
    }
    @SubCommand(aliases = ["Online_Player"])
    val statusinfo = object : BaseSubCommand(){
        override fun getDescription(): String {
            return TLocale.asString("Command.description.statusinfo")
        }

        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            val p = Bukkit.getPlayer(args[0])
            if(p == null){
                SendUtils.send(sender,TLocale.asString("Command.player_offline"))
                return
            }
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7&l>&8&l&m-----&f&l[ &8${p.name} &fAttributeInfo &7&l]&8&l&m-----&7&l<"))
            ArathothAPI.getEntityAttrDataMap(p).forEach { (t, u) ->  sender.sendMessage("    &8&l> &7${ArathothAPI.getAttributeMap()[t]?.displayName} : &f${u.getPlaceHolder(PlaceHolderType.TOTAL)}")}
        }
    }
    @SubCommand
    val reload = object : BaseSubCommand(){
        override fun getDescription(): String {
            return TLocale.asString("Command.description.reload")
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            SendUtils.send(sender,TLocale.asString("Command.try_reload"))
            var time = System.currentTimeMillis();
            try {
                ArathothII.loadPlugin()
                SendUtils.send(sender,TLocale.asString("Command.success_reload",(System.currentTimeMillis() - time)))
            }catch (t:Throwable){
                SendUtils.send(sender,TLocale.asString("Command.failed_reload",t.localizedMessage))
                t.printStackTrace()
            }
        }
    }
    @SubCommand
    val list = object : BaseSubCommand(){
        override fun getDescription(): String {
            return TLocale.asString("Command.description.list")
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            SendUtils.send(sender,"Attributes/Conditions list")
            AttributeManager.attributeList.forEach { attr -> TLocale.sendTo(sender,"list_json","BaseAttribute",attr.name,
            ChatColor.translateAlternateColorCodes('&', arrayListOf("&7&l ● &8Name: &f${attr.name}",
                    "&7&l ● &8Enable: &f${attr.isEnable}",
                    "&7&l ● &8Priority: &f${attr.priority}",
                    "&7&l ● &8DisplayName: &f${attr.displayName}",
                    "&7&l ● &8Description: &f${attr.description}",
                    "&7&l ● &8RegexPatterns: &f${attr.patterns.joinToString("\n   &7- &f")}"
                    ).joinToString("\n"))) }
            AttributeManager.conditionList.forEach { cond ->  TLocale.sendTo(sender,"list_json","BaseCondition",cond.name,
                    ChatColor.translateAlternateColorCodes('&', arrayListOf("&7&l ● &8Name: &f${cond.name}",
                            "&7&l ● &8Enable: &f${cond.isEnable}",
                            "&7&l ● &8Priority: &f${cond.priority}",
                            "&7&l ● &8DisplayName: &f${cond.displayName}",
                            "&7&l ● &8RegexPatterns: &f${cond.patterns.joinToString("\n   &7- &f")}"
                    ).joinToString("\n"))) }
        }
    }
}