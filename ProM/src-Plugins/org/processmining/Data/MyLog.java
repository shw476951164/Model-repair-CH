package org.processmining.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.FootMatrix.Matrix;
import org.processmining.Gather.Trid;
import org.processmining.Gather.Tuple;

public class MyLog extends ArrayList<MyTrace> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private TreeSet<MyEvent> myeventset ;
	public MyLog(XLog xlog)
	{
		myeventset = new TreeSet<MyEvent>();
		changeXLogtoMyLog(xlog);
	}
	private void changeXLogtoMyLog(XLog log)
	{
		for(XTrace xtrace:log)
		{
			MyTrace mytrace = new MyTrace();
			for(XEvent xevent:xtrace)
			{
				XAttributeMap attributemap = xevent.getAttributes();
				XAttribute attribute = attributemap.get("concept:name");
				MyEvent myevent = new MyEvent(attribute.toString());
				mytrace.add(myevent);
				myeventset.add(myevent);
			}
			add(mytrace);
		}
	}
	public Set<Tuple> getAllTuple()
	{
		Set<Tuple> tupleset = new HashSet<Tuple>();
		for(int i = 0 ; i < size() ; i ++)
		{
			tupleset.addAll(get(i).getAllTuple());
		}
		return tupleset;
	}
	public Set<String> getAllN2set()
	{
		Set<String> strset = new HashSet<String>();
		Set<String> str2set = new HashSet<String>();
		for(int i = 0 ; i < size() ; i ++)
		{
			strset.addAll(get(i).getNum2set());
		}
		for (String str : strset) {  
			for(int i = 0 ; i < size() ; i ++)
			{
				if(get(i).getEventnumber(str)==0)
				{
					str2set.add(str);
					break;
				}
				else continue;
			}
		}
		//System.out.println("FreeActivitySet:"+str2set);
		return str2set;
		
	}
	public ArrayList<Trid> createLoopTrid()
	{
		ArrayList<Trid> tridlist = new ArrayList<Trid>();
		ArrayList<String> alln2list = new ArrayList<String>(this.getAllN2set());
		HashMap<Integer, Trid> ItoTmap = new HashMap<Integer, Trid>();
		HashMap<String , Integer> StoImap = new HashMap<String , Integer>();
		for(int i = 0 ; i < alln2list.size() ; i++)
		{
			String s = alln2list.get(i);
			Trid trid = new Trid(s);
			tridlist.add(trid);
			ItoTmap.put(i, trid);
			StoImap.put(s, i);
		}
		
		ArrayList<Tuple> tuplelist = new ArrayList<Tuple>(this.getAllTuple());
		for(int i = 0 ; i < tuplelist.size() ; i++)
		{
			Tuple tuple = tuplelist.get(i);
			String first = tuple.getFirst();
			String second = tuple.getSecond();
			if(StoImap.keySet().contains(first))
			{
				int pos1 = StoImap.get(first);
				Trid t1 = tridlist.get(pos1) ;
				t1.addPostSet(second);
			}
			
			if(StoImap.keySet().contains(second))
			{
				int pos2 = StoImap.get(second);
				Trid t2 = tridlist.get(pos2);
				t2.addPreSet(first);
			}
		}
		
		return tridlist;
		
	}
	
	public Matrix createMatrix()
	{
		Matrix matrix = new Matrix(this);
		return matrix;
	}
	public TreeSet<MyEvent> getMyEventSet()
	{
		return myeventset;
	}
	public MyTrace getFirstTrace()
	{
		return get(0);
	}
	public MyTrace getTrace(Integer i)
	{
		return get(i);
	}

}
