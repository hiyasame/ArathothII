package ink.rainbowbridge.arathoth2.moudle.base.data

import io.izzel.taboolib.util.serialize.TSerializable
import io.izzel.taboolib.util.serialize.TSerializeMap
import java.util.*
import kotlin.collections.HashMap


/**
 * @Author 寒雨
 * @Since 2021/5/2 0:50
 */
data class EntityData(val uuid: UUID,@TSerializeMap var addonDataMap:HashMap<Long,HashMap<String,StatusData>>) : TSerializable{

}