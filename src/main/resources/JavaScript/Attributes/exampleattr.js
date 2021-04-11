importClass(org.bukkit.scheduler.BukkitRunnable);

//必填变量
var type = 'ATTACK';
var priority = 10;
//选填变量，不填为默认值
var isZeroExecute = false;
var isPercentAttribute = false;
var description = 'js属性例子';
//执行属性方法，必填，就是是个空属性也要整个空的function上去
//不然报错
function onExecute(eventData){
    var event = eventData.getAttackEvent();
    //简单的增加物理伤害
    event.setDamage(event.getDamage()+eventData.getExecutorData().solveData());
}
//创建runnable的例子,运行时的插件参数直接用jsUtils里的getPlugin()方法获取
var r = new org.bukkit.scheduler.BukkitRunnable(){
     run: function(){
         print('fuck you');
      }
}