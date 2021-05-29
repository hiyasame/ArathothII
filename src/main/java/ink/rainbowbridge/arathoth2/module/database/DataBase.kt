package ink.rainbowbridge.arathoth2.module.database

import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

/**
 * @Author 寒雨
 * @Since 2021/5/22 8:17
 */
abstract class DataBase {

    abstract fun download(player: Player): FileConfiguration

    abstract fun upload(player: Player)
}