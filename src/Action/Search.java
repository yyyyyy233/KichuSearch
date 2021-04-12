package Action;

import Beans.Line;
import Beans.Star;

import java.io.*;
import java.util.*;

/**
 * 类名：Search
 * 作者：20181002846 左雨航
 * 用途：
 **/
public class Search {
    public String Search4words(List kichustar,List<Integer> listIndex,String searchsen) throws IOException, ClassNotFoundException {
        String result="搜索结果";
        String[] searchWord=searchsen.split(" ");
        List<Star> star4serach=new ArrayList<Star>();
        int i;
        Iterator<Integer> listIterator=listIndex.iterator();
        while (listIterator.hasNext())
        {
            String starToDeserlize=kichustar.get(listIterator.next())+".ser";
            Star startoSerachtmp= (Star) new ObjectInputStream(new FileInputStream(starToDeserlize)).readObject();
            star4serach.add(startoSerachtmp);
        }
        for (i=0;i<searchWord.length;i++)
        {
            //搜索标准：名场面的优先（需要进行指定），有连词的随后，最后是单个词（目前尚未实现）
            result+="————————————————————————————————\n";
            result+=(searchWord[i]+"的搜索结果：");
            boolean haveanswer=false;//能不能搜到
            Iterator search=star4serach.iterator();
            while (search.hasNext())
            {
                Star starseraching= (Star) search.next();
                List listLinetmp=starseraching.getLineList();
                Iterator<Line> lineIterator=listLinetmp.iterator();
                while (lineIterator.hasNext())
                {
                    Line linetmp=lineIterator.next();
                    List<String> linetmpSayList=linetmp.getSayList();
                    if (linetmpSayList.contains(searchWord[i]))
                    {
                        result+=(linetmp.getTitle()+"素材:"+linetmp.getCol()+"行,第"+(linetmp.getSayList().indexOf(searchWord[i])+1)+"个\n");
                        result+=linetmp.getHanzisayList()+"\n";
                        haveanswer=true;
                    }
                }
            }
            if (!haveanswer)
            {
                result+="这个字搜不到吖~是不是格式错了呢(如果没有的话就靠你发挥了·-·)\n";
            }
        }
        //System.out.println(result);
        //System.out.println(result.replaceAll("——",""));
        if (result.replaceAll("——","").equals("搜索结果"))
        {

            return "找不到搜索内容啦~你到底要迫害谁呐·-·";
        }

        return result;
    }


}
