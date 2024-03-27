package application;

public class SLLNode {

	private Point element;
	private classicSLL edges = new classicSLL();
	private SLLNode next;

	SLLNode(Point element) {
		this.element = element;

	}

	public Point getElement() {
		return element;
	}

	public void setElement(Point element) {
		this.element = element;
	}

	public SLLNode getNext() {
		return next;
	}

	public void setNext(SLLNode next) {
		this.next = next;
	}

	public classicSLL getEdges() {
		return edges;
	}

	public void setEdges(classicSLL edges) {
		this.edges = edges;
	}

	@Override
	public String toString() {
		return "Node [element=" + element;
//		+ "edges: " + edges.toString();
	}

}
