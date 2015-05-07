package tw.geodoer.mPriority.controller;

public class PriorityCalculatorNew
{
	private static final double meanW = 500000;
    private static final double mean = 50000 ;
    private static final double Tw = 60000;
    private static final double Lw = 0.0005;
	public PriorityCalculatorNew()
	{

	}

	public double weight(double T,double L)
	{
        double T_part = meanW * Tw * mean  / (T + Tw * mean );

        double L_part = meanW * Lw * mean  / (L + Lw * mean );

		return L_part + T_part;
	}
	public int get_weight(long pT, double pL) //pT = millis  , pL = km
	{
        if(pT==0 && pL==0) return 0;

        double T = pT;
        Double result = this.weight(T,pL) ;

        return result.intValue();
	}
}