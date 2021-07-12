package JavaCode.UI;

import JavaCode.resources.DataAlgorithm;
import JavaCode.resources.Graph;
import JavaCode.resources.UserMeta;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import static java.awt.Cursor.getPredefinedCursor;

public class AlgorithmPanel extends JPanel {

    private final JWindow toolTipWindow = new JWindow();
    private final JLabel toolTipLabel = new JLabel("", SwingConstants.CENTER);

    private final JTextArea jTextArea = new JTextArea();
    private final DrawPanel drawPanel = new DrawPanel();
    private ArrayList<DataAlgorithm> dataAlgorithms = new ArrayList<>();

    private final JButton stepBack = new JButton("Назад");
    private final JButton stepForward = new JButton("Вперёд");

    private final DefaultTableModel model = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable jTable = new JTable(model);

    private int stepIndex = 0;

    public AlgorithmPanel() {
        initUI();
    }

    private void initUI() {
        toolTipLabel.setOpaque(false);
        toolTipLabel.setBackground(UIManager.getColor("ToolTip.background"));
        toolTipLabel.setBorder(UIManager.getBorder("ToolTip.border"));
        toolTipWindow.add(toolTipLabel);
        toolTipWindow.setSize(30, 20);
        toolTipWindow.setVisible(false);

        drawPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (drawPanel.getGraph() != null) {
                    Graph.Node node = drawPanel.getGraph().getNodeOnFocus(e.getX(), e.getY());
                    if (node != null) {
                        toolTipLabel.setText(Integer.toString(drawPanel.getGraph().getCountParents(node)));
                        Point point = e.getPoint();
                        SwingUtilities.convertPointToScreen(point, e.getComponent());
                        point.translate(-toolTipWindow.getWidth() / 2, (int) (-(toolTipWindow.getHeight() + MainWindow.userMeta.settings.UIStyleSetting_NodeSize)));
                        toolTipWindow.setLocation(point);
                        toolTipWindow.setVisible(true);
                    }
                    else {
                        toolTipWindow.setVisible(false);
                    }
                }
            }
        });


        jTextArea.setBackground(new Color(239, 239, 239));
        jTextArea.setEditable(false);

        JScrollPane jTableScrollPane = new JScrollPane(jTable);
        jTable.setBackground(new Color(239, 239, 239));

        drawPanel.setCurved(true);
        drawPanel.setManaged(false);

        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        JPanel midPanel = new JPanel();
        JPanel downPanel = new JPanel();

        northPanel.setLayout(new BorderLayout());
        midPanel.setLayout(new BorderLayout());
        downPanel.setLayout(new BorderLayout());

        northPanel.setBorder(new TitledBorder(null, "Пояснения", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        northPanel.add(jTextArea, BorderLayout.CENTER);

        JPanel centralPanel = new JPanel();

        centralPanel.setLayout(new GridLayout(2, 1));

        midPanel.setBorder(new TitledBorder(null, "Таблица", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        midPanel.add(jTableScrollPane, BorderLayout.CENTER);

        downPanel.setBorder(new TitledBorder(null, "Результат", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        downPanel.add(drawPanel, BorderLayout.CENTER);

        centralPanel.add(midPanel);
        centralPanel.add(downPanel);

        JPanel southPanel = new JPanel();

        stepBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stepIndex != 0) {
                    stepIndex -= stepIndex == 0 ? 0 : 1;
                    jTextArea.setText(dataAlgorithms.get(stepIndex).explanation);
                    drawPanel.setGraph(dataAlgorithms.get(stepIndex).graph);
                    model.removeRow(stepIndex + 1);

                    int counter = 0;
                    int dx = drawPanel.getWidth() / dataAlgorithms.get(stepIndex).graph.nodes.size();
                    for (int nodeID : dataAlgorithms.get(stepIndex).getQueue()) {
                        dataAlgorithms.get(stepIndex).graph.nodes.get(nodeID).move(MainWindow.userMeta.settings.UIStyleSetting_NodeSize / 2 + counter++ * dx, drawPanel.getHeight() / 2);
                    }
                    drawPanel.setGraph(dataAlgorithms.get(stepIndex).graph);

                    setEnabled(stepIndex != 0);
                }

                stepBack.setEnabled(stepIndex != 0);
                stepForward.setEnabled(stepIndex + 1 < dataAlgorithms.size());
            }
        });

        stepForward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stepIndex < dataAlgorithms.size() - 1) {
                    stepIndex += stepIndex == dataAlgorithms.size() - 1 ? 0 : 1;
                    jTextArea.setText(dataAlgorithms.get(stepIndex).explanation);
                    drawPanel.setGraph(dataAlgorithms.get(stepIndex).graph);
                    model.addRow(dataAlgorithms.get(stepIndex).values);

                    int counter = 0;
                    int dx = drawPanel.getWidth() / dataAlgorithms.get(stepIndex).graph.nodes.size();
                    for (int nodeID : dataAlgorithms.get(stepIndex).getQueue()) {
                        dataAlgorithms.get(stepIndex).graph.nodes.get(nodeID).move(MainWindow.userMeta.settings.UIStyleSetting_NodeSize / 2 + counter++ * dx, drawPanel.getHeight() / 2);
                    }
                    drawPanel.setGraph(dataAlgorithms.get(stepIndex).graph);


                }

                stepBack.setEnabled(stepIndex != 0);
                stepForward.setEnabled(stepIndex + 1 < dataAlgorithms.size());
            }
        });

        stepBack.setEnabled(stepIndex != 0);
        stepForward.setEnabled(stepIndex + 1 < dataAlgorithms.size());

        southPanel.add(stepBack);
        southPanel.add(stepForward);

        add(northPanel, BorderLayout.NORTH);
        add(centralPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        revalidate();
    }

    public void setTextjLabel(String string){
        jTextArea.setText(string);
    }

    public void setDataAlgorithms(ArrayList<DataAlgorithm> dataAlgorithms) {
        clear();
        if (this.dataAlgorithms != null) {
            this.dataAlgorithms = dataAlgorithms;

            this.jTextArea.setText(this.dataAlgorithms.get(stepIndex).explanation);

            if (this.dataAlgorithms.get(stepIndex).names != null) {
                for (String name : this.dataAlgorithms.get(stepIndex).names.split(" ")) {
                    this.model.addColumn(name);
                }
            }

            if (this.dataAlgorithms.get(stepIndex).values != null) {
                for (int index = 0; index < stepIndex + 1; ++index) {
                    this.model.addRow(this.dataAlgorithms.get(index).values);
                }
            }

                for (DataAlgorithm date : this.dataAlgorithms) {

                    if (date.graph != null) {
                        int counter = 0;
                        int dx = drawPanel.getWidth() / date.graph.nodes.size();

                        for (int nodeID : date.getQueue()) {
                            date.graph.nodes.get(nodeID).move(MainWindow.userMeta.settings.UIStyleSetting_NodeSize / 2 + counter++ * dx, drawPanel.getHeight() / 2);
                        }
                    }
                }

            this.drawPanel.setGraph(dataAlgorithms.get(stepIndex).graph);

            stepBack.setEnabled(stepIndex != 0);
            stepForward.setEnabled(stepIndex + 1 < dataAlgorithms.size());
        }
    }

    public void clear() {
        jTextArea.setText(null);
        this.drawPanel.setGraph(null);

        model.setColumnCount(0);
        model.getDataVector().removeAllElements();

        stepBack.setEnabled(stepIndex != 0);
        stepForward.setEnabled(stepIndex + 1 < dataAlgorithms.size());
        dataAlgorithms = new ArrayList<>();
    }

    public DrawPanel getDrawPanel() {
        return drawPanel;
    }

    public ArrayList<DataAlgorithm> getDataAlgorithms() {
        return dataAlgorithms;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }
}
