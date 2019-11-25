package ranGraph;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MainFunction {
	public static void configLoggerWithPropertiesFile() {
		// First configure Log4j configuration
		try {
			String log4jConfigFile = System.getProperty("user.dir")
	                + File.separator + "log4j.properties";
	        PropertyConfigurator.configure(log4jConfigFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final Logger logger = Logger.getLogger(MainFunction.class);

	
	//Method for Dijsktra algorithm call
	public static void runDijkstra(List<RandomGraph> rgList, int sourcesink[][], int vert, int gNo) {
		List<DijkstraAlg> dijks = new LinkedList<DijkstraAlg>();
		for (int i = 0; i<5;i++){
			dijks.add(new DijkstraAlg(vert));
		}

		int[][] dad = new int[5][vert];
		System.out.println("\nGraph #" + gNo + " Algorithm : Dijkstra");
		for(int i = 0; i < 5; i++){ 

			long startTime = System.nanoTime();
			dad[i] = dijks.get(i).rout(rgList.get(i), sourcesink[i][0], sourcesink[i][1], vert);
			long stopTime = System.nanoTime();

			//if(i == 0) System.out.println("\n");
			System.out.println("Run #"+(i+1)+", in time "
					+TimeUnit.MILLISECONDS.convert((stopTime-startTime),TimeUnit.NANOSECONDS) +" milliseconds");
			int j = sourcesink[i][1];
			System.out.print("Path = " + j);
			while(j != sourcesink[i][0]){
				System.out.print("-->" + dad[i][j]);
				j = dad[i][j];
			}
			System.out.println("\nPath-Bandwidth = "+dijks.get(i).bw[sourcesink[i][1]]);
		}

	}
	
	public static void runDijkstraHeap(List<RandomGraph> rgList, int sourcesink[][], int vert, int gNo) {
		List<DijkstraHeap> dijHeapList = new LinkedList<DijkstraHeap>();
		for (int i = 0; i<5;i++){
			dijHeapList.add(new DijkstraHeap(vert));
		}

		int[][] dadDijHeap = new int[5][vert];
		System.out.println("\nGraph #" + gNo + " Algorithm : Dijkstra-Heap");
		for(int i = 0; i < 5; i++){ 

			long startTime = System.nanoTime();
			dadDijHeap[i] = dijHeapList.get(i).dijkstraHeapRout(rgList.get(i), sourcesink[i][0], sourcesink[i][1], vert);
			long stopTime = System.nanoTime();

			//if(i == 0)	System.out.println("\n");

			System.out.println("Run #"+(i+1)+ " in time "
					+TimeUnit.MILLISECONDS.convert((stopTime-startTime),TimeUnit.NANOSECONDS) +" milliseconds");

			int j = sourcesink[i][1];
			System.out.print("Path = " + j);
			while(j != sourcesink[i][0]){
				System.out.print("-->" + dadDijHeap[i][j]);
				j = dadDijHeap[i][j];
			}
			System.out.println("\nPath-Bandwidth = "+dijHeapList.get(i).heapForFringe.vertValues[sourcesink[i][1]]);
		}
	}
	
	
	public static void runKruskal(List<RandomGraph> rgList, int sourcesink[][], int vert, int gNo) {
		List<KruskalAlg> kruskalList = new LinkedList<KruskalAlg>();
		for(int i = 0; i < 5; i++){
			kruskalList.add(new KruskalAlg(rgList.get(i).edgesVector, rgList.get(i).totalEdges, rgList.get(i).weightMatrix, vert));
		}

		int[][] kruskalDad = new int[5][vert];
		System.out.println("\nGraph #" + gNo + " Algorithm : Kruskal");
		for(int i = 0; i < 5; i++){

			long startTime = System.nanoTime();
			kruskalDad[i] = kruskalList.get(i).rout(rgList.get(i).edgeWeightVector, sourcesink[i][0], sourcesink[i][1]);;
			long stopTime = System.nanoTime();

			//if(i == 0) System.out.println("\n");
			System.out.println("Run #"+(i+1)+ " , in time "
					+ TimeUnit.MILLISECONDS.convert((stopTime-startTime),TimeUnit.NANOSECONDS) + " milliseconds.");

			int j = sourcesink[i][1];
			double bandWidth = rgList.get(i).weightMatrix[j][kruskalDad[i][j]];
			System.out.print("Path = " + j);
			while(j != sourcesink[i][0]){
				System.out.print("-->" + kruskalDad[i][j]);
				if(bandWidth > rgList.get(i).weightMatrix[j][kruskalDad[i][j]]){
					bandWidth = rgList.get(i).weightMatrix[j][kruskalDad[i][j]];
				}
				j = kruskalDad[i][j];
			}
			System.out.println("\nPath-BandWidth = "+bandWidth);
		}

	}
	
	
	public static void main(String args[]) {
		configLoggerWithPropertiesFile();
		int vert = 0;
		boolean invalid = true;
		Scanner sc = null;
		try {
			while (invalid) {
				System.out.println("Enter total number of vertices (1 to 5000):");
				sc = new Scanner(System.in);
				vert = sc.nextInt();
				if(!(vert<1 || vert>5000))
					invalid = false;
				else
					System.out.println("Input out of suggested range, try again.");
			}
			sc.close();
			System.out.println("You have chosen " + vert + " vertices for random graph generation.");
			logger.info("Random graph has " + vert + " vertices");
		} catch (Exception e) {
			logger.error("Error reading user input", e);
		}
		
		long algorithmStartTime = System.nanoTime();

		// randomly generate first graph G
		System.out.println("Generating FIRST RANDOM GRAPH with vertices of average degree 6.....");
		long partStartTime = System.nanoTime();
		RandomGraph firstRanGraph = new RandomGraph(vert);
		
		//Add random edges to first random graph.
		firstRanGraph = RandomGraph.applyEdge(firstRanGraph, vert);

		//get the source and sink term for five runs.
		int sourcesink[][] = new int[5][2];
		sourcesink = firstRanGraph.pickSourceSink(sourcesink, vert);

		List<RandomGraph> rgList = new LinkedList<RandomGraph>();
		for(int i = 0; i < 5; i++){ 
			rgList.add(firstRanGraph);
			int s = sourcesink[i][0];
			int t = sourcesink[i][1];
			System.out.println("[Src : Dest] for run " + (i+1) + " = " + s +" --> "+t);
			ProjectUtil projectutil = new ProjectUtil();
			RandomGraph rgTemp = projectutil.addPath(rgList.get(i), s, t, vert);
			rgList.set(i, rgTemp);
		}
		
		long partEndTime = System.nanoTime();
		System.out.println("\nFirst graph generated in time " + TimeUnit.MILLISECONDS.convert((partEndTime-partStartTime),TimeUnit.NANOSECONDS) +" milliseconds.");
		
		//Using Dijkstra's method for 1st graph
		runDijkstra(rgList, sourcesink, vert, 1);
		
		//Using DijkstraHeap method
		runDijkstraHeap(rgList,sourcesink, vert, 1);

		//using Kruskal's method
		runDijkstraHeap(rgList,sourcesink, vert, 1);

		System.out.println("\nRouting for firth graph is complete.\n");
		
		System.out.println("Generating SECOND RANDOM GRAPH with 20% adjacent vertices.....");		
		partStartTime = System.nanoTime();
		RandomGraph secondGraph = new RandomGraph(vert);
		secondGraph.applyEdgesNew(secondGraph, vert);

		//get the same source and sink term for five runs.
		int sourcesink2[][] = sourcesink;
		List<RandomGraph> rgList2 = new LinkedList<RandomGraph>();
		for(int i = 0; i < 5; i++){ 
			rgList2.add(secondGraph);
			int s = sourcesink2[i][0];
			int t = sourcesink2[i][1];
			System.out.println("[Src : Dest] for run " + (i+1) + " = " + s +" --> "+t);
			ProjectUtil projectutil2 = new ProjectUtil();
			RandomGraph rgTemp2 = projectutil2.addPath(rgList2.get(i), s, t, vert);
			rgList2.set(i, rgTemp2);
		}

		partEndTime = System.nanoTime();
		System.out.println("\nSecond graph generated in time " + TimeUnit.MILLISECONDS.convert((partEndTime-partStartTime),TimeUnit.NANOSECONDS) +" milliseconds.");
		
		//Using Dijkstra's method for 2ndt graph
		runDijkstra(rgList2, sourcesink2, vert, 2);
		
		//Using DijkstraHeap method for 2nd graph
		runDijkstraHeap(rgList2,sourcesink2, vert, 2);

		//using Kruskal's method for 2nd graph
		runDijkstraHeap(rgList2,sourcesink2, vert, 2);

		long algorithmEndTime = System.nanoTime();
		
		System.out.println("\n\nEnd of Program in time " + TimeUnit.SECONDS.convert((algorithmEndTime-algorithmStartTime),TimeUnit.NANOSECONDS) +" seconds with 10 successfull runs.");
	}

}
