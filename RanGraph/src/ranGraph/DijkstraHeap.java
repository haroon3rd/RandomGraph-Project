package ranGraph;


import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class DijkstraHeap {
	public static final Logger logger = Logger.getLogger(DijkstraHeap.class);

	public char status[];
	public int dad[];
	public int[] bw;
	HeapStruct heapForFringe;
	
	public DijkstraHeap(int v){
		
		//create a heap for fringe
		heapForFringe = new HeapStruct(v+1);
		
		//set 3 arrays from 1 to v(not from 0 to v-1)
		status = new char[v+1];
		dad = new int[v+1];
		bw = new int[v+1];
		
		//initialize the status of all vertices to be unseen
		//dad to be 0, and bw to be infinity
		for(int i = 1; i <= v; i++){
			status[i] = 'u';
			dad[i] = 0;
			bw[i] = (int) Double.POSITIVE_INFINITY;
		}
		
		status[0] = 'p';
		dad[0] = -1;
		bw[0] = (int) 0.0;
	}
	

	
	public void relax(int v, int i, List<Integer> list1, List<Map<Integer, Integer>> list2){
		int w = list1.get(i); 
		double weight_on_w = list2.get(i).get(w);
		if(status[w] == 'u'){
			status[w] = 'f';
			dad[w] = v;
			heapForFringe.vertValues[w] = (int) Math.min(heapForFringe.vertValues[v], weight_on_w);
			heapForFringe.Insert(w);
		}else if(status[w] == 'f' && heapForFringe.vertValues[w] < Math.min(heapForFringe.vertValues[v], weight_on_w)){
			
			int wrongPos = heapForFringe.index[w];
			heapForFringe.Delete(wrongPos);
			dad[w] = v;
			heapForFringe.vertValues[w] = (int) Math.min(heapForFringe.vertValues[v], weight_on_w);
			heapForFringe.Insert(w);
		}
	}
	
	public int[] dijkstraHeapRout(RandomGraph rg, int s, int t, int v){
		status[s] = 'i';
		List<Integer> sourceList1 = rg.getEdge(s);
		List<Map<Integer, Integer>> sourceList2 = rg.getEdgeWeight(s);
//		List<Map<Integer, Double>> sourceList2 = rg.getEdgeWeight(s);
		
		//Dealing with 0th position.
		int w0 = sourceList1.get(0);
		status[w0] = 'f';
		dad[w0] = s;
		heapForFringe.vertValues[w0] = sourceList2.get(0).get(w0);
		heapForFringe.maxHeap[0] = w0;
		
		try {
			for(int i = 1; i<sourceList2.size();i++){
				int w = sourceList1.get(i);
				status[w] = 'f';
				dad[w] = s;
				heapForFringe.vertValues[w] = sourceList2.get(i).get(w);
				heapForFringe.Insert(w);
			}
		} catch (Exception e) {
			logger.error("Error in dijkstraHeapRout for Loop" + e);
		}

		try {
			while(heapForFringe.size != -1){
				int w = heapForFringe.Max();
				heapForFringe.Delete(0);
				status[w] = 'i';
				List<Integer> edgeList1 = rg.getEdge(w);
				List<Map<Integer, Integer>> edgeList2 = rg.getEdgeWeight(w);
//				List<Map<Integer, Double>> edgeList2 = rg.getEdgeWeight(w);
				for(int i = 0; i < edgeList1.size(); i++){
					relax(w, i, edgeList1, edgeList2);
				}
			}
		} catch (Exception e) {
			logger.error("Error in dijkstraHeapRout while Loop" + e);
		}
		return dad;
	}

}
