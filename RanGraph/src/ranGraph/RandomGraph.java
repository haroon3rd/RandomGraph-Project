package ranGraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;



public class RandomGraph {
	public static final Logger logger = Logger.getLogger(RandomGraph.class);

	public int totalEdges = 0;
	//public int doneVertex = 0;

	private Map<Integer, List<Integer>> adjacencyList;
	private Map<Integer, List<Map<Integer, Integer>>> edgeWeight;
	//	private Map<Integer, List<Map<Integer, Double>>> edgeWeight;
	public List<Edge> edges;

	public int[] edgeWtVect;
	public Edge[] edgesVect;
	public int[][] wtMatrix;

	public RandomGraph(int verts) {
		adjacencyList = new HashMap<Integer, List<Integer>>();
		edgeWeight = new HashMap<Integer, List<Map<Integer, Integer>>>();
		//		edgeWeight = new HashMap<Integer, List<Map<Integer, Double>>>();

		edges = new LinkedList<Edge>();

		edgeWtVect = new int[5000000]; //(20%*5000 = 1000) * 5000
		edgesVect = new Edge[5000000];
		wtMatrix = new int[5001][5001];

		for (int i = 1; i <= verts; i++) {
			adjacencyList.put(i, new LinkedList<Integer>());
			edgeWeight.put(i, new LinkedList<Map<Integer, Integer>>());
		}
	}

	public void setEdge(int to, int from) {
		if (to > adjacencyList.size() || from > adjacencyList.size())
			System.out.println("The vertices do not exist");

		List<Integer> srcList = adjacencyList.get(to);
		srcList.add(from);

		List<Integer> destList = adjacencyList.get(from);
		destList.add(to);

		// create instance of Random class 
		Random rand = new Random();
		int weight = rand.nextInt(100)+1; //A weight of 0 to 100 randomly chosen

		Map<Integer, Integer> srcListweight = new HashMap<Integer, Integer>();
		srcListweight.put(from, weight);
		List<Map<Integer, Integer>> weightlist1 = edgeWeight.get(to);
		weightlist1.add(srcListweight);

		Map<Integer, Integer> destListweight = new HashMap<Integer, Integer>();
		destListweight.put(to, weight);
		List<Map<Integer, Integer>> weightlist2 = edgeWeight.get(from);
		weightlist2.add(destListweight);

//		if (this.getEdge(to).size() == 6)
//			this.doneVertex++;
//
//		if(this.getEdge(from).size() == 6)
//			this.doneVertex++;

		Edge edgeobject = new Edge();
		edgeobject.edge1 = to;
		edgeobject.edge2 = from;
		edgeobject.edgeweight = weight;

		edgesVect[totalEdges] = edgeobject;
		edgeWtVect[totalEdges] = weight;
		wtMatrix[to][from] = weight;
		wtMatrix[from][to] = weight;

		totalEdges++;
	}

	public List<Integer> getEdge(int to) {
		try{
			if (adjacencyList==null && (to > adjacencyList.size())) {
				System.out.println("getEdge - The vertices do not exist");
				return null;
			}
		}catch (Exception e) {
			logger.error("Error" + e);
		}
		return adjacencyList.get(to);
	}

	public List<Map<Integer, Integer>> getEdgeWeight(int to) {
		if (to > adjacencyList.size()) {
			System.out.println("getEdgeWeight - The vertices do not exist");
			return null;
		}
		return edgeWeight.get(to);
	}

	//This will be called for first Graph
	public void applyEdges(RandomGraph ranGraph, int v, int edgePerV) {
		logger.info("I am here.....");
		int count = 1, to, from;
		try {
			while (count <= v) {
				logger.info("I am here..... in while " + count);
				from = count;
				int toCount = 1;
				while (ranGraph.getEdge(from).size() < edgePerV) {
					Random dest = new Random();
					to = dest.nextInt(v)+1;
					try {
						if (to != from && !ranGraph.getEdge(from).contains(to) &&!ranGraph.getEdge(to).contains(from)) {
							ranGraph.setEdge(to, from);
							if(toCount>6)
								break;
						}
					}catch (Exception e) {
						logger.error("Error in applyEdges nested IF");
					}
				}
				count++;
			}
		}catch (Exception e) {
			logger.error("Error in applyEdges while loop");
		}
		System.out.println("Random Edge and Weight Applied to " + (count-1) + " vertices.");
	}


	//This will be called for second graph
	public void applyEdges(RandomGraph ranGraph, int v) {
		int count = 1, to, from;
		try {
			while (count <= v) {
				from = count;
				int k = count;
				int toCount = 1;
				while (k <= v) {
					to = k++;
					double random2 = Math.random();
					if (random2 <= 0.2 && to != from && !ranGraph.getEdge(from).contains(to)
							&&!ranGraph.getEdge(to).contains(from)) {
						ranGraph.setEdge(to, from);
						toCount++;
						if(toCount>0.2*v)
							break;
					}
				}
				count++;
			}
		}catch (Exception e) {
			logger.error("Error in applyEdges while loop Dense Graph");
		}
		System.out.println("Random Edge and Weight Applied to " + (count-1) + " vertices.");
	}


	public int[][] pickSourceSink(int sourcesink[][], int v){
		//randomly generate 5 pairs of sources and sinks
		Random rand = new Random();
		int i = 0;
		while (i < 5) {
			int s = rand.nextInt(v)+1;
			int t = rand.nextInt(v)+1;
			if (s != t && s != 1 && s != v && t != 1 && t != v) {
				sourcesink[i][0] = s;
				sourcesink[i][1] = t;
				i++;
			}
		}
		return sourcesink;
	}
}