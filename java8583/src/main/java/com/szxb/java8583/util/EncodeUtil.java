package com.szxb.java8583.util;

import java.io.ByteArrayOutputStream;

import static java.lang.System.arraycopy;

/**
 * <p>编码转换工具类.如:BCD和HEX</p>
 *
 * @author Magic Joey
 * @version EncodeUtil.java 1.0 @2014-07-10 09:51 $
 */
public class EncodeUtil {

    protected static final char[] HEX = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    protected static final char[] BINARY = new char[]{'0','1'};

    //传入参数为只有01的字符串
    public static byte[] binary(String binaryStr){
        //长度不是8倍数的话，无法知道在左边或右边补零，会引起歧义，导致结果不正确
        if(binaryStr.length()%8!=0){
            throw new IllegalArgumentException("传入的参数长度必须是8的倍数");
        }
        StringBuffer accum = new StringBuffer();
        for(int i=0;i<binaryStr.length();i+=4){
            String temp = binaryStr.substring(i,i+4);
            int value=0;
            for(int j=0;j<4;j++){
                if(temp.charAt(j)=='1'){
                    value+=Math.pow(2, 3 - j);//计算值
                }
            }
            accum.append(HEX[value]);
        }
        return bcd(accum.toString());
    }

    /**
     * 将byte数组转化为String类型的十六进制编码格式
     * 本方法实现的思路是：
     * 1）每位byte数组转换为2位的十六进制数
     * 2）将字节左移4位取得高四位字节数值，获取对应的char类型数组编码
     * 3）将字节与0x0F按位与，从而获取第四位的字节，同样获取编码
     */
    public static String hex(byte[] bParam){
        StringBuilder accum = new StringBuilder();
        for(byte bt:bParam){
             accum.append(HEX[bt>>4&0x0F]);//&0x0F的目的是为了转换负数
             accum.append(HEX[bt&0x0F]);
        }
        return accum.toString();
    }
    
    public static String binary(byte[] bts){
    	StringBuffer accum = new StringBuffer();
    	for(byte bt:bts){
    		accum.append(binary(bt));
    	}
    	return accum.toString();
    }
    
    //本方法修改于Integer.toBinaryString
    //参数的每个字节都会转化为8位2进制字符串，如1会转换为00000001
    private static String binary(byte bt){
    	int num = bt&0xFF;
    	char[] arrayOfChar = new char[8];
		int i = 8;
		for(int times=0;times<8;times++){
			arrayOfChar[(--i)] = BINARY[(num & 0x01)];
			num >>>= 1;
		}
		return new String(arrayOfChar);
    }
    
    /**
     * <p>8421 BCD编码支持</p>
     * @Title: bcd
     * @Description: 8421 BCD编码支持
     * @param data 需要做编码的数据
     * @return byte[] 编码结果
     * @author Ajsgn@foxmail.com
     * @date 2017年8月19日 下午11:54:32
     */
	public static byte[] bcd(String data){
		//空值校验
		if(null == data || "".equals(data.trim()))
			return new byte[0];
		//去首位空格
		data = data.trim();
		//左位补0
		data = data.length() % 2 == 0 ? data : "0" + data;
		//结果数据
		byte[] result = new byte[(data.length() + data.length() % 2) / 2];
		//依次写入数据
		for(int pointer=0,index=0;pointer<data.length();pointer++,index++){
			//左边4bit
			int left = Character.digit(data.charAt(pointer), 16);
			//右边4bit
			int right = Character.digit(data.charAt(++pointer), 16);
			//获得结果
			result[index] = (byte)((left << 4) | right);
		}
		return result;
	}

    /**
     * 十六进制串转化为byte数组
     *
     * @return the array of byte
     */
    public static byte[] hex2byte(String hex)
            throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        char[] arr = hex.toCharArray();
        byte[] b = new byte[hex.length() / 2];
        for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
            String swap = "" + arr[i++] + arr[i];
            int byteint = Integer.parseInt(swap, 16) & 0xFF;
            b[j] = Integer.valueOf(byteint).byteValue();
        }
        return b;
    }

    /**
     * byte 转hex
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 10进制串转为BCD码<br/>
     *
     * @param data 10进制串
     * @return byte[] BCD码
     */
    public static byte[] str2Bcd(String data) {
        return str2Bcd(data, 0);
    }

    public static byte[] str2Bcd(String data, int flag, int len) {
        int dataLen = data.length();
        if (dataLen < len) {
            int addLen = len - dataLen;
            for (int i = 0; i < addLen; i++) {
                data = "0" + data;
            }
        }
        return str2Bcd(data, flag);
    }


    /**
     * 10进制串转为BCD码<br/>
     *
     * @param data 10进制串
     * @param flag 0:默认左补0，1右补0
     * @return
     */
    public static byte[] str2Bcd(String data, int flag) {
        if (data.length() == 0) {
            return new byte[0];
        }

        String str = data;
        // 奇数个数字需左补零
        if (str.length() % 2 != 0) {
            str = flag == 0 ? "0" + str : str + "0";
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        char[] cs = str.toCharArray();
        for (int i = 0; i < cs.length; i += 2) {
            int high = cs[i] - 48;
            int low = cs[i + 1] - 48;
            baos.write(high << 4 | low);
        }
        return baos.toByteArray();
    }


    /**
     * 合并byte数组
     *
     * @param datas .
     * @return .
     */
    public static byte[] mergeByte(byte[]... datas) {
        int length = 0;
        byte[] endData = new byte[2048];
        for (byte[] data : datas) {
            arraycopy(data, 0, endData, length, data.length);
            length += data.length;
        }
        byte[] data = new byte[length];
        arraycopy(endData, 0, data, 0, length);
        return data;
    }

}
