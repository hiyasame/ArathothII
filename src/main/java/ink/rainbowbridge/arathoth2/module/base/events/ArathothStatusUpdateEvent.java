package ink.rainbowbridge.arathoth2.module.base.events;

import ink.rainbowbridge.arathoth2.module.base.data.StatusData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;

/**
 * @Author 寒雨
 * @Since 2021/3/12 22:13
 */
public class ArathothStatusUpdateEvent extends Event {
    public final static HandlerList handlerList = new HandlerList();
    @Getter
    private LivingEntity executor;
    @Getter
    @Setter
    private HashMap<String,StatusData> data;
    public ArathothStatusUpdateEvent(LivingEntity executor, HashMap<String, StatusData> data){
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
}
