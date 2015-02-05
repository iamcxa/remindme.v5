package tw.geodoer.mPriority.API;

public class Example_WeightCaculator
{
    public static void main(String[] args)
    {
        WeightCalculator fun = new WeightCalculator();

        // .getweight(時間_millis(double),距離_km(double))   (double)
        System.out.println(" 1000 millis , 0.1 kilometer");
        System.out.println("= "+ fun.getweight(1000,0.1));

    }
}