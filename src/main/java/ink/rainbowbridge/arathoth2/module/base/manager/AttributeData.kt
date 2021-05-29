package ink.rainbowbridge.arathoth2.module.base.manager

import ink.rainbowbridge.arathoth2.module.base.data.StatusData
import java.util.*
import kotlin.collections.HashMap

/**
 * ArathothII
 * ink.rainbowbridge.arathoth2.module.base.manager
 *
 * @author 寒雨
 * @since 2021/5/29 12:31
 */
object AttributeData {
    val entityStatus = HashMap<UUID,HashMap<String,StatusData>>()
    /**
     * 清除属性缓存
     */
    fun clean() = entityStatus.clear()
}