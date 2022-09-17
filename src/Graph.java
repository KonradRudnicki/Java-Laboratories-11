import org.w3c.dom.Node;

import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("unchecked")
public class Graph {
    int[][] arr;
    //TODO? Collection to map Document to index of vertex
    // You can change it
    HashMap<String, Integer> name2Int = new HashMap<>();
    @SuppressWarnings("unchecked")
    //TODO? Collection to map index of vertex to Document
    // You can change it
    Entry<String, Document>[] arrDoc = (Map.Entry<String, Document>[]) new Map.Entry[0];

    // The argument type depend on a selected collection in the Main class
    public Graph(SortedMap<String, Document> internet) {
        int size = internet.size();
        arr = new int[size][size];

        int index = 0;

        for (String iter : internet.keySet()) {
            name2Int.put(iter, index);
            index++;
        }

        arrDoc = new Entry[size];
        index = 0;

        for (Entry<String, Document> stringDocumentEntry : internet.entrySet()) {
            arrDoc[index] = stringDocumentEntry;
            index++;
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (i == j) {
                    arr[i][j] = 0;
                } else if (arrDoc[i].getValue().link.containsKey(arrDoc[j].getKey())) {
                    arr[i][j] = arrDoc[i].getValue().link.get(arrDoc[j].getKey()).weight;

                } else {
                    arr[i][j] = -1;
                }
            }
        }
    }

    public String bfs(String start) {
        if ((!name2Int.containsKey(start))) {
            return null;
        }

        StringBuilder result = new StringBuilder(start);
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[arr.length];
        visited[name2Int.get(start)] = true;

        for (int i = 0; i < arr.length; i++) {
            if (arr[name2Int.get(start)][i] > 0) {
                queue.add(i);
                visited[i] = true;
            }

        }

        while (!queue.isEmpty()) {
            if (!visited[queue.peek()]) {
                for (int i = 0; i < arr.length; i++) {
                    if (arr[queue.peek()][i] > 0 && !visited[i]) {
                        queue.add(i);
                        visited[i] = true;
                    }
                }
            }

            result.append(", " + arrDoc[queue.peek()].getKey());
            queue.poll();
        }

        return result.toString();
    }

    public String dfs(String start) {
        if ((!name2Int.containsKey(start))) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        int head;
        boolean[] visited = new boolean[arr.length];

        Stack<Integer> stack = new Stack<>();
        stack.push(name2Int.get(start));

        while (!stack.empty()) {
            head = stack.peek();
            stack.pop();

            if (!visited[head]) {
                if (result.toString().equals("")) result.append(start);
                else result.append(", " + arrDoc[head].getKey());

                visited[head] = true;
            }

            for (int i = arr.length - 1; i >= 0; i--) {
                if (arr[head][i] > 0 && !visited[i]) stack.push(i);
            }
        }

        return result.toString();
    }

    public int connectedComponents() {
        DisjointSetForest forest = new DisjointSetForest(arr.length);

        for (int i = 0; i < arr.length; i++) {
            forest.makeSet(i);
        }

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] > 0 && forest.findSet(i) != forest.findSet(j)) {
                    forest.union(i, j);
                }
            }
        }

        return forest.countSets();
    }

    private class Node {
        int distance;
        int predecessor;
        String name;

        public Node(int distance, int predecessor, String name) {
            this.distance = distance;
            this.predecessor = predecessor;
            this.name = name;
        }
    }

    public String DijkstraSSSP(String startVertexStr) {
        if ((!name2Int.containsKey(startVertexStr))) {
            return null;
        }

        int starting_vertex = name2Int.get(startVertexStr);
        Node[] nodes = new Node[arr.length];
        boolean[] visited = new boolean[arr.length];

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(Integer.MAX_VALUE, -1, arrDoc[i].getKey());
        }

        nodes[starting_vertex].distance = 0;

        int min;
        int position = -1;

        for (int i = 0; i < arr.length; i++) {
            min = Integer.MAX_VALUE;

            for (int j = 0; j < nodes.length; j++) {
                if (!visited[j] && nodes[j].distance < min) {
                    min = nodes[j].distance;
                    position = j;
                }
            }

            for (int j = 0; j < arr.length; j++) {
                if (arr[position][j] != -1) {
                    if (nodes[j].distance > (nodes[position].distance + arr[position][j])) {
                        nodes[j].distance = nodes[position].distance + arr[position][j];
                        nodes[j].predecessor = position;
                    }
                }
            }

            visited[position] = true;

        }

        StringBuilder[] lines = new StringBuilder[nodes.length];
        StringBuilder result = new StringBuilder();
        Node temp;

        for (int i = 0; i < nodes.length; i++) {
            lines[i] = new StringBuilder("");

           if (i == starting_vertex){
               lines[starting_vertex] = new StringBuilder("0=" + startVertexStr);
               continue;

           } else if (nodes[i].predecessor == -1) {
                lines[i] = new StringBuilder(arrDoc[i].getKey() +" ot htap on" ); // revers for "no path on"
                continue;

           } else {
                temp = nodes[i];
                lines[i].append(temp.distance + "=");

                while (temp.predecessor != -1) {
                    lines[i].append(temp.name + ">-");
                    temp = nodes[temp.predecessor];
                }
           }

           lines[i].append(startVertexStr);
        }

        for (StringBuilder line : lines) result.append(line.reverse() + "\n");

        return result.toString();
    }
}