package ink.rainbowbridge.arathoth2.module.base.data.impl

import ink.rainbowbridge.arathoth2.module.base.data.EventData
import ink.rainbowbridge.arathoth2.module.base.data.StatusData
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType
import ink.rainbowbridge.arathoth2.module.base.events.ArathothRunTimeEvent
import org.bukkit.entity.Player

/**
 * @Author 寒雨
 * @Since 2021/5/29 11:43
 */
class RunTimeData(val event:ArathothRunTimeEvent,val executor:Player,override val executorData: StatusData) : EventData(){
    override fun getType(): StatusType = StatusType.RUNTIME
}