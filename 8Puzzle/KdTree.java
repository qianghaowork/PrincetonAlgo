import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int count;

    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;

        private Node(Point2D p, double[] coord) {
            this.p = p;
            this.rect = new RectHV(coord[0], coord[1], coord[2], coord[3]);
        }
    }

    public KdTree()                               // construct an empty set of points
    {
        this.root = null;
        this.count = 0;
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return count == 0;
    }

    public int size()                         // number of points in the set
    {
        return count;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null)
            throw new IllegalArgumentException("null Point2D.");
        if (!contains(p))
            root = insert(root, p, true, new double[]{0, 0, 1, 1});
    }

    private double comparePoints(Point2D p, Node n, boolean evenLevel) {
        if (evenLevel)
            return p.x() - n.p.x();
        else
            return p.y() - n.p.y();
    }

    private Node insert(Node n, Point2D p, boolean evenLevel, double[] coord) {
        if (n == null) {
            count++;
            return new Node(p, coord);
        }
        double cmp = comparePoints(p, n, evenLevel);
        if (evenLevel) {
            if (cmp < 0) {
                coord[2] = n.p.x();
                n.lb = insert(n.lb, p, !evenLevel, coord);
            } else {
                coord[0] = n.p.x();
                n.rt = insert(n.rt, p, !evenLevel, coord);
            }
        } else {
            if (cmp < 0) {
                coord[3] = n.p.y();
                n.lb = insert(n.lb, p, !evenLevel, coord);
            } else {
                coord[1] = n.p.y();
                n.rt = insert(n.rt, p, !evenLevel, coord);
            }
        }
        return n;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null)
            throw new IllegalArgumentException("null Point2D.");
        return contains(root, p, true);
    }

    private boolean contains(Node n, Point2D p, boolean evenLevel) {
        if (n == null) return false;
        if (n.p.equals(p)) return true;
        double cmp = comparePoints(p, n, evenLevel);
        if (cmp < 0)
            return contains(n.lb, p, !evenLevel);
        else
            return contains(n.rt, p, !evenLevel);
    }

    public void draw()                         // draw all points to standard draw
    {
        draw(root, true);
    }

    private void draw(Node n, boolean evenLevel) {
        if (n == null) return;
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        n.p.draw();
        StdDraw.setPenRadius();
        if (evenLevel) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }

        draw(n.lb, !evenLevel);
        draw(n.rt, !evenLevel);
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null)
            throw new IllegalArgumentException("null RectHV.");
        Queue<Point2D> result = new Queue<>();
        range(rect, root, result);
        return result;
    }

    private void range(RectHV rect, Node n, Queue<Point2D> result) {
        if (n == null) return;
        if (!rect.intersects(n.rect)) return;
        if (rect.contains(n.p)) result.enqueue(n.p);
        range(rect, n.lb, result);
        range(rect, n.rt, result);
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null)
            throw new IllegalArgumentException("null Point2D.");
        if (isEmpty()) return null;
        Node nearest = root;
        return nearest(root, nearest, p, true).p;
    }

    private Node nearest(Node n, Node nearest, Point2D p, boolean evenLevel) {
        if (n == null) return nearest;
        double min = nearest.p.distanceSquaredTo(p);
        if (min < n.rect.distanceSquaredTo(p)) return nearest;
        double dis = n.p.distanceSquaredTo(p);
        if (dis < min) {
            nearest = n;
        }
        double cmp = comparePoints(p, n, evenLevel);
        if (cmp < 0) {
            nearest = nearest(n.lb, nearest, p, !evenLevel);
            nearest = nearest(n.rt, nearest, p, !evenLevel);
        } else {
            nearest = nearest(n.rt, nearest, p, !evenLevel);
            nearest = nearest(n.lb, nearest, p, !evenLevel);
        }
        return nearest;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        KdTree test = new KdTree();
        test.insert(new Point2D(0.5, 0.8));
        test.insert(new Point2D(0.72, 0.15));
        test.nearest(new Point2D(0.3, 0.8));
    }
}
