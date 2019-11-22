package ranGraph;

public class ProjectUtil {

	public RandomGraph addPath(RandomGraph rg, int s, int t, int v){
		int ss = s;
		int tt = t;
		if( ss < tt ){
			
			//s to t-1
			while(ss < tt-1){
				if (!rg.getEdge(ss).contains(ss+1) && !rg.getEdge(ss+1).contains(ss)){
					rg.setEdge(ss, ss+1);
				}
				ss++;
			}
			
			//t-1 to s-1
			if(!rg.getEdge(ss).contains(s-1) && !rg.getEdge(s-1).contains(ss))
				rg.setEdge(ss, s-1);
			ss = s-1;
			
			//s-1 to 1
			while(ss > 1){
				if (!rg.getEdge(ss).contains(ss-1) && !rg.getEdge(ss-1).contains(ss)){
					rg.setEdge(ss, ss-1);
				}
				ss--;
			}
			
			// 1 to v
			if(!rg.getEdge(ss).contains(v) && !rg.getEdge(v).contains(ss))
				rg.setEdge(ss, v);
			ss = v;
			
			//v to t
			while(ss > t){
				if(!rg.getEdge(ss).contains(ss-1) && !rg.getEdge(ss-1).contains(ss)){
					rg.setEdge(ss, ss-1);
				}
				ss--;
			}
		}
		
		if(ss > tt){
			
			//s to t+1
			while(ss > t+1){
				if(!rg.getEdge(ss).contains(ss-1) && !rg.getEdge(ss-1).contains(ss)){
					rg.setEdge(ss, ss-1);
				}
				ss--;
			}
			
			//t+1 to s+1
			if(!rg.getEdge(ss).contains(s+1) && !rg.getEdge(s+1).contains(ss))
				rg.setEdge(ss, s+1);
			ss = s+1;
			
			//s+1 to v
			while(ss < v){
				if(!rg.getEdge(ss).contains(ss+1) && !rg.getEdge(ss+1).contains(ss)){
					rg.setEdge(ss, ss+1);
				}
				ss++;
			}
			
			//v to 1
			if(!rg.getEdge(ss).contains(1) && !rg.getEdge(1).contains(ss))
				rg.setEdge(1, ss);
			ss = 1;
			
			//1 to t
			while(ss < t){
				if(!rg.getEdge(ss).contains(ss+1) && !rg.getEdge(ss+1).contains(ss)){
					rg.setEdge(ss, ss+1);
				}
				ss++;
			}
		}
		return rg;
	}
}
