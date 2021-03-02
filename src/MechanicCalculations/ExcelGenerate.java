package MechanicCalculations;
import  java.io.*;
import java.text.DecimalFormat;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelGenerate {
    boolean FileCreated = false;

    public void MountTableCalc (int InitDist, int DistStep, int DistCount, int WindReg, int YearRate, double IceThick, double Weight, double Diam, double CrosSec,
                               double MaxTens, double LowTempTens, double AverTens, double ElastM, double KLTE, int tmin, int tmax, int taverage, int Tice, int T_step, String FilePath, String FileName, int N, double K_wind, double K_ice) {


        DecimalFormat MyF = new DecimalFormat("##0.00");
        double[] Prolety =  {471, 336, 403, 315, 327.5, 383.5, 384.5, 413.5, 438.5, 472.5, 298, 335.5, 381, 273, 147.5 } ;

// AC 300
        MountTable Calc = new MountTable(300, WindReg,
                YearRate, IceThick, Weight, Diam, CrosSec,
                MaxTens, LowTempTens, AverTens, ElastM, KLTE, tmin, tmax, taverage, Tice);

        WireMechLoad Calc1 = new WireMechLoad((InitDist + (DistCount - 1) * DistStep), WindReg,
                YearRate, IceThick, Weight, Diam, CrosSec,
                MaxTens, LowTempTens, AverTens, ElastM, KLTE, tmin, tmax, taverage, Tice);


        Workbook Book = new XSSFWorkbook();
        Sheet newSheet = Book.createSheet("Стрелы провеса");
        CellStyle MyStyle = newSheet.getWorkbook().createCellStyle();
        MyStyle.setAlignment(HorizontalAlignment.CENTER);
        MyStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        newSheet.setColumnWidth(0, 3000);
        newSheet.setColumnWidth(1, 4000);
//        newSheet.setColumnWidth(2, 6000);
        CellRangeAddress mergHeader = new CellRangeAddress(0,0,0,18);
        newSheet.addMergedRegion(mergHeader);
        CellRangeAddress mergDist = new CellRangeAddress(1,2,0,0);
        newSheet.addMergedRegion(mergDist);
        CellRangeAddress mergParam = new CellRangeAddress(1,2,1,1);
        newSheet.addMergedRegion(mergParam);
        CellRangeAddress mergTemp = new CellRangeAddress(1,1,2,20);
        newSheet.addMergedRegion(mergTemp);


        Row row = newSheet.createRow(0);
        row.setHeightInPoints(20);
        String YEar = "";
        if (YearRate == 1) YEar = " с повторяемостью 1 раз в 10 лет";
        else if (YearRate == 2) YEar = ", с повторяемостью 1 раз в 25 лет";
        row.createCell(0).setCellValue("Климатические условия: " + "Ветровой район: " + WindReg + YEar + " (q = " + Calc1.getWindPress() + " даН/м2, V = " + Calc1.getWindVelos() + ", м/с)" + " , Толщина стенки гололёда: " + IceThick + " мм");

        Row row1 = newSheet.createRow(1);
        row1.createCell(0).setCellValue("Пролет, м");
        row1.createCell(1).setCellValue("Характеристики");
        row1.createCell(2).setCellValue("Температура");

        int j;
        int Tmin = tmin;
        int Tmax = tmax;
        int Tstep = T_step;
        int t = (Tmax - Tmin) / Tstep + 3;

        Row row2 = newSheet.createRow(2);
        for (int i = 2; i < t; i++) row2.createCell(i).setCellValue(Tmin + (i-2)*Tstep);
        row2.createCell(t).setCellValue("-5Г");


// (initDist + 10 * ((double)i / 3 - 1))
       // Prolety[i / 3 - 1]
        for (int i = 3; i < (DistCount * 3 + 3); i += 3) {
            newSheet.addMergedRegion(new CellRangeAddress(i,i+2,0,0));
            Row rowA = newSheet.createRow(i);
            Cell cell = rowA.createCell(0);
            cell.setCellValue((InitDist + DistStep * ((double)i / 3 - 1)));
            cell.setCellStyle(MyStyle);
            rowA.createCell(1).setCellValue("Тяжение, кг");
            Row rowB = newSheet.createRow(i+1);
            rowB.createCell(1).setCellValue("σ, кгс/м*мм2");
            Row rowC = newSheet.createRow(i+2);
            rowC.createCell(1).setCellValue("Стрела, м");
            for ( j = 2; j < t; j++) {
                Calc.Calculation((InitDist + DistStep * ((double)i / 3 - 1)), Tmin + ((double)j - 2) * Tstep, K_wind, K_ice);
                rowA.createCell(j).setCellValue(MyF.format(Calc.getTension()));
                rowB.createCell(j).setCellValue(MyF.format(Calc.getMountLoad()));
                rowC.createCell(j).setCellValue(MyF.format(Calc.getSag()));
//                System.out.println("Массив" + Prolety[i / 3 - 1]);
            }
            Calc.Calculation((InitDist + DistStep * ((double)i / 3 - 1)), -5, K_wind, K_ice);
            rowA.createCell(j).setCellValue(MyF.format(Calc.getTensionIce()));
            rowB.createCell(j).setCellValue(MyF.format(Calc.getMountLoadIce()));
            rowC.createCell(j).setCellValue(MyF.format(Calc.getSagIce()));

        }

        Calc1.Calculation(K_wind, K_ice);

        int offset = 3 * DistCount + 5;
        for (int z = 0; z < 6; z++) {
            newSheet.addMergedRegion(new CellRangeAddress(offset + z, offset + z, 0, 3));
        }
        for (int v = 0; v < 4; v++) {
            for (int z = 0; z < 6; z++) {
                newSheet.addMergedRegion(new CellRangeAddress(offset + z, offset + z, 4 + v * 2, 5 + v * 2));
            }
        }
        Row ModeHeader = newSheet.createRow(offset);
        ModeHeader.createCell(0).setCellValue("Режимы для пролёта " + (InitDist + (DistCount - 1) * DistStep) + "м");
        ModeHeader.createCell(4).setCellValue("σ, кгс/м*мм2");
        ModeHeader.createCell(6).setCellValue("Тяжение, кг");
        ModeHeader.createCell(8).setCellValue("Стрела верт., м");
        ModeHeader.createCell(10).setCellValue("Стрела горизонт., м");
        System.out.println("InitDist= " + InitDist);
        System.out.println("DistCount= " + DistCount);
        System.out.println("DistStep= " + DistStep);

        Row Mode1 = newSheet.createRow(offset + 2);
        Mode1.createCell(0).setCellValue("Гололёд, t = -5, ветер 0,25q");
        Mode1.createCell(4).setCellValue(MyF.format(Calc1.getLoadMode1()));
        Mode1.createCell(6).setCellValue(MyF.format(Calc1.getLoadMode1() * CrosSec));
        Mode1.createCell(8).setCellValue(MyF.format(Calc1.getSagMode1V()));
        Mode1.createCell(10).setCellValue(MyF.format(Calc1.getSagMode1H()));

        Row Mode2 = newSheet.createRow(offset + 3);
        Mode2.createCell(0).setCellValue("Гололёд, t = -5, ветера нет q=0");
        Mode2.createCell(4).setCellValue(MyF.format(Calc1.getLoadMode2()));
        Mode2.createCell(6).setCellValue(MyF.format(Calc1.getLoadMode2() * CrosSec));
        Mode2.createCell(8).setCellValue(MyF.format(Calc1.getSagMode2()));
        Mode2.createCell(10).setCellValue("0");

        Row Mode3 = newSheet.createRow(offset + 4);
        Mode3.createCell(0).setCellValue("Гололёда нет, t = -5, ветер q");
        Mode3.createCell(4).setCellValue(MyF.format(Calc1.getLoadMode3()));
        Mode3.createCell(6).setCellValue(MyF.format(Calc1.getLoadMode3() * CrosSec));
        Mode3.createCell(8).setCellValue(MyF.format(Calc1.getSagMode3V()));
        Mode3.createCell(10).setCellValue(MyF.format(Calc1.getSagMode3H()));

        Row Mode4 = newSheet.createRow(offset + 5);
        Mode4.createCell(0).setCellValue("Среднегодовая tэ = 5.0, ветра и голёда нет");
        Mode4.createCell(4).setCellValue(MyF.format(Calc1.getLoadMode4()));
        Mode4.createCell(6).setCellValue(MyF.format(Calc1.getLoadMode4() * CrosSec));
        Mode4.createCell(8).setCellValue(MyF.format(Calc1.getSagMode4()));
        Mode4.createCell(10).setCellValue("0");

        Calc1.printCalculation();

        File theDir = new File(FilePath);
        theDir.mkdirs();

        try {
            FileOutputStream fileOutput = new FileOutputStream(FilePath + "\\" + FileName + "_" + N +".xlsx");
            Book.write(fileOutput);
            fileOutput.close();
            System.out.println("Файл записан!");
            FileCreated = true;
        }
        catch (Exception e) {
            System.out.println("Что-то пошло не так");
            FileCreated = false;
        }

    }
    public boolean getFileStatus () {
        return FileCreated;
    }

}