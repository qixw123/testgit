package cn.redcdn.jec.common.util;

import java.text.NumberFormat;

public class NumberUtil
{
	/**
	 * 
	 * 获取百分比概率
	 * @param molecule 分子
	 * @param denominator 分母
	 * @param num 小数点位数
	 * 
	 * @return 获取百分比
	 */
	public static String getPercentage(long molecule, long denominator, int num) {
	    // 如果分母为空
	   if (denominator == 0) {
		   denominator = 1;
	   }
	   double mo = molecule;
	   double de = denominator;
	   NumberFormat numberFormat  =  NumberFormat.getPercentInstance();
	   numberFormat.setMinimumFractionDigits(0);
	   return numberFormat.format(mo/de);
	}
}
