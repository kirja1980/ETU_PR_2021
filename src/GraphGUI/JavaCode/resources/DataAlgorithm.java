package JavaCode.resources;

import java.util.ArrayList;

public class DataAlgorithm {
    public Graph graph;
    public String explanation;
    public String names;
    public String[] values;
    private ArrayList<Integer> queue;

    public DataAlgorithm(Graph graph, String explanation, String names, String[] values, ArrayList<Integer> queue) {
        this.graph = graph;
        this.explanation = explanation;
        this.names = names;
        this.values = values;
        setQueue(queue);
    }

    public void setQueue(ArrayList<Integer> queue) {
        if (queue != null) {
            this.queue = new ArrayList<>(queue);
        }
    }

    public ArrayList<Integer> getQueue(){
        if (this.queue != null) {
            System.out.println(this.queue);
            return queue;
        }

        return null;
    }
}
