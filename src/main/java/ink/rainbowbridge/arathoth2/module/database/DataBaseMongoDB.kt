package ink.rainbowbridge.arathoth2.module.database

import ink.rainbowbridge.arathoth2.ArathothII.config
import io.izzel.taboolib.cronus.bridge.CronusBridge
import io.izzel.taboolib.cronus.bridge.database.IndexType
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

/**
 * @Author 寒雨
 * @Since 2021/5/22 8:22
 */
class DataBaseMongoDB : DataBase(){
    val collection = CronusBridge.get(config.getString("Database.url.client"), config.getString("Database.url.database"), config.getString("Database.url.collection"), IndexType.UUID)!!

    override fun download(player: Player): FileConfiguration {
        return collection.get(player.uniqueId.toString()).run {
            if (this.contains("username")) {
                this.set("username", player.name)
            }
            this
        }
    }

    override fun upload(player: Player) {
            collection.update(player.uniqueId.toString())
    }
}