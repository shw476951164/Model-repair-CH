package org.processmining.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.processmining.Gather.Tuple;

public class MyTrace extends ArrayList<MyEvent> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String , Integer> map ;
	public HashMap<String, ArrayList<Integer>> posmap;
	
    public MyTrace()
    {
    	map = new HashMap<String , Integer>();
    	posmap = new HashMap<String, ArrayList<Integer>>();
    }
	public boolean add(MyEvent e)
	{
		Integer number = map.get(e.getName());
		map.put(e.getName(), number == null ? 1 : number + 1);

		if (posmap.get(e.getName()) == null){
			posmap.put(e.getName(), new ArrayList<Integer>());
			posmap.get(e.getName()).add(size());
		}
		else{
			posmap.get(e.getName()).add(size());
		}
		
		
		return super.add(e);
	}
	public int getEventnumber(String str)
	{
		Integer number = map.get(str);
		return number == null ? 0 : number ;
	}
	public int getEventnumber(MyEvent e)
	{
		Integer number = this.getEventnumber(e.getName());
		return  number ;
	}

	public ArrayList<Integer> getposlist(String e){
		return posmap.get(e);
	}
	
	public Set<String> getAllset()
	{
		ArrayList<String> keylist = new ArrayList<String>(map.keySet());
		Set<String> keyset = new HashSet<String>();
		for(int i = 0 ; i < keylist.size() ; i++)
		{
			String str = keylist.get(i);
				keyset.add(str);
		}
		
		return keyset;
	}
	public Set<String> getNum2set()
	{
		ArrayList<String> keylist = new ArrayList<String>(map.keySet());
		Set<String> keyset = new HashSet<String>();

		for(int i = 0 ; i < keylist.size() ; i++)
		{
			String str = keylist.get(i);
			if(map.get(str)>=2)
			{
				keyset.add(str);
			}
		}
		return keyset;
	}
	public Set<Tuple> getAllTuple()
	{
		Set<Tuple> tupleset = new HashSet<Tuple>();
		for(int i = 0 ; i < size()-1 ; i ++)
		{
			MyEvent e1 = get(i);
			MyEvent e2 = get(i+1);
			String s1 = e1.getName();
			String s2 = e2.getName();
			Tuple tuple = new Tuple(s1,s2);
			tupleset.add(tuple);
		}
		return tupleset;
	}
	public MyEvent getFirstEvent()
	{
		return get(0);
	}
	public MyEvent getLastEvent()
	{
		return get(size()-1);
	}
	public ArrayList<String> getTraceToStringList()
	{
		ArrayList<String> strlist = new ArrayList<String>();
		for(int i = 0 ; i < size() ; i ++)
		{
			strlist.add(get(i).getName());
		}
		return strlist;
	}
}
