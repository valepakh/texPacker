import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

class Bin {
    public Bin(List<Icon> icons, int width, int height) {
        this.icons = icons;
        this.width = width;
        this.height = height;
    }

    List<Icon> icons;
    private final int width;
    private final int height;

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public BufferedImage packIcons() throws IOException {
        if (icons.isEmpty()) {
            throw new IllegalArgumentException("Empty bin");
        }
        BufferedImage ref = ImageIO.read(new File(icons.get(0).getPath()));

        BufferedImage res = new BufferedImage(
                ref.getColorModel(),
                ref.getRaster().createCompatibleWritableRaster(width, height),
                ref.isAlphaPremultiplied(), new Hashtable<>());
        for (Icon i : icons) {
            BufferedImage icon = ImageIO.read(new File(i.getPath()));
            Rectangle r = i.getRect();
            BufferedImage subimage = res.getSubimage(r.x, r.y, r.width, r.height);
            subimage.setData(icon.getData());
        }
        return res;
    }
}
