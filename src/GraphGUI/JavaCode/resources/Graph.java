package JavaCode.resources;

import kotlin.Pair;

import static JavaCode.UI.MainWindow.userMeta;

import java.util.*;

public class Graph {

    static int nodeIDcounter = 0;
    public static HashMap<String, Node> nodes = new HashMap<>();

    public void addNode(double x, double y) {
        nodes.put(Integer.toString(nodeIDcounter), new Node(Integer.toString(nodeIDcounter), x, y));
        nodeIDcounter++;
    }

    public void deleteNode(Node node) {
        if (node != null) {
            for (Map.Entry<String, Node> currentNode : nodes.entrySet()) {
                currentNode.getValue().deleteNode(node);
            }
            nodes.remove(node.ID);
        }
    }

    Stack<Node> visited = new Stack<>();
    Stack<Node> queue = new Stack<>();

    void _Algorithm(){
        Node buff;
        for( Node node : nodes.values() ){

        }
    }

    public void Alghorinthm() {
        HashMap<String,Integer> GraphCicle = new HashMap<>();
        int counter = 1;
        System.out.println("Алгоритм");

        //Проверка возможности работы алгоритма
        for( String name : nodes.keySet()){
            if(!GraphCicle.containsKey(name))
                GraphCicle.put(name, 0);
            if(!nodes.get(name).childs.isEmpty()){
                for( Node child: nodes.get(name).childs ){
                    if(GraphCicle.containsKey(child.ID)){
                        GraphCicle.put(child.ID, GraphCicle.get(child.ID) + 1);
                    }
                    else{
                        GraphCicle.put(child.ID, 1);
                    }
                }
            }
        }

        for(Node node : nodes.values()){
            if(GraphCicle.get(node.ID) == 0 && node.childs.isEmpty()){
                System.out.println("Вершины не соединены");
                return;
            }
        }

        int breakCounter = 0;

        while (true){
            for(String str : GraphCicle.keySet()){
                if(GraphCicle.get(str) == 0){
                    System.out.print("Удаляем \""+str+"\" "); System.out.println(GraphCicle);
                    for(Node node : nodes.get(str).childs){
                        GraphCicle.put(node.ID, GraphCicle.get(node.ID) - 1);
                    }
                    GraphCicle.put(str, GraphCicle.get(str) - 1);
                    deleteNode(nodes.get(str));
                }
            }
            for(Integer integer : GraphCicle.values()){
                if(integer == -1){
                    breakCounter++;
                }
            }
            if(breakCounter == GraphCicle.size()) break;
            breakCounter = 0;
        }

        System.out.println();
        System.out.println(GraphCicle);
    }

    public void addEdge(Node first, Node second) {
        if (first != null && second != null) {
            nodes.get(first.ID).addEdge(second);
        }
    }

    public Node getNodeOnClicked(double x, double y) {
        for (Map.Entry<String, Node> node : nodes.entrySet()) {
            double currentX = (x - node.getValue().x - 7);
            double currentY = (y - node.getValue().y - 52);
            double currentR = (userMeta.settings.UIStyleSetting_NodeSize / 2);

            if ((Math.pow(currentX, 2f)) + (Math.pow(currentY, 2f)) <=  Math.pow(currentR, 2f)) {
                return node.getValue();
            }
        }

        return null;
    }

    public class Node {

        public double x;
        public double y;
        public String ID;

        public LinkedList<Node> childs = new LinkedList<>();

        Node (String ID, double x, double y) {
            this.ID = ID;
            this.x = x;
            this.y = y;
        }

        public void addEdge(Node node) {
            if (node != null) {
                if (!childs.contains(node)) {
                    childs.add(node);
                }
            }
        }

        public void move(double x, double y) {
            this.x = x - 7;
            this.y = y - 52;
        }

        public void deleteNode(Node node) {
            if (node != null) {
                childs.remove(node);
            }
        }
    }
}