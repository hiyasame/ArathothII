package ink.rainbowbridge.arathoth2.moudle.base.data

import ink.rainbowbridge.arathoth2.ArathothII
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*
import kotlin.collections.HashMap

/**
 * 生物data加载器
 * @Author 寒雨
 * @Since 2021/5/2 11:40
 */
object DataLoader {
    val data:HashMap<UUID,EntityData> = HashMap()
    fun onLoadData(){
        var file = File(ArathothII.getInstance().plugin.dataFolder,"Data")
        file.list().forEach { str ->
            if (str.endsWith(".yml")){
                //本来想用.json 但是不知道怎么把json序列化到文件里，只好先用.yml
                var conf = YamlConfiguration.loadConfiguration(File(file,str))
                try{
                    var entitydata:EntityData = EntityData(UUID.randomUUID(), HashMap())
                    var data1 = entitydata.read(conf.getString("Json")) as EntityData
                    data.put(UUID.fromString(str.replace(".yml","")),data1)
                }catch (t:Throwable){
                    t.printStackTrace()
                }
            }
        }
    }
}