package ink.rainbowbridge.arathoth2.moudle.base.events

import io.izzel.taboolib.module.event.EventCancellable
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * @Author 寒雨
 * @Since 2021/5/2 0:04
 */
class ArathothPostElementsEvent(val damager: Entity,val victim: LivingEntity,var source: String, var damage: Float) : EventCancellable<ArathothPostElementsEvent>() {

    init {
        async(!Bukkit.isPrimaryThread())
    }
}