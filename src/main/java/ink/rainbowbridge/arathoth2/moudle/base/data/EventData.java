package ink.rainbowbridge.arathoth2.moudle.base.data;

import ink.rainbowbridge.arathoth2.api.ArathothAPI;
import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseAttribute;
import ink.rainbowbridge.arathoth2.moudle.base.enums.StatusType;
import ink.rainbowbridge.arathoth2.moudle.base.events.ArathothStatusUpdateEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * 事件data
 * @Author 寒雨
 * @Since 2021/3/13 8:44
 */
public class EventData {
    @Getter
    private StatusType type;
    @Getter
    private EntityDamageByEntityEvent attackEvent;
    @Getter
    private EntityDamageEvent defenseEvent;
    @Getter
    private ArathothStatusUpdateEvent updateEvent;
    @Getter
    private LivingEntity executor;
    @Getter
    private LivingEntity attacker;
    @Getter
    private LivingEntity victim;
    @Getter
    @Setter
    private StatusData executorData;
    @Getter
    @Setter
    private StatusData attackerData;
    @Getter
    @Setter
    private StatusData victimData;
    @Getter
    private Projectile projectile;
    @Getter
    private StatusData projectileData;
    @Getter
    private Event customEvent;

    /**
     * 攻击类型data构造
     */
    public EventData(EntityDamageByEntityEvent attackEvent, BaseAttribute attr){
        this.type = StatusType.ATTACK;
        this.attackEvent = attackEvent;
        this.victim = (LivingEntity)attackEvent.getEntity();
        this.victimData = ArathothAPI.getEntityAttrData(victim,attr);
        if (attackEvent.getDamager() instanceof Projectile){
            this.projectile = ((Projectile)attackEvent.getDamager());
            this.projectileData = ArathothAPI.getEntityAttrData(projectile,attr);
            if (projectile.getShooter() instanceof LivingEntity){
                this.executor = (LivingEntity)projectile.getShooter();
                this.attacker = executor;
                this.executorData = ArathothAPI.getEntityAttrData(executor,attr);
                this.attackerData = executorData;
            }
        }else if (attackEvent.getDamager() instanceof LivingEntity){
            this.attacker = (LivingEntity)attackEvent.getDamager();
            this.executor = attacker;
            this.executorData = ArathothAPI.getEntityAttrData(executor,attr);
            this.attackerData = executorData;
        }
    }

    /**
     * 防御类属性事件data
     * @param defenseEvent
     * @param attr
     */
    public EventData(EntityDamageEvent defenseEvent , BaseAttribute attr){
        this.type = StatusType.DEFENSE;
        this.defenseEvent = defenseEvent;
        this.victim = (LivingEntity) defenseEvent.getEntity();
        this.victimData = ArathothAPI.getEntityAttrData(victim,attr);
        this.executorData = victimData;
        this.executor = victim;
    }

    public EventData(ArathothStatusUpdateEvent updateEvent , BaseAttribute attr){
        this.type = StatusType.UPDATE;
        this.updateEvent = updateEvent;
        this.executor = updateEvent.getExecutor();
        this.executorData = ArathothAPI.getEntityAttrData(executor,attr);
    }

    public EventData(Event event , BaseAttribute attr){
        this.type = StatusType.CUSTOM;
        this.customEvent = event;
    }
}
