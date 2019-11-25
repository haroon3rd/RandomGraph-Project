package ranGraph;

import java.util.*;

import org.apache.log4j.Logger;

public class DijkstraAlg {
	public static final Logger logger = Logger.getLogger(DijkstraAlg.class);

	public Character status[];
	public int dad[];
	public double bw[];
	//public static int fringe[];
	
	//constructor
	public DijkstraAlg(int v) {
		
		//set status from 1 to v(not 0 to v-1)
		status = new Character[v+1];
		dad = new int[v+1];
		bw = new double[v+1];
		//initialize the status of all vertices to be unseen
		// dad to be 0, and bw to be infinity
		for (int i = 1; i <= v; i++) {
			status[i] = 'u';
			dad[i] = 0;
			bw[i] = Double.POSITIVE_INFINITY;;
		}
		
		status[0] = 'p';
		dad[0] = -1;
		bw[0] = 0.0;
	}
	
	
	public void setStatus(Character status[], int i, Character s){
		status[i] = s;
	}
	
	public void relax(int v, int i, List<Integer> list1, List<Map<Integer, Double>> list2){
		int w = list1.get(i);
		double weight_at_w = list2.get(i).get(w);
		if(status[w] == 'u'){
			status[w] = 'f';
			dad[w] = v;
			bw[w] = Math.min(bw[v], weight_at_w);
		}else if(status[w] == 'f' && bw[w] < Math.min(bw[v], weight_at_w)){
			dad[w] = v;
			bw[w] = Math.min(bw[v], weight_at_w);
		}
	}
	
	public int[] rout(RandomGraph rg, int s, int t, int v ){
		//rg.presentGraph(rg, v);
		status[s] = 'i';
		
		//fringe= new int[v+1]  !!!there is no vertex at fringe[0]
		List<Integer> sourcelist1 = rg.getEdge(s);
		List<Map<Integer, Double>> sourceList2 = rg.getEdgeWeight(s);
		
		for(int i = 1; i<= sourceList2.size(); i++){
			int w = sourcelist1.get(i-1);
			status[w] = 'f';
			dad[w] = s;
			bw[w] = sourceList2.get(i-1).get(w);
		}
		
		while(Arrays.asList(status).contains('f')){
			double max = 0.0;
			int max_index = 0;
			for(int i = 1; i <= v; i++){
				if(status[i] == 'f' && bw[i] > max){
					max = bw[i];
					max_index = i;
				}
			}
			
			status[max_index] = 'i';	//put the maximum fringe into 'in-tree'
		
			//for each [max_index, w], in which max_index is the maximum in the fringe
			List<Integer> edgeList1 = rg.getEdge(max_index);
			List<Map<Integer, Double>> edgeWeightList1 = rg.getEdgeWeight(max_index);
			for(int i = 0; i < edgeList1.size(); i++){
				relax(max_index, i, edgeList1, edgeWeightList1);	
			}
		}
//		for(int i = 0;i<dad.length; i++){
//			if(dad[i] == 0){
//				int a = 3;
//			}
//		}
		return dad;
	}


}
