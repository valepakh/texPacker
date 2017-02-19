import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MaxRects {
    private  int maxWidth = 512;
    private int maxHeight = 512;

    public MaxRects(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public List<Bin> pack(List<Icon> src) {
        List<Bin> bins = new ArrayList<>();
        Bin bestRes = null;
        int w = maxWidth;
        int h = maxHeight;
        while (!src.isEmpty()) {
            List<Icon> srcCopy = new ArrayList<>(src);
            int srcLen = srcCopy.size();
            List<Icon> res = packBin(srcCopy, w, h);
            System.out.println("packed " + res.size() + " from " + srcLen + " into " + w + "x" + h);
            if (srcCopy.isEmpty()) { // packed fully, try to minimize
                System.out.println("packed fully into " + w + "x" + h);
                bestRes = new Bin(res, w, h);
                w >>= 1;
                h >>= 1;
                System.out.println("trying " + w + "x" + h);
            } else if (bestRes != null) { // can't pack fully anymore, get best result
                System.out.println("left " + srcCopy.size());
                System.out.println("selecting best result " + bestRes.getWidth() + "x" + bestRes.getHeight());
                bins.add(bestRes);
                return bins;
            } else {
                bins.add(new Bin(res, w, h));
                System.out.println("left " + srcCopy.size());
                src = srcCopy;
            }
        }
        return bins;
    }

    public List<Icon> packBin(List<Icon> icons, int w, int h) {
        List<Icon> res = new ArrayList<>();
        List<Rectangle> freeRects = new ArrayList<>();
        freeRects.add(new Rectangle(0, 0, w, h));
        int bestFreeRect = 0;
        int bestSrcRect = 0;

        while (!icons.isEmpty()) {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < freeRects.size(); i++) {
                Rectangle f = freeRects.get(i);
                for (int j = 0; j < icons.size(); j++) {
                    Rectangle s = icons.get(j).getRect();
                    if (s.width <= f.width && s.height <= f.height) {
                        int cur = Math.min(f.width - s.width, f.height - s.height);
                        if (cur < min) {
                            min = cur;
                            bestFreeRect = i;
                            bestSrcRect = j;
                        }
                    }
                }
            }
            if (min == Integer.MAX_VALUE) { // can't find any free rects
                return res;
            }
            Icon bestSrc = icons.get(bestSrcRect);
            Rectangle bestFree = freeRects.get(bestFreeRect);
            bestSrc.getRect().setLocation(bestFree.x, bestFree.y);
            res.add(bestSrc);
            splitFreeRect(freeRects, bestFree, bestSrc.getRect());
            freeRects.remove(bestFreeRect);
            icons.remove(bestSrcRect);
        }
        return res;
    }

    private void splitFreeRect(List<Rectangle> freeRects, Rectangle f, Rectangle r) {
        int w = f.width - r.width;
        int h = f.height - r.height;
        if (w < h) { // split horizontal
            freeRects.add(new Rectangle(f.x + r.width, f.y, w, r.height));
            freeRects.add(new Rectangle(f.x, f.y + r.height, f.width, h));
        } else { // vertical
            freeRects.add(new Rectangle(f.x + r.width, f.y, w, f.height));
            freeRects.add(new Rectangle(f.x, f.y + r.height, r.width, h));
        }
    }}
