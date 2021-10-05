import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null)
            throw new IllegalArgumentException("null argument.");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("null entries in given points.");
        }

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

        Point orig = new Point(0, 0);
        Arrays.sort(localPoints, orig.slopeOrder());
        ArrayList<LineSegment> seg = new ArrayList<LineSegment>();
        Point[] temp = new Point[4];
        for (int i = 0; i < points.length - 3; i++)
            for (int j = i + 1; j < points.length - 2; j++)
                for (int p = j + 1; p < points.length - 1; p++)
                    for (int q = p + 1; q < points.length; q++) {
                        temp[0] = localPoints[i];
                        temp[1] = localPoints[j];
                        temp[2] = localPoints[p];
                        temp[3] = localPoints[q];
                        double slope1 = localPoints[i].slopeTo(localPoints[j]);
                        double slope2 = localPoints[i].slopeTo(localPoints[p]);
                        double slope3 = localPoints[i].slopeTo(localPoints[q]);
                        if (Double.compare(slope1, slope2) == 0 && Double.compare(slope1, slope3) == 0) {
                            Arrays.sort(temp);
                            seg.add(new LineSegment(temp[0], temp[3]));
                        }
                    }
        segments = seg.toArray(new LineSegment[0]);
    }

    public int numberOfSegments()        // the number of line segments
    {
        return segments.length;
    }

    public LineSegment[] segments()                // the line segments
    {
        return segments;
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
