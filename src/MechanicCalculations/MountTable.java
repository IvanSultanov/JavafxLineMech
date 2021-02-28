package MechanicCalculations;

public class MountTable extends WireMechLoad {

    double MountLoad, MountLoadIce;
    double Tension, TensionIce;
    double Sag, SagIce;

    MountTable (double Distance, int WindReg, int YearRate, double Ice, double Weight, double Diam, double CrosSec,
                  double MaxTens, double LowTempTens, double AverTens, double ElastM, double KLTE, double tmin, double tmax, double taverage, double Tice) {
        super (Distance, WindReg, YearRate, Ice, Weight, Diam, CrosSec, MaxTens, LowTempTens, AverTens, ElastM, KLTE, tmin, tmax, taverage, Tice);

    }
    public void  Calculation (double Distance, double Temp, double K_wind, double K_ice) {

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
//        q = 100;
//    System.out.println(q);
// Сх - коэффициент лобового сопротивления
        if ((KabDiam < 20) | c > 0 ) Cx = 1.2;
        if ((KabDiam > 20) & c == 0 ) Cx = 1.1;

//        double K_ice = 1.000;
//        double K_wind = 1.2;

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
        System.out.println(Distance);

///////////////////////////////////////////////////////
        if ((L1k < L2k) & (L2k < L3k) & (Distance < L1k)) {
            System.out.println("L1k < L2k < L3k, режим Н");
            MountCalc (y1, tmin, MaxTens, Temp, Distance);
        }
////////////////////////////////////////////////////////
        if ((L1k < L2k) & (L2k < L3k) & (Distance > L1k) & (Distance < L3k)) {
            System.out.println("L1k < L2k < L3k, режим С");
            MountCalc (y1, taverage, AverTens, Temp, Distance);
        }
////////////////////////////////////////////////////////
        if ((L1k < L2k) & (L2k < L3k) & (Distance > L3k)) {
            System.out.println("L1k < L2k < L3k, режим Г");
            MountCalc (y7, tIce, MaxTens, Temp, Distance);
        }
////////////////////////////////////////////////////////
        if ((L1k > L2k) & (L2k > L3k) & (Distance < L2k)) {
            System.out.println("L3k < L2k < L1k, режим Н");
            MountCalc (y1, tmin, MaxTens, Temp, Distance);
        }
////////////////////////////////////////////////////////
        if ((L1k > L2k) & (L2k > L3k) & (Distance > L2k)) {
            System.out.println("L3k < L2k < L1k, режим Г");
            MountCalc (y7, tIce, MaxTens, Temp, Distance);
        }
////////////////////////////////////////////////////////
        if (Double.isNaN(L1k) & (Distance < L3k)) {
            System.out.println("L1k мнимый, режим C");
            MountCalc (y1, taverage, AverTens, Temp, Distance);
        }
////////////////////////////////////////////////////////
        if (Double.isNaN(L1k) & (Distance > L3k)) {
            System.out.println("L1k мнимый, режим Г");
            MountCalc (y7, tIce, MaxTens, Temp, Distance);
        }
////////////////////////////////////////////////////////
        if (Double.isNaN(L3k) & (Distance < L1k)) {
            System.out.println("L3k мнимый, режим Н");
            MountCalc(y1, tmin, MaxTens, Temp, Distance);
        }
////////////////////////////////////////////////////////
        if (Double.isNaN(L3k) & (Distance > L1k)) {
            System.out.println("L3k мнимый, режим C");
            MountCalc(y1, taverage, AverTens, Temp, Distance);
        }
////////////////////////////////////////////////////////
        if (Double.isNaN(L1k) & Double.isNaN(L3k)) {
            System.out.println("L1k и L3k мнимые, режим C");
            MountCalc(y1, taverage, AverTens, Temp, Distance);
        }

    }

    public double LoadCalc (double y, double yRef, double t, double tRef, double Gref, double Distance) {
        double G = 1.00;
        double X;

        while (true) {
            X = G - ((y * y * ElastM * Distance * Distance) / (24 * G * G))
                    - Gref + ((Math.pow(yRef, 2) * ElastM * Distance * Distance) / (24 * Gref * Gref))
                    + (KLTE * ElastM * (t - tRef));
            G = G + 0.01;
            if (X > 0) {
                break;
            }
        }
        return G;
    }

    public void MountCalc (double Yref, double Ttef, double Gref, double Tempi, double Distance) {

        MountLoad = LoadCalc(y1, Yref, Tempi, Ttef, Gref, Distance);
        Tension = MountLoad * CrSec;
        Sag = y1 * Distance * Distance / (8 * MountLoad);

        MountLoadIce = LoadCalc(y3, Yref, Tempi, Ttef, Gref, Distance);
        TensionIce = MountLoadIce * CrSec;
        SagIce = y3 * Distance * Distance / (8 * MountLoadIce);

    }

    public double getMountLoad() {
        return MountLoad;
    }
    public double getTension() {
        return Tension;
    }
    public double getSag() {
        return Sag;
    }

    public double getMountLoadIce() {
        return MountLoadIce;
    }
    public double getTensionIce() {
        return TensionIce;
    }
    public double getSagIce() {
        return SagIce;
    }

}
