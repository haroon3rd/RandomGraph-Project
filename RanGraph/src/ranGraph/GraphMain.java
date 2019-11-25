package ranGraph;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class GraphMain {
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

		//Random random = new Random();


		// randomly generate first graph G
		System.out.println("Generating first random graph, this may take a while...");
		long partStartTime = System.nanoTime();
		RandomGraph firstRanGraph = new RandomGraph(vert);
		
		//Add random edges to first random graph.
		firstRanGraph = firstRanGraph.applyEdge(firstRanGraph, vert);

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
		List<DijkstraAlg> dijks = new LinkedList<DijkstraAlg>();
		for (int i = 0; i<5;i++){
			dijks.add(new DijkstraAlg(vert));
		}

		int[][] dad = new int[5][vert];
		System.out.println("\nGraph #1 Algorithm : Dijkstra");
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


		//Using DijkstraHeap method
		List<DijkstraHeap> dijHeapList = new LinkedList<DijkstraHeap>();
		for (int i = 0; i<5;i++){
			dijHeapList.add(new DijkstraHeap(vert));
		}

		int[][] dadDijHeap = new int[5][vert];
		System.out.println("\nGraph #1 Algorithm : Dijkstra-Heap");
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



		//using Kruskal's method
		List<KruskalAlg> kruskalList = new LinkedList<KruskalAlg>();
		for(int i = 0; i < 5; i++){
			kruskalList.add(new KruskalAlg(rgList.get(i).edgesVector, rgList.get(i).totalEdges, rgList.get(i).weightMatrix, vert));
		}

		int[][] kruskalDad = new int[5][vert];
		System.out.println("\nGraph #1 Algorithm : Kruskal");
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

		System.out.println("\nRouting for firth graph is complete.\n");
		
		System.out.println("Generating second random graph, this may take a while...");		
		partStartTime = System.nanoTime();
		RandomGraph secondGraph = new RandomGraph(vert);
		secondGraph.applyEdgesNew(secondGraph, vert);

		//get the source and sink term here
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
		
		//Using Dijkstra's method for 2nd graph
		List<DijkstraAlg> dij2 = new LinkedList<DijkstraAlg>();
		for (int i = 0; i<5;i++){
			dij2.add(new DijkstraAlg(vert));
		}

		int[][] dad2 = new int[5][vert];
		for(int i = 0; i < 5; i++){ 

			long startTime = System.nanoTime();
			dad2[i] = dij2.get(i).rout(rgList2.get(i), sourcesink2[i][0], sourcesink2[i][1], vert);
			long stopTime = System.nanoTime();

			if(i == 0) System.out.println("\n");
			System.out.println("2nd Graph: Dijkstra Algorithm run "+(i+1)+" path using time  "+(stopTime-startTime)*(1e-9)
					+"  seconds, bandwidth = "+dij2.get(i).bw[sourcesink2[i][1]]);

			int j = sourcesink2[i][1];
			while(j != sourcesink2[i][0]){
				System.out.print("  "+j + " -> " + dad2[i][j]);
				j = dad2[i][j];
			}
			System.out.println();
		}


		//using Kruskal's method
		List<KruskalAlg> kruskalList2 = new LinkedList<KruskalAlg>();
		for(int i = 0; i < 5; i++){
			kruskalList2.add(new KruskalAlg(rgList2.get(i).edgesVector, rgList2.get(i).totalEdges, rgList2.get(i).weightMatrix, vert));
		}

		//Using DijkstraHeap method
		List<DijkstraHeap> dijHeapList2 = new LinkedList<DijkstraHeap>();
		for (int i = 0; i<5;i++){
			dijHeapList2.add(new DijkstraHeap(vert));
		}

		int[][] dadDijHeap2 = new int[5][vert];
		for(int i = 0; i < 5; i++){ 

			long startTime = System.nanoTime();
			dadDijHeap2[i] = dijHeapList2.get(i).dijkstraHeapRout(rgList2.get(i), sourcesink2[i][0], sourcesink2[i][1], vert);
			long stopTime = System.nanoTime();

			if(i == 0)	System.out.println("\n");

			System.out.println("2nd Graph: Dijkstra-Heap Algorithm run "+(i+1)+" path using time "+(stopTime-startTime)*(1e-9)
					+"  seconds, bandwidth = "+dijHeapList2.get(i).heapForFringe.vertValues[sourcesink2[i][1]]);

			int j = sourcesink2[i][1];
			while(j != sourcesink2[i][0]){
				System.out.print("  "+j + " -> " + dadDijHeap2[i][j]);
				j = dadDijHeap2[i][j];
			}
			System.out.println();
		}


		int[][] kruskalDad2 = new int[5][vert];
		for(int i = 0; i < 5; i++){

			long startTime = System.nanoTime();
			kruskalDad2[i] = kruskalList2.get(i).rout(rgList2.get(i).edgeWeightVector, sourcesink2[i][0], sourcesink2[i][1]);;
			long stopTime = System.nanoTime();

			if(i == 0) System.out.println("\n");
			System.out.println("2nd Graph: Kruskal Algorithm run "+(i+1)+" path using time  "+(stopTime - startTime)*(1e-9)+"  seconds");

			int j = sourcesink2[i][1];
			double bandWidth = rgList2.get(i).weightMatrix[j][kruskalDad2[i][j]];
			while(j != sourcesink2[i][0]){
				System.out.print(" "+j+" -> "+kruskalDad2[i][j]);
				if(bandWidth > rgList2.get(i).weightMatrix[j][kruskalDad2[i][j]]){
					bandWidth = rgList2.get(i).weightMatrix[j][kruskalDad2[i][j]];
				}
				j = kruskalDad2[i][j];
			}
			System.out.println();
			System.out.println("BandWidth of this path is "+bandWidth);
		}

		long algorithmEndTime = System.nanoTime();
		
		System.out.println("End of Program in time " + TimeUnit.SECONDS.convert((algorithmEndTime-algorithmStartTime),TimeUnit.NANOSECONDS) +" seconds with 10 successfull runs.");
	}

}
