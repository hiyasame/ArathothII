package ink.rainbowbridge.arathoth2.moudle.command

import ink.rainbowbridge.arathoth2.moudle.base.manager.AttributeManager
import ink.rainbowbridge.arathoth2.utils.SendUtils
import io.izzel.taboolib.module.command.base.*
import io.izzel.taboolib.module.locale.TLocale
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

/**
 * @Author 寒雨
 * @Since 2021/4/24 9:33
 */
@BaseCommand(name = "arathothnbt",permission = "arathoth.admin",description = "Arathoth NBTCommand",aliases = ["aranbt"])
class NBTCommandHandler : BaseMainCommand() {
    //查看手中物品nbt信息，代码来自TabooLib - ItemTool
    @SubCommand(type = CommandType.PLAYER)
    val nbtinfo = object : BaseSubCommand(){
        override fun getDescription(): String {
            return TLocale.asString("Command.description.nbtinfo")
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {

        }
    }
}