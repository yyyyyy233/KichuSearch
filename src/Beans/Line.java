package Beans;

import java.io.Serializable;
import java.util.List;

/**
 * 类名：Line
 * 作者：20181002846 左雨航
 * 用途：行的实体类。代表的是对于每个鬼畜明星的鬼畜语录的一行，这里是为了方便定义而显示上下文，可以快速的帮up主找到实际行所在位置
 **/
public class Line implements Serializable{
    String title;//鬼畜素材的来源，如同样是五五开的语录，title=17cards代表该语录来自卢本伟斗地主素材（17张牌你能秒我？） title=waigua代表该语录来自卢本伟对外挂的澄清。
    List<String> sayList;//具体说了啥,拼音形式
    String hanzisayList;
    int col;



    public Line(String title, List<String> sayList, String hanzisayList, int col) {
        this.title = title;
        this.sayList = sayList;
        this.hanzisayList = hanzisayList;
        this.col = col;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getSayList() {
        return sayList;
    }

    public void setSayList(List<String> sayList) {
        this.sayList = sayList;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getHanzisayList() {
        return hanzisayList;
    }

    public void setHanzisayList(String hanzisayList) {
        this.hanzisayList = hanzisayList;
    }
}
