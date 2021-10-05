package org.processmining.FootMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.processmining.Data.MyEvent;
import org.processmining.Data.MyLog;
import org.processmining.Gather.Tuple;
import org.processmining.Relation.Relations;

public class Matrix {
	private MyLog mylog;
	private Relations[][] relation;
	private HashMap<String,Integer> StoImap ;
	private HashMap<Integer,String> ItoSmap;
	public Matrix(MyLog log)
	{
		mylog = log ;
		
		StoImap = new HashMap<String , Integer>();//字符串转换成整型
		ItoSmap = new HashMap<Integer ,String>();//整型转换成字符串
		initial();
		createFootMatrix();//构建足迹矩阵
	}
	public HashMap<String, Integer> getStoImap()
	{
		return StoImap;
	}
	public HashMap<Integer , String> getItoSmap()
	{
		return ItoSmap;
	}
	private void initial()
	{
		ArrayList<MyEvent> eventlist = new ArrayList<MyEvent>();//新建一个事件列表
		eventlist.addAll(mylog.getMyEventSet());//将事件添加到事件列表中去
		
		System.out.println("所有事件列表:" + eventlist);//生成所有事件列表
		
		
		int len = eventlist.size();
		relation = new Relations[len][len];//获取事件日志之间的 关系
		
		for(int x = 0 ; x < len ; x++)
		{
			for(int y = 0 ; y < len ; y++)
			{
				relation[x][y] = Relations.NoRel;
			}
		}
		//给关系数组赋初始值。
    System.out.println("关系数组初始值");	
	for(int i=0;i < len ; i++)
	{
		for(int j = 0 ; j < len ; j++)
		{
		System.out.print(relation[i][j]);
		}
		System.out.println();//换行
	}
		
	
		
		for(int i = 0  ; i < eventlist.size() ; i ++)
		{
			MyEvent event = eventlist.get(i);
			String s = event.getName();
			StoImap.put(s, i);
			ItoSmap.put(i, s);
		}
	}
	
	//构建足迹矩阵
	private void createFootMatrix()
	{
		ArrayList<Tuple> tuplelist = new ArrayList<Tuple>();//二元数组列表
	    tuplelist.addAll(mylog.getAllTuple());
	    
	    
		ArrayList<Tuple> parallellist = new ArrayList<Tuple>();//存放并发关系列表
		ArrayList<Tuple> casuallist = new ArrayList<Tuple>();//存放因果关系序列
		while(tuplelist.size()>0)
		{
			Tuple tuple = tuplelist.get(0);
			Tuple retuple = new Tuple(tuple.getSecond(),tuple.getFirst());
			if(tuplelist.contains(retuple))
			{
				tuplelist.remove(retuple);
				tuplelist.remove(tuple);
				parallellist.add(tuple);
			}
			else
			{
				tuplelist.remove(tuple);
				casuallist.add(tuple);
			}
		}
		for(int i = 0 ; i < parallellist.size() ; i ++)
		{
			Tuple t = parallellist.get(i);
			String f = t.getFirst();
			String s = t.getSecond();
			int x = StoImap.get(f);
			int y = StoImap.get(s);
			relation[x][y] = Relations.Parallel;
			relation[y][x] = Relations.Parallel;
		}
		for(int i = 0 ; i < casuallist.size() ; i ++)
		{
			Tuple t = casuallist.get(i);
			String f = t.getFirst();
			String s = t.getSecond();
			int x = StoImap.get(f);
			int y = StoImap.get(s);
			relation[x][y] = Relations.DirectCasually;
			relation[y][x] = Relations.reDirectCasually;
		}
	}
	public void changeParalleltoCasual(String s1,String s2)
	{
		int x = StoImap.get(s1);
		int y = StoImap.get(s2);
		relation[x][y] = Relations.DirectCasually;
		relation[y][x] = Relations.DirectCasually;
	}
	public void changeParalleltoLoop2(String s1,String s2)
	{
		int x = StoImap.get(s1);
		int y = StoImap.get(s2);
		relation[x][y] = Relations.Loop2;
		relation[y][x] = Relations.reLoop2;
	}
	public void changeNoReltoLoop1(String s1)
	{
		int x = StoImap.get(s1);
		int y = StoImap.get(s1);
		relation[x][y] = Relations.Loop1;
		relation[y][x] = Relations.reLoop1;
	}
	public void loopntoCasual(String s1,String s2)
	{
		int x = StoImap.get(s1);
		int y = StoImap.get(s2);
		relation[x][y] = Relations.DirectCasually;
		relation[y][x] = Relations.DirectCasually;
	}
	public Set<Tuple> getCasualSet()
	{
		Set<Tuple> tupleset = new HashSet<Tuple>();
		for(int i = 0 ; i < relation.length ; i++)
		{
			for(int j = 0 ; j < relation.length ; j++)
			{
				if(relation[i][j].equals(Relations.DirectCasually))					
				{
					tupleset.add(new Tuple(ItoSmap.get(i), ItoSmap.get(j)));
				}
			}
		}
		return tupleset;
	}
	
	public Set<Tuple> getParallelSet()
	{
		Set<Tuple> tupleset = new HashSet<Tuple>();
		for(int i = 0 ; i < relation.length ; i++)
		{
			for(int j = 0 ; j < relation.length ; j++)
			{
				if(relation[i][j].equals(Relations.Parallel))					
				{
					tupleset.add(new Tuple(ItoSmap.get(i), ItoSmap.get(j)));
				}
			}
		}
		return tupleset;
	}
	
	public Relations getRelation(String s1, String s2)
	{
		return relation[StoImap.get(s1)][StoImap.get(s2)];
	}
	
	
    public void ShowMatrix()
    {
    	int length = relation.length;
    	String [] [] tostr = new String[length+1][length+1];
    	tostr[0][0] = " ";
    	for(int i = 1 ; i <= length ; i ++ )
    	{
    		tostr[0][i] = ItoSmap.get(i-1);
    		tostr[i][0] = ItoSmap.get(i-1);
    	}
    	for(int i = 0 ; i < length ; i++)
    	{
    		for(int j = 0 ; j <length ; j++)
    		{
    			tostr[i+1][j+1] = relation[i][j].toString();
    		}
    	}
    	for(int i = 0 ; i <= length ; i ++)
    	{
    		for(int  j = 0 ; j <= length ; j++)
    		{
    			System.out.printf("%4s",tostr[i][j]);
    		}
    		System.out.println();
    	}
    	
    }
	public Relations[][] getRelations()
	{
		return relation;
	}
	
	
	
	
	
}
