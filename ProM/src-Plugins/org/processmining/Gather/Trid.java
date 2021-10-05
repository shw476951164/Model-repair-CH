package org.processmining.Gather;

import java.util.HashSet;
import java.util.Set;

public class Trid {
	private String name ;
	private HashSet<String> preset;
    private HashSet<String> postset;
    public Trid()
    {
    	name = "";
    	preset = new HashSet<String>();
    	postset = new HashSet<String>();
    }
	public Trid(Set<String> pre , Set<String> post)
	{
		this();
		preset.addAll(pre);
		postset.addAll(post);
	}
	public Trid(String n,Set<String> pre , Set<String> post)
	{
		this(pre,post);
		name = n;
	}
	public Trid(String n)
	{
		this();
		name = n;
	}
	public void addPreSet(Set<String> s)
	{
		preset.addAll(s);
	}
	public void addPostSet(Set<String> s)
	{
		postset.addAll(s);
	}
	
	public void addPreSet(String s)
	{
		preset.add(s);
	}
	public void addPostSet(String s)
	{
		postset.add(s);
	}
	
	public Set<String> getPreSet()
	{
		return preset;
	}
	
	public Set<String> getPostSet()
	{
		return postset;
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
		if(obj instanceof Trid)
		{
			Trid t = (Trid) obj;
			if(t.getName().equals(name))
			{
				return true;
			}
		}
		return false;
		
	}
	
	public String toString()
	{
		return name+":  "+preset.toString()+"   "+postset.toString();
	}
	
	
	
	
	
	
	
	

}
