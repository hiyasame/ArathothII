package ink.rainbowbridge.arathoth2.moudle.base.abstracts;

/**
 * v2新增特性，牛逼物品特性
 * @Author 寒雨
 * @Since 2021/4/11 0:55
 */
public abstract class ItemFeature {
    public String getName(){
        return this.getClass().getSimpleName();
    }
}
