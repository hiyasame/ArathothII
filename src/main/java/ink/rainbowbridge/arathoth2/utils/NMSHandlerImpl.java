package ink.rainbowbridge.arathoth2.utils;

import io.izzel.taboolib.Version;
import io.izzel.taboolib.util.Reflection;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * @Author 寒雨
 * @Since 2021/4/24 12:31
 */
public class NMSHandlerImpl extends NMSHandler{
    private Field tagListField;
    private Field intArrayDataField;
    private Field byteArrayDataField;
    private Field longArrayDataField;

    public NMSHandlerImpl() {
        try {
            tagListField = Reflection.getField(NBTTagList.class, true, "list");
            intArrayDataField = Reflection.getField(NBTTagIntArray.class, true, "data");
            byteArrayDataField = Reflection.getField(NBTTagByteArray.class, true, "data");
            // v1.12+
            if (Version.isAfter(Version.v1_13)) {
                longArrayDataField = NBTTagLongArray.class.getDeclaredFields()[0];
                longArrayDataField.setAccessible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void sendItemNBT(@NotNull Player player, @NotNull ItemStack itemStack) {
        Object nmsItem = CraftItemStack.asNMSCopy(itemStack);
        Object itemTag = ((net.minecraft.server.v1_16_R1.ItemStack) nmsItem).hasTag() ? ((net.minecraft.server.v1_16_R1.ItemStack) nmsItem).getTag() : new NBTTagCompound();
        sendItemNBT(player, "NBT &8->&f", itemTag, 0);
    }

    private void sendItemNBT(Player player, String key, Object nbtBase, int node) {
        if (nbtBase instanceof NBTTagCompound) {
            Set<String> keys = Version.isAfter(Version.v1_13) ? ((net.minecraft.server.v1_13_R1.NBTTagCompound) nbtBase).getKeys() : ((NBTTagCompound) nbtBase).c();
            if (keys.isEmpty()) {
                Message.INSTANCE.send(player, getNodeSpace(node) + key + " {}");
            } else {
                Message.INSTANCE.send(player, getNodeSpace(node) + key + (key.equals("-") ? " {" : ""));
                for (String subKey : keys) {
                    sendItemNBT(player, subKey + ":", ((NBTTagCompound) nbtBase).get(subKey), node + 1);
                }
                if (key.equals("-")) {
                    Message.INSTANCE.send(player, getNodeSpace(node) + "}");
                }
            }
        } else if (nbtBase instanceof NBTTagList) {
            try {
                List tagList = (List) tagListField.get(nbtBase);
                if (tagList.isEmpty()) {
                    Message.INSTANCE.send(player, getNodeSpace(node) + key + " []");
                } else {
                    Message.INSTANCE.send(player, getNodeSpace(node) + key);
                    tagList.forEach(aTagList -> sendItemNBT(player, "-", (NBTBase) aTagList, node));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (nbtBase instanceof NBTTagIntArray) {
            try {
                int[] array = (int[]) intArrayDataField.get(nbtBase);
                if (array.length == 0) {
                    Message.INSTANCE.send(player, getNodeSpace(node) + key + " []");
                } else {
                    Message.INSTANCE.send(player, getNodeSpace(node) + key);
                    for (int var : array) {
                        Message.INSTANCE.send(player, getNodeSpace(node) + "- &f" + var);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (nbtBase instanceof NBTTagByteArray) {
            try {
                byte[] array = (byte[]) byteArrayDataField.get(nbtBase);
                if (array.length == 0) {
                    Message.INSTANCE.send(player, getNodeSpace(node) + key + " []");
                } else {
                    Message.INSTANCE.send(player, getNodeSpace(node) + key);
                    for (byte var : array) {
                        Message.INSTANCE.send(player, getNodeSpace(node) + "- &f" + var);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (nbtBase.getClass().getSimpleName().equals("NBTTagLongArray")) {
            try {
                long[] array = (long[]) longArrayDataField.get(nbtBase);
                if (array.length == 0) {
                    Message.INSTANCE.send(player, getNodeSpace(node) + key + " []");
                } else {
                    Message.INSTANCE.send(player, getNodeSpace(node) + key);
                    for (long var : array) {
                        Message.INSTANCE.send(player, getNodeSpace(node) + "- &f" + var);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            Message.INSTANCE.send(player, getNodeSpace(node) + key + " &f" + (nbtBase == null ? "" : nbtBase instanceof NBTTagString ? "&7\"&r" + nbtBase.toString().substring(1, nbtBase.toString().length() - 1) + "&7\"" : nbtBase.toString()));
        }
    }
}
