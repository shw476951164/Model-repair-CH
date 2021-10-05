package org.processmining.Data;

import java.util.ArrayList;


public class MyTransition implements Comparable<MyTransition> {
	private String name;
	private ArrayList<Integer> pos;
	
	public MyTransition()
	{
		name = new String();
		pos = new ArrayList<Integer>();
	}
	public MyTransition(String n)
	{  
		this();
		name = n;
	}
	public String setName(String n)
	{
		name = n;
		return this.getName();
	}
	public String getName()
	{
		return name;
	}
	

	
	
	public int hashCode()
	{
		return name.hashCode();
		
	}
	public boolean equals(Object obj)
	{
		boolean bool = false;
	   if (obj instanceof MyTransition)
	   {
		   MyTransition new_name = (MyTransition) obj;
		   if(new_name.getName().equals(name))
		   {
			   bool =true;
		   }
		}
		return bool;
	}
    public String toString()
    {
    	return this.getName();
    }
	@Override
	public int compareTo(MyTransition o) {
		// TODO Auto-generated method stub
		int size = name.hashCode() - o.getName().hashCode();
		return size;
	}
}
