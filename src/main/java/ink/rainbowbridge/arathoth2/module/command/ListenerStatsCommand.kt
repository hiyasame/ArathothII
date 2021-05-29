package ink.rainbowbridge.arathoth2.module.command

import ink.rainbowbridge.arathoth2.ArathothII
import ink.rainbowbridge.arathoth2.utils.KetherUtils
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.module.locale.TLocale
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerCommandPreprocessEvent

/**
 * @Author 寒雨
 * @Since 2021/5/22 8:26
 */
@TListener
class ListenerStatsCommand : Listener {
    @EventHandler
    fun listen(e:PlayerCommandPreprocessEvent){
        if (e.message.equals("/"+ArathothII.config.getString("StatusInfo.command","my"))){
            e.isCancelled = false
          var papi = ArathothII.config.getStringList("StatusInfo.papi").run {
              var list = ArrayList<String>()
              forEach { str -> if (PlaceholderAPI.setPlaceholders(e.player,str) == ArathothII.DecimalFormat.format(0.0)
                      || PlaceholderAPI.setPlaceholders(e.player,str) == ArathothII.DecimalFormat.format(0.0)+"%") {
                  list.add(str)
              } }
              list
          }
            var strs = ArathothII.config.getStringListColored("StatusInfo.pattern").run {
                var list = ArrayList<String>()
                forEach { str ->
                    run {
                        var hide = false
                        for (s in papi) {
                            if (str.contains(s)) {
                                hide = true
                                break
                            }
                        }
                        if (!hide) {
                            list.add(str)
                        }
                    }
                 }
                list
            }
            if (strs.size >= 2){
                // 骚操作
                e.player.sendMessage(TLocale.Translate.setColored(PlaceholderAPI.setPlaceholders(e.player,strs.joinToString { "\n" })))
            }else{
                KetherUtils.eval(e.player,ArathothII.config.getStringList("StatusInfo.noStatusAction"))
            }
            }
        }
    }