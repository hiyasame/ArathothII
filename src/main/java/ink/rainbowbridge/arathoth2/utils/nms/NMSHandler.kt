package ink.rainbowbridge.arathoth2.utils.nms

import io.izzel.taboolib.module.inject.TInject
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * @Author 寒雨
 * @Since 2021/5/1 1:21
 */
abstract class NMSHandler {
    abstract fun damage(damager: Entity, victim:LivingEntity, source:String, damage:Float)
    companion object{
        @TInject(asm = "ink.rainbowbridge.arathoth2.utils.nms.NMSHandlerImpl")
        val nmsHandler: NMSHandler? = null
    }
}