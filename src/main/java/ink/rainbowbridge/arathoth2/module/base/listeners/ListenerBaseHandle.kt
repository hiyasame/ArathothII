package ink.rainbowbridge.arathoth2.module.base.listeners

import ink.rainbowbridge.arathoth2.api.ArathothAPI
import ink.rainbowbridge.arathoth2.module.base.data.impl.AttackData
import ink.rainbowbridge.arathoth2.module.base.data.impl.DefenseData
import ink.rainbowbridge.arathoth2.module.base.data.impl.RunTimeData
import ink.rainbowbridge.arathoth2.module.base.data.impl.UpdateData
import ink.rainbowbridge.arathoth2.module.base.enums.StatusType
import ink.rainbowbridge.arathoth2.module.base.events.ArathothRunTimeEvent
import ink.rainbowbridge.arathoth2.module.base.events.ArathothStatusUpdateEvent
import ink.rainbowbridge.arathoth2.module.base.manager.AttributeManager
import io.izzel.taboolib.module.inject.TListener
import io.izzel.taboolib.module.inject.TSchedule
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent

/**
 * @Author 寒雨
 * @Since 2021/5/29 10:36
 */
@TListener
object ListenerBaseHandle : Listener {
    @EventHandler
    fun onAttrExecuteAttack(e: EntityDamageByEntityEvent) {
        AttributeManager.attributeList.forEach {
            if (it.type == StatusType.ATTACK) {
                if (e.damager is LivingEntity){
                    val attacker = e.damager as LivingEntity
                    val data = AttackData(e,e.damager, e.entity as LivingEntity,attacker, ArathothAPI.getEntityAttrData(e.damager,it))
                    it.execute(data)
                }
                else if (e.damager is Projectile) {
                    if ((e.damager as Projectile).shooter is LivingEntity){
                       val attacker = (e.damager as Projectile).shooter as LivingEntity
                        val data = AttackData(e,e.damager, e.entity as LivingEntity,attacker, ArathothAPI.getEntityAttrData(e.damager,it))
                        it.execute(data)
                    }
                }
            }
        }
    }

    @EventHandler
    fun onAttrExecuteDefense(e: EntityDamageEvent) {
        AttributeManager.attributeList.forEach {
                if (it.type == StatusType.DEFENSE){
                    it.execute(DefenseData(e,e.entity as LivingEntity,ArathothAPI.getEntityAttrData(e.entity,it)))
                }
            }
        }

    @EventHandler
    fun onAttrExecuteUpdate(e: ArathothStatusUpdateEvent) {
        AttributeManager.attributeList.forEach {
            if (it.type == StatusType.UPDATE){
                it.execute(UpdateData(e,e.executor,ArathothAPI.getEntityAttrData(e.executor,it)))
            }
        }
    }

    @EventHandler
    fun onAttrExecuteRunTime(e: ArathothRunTimeEvent) {
        AttributeManager.attributeList.forEach {attr ->
            Bukkit.getOnlinePlayers().forEach{
                attr.execute(RunTimeData(e,it,ArathothAPI.getEntityAttrData(it,attr)))
            }
        }
    }


    @TSchedule(period = 20,async = true)
    fun callRunTimeEvent(){
        ArathothRunTimeEvent(System.currentTimeMillis()/1000).call()
    }
}