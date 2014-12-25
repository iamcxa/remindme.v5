package tw.geodoer.mPriority.API;

public class example
{
    public static void main(String[] args)
    {
        remindweight fun = new remindweight(1000,5,60,10);
        //new remindweight(最大指數(int) , 斜率(int) , 時間單位(分鐘)(int) , 距離單位(公尺)(int))

        // .getweight(時間(int),距離(int))   (int)
        System.out.println(" 0 min , 0 meter");
        System.out.println("= "+ fun.getweight(0,0));

        System.out.println(" 10 min , 0 meter");
        System.out.println("= "+ fun.getweight(10,0));

        System.out.println(" 10 min , 10 meter");
        System.out.println("= "+ fun.getweight(10,10));

        System.out.println(" 0 min , 11 meter");
        System.out.println("= "+ fun.getweight(0,11));

        System.out.println(" 10 min , 11 meter");
        System.out.println("= "+ fun.getweight(10,11));

        System.out.println(" 11 min , 10 meter");
        System.out.println("= "+ fun.getweight(11,10));


        System.out.println(" 600 min , 100 meter");
        System.out.println("= "+ fun.getweight(600,100));

        System.out.println(" 1440 min , 100 meter");
        System.out.println("= "+ fun.getweight(1440,100));
    }
}