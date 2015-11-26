package BALSUBTR;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Node {
  private int value;
  private List<Node> children;
  
  public Node(int value) {
    this.value = value;
    this.children = new ArrayList<Node>();
  }
  
  public void setValue(int value) {
	  this.value = value;
  }
  
  public void setChildren(List<Node> children) {
	  this.children = children;
  }
  
  public int getValue() {
	  return this.value;
  }
  
  public List<Node> getChildren() {
	  return this.children;
  }
  
  public Node addChild(Node node) {
	  this.children.add(node);
	  return node;
  }
  
  public String toString() {
	  return ":" + this.value + ":";
  }
}

public class Balsubtr {
	
	private Node root;
	private Node[] nodes;
	private int currentValue = Integer.MAX_VALUE;
	private int numberOfNodes;
	private Node node1,node2;
	
	
	public Balsubtr() {}
	
	public void addChild(Node node, Node childValue) {
		node.addChild(childValue);
	}
	
	public int DFS(Node node) {
		int ret = 0;
		int tmp = 0;
		if (node.getChildren().isEmpty()) {
			return 1;
		}
		for (Node child : node.getChildren()) {
			tmp = DFS(child);
			childWeight(node, child, tmp);
			ret += tmp;
		
		}
		
		ret++;
		return ret;
	}
	
	private void childWeight(Node parent,Node child, int size) {
		int tempValue = Math.abs(2*size - numberOfNodes);
		if (tempValue < currentValue) {
			currentValue = tempValue;
			node1 = parent;
			node2 = child;
		} else if (tempValue == currentValue){
			if ( parent.getValue() < node1.getValue() ) {
				node1 = parent;
				node2 = child;
			} else if (parent.getValue() == node1.getValue() ) {
				if ( child.getValue() < node2.getValue() ) {
					node1 = parent;
					node2 = child;
				}
			}
		}
	}
	
	private void printSolution() {
		System.out.println(node1.getValue() + " " + node2.getValue());
	}
	
	
	public void addRoot(Node node) {
		this.root = node;
	}
	
	public void setTotalNumberOfNodes(int totalNodes) {
		this.numberOfNodes = totalNodes;
	}
	
	public static void main(String[] args) throws IOException {
		Balsubtr tree = new Balsubtr();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String inLine = br.readLine();
		int edgesNumber = Integer.parseInt(inLine) - 1;
		tree.setTotalNumberOfNodes(edgesNumber+1);
		tree.nodes = new Node[tree.numberOfNodes+1];
		
		for ( ; edgesNumber > 0; edgesNumber-- ) {
			inLine = br.readLine();
			String[] relatives = inLine.split(" ");
			
			int parentValue = Integer.parseInt(relatives[0]);
			int childValue = Integer.parseInt(relatives[1]);
			if ( parentValue > childValue) {
				int tmp = childValue;
				childValue = parentValue;
				parentValue = tmp;
			}
			
			if ( tree.nodes[parentValue] == null ) {
				tree.nodes[parentValue] = new Node(parentValue);
			}
			if ( tree.nodes[childValue] == null ) {
				tree.nodes[childValue] = new Node(childValue);
			}
			
			tree.nodes[parentValue].addChild(tree.nodes[childValue]);
		}

		tree.root = tree.nodes[1];
		tree.DFS(tree.root);
		tree.printSolution();
	}
}

