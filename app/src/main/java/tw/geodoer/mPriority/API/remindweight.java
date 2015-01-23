package tw.geodoer.mPriority.API;

public class RemindWeight
{
	private int meanW;
	private int mean;
	private int Tw;
	private int Lw;
		
	public void setTw(int T)
	{
		this.Tw = T;	
	}
	public int getTw()
	{
		return this.Tw;
	}	
	public void setLw(int L)
	{
		this.Lw = L;	
	}
	public int getLw()
	{
		return this.Lw;
	}
	public void setmeanW(int m)
	{
		this.meanW = m;	
	}
	public int getmeanW()
	{
		return this.meanW;
	}	
	public void setmean(int m)
	{
		this.mean = m;	
	}
	public int getmean()
	{
		return this.mean;
	}
	public RemindWeight(int mW, int m, int T, int L)
	{
		this.setmeanW(mW);
		this.setmean(m);
		this.setTw(T);
		this.setLw(L);
	}
	public RemindWeight()
	{
		this.setmeanW(10000);
		this.setmean(5);
		this.setTw(60);
		this.setLw(10);
	}
	private int subL(int L)
	{
		if( L <= this.getLw() ) return 1;
		else return 0;
	}
	public int weight(int T,int L)
	{
		int Tpart = this.getmeanW()*this.getTw()*this.getmean();
		Tpart = Tpart /  (T + this.getTw()*this.getmean() ) ;
		int Lpart = this.getmeanW()*this.getLw()*this.getmean();
		Lpart = Lpart /  (L + this.getLw()*this.getmean() ) ;
		return Lpart + Tpart;
	}
	public int getweight(int T,int L)
	{
		return this.weight(T,L) + this.subL(L)*weight(0,this.getLw());
	}
}