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

        WireMechLoad Calc4 = new WireMechLoad(300, 4,
                1, 20, 2428, 29.2, 501.89,
                25.4, 25.4, 16.9, 11400, 15.5, -40, 40, 5, -5);


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
        else if (YearRate == 2) YEar = " с повторяемостью 1 раз в 25 лет";
        row.createCell(0).setCellValue("Климатические условия: " + "Ветровой район: " + WindReg + YEar + ", Толщина стенки гололёда: " + IceThick + " мм");

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
//        DistCount += 2;
//        int offset = 8;
//        for (int z = 0; z < 8; z++) {
//            newSheet.addMergedRegion(new CellRangeAddress(DistCount + offset + z, DistCount + offset + z, 0, 3));
//        }
//        for (int v = 0; v < 4; v++) {
//            for (int z = 0; z < 8; z++) {
//                newSheet.addMergedRegion(new CellRangeAddress(DistCount + offset + z, DistCount + offset + z, 4 + v * 2, 5 + v * 2));
//            }
//        }
//        Row ModeHeader = newSheet.createRow(DistCount + offset);

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
        Calc4.getCalculation();
    }
    public boolean getFileStatus () {
        return FileCreated;
    }

}