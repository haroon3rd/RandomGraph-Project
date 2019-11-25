package ranGraph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import java.util.HashMap;

public class KruskalAlg {
	public static final Logger logger = Logger.getLogger(KruskalAlg.class);

	public int edgenumber;
	public int vertexnumber;
	//private List<EdgeObject> edgeList;
	private int[] findArray;
	
	private EdgeObject[] unsortedEdgeVector;
	private EdgeObject[] sortedEdgeVector;
	private double[][] weightMatrix;
	
	//using list to store the edges and weights
	public Map<Integer, List<Integer>> mstAdjacencyList;
	public Map<Integer, List<Map<Integer, Double>>> mstEdgeweightList;
	public int[] dad; 
	public List<EdgeObject>  sortedEdgeList;
	
	public KruskalAlg(EdgeObject[] edgesVector, int totalEdges, double[][] rgWeightMatrix, int v){
		edgenumber = totalEdges;
		vertexnumber = v;
		findArray = new int[vertexnumber+1];
		//edgeList = edgeobjects;
		
		dad = new int[vertexnumber+1];
		sortedEdgeVector = new EdgeObject[edgenumber];
		unsortedEdgeVector = new EdgeObject[edgenumber];
		unsortedEdgeVector = edgesVector;
		setWeightMatrix(new double[vertexnumber+1][vertexnumber+1]);
		setWeightMatrix(rgWeightMatrix);
		
				
		mstAdjacencyList = new HashMap<Integer, List<Integer>>();
		mstEdgeweightList = new HashMap<Integer, List<Map<Integer, Double>>>();
		
		sortedEdgeList = new LinkedList<EdgeObject>();
		
		for(int i = 1; i <= v; i++){
			mstAdjacencyList.put(i, new LinkedList<Integer>());
			mstEdgeweightList.put(i, new LinkedList<Map<Integer, Double>>());
		}
	}
	
	private int find(int v){
		int w = v;
		while(findArray[w] != 0){
			w = findArray[w];
		}
		return w;
	}
	
	public void setEdge(int to, int from, double weight){
		
		if(to > mstAdjacencyList.size() || from > mstAdjacencyList.size())
			System.out.println("The vertices does not exist");
		
		//add vertex to list
		List<Integer> sls = mstAdjacencyList.get(to);
		sls.add(from);
		
		List<Integer> dls = mstAdjacencyList.get(from);
		dls.add(to);
		
		//add vertex and weight to list
		Map<Integer, Double> slsweight = new HashMap<Integer, Double>();
		slsweight.put(from, weight);
		List<Map<Integer, Double>> slsweightList = mstEdgeweightList.get(to);
		slsweightList.add(slsweight);
		
		Map<Integer, Double> dlsweight = new HashMap<Integer, Double>();
		dlsweight.put(to, weight);
		List<Map<Integer, Double>> dlsweightList = mstEdgeweightList.get(from);
		dlsweightList.add(dlsweight);
		
	}
	
	public int[] rout(double[] edgeWeightVector, int s, int t){
		
		HeapStruct heapSortEdge = new HeapStruct(edgenumber);
		heapSortEdge.D = edgeWeightVector;
		heapSortEdge.maxHeap[0] = 0;
		
		for(int i = 1; i < edgenumber; i++){
			heapSortEdge.Insert(i);
		}

		for(int i = 0; i < edgenumber; i++){
			int maxEdge = heapSortEdge.Max();
			//sortedEdgeList.add(edgeList.get(maxEdge));
			sortedEdgeVector[i] = unsortedEdgeVector[maxEdge];
			heapSortEdge.Delete(0);
		}

		for(int i = 0; i < edgenumber; i++){
			//EdgeObject e = sortedEdgeList.get(i);
			EdgeObject e = sortedEdgeVector[i];
			int v1 = find(e.edge1);
			int v2 = find(e.edge2);
			if(v1 != v2){
				setEdge(e.edge1, e.edge2, e.edgeweight);
				findArray[v1] = v2;
			}
		}
		
		char[] color = new char[vertexnumber+1];
		for (int i = 1; i <= vertexnumber; i++){
			color[i] = 'w';
		}
		
		dfs(mstEdgeweightList, mstAdjacencyList, color, s);
		
		return dad;
	}
	
	public void dfs(Map<Integer, List<Map<Integer, Double>>> edgeWeightList, 
		Map<Integer, List<Integer>>	edgeList,	char[] color, int v){			//v is the vertex, not the total vertex number
		
		color[v] = 'g';
		//List<Map<Integer, Double>> currentWeightList = edgeWeightList.get(v);
		List<Integer> currentEdgeList = edgeList.get(v);
		
		if(currentEdgeList.size() > 0){
			for (int w: currentEdgeList){
				
				if(color[w] == 'w'){
					dad[w] = v;
					dfs(edgeWeightList, edgeList, color, w);
				}
			}
		}
		color[v] = 'b';
	}

	public double[][] getWeightMatrix() {
		return weightMatrix;
	}

	public void setWeightMatrix(double[][] weightMatrix) {
		this.weightMatrix = weightMatrix;
	}
}
