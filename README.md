# AzurLaneCalculation-Summary
用于收集碧蓝航线经验计算，升级所需等公式和算法。

### 升级所需经验
>每一级所需经验对比图象
[![前一级提升至此级所需经验对比](https://200git.github.io/EXPcalculation.github.io/modified_per_exp.png)](https://200git.github.io/EXPcalculation.github.io/modified_per_exp.png)
>升级所需总经验对比图象
[![升级所需总经验对比](https://200git.github.io/EXPcalculation.github.io/modified_exp.png)](https://200git.github.io/EXPcalculation.github.io/modified_exp.png)
>其中红色高亮曲线为10.1更新后海上传奇与决战方案舰船（UR、PRY）经验曲线，蓝色高亮曲线为10.1更新后普通舰船（N、R、SR、SSR）经验曲线，而半透明曲线为10.1日更新前舰船经验曲线。

以ExpUtils为基础的HTML版本，可以计算提升等级所需的经验：
[经验计算器](https://200git.github.io/EXPcalculation.github.io/)(未更新！）

## java算法
>10月1日更新调整了100-120级的经验算法并添加了125级新节点。已经进行一次逻辑重整，但计算器中的js代码仍是之前的逻辑。
```java
public class ModifiedExpUtil
{
        //10.1更新对这三组数据有影响
	int [] minExp=
	{0,4000,8000,11000,
		15000,20000,22000,26000,
		30000,40000,60000,132000,
		50000,65000,95000,145000,220000};
	//每个节点的最小经验值
	int [] perExp=
	{100,200,300,400,
		500,1000,2000,4000,
		5000,20000,72000,-82000,
		3000,6000,10000,15000,21000};
	int [] levels=
	{1,41,61,71,
		81,91,93,95,
		96,98,99,100,
		101,106,111,116,121,126};
	boolean isURorPRY;


	//每两个节点之间的经验增长大小
	public ModifiedExpUtil(boolean isURorPRY)
	{
		//判断是否为海上传奇或决战方案
		this.isURorPRY=isURorPRY;
	}
	
	public int getTotal(int level)
	{
		int formatExp=0;
		for(int i=0;i<=level;i++)
		{
			formatExp+=switchExp(i);
		}
		return formatExp;
	}
	
	public int switchExp(int level)
	{
		int formatExp=0;
		for(int i=0;i<(levels.length-1);i++)
		{
			if(level>=levels[i]&&level<levels[i+1])
			{
				int temp=minExp[i]+(level-levels[i])*perExp[i];
				//10.1更新对此处代码并无影响
				formatExp=!isURorPRY?temp:(int)(i>4&&i<=11?temp*1.3:temp*1.2);
				break;
			}
		}
		return formatExp;
	}
}
```
>调用
```java
ModifiedExpUtil meu=new ModifiedExpUtil(true);
//meu=new ModifiedExpUtil(false);
int exp=meu.switchExp(124);
```

### 舰艇演习数据（经验书）
|演习数据类型|提供经验（每单位经验）|
|---|---|
|舰艇演习数据T1[![经验书T1](https://200git.github.io/EXPcalculation.github.io/modified_exp.png)](https://200git.github.io/EXPcalculation.github.io/exp_book_T1.jpg)|3000|
|舰艇演习数据T2[![经验书T2](https://200git.github.io/EXPcalculation.github.io/modified_exp.png)](https://200git.github.io/EXPcalculation.github.io/exp_book_T2.jpg)|15000|

### 后宅经验加成
经验的加成和舒适度的关系如下：
```java
bonus_rate=c_lv/(c_lv+100);
```
其中：`bonus_rate`是加成的百分比，`c_lv`是舒适度。

### 后宅存粮消耗
存粮消耗和舰船数有关，有存粮的条件下每`15s`经验跳动一次，消耗存粮`4n`（n为同时获得经验舰船数）。
这样时间`t`（每小时）和存粮`s`（每单位存粮）的关系如下：
```java
t=s/(4*n)*15/3600
```
根据上边的公式，能得出以下存粮时间关系，~~方便摸鱼~~。

#### 以单艘舰船为基数
|存粮（每单位存粮）|时间（时）|等价关系|
|----|----|----|
|960|1|1船1时|
|1920|2|2船1时|
|2880|3|3船1时|
|3840|4|4船1时|
|4800|5|5船1时|
|5760|6|6船1时|
|11520|12|6船2时|
|23040|24|6船4时|
|34560|36|6船6时|
|46080|48|6船8时|

### 后宅存粮下限及上限（每单位存粮）
后宅存粮下限为`40000`，可供单舰船消耗`41.67小时`；
后宅存粮上限为`90000`，可供单舰船消耗`93.75小时`。

### 后宅存粮经验加成
|名称|食物量|经验加成|加成持续时间（每个）|
|----|----|----|----|
|酸素可乐|1000|-|-|
|秘制冷却水|2000|-|-|
|鱼雷天妇罗|3000|-|-|
|海军咖喱|5000|5%|1h|
|皇家料理|10000|10%|3h|
|满汉全席（~~添加防腐剂~~）|20000|20%|6h|

### 经验跳动（每艘船每时）
>数据来自[大地母亲再忽悠着你]
```java
(l_lv*12+240)*(1+bonus_rate)*(1+buff_bonus_rate)*(mag/n);
```
其中：`l_lv`是指挥官等级，`bonus_rate`是舒适度加成的百分比，`buff_bonus_rate`是食物buff总和加成的百分比，`mag`是舰船倍率，`n`是同时获得经验的舰船数。
舰船倍率和后宅舰船数的关系如下：
|后宅舰船数|舰船倍率|
|----|----|
|1|1.0|
|2|1.8|
|3|2.4|
|4|2.8|
|5|3.2|
|6|3.6|
