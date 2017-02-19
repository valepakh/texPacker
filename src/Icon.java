import java.awt.*;

class Icon {
    public Icon(String path, int width, int height) {
        this.path = path;
        this.rect = new Rectangle(width, height);
    }

    @Override
    public String toString() {
        return "Icon{path=" + path + ", " + rect + '}';
    }

    public Rectangle getRect() {
        return rect;
    }

    public String getPath() {
        return path;
    }

    private final String path;
    private Rectangle rect;
}
