package ink.rainbowbridge.arathoth2.utils

import io.izzel.taboolib.kotlin.kether.KetherShell
import io.izzel.taboolib.kotlin.kether.common.util.LocalizedException
import org.bukkit.entity.Player

/**
 * java不配用KetherShell.eval方法
 * 参数太阴间了
 * @Author 寒雨
 * @Since 2021/3/21 8:49
 */
object KetherUtils {
    /**
     * 运行kether脚本
     */
    @JvmStatic
    fun eval(player: Player, action: List<String>) {
        try {
            KetherShell.eval(action) {
                sender = player
            }
        } catch (e: LocalizedException) {
            e.print()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun LocalizedException.print() {
        println("[Arathoth - Kether] Unexpected exception while parsing kether shell:")
        localizedMessage.split("\n").forEach {
            println("[Arathoth - Kether] $it")
        }
    }
}