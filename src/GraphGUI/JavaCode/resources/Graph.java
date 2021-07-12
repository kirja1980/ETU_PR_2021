package JavaCode.resources;

import javax.swing.*;

import static JavaCode.UI.MainWindow.userMeta;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Graph {

    private int nodeIDcounter = 0;
    public HashMap<Integer, Node> nodes = new HashMap<>();

    public Graph() {
        nodeIDcounter = 0;
    }

    // Конструктор копирования.
    public Graph(Graph other) {
        for(Map.Entry<Integer, Node> currentNode : other.nodes.entrySet()) {
            this.addNode(currentNode.getValue().ID, currentNode.getValue().x, currentNode.getValue().y);
            for (Node child : currentNode.getValue().childs) {
                this.nodes.get(currentNode.getKey()).addEdge(this.nodes.get(child.ID));
            }
        }
    }

    public String saveGraph(JFileChooser fileChooser, int result){
        String ret = "";

        StringBuilder output = new StringBuilder("BEGIN " + nodes.size() + "\n");
        for (Graph.Node node : nodes.values()){
            output.append(node.ID).append(" ").append((int)node.x).append(" ").append((int)node.y).append("\n");
        }

        output.append("////////////////\n");
        for(Graph.Node node : nodes.values()){
            output.append(node.ID).append(" : " + node.childs.size() + " -");
            for (Graph.Node child : node.childs){
                output.append(" ").append(child.ID);
            }
            output.append("\n");
        }
        output.append("END");

        System.out.println(output);

        File file1 = fileChooser.getSelectedFile();
        if(result == JFileChooser.APPROVE_OPTION){

            String fileName = fileChooser.getSelectedFile().getName().matches(".*\\.txt$") ?
                    fileChooser.getSelectedFile().getParent() + "\\" + fileChooser.getSelectedFile().getName() :
                    fileChooser.getSelectedFile().getParent() + "\\" + fileChooser.getSelectedFile().getName() + ".txt";
            try (FileWriter writer = new FileWriter(fileName, false)){
                writer.write(output.toString());
                writer.flush();
                ret = "Граф сохранен в файле " + fileName;
            } catch (IOException ioException) {
                ret = "Граф не удалость сохранить!";
                ioException.printStackTrace();
            }
        }
        return ret;
    }

    public String loadGraph(JFileChooser fileChooser, int result){

        boolean isLoad = false;

        fileChooser.setDialogTitle("Загрузка файла");
        // Если директория выбрана, покажем ее в сообщении
        StringBuilder input = new StringBuilder();
        if (result == JFileChooser.APPROVE_OPTION ){
//                    JOptionPane.showMessageDialog(MainWindow.this,
//                            fileChooser.getSelectedFile());
            try {
                FileReader reader = new FileReader(fileChooser.getSelectedFile());
                int c;
                while((c=reader.read())!=-1){
                    input.append((char) c);
                }
                //System.out.println(input);
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }
//                if(input.toString().matches("^BEGIN\\s\\d+[\\n](\\d+\\s\\d+\\s\\d+[\\n])+[/]+[\\n](\\d+\\s:\\s\\d+\\s-\\s\\d?\\n)+[^$]+END$")){
//                    System.out.println("Сохранение некорректно!");
//                    return;
//                }


        if(!input.isEmpty()){
            Scanner scanner = new Scanner( input.toString() );
            scanner.useDelimiter( "\\D+" );

            int N = scanner.nextInt(); // get int
            String regex = "^BEGIN \\d+\n(\\d+ \\d+ \\d+\n){"+N+"}[/]{16}\n(\\d+ : \\d+ -( \\d+)*\n){"+N+"}END$";

            if(!input.toString().matches(regex)){
                System.out.println("Сохранение некорректно!");
                return "Сохранение некорректно!";
            }
            isLoad = true;

            nodes.clear();
            for(int i = 0; i < N; i++){

                String ID = scanner.next();
                System.out.println(ID);
                addNode(Integer.parseInt(ID), scanner.nextInt(),scanner.nextInt());
            }
            System.out.println();
            if(true){
                for(int i = 0; i < N; i++){
                    Integer ID = scanner.nextInt();
                    int K = scanner.nextInt();
                    for(int k = 0; k < K; k++){
                        Integer buff = scanner.nextInt();
                        System.out.println(buff);
                        addEdge(nodes.get(ID), nodes.get(buff));
                    }
                }
            }
            else {
                System.out.println("Некорректный файл.");
            }
        }

        if(!nodes.isEmpty())
            nodeIDcounter = Collections.max(nodes.keySet()) + 1;
        System.out.println(nodeIDcounter);


        if(isLoad)
            return "Граф успешно загружен!";
        return "";
    }

    public void addNode(double x, double y) {
        nodes.put(nodeIDcounter, new Node(nodeIDcounter, x, y));
        nodeIDcounter++;

        System.out.println(nodes);
    }

    public void addNode(int ID, double x, double y) {
        nodes.put(ID, new Node(ID, x, y));
    }

    public void addNode(int ID, int name, double x, double y) {
        nodes.put(ID, new Node(name, x, y));
    }

    public void deleteNode(Node node) {
        if (node != null) {
            for (Map.Entry<Integer, Node> currentNode : nodes.entrySet()) {
                currentNode.getValue().deleteNode(node);
            }
            nodes.remove(node.ID);
        }
    }

    private void _Algorithm(HashMap<Integer, Integer> GraphCycle, ArrayList<DataAlgorithm> dataAlgorithms){
        int breakCounter = 0;
        ArrayList<Integer> deletedNodes = new ArrayList<>();

        while (true) {
            for(Integer str : GraphCycle.keySet()) {
                if(GraphCycle.get(str) == 0){
                    deletedNodes.add(str);
                    Graph buff = new Graph();
                    for(int names : deletedNodes){
                        buff.addNode(names, 0, 0);
                    }
                    for(Integer first : deletedNodes){
                        for(Integer second : deletedNodes){
                            if(this.nodes.get(first).childs.contains(this.nodes.get(second))){
                                buff.addEdge(buff.nodes.get(first),buff.nodes.get(second));
                            }
                        }
                    }

                    dataAlgorithms.add(new DataAlgorithm(buff, "Удаляем вершину ( " + str + " ) т.к. у неё " + GraphCycle.get(str)
                            + " (минимальное) число вхождений. \nУменьшаем значения количества вхождений у вершин ( "
                            , GraphCycle.keySet().toString().replaceAll("[\\]\\[\\,]", "")
                            , GraphCycle.values().toString().replaceAll("[\\]\\[\\,]", "").replaceAll("-1", "-").split(" "), deletedNodes));

                    for(Node node : nodes.get(str).childs){
                        dataAlgorithms.get(dataAlgorithms.size() - 1).explanation += node.ID + " ";
                        GraphCycle.put(node.ID, GraphCycle.get(node.ID) - 1);
                    }
                    dataAlgorithms.get(dataAlgorithms.size() - 1).explanation += ").";

                    GraphCycle.put(str, GraphCycle.get(str) - 1);
                }

            }
            for(Integer integer : GraphCycle.values()){
                if(integer == -1){
                    breakCounter++;
                }
            }

            if(breakCounter == GraphCycle.size()) break;
            breakCounter = 0;
        }
    }

    public Integer getCountParents(Node node) {
        HashMap<Integer,Integer> GraphCycle = new HashMap<>();
        for( Integer name : nodes.keySet()) {
            if(!GraphCycle.containsKey(name)) {
                GraphCycle.put(name, 0);
            }
            if(!nodes.get(name).childs.isEmpty()) {
                for (Node child: nodes.get(name).childs) {
                    if (GraphCycle.containsKey(child.ID)) {
                        GraphCycle.put(child.ID, GraphCycle.get(child.ID) + 1);
                    }
                    else {
                        GraphCycle.put(child.ID, 1);
                    }
                }
            }
        }

        if(GraphCycle.containsKey(node.ID)){
            return GraphCycle.get(node.ID);
        }

        return 0;
    }
    public ArrayList<DataAlgorithm> Algorithm() {
        ArrayList<DataAlgorithm> dataAlgorithms = new ArrayList<>();

        if(this.nodes.isEmpty()){
            System.out.println("Граф не построен. Сортировка невозможна!");
            dataAlgorithms.add(new DataAlgorithm(null,"Граф не построен. Сортировка невозможна!", null,null, null));
            return dataAlgorithms;
        }

        //Храним ключ - название вершины, значение - количество ребер, входящих в эту вершину

        System.out.println("Алгоритм");



        //проверка условия работы алгоритма
        System.out.println("Проверка графа на цикл!");
        for(Node node : nodes.values()){
            ArrayList<Integer> visited = new ArrayList<>();

            if (node.isCycle(visited)) {
                dataAlgorithms.add(new DataAlgorithm(null,"Граф содержит цикл. Сортировка невозможна!", null,null, null));
                System.out.println("Граф содержит цикл. Сортировка невозможна!");
                return dataAlgorithms;
            }

            visited.clear();
        }
        System.out.println("Граф не содержит циклов!");

        HashMap<Integer,Integer> GraphCycle = new HashMap<>();

        for( Node node : nodes.values()){
            GraphCycle.put(node.ID,getCountParents(node));
        }



//        for(Node node : nodes.values()){
//            if(GraphCycle.get(node.ID) == 0 && node.childs.isEmpty()){
//                dataAlgorithms.add(new DataAlgorithm(null,"Вершины не соединены!", null,null));
//                System.out.println("Вершины не соединены");
//                return dataAlgorithms;
//            }
//        }

        _Algorithm(GraphCycle, dataAlgorithms);

        return dataAlgorithms;
    }

    public void addEdge(Node first, Node second) {
        if (first != null && second != null) {
            nodes.get(first.ID).addEdge(second);
        }
    }

    public Node getNodeOnFocus(double x, double y) {
        for (Map.Entry<Integer, Node> node : nodes.entrySet()) {
            double currentX = (x - node.getValue().x);
            double currentY = (y - node.getValue().y);
            double currentR = (userMeta.settings.UIStyleSetting_NodeSize / 2);

            if ((Math.pow(currentX, 2f)) + (Math.pow(currentY, 2f)) <=  Math.pow(currentR, 2f)) {
                return node.getValue();
            }
        }

        return null;
    }

    public static class Node {

        public double x;
        public double y;
        public int ID;

        public LinkedList<Node> childs = new LinkedList<>();

        Node (int ID, double x, double y) {
            this.ID = ID;
            this.x = x;
            this.y = y;
        }

        public boolean isCycle(ArrayList<Integer> visited) {
            if(visited.contains(this.ID)) return true;
            visited.add(this.ID);
            for (Node child : childs){
                if(visited.size() - 1 != 0 && visited.get(0).equals(visited.get(visited.size() - 1)) || child.isCycle(visited)){
                    System.out.println("Cycle: "+visited);
                    return true;
                }
                for(int index = visited.indexOf(child.ID); index < visited.size(); index++){
                    visited.remove(visited.size()-1);
                }
            }
            return false;
        }

        public void addEdge(Node node) {
            if (node != null) {
                if (!childs.contains(node)) {
                    childs.add(node);
                }
            }
        }

        public void move(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public void deleteNode(Node node) {
            if (node != null) {
                childs.remove(node);
            }
        }
    }
}