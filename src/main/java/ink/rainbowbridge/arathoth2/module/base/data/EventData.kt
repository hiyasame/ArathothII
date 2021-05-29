package ink.rainbowbridge.arathoth2.module.base.data

import ink.rainbowbridge.arathoth2.module.base.data.impl.*
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType

/**
 * @Author 寒雨
 * @Since 2021/5/29 7:48
 */
abstract class EventData {
    abstract fun getType():StatusType
    abstract val executorData:StatusData;
    fun asAttackData():AttackData = this as AttackData
    fun asDefenseData():DefenseData = this as DefenseData
    fun asUpdateData():UpdateData = this as UpdateData
    fun asCustomData():CustomData = this as CustomData
    fun asRunTimeData():RunTimeData = this as RunTimeData
}