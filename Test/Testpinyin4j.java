import Action.Refresh;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 类名：Testpinyin4j
 * 作者：20181002846 左雨航
 * 用途：
 **/
public class Testpinyin4j {
    HanyuPinyinOutputFormat hanyuPinyinOutputFormat=new HanyuPinyinOutputFormat();

    @Test
    public void test() throws BadHanyuPinyinOutputFormatCombination {
        hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);//输出 拼音+字母（1-4）
        hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        String hanzi="全民制作人大家好，我是练习时长两年半的个人练习生蔡徐坤，来自长沙，喜欢唱跳rap篮球。";
        char[] hanzichar=hanzi.toCharArray();
        int i;
        for (i=0;i<hanzichar.length;i++)
        {

            String[] pinyinone= PinyinHelper.toHanyuPinyinStringArray(hanzichar[i],hanyuPinyinOutputFormat);//非汉字无法识别本句子（比如说句号）
            if (pinyinone==null)
            {
                continue;
            }
            if (pinyinone.length>0)
            {
                System.out.print(pinyinone[0]+" ");

            }
            else
            {
                System.out.println(" 这个东西识别不了 ");
            }
        }
    }
    @Test
    public void test2() throws IOException, ClassNotFoundException, BadHanyuPinyinOutputFormatCombination {
        Map<String,List<String>> duoyinziDic=(Map<String, List<String>>) new ObjectInputStream(new FileInputStream("duoyinzicidian.ser")).readObject();
        System.out.println(new Refresh().topinyin("全民制作人大家好，我是练习时长两年半的个人练习生蔡徐坤，来自长沙，喜欢唱跳rap篮球。".toCharArray(),duoyinziDic));
    }
    @Test
    public void  test3()
    {
        String s="D:\\gdu\\java\\kikchusearch\\kangxi.ser";
        System.out.println(s.lastIndexOf("\\"));
        System.out.println(s.substring(0,24));
    }
}
