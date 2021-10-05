package org.processmining.Gather;

public class Tuple {
	private String first;
	private String second;
	public Tuple()
	{
		first = new String();
		second = new String();
	}
	public Tuple(String f , String s)
	{
		this();
		first = f ; 
		second = s ;
	}
	public String getFirst()
	{
		return first;
	}
	public String getSecond()
	{
		return second;
	}
	public int hashCode()
	{
		int i ;
		i = first.toString().hashCode()*100 + second.toString().hashCode();
		return i;
	}
	public boolean equals(Object obj)
	{
		if(obj instanceof Tuple)
		{
			Tuple t = (Tuple) obj;
			if(t.getFirst().equals(first)&&t.getSecond().equals(second))
			{
				return true;
			}
		}
		return false;
	}
	public String toString()
	{
		return "("+first+","+second+")";
	}

}
