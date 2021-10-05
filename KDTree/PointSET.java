import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> pointSet = new TreeSet<>();

    public PointSET()                               // construct an empty set of points
    {
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return this.pointSet.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return this.pointSet.size();
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null)
            throw new IllegalArgumentException("null Point2D.");
        if (!this.pointSet.contains(p))
            this.pointSet.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null)
            throw new IllegalArgumentException("null Point2D.");
        return this.pointSet.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        StdDraw.setPenColor(StdDraw.MAGENTA);
        for (Point2D p : this.pointSet) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null)
            throw new IllegalArgumentException("null RectHV.");
        if (isEmpty()) return null;
        LinkedList<Point2D> result = new LinkedList<>();
        for (Point2D p : this.pointSet) {
            if (rect.contains(p))
                result.add(p);
        }
        return result;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null)
            throw new IllegalArgumentException("null Point2D.");
        if (isEmpty()) return null;
        double min = Double.POSITIVE_INFINITY;
        Point2D nearest = p;
        for (Point2D point : pointSet) {
            if (point.distanceSquaredTo(p) < min) {
                min = point.distanceSquaredTo(p);
                nearest = point;
            }
        }
        return nearest;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        PointSET test = new PointSET();
        test.insert(new Point2D(0.5, 0.8));
        test.insert(new Point2D(0.72, 0.15));
        test.nearest(new Point2D(0.3, 0.8));
    }
}
