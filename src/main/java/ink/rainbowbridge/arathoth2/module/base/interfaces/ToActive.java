package ink.rainbowbridge.arathoth2.module.base.interfaces;

import java.lang.annotation.*;

/**
 * 快捷注册属性注解
 * 原理同TListener
 * @Author 寒雨
 * @Since 2021/3/7 10:24
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ToActive {
}
