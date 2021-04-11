package ink.rainbowbridge.arathoth2.moudle.hook;


import ink.rainbowbridge.arathoth2.api.ArathothAPI;
import ink.rainbowbridge.arathoth2.moudle.base.enums.PlaceHolderType;
import io.izzel.taboolib.module.locale.TLocale;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

/**
 * @Author 寒雨
 * @Since 2021/4/11 1:29
 */
public class PlaceHolderAPIHook extends PlaceholderExpansion {
    @Override
    public String onPlaceholderRequest(Player p, String params) {
        try {
            switch (params.split(":")[0].toUpperCase()) {
                case "MIN": {
                    return ArathothAPI.getAttributeMap().get(params.split(":")[1]).getPlaceHolder(PlaceHolderType.MIN, p);
                }
                case "MAX": {
                    return ArathothAPI.getAttributeMap().get(params.split(":")[1]).getPlaceHolder(PlaceHolderType.MAX, p);
                }
                case "PERCENT": {
                    return ArathothAPI.getAttributeMap().get(params.split(":")[1]).getPlaceHolder(PlaceHolderType.PERCENT, p);
                }
                case "TOTAL": {
                    return ArathothAPI.getAttributeMap().get(params.split(":")[1]).getPlaceHolder(PlaceHolderType.TOTAL, p);
                }
                default: {
                    return TLocale.asString("Plugin.PLACEHOLDER_EXCEPTION");
                }
            }
        }catch (Throwable t){
            return TLocale.asString("Plugin.PLACEHOLDER_EXCEPTION");
        }
    }

    @Override
    public String getIdentifier() {
        return "ArathothII";
    }

    @Override
    public String getAuthor() {
        return "Rain";
    }

    @Override
    public String getVersion() {
        return "2.0";
    }
}
