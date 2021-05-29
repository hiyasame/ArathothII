package ink.rainbowbridge.arathoth2.module.base.impl

import ink.rainbowbridge.arathoth2.module.base.abstracts.BaseAttribute
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType

/**
 * @Author 寒雨
 * @Since 2021/5/22 10:07
 */
class PhysicalDamage : BaseAttribute(){

    override fun onExecute(eventData: EventData) {
        eventData.attackEvent.damage += eventData.executorData.solveData()
    }

    override fun getType(): StatusType = StatusType.ATTACK
}