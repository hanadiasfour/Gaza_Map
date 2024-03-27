package application;


public class Heap {

	private int maxNumber;
	private Point[] elements;

	Heap(int maxNum) {
		setup(maxNum);

	}

	private void setup(int maxNum) {

		if (maxNum > 0) {
			maxNumber = 0;
			elements = new Point[maxNum + 1];
			Point minimum = new Point(Double.MIN_VALUE);
			elements[0] = minimum; // the ultimate minimal value

		}

	}

	public void insertMinHeap(Point data) {

		maxNumber++;
		elements[maxNumber] = data;
		int i = maxNumber;
		int parent;

		while (i > 1) {
			parent = i / 2;
			if (elements[parent].compareTo(elements[i]) >= 0) {
				swap(parent, i);

			} else
				return;

			i = parent;
		}

	}

	private void minHeapify(int n, int i) {

		int smallest = i;
		int left = 2 * i;
		int right = 2 * i + 1;

		if (left <= n && elements[left].compareTo(elements[smallest]) < 0)
			smallest = left;

		if (right <= n && elements[right].compareTo(elements[smallest]) < 0)
			smallest = right;

		if (smallest != i) {
			swap(smallest, i);
			minHeapify(n, smallest);

		}

	}

	public Point removeMin() {
		Point temp = elements[1];

		swap(1, maxNumber);
		maxNumber--;
		minHeapify(maxNumber, 1);
		return temp;

	}

	public Point getMin() {

		return elements[1];

	}

	public int size() {

		return maxNumber;
	}

	public boolean isEmpty() {
		return maxNumber == 0;

	}

	private void swap(int a, int b) {
		Point temp;
		temp = elements[a];
		elements[a] = elements[b];
		elements[b] = temp;

	}

	public void showStructure() {
		System.out.println("root : " + elements[1].toString());

		for (int i = 1; i <= maxNumber / 2; i++) {
//			System.out.print(elements[i] + " ");
			System.out.print("root : " + elements[i].toString() + " |left : " + elements[2 * i].toString() + " |right :"
					+ (((2 * i + 1) <= maxNumber) ? elements[2 * i + 1].toString() : " empty") + "\n");
		}
	}

}
