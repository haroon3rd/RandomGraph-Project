package ranGraph;

import org.apache.log4j.Logger;

public class ProjectUtil {
	public static final Logger logger = Logger.getLogger(ProjectUtil.class);

	public RandomGraph addPath(RandomGraph ranGraph, int s, int t, int v){
		int src = s;
		int dest = t;
		try {
			if( src < dest ){

				//s to t-1
				while(src < dest-1){
					if (!ranGraph.getEdge(src).contains(src+1) && !ranGraph.getEdge(src+1).contains(src)){
						ranGraph.setEdge(src, src+1);
					}
					src++;
				}

				//t-1 to s-1
				if(!ranGraph.getEdge(src).contains(s-1) && !ranGraph.getEdge(s-1).contains(src))
					ranGraph.setEdge(src, s-1);
				src = s-1;

				//s-1 to 1
				while(src > 1){
					if (!ranGraph.getEdge(src).contains(src-1) && !ranGraph.getEdge(src-1).contains(src)){
						ranGraph.setEdge(src, src-1);
					}
					src--;
				}

				// 1 to v
				if(!ranGraph.getEdge(src).contains(v) && !ranGraph.getEdge(v).contains(src))
					ranGraph.setEdge(src, v);
				src = v;

				//v to t
				while(src > t){
					if(!ranGraph.getEdge(src).contains(src-1) && !ranGraph.getEdge(src-1).contains(src)){
						ranGraph.setEdge(src, src-1);
					}
					src--;
				}
			}

			if(src > dest){

				//source to destination+1
				while(src > t+1){
					if(!ranGraph.getEdge(src).contains(src-1) && !ranGraph.getEdge(src-1).contains(src)){
						ranGraph.setEdge(src, src-1);
					}
					src--;
				}

				//destination+1 to source+1
				if(!ranGraph.getEdge(src).contains(s+1) && !ranGraph.getEdge(s+1).contains(src))
					ranGraph.setEdge(src, s+1);
				src = s+1;

				//source+1 to v

				while(src < v){
					if(!ranGraph.getEdge(src).contains(src+1) && !ranGraph.getEdge(src+1).contains(src)){
						ranGraph.setEdge(src, src+1);
					}
					src++;
				}

				//v to 1
				if(!ranGraph.getEdge(src).contains(1) && !ranGraph.getEdge(1).contains(src))
					ranGraph.setEdge(1, src);
				src = 1;

				//1 to destination
				while(src < t){
					if(!ranGraph.getEdge(src).contains(src+1) && !ranGraph.getEdge(src+1).contains(src)){
						ranGraph.setEdge(src, src+1);
					}
					src++;
				}
			}
		} catch (Exception e) {
			logger.error("Error encountered" + e);
		}
		return ranGraph;
	}
}
