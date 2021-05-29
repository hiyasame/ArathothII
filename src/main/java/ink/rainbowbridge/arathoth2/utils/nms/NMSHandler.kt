package ink.rainbowbridge.arathoth2.utils.nms

import io.izzel.taboolib.module.inject.TInject
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent

/**
 * @Author 寒雨
 * @Since 2021/5/1 1:21
 */
abstract class NMSHandler {
    abstract fun damage(victim:LivingEntity, cause:EntityDamageEvent.DamageCause, damage:Float)
    companion object{
        @TInject(asm = "ink.rainbowbridge.arathoth2.utils.nms.NMSHandlerImpl")
        val nmsHandler: NMSHandler? = null
    }
}