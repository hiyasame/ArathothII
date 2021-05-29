package ink.rainbowbridge.arathoth2.module.base.events;

import ink.rainbowbridge.arathoth2.api.ArathothAPI;
import ink.rainbowbridge.arathoth2.module.base.abstracts.BaseAttribute;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 概率属性执行事件
 * @Author 寒雨
 * @Since 2021/3/13 8:36
 */
public class ArathothChanceAttrExecuteEvent extends Event implements Cancellable {
    public final static HandlerList handlerList = new HandlerList();
    @Getter
    private LivingEntity executor;
    @Getter
    @Setter
    private BaseAttribute attr;
    private boolean isCancelled = false;

    public ArathothChanceAttrExecuteEvent(LivingEntity executor , BaseAttribute attr ,Double rate){
        this.executor = executor;
        this.attr = attr;
        this.isCancelled = true;
        if (ArathothAPI.Chance(rate)){
            this.isCancelled = false;
        }
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.isCancelled = b;
    }
}
