package ink.rainbowbridge.arathoth2.module.base.data.impl

import ink.rainbowbridge.arathoth2.module.base.data.EventData
import ink.rainbowbridge.arathoth2.module.base.data.StatusData
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.event.entity.EntityDamageByEntityEvent

/**
 * @Author 寒雨
 * @Since 2021/5/29 7:50
 */
class AttackData(val event:EntityDamageByEntityEvent, val executor: Entity, val victim : LivingEntity,
                 val attacker : LivingEntity, override val executorData: StatusData) : EventData() {
    override fun getType(): StatusType = StatusType.ATTACK
}