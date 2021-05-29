package ink.rainbowbridge.arathoth2.module.base.impl

import ink.rainbowbridge.arathoth2.module.base.abstracts.BaseAttribute
import ink.rainbowbridge.arathoth2.module.base.data.EventData
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType
import ink.rainbowbridge.arathoth2.utils.nms.NMSHandler
import org.bukkit.event.entity.EntityDamageEvent

/**
 * @Author 寒雨
 * @Since 2021/5/22 10:30
 */
class MagicDamage : BaseAttribute() {
    override fun onExecute(eventData: EventData) {
        NMSHandler.nmsHandler?.damage(eventData.victim,EntityDamageEvent.DamageCause.MAGIC,eventData.executorData.solveData().toFloat())
    }

    override fun isZeroExecute(): Boolean = false
    override fun isFixValue(): Boolean = true
    override fun getType(): StatusType = StatusType.ATTACK
}