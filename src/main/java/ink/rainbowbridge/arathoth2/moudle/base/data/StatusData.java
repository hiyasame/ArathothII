package ink.rainbowbridge.arathoth2.moudle.base.data;

import ink.rainbowbridge.arathoth2.ArathothII;
import ink.rainbowbridge.arathoth2.api.ArathothAPI;
import ink.rainbowbridge.arathoth2.moudle.base.enums.PlaceHolderType;
import io.izzel.taboolib.util.serialize.TSerializable;
import io.izzel.taboolib.util.serialize.TSerializeCustom;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * @Author 寒雨
 * @Since 2021/3/12 22:22
 */
public class StatusData implements TSerializable {
    @Getter
    @Setter
    private Double min;
    @Getter
    @Setter
    private Double max;
    @Getter
    @Setter
    private Double percent;

    /**
     * 初始化构造方法
     */
    public StatusData(){
        this.min = 0.0D;
        this.max = 0.0D;
        this.percent = 0.0D;
    }

    /**
     * 初始赋值构造方法
     *
     * @param min 最小值
     * @param max 最大值
     * @param percent 百分比
     */
    public StatusData(@NotNull Double min, @NotNull Double max, @NotNull Double percent){
        this.min = min;
        this.max = max;
        this.percent = percent;
    }

    /**
     * 解析data获得可以直接使用的属性值
     *
     * @return 结果
     */
    public Double solveData(){
        if(this.min.equals(this.max)){
            return min*(1+percent/100);
        }
        else{
            return ArathothAPI.getRandom(min,max)*(1+percent/100);
        }
    }

    /**
     * 转换为PlaceHolder变量
     *
     * @param type 转换的类型
     * @return Placeholder
     */
    public String getPlaceHolder(PlaceHolderType type){
        switch (type){
            case MIN:{
                return ArathothII.DecimalFormat.format(min);
            }
            case MAX:{
                return ArathothII.DecimalFormat.format(max);
            }
            case PERCENT:{
                return ArathothII.DecimalFormat.format(percent);
            }
            case TOTAL:{
                if (!(min.equals(max))){
                    return ArathothII.DecimalFormat.format(min*(1+percent/100))+"-"+ArathothII.DecimalFormat.format(max*(1+percent/100));}
                else{
                    return ArathothII.DecimalFormat.format(min*(1+percent/100));
                }
            }
        }
        return null;
    }


    public StatusData importData(StatusData data){
        return new StatusData(min+data.getMin(),max+data.getMax(),percent+data.getPercent());
    }

    /**
     * 纠正负值
     */
    public StatusData FixZeroValue(){
        Double MIN = min;
        Double MAX = max;
        Double PCT = percent;
        if (max < 0){
            MAX = 0.0D;
        }
        if (min < 0){
            MIN = 0.0D;
        }
        if (percent < 0){
            PCT = 0.0D;
        }
        return new StatusData(MIN,MAX,PCT);
    }

    /**
     * 是否为0值data
     * @return boolean
     */
    public boolean isZeroData(){
        return min == 0.0D && max == 0.0D;
    }

}
