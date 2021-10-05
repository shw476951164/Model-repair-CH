package org.processmining.Relation;

public enum Relations {
	DirectFollow,
	DirectCasually,
	Parallel,
	NoRel,
	Loop1,
	Loop2,
	reDirectFollow,
	reDirectCasually,
	reParallel,
	reNoRel,
	reLoop1,
	reLoop2;
	public String toString()
	{
		String symbale = new String();
		symbale = "";
		switch(this)
		{
		   case DirectFollow:
			   symbale = " >";
			   break;
		   case DirectCasually:
			   symbale = "->";
			   break;
		   case Parallel:
			   symbale = "||";
			   break;
		   case Loop1:
			   symbale = "O>";
			   break;
		   case Loop2:
			   symbale = "O>";
			   break;
		   case reDirectFollow:
			   symbale = " <";
			   break;
		   case reDirectCasually:
			   symbale = "<-";
			   break;
		   case reParallel:
			   symbale = "||";
			   break;
		   case reLoop1:
			   symbale = "<O";
			   break;
		   case reLoop2:
			   symbale = "<O";
			   break;
		   default:
			   symbale = " #";
			   break;
		}
		return symbale;
	}
	public Relations getReRelation()
	{
		Relations symbale;
		switch(this)
		{
		   case DirectFollow:
			   symbale = Relations.reDirectFollow;
			   break;
		   case DirectCasually:
			   symbale = Relations.reDirectCasually;
			   break;
		   case Parallel:
			   symbale = Relations.reParallel;
			   break;
		   case Loop1:
			   symbale = Relations.reLoop2;
			   break;
		   case Loop2:
			   symbale = Relations.reLoop2;
			   break;
		   case reDirectFollow:
			   symbale = Relations.DirectFollow;
			   break;
		   case reDirectCasually:
			   symbale = Relations.DirectCasually;
			   break;
		   case reParallel:
			   symbale = Relations.Parallel;
			   break;
		   case reLoop1:
			   symbale = Relations.reLoop2;
			   break;
		   case reLoop2:
			   symbale = Relations.Loop2;
			   break;
		   default:
			   symbale = Relations.NoRel;
			   break;
		}
		return symbale;
	}

}
