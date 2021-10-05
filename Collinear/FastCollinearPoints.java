import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> seg = new ArrayList<>();

    public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
    {
        // 1. null check
        if (points == null)
            throw new IllegalArgumentException("null argument.");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("null entries in given points.");
        }

        // copy input parameter to avoid direct modify
        Point[] localPoints = points.clone();

        // sort local points to avoid mutate input
        Arrays.sort(localPoints);

        // 2. duplicate check
        if (localPoints.length > 1) {
            for (int m = 1; m < localPoints.length; m++) {
                if (localPoints[m].compareTo(localPoints[m - 1]) == 0)
                    throw new IllegalArgumentException("Input contains duplicate.");
            }
        }

        if (localPoints.length > 3) {
            // loop through points in backup array, and sort the temp points array
            for (Point p : localPoints) {
                findSegments(localPoints, p);
            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return seg.size();
    }

    public LineSegment[] segments()                // the line segments
    {
        return seg.toArray(new LineSegment[seg.size()]);
    }

    private void addLine(Point[] points, Point orig, int start, int end) {
        ArrayList<Point> foundPoints = new ArrayList<>();
        foundPoints.add(orig);
        for (int i = start; i <= end; i++) {
            foundPoints.add(points[i]);
        }
        foundPoints.sort(null);
        if (foundPoints.get(0).compareTo(orig) == 0) {
            seg.add(new LineSegment(foundPoints.get(0), foundPoints.get(foundPoints.size() - 1)));
        }
    }

    private void findSegments(Point[] points, Point orig) {
        Point[] pointsCopy = points.clone();
        Arrays.sort(pointsCopy, orig.slopeOrder());
        double[] slopes = new double[points.length];
        for (int i = 0; i < pointsCopy.length; i++)
            slopes[i] = orig.slopeTo(pointsCopy[i]);
        int start = 1;
        for (int i = 2; i < slopes.length; i++) {
            if (Double.compare(slopes[i - 1], slopes[i]) != 0) {
                if (i - start >= 3) {
                    addLine(pointsCopy, orig, start, i - 1);
                }
                start = i;
            }
        }
        if (slopes.length - start >= 3) {
            addLine(pointsCopy, orig, start, slopes.length - 1);
        }
    }

    public static void main(String[] args) {
        // read the N points from a file
        In in = new In(args[0]);
        int N = in.readInt();
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
}
