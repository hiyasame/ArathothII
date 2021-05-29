package ink.rainbowbridge.arathoth2.utils.nms

import net.minecraft.server.v1_16_R1.DamageSource
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageEvent

/**
 * update 2021.5.22
 * NMS的伤害类型怎么都无所谓了
 * 只要事件上的伤害类型能custom就ok
 * @Author 寒雨
 * @Since 2021/5/1 8:26
 */
class NMSHandlerImpl : NMSHandler() {
    /**
     * 造成元素伤害
     */
    override fun damage(victim: LivingEntity, cause: EntityDamageEvent.DamageCause, damage: Float) {
        // 默认GENERIC
        var source:DamageSource = DamageSource.GENERIC
        when(cause){
            EntityDamageEvent.DamageCause.FIRE -> source = DamageSource.FIRE
            EntityDamageEvent.DamageCause.STARVATION -> source = DamageSource.STARVE
            EntityDamageEvent.DamageCause.WITHER -> source = DamageSource.WITHER
            EntityDamageEvent.DamageCause.SUFFOCATION -> source = DamageSource.STUCK
            EntityDamageEvent.DamageCause.DROWNING -> source = DamageSource.DROWN
            EntityDamageEvent.DamageCause.FIRE_TICK -> source = DamageSource.BURN
            EntityDamageEvent.DamageCause.MAGIC -> source = DamageSource.MAGIC
            EntityDamageEvent.DamageCause.FALL -> source = DamageSource.FALL
            EntityDamageEvent.DamageCause.FLY_INTO_WALL -> source = DamageSource.FLY_INTO_WALL
            EntityDamageEvent.DamageCause.CRAMMING -> source = DamageSource.DRYOUT
            EntityDamageEvent.DamageCause.CUSTOM -> source = DamageSource.GENERIC
            EntityDamageEvent.DamageCause.HOT_FLOOR -> source = DamageSource.HOT_FLOOR
            EntityDamageEvent.DamageCause.VOID -> source = DamageSource.OUT_OF_WORLD
            EntityDamageEvent.DamageCause.LAVA -> source = DamageSource.LAVA
        }
        // 自己call事件还行，我还以为NMS都是没改过的呢,没想到还是被bk魔改过了
            (victim as CraftEntity).handle.damageEntity(source,damage)
    }
}