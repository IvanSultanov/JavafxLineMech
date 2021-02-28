package GUI;
import MechanicCalculations.ExcelGenerate;
import MechanicCalculations.WireMechLoad;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class Controller {

    @FXML
    private Button button1;

    @FXML
    private TextField KabWeight;

    @FXML
    private TextField KabDiam;

    @FXML
    private TextField CrosSec;

    @FXML
    private TextField TensLimit;

    @FXML
    private TextField MaxTens;

    @FXML
    private TextField AverTens;

    @FXML
    private TextField ElastMod;

    @FXML
    private TextField KLTE;

    @FXML
    private TextField Tmax;

    @FXML
    private TextField Tmin;

    @FXML
    private TextField T_aver;

    @FXML
    private TextField T_ice;

    @FXML
    private TextField T_step;

    @FXML
    private TextField WindReg;

    @FXML
    private TextField IceThick;

    @FXML
    private TextField K_wind;

    @FXML
    private TextField K_ice;

    @FXML
    private TextField InitDist;

    @FXML
    private TextField DistStep;

    @FXML
    private TextField DistCount;

    @FXML
    private TextField FilePath;

    @FXML
    private TextField FileName;

    @FXML
    private Label OutputMessage;

    @FXML
    private RadioButton YearRate25;

    @FXML
    private RadioButton YearRate10;

    @FXML
    protected void initialize() {
        KabWeight.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; KabWeight.setText(oldValue.replace("", "")); });
        KabDiam.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; KabDiam.setText(oldValue.replace("", "")); });
        CrosSec.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; CrosSec.setText(oldValue.replace("", "")); });
        TensLimit.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; TensLimit.setText(oldValue.replace("", "")); });
        MaxTens.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; MaxTens.setText(oldValue.replace("", "")); });
        AverTens.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; AverTens.setText(oldValue.replace("", "")); });
        ElastMod.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; ElastMod.setText(oldValue.replace("", "")); });
        KLTE.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; KLTE.setText(oldValue.replace("", "")); });
        Tmax.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*")) return; Tmax.setText(oldValue.replace("", "")); });
        Tmin.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("[-0-9]*")) return; Tmin.setText(oldValue.replace("", "")); });
        T_aver.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("[-0-9]*")) return; T_aver.setText(oldValue.replace("", "")); });
        T_ice.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("[-0-9]*")) return; T_ice.setText(oldValue.replace("", "")); });
        T_step.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*")) return; T_step.setText(oldValue.replace("", "")); });
        WindReg.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("[1]*|[2]*|[3]*|[4]*|[5]*|[6]*|[7]*|[8]*")) return; WindReg.setText(oldValue.replace("", "")); });
        IceThick.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; IceThick.setText(oldValue.replace("", "")); });
        K_wind.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; K_wind.setText(oldValue.replace("", "")); });
        K_ice.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*|\\d+[.]\\d*")) return; K_ice.setText(oldValue.replace("", "")); });
        InitDist.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*")) return; InitDist.setText(oldValue.replace("", "")); });
        DistStep.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*")) return; DistStep.setText(oldValue.replace("", "")); });
        DistCount.textProperty().addListener((observable, oldValue, newValue) -> { if (newValue.matches("\\d*")) return; DistCount.setText(oldValue.replace("", "")); });
        ToggleGroup YearRate = new ToggleGroup();
        YearRate10.setToggleGroup(YearRate);
        YearRate25.setToggleGroup(YearRate);
//        YearRate.selectedToggleProperty().addListener((observable, oldVal, newVal) -> { System.out.println(YearRate10.isSelected());
//            System.out.println(YearRate25.isSelected());
//        if (newVal.equals(YearRate10)) System.out.println("1");
//        if (newVal.equals(YearRate25)) System.out.println("1");
//        });


    }

    private int N = 1;

    @FXML
    protected void onClick(ActionEvent event) {
        int YearRateNumb = 0;
        if (YearRate10.isSelected()) YearRateNumb  = 1;
        if (YearRate25.isSelected()) YearRateNumb  = 2;
        ExcelGenerate NewTable = new ExcelGenerate();
        NewTable.MountTableCalc(Integer.parseInt(InitDist.getText()), Integer.parseInt(DistStep.getText()), Integer.parseInt(DistCount.getText()), Integer.parseInt(WindReg.getText()),YearRateNumb, Double.parseDouble(IceThick.getText()),
                Double.parseDouble(KabWeight.getText()), Double.parseDouble(KabDiam.getText()), Double.parseDouble(CrosSec.getText()), Double.parseDouble(MaxTens.getText()),
                Double.parseDouble(MaxTens.getText()), Double.parseDouble(AverTens.getText()), Double.parseDouble(ElastMod.getText()), Double.parseDouble(KLTE.getText()),
                Integer.parseInt(Tmin.getText()), Integer.parseInt(Tmax.getText()), Integer.parseInt(T_aver.getText()), Integer.parseInt(T_ice.getText()), Integer.parseInt(T_step.getText()),
                FilePath.getText(), FileName.getText(), N, Double.parseDouble(K_wind.getText()), Double.parseDouble(K_ice.getText()));
        if (NewTable.getFileStatus()) {
            OutputMessage.setText("Файл " + FileName.getText() + "_" + N + " успешно сохранен");
            N++;
        } else OutputMessage.setText("Что-то пошло не так...");
        System.out.println(YearRateNumb);
    }

}


// KabWeight.textProperty().addListener((observable, oldValue, newValue) -> {
//         if (newValue.matches("\\d*|\\d+[.]\\d*")) {
//         System.out.println("совпадает");
//         return;
//         }
//         System.out.println("не совпадает");
//         KabWeight.setText(oldValue.replace("", ""));
//         });