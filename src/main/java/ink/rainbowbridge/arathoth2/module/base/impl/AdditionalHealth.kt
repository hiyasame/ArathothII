package ink.rainbowbridge.arathoth2.module.base.impl

import ink.rainbowbridge.arathoth2.module.base.abstracts.BaseAttribute
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

/**
 * 该属性只对玩家有效，修复上版本的bug
 * @Author 寒雨
 * @Since 2021/5/22 10:22
 */
class AdditionalHealth : BaseAttribute() {
    val base = config.getDouble(name+".settings.base_health")
    val min = config.getDouble(name+".settings.min_health")
    override fun onExecute(eventData: EventData) {
        if(eventData.executor is Player){
            var value = base + eventData.executorData.solveData()
            if (value > min) {
                eventData.executor.health = base + eventData.executorData.solveData()
            }else{
                eventData.executor.health = min
            }
        }
    }
    //有一说一，kt的空安全蛮烦人的
    override fun setDefaultConfig(config: FileConfiguration): FileConfiguration? = config.run {
        set("$name.settings.base_health",20.0)
        set("$name.settings.min_health",1.0)
        this
    }
    override fun getType(): StatusType = StatusType.UPDATE
}