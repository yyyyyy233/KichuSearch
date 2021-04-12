package UI;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 类名：SearchWindow
 * 作者：20181002846 左雨航
 * 用途：
 **/
public class SearchWindow {
    Stage stage=new Stage();

    public void setSelectedStar(List<Integer> selectedStar) {
        this.selectedStar = selectedStar;
    }



    List<String> selectedStartmp =new ArrayList<>();//tmp:缓存，看谁被选
    List<Integer> selectedStar =new ArrayList<>();//没有tmp:非缓存，看谁真被选。一次方法结束后，返回（包括之前）被真选中的元素。搜索功能的明星选择以此为准。

    public Stage getStage() {
        return stage;
    }

    boolean inSearchMode;//看是否在搜索状态

    public List<Integer> getSelectedStar() {
        return selectedStar;
    }

    public SearchWindow(List kichuStarList)
    {

        inSearchMode=false;
        HBox hBoxrightSearch=new HBox();
        TextField textFieldinputSearch=new TextField();
        Button buttonSearchStar=new Button("🔍");
        VBox vBoxright=new VBox();
        ListView<String> listViewKichuStarName=new ListView<String>();

        Button buttonapply=new Button("确定");
        hBoxrightSearch.getChildren().addAll(textFieldinputSearch,buttonSearchStar);

        vBoxright.getChildren().addAll(hBoxrightSearch,new Label("鬼畜明星范围"),listViewKichuStarName,buttonapply);
        vBoxright.setSpacing(10);
        vBoxright.setPadding(new Insets(10));

        listViewKichuStarName.setItems(FXCollections.observableArrayList(kichuStarList));

        buttonSearchStar.setOnAction(e->{
            inSearchMode=true;
            selectedStartmp.clear();
            Iterator kichustarIterator=kichuStarList.iterator();
            //List<String> kichuStartmp=new ArrayList<String>();
            while (kichustarIterator.hasNext())
            {

                String nametmp= (String) kichustarIterator.next();
                String pattern=".*"+textFieldinputSearch.getText()+".*";
                boolean matches= Pattern.matches(pattern,nametmp);
                if (matches)
                {
                    selectedStartmp.add(nametmp);
                }
            }
            listViewKichuStarName.setItems(FXCollections.observableArrayList(selectedStartmp));
            if (kichuStarList.contains(textFieldinputSearch.getText()))
            {

                listViewKichuStarName.scrollTo(selectedStartmp.indexOf(textFieldinputSearch.getText()));//这一方法操纵了滚轮的滚动条。
                listViewKichuStarName.getSelectionModel().selectIndices(selectedStartmp.indexOf(textFieldinputSearch.getText()));

            }
            else if (selectedStartmp.isEmpty())
            {
                Alert alertsearchstarerror=new Alert(Alert.AlertType.ERROR);
                alertsearchstarerror.setTitle("反正就是找不到");
                alertsearchstarerror.setHeaderText("你要的明星搜不到·-·");
                alertsearchstarerror.setContentText("要按照英文格式找哦~直接搜中文搜不到哒");
                alertsearchstarerror.showAndWait();
            }
        });

        textFieldinputSearch.setOnKeyReleased(e->{
            selectedStartmp.clear();
            if (textFieldinputSearch.getText().equals(""))
            {
                inSearchMode=false;
                listViewKichuStarName.setItems(FXCollections.observableArrayList(kichuStarList));
            }
        });

        buttonapply.setOnAction(e->{//如果当时在搜索状态（显示的不是完整的鬼畜明星列表），就把选中的值和原值进行比对返回下标，否则直接返回原值。
            if (inSearchMode)
            {
                //定义string的list，通过index往回寻找
                List selectedTmp=listViewKichuStarName.getItems();
                Iterator selectedTmpIterator=selectedTmp.iterator();
                while (selectedTmpIterator.hasNext())
                {
                    String next=(String)selectedTmpIterator.next();
                    selectedStar.add(kichuStarList.indexOf(next));
                }
            }
            else
            {
                selectedStar.addAll(listViewKichuStarName.getSelectionModel().getSelectedIndices());
                System.out.println(selectedStar);
            }
            selectedStar=selectedStar.stream().distinct().collect(Collectors.toList());
            //去重
        });
        stage.setScene(new Scene(vBoxright));

    }
    public void show()
    {
        stage.showAndWait();
    }
    public void clear()
    {
        selectedStar.clear();
    }
}
