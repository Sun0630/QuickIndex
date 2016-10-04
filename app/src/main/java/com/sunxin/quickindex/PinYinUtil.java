package com.sunxin.quickindex;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by sunxin on 2016/10/3.
 *
 */
public class PinYinUtil {

    /**
     * 获取汉字的拼音，会消耗一定的资源，不宜频繁调用
     * @param chinese
     * @return
     */
    public static String getPinYin(String chinese){

        //判断非空
        if (TextUtils.isEmpty(chinese)){
            return null;
        }

        //因为只能对一个汉字转化，所以要把汉字转换成字符数组，然后对每个字符转化，最后拼接起来
        char[] charArray = chinese.toCharArray();
        String pinYin = "";

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);//设置格式为大写
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);//设置声调为不管声调

        //循环判断
        for (int i = 0 ; i < charArray.length;i++){
            //1,过滤空格
            if (Character.isWhitespace(charArray[i])){
                //是空格，直接忽略
                continue;
            }

            //2,判断是否是汉字
            //因为一个汉字占2个字节，一个字节的范围-128~127,所以可以通过这个特征来粗略的判断是否是汉字
            if (charArray[i] > 127){
                //认为是汉字，开始转换成汉语拼音
                try {
                    //有可能会有多音字的存在，所以
                    String[] pinYinArray = PinyinHelper.toHanyuPinyinStringArray(charArray[i], format);

                    if (pinYinArray!=null){
                        //拼接
                        pinYin += pinYinArray[0];//就算有多音字，也取第一个
                    }else {
                        //说明没有找到对应的拼音，就忽略

                    }

                } catch (BadHanyuPinyinOutputFormatCombination combination) {
                    combination.printStackTrace();
                    //转化失败，忽略
                }

            }else {
                //肯定不是汉字，忽略不管
                pinYin+=charArray[i];
            }

        }


        return pinYin;
    }
}
