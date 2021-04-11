package ink.rainbowbridge.arathoth2.utils;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

/**
 * @Author 寒雨
 * @Since 2021/4/11 0:58
 */
public class FileUtils {
    /**
     * 获取插件所有类
     *
     * @return {@link List<Class>}
     */
    public static List<Class> getClasses(Plugin plugin) {
        List<Class> classes = new ArrayList<>();
        URL url = plugin.getClass().getProtectionDomain().getCodeSource().getLocation();
        try {
            File src;
            try {
                src = new File(url.toURI());
            } catch (URISyntaxException e) {
                src = new File(url.getPath());
            }
            new JarFile(src).stream().filter(entry -> entry.getName().endsWith(".class")).forEach(entry -> {
                String className = entry.getName().replace('/', '.').substring(0, entry.getName().length() - 6);
                try {
                    classes.add(Class.forName(className, false, plugin.getClass().getClassLoader()));
                } catch (Throwable ignored) {
                }
            });
        } catch (Throwable ignored) {
        }
        return classes;
    }
}
