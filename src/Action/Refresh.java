package Action;

import Beans.Line;
import Beans.Star;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.*;
import java.util.*;

/**
 * 类名：Refresh
 * 作者：
 * 用途：当鬼畜明星的台词有所修改时候，进行刷新
 **/
public class Refresh {
    public  List refresh(File file,Map<String,List<String>> dzyMap) throws IOException, BadHanyuPinyinOutputFormatCombination {
        File[] fileall=file.listFiles();
        int i;
        List<String> refreshList=new ArrayList<String>();
        for(i=0;i<fileall.length;i++)
        {
            if(fileall[i].getName().endsWith(".txt"))
            {
                refreshList.add(fileall[i].getName().replaceAll(".txt",""));
            }

        }
        Formout(refreshList,dzyMap);
        return refreshList;
    }

    public void Formout(List kichuStar,Map<String,List<String>> dzyMap) throws IOException, BadHanyuPinyinOutputFormatCombination {
        String title="";
        Iterator<String> indexIterator=kichuStar.iterator();

        while (indexIterator.hasNext())
        {
            List<Line> lineListforOne=new ArrayList<Line>();
            String indexstar=indexIterator.next();
            int whatline=1;//指的是当前已经到达这一素材的第几行
            //File star=new File(indexstar+".txt");
            //Scanner starScanner=new Scanner(star);
            BufferedReader starScanner=new BufferedReader(new InputStreamReader((new FileInputStream("src\\Stars\\"+indexstar+".txt")),"utf-8"));
            String thisLine="";
            while ((thisLine=starScanner.readLine())!=null)
            {

                if (thisLine.indexOf("#")!=-1)
                {
                    title=thisLine.replaceAll("#","");
                    whatline=1;
                }
                else
                {
                    if (title!="")
                    {
                        char[] sen=thisLine.toCharArray();
                        //把输入界面的中文全部转化为拼音形式
                        String senpinyin=topinyin(sen,dzyMap);

                        //记录入有关数据结构中
                       lineListforOne.add(new Line(title,Arrays.asList(  senpinyin.split(" ")),thisLine,whatline));
                        whatline++;

                    }
                }
            }
            //形成类并序列化
            Star star=new Star(lineListforOne);
            FileOutputStream fileOutputStream=new FileOutputStream(indexstar+".ser");
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(star);
            objectOutputStream.close();
            fileOutputStream.close();
        }

    }

    public String topinyin(char[] sen,Map<String,List<String>> dzymap) throws BadHanyuPinyinOutputFormatCombination {
        int i;
        String result="";
        boolean chinesekg=false;//目的：希望能把例如rap，baby一类词单独隔开。防止出现raplan2等情况。
        for (i=0;i<sen.length;i++)
        {

            HanyuPinyinOutputFormat hanyuPinyinOutputFormat=new HanyuPinyinOutputFormat();
            hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
            hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
            String[] resulttmp= PinyinHelper.toHanyuPinyinStringArray(sen[i],hanyuPinyinOutputFormat);
            if (resulttmp==null)
            {
                result+=(sen[i]);
                chinesekg=true;
            }
            else if (resulttmp.length==1)
            {
                if (chinesekg)
                {
                    result+=" ";
                    chinesekg=false;
                }
                result+=resulttmp[0];
                result+=" ";
            }
            else//多音字
            {
                if (chinesekg)
                {
                    result+=" ";
                    chinesekg=false;
                }
                String sentmp=String.valueOf(sen);//toString的结果是一个对象地址
                int j;
                boolean flag=false;//是否能在多音字字典里面找到值
                for (j=0;j<resulttmp.length;j++)
                {
                    
                    try {
                        String resulttmp2=resulttmp[j].substring(0,resulttmp[j].length()-1);
                        System.out.println(resulttmp2);
                        if (dzymap.get(resulttmp2)==null)
                        {
                            break;
                        }
                        else if (dzymap.get(resulttmp2).contains(sentmp.substring(i,i+1)))//可能有些多音字的注音在dzymap里面根本没有，你找不到对应的key。
                        {
                            result+=resulttmp[j];flag=true;break;
                        }
                        else if ( i>0 &&dzymap.get(resulttmp2).contains(sentmp.substring(i-1,i+1)))
                        {
                            result+=resulttmp[j];flag=true;break;
                        }
                        else if (i<resulttmp.length-1 && dzymap.get(resulttmp2).contains(sentmp.substring(i,i+2)))
                        {
                            result+=resulttmp[j];flag=true;break;
                        }
                        else  if (i>0 && i<resulttmp.length-1 && dzymap.get(resulttmp2).contains(sentmp.substring(i-1,i+2)))
                        {
                            result+=resulttmp[j];flag=true;break;
                        }
                        else if (i>1&& dzymap.get(resulttmp2).contains(sentmp.substring(i-2,i+1)))
                        {
                            result+=resulttmp[j];flag=true;break;
                        }
                        else if (i<resulttmp.length-2 && dzymap.get(resulttmp2).contains(sentmp.substring(i,i+3)))
                        {
                            result+=resulttmp[j];flag=true;break;
                        }
                        else if (i>0 && i<resulttmp.length-2 && dzymap.get(resulttmp2).contains(sentmp.substring(i-1,i+3)))
                        {
                            result+=resulttmp[j];flag=true;break;
                        }


                    }
                    catch (Exception e)
                    {

                        System.out.println("Exception:"+resulttmp);
                        System.out.println(sentmp.toCharArray());
                    }

                }
                if (!flag)
                {
                    result+=resulttmp[0];//否则，取第一个值作为默认值
                }
                result+=" ";
            }

           // System.out.println(result);
        }
        return result;
    }
}
