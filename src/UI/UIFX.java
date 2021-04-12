package UI;

import Action.Autoinput;
import Action.Refresh;
import Action.Search;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名：UIFX
 * 作者：
 * 用途：主界面的FX实现类，定义了各个键摁下去之后的行为
 **/
public class UIFX extends Application{
    TextArea textAreaOut=new TextArea();
    TextField textFieldin=new TextField();
    ListView<String> listViewKichuStarName=new ListView<String>();
    Button buttonSearch=new Button("搜索");
    Button buttonF5=new Button("刷新");
    //Button buttonSearchStar=new Button("🔍");
    Button buttonaddStar=new Button("➕");
    Button buttonminusStar=new Button("➖");
    Button buttonBack=new Button("撤销");
    Button buttonForward=new Button("恢复");
    Button buttonOutput=new Button("导出文本");
    Button buttoninputAuto=new Button("一键输入");
   // TextField textFieldinputSearch=new TextField();



    BorderPane mainbox=new BorderPane();
    HBox hBoxrightSearch=new HBox();
    HBox hBoxbtn=new HBox();
    VBox vBoxright=new VBox();
    VBox vBoxbtn=new VBox();
    List<String> kichuStarList=new ArrayList<String>();
    Map<String,List<String>> duoyinziDic;

    List<String> searchHistoryResult=new ArrayList<String>();
    List<String> kichuStaroutputList=new ArrayList<String>();//实际上主菜单中设置显示的list。
    int nowdisplay=-1;//现在显示的搜索结果在list的下标。一开始因为没有元素，所以初始值设置为-1.
    //int nowsize=0;//显示现在List的大小。由于在回退状态下搜索会重新进行分支，因此需要手动定义长度。
    SearchWindow searchWindow;
    @Override
    public void start(Stage primaryStage) throws Exception {
        textAreaOut.setEditable(false);
        mainbox.setCenter(textAreaOut);
        //hBoxrightSearch.getChildren().addAll(textFieldinputSearch,buttonSearchStar);
        hBoxrightSearch.getChildren().addAll(buttonaddStar,buttonminusStar);
        hBoxrightSearch.setSpacing(5);
        vBoxright.getChildren().addAll(hBoxrightSearch,new Label("当前惨遭迫害的明星"),listViewKichuStarName);
        mainbox.setRight(vBoxright);
        vBoxright.setPadding(new Insets(10));
        vBoxright.setSpacing(10);

        textFieldin.setPrefWidth(800);

        hBoxbtn.getChildren().addAll(new Label("输入你想转化为鬼畜语录的拼音"),textFieldin,buttonF5,buttonSearch,buttonBack,buttonForward,buttonOutput,buttoninputAuto);

        hBoxbtn.setSpacing(10);
        hBoxbtn.setPadding(new Insets(10));
        hBoxbtn.setAlignment(Pos.CENTER);
        vBoxbtn.getChildren().addAll(hBoxbtn,new Label("输入可直接输入汉字 也可以输出汉字拼音对应的编码 即：(拼音本身)(声调 1-5 5代表轻声) B站浅音Ls制作 不关注下我么（doge"));

        vBoxbtn.setSpacing(10);

        mainbox.setBottom(vBoxbtn);
        mainbox.setPadding(new Insets(10));
        listViewKichuStarName.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listViewKichuStarName.setPrefWidth(250);
        listViewKichuStarName.setPlaceholder(new Label("嗯➕键来添（po）\n加（hai）自己喜欢\n（也可能一点都不喜\n欢）的明星吧。"));

        config();

        duoyinziDic=(Map<String, List<String>>) new ObjectInputStream(new FileInputStream("duoyinzicidian.ser")).readObject();
        searchWindow=new SearchWindow(kichuStarList);
        primaryStage.setTitle("鬼畜填词姬 1.00 浅音制作~");
        primaryStage.setScene(new Scene(mainbox));
        primaryStage.show();

        buttonF5.setOnAction(e->{
            try {
                kichuStarList= new Refresh().refresh(new File("src\\Stars"),duoyinziDic);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //listViewKichuStarName.setItems(FXCollections.observableArrayList(kichuStarList));
           //把src/Stars下所有的txt文件文件名存储进List

            try {
                FileOutputStream fileOutputStreamtmp=new FileOutputStream("starsnow.ser");
                ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStreamtmp);
                objectOutputStream.writeObject(kichuStarList);
                objectOutputStream.close();
                fileOutputStreamtmp.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //把当前的全明星列表进行缓存，下次可直接显示全明星列表而不需要刷新
            searchWindow=new SearchWindow(kichuStarList);
            listViewKichuStarName.setItems(null);//刷新后，清空所有已经被选中的明星。
        });

        buttonSearch.setOnAction(e->{

            try {
                List<Integer> selectedStars= searchWindow.getSelectedStar();
                String inputtemp=new Refresh().topinyin(textFieldin.getText().trim().toCharArray(),duoyinziDic);
                //System.out.println(inputtemp);
                inputtemp+=" ";//目的是能够区分句尾的标点符号
               inputtemp= inputtemp.replace("， ","");
               inputtemp= inputtemp.replace("。 ","");
               inputtemp= inputtemp.replace("？ ","");
               inputtemp= inputtemp.replace("！ ","");
               inputtemp= inputtemp.replace("~ ","");
               inputtemp= inputtemp.replace(", ","");
               inputtemp= inputtemp.replace(". ","");
               inputtemp= inputtemp.replace("… ","");
               inputtemp= inputtemp.replace("? ","");
               inputtemp= inputtemp.replace("! ","");
                //用户输入的时候这些符号会被省略.
                //注意：1.replace()函数不回改变原状态，只会返回值，你需要对返回值进行保存。
                //2.连续两个空格时候split(" ")，会使得中间出现空串，而如果不再replace的原数组后面加" " 的话，就会导致系统认为需要搜索空串。

                textAreaOut.setText(new Search().Search4words(kichuStarList,selectedStars,inputtemp));


                int i,size=searchHistoryResult.size();//因为随着元素的删除，size会相应改变，所以必须固化size
                for (i=nowdisplay+1;i<size;i++)
                {
                    searchHistoryResult.remove(nowdisplay+1);//同理，由于元素的删除，其他元素的坐标也会发生改变。
                }
                nowdisplay++;

                searchHistoryResult.add(textAreaOut.getText());
                //使用列表，把搜索结果放入列表中，这条一定要放后面，先删了后面的再加回来，否则容易误伤。
                //System.out.println(searchHistoryResult.toString());

            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
                Alert alert=new Alert(Alert.AlertType.ERROR,"IO出现错误(错误代码：1");
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
                Alert alert=new Alert(Alert.AlertType.ERROR,"类文件丢失(错误代码：2");
            }catch (NullPointerException e1){
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("你还没有选中鬼畜明星");
                alert.showAndWait();
            }

        });



        buttonaddStar.setOnAction(e->{
            buttonaddStar.setDisable(true);
            searchWindow.show();


            Iterator<Integer> iteratortmp=searchWindow.getSelectedStar().iterator();
            kichuStaroutputList.clear();
            while (iteratortmp.hasNext())
            {
                int nextindex=iteratortmp.next();

                kichuStaroutputList.add(kichuStarList.get(nextindex));
            }
            listViewKichuStarName.setItems(FXCollections.observableArrayList(kichuStaroutputList));
            buttonaddStar.setDisable(false);
        });

        buttonminusStar.setOnAction(e->{
            List<String> deleteTmp= listViewKichuStarName.getSelectionModel().getSelectedItems();
            Iterator<String> deleteTmpIterator=deleteTmp.iterator();
            while (deleteTmpIterator.hasNext())
            {
                String next=deleteTmpIterator.next();

                kichuStaroutputList.remove(next);
                List selectStarTmp=searchWindow.getSelectedStar();//拿到的是索引
                selectStarTmp.remove(new Integer(kichuStarList.indexOf(next)));
                searchWindow.setSelectedStar(selectStarTmp);
                //从显示和实际选择两个方面删除被删的元素
            }
            listViewKichuStarName.setItems(FXCollections.observableArrayList(kichuStaroutputList));
        });

        buttonBack.setOnAction(e->{
            if (nowdisplay>0)
            {
                nowdisplay--;
                textAreaOut.setText(searchHistoryResult.get(nowdisplay));
                //System.out.println(nowdisplay);
            }
            else
            {
                Alert alerterr=new Alert(Alert.AlertType.ERROR);
                alerterr.setHeaderText("已经到头了哦");
                alerterr.setContentText("不能再撤销了");
                alerterr.showAndWait();
            }
        });

        buttonForward.setOnAction(e->{
            if (nowdisplay<searchHistoryResult.size()-1)
            {
                nowdisplay++;
                textAreaOut.setText(searchHistoryResult.get(nowdisplay));
                //System.out.println(nowdisplay);
            }
            else
            {
                Alert alerterr=new Alert(Alert.AlertType.ERROR);
                alerterr.setHeaderText("已经到尾了哦");
                alerterr.setContentText("不能再恢复了");
                alerterr.showAndWait();
            }
        });
        buttonOutput.setOnAction(e->{

            if (textAreaOut.getText().equals(""))
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("你还没有搜索呢，搜索完再说8·-·");
                alert.showAndWait();
            }
            else {


                FileChooser fileChooser = new FileChooser();
                File huancun=new File("lastfile.ser");
                SimpleDateFormat simpleDateFormatNow=new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
                fileChooser.setInitialFileName(simpleDateFormatNow.format(new Date())+"搜索导出.txt");
                FileChooser.ExtensionFilter extensionFilter=new FileChooser.ExtensionFilter("文本文件","*.txt");//第一个参数：描述（实际显示），第二个参数：底层转换（为什么值）
                fileChooser.getExtensionFilters().add(extensionFilter);
                fileChooser.setSelectedExtensionFilter(extensionFilter);
                if (huancun.exists())
                {
                    try {
                        /*FileInputStream fi=new FileInputStream(huancun);
                        byte[] lastbaocunbyte=new byte[512];
                        fi.read(lastbaocunbyte);*/

                        String lastbaocun=(String) new ObjectInputStream(new FileInputStream(huancun)).readObject();
                        lastbaocun=lastbaocun.substring(0,lastbaocun.lastIndexOf("\\"));
                        //System.out.println(lastbaocun);
                        //System.out.println(new File(lastbaocun).isDirectory());
                        fileChooser.setInitialDirectory(new File(lastbaocun));
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
               // fileChooser.setInitialDirectory();
                File file = fileChooser.showSaveDialog(null);

                if (file == null) {
                    //System.out.println("file is null");
                } else {


                    try {
                        PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, true), "utf-8"));
                        pw.println(textAreaOut.getText());
                        pw.close();
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }

                }
                try {
                    if (file!=null && !file.toString().trim().equals("")) {


                        FileOutputStream fileOutputStream = new FileOutputStream("lastfile.ser");
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);


                        objectOutputStream.writeObject(file.toString());


                        objectOutputStream.close();
                        fileOutputStream.close();
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        });

        buttoninputAuto.setOnAction(e->{
            buttoninputAuto.setDisable(true);
            Autoinput autoinput=new Autoinput();

            autoinput.show();
            String Autoinstr=autoinput.getResult();
            if (Autoinstr!=null)
            {
                textFieldin.appendText(Autoinstr);
            }

            buttoninputAuto.setDisable(false);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }


    public void config() throws IOException, ClassNotFoundException//在FX开始运行前运行
    {
        File src=new File("duoyinzicidian.ser");
        if (src.exists())
        {
            //System.out.println("已经经过序列化");
        }
        else
        {
            Map<String,List<String>> duoyinziMap=new HashMap<String,List<String>>();
            //Scanner scanner=new Scanner(new File("duoyinzicidian.txt"));
            BufferedReader scanner=new BufferedReader(new InputStreamReader(new FileInputStream("resource/duoyinzicidian.txt"),"utf-8"));
            String linetmp;
            while ((linetmp=(scanner.readLine()))!=null)
            {

                String thisline[]=linetmp.split("#");
                List<String> words4Example=Arrays.asList(thisline[1].split("/"));
                duoyinziMap.put(thisline[0],words4Example);

            }
            FileOutputStream fileOutputStream=new FileOutputStream("duoyinzicidian.ser");
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(duoyinziMap);
            objectOutputStream.close();
            fileOutputStream.close();

        }

        File starsrc=new File("starsnow.ser");
        if (starsrc.exists())
        {
            kichuStarList= (List<String>) new ObjectInputStream((new FileInputStream("starsnow.ser"))).readObject();
            //listViewKichuStarName.setItems(FXCollections.observableArrayList(kichuStarList));
        }
    }
}
