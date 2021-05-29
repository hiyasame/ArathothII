package ink.rainbowbridge.arathoth2.module.base.impl

import ink.rainbowbridge.arathoth2.module.base.abstracts.BaseAttribute
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType
import org.bukkit.event.entity.EntityDamageEvent

/**
 * @Author 寒雨
 * @Since 2021/5/22 10:21
 */
class PhysicalArmor : BaseAttribute(){
    override fun onExecute(eventData: EventData) {
        if(eventData.defenseEvent.cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK
                || eventData.defenseEvent.cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK)
            eventData.defenseEvent.damage -= eventData.executorData.solveData()
    }

    override fun getType(): StatusType = StatusType.DEFENSE
}