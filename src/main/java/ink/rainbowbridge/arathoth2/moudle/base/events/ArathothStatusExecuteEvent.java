package ink.rainbowbridge.arathoth2.moudle.base.events;

import ink.rainbowbridge.arathoth2.moudle.base.abstracts.BaseAttribute;
import ink.rainbowbridge.arathoth2.moudle.base.data.EventData;
import ink.rainbowbridge.arathoth2.moudle.base.data.StatusData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * @Author 寒雨
 * @Since 2021/3/13 8:24
 */
public class ArathothStatusExecuteEvent extends Event implements Cancellable {
    public final static HandlerList handlerList = new HandlerList();
    @Getter
    private LivingEntity executor; // 执行者
    @Getter
    @Setter
    private BaseAttribute attr;
    @Getter
    @Setter
    private StatusData data; // 执行者data
    @Getter
    @Setter
    private EventData eventData;
    private boolean isCancelled = false;

    public ArathothStatusExecuteEvent(LivingEntity executor, BaseAttribute attr, StatusData data, EventData eventData){
        this.attr = attr;
        this.executor = executor;
        this.data = data;
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
