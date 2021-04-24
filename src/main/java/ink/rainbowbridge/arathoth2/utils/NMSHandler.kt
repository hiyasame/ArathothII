package ink.rainbowbridge.arathoth2.utils

import io.izzel.taboolib.module.inject.TInject
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @Author 寒雨
 * @Since 2021/4/24 9:59
 */
abstract class NMSHandler {
   abstract fun sendItemNBT(player : Player , itemStack: ItemStack)
    companion object{
        @TInject(asm = "ink.rainbowbridge.arathoth2.utils.NMSHandlerImpl")
        val nmsHandler:NMSHandler? = null
    }
}