package Beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类名：Star
 * 作者：20181002846 左雨航
 * 用途：把某一鬼畜明星的全部语录记录在这里用来序列化。
 **/
public class Star implements Serializable{
    List<Line> lineList=new ArrayList<Line>();

    public Star(List<Line> lineList) {
        this.lineList = lineList;
    }

    public List<Line> getLineList() {
        return lineList;
    }
}
