package ink.rainbowbridge.arathoth2.utils.nms

import ink.rainbowbridge.arathoth2.moudle.base.events.ArathothPostElementsEvent
import net.minecraft.server.v1_16_R1.EntityDamageSource
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

/**
 * @Author 寒雨
 * @Since 2021/5/1 8:26
 */
class NMSHandlerImpl : NMSHandler() {
    /**
     * 造成元素伤害
     */
    override fun damage(damager: Entity, victim: LivingEntity, source: String, damage: Float) {
        var eve = ArathothPostElementsEvent(damager,victim,source,damage)
        Bukkit.getPluginManager().callEvent(eve)
        var damageSource = EntityDamageSource(eve.source,(damager as org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity).handle)
        if (!eve.isCancelled) {
            (damager as org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity).handle.damageEntity(damageSource, eve.damage)
        }
    }
}