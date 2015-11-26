package ROADWORK;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.*;


/*
 * Solver for the Roadwork problem has given by the
 * Algorithm Design homework. Uses the Dinic's Algorithm
 * (quadratic in the number of vertices, linear in the number of edges)
 * because of the nature of the problem ( edges > vertices ) and mostly
 * because this is a generic case where all capacity are equals to 1. 
 */

public class Roadwork {

	static class Edge {
		int t, rev, cap, f;

		public Edge(int t, int rev, int cap) {
			this.t = t;
			this.rev = rev;
			this.cap = cap;
		}
		
		public String toString() {
			//return "[" + this.t + "," + this.rev + "," + this.cap + "]";
			return "[" + this.t + "]";
		}
	}
	
	static boolean dinicBfs(ArrayList<LinkedList<Edge>> graph, int src, int dest, int[] dist) {
		Arrays.fill(dist, -1);
		dist[src] = 0;
		int[] Q = new int[graph.size()];
		int sizeQ = 0;
		Q[sizeQ++] = src;
		for (int i = 0; i < sizeQ; i++) {
			int u = Q[i];
			for (Edge e : graph.get(u)) {
				if (dist[e.t] < 0 && e.f < e.cap) {
					dist[e.t] = dist[u] + 1;
					Q[sizeQ++] = e.t;
				}
			}
		}
		return dist[dest] >= 0;
	}
	
	static int dinicDfs(ArrayList<LinkedList<Edge>> graph, int[] ptr, int[] dist, int dest, int u, int f) {
		if (u == dest)
			return f;
		for (; ptr[u] < graph.get(u).size(); ++ptr[u]) {
			Edge e = graph.get(u).get(ptr[u]);
			if (dist[e.t] == dist[u] + 1 && e.f < e.cap) {
				int df = dinicDfs(graph, ptr, dist, dest, e.t, Math.min(f, e.cap - e.f));
				if (df > 0) {
					e.f += df;
					graph.get(e.t).get(e.rev).f -= df;
					return df;
				}
			}
		}
		return 0;
	}
	
	public static int maxFlow(ArrayList<LinkedList<Edge>> graph, int src, int dest) {
		int flow = 0;
		int[] dist = new int[graph.size()];
		while (dinicBfs(graph, src, dest, dist)) {
			int[] ptr = new int[graph.size()];
			while (true) {
				int df = dinicDfs(graph, ptr, dist, dest, src, Integer.MAX_VALUE);
				if (df == 0)
					break;
				flow += df;
			}
		}
		return flow;
	}
	
	public static void addCrossroadTwinEdge(ArrayList<LinkedList<Edge>> graph,int i, int j, int numberOfClmns, int numberOfCrossroads) {
		int currentPosition = numberOfClmns * i + j + 2;
		graph.get(currentPosition).add(new Edge(currentPosition + numberOfCrossroads, graph.get(currentPosition+numberOfCrossroads).size(),1));
		graph.get(currentPosition+numberOfCrossroads).add(new Edge(currentPosition, graph.get(currentPosition).size() - 1,0));		
	}
	
	public static void connectNodesToSink(ArrayList<LinkedList<Edge>> graph,int i, int j, int numberOfClmns, int numberOfCrossroads) {
		int currentPosition = numberOfClmns * i + j + 2;
		graph.get(currentPosition + numberOfCrossroads).add(new Edge(1,graph.get(1).size(),1));
		graph.get(1).add(new Edge(currentPosition + numberOfCrossroads,graph.get(currentPosition + numberOfCrossroads).size() - 1,0));
	}
	
	public static void connectSourceToCriticalPoint(ArrayList<LinkedList<Edge>> graph,int position) {
		graph.get(0).add(new Edge(position, graph.get(position).size(), 1));
		graph.get(position).add(new Edge(0, graph.get(0).size() - 1, 0));
	}
	
	public static void connectCriticalPointToCrossroads(ArrayList<LinkedList<Edge>> graph,int i, int j, int numberOfClmns, int position) {
		int normalizedClmn = j/2;
		int normalizedRow = i/2;
		if ( (i % 2) == 0 ) { 
			//Even i indexes indicates that the critical point is
			//replacing a horizontal road
			int leftCrossroadPosition = numberOfClmns * normalizedRow + normalizedClmn + 2;
			graph.get(position).add(new Edge(leftCrossroadPosition,graph.get(leftCrossroadPosition).size(),1));
			graph.get(leftCrossroadPosition).add(new Edge(position,graph.get(position).size() - 1,0));
			int rightCrossroadPosition = numberOfClmns * normalizedRow + normalizedClmn + 1 + 2;
			graph.get(position).add(new Edge(rightCrossroadPosition,graph.get(rightCrossroadPosition).size(),1));
			graph.get(rightCrossroadPosition).add(new Edge(position,graph.get(position).size() - 1,0));
		} else if ((j % 2) == 0 ) {
			//Even j indexes indicates that the critical point is 
			//replacing a vertical road
			int upperCrossroadPosition = numberOfClmns * normalizedRow + normalizedClmn + 2;
			graph.get(position).add(new Edge(upperCrossroadPosition, graph.get(upperCrossroadPosition).size(), 1));
			graph.get(upperCrossroadPosition).add(new Edge(position, graph.get(position).size() - 1, 0));
			int lowerCrossroadPosition = numberOfClmns * (normalizedRow + 1) + normalizedClmn + 2;
			graph.get(position).add(new Edge(lowerCrossroadPosition, graph.get(lowerCrossroadPosition).size(), 1));
			graph.get(lowerCrossroadPosition).add(new Edge(position, graph.get(position).size() - 1, 0));		
		}
	}
	
	public static void connectHorizontalCrossroads(ArrayList<LinkedList<Edge>> graph, int i, int j, int numberOfClmns, int numberOfCrossroads) {
		int leftCrossroadPosition = numberOfClmns * (i/2) + (j/2) + 2;
		int rightCrossroadPosition = numberOfClmns * (i/2) + (j/2) + 3;
		
		int leftCrossroadTwinPosition = numberOfCrossroads + leftCrossroadPosition;
		int rightCrossroadTwinPosition = numberOfCrossroads + rightCrossroadPosition;
		
		graph.get(leftCrossroadTwinPosition).add(new Edge(rightCrossroadPosition,graph.get(rightCrossroadPosition).size(),1));
		graph.get(rightCrossroadPosition).add(new Edge(leftCrossroadTwinPosition,graph.get(leftCrossroadTwinPosition).size()-1,0));
		
		graph.get(rightCrossroadTwinPosition).add(new Edge(leftCrossroadPosition,graph.get(leftCrossroadPosition).size(),1));
		graph.get(leftCrossroadPosition).add(new Edge(rightCrossroadTwinPosition,graph.get(rightCrossroadTwinPosition).size()-1,0));
	}
	
	public static void connectVerticalCrossroads(ArrayList<LinkedList<Edge>> graph, int i, int j, int numberOfClmns, int numberOfCrossroads) {
		int upperCrossroadPosition = numberOfClmns * (i/2) + (j/2) + 2;
		int lowerCrossroadPosition = numberOfClmns * (i/2 + 1) + (j/2) + 2;
		
		int upperCrossroadTwinPosition = numberOfCrossroads + upperCrossroadPosition;
		int lowerCrossroadTwinPosition = numberOfCrossroads + lowerCrossroadPosition;
		
		graph.get(upperCrossroadTwinPosition).add(new Edge(lowerCrossroadPosition,graph.get(lowerCrossroadPosition).size(),1));
		graph.get(lowerCrossroadPosition).add(new Edge(upperCrossroadTwinPosition,graph.get(upperCrossroadTwinPosition).size()-1,0));
		
		graph.get(lowerCrossroadTwinPosition).add(new Edge(upperCrossroadPosition,graph.get(upperCrossroadPosition).size(),1));
		graph.get(upperCrossroadPosition).add(new Edge(lowerCrossroadTwinPosition,graph.get(lowerCrossroadTwinPosition).size()-1,0));
	}

	// Usage example
	// hash array_index = num_of_clms * i + j
	public static void main(String[] args) throws IOException {
		InputStreamReader in = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(in);

		String inLine = br.readLine();
		String[] dimensions = inLine.split(" ");
		
		int height = Integer.parseInt(dimensions[0]);
		int width = Integer.parseInt(dimensions[1]);
		int numberOfCrossroads = height*width;

		ArrayList<LinkedList<Edge>> graph = new ArrayList<LinkedList<Edge>>();
		
		
		//graph[0] is the source
		//graph[1] is the sink
		for ( int i = 0; i < 2*numberOfCrossroads + 2; i++) {
			graph.add(i, new LinkedList<Edge>());
		}
		
		String[] matrix = new String[2*height - 1];
		int numberOfCriticalPoints = 0;
		for (int i = 0; i < ((2*height) - 1); i++) {
			inLine = br.readLine();
			matrix[i] = inLine;
			for (int j = 0; j < ((2*width) - 1); j++) {
				switch(matrix[i].charAt(j)) {
				case '+':
					addCrossroadTwinEdge(graph, i/2, j/2, width, numberOfCrossroads);
					if (i == 0 || i == (2*height - 2) || j == 0 || j == (2*width - 2)) {
						connectNodesToSink(graph, i/2, j/2, width, numberOfCrossroads);
					}
					break;

				case 'c':
					numberOfCriticalPoints++;
					int position = graph.size();
					graph.add(position,new LinkedList<Edge>());
					connectSourceToCriticalPoint(graph, position);
					connectCriticalPointToCrossroads(graph, i, j,width, position);
					break;

				case '-':
					connectHorizontalCrossroads(graph, i, j, width, numberOfCrossroads);
					break;
				
				case '|':
					connectVerticalCrossroads(graph, i, j, width, numberOfCrossroads);
					break;

				default:
					break;
				}
			}
		}
		System.out.println(numberOfCriticalPoints == maxFlow(graph, 0, 1) ? "YES" : "NO" );
	}
}