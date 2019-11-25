package ranGraph;

import org.apache.log4j.Logger;

public class HeapStruct {
	public static final Logger logger = Logger.getLogger(HeapStruct.class);

	private int n;
	public int size;
	public int maxHeap[];
	public double D[];
	public int index[];
	
	public HeapStruct(int s) {
		n = s;
		size = 0;
		maxHeap = new int[n];
		D = new double[n];
		index = new int[n];
	}

	public int Max() {
		return maxHeap[0];
	}

	public void Insert(int e) {
		this.maxHeap[++size] = e;
		this.index[e] = size;
		HeapFy(this.maxHeap, size);
	}

	public void Delete(int bugPos) {
		int temp = this.maxHeap[size];

		//before swap, we record the index
		this.index[temp] = bugPos;
		this.index[maxHeap[bugPos]] = size;
		
		this.maxHeap[size] = this.maxHeap[bugPos];
		this.maxHeap[bugPos] = temp;
		size--;
		HeapFy(this.maxHeap, bugPos);
	}

	
	private void HeapFy(int[] maxHeap, int k) {
		if (k + 1 > 1	&& D[maxHeap[k]] > D[maxHeap[(int) Math.floor((k + 1) / 2) - 1]]) {
			int h = k;
			while (h + 1 > 1 && D[maxHeap[h]] > D[maxHeap[(int) Math.floor((h + 1) / 2) - 1]]) {
				int e = maxHeap[h];
				
				//compute index before we swap elements
				this.index[e] = (int) Math.floor((h + 1) / 2) - 1;
				this.index[maxHeap[(int) Math.floor((h + 1) / 2) - 1]] = h;
				
				maxHeap[h] = maxHeap[(int) Math.floor((h + 1) / 2) - 1];
				maxHeap[(int) Math.floor((h + 1) / 2) - 1] = e;
				h = (int) Math.floor((h + 1) / 2) - 1;
			}
		} else if (k <= (int) Math.floor(size / 2)	&& D[maxHeap[k]] < Math.max(D[maxHeap[2 * k + 1]],	D[maxHeap[2 * k + 2]]) && (2*k+1) <=size && (2*k+2) <= size) {
			int h = k;
			while (h <= (int) Math.floor(size / 2)	&& D[maxHeap[h]] < Math.max(D[maxHeap[2 * h + 1]],	D[maxHeap[2 * h + 2]]) && (2*h+1) <=size && (2*h+2) <= size) {
				if (Math.max(D[maxHeap[2 * h + 1]], D[maxHeap[2 * h + 2]]) == D[maxHeap[2 * h + 1]]) {
					int e = maxHeap[h];
					
					//compute index before we swap elements
					this.index[e] = 2*h+1;
					this.index[maxHeap[2*h+1]] = h;
					
					maxHeap[h] = maxHeap[2 * h + 1];
					maxHeap[2 * h + 1] = e;
					h = 2 * h + 1;
				} else {
					int e = maxHeap[h];
					
					//compute index before we swap elements
					this.index[e] = 2*h+2;
					this.index[maxHeap[2*h+2]] = h;
							
					maxHeap[h] = maxHeap[2 * h + 2];
					maxHeap[2 * h + 2] = e;
					h = 2 * h + 2;
				}
			}
		}else if(size == 1){
			int h = k;
			if(h <= (int) Math.floor(size / 2) && D[maxHeap[h]] < D[maxHeap[2*h+1]]){
				int e = maxHeap[h];
				
				//compute index before we swap elements
				this.index[e] = 2*h+1;
				this.index[maxHeap[2*h+1]] = h;
				
				maxHeap[h] = maxHeap[2*h + 1];
				maxHeap[2*h + 1] = e;
				
			}
		}
	}
	

	/*public static void main(String args[]) {
		HeapStruct heap = new HeapStruct(20);
		List<Integer> maxEdgeNumberList = new LinkedList<Integer>();
		double data[] = { 15.5, 3.5, 0.3, 3.7, 6.8, 17.2, 9.3, 11.2, 7.4, 13.5,
				6.5, 6.8, 7.3, 1.0, 19.6, 4.7, 14.4, 20.0, 0.9, 3.8 };
		heap.D = data;
		heap.maxHeap[0] = 0;
		for (int i = 1; i < 20; i++) {
			heap.Insert(i);
		}
		
		System.out.println(Arrays.toString(heap.maxHeap));
		
		for (int i = 0; i < 20; i++){
			int maxEdgenumber = heap.Max();
			maxEdgeNumberList.add(maxEdgenumber);
			System.out.println(maxEdgeNumberList);
			System.out.println("size = "+ heap.size);
			heap.Delete(0);
			System.out.println(Arrays.toString(heap.maxHeap));
			System.out.println("size = "+ heap.size);
		}
		for (int i = 0; i < 20; i++) {
			System.out.println(i + " -> " + maxEdgeNumberList.get(i) + " -> "
					+ heap.D[maxEdgeNumberList.get(i)] + " -> (index)"+heap.index[maxEdgeNumberList.get(i)]);
		}
	}*/
}