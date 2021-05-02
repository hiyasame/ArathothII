package ink.rainbowbridge.test

import ink.rainbowbridge.arathoth2.moudle.base.data.EntityData
import ink.rainbowbridge.arathoth2.moudle.base.data.StatusData
import java.util.*
import kotlin.collections.HashMap

/**
 * 测试类
 * @Author 寒雨
 * @Since 2021/5/2 11:13
 */
object Test {
    @JvmStatic
    fun main(args: Array<String>) {
        // 创建对象
        val data = EntityData(UUID.randomUUID(), HashMap())
        // 修改参数
        var map:HashMap<String, StatusData> = HashMap()
        map.put("Example", StatusData())
        data.addonDataMap.put(10000L,map)
        // 序列化
        val value = data.write()
        // 打印
        println(value)
        // 创建新的对象
        val dataCopy = EntityData(UUID.randomUUID(), HashMap())
        // 反序列化
        dataCopy.read(value)
        // 打印
        println(dataCopy)
    }
}