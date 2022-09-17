public class DisjointSetForest implements DisjointSetDataStructure {

	private int size;

	private class Element {
		int rank;
		int parent;

		public Element(int rank, int parent) {
			this.rank = rank;
			this.parent = parent;
		}
	}

	Element[] arr;

	public DisjointSetForest(int size) {
		arr = new DisjointSetForest.Element[size];
		this.size = size;
	}

	@Override
	public void makeSet(int item) {
		arr[item] = new DisjointSetForest.Element(0, item);
	}

	@Override
	public int findSet(int item) {
		if (item >= size || item < 0) return -1;

		if (arr[item].parent != item) {
			arr[item].parent = findSet(arr[item].parent);
		}

		return arr[item].parent;
	}

	@Override
	public boolean union(int itemA, int itemB) {
		if (itemA >= size || itemB >= size || findSet(arr[itemA].parent) == findSet(arr[itemB].parent)) return false;

		return link(findSet(itemA), findSet(itemB));
	}

	private boolean link(int itemA, int itemB) {
		if (arr[itemA].rank > arr[itemB].rank) {
			arr[itemB].parent = itemA;
			arr[itemA].rank += arr[itemB].rank;

		} else {
			arr[itemA].parent = itemB;
			arr[itemB].rank += arr[itemA].rank;

			if (arr[itemA].rank == arr[itemB].rank) {
				arr[itemB].rank++;
			}
		}

		return true;
	}

	@Override
	public String toString() {
		if (size == 0) return "Disjoint sets as forest:";

		StringBuilder result = new StringBuilder("Disjoint sets as forest:" + "\n");

		for (int i = 0; i < arr.length; i++) {
			result.append(i + " -> " + arr[i].parent);
			if (i != arr.length - 1) {
				result.append("\n");
			}
		}

		return result.toString();
	}

	@Override
	public int countSets() {
		int counter = 0;
		for (int i = 0; i < size; i++) {
			if (i == arr[i].parent) {
				counter++;
			}
		}

		return counter;
	}
}