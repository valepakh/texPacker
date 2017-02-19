import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Packer {
    private static List<Icon> scanPath(Path dir) {
        URI baseUri = new File(".").toURI();
        List<Icon> res = new ArrayList<>();
        try (DirectoryStream<Path> list = Files.newDirectoryStream(dir, "*.png")) {
            for (Path p : list) {
                ImageInputStream iis = ImageIO.createImageInputStream(p.toFile());
                Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
                if (readers.hasNext()) {
                    ImageReader reader = readers.next();
                    reader.setInput(iis);
                    // TODO image format
                    Icon icon = new Icon(baseUri.relativize(p.toUri()).getPath(), reader.getWidth(0), reader.getHeight(0));
                    res.add(icon);
                    System.out.println(icon);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        File dir = new File(args[0]);
        List<Icon> icons = scanPath(dir.toPath());

        int maxWidth = 512;
        int maxHeight = 512;
        MaxRects packer = new MaxRects(maxWidth, maxHeight);

        List<Bin> bins = packer.pack(icons);
        for (int i = 0; i < bins.size(); i++) {
            Bin b = bins.get(i);
            System.out.println(b.icons);
            try {
                BufferedImage packedBin = b.packIcons();
                ImageIO.write(packedBin, "png", new File("packed" + i + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson g = new GsonBuilder().setPrettyPrinting().create();
            String j = g.toJson(b);
            File jsonFile = new File("packed" + i + ".json");
            try {
                Files.write(jsonFile.toPath(), j.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
