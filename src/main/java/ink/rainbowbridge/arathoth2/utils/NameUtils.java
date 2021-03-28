package ink.rainbowbridge.arathoth2.utils;

import io.izzel.taboolib.module.i18n.version.I18nOrigin;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

/**
 * @Author 寒雨
 * @Since 2021/3/21 9:03
 */
public class NameUtils {
    public static String getEntityName(Player p, LivingEntity entity){
        if (entity.getCustomName() == null){
            return I18nOrigin.INSTANCE.getName(p,entity);
        }
        return entity.getCustomName();
    }
}
