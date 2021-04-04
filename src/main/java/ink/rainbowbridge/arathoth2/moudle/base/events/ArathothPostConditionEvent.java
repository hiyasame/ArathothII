package ink.rainbowbridge.arathoth2.moudle.base.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

/**
 * @Author 寒雨
 * @Since 2021/4/4 8:51
 */
public class ArathothPostConditionEvent extends Event {
    public final static HandlerList handlerList = new HandlerList();
    @Getter
    private Player player;
    @Getter
    @Setter
    private List<String> values;

    public ArathothPostConditionEvent(Player p,List<String> values){
        this.player = p;
        this.values = values;
    }
    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
