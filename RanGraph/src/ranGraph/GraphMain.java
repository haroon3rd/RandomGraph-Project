package ranGraph;


import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class GraphMain {


	public static void main(String args[]) {
		System.out.print("Enter the number of vertices: ");
		Scanner sc = new Scanner(System.in);
		int vertices = sc.nextInt();
		try {
			System.out.println("Random graph has " + vertices + " vertices");

//			Random random = new Random();

			// randomly generate G
			RandomGraph ranGraph1 = new RandomGraph(vertices);
			System.out.println("We're generating 1st graph, this may take some time, please wait...");
			RandomGraph ranGraph0 = ranGraph1.applyEdge(ranGraph1, vertices);
			
			//rg0.presentGraph(rg0, v);
			//get the source and sink term here
			int sourcesink[][] = new int[5][2];
			//int st[][] = ranGraph0.pickSourceSink(sourcesink, vertices);
			ranGraph0.pickSourceSink(sourcesink, vertices);
			
			List<RandomGraph> rgList = new LinkedList<RandomGraph>();
			for(int i = 0; i < 5; i++){ 
				rgList.add(ranGraph0);
				int s = sourcesink[i][0];
				int t = sourcesink[i][1];
				System.out.println("Source:Destination  " +s+" --> "+t);
				ProjectUtil projectutil = new ProjectUtil();
				RandomGraph rgTemp = projectutil.addPath(rgList.get(i), s, t, vertices);
				rgList.set(i, rgTemp);
			}
			
			//Using Dijkstra's method for 1st graph
			List<DijkstraAlg> dij = new LinkedList<DijkstraAlg>();
			for (int i = 0; i<5;i++){
				dij.add(new DijkstraAlg(vertices));
			}
			
			int[][] dad = new int[5][vertices];
			for(int i = 0; i < 5; i++){ 
				
				long startTime = System.nanoTime();
				dad[i] = dij.get(i).rout(rgList.get(i), sourcesink[i][0], sourcesink[i][1], vertices);
				long stopTime = System.nanoTime();
				
				if(i == 0) System.out.println("\n");
				System.out.println("1st Graph: Dijkstra Algorithm run "+(i+1)+" path using time  "+(stopTime-startTime)*(1e-9)
						+"  seconds, bandwidth = "+dij.get(i).bw[sourcesink[i][1]]);
				
				int j = sourcesink[i][1];
				while(j != sourcesink[i][0]){
					System.out.print("  "+j + " -> " + dad[i][j]);
					j = dad[i][j];
				}
				System.out.println();
			}
			
			//using Kruskal's method
			List<KruskalAlg> kruskalList = new LinkedList<KruskalAlg>();
			for(int i = 0; i < 5; i++){
				kruskalList.add(new KruskalAlg(rgList.get(i).edgesVector, rgList.get(i).totalEdges, rgList.get(i).weightMatrix, vertices));
			}
			
			int[][] kruskalDad = new int[5][vertices];
			for(int i = 0; i < 5; i++){
				
				long startTime = System.nanoTime();
				kruskalDad[i] = kruskalList.get(i).rout(rgList.get(i).edgeWeightVector, sourcesink[i][0], sourcesink[i][1]);;
				long stopTime = System.nanoTime();
				
				if(i == 0) System.out.println("\n");
				System.out.println("1st Graph: Kruskal Algorithm run "+(i+1)+" path using time "+(stopTime - startTime)*(1e-9));
				
				int j = sourcesink[i][1];
				double bandWidth = rgList.get(i).weightMatrix[j][kruskalDad[i][j]];
				while(j != sourcesink[i][0]){
					System.out.print(" "+j+" -> "+kruskalDad[i][j]);
					if(bandWidth > rgList.get(i).weightMatrix[j][kruskalDad[i][j]]){
						bandWidth = rgList.get(i).weightMatrix[j][kruskalDad[i][j]];
					}
					j = kruskalDad[i][j];
				}
				System.out.println();
				System.out.println("BandWidth of this path is "+bandWidth);
			}

			//Using DijkstraHeap method
			List<DijkstraHeap> dijHeapList = new LinkedList<DijkstraHeap>();
			for (int i = 0; i<5;i++){
				dijHeapList.add(new DijkstraHeap(vertices));
			}
			
			int[][] dadDijHeap = new int[5][vertices];
			for(int i = 0; i < 5; i++){ 
				
				long startTime = System.nanoTime();
				dadDijHeap[i] = dijHeapList.get(i).dijkstraHeapRout(rgList.get(i), sourcesink[i][0], sourcesink[i][1], vertices);
				long stopTime = System.nanoTime();
				
				if(i == 0)	System.out.println("\n");
				
				System.out.println("1st Graph: Dijkstra-Heap Algorithm run "+(i+1)+" path using time "+(stopTime-startTime)*(1e-9)
						+", bandwidth = "+dijHeapList.get(i).heapForFringe.D[sourcesink[i][1]]);
				
				int j = sourcesink[i][1];
				while(j != sourcesink[i][0]){
					System.out.print("  "+j + " -> " + dadDijHeap[i][j]);
					j = dadDijHeap[i][j];
				}
				System.out.println();
			}
			
			
			RandomGraph rg2 = new RandomGraph(vertices);
			System.out.println("\n\n");
			System.out.println("1st Graph routing is over, we are creating 2nd graph, this may also take some time, please wait...");
			rg2.applyEdgesNew(rg2, vertices);
			
			//get the source and sink term here
			int sourcesink2[][] = new int[5][2];
			//int st2[][] = rg2.pickSourceSink(sourcesink2, vertices);
			rg2.pickSourceSink(sourcesink2, vertices);
			
			List<RandomGraph> rgList2 = new LinkedList<RandomGraph>();
			for(int i = 0; i < 5; i++){ 
				rgList2.add(rg2);
				int s = sourcesink2[i][0];
				int t = sourcesink2[i][1];
				System.out.println(s+" --> "+t);
				ProjectUtil projectutil2 = new ProjectUtil();
				RandomGraph rgTemp2 = projectutil2.addPath(rgList2.get(i), s, t, vertices);
				rgList2.set(i, rgTemp2);
			}
			
			//Using Dijkstra's method for 2nd graph
			List<DijkstraAlg> dij2 = new LinkedList<DijkstraAlg>();
			for (int i = 0; i<5;i++){
				dij2.add(new DijkstraAlg(vertices));
			}
			
			int[][] dad2 = new int[5][vertices];
			for(int i = 0; i < 5; i++){ 
				
				long startTime = System.nanoTime();
				dad2[i] = dij2.get(i).rout(rgList2.get(i), sourcesink2[i][0], sourcesink2[i][1], vertices);
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
				kruskalList2.add(new KruskalAlg(rgList2.get(i).edgesVector, rgList2.get(i).totalEdges, rgList2.get(i).weightMatrix, vertices));
			}
			
			int[][] kruskalDad2 = new int[5][vertices];
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
			
			
			//Using DijkstraHeap method
			List<DijkstraHeap> dijHeapList2 = new LinkedList<DijkstraHeap>();
			for (int i = 0; i<5;i++){
				dijHeapList2.add(new DijkstraHeap(vertices));
			}
			
			int[][] dadDijHeap2 = new int[5][vertices];
			for(int i = 0; i < 5; i++){ 
				
				long startTime = System.nanoTime();
				dadDijHeap2[i] = dijHeapList2.get(i).dijkstraHeapRout(rgList2.get(i), sourcesink2[i][0], sourcesink2[i][1], vertices);
				long stopTime = System.nanoTime();
				
				if(i == 0)	System.out.println("\n");
				
				System.out.println("2nd Graph: Dijkstra-Heap Algorithm run "+(i+1)+" path using time "+(stopTime-startTime)*(1e-9)
						+"  seconds, bandwidth = "+dijHeapList2.get(i).heapForFringe.D[sourcesink2[i][1]]);
				
				int j = sourcesink2[i][1];
				while(j != sourcesink2[i][0]){
					System.out.print("  "+j + " -> " + dadDijHeap2[i][j]);
					j = dadDijHeap2[i][j];
				}
				System.out.println();
			}
			
			
			
			
		} catch (Exception E) {
			//System.out.println("Something went wrong");
			System.out.println(E);
		}
		sc.close();
	}

}
