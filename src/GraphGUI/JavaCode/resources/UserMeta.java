package JavaCode.resources;

import JavaCode.UI.UIStyleSetting;

import java.awt.geom.Point2D;

public class UserMeta {
    public UIStyleSetting settings = new UIStyleSetting();

    public EditMode editMode = EditMode.None;

    public Graph.Node buffer = null;

    public enum EditMode {
        Creating,
        Deleting,
        Moving,
        Linking,
        None
    }

    public Point2D.Double startPoint = null;
    public Point2D.Double finishPoint = null;

    public UserMeta () {
    }
}
