package ink.rainbowbridge.arathoth2.module.base.data.impl

import ink.rainbowbridge.arathoth2.module.base.data.EventData
import ink.rainbowbridge.arathoth2.module.base.data.StatusData
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Event

/**
 * @Author 寒雨
 * @Since 2021/5/29 10:21
 */
class CustomData(val event: Event, val executor:LivingEntity, override val executorData: StatusData) : EventData(){
    override fun getType(): StatusType = StatusType.CUSTOM
}