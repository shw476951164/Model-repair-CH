package org.processmining.Alignment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.deckfour.xes.model.XLog;
import org.processmining.Data.MyLog;
import org.processmining.Data.MyTrace;
import org.processmining.FootMatrix.Matrix;
import org.processmining.Relation.Relations;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.plugins.petrinet.replayer.PNLogReplayer;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;
import org.processmining.plugins.replayer.replayresult.SyncReplayResult;

import nl.tue.astar.AStarException;

public class RepairModel {
	@Plugin(name = "Model Repair-CH", parameterLabels = { "Petri net", "XLog" }, returnLabels = { "new pn" }, returnTypes = { Petrinet.class }, userAccessible = true, help = "This method is to repair an original model to a process model with free loop structures.")
	@UITopiaVariant(affiliation = "Shandong University of Science and Technology", author = "Hongwei Sun", email = "sunhongwei_doctor@yeah.net.com")
	public Petrinet doMining(UIPluginContext context, Petrinet net, XLog log) throws ConnectionCannotBeObtained,
			AStarException {
		PNRepResult result;
		PNLogReplayer replayer = new PNLogReplayer();
		result = replayer.replayLog(context, net, log);
		Petrinet newpn = PetrinetFactory.clonePetrinet(net);
		ArrayList<String> flactivities = new ArrayList<String>();
		ArrayList<ArrayList> flalist = new ArrayList<ArrayList>();
		for (SyncReplayResult res : result) {
			ArrayList<String> flal = new ArrayList<String>();
			for (int i = 1; i < res.getStepTypes().size(); i++) {
				if (res.getStepTypes().get(i) == org.processmining.plugins.petrinet.replayresult.StepTypes.L) {
					String a = res.getNodeInstance().get(i).toString();
					flal.add(a);
					if (!flactivities.contains(a))
						flactivities.add(a);
				}

			}
			if (!flal.isEmpty()) {
				flalist.add(flal);
			}
		}
		MyLog mylog = new MyLog(log);
		Matrix matrix = mylog.createMatrix();
		ArrayList<String> allfla = new ArrayList<String>();
		for (Transition t : newpn.getTransitions()) {
			String tlabel = t.getLabel();
			int num0flag = 1;
			int num2flag = 2;
			for (int len = 0; len < mylog.size(); len++) {
				MyTrace mytrace = mylog.get(len);
				int tnum = mytrace.getEventnumber(t.getLabel());
				if (tnum == 0) {
					num0flag = 0;
				}
				if (tnum > 1) {
					num2flag = 0;
				}
			}
			if ((num0flag + num2flag == 0) && !allfla.contains(tlabel)) {
				allfla.add(tlabel);
			}
		}
		allfla.removeAll(flactivities);
		

		ArrayList<String> fla_lone = new ArrayList<String>();
		for (int len = 0; len < mylog.size(); len++) {
			MyTrace mytrace = mylog.get(len);
			ArrayList<String> mt = new ArrayList<String>(mytrace.getTraceToStringList());
			for (int i = 1; i < mt.size() - 1; i++) {
				if ((mt.get(i).equals(mt.get(i + 1))) && flactivities.contains(mt.get(i))) {
					if (!fla_lone.contains(mt.get(i))) {
						fla_lone.add(mt.get(i));

					}

				}


			}
		}
		
		for (String fla : fla_lone) {
			matrix.changeNoReltoLoop1(fla);
		}

		ArrayList<String> fla_nolone = new ArrayList<String>(flactivities);
		fla_nolone.removeAll(fla_lone);
		Set<ArrayList> fla_2twopre = new HashSet<ArrayList>();

		for (int len = 0; len < mylog.size(); len++) {
			MyTrace mytrace2 = mylog.get(len);

			ArrayList<String> mt2 = new ArrayList<String>(mytrace2.getTraceToStringList());

			for (int i = 1; i < mt2.size() - 2; i++) {
				if ((mt2.get(i).equals(mt2.get(i + 2))) && (!mt2.get(i).equals(mt2.get(i + 1)))
						&& (fla_nolone.contains(mt2.get(i)) && fla_nolone.contains(mt2.get(i + 2)))) {
					ArrayList<String> abap = new ArrayList<String>();
					abap.add(mt2.get(i));
					abap.add(mt2.get(i + 1));
					abap.add(mt2.get(i + 2));
					if (!fla_2twopre.contains(abap)) {
						fla_2twopre.add(abap);

					}

				}

			}
		}

		ArrayList<ArrayList> fla_2twopr = new ArrayList<ArrayList>(fla_2twopre);
		Set<Set> fla_2twop = new HashSet<Set>();
		for (int i = 0; i < fla_2twopr.size(); i++) {
			ArrayList<String> aba = fla_2twopr.get(i);
			for (int j = 0; j < fla_2twopr.size(); j++) {
				if ((i != j) && (AEB(fla_2twopr.get(i), fla_2twopr.get(j)) == true)) {
					Set<String> ab = new HashSet<String>();
					ab.add(fla_2twopr.get(i).get(0).toString());
					ab.add(fla_2twopr.get(i).get(1).toString());
					fla_2twop.add(ab);
				}
			}
		}

		ArrayList<ArrayList> fla_2two = new ArrayList<ArrayList>();
		Iterator<Set> it = fla_2twop.iterator();

		while (it.hasNext()) {
			Set sab = it.next();
			ArrayList<String> sab2 = new ArrayList<String>(sab);
			String sa = sab2.get(0);
			String sb = sab2.get(1);

			for (int len = 0; len < mylog.size(); len++) {
				MyTrace mytrace3 = mylog.get(len);

				ArrayList<String> mt3 = new ArrayList<String>(mytrace3.getTraceToStringList());
				if (mt3.indexOf(sa) < mt3.indexOf(sb)) {
					ArrayList<String> fla2l = new ArrayList<String>();
					fla2l.add(sa);
					fla2l.add(sb);
					if (!fla_2two.contains(fla2l)) {
						fla_2two.add(fla2l);
					}
				} else if (mt3.indexOf(sa) > mt3.indexOf(sb)) {
					ArrayList<String> fla2l = new ArrayList<String>();
					fla2l.add(sb);
					fla2l.add(sa);
					if (!fla_2two.contains(fla2l)) {
						fla_2two.add(fla2l);
					}
				} else
					continue;
			}
			

		}

		ArrayList<String> fla_long = new ArrayList<String>(fla_nolone);
		for (int i = 0; i < fla_2two.size(); i++) {
			fla_long.remove(fla_2two.get(i).get(0));
			fla_long.remove(fla_2two.get(i).get(1));
		}
		
		ArrayList<ArrayList> fla_longst = new ArrayList<ArrayList>();
		for (int i = 0; i < fla_long.size(); i++) {
			for (int j = 0; j < fla_long.size(); j++) {
				String flals = fla_long.get(i);
				String flalt = fla_long.get(j);
				for (int len = 0; len < mylog.size(); len++) {
					MyTrace mytrace3 = mylog.get(len);
					ArrayList<String> mt2 = new ArrayList<String>(mytrace3.getTraceToStringList());
					if ((mt2.contains(flals)) && (mt2.contains(flalt)) && (i != j)
							&& (mt2.indexOf(flals) < mt2.indexOf(flalt))
							&& (matrix.getRelation(flalt, flals).equals(Relations.DirectCasually))) {
						ArrayList<String> fla_st = new ArrayList<String>();
						
						fla_st.add(flals);
						fla_st.add(flalt);
						if (!fla_longst.contains(fla_st)) {
							fla_longst.add(fla_st);
						}
					}
				}

			}
		}


		for (int i = 0; i < fla_2two.size(); i++) {
			String fl2a = fla_2two.get(i).get(0).toString();
			String fl2b = fla_2two.get(i).get(1).toString();
			matrix.changeParalleltoLoop2(fl2a, fl2b);
		}
		matrix.ShowMatrix();
		ArrayList<String> fla_fls = new ArrayList<String>(fla_lone);
		ArrayList<String> fla_flt = new ArrayList<String>(fla_lone);
		for (int i = 0; i < fla_2two.size(); i++) {
			String fl2a = fla_2two.get(i).get(0).toString();
			String fl2b = fla_2two.get(i).get(1).toString();
			if (!fla_fls.contains(fl2a)) {
				fla_fls.add(fl2a);
			}
			if (!fla_flt.contains(fl2b)) {
				fla_flt.add(fl2b);
			}

		}
		for (int i = 0; i < fla_longst.size(); i++) {
			String flls = fla_longst.get(i).get(0).toString();
			String fllt = fla_longst.get(i).get(1).toString();
			if (!fla_fls.contains(flls)) {
				fla_fls.add(flls);
			}
			if (!fla_flt.contains(fllt)) {
				fla_flt.add(fllt);
			}
		}
		for (String told : allfla) {
			for (String tfl : flactivities) {
				if ((matrix.getRelation(told, tfl).equals(Relations.DirectCasually)) && fla_fls.contains(tfl)) {

					AddIvTLOld(newpn, told, tfl);
				}
				if ((matrix.getRelation(tfl, told).equals(Relations.DirectCasually)) && fla_flt.contains(tfl)) {

					AddIvTROld(newpn, tfl, told);
				}
			}
		}

		ArrayList<String> fls = new ArrayList<String>(fla_lone);
		ArrayList<String> flt = new ArrayList<String>(fla_lone);
		for (ArrayList fla_2 : fla_2two) {
			fls.add(fla_2.get(0).toString());
			flt.add(fla_2.get(1).toString());
		}
		for (ArrayList fla_l : fla_longst) {
			fls.add(fla_l.get(0).toString());
			flt.add(fla_l.get(1).toString());
		}

		for (int i = 0; i < flt.size(); i++) {
			for (int j = 0; j < fls.size(); j++) {
				ArrayList<String> fllte = new ArrayList<String>();
				fllte.add(fls.get(j));
				fllte.add(flt.get(i));
				if ((matrix.getRelation(flt.get(i), fls.get(j)).equals(Relations.DirectCasually))
						&& (!fla_longst.contains(fllte))) {
					AddIvT(newpn, flt.get(i), fls.get(j));
				}

			}

		}

		for (String fl1 : fla_lone) {
			newpn = ChangeToFL(newpn, fl1, fl1);
		}
		for (int i = 0; i < fla_2two.size(); i++) {
			String fl2a = fla_2two.get(i).get(0).toString();
			String fl2b = fla_2two.get(i).get(1).toString();
			newpn = ChangeToFL(newpn, fl2a, fl2b);
		}
		for (int i = 0; i < fla_longst.size(); i++) {
			String flls = fla_longst.get(i).get(0).toString();
			String fllt = fla_longst.get(i).get(1).toString();
			newpn = ChangeToFL(newpn, flls, fllt);
		}
		return newpn;
	}

	public static Petrinet ChangeToFL(Petrinet net, String t1, String t2) {

		Place pa = null, pb = null;
		for (PetrinetEdge i : net.getEdges()) {

			if ((i.getSource() instanceof Place) && (((Transition) i.getTarget()).toString()).equals(t1)) {
				
				pa = (Place) i.getSource();
			}
			if ((i.getTarget() instanceof Place) && (((Transition) i.getSource()).toString()).equals(t2)) {

				pb = (Place) i.getTarget();

			}
		}
		net = CombinePlace(net, pa, pb);

		return net;
	}

	public static Petrinet AddIvTLOld(Petrinet net, String t1, String t2) {
		Transition tpr = findElementsinPN.findTransition(net, t1);
		Transition tpo = findElementsinPN.findTransition(net, t2);
		String labelivt = "it(" + t1 + "," + t2 + ")";

		net.addTransition(labelivt);
		Transition ivt = findElementsinPN.findTransition(net, labelivt);
		ivt.setInvisible(true);
		String labelnewp2 = "[" + labelivt + "," + t2 + "]";
		
		
		for (PetrinetEdge i : net.getEdges()) {
			if (i.getTarget().equals(tpo)) {
				Place p = (Place) i.getSource();


				net.removeArc(p, tpo);
				net.addArc(p, ivt);
			}
		}
		
		net.addPlace(labelnewp2);

		Place newp2 = findElementsinPN.findPlace(net, labelnewp2);
		net.addArc(ivt, newp2);
		net.addArc(newp2, tpo);
		return net;
	}

	public static Petrinet AddIvTROld(Petrinet net, String t1, String t2) {
		Transition tpr = findElementsinPN.findTransition(net, t1);
		Transition tpo = findElementsinPN.findTransition(net, t2);
		String labelivt = "it(" + t1 + "," + t2 + ")";

		net.addTransition(labelivt);
		Transition ivt = findElementsinPN.findTransition(net, labelivt);
		ivt.setInvisible(true);
		

		for (PetrinetEdge i : net.getEdges()) {
			if (i.getSource().equals(tpr)) {
				Place p = (Place) i.getTarget();


				net.removeArc(tpr, p);
				net.addArc(ivt, p);
			}
		}
		String labelnewp1 = "[" + t1 + "," + labelivt + "]";
		net.addPlace(labelnewp1);

		Place newp1 = findElementsinPN.findPlace(net, labelnewp1);

		net.addArc(tpr, newp1);
		net.addArc(newp1, ivt);
		return net;
	}

	public static Petrinet AddIvT(Petrinet net, String t1, String t2) {
		Transition tpr = findElementsinPN.findTransition(net, t1);
		Transition tpo = findElementsinPN.findTransition(net, t2);
		for (PetrinetEdge i : net.getEdges()) {
			if (i.getSource().equals(tpr)) {
				Place p = (Place) i.getTarget();


				net.removeArc(tpr, p);
			}
		}
		for (PetrinetEdge i : net.getEdges()) {
			if (i.getTarget().equals(tpo)) {
				Place p = (Place) i.getSource();


				net.removeArc(p, tpo);
				net.removePlace(p);
			}
		}
		String labelivt = "it(" + t1 + "," + t2 + ")";
		net.addTransition(labelivt);
		Transition ivt = findElementsinPN.findTransition(net, labelivt);
		ivt.setInvisible(true);
		String labelnewp1 = "[" + t1 + "," + labelivt + "]";
		net.addPlace(labelnewp1);
		String labelnewp2 = "[" + labelivt + "," + t2 + "]";
		net.addPlace(labelnewp2);
		Place newp1 = findElementsinPN.findPlace(net, labelnewp1);
		Place newp2 = findElementsinPN.findPlace(net, labelnewp2);
		net.addArc(tpr, newp1);
		net.addArc(newp1, ivt);
		net.addArc(ivt, newp2);
		net.addArc(newp2, tpo);

		return net;
	}

	public static Petrinet CombinePlace(Petrinet net, Place p1, Place p2) {
		for (PetrinetEdge i : net.getEdges()) {
			if (i.getTarget().equals(p2)) {
				Transition t = (Transition) i.getSource();
				
				net.addArc(t, p1);
		
				net.removeArc(t, p2);
			}
			if (i.getSource().equals(p2)) {
				Transition t = (Transition) i.getTarget();
	
				net.addArc(p1, t);

				net.removeArc(p2, t);
			}

		}

		net.removePlace(p2);

		return net;
	}

	public static boolean AEB(ArrayList aba, ArrayList bab) {
		if (aba.get(0).equals(bab.get(1)) && aba.get(1).equals(bab.get(0))) {
			return true;
		}
		return false;
	}

	public static boolean casual(Matrix matrix, ArrayList<String> pre, ArrayList<String> post) {
		boolean bool = true;

		for (int i = 0; i < pre.size(); i++) {
			for (int j = 0; j < post.size(); j++) {
				bool = bool && (matrix.getRelation(pre.get(i), post.get(j)).equals(Relations.DirectCasually));
			}
		}
		return bool;
	}

}
