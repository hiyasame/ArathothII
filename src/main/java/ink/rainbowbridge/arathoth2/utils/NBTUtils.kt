package ink.rainbowbridge.arathoth2.utils

import io.izzel.taboolib.module.locale.TLocale
import io.izzel.taboolib.module.nms.NMS
import io.izzel.taboolib.module.nms.nbt.NBTBase
import io.izzel.taboolib.module.nms.nbt.NBTCompound
import io.izzel.taboolib.module.nms.nbt.NBTType
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 感谢坏黑哥哥的TabooLib里提供的NBT结构映射类
 * 真的太爽了
 * @Author 寒雨
 * @Since 2021/5/1 0:41
 */
object NBTUtils {
    /**
     * 发送单个NBTBase
     */
    private fun sendNBTBase(p: Player,pause: String,key1:String,value:NBTBase){
        var key = key1
        if (key != ""){
            key += ":"
        }
        when(value.type){
            NBTType.STRING -> p.send(pause+key,value.asString())
            NBTType.DOUBLE -> p.send(pause+key,value.asDouble().toString())
            NBTType.BYTE -> p.send(pause+key,value.asByte().toInt().toString())
            NBTType.BYTE_ARRAY -> p.send(pause+key,value.asByteArray().toString())
            NBTType.INT -> p.send(pause+key,value.asInt().toString())
            NBTType.INT_ARRAY -> p.send(pause+key,value.asIntArray().toString())
            NBTType.FLOAT -> p.send(pause+key,value.asFloat().toString())
            NBTType.LONG -> p.send(pause+key,value.asLong().toString())
            NBTType.SHORT -> p.send(pause+key,value.asShort().toString())
            NBTType.LIST -> {
                value.asList().forEach { base ->
                    sendNBTBase(p, "$pause -","",base)
                }
            }
            NBTType.COMPOUND -> sendNBTCompound(p, "$pause ",value.asCompound())
        }

    }

    private fun CommandSender.send(key: String ,value: String){
        sendMessage(TLocale.Translate.setColored(" §8[§3§k§l§o|§8] §7$key &f$value"))
    }

    private fun sendNBTCompound(p:Player,pause:String,compound: NBTCompound){
        compound.forEach { key, value -> sendNBTBase(p,pause,key,value)}
    }

    fun sendNBT(p:Player,item:ItemStack){
        p.send("NBT ","->")
        sendNBTCompound(p," ",NMS.handle().loadNBT(item))
    }
}