package ink.rainbowbridge.arathoth2.module.base.data

import io.izzel.taboolib.util.serialize.TSerializable
import io.izzel.taboolib.util.serialize.TSerializeMap
import kotlin.collections.HashMap


/**
 * @Author 寒雨
 * @Since 2021/5/2 0:50
 */
class EntityData : TSerializable{
    @TSerializeMap var addonDataMap:HashMap<Long,HashMap<String,StatusData>> = HashMap()
}