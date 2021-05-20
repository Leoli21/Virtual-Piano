
public class Note 
{
	private int keyNum;
	private double tHeld;
	
	public Note(int key, double timeHeld)
	{
		this.keyNum = key;
		this.tHeld = timeHeld;
	}
	
	public int getKeyNum()
	{
		return keyNum;
	}
	
	public double getTimeHeld()
	{
		return tHeld;
	}
	
	public void setTimeHeld(double duration)
	{
		tHeld = duration;
	}
}
