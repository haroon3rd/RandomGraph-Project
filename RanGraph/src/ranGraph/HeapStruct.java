package ranGraph;

import org.apache.log4j.Logger;

public class HeapStruct {
	public static final Logger logger = Logger.getLogger(HeapStruct.class);

	private int n;
	public int size;
	public int maxHeap[];
	public double vertValues[];
	public int index[];
	
	public HeapStruct(int s) {
		n = s;
		size = 0;
		maxHeap = new int[n];
		vertValues = new double[n];
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
		if (k + 1 > 1	&& vertValues[maxHeap[k]] > vertValues[maxHeap[(int) Math.floor((k + 1) / 2) - 1]]) {
			int h = k;
			while (h + 1 > 1 && vertValues[maxHeap[h]] > vertValues[maxHeap[(int) Math.floor((h + 1) / 2) - 1]]) {
				int e = maxHeap[h];
				
				//compute index before we swap elements
				this.index[e] = (int) Math.floor((h + 1) / 2) - 1;
				this.index[maxHeap[(int) Math.floor((h + 1) / 2) - 1]] = h;
				
				maxHeap[h] = maxHeap[(int) Math.floor((h + 1) / 2) - 1];
				maxHeap[(int) Math.floor((h + 1) / 2) - 1] = e;
				h = (int) Math.floor((h + 1) / 2) - 1;
			}
		} else if (k <= (int) Math.floor(size / 2)	&& vertValues[maxHeap[k]] < Math.max(vertValues[maxHeap[2 * k + 1]],	vertValues[maxHeap[2 * k + 2]]) && (2*k+1) <=size && (2*k+2) <= size) {
			int h = k;
			while (h <= (int) Math.floor(size / 2)	&& vertValues[maxHeap[h]] < Math.max(vertValues[maxHeap[2 * h + 1]],	vertValues[maxHeap[2 * h + 2]]) && (2*h+1) <=size && (2*h+2) <= size) {
				if (Math.max(vertValues[maxHeap[2 * h + 1]], vertValues[maxHeap[2 * h + 2]]) == vertValues[maxHeap[2 * h + 1]]) {
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
			if(h <= (int) Math.floor(size / 2) && vertValues[maxHeap[h]] < vertValues[maxHeap[2*h+1]]){
				int e = maxHeap[h];
				
				//compute index before we swap elements
				this.index[e] = 2*h+1;
				this.index[maxHeap[2*h+1]] = h;
				
				maxHeap[h] = maxHeap[2*h + 1];
				maxHeap[2*h + 1] = e;
				
			}
		}
	}
}