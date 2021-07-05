package JavaCode.UI;

import java.awt.*;

public class UIStyleSetting {

    public double UIStyleSetting_NodeSize   = 20d;
    public double UIStyleSetting_EdgeSize   = 1d;
    public double UIStyleSetting_IDSize     = 12d;

    public Object UIStyleSetting_Node_KEY_ANTIALIASING     = RenderingHints.VALUE_ANTIALIAS_ON;
    public Object UIStyleSetting_ID_KEY_TEXT_ANTIALIASING  = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
    public Object UIStyleSetting_Edge_KEY_ANTIALIASING     = RenderingHints.VALUE_ANTIALIAS_ON;

    public UIStyleColor colors = new UIStyleColor();

    public UIStyleSetting() {
    }

    class UIStyleColor {
        Color UIStyleColor_Node = Color.blue;
        Color UIStyleColor_ID = Color.white;
        Color UIStyleColor_Edge = Color.black;

        UIStyleColor() {
        }
    }
}
