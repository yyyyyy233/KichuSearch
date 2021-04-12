package Action;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * 类名：Autoinput
 * 作者：
 * 用途：输入时候的窗体界面
 **/
public class Autoinput {
    String result;
    VBox mainpane=new VBox();
    HBox hBoxbtn=new HBox();
    Button buttonok=new Button("确定");
    TextField tfin=new TextField();
    Stage mainstage=new Stage();
    public Autoinput()
    {
        hBoxbtn.getChildren().addAll(buttonok);
        hBoxbtn.setPadding(new Insets(10));
        hBoxbtn.setAlignment(Pos.BASELINE_RIGHT);
        mainpane.getChildren().addAll(new Label("示例：输入pa 输出pa1 pa2 pa3 pa4 pa5"),tfin,buttonok);
        mainpane.setSpacing(10);
        mainpane.setPadding(new Insets(10));
        mainstage.setScene(new Scene(mainpane));

        buttonok.setOnAction(e->{
            if (tfin.getText().trim().length()>6)
            {
                new Alert(Alert.AlertType.ERROR,"长度超过拼音长度").showAndWait();
            }
            else if (tfin.getText().trim().matches(".*([^a-z])+.*"))//左右两个星号不加，则诸如qwer1一类的会通过审核，中间加号是为了防止误伤
            {
                new Alert(Alert.AlertType.ERROR,"输入的似乎不是拼音·w·").showAndWait();
            }
            else if (tfin.getText().trim().equals("") || (tfin.getText().trim().equals(null)))
            {
                //do nothing
            }
            else
            {
                String beforemanupro =tfin.getText().trim();
                String aftermanupro=beforemanupro+"1 "+beforemanupro+"2 "+beforemanupro+"3 "+beforemanupro+"4 "+beforemanupro+"5 ";
                this.result=aftermanupro;
                mainstage.close();
            }
        });
    }
    public void show()
    {
        mainstage.showAndWait();
    }
    public String getResult()
    {
        return result;
    }
}
