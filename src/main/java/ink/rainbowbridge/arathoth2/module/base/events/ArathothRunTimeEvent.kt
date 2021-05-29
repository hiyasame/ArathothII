package ink.rainbowbridge.arathoth2.module.base.events

import io.izzel.taboolib.module.event.EventNormal
import org.bukkit.Bukkit

/**
 * RUNTIME类属性监听的事件
 * tick即当前时间戳/1000 即1970年到现在流过的的时间秒数
 * 通过判断其是否为某个数的倍数实现任意秒执行一次
 * eg. if(event.tick % 10 == 0){ //操作 } <- 这样便可实现十秒执行一次
 * 但这样做有一定的缺陷，就是第一次执行未必间隔十秒
 * 注意，该事件异步呼出
 * @Author 寒雨
 * @Since 2021/5/29 11:22
 */
class ArathothRunTimeEvent(val tick:Long) : EventNormal<ArathothRunTimeEvent>(){
    init {
        async(!Bukkit.isPrimaryThread())
    }
}