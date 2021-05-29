package ink.rainbowbridge.arathoth2.module.database

import io.izzel.taboolib.module.db.local.LocalPlayer
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

/**
 * @Author 寒雨
 * @Since 2021/5/22 8:20
 */
class DataBaseLocal : DataBase(){
    override fun download(player: Player): FileConfiguration {
        return LocalPlayer.get(player)
    }

    override fun upload(player: Player) {
    }
}