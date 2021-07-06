package JavaCode.UI;

import JavaCode.resources.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Map;

import static JavaCode.UI.MainWindow.userMeta;

// Панель для отображения графа.
class DrawPanel extends JPanel {

    private void drawNodes(Graphics2D g2d) {
        // Выводим круги.
        g2d.setColor(userMeta.settings.colors.UIStyleColor_Node);
        // Установил сглаживание фигур.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, userMeta.settings.UIStyleSetting_Node_KEY_ANTIALIASING);

        for (Map.Entry<String, Graph.Node> node : Graph.nodes.entrySet()) {
            Ellipse2D ellipse2D = new Ellipse2D.Double(
                    node.getValue().x - userMeta.settings.UIStyleSetting_NodeSize / 2d,
                    node.getValue().y - userMeta.settings.UIStyleSetting_NodeSize / 2d,
                    userMeta.settings.UIStyleSetting_NodeSize,
                    userMeta.settings.UIStyleSetting_NodeSize);
            g2d.fill(ellipse2D);
            g2d.draw(ellipse2D);
        }
    }

    private void drawNames(Graphics2D g2d) {
        // Выводим имена.
        g2d.setColor(userMeta.settings.colors.UIStyleColor_ID);
        // Установил сглаживание текста.
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, userMeta.settings.UIStyleSetting_ID_KEY_TEXT_ANTIALIASING);

        for (Map.Entry<String, Graph.Node> node : Graph.nodes.entrySet()) {
            g2d.drawString(node.getValue().ID, (float) (node.getValue().x - node.getValue().ID.length() * 3.5f), (float) (node.getValue().y + 5f));
        }
    }

    private void drawArrow(Graphics2D g2d, Point2D.Double start, Point2D.Double finish, double radius, boolean isCurved) {

        // Выводим рёбра.
        g2d.setColor(userMeta.settings.colors.UIStyleColor_Edge);
        // Установил сглаживание рёбер.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING , userMeta.settings.UIStyleSetting_Edge_KEY_ANTIALIASING);

        double angle1 = Math.atan2(finish.getY() - start.getY(), finish.getX() - start.getX() + ((isCurved) ? radius * 2 : 0));
        double cosx1 = Math.cos(angle1);
        double siny1 = Math.sin(angle1);
        double cosx2 = Math.cos(angle1 + Math.PI);
        double siny2 = Math.sin(angle1 + Math.PI);

        var fin1x = cosx1 * radius + start.getX() - ((isCurved) ? radius : 0);
        var fin1y = siny1 * radius + start.getY() + ((isCurved) ? radius : 0);
        var fin2x = cosx2 * radius + finish.getX() + ((isCurved) ? radius : 0);
        var fin2y = siny2 * radius + finish.getY() + ((isCurved) ? radius : 0);

        QuadCurve2D QC2D = new QuadCurve2D.Double(fin1x, fin1y,(fin2x+fin1x) / 2, ((isCurved) ? (fin2x+fin1x)/2: (fin2y+fin1y)/2), fin2x, fin2y);
        AffineTransform tx = new AffineTransform();
        AffineTransform oldTx = g2d.getTransform();
        Polygon polygon = new Polygon();
        polygon.addPoint(0, 2);
        polygon.addPoint(-5, -5);
        polygon.addPoint(5, -5);

        double angle = Math.atan2(QC2D.getY2() - ((isCurved) ? QC2D.getCtrlY() : QC2D.getY1()), QC2D.getX2() - ((isCurved) ? QC2D.getCtrlX() : QC2D.getX1()));
        tx.translate(QC2D.getX2(), QC2D.getY2());
        tx.rotate((angle - Math.PI / 2d));
        g2d.setTransform(tx);
        g2d.fill(polygon);
        g2d.setTransform(oldTx);
        g2d.draw(QC2D);
    }

    private void drawEdges(Graphics2D g2d) {
        // Выводим рёбра.
        g2d.setColor(userMeta.settings.colors.UIStyleColor_Edge);
        // Установил сглаживание рёбер.
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING , userMeta.settings.UIStyleSetting_Edge_KEY_ANTIALIASING);

        for (Map.Entry<String, Graph.Node> node : Graph.nodes.entrySet()) {
            for(Graph.Node child : node.getValue().childs) {
                drawArrow(
                        g2d,
                        new Point2D.Double(node.getValue().x, node.getValue().y),
                        new Point2D.Double(child.x, child.y),
                        userMeta.settings.UIStyleSetting_NodeSize / 2d,
                        false);
            }
        }
    }

    private void drawUserMeta(Graphics2D g2d) {
        if (userMeta.startPoint != null && userMeta.finishPoint != null) {
            drawArrow(g2d, new Point2D.Double(
                            userMeta.startPoint.x - 7,
                            userMeta.startPoint.y - 52),
                    new Point2D.Double(userMeta.finishPoint.x - 7, userMeta.finishPoint.y - 52),
                    0d,
                    false);
        }
    }

    private void render(Graphics g) {
        var g2d = (Graphics2D) g;

        drawUserMeta(g2d);
        drawEdges(g2d);
        drawNodes(g2d);
        drawNames(g2d);

        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }
}
