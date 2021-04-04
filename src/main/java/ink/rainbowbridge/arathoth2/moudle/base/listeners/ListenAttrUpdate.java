package ink.rainbowbridge.arathoth2.moudle.base.listeners;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.moudle.base.manager.AttributeManager;
import io.izzel.taboolib.module.inject.TListener;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 寒雨
 * @Since 2021/4/4 7:55
 */
@TListener
public class ListenAttrUpdate implements Listener {
    private void update(LivingEntity e, ItemStack itemInHand){
        if (e instanceof Player){
            AttributeManager.updatePlayer((Player)e,itemInHand);
        }else{
            AttributeManager.updateEntity(e);
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    void onPlayerItemHeldEvent(PlayerItemHeldEvent event){
        if (event.isCancelled()){return;}
        update(event.getPlayer(),event.getPlayer().getInventory().getItem(event.getNewSlot()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    void onPlayerSwapHandItemsEvent(PlayerSwapHandItemsEvent event){
        if (event.isCancelled()){return;}
        update(event.getPlayer(),event.getMainHandItem());
    }
    @EventHandler(priority = EventPriority.LOWEST)
    void onInventoryCloseEvent(InventoryCloseEvent event){
        update(event.getPlayer(),event.getPlayer().getInventory().getItemInMainHand());
    }
    @EventHandler(priority = EventPriority.LOWEST)
    void onPlayerDropEvent(PlayerDropItemEvent event){
        if (event.isCancelled()){return;}
        update(event.getPlayer(),event.getPlayer().getInventory().getItemInMainHand());
    }
    @EventHandler(priority = EventPriority.LOWEST)
    void onPlayerPickupItemEvent(PlayerPickupItemEvent event){
        if (event.isCancelled()){return;}
        update(event.getPlayer(),event.getPlayer().getInventory().getItemInMainHand());
    }
    @EventHandler(priority = EventPriority.LOWEST)
    void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.isCancelled()){return;}
        update(event.getPlayer(),event.getPlayer().getInventory().getItemInMainHand());
    }
    @EventHandler(priority = EventPriority.LOWEST)
    void onPlayerJoinEvent(PlayerJoinEvent event) {
        update(event.getPlayer(),event.getPlayer().getInventory().getItemInMainHand());
    }
    @EventHandler(priority = EventPriority.LOWEST)
    void onEntitySpawnEvent(CreatureSpawnEvent event)  {
        if (event.isCancelled()){return;}
        update(event.getEntity(),null);
    }
}
