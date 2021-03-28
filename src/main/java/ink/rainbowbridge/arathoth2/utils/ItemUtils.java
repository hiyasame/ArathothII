package ink.rainbowbridge.arathoth2.utils;

import io.izzel.taboolib.module.nms.NMS;
import io.izzel.taboolib.module.nms.nbt.NBTCompound;
import io.izzel.taboolib.module.nms.nbt.NBTList;
import io.izzel.taboolib.util.item.Items;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

/**
 * @Author 寒雨
 * @Since 2021/3/21 15:20
 */
public class ItemUtils {
    public static boolean isApproveItem(ItemStack item) {
        return Items.nonNull(item) && Items.hasLore(item);
    }

    public static boolean isType(ItemStack item, String type) {
        return isApproveItem(item) && ChatColor.translateAlternateColorCodes('&', (String)item.getItemMeta().getLore().get(0)).contains(type) || NMS.handle().loadNBT(item).getOrDefault("Arathoth", new NBTCompound()).asCompound().getOrDefault("Type", new NBTList()).asList().contains(type);
    }
}
