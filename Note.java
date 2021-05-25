public class Note 
{
	private int keyNum;
	private double tHeld;
	private boolean isRest;
	
	public Note(int key, double timeHeld, boolean resting)
	{
		this.keyNum = key;
		this.tHeld = timeHeld;
		isRest = resting;
		
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
	public boolean checkIfRest()
	{
		return isRest;
	}
}