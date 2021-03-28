package ink.rainbowbridge.arathoth2.moudle.base.listeners;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.api.ArathothAPI;
import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseAttribute;
import ink.rainbowbridge.arathoth2.moudle.base.data.EventData;
import ink.rainbowbridge.arathoth2.moudle.base.enums.StatusType;
import ink.rainbowbridge.arathoth2.moudle.base.events.ArathothStatusUpdateEvent;
import ink.rainbowbridge.arathoth2.moudle.base.manager.AttributeManager;
import ink.rainbowbridge.arathoth2.utils.KetherUtils;
import ink.rainbowbridge.arathoth2.utils.NameUtils;
import io.izzel.taboolib.module.inject.TListener;
import io.izzel.taboolib.util.item.Items;
import lombok.var;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.List;

/**
 * @Author 寒雨
 * @Since 2021/3/21 0:48
 */
@TListener
public class ListenBaseHandle implements Listener {

    @EventHandler
    public void onSendBowData(EntityShootBowEvent event){
        ArathothAPI.setArrowData(event.getProjectile(),ArathothAPI.getEntityAttrDataMap(event.getEntity()));
    }

    @EventHandler
    public void onCancelBowAttack(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player){
            var p = (Player)event.getDamager();
            if (!Items.isNull(p.getInventory().getItemInMainHand())){
                if(p.getInventory().getItemInMainHand().getType() == Material.BOW)
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAttackKetherAction(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player) {
            var p = (Player)event.getDamager();
            List<String> scripts = ArathothII.config.getStringList("KetherAction.ATTACK");
            scripts.forEach(s -> s = s
                    .replace("[DAMAGE]", ArathothII.DecimalFormat.format(event.getDamage()))
                    .replace("[NAME]", NameUtils.getEntityName(p,(LivingEntity) event.getEntity())));
            KetherUtils.eval(p,scripts);
        }
        if(event.getDamager() instanceof Projectile){
            if (((Projectile)event.getDamager()).getShooter() instanceof Player){
                var p = (Player)((Projectile)event.getDamager()).getShooter();
                List<String> scripts = ArathothII.config.getStringList("KetherAction.ATTACK");
                scripts.forEach(s -> s = s
                        .replace("[DAMAGE]", ArathothII.DecimalFormat.format(event.getDamage()))
                        .replace("[NAME]", NameUtils.getEntityName(p,(LivingEntity) event.getEntity())));
                KetherUtils.eval(p,scripts);
            }
        }
    }

    @EventHandler
    public void onExecuteAttr_ATTACK(EntityDamageByEntityEvent event){
        //同时也支持弓箭属性，较上个版本简化写法
        //这不是简洁多了?
            for(BaseAttribute attr : AttributeManager.attributeList){
                if (attr.getType() == StatusType.ATTACK){
                   attr.execute(new EventData(event,attr));
                }
        }
    }

    @EventHandler
    public void onExecuteATTR_DEFENSE(EntityDamageEvent event){
        for(BaseAttribute attr : AttributeManager.attributeList){
            if (attr.getType() == StatusType.DEFENSE){
                attr.execute(new EventData(event,attr));
            }
        }
    }

    @EventHandler
    public void onExecuteATTR_UPDATE(ArathothStatusUpdateEvent event){
        for(BaseAttribute attr : AttributeManager.attributeList){
            if (attr.getType() == StatusType.UPDATE){
                attr.execute(new EventData(event,attr));
            }
        }
    }

}
