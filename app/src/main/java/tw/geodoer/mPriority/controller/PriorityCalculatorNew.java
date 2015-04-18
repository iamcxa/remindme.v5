package tw.geodoer.mPriority.controller;

public class PriorityCalculatorNew
{
	private final double meanW = 500000;
    private final double mean = 50000 ;
    private final double Tw = 60000;
    private final double Lw = 0.0005;

	public double getTw()
	{
		return this.Tw;
	}
	public double getLw()
	{
		return this.Lw;
	}
	public double getMeanW()
	{
		return this.meanW;
	}
	public double getMean()
	{
		return this.mean;
	}

	public PriorityCalculatorNew()
	{

	}

	public double weight(double T,double L)
	{
        double Tpart = this.getMeanW()*this.getTw()*this.getMean()  / (T + this.getTw()*this.getMean() );

        double Lpart = this.getMeanW()*this.getLw()*this.getMean()  / (L + this.getLw()*this.getMean() );

		return Lpart + Tpart;
	}
	public int getweight(long pT, double pL) //pT = millis  , pL = km
	{
        if(pT==0 && pL==0) return 0;

        double T = pT;
        Double result = this.weight(T,pL) ;
        return result.intValue();
	}
}