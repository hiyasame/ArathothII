package ink.rainbowbridge.arathoth2.module.command

import ink.rainbowbridge.arathoth2.utils.NBTUtils
import ink.rainbowbridge.arathoth2.utils.SendUtils
import io.izzel.taboolib.module.command.base.*
import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.module.nms.NMS
import io.izzel.taboolib.module.nms.nbt.NBTBase
import io.izzel.taboolib.module.nms.nbt.NBTList
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @Author 寒雨
 * @Since 2021/4/24 9:33
 */
@BaseCommand(name = "arathothnbt",permission = "arathoth.admin",description = "Arathoth NBTCommand",aliases = ["aranbt"])
class NBTCommandHandler : BaseMainCommand() {
    @SubCommand(type = CommandType.PLAYER)
    val info = object : BaseSubCommand(){
        override fun getDescription(): String {
            return TLocale.asString("Command.description.info")
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            NBTUtils.sendNBT(sender as Player, sender.inventory.itemInMainHand)
        }
    }
    @SubCommand(type = CommandType.PLAYER)
    val set = object : BaseSubCommand() {
        override fun getArguments(): Array<Argument> = arrayOf(
                Argument("cond/attr/all"){ arrayListOf("cond","attr","all")},
                Argument("name"),
                Argument("value")
        )
        override fun getDescription(): String {
            return TLocale.asString("Command.description.set")
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            var type = args[0]
            var name = args[1]
            var value = args[2]
            when(type){
                "cond" -> {
                    var item = (sender as Player).inventory.itemInMainHand
                    var fabric = NMS.handle().loadNBT(item)
                    var list = NBTList()
                    list.add(NBTBase(value))
                    //我淦，这个putDeep也太好用了吧，病毒库牛逼
                    //条件直接放个list进去
                    fabric.putDeep("Arathoth.Condition."+name,list)
                    (sender as Player).inventory.setItemInMainHand(NMS.handle().saveNBT(item,fabric))
                    }
                "attr" -> {
                    var item = (sender as Player).inventory.itemInMainHand
                    var fabric = NMS.handle().loadNBT(item)
                    fabric.putDeep("Arathoth.Attribute."+name, NBTBase(value))
                    (sender as Player).inventory.setItemInMainHand(NMS.handle().saveNBT(item,fabric))
                }
                else -> SendUtils.send(sender,TLocale.asString("Command.unknownType"))
            }
        }
    }
    @SubCommand(type = CommandType.PLAYER)
    val remove = object : BaseSubCommand(){
        override fun getArguments(): Array<Argument> = arrayOf(
                Argument("cond/attr/all"){ arrayListOf("cond","attr","all")},
                Argument("name")
        )
        override fun getDescription(): String {
            return TLocale.asString("Command.description.remove")
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            var type = args[0]
            var name = args[1]
            when(type){
                "cond" -> {
                    var item = (sender as Player).inventory.itemInMainHand
                    var fabric = NMS.handle().loadNBT(item)
                    fabric.removeDeep("Arathoth.Condition.$name")
                    (sender as Player).inventory.setItemInMainHand(NMS.handle().saveNBT(item,fabric))
                }
                "attr" -> {
                    var item = (sender as Player).inventory.itemInMainHand
                    var fabric = NMS.handle().loadNBT(item)
                    fabric.removeDeep("Arathoth.Attribute.$name")
                    (sender as Player).inventory.setItemInMainHand(NMS.handle().saveNBT(item,fabric))
                }
                else -> SendUtils.send(sender,TLocale.asString("Command.unknownType"))
            }
        }
    }
    @SubCommand(type = CommandType.PLAYER)
    val removeAll = object : BaseSubCommand(){
        override fun getArguments(): Array<Argument> = arrayOf(
                Argument("cond/attr/all"){ arrayListOf("cond","attr","all")}
        )
        override fun getDescription(): String {
            return TLocale.asString("Command.description.removeAll")
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            var type = args[0]
            when(type){
                "cond" -> {
                    var item = (sender as Player).inventory.itemInMainHand
                    var fabric = NMS.handle().loadNBT(item)
                    fabric.removeDeep("Arathoth.Condition")
                    (sender as Player).inventory.setItemInMainHand(NMS.handle().saveNBT(item,fabric))
                }
                "attr" -> {
                    var item = (sender as Player).inventory.itemInMainHand
                    var fabric = NMS.handle().loadNBT(item)
                    fabric.removeDeep("Arathoth.Attribute")
                    (sender as Player).inventory.setItemInMainHand(NMS.handle().saveNBT(item,fabric))
                }
                "all" -> {
                    var item = (sender as Player).inventory.itemInMainHand
                    var fabric = NMS.handle().loadNBT(item)
                    fabric.removeDeep("Arathoth")
                    (sender as Player).inventory.setItemInMainHand(NMS.handle().saveNBT(item,fabric))
                }
                else -> SendUtils.send(sender,TLocale.asString("Command.unknownType"))
            }
        }
    }
    @SubCommand(type = CommandType.PLAYER,arguments = ["value"])
    val addType = object : BaseSubCommand(){
        override fun getDescription(): String {
            return TLocale.asString("Command.description.addType")
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            var value = args[0]
                    var item = (sender as Player).inventory.itemInMainHand
                    var fabric = NMS.handle().loadNBT(item)
                    var list = fabric.getDeepOrElse("Arathoth.Type",NBTList()).asList()
                    list.add(NBTBase(value))
                    fabric.putDeep("Arathoth.Type",list)
                    (sender as Player).inventory.setItemInMainHand(NMS.handle().saveNBT(item,fabric))
        }
    }
    @SubCommand(type = CommandType.PLAYER,arguments = ["key","value"])
    val addCond = object : BaseSubCommand() {
        override fun getDescription(): String {
            return TLocale.asString("Command.description.addCond")
        }
        override fun onCommand(sender: CommandSender, command: Command, s: String, args: Array<out String>) {
            var key = args[0]
            var value = args[1]
            var item = (sender as Player).inventory.itemInMainHand
            var fabric = NMS.handle().loadNBT(item)
            var list = fabric.getDeepOrElse("Arathoth.Condition.$key", NBTList()).asList()
            list.add(NBTBase(value))
            fabric.putDeep("Arathoth.Condition.$key", list)
            (sender as Player).inventory.setItemInMainHand(NMS.handle().saveNBT(item, fabric))
        }
    }
}