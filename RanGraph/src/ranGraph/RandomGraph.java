package ranGraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class RandomGraph {
	private int vertices;
	
	//TestKruskal
	public int totalEdges = 0;
	public int doneVertex = 0;
	
	private Map<Integer, List<Integer>> adjacencyList;
	private Map<Integer, List<Map<Integer, Double>>> edgeWeight;
	public List<EdgeObject> edges;
	
	public double[] edgeWeightVector;
	public EdgeObject[] edgesVector;
	public double[][] weightMatrix;
	
	public RandomGraph(int verts) {
		vertices = verts;
		adjacencyList = new HashMap<Integer, List<Integer>>();
		edgeWeight = new HashMap<Integer, List<Map<Integer, Double>>>();
		
		edges = new LinkedList<EdgeObject>();
		
		edgeWeightVector = new double[3000000];
		edgesVector = new EdgeObject[3000000];
		weightMatrix = new double[5001][5001];
		
		for (int i = 1; i <= verts; i++) {
			adjacencyList.put(i, new LinkedList<Integer>());
			edgeWeight.put(i, new LinkedList<Map<Integer, Double>>());
		}
	}

	public void setEdge(int to, int from) {
		if (to > adjacencyList.size() || from > adjacencyList.size())
			System.out.println("The vertices does not exist");

		List<Integer> srcList = adjacencyList.get(to);
		srcList.add(from);

		List<Integer> destList = adjacencyList.get(from);
		destList.add(to);

		double weight = Math.random() * vertices + 1;

		Map<Integer, Double> srcListweight = new HashMap<Integer, Double>();
		srcListweight.put(from, weight);
		List<Map<Integer, Double>> weightlist1 = edgeWeight.get(to);
		weightlist1.add(srcListweight);

		Map<Integer, Double> destListweight = new HashMap<Integer, Double>();
		destListweight.put(to, weight);
		List<Map<Integer, Double>> weightlist2 = edgeWeight.get(from);
		weightlist2.add(destListweight);
		
		if (this.getEdge(to).size() == 6)
			this.doneVertex++;
		
		if(this.getEdge(from).size() == 6)
			this.doneVertex++;
		
		EdgeObject edgeobject = new EdgeObject();
		edgeobject.edge1 = to;
		edgeobject.edge2 = from;
		edgeobject.edgeweight = weight;
		
		edgesVector[totalEdges] = edgeobject;
		edgeWeightVector[totalEdges] = weight;
		weightMatrix[to][from] = weight;
		weightMatrix[from][to] = weight;
		
		totalEdges++;
	}

	public List<Integer> getEdge(int to) {
		if (to > adjacencyList.size()) {
			System.out.println("The vertices does not exit");
			return null;
		}
		return adjacencyList.get(to);
	}

	public List<Map<Integer, Double>> getEdgeWeight(int to) {
		if (to > adjacencyList.size()) {
			System.out.println("The vertices does not exit");
			return null;
		}
		return edgeWeight.get(to);
	}

	public RandomGraph applyEdge(RandomGraph rg, int v) {

		RandomGraph rgReturn = new RandomGraph(v);

		while(true){

			RandomGraph rgTest = new RandomGraph(v);

			int count = 0, to, from;
			Random random = new Random();
			while (rgTest.doneVertex < v) {
				count++;
				from = Math.abs(random.nextInt(v)+1);
				to = Math.abs(random.nextInt(v)+1);
				if (!rgTest.getEdge(from).contains(to) && !rgTest.getEdge(to).contains(from) && to != from 
						&& rgTest.getEdge(to).size() < 6 && rgTest.getEdge(from).size() < 6) {
					rgTest.setEdge(to, from);
				}	
				if(count > 1800*rgTest.vertices)
					break;
			}
			if(rgTest.doneVertex == v){

				rgReturn = rgTest;
				//System.out.println("count = "+ count); 
				break;
			}
		}

		return rgReturn;
	}

	public void applyEdgesNew(RandomGraph rg, int v) {
		int count = 1, to, from;
		int k;
		while (count <= v) {
			from = count;
			k = count;
			while (k <= v) {
				to = k++;
				double random2 = Math.random();
				if (random2 <= 0.2 && to != from && !rg.getEdge(from).contains(to) &&!rg.getEdge(to).contains(from))
					rg.setEdge(to, from);
			}
			count++;
		}
	}

	/*public static void presentGraph(RandomGraph rg, int v) {
		System.out.println("The Adjacency List Representation is:");
		for (int i = 1; i <= v; i++) {
			List<Integer> edgeList1 = rg.getEdge(i);
			System.out.print(i + " -> ");
			List<Map<Integer, Double>> edgeList2 = rg.getEdgeWeight(i);
			if (edgeList2.size() == 0)
				System.out.print("Null");
			else {
				for (int j = 1;; j++) {
					if (j != edgeList2.size())
						System.out.print("( "
								+ edgeList1.get(j - 1)
								+ " , "
								+ edgeList2.get(j - 1).get(
										edgeList1.get(j - 1)) + " ) -> ");
					else {
						System.out.print("( "
								+ edgeList1.get(j - 1)
								+ " , "
								+ edgeList2.get(j - 1).get(
										edgeList1.get(j - 1)) + " )");
						break;
					}
				}
			}
			System.out.println();
		}
	}*/

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