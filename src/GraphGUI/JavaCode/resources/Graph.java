package JavaCode.resources;

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
