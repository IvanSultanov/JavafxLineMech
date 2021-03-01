package MechanicCalculations;

import java.text.DecimalFormat;
public class WireMechLoad {
    DecimalFormat Myformat = new DecimalFormat("##0.00");
    DecimalFormat Myformat2 = new DecimalFormat("##0.000000");
    double Distance; // Пролёт, м;
    double KabWeight; // Вес провода (троса), кг/км;
    double KabDiam; // Диаметр провода (троса), мм;
    double CrSec; // Поперечное сечение провода (троса), мм;
    double MaxTens; // Допустимое напряжение;
    double LowTempTens; // Допустимое напряжение при низшей температуре;
    double AverTens; // Среднеэксплуатационное напряжение;
    double KLTE; // Коэффициент лмнейного температурного расширения;
    double ElastM; // Модуль упругости;
    double tmin; // Минимальная температура;
    double tmax; // Максимальная температура;
    double taverage; // Среднегодовая температура;
    double tIce; // Температура голодедообразования;
    double LoadMode1, LoadMode2, LoadMode3, LoadMode4, LoadMode5, LoadMode6, LoadMode7;
    double SagMode1, SagMode2, SagMode3, SagMode4, SagMode5, SagMode6, SagMode7;
    double c; // Толщина стенки гололёда, мм;
    double p1; // Нагркзка от собственного веса провода даН/м;
    double p2; // Нагркзка от гололёда даН/м;
    double p3; // Нагркзка от собственного веса провода и гололёда даН/м;
    double p4; // Нагркзка от ветра без гололеда даН/м;
    double p5; // Нагркзка от ветра с гололедом даН/м;
    double p6; // Нагркзка от ветра и веса без гололедоа даН/м;
    double p7; // Нагркзка от ветра и веса с гололедом даН/м;

    double y1; // Удельная нагркзка от собственного веса провода даН/(м*мм2);
    double y2; // Удельная нагркзка от гололёда даН/(м*мм2);
    double y3; // Удельная нагркзка от собственного веса провода и гололёда даН/(м*мм2);
    double y4; // Удельная нагркзка от ветра без гололеда даН/(м*мм2);
    double y5; // Удельная нагркзка от ветра с гололедом даН/(м*мм2);
    double y6; // Удельная нагркзка от ветра и веса без гололедоа даН/(м*мм2);
    double y7; // Удельная нагркзка от ветра и веса с гололедом даН/(м*мм2);
    double q; // Скоростной напор ветра даН/м2;
    double L1k, L2k, L3k; // Первый, второй и третий критические пролеты;

    double Cx = 1; // Коэффициент лобового сопротивления;
    int r; // Район по гололёду 1-7;
    int y; // Повторяемость 1 - раз в 10 лет, 2 - раз в 25 лет;
    // int p; // 1 - скоростной напор, даН/м2; 2 - скорость ветра, м/с;

   public WireMechLoad (double Distance, int WindReg, int YearRate, double Ice, double Weight, double Diam, double CrosSec,
                  double MaxTens, double LowTempTens, double AverTens, double ElastM, double KLTE, double tmin, double tmax, double taverage, double Tice) {
        this.Distance = Distance; // Пролёт;
        KabWeight = Weight; // Вес провода (троса), кг/км;
        KabDiam = Diam; // Диаметр провода (троса), мм;
        this.MaxTens = MaxTens; // Допустимое напряжение;
        this.LowTempTens = MaxTens; // Допустимое напряжение при низшей температуре;
        this.AverTens = AverTens; // Среднеэксплуатационное напряжение;
        this.ElastM = ElastM; // Модуль упругости;
        this.KLTE = KLTE / 1000000; // Коэффициент лмнейного температурного расширения;
        this.tmin = tmin; // Минимальная температура;
        this.taverage = taverage; // Среднегодовая температура;
        this.tmax = tmax; // Максимальная температура;
        this.tIce = Tice;  // Температура голодедообразования;
        c = Ice; // Толщина стенки гололёда, мм;
        r = WindReg; // Район по ветру 1-7;
        y = YearRate; // Повторяемость 1 - раз в 10 лет, 2 - раз в 25 лет;
        CrSec = CrosSec;
//        p = 1; // 1 - скоростной напор, даН/м2; 2 - скорость ветра, м/с;
    }


public void Calculation (double K_wind, double K_ice) {

    double [][][] wind = new double[9][3][3];
    wind[1][1][1] = 40; wind[1][1][2] = 25; wind[1][2][1] = 55; wind[1][2][2] = 30;
    wind[2][1][1] = 40; wind[2][1][2] = 25; wind[2][2][1] = 55; wind[2][2][2] = 30;
    wind[3][1][1] = 50; wind[3][1][2] = 29; wind[3][2][1] = 65; wind[3][2][2] = 32;
    wind[4][1][1] = 65; wind[4][1][2] = 32; wind[4][2][1] = 80; wind[4][2][2] = 36;
    wind[5][1][1] = 80; wind[5][1][2] = 36; wind[5][2][1] = 100; wind[5][2][2] = 40;
    wind[6][1][1] = 100; wind[6][1][2] = 40; wind[6][2][1] = 125; wind[6][2][2] = 45;
    wind[7][1][1] = 125; wind[7][1][2] = 45; wind[7][2][1] = 150; wind[7][2][2] = 49;
    wind[8][1][1] = 145; wind[8][1][2] = 48; wind[8][2][1] = 175; wind[8][2][2] = 53;

    q = wind[r][y][1];

//    q = 100;
//    System.out.println(q);
// Сх - коэффициент лобового сопротивления
//    double Kl = 1.200;
//    double Kh = 1.3;
    if ((KabDiam < 20) | c > 0 ) Cx = 1.2;
    if ((KabDiam > 20) & c == 0 ) Cx = 1.1;


    p1 = 0.980665 * KabWeight / 1000; // даН/м;
    p2 = K_ice * 0.9 * Math.PI * c * (KabDiam + c) / 1000; // даН/м;
    p3 = p1 + p2; // даН/м;
    p4 = alfa(q) * Cx * K_wind * q * KabDiam / 1000; // даН/м;
    p5 = alfa((q * 0.25)) * Cx * K_wind * 0.25 * q * (KabDiam + 2 * c) / 1000; // даН/м;
    p6 = Math.sqrt(p1*p1 + p4*p4); // даН/м;
    p7 = Math.sqrt(p3*p3 + p5*p5); // даН/м;

    y1 = p1 / CrSec;
    y2 = p2 / CrSec;
    y3 = p3 / CrSec;
    y4 = p4 / CrSec;
    y5 = p5 / CrSec;
    y6 = p6 / CrSec;
    y7 = p7 / CrSec;

    L1k = CritDist (AverTens, LowTempTens, y1, y1, taverage, tmin);
    L2k = CritDist (MaxTens, LowTempTens, y7, y1, tIce, tmin);
    L3k = CritDist (MaxTens, AverTens, y7, y1, tIce, taverage);
    System.out.println("1 крит. проелет: " + Myformat.format(L1k) + "м");
    System.out.println("2 крит. проелет: " + Myformat.format(L2k) + "м");
    System.out.println("3 крит. проелет: " + Myformat.format(L3k) + "м");

///////////////////////////////////////////////////////
    if ((L1k < L2k) & (L2k < L3k) & (Distance < L1k)) {
        System.out.println("L1k < L2k < L3k, режим Н");
        ModeCalc (y1, tmin, MaxTens);
        MountCalc (y1, tmin, MaxTens);
    }
////////////////////////////////////////////////////////
    if ((L1k < L2k) & (L2k < L3k) & (Distance > L1k) & (Distance < L3k)) {
        System.out.println("L1k < L2k < L3k, режим С");
        ModeCalc (y1, taverage, AverTens);
        MountCalc (y1, taverage, AverTens);
    }
////////////////////////////////////////////////////////
    if ((L1k < L2k) & (L2k < L3k) & (Distance > L3k)) {
        System.out.println("L1k < L2k < L3k, режим Г");
        ModeCalc (y7, tIce, MaxTens);
        MountCalc (y7, tIce, MaxTens);
    }
////////////////////////////////////////////////////////
    if ((L1k > L2k) & (L2k > L3k) & (Distance < L2k)) {
        System.out.println("L3k < L2k < L1k, режим Н");
        ModeCalc (y1, tmin, MaxTens);
        MountCalc (y1, tmin, MaxTens);
    }
////////////////////////////////////////////////////////
    if ((L1k > L2k) & (L2k > L3k) & (Distance > L2k)) {
        System.out.println("L3k < L2k < L1k, режим Г");
        ModeCalc (y7, tIce, MaxTens);
        MountCalc (y7, tIce, MaxTens);
    }
////////////////////////////////////////////////////////
    if (Double.isNaN(L1k) & (Distance < L3k)) {
        System.out.println("L1k мнимый, режим C");
        ModeCalc (y1, taverage, AverTens);
        MountCalc (y1, taverage, AverTens);
    }
////////////////////////////////////////////////////////
    if (Double.isNaN(L1k) & (Distance > L3k)) {
        System.out.println("L1k мнимый, режим Г");
        ModeCalc (y7, tIce, MaxTens);
        MountCalc (y7, tIce, MaxTens);
    }
////////////////////////////////////////////////////////
    if (Double.isNaN(L3k) & (Distance < L1k)) {
        System.out.println("L3k мнимый, режим Н");
        ModeCalc(y1, tmin, MaxTens);
        MountCalc(y1, tmin, MaxTens);
    }
////////////////////////////////////////////////////////
    if (Double.isNaN(L3k) & (Distance > L1k)) {
        System.out.println("L3k мнимый, режим C");
        ModeCalc(y1, taverage, AverTens);
        MountCalc(y1, taverage, AverTens);
    }
////////////////////////////////////////////////////////
    if (Double.isNaN(L1k) & Double.isNaN(L3k)) {
        System.out.println("L1k и L3k мнимые, режим C");
        ModeCalc(y1, taverage, AverTens);
        MountCalc(y1, taverage, AverTens);
    }

}
    // alfa Поправочный коэффициент, зависяций от скорости ветра;
public double alfa (double qVal) {
        double a = 0;
    if (qVal <= 27) a = 1;
    if (qVal == 40) a = 0.85;
    if (qVal == 55) a = 0.75;
    if (qVal >= 76) a = 0.7;
    if (qVal > 27 & qVal < 40) a = (1 + (0.85 - 1) * (qVal - 27) / (40 - 27));
    if (qVal> 40 & qVal < 55) a = (0.85 + (0.75 - 0.85) * (qVal - 40) / (55 - 40));
    if (qVal > 55 & qVal < 76) a = (0.75 + (0.7 - 0.75) * (qVal - 55) / (76 - 55));
    return a;
}

//public void getCalculation () {
//    System.out.print("Длина пролета: " + Distance + "\t");
//    System.out.println("Сечение провода: " + CrSec + "\t");
//    Calculation();
//    System.out.println();
//    System.out.println("p1 = " + Myformat2.format(p1) + "\t\t" + "y1 = " + Myformat2.format(y1));
//    System.out.println("p2 = " + Myformat2.format(p2) + "\t\t" + "y2 = " + Myformat2.format(y2));
//    System.out.println("p3 = " + Myformat2.format(p3) + "\t\t" + "y3 = " + Myformat2.format(y3));
//    System.out.println("p4 = " + Myformat2.format(p4) + "\t\t" + "y4 = " + Myformat2.format(y4));
//    System.out.println("p5 = " + Myformat2.format(p5) + "\t\t" + "y5 = " + Myformat2.format(y5));
//    System.out.println("p6 = " + Myformat2.format(p6) + "\t\t" + "y6 = " + Myformat2.format(y6));
//    System.out.println("p7 = " + Myformat2.format(p7) + "\t\t" + "y7 = " + Myformat2.format(y7));
//    System.out.println("q = " + Myformat.format(q));
//    System.out.println("a(q) = " + Myformat.format(alfa(q)));
//    System.out.println("a(0.25q) = " + Myformat.format(alfa(q * 0.25)));
//    System.out.println("Cx = " + Myformat.format(Cx));
//    System.out.println("r = " + Myformat.format(r));
//    System.out.println("y = " + Myformat.format(y));
//    System.out.print("Режим 1 (Гололёд, t = -5, ветер 0,25q) \t\t\t\t" + Myformat.format(LoadMode1));
//    System.out.println("\t\t" + "Провис, м = " + Myformat.format(SagMode1) + "\t" + Myformat.format(SagMode1 * p3 / p7));
//    System.out.print("Режим 2 (Гололёд, t = -5, ветера нет q=0) \t\t\t" + Myformat.format(LoadMode2));
//    System.out.println("\t\t" + "Провис, м = " + Myformat.format(SagMode2));
//    System.out.print("Режим 3 (Гололёда нет, t = -5, ветер q) \t\t\t" + Myformat.format(LoadMode3));
//    System.out.println("\t\t" + "Провис, м = " + Myformat.format(SagMode3) + "\t" + Myformat.format(SagMode1 * p1 / p6));
//    System.out.print("Режим 4 (Среднегод. tэ = " + taverage + ", вет. и гол. нет) \t\t" + Myformat.format(LoadMode4));
//    System.out.println("\t\t" + "Провис, м = " + Myformat.format(SagMode4));
//    System.out.print("Режим 5 (t = +15, ветра и гол. нет) \t\t\t\t" + Myformat.format(LoadMode5));
//    System.out.println("\t\t" + "Провис, м = " + Myformat.format(SagMode5));
//    System.out.print("Режим 6 (t = " + tmin + " ветера и гололёда нет) \t\t\t" + Myformat.format(LoadMode6));
//    System.out.println("\t\t" + "Провис, м = " + Myformat.format(SagMode6));
//    System.out.print("Режим 7 (t = " + tmax + " ветера и гололёда нет) \t\t\t" + Myformat.format(LoadMode7));
//    System.out.println("\t\t" + "Провис, м = " + Myformat.format(SagMode7));
//    System.out.println();
//}

public double LoadCalc (double y, double yRef, double t, double tRef, double Gref) {
        double G = 1.00;
        double X;

        while (true) {
            X = G - ((y * y * ElastM * Distance * Distance) / (24 * G * G))
                    - Gref + ((Math.pow(yRef, 2) * ElastM * Distance * Distance) / (24 * Gref * Gref))
                    + (KLTE * ElastM * (t - tRef));
//            System.out.println("G " + G);
//            System.out.println("X " + X);
            G = G + 0.01;
            if (X > 0) {
                break;
            }
        }
        return G;
}

public double CritDist (double Gn, double Gm, double Yn, double Ym, double tn, double tm) {
        double Lk;
        Lk = Math.sqrt(((Gn - Gm) + KLTE * ElastM * (tn - tm)) / (((Yn * Yn * ElastM) / (24 * Gn * Gn)) - ((Ym * Ym * ElastM) / (24 * Gm * Gm)) ));
        return Lk;
}

public double Sag (double y, double Load) {
        double f;
        f = y * Distance * Distance / (8 * Load);
        return f;
}

public void ModeCalc (double Yref, double Ttef, double Gref) {
    LoadMode1 = LoadCalc(y7, Yref, -5, Ttef, Gref);
    LoadMode2 = LoadCalc(y3, Yref, -5, Ttef, Gref);
    LoadMode3 = LoadCalc(y6, Yref, -5, Ttef, Gref);
    LoadMode4 = LoadCalc(y1, Yref, taverage, Ttef, Gref);
    LoadMode5 = LoadCalc(y1, Yref, 15, Ttef, Gref);
    LoadMode6 = LoadCalc(y1, Yref, tmin, Ttef, Gref);
    LoadMode7 = LoadCalc(y1, Yref, tmax, Ttef, Gref);
    SagMode1 = Sag(y7, LoadMode1);
    SagMode2 = Sag(y3, LoadMode2);
    SagMode3 = Sag(y6, LoadMode3);
    SagMode4 = Sag(y1, LoadMode4);
    SagMode5 = Sag(y1, LoadMode5);
    SagMode6 = Sag(y1, LoadMode6);
    SagMode7 = Sag(y1, LoadMode7);
}
public double getLoadMode1 () { return LoadMode1; }
public double getLoadMode2 () { return LoadMode2; }
public double getLoadMode3 () { return LoadMode3; }
public double getLoadMode4 () { return LoadMode4; }
public double getLoadMode5 () { return LoadMode5; }
public double getLoadMode6 () { return LoadMode6; }
public double getLoadMode7 () { return LoadMode7; }

public double getSagMode1V () { return (SagMode1 * p3 / p7); }
public double getSagMode1H () { return (SagMode1 * p5 / p7); }

public double getSagMode2 () { return SagMode2; }

public double getSagMode3V () { return (SagMode3 * p1 / p6); }
public double getSagMode3H () { return (SagMode3 * p4 / p6); }

public double getSagMode4 () { return SagMode4; }
public double getSagMode5 () { return SagMode5; }
public double getSagMode6 () { return SagMode6; }
public double getSagMode7 () { return SagMode7; }

public double getWindPress () {
    double [][][] wind =new double[9][3][3];
    wind[1][1][1] = 40; wind[1][1][2] = 25; wind[1][2][1] = 55; wind[1][2][2] = 30;
    wind[2][1][1] = 40; wind[2][1][2] = 25; wind[2][2][1] = 55; wind[2][2][2] = 30;
    wind[3][1][1] = 50; wind[3][1][2] = 29; wind[3][2][1] = 65; wind[3][2][2] = 32;
    wind[4][1][1] = 65; wind[4][1][2] = 32; wind[4][2][1] = 80; wind[4][2][2] = 36;
    wind[5][1][1] = 80; wind[5][1][2] = 36; wind[5][2][1] = 100; wind[5][2][2] = 40;
    wind[6][1][1] = 100; wind[6][1][2] = 40; wind[6][2][1] = 125; wind[6][2][2] = 45;
    wind[7][1][1] = 125; wind[7][1][2] = 45; wind[7][2][1] = 150; wind[7][2][2] = 49;
    wind[8][1][1] = 145; wind[8][1][2] = 48; wind[8][2][1] = 175; wind[8][2][2] = 53;
    return wind[r][y][1];
   }

public double getWindVelos () {
    double [][][] wind =new double[9][3][3];
    wind[1][1][1] = 40; wind[1][1][2] = 25; wind[1][2][1] = 55; wind[1][2][2] = 30;
    wind[2][1][1] = 40; wind[2][1][2] = 25; wind[2][2][1] = 55; wind[2][2][2] = 30;
    wind[3][1][1] = 50; wind[3][1][2] = 29; wind[3][2][1] = 65; wind[3][2][2] = 32;
    wind[4][1][1] = 65; wind[4][1][2] = 32; wind[4][2][1] = 80; wind[4][2][2] = 36;
    wind[5][1][1] = 80; wind[5][1][2] = 36; wind[5][2][1] = 100; wind[5][2][2] = 40;
    wind[6][1][1] = 100; wind[6][1][2] = 40; wind[6][2][1] = 125; wind[6][2][2] = 45;
    wind[7][1][1] = 125; wind[7][1][2] = 45; wind[7][2][1] = 150; wind[7][2][2] = 49;
    wind[8][1][1] = 145; wind[8][1][2] = 48; wind[8][2][1] = 175; wind[8][2][2] = 53;
    return wind[r][y][2];
   }


    public void MountCalc (double Yref, double Ttef, double Gref) {
        double MountLoad, Sag, Tension;
        System.out.println("Монтажная таблица:");
        System.out.println("Температура  \t" + "Напряжение, даН/мм2 \t" + "Стрела провеса, м \t\t" + "Тяжение, кгс");
        for (int i = - 40; i <= 50; i += 5) {
            MountLoad = LoadCalc(y1, Yref, i, Ttef, Gref);
            Sag = Sag(y1, MountLoad);
            Tension = MountLoad * CrSec;
            System.out.println("\t" + i + "\t\t | \t\t" + Myformat.format(MountLoad) + "\t\t\t | \t\t\t" + Myformat.format(Sag) + "\t\t | \t\t" + Myformat.format(Tension));
//            System.out.println("t = " + i + "\t" + "Напряжение, даН/мм2 = " + Myformat.format(MountLoad) + "\t" + "Стрела провеса, м = " + Myformat.format(Sag) + "\t" + "Тяжение, кгс = " + Myformat.format(Tension));
        }
    }
}
