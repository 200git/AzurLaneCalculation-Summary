public class ExpUtils
{
	double level_rate=1;//海上传奇和决战方案的增长率
	int [] minExp={0,4000,8000,11000,15000,20000,22000,26000,30000,40000,60000,132000,70000,78000,85000,145000,235000};
	//每个节点的最小经验值
	int [] perExp={100,200,300,400,500,1000,2000,4000,5000,20000,72000,-62000,2000,7000,12000,18000,21000};
	//每两个节点之间的经验增长大小
	int []out_minExp=new int[minExp.length];
	int []out_perExp=new int[perExp.length];
	public ExpUtils(boolean isURorPRY)
	{
		//判断是否为海上传奇或决战方案
		if(isURorPRY)
		{
			for(int i=0;i<perExp.length;i++)
			{
				out_perExp[i]=(int)(1.2*perExp[i]);//扩大最低经验和增长大小
				out_minExp[i]=(int)(1.2*minExp[i]);//缩放率就是恢复原状的关键部分
			}
			level_rate=1.3;//90-100级1.3倍，float不精确
		}
		else
		{
			for(int i=0;i<perExp.length;i++)
			{
				out_perExp[i]=perExp[i];//扩大最低经验和增长大小
				out_minExp[i]=minExp[i];//缩放率就是恢复原状的关键部分
			}
		}
	}
	public long addExp(int l,int s)
	{
		if(l<=s)
		{
			int sum=0;
			for(int i=l+1;i<=s;i++)
			sum+=switchExp(i);
			return sum;
		}
		return 0;
	}
	public int switchExp(int level)
	{
		int formatExp=0;//默认是0
		if(level>0&&level<=41)
		{
			formatExp=(out_minExp[0]+(level-1)*out_perExp[0]);
		}else
		if(level>41&&level<=61)
		{
			formatExp=(out_minExp[1]+(level-41)*out_perExp[1]);
		}else
		if(level>61&&level<=71)
		{
			formatExp=(out_minExp[2]+(level-61)*out_perExp[2]);
		}else
		if(level>71&&level<=81)
		{
			formatExp=(out_minExp[3]+(level-71)*out_perExp[3]);
		}else
		/*
		*由于<80→81>级到<90→91>级这一段在普通舰船是1倍经验，但UR和PRY
		*在<90→91>级到<99→100>级的1.3倍经验段和普通舰船有一个不可避免的交集，
		*所以采取这种办法解决交集问题。
		*/
		if(level>81&&level<=91)
		{
			formatExp=(level_rate>1&&level==91)?
			(int)((minExp[4]+(level-81)*perExp[4])*level_rate):
			(out_minExp[4]+(level-81)*out_perExp[4]);
		}else
		//这部分缩放率起主要作用
		if(level>91&&level<=93)
		{
			formatExp=(int)((minExp[5]*level_rate+perExp[5]*level_rate*(level-91)));
		}else
		if(level>93&&level<=95)
		{
			formatExp=(int)((minExp[6]*level_rate+perExp[6]*level_rate*(level-93)));
		}else
		if(level>95&&level<=96)
		{
			formatExp=(int)((minExp[7]*level_rate+perExp[7]*level_rate*(level-95)));
		}else
		if(level>96&&level<=98)
		{
			formatExp=(int)((minExp[8]*level_rate+perExp[8]*level_rate*(level-96)));
		}else
		if(level>98&&level<=99)
		{
			formatExp=(int)((minExp[9]*level_rate+perExp[9]*level_rate*(level-98)));
		}else
		if(level>99&&level<=100)
		{
			formatExp=(int)((minExp[10]*level_rate+perExp[10]*level_rate*(level-99)));
		}
		//100级以上恢复为1.2倍经验
		if(level>100&&level<=101)
		{
			formatExp=(out_minExp[11]+(level-100)*out_perExp[11]);
		}else
		if(level>101&&level<=105)
		{
			formatExp=(out_minExp[12]+(level-101)*out_perExp[12]);
		}else
		if(level>105&&level<=106)
		{
			formatExp=(out_minExp[13]+(level-105)*out_perExp[13]);
		}else
		if(level>106&&level<=111)
		{
			formatExp=(out_minExp[14]+(level-106)*out_perExp[14]);
		}else
		if(level>111&&level<=116)
		{
			formatExp=(out_minExp[15]+(level-111)*out_perExp[15]);
		}else
		if(level>116&&level<=120)
		{
			formatExp=(out_minExp[16]+(level-116)*out_perExp[16]);
		}
		return formatExp;
	}
}
