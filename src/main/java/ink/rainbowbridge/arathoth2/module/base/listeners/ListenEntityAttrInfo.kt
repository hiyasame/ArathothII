package ink.rainbowbridge.arathoth2.module.base.listeners

import ink.rainbowbridge.arathoth2.ArathothII
import ink.rainbowbridge.arathoth2.ArathothII.config
import ink.rainbowbridge.arathoth2.module.base.data.StatusData
import ink.rainbowbridge.arathoth2.module.base.enums.PlaceHolderType
import ink.rainbowbridge.arathoth2.utils.ItemUtils
import ink.rainbowbridge.arathoth2.utils.NameUtils
import ink.rainbowbridge.arathoth2.utils.SendUtils
import io.izzel.taboolib.module.inject.TListener
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import java.util.HashMap

/**
 * 属性查询器
 * @Author 寒雨
 * @Since 2021/4/23 23:25
 */
@TListener
@SuppressWarnings("unchecked")
class ListenEntityAttrInfo : Listener {
    @EventHandler
    fun onCheck(e:PlayerInteractEntityEvent) {
        if (ItemUtils.isApproveItem(e.player.inventory.itemInMainHand)){
            if(config.getString("AttrCheckerType")?.let { e.player.inventory.itemInMainHand.itemMeta?.lore?.get(0)?.contains(it) }!!
                    && e.rightClicked is LivingEntity){
              var data =  ArathothII.getMetadata(e.rightClicked,"StatusData",ArathothII.getInstance().plugin) as HashMap<String,StatusData>
                SendUtils.send(e.player,"&fEntity ${NameUtils.getEntityName(e.player, e.rightClicked as LivingEntity)} &f's Status :")
                data.forEach{(str,d) -> SendUtils.send(e.player,"&8${str} : &f${d.getPlaceHolder(PlaceHolderType.TOTAL)}")
                }
            }
        }
    }
}