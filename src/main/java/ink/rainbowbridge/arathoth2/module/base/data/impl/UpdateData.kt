package ink.rainbowbridge.arathoth2.module.base.data.impl

import ink.rainbowbridge.arathoth2.module.base.data.EventData
import ink.rainbowbridge.arathoth2.module.base.data.StatusData
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType
import ink.rainbowbridge.arathoth2.module.base.events.ArathothStatusUpdateEvent
import org.bukkit.entity.LivingEntity

/**
 * @Author 寒雨
 * @Since 2021/5/29 8:17
 */
class UpdateData(val event: ArathothStatusUpdateEvent, val executor: LivingEntity, override val executorData: StatusData) : EventData()  {
    override fun getType(): StatusType = StatusType.UPDATE
}