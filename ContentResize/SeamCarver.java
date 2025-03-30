import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class SeamCarver {
    private int width;
    private int height;
    private double[][] energy;
    private int[][] color;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new IllegalArgumentException("Picture is null");
        this.width = picture.width();
        this.height = picture.height();
        this.color = new int[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                color[i][j] = picture.getARGB(i, j);
        initializeEnergy();
    }

    private int getRed(int val) {
        return (val >> 0) & (0xFF);
    }

    private int getGreen(int val) {
        return (val >> 8) & (0xFF);
    }

    private int getBlue(int val) {
        return (val >> 16) & (0xFF);
    }

    private double calcEnergy(int x, int y) {
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            return 1000;
        double rx = getRed(this.color[x + 1][y]) - getRed(this.color[x - 1][y]);
        double gx = getGreen(this.color[x + 1][y]) - getGreen(this.color[x - 1][y]);
        double bx = getBlue(this.color[x + 1][y]) - getBlue(this.color[x - 1][y]);
        double ry = getRed(this.color[x][y + 1]) - getRed(this.color[x][y - 1]);
        double gy = getGreen(this.color[x][y + 1]) - getGreen(this.color[x][y - 1]);
        double by = getBlue(this.color[x][y + 1]) - getBlue(this.color[x][y - 1]);
        return Math.sqrt(rx * rx + gx * gx + bx * bx + ry * ry + gy * gy + by * by);
    }

    private void initializeEnergy() {
        this.energy = new double[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                this.energy[i][j] = calcEnergy(i, j);
            }
    }

    // current picture
    public Picture picture() {
        Picture pic = new Picture(this.width, this.height);
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                pic.setARGB(i, j, this.color[i][j]);
        initializeEnergy();
        return pic;
    }

    // width of current picture
    public int width() {
        return this.width;
    }

    // height of current picture
    public int height() {
        return this.height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > this.width - 1)
            throw new IllegalArgumentException("x should be 0 and " + this.width);
        if (y < 0 || y > this.height - 1)
            throw new IllegalArgumentException("y should be 0 and " + this.height);
        return energy[x][y];
    }

    private void relaxHorizon(int x, int y, int y1, double[][] distTo, int[][] edgeFrom) {
        if (x >= 0 && x < this.width - 1 && y1 >= 0 && y1 < this.height) {
            if (distTo[x + 1][y1] > distTo[x][y] + this.energy[x + 1][y1]) {
                distTo[x + 1][y1] = distTo[x][y] + this.energy[x + 1][y1];
                edgeFrom[x + 1][y1] = y;
            }
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] distTo = new double[this.width][this.height];
        int[][] edgeFrom = new int[this.width][this.height];
        for (double[] r : distTo) Arrays.fill(r, Double.POSITIVE_INFINITY);
        for (int[] r : edgeFrom) Arrays.fill(r, Integer.MAX_VALUE);

        for (int i = 0; i < height; i++) {
            distTo[0][i] = 1000;
            edgeFrom[0][i] = i;
        }
        for (int i = 0; i < width - 1; i++)
            for (int j = 0; j < height; j++) {
                relaxHorizon(i, j, j, distTo, edgeFrom);
                if (j == 0) {
                    relaxHorizon(i, j, j + 1, distTo, edgeFrom);
                } else if (j == this.height - 1) {
                    relaxHorizon(i, j, j - 1, distTo, edgeFrom);
                } else {
                    relaxHorizon(i, j, j - 1, distTo, edgeFrom);
                    relaxHorizon(i, j, j + 1, distTo, edgeFrom);
                }
            }
        double minDist = Double.POSITIVE_INFINITY;
        int minPos = 0;
        int[] seam = new int[this.width];
        for (int j = 0; j < height; j++) {
            if (minDist > distTo[width - 1][j]) {
                minDist = distTo[width - 1][j];
                minPos = j;
            }
        }
        for (int i = width - 1; i >= 0; i--) {
            seam[i] = minPos;
            minPos = edgeFrom[i][minPos];
        }
        return seam;
    }

    private void relaxVerical(int y, int x, int x1, double[][] distTo, int[][] edgeFrom) {
        if (x1 >= 0 && x1 < this.width && y >= 0 && y < this.height - 1) {
            if (distTo[x1][y + 1] > distTo[x][y] + this.energy[x1][y + 1]) {
                distTo[x1][y + 1] = distTo[x][y] + this.energy[x1][y + 1];
                edgeFrom[x1][y + 1] = x;
            }
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[this.width][this.height];
        int[][] edgeFrom = new int[this.width][this.height];
        for (double[] r : distTo) Arrays.fill(r, Double.POSITIVE_INFINITY);
        for (int[] r : edgeFrom) Arrays.fill(r, Integer.MAX_VALUE);

        for (int i = 0; i < width; i++) {
            distTo[i][0] = 1000;
            edgeFrom[i][0] = i;
        }
        for (int j = 0; j < height - 1; j++)
            for (int i = 0; i < width; i++) {
                relaxVerical(j, i, i, distTo, edgeFrom);
                if (i == 0) {
                    relaxVerical(j, i, i + 1, distTo, edgeFrom);
                } else if (i == this.width - 1) {
                    relaxVerical(j, i, i - 1, distTo, edgeFrom);
                } else {
                    relaxVerical(j, i, i - 1, distTo, edgeFrom);
                    relaxVerical(j, i, i + 1, distTo, edgeFrom);
                }
            }
        double minDist = Double.POSITIVE_INFINITY;
        int minPos = 0;
        int[] seam = new int[this.height];
        for (int i = 0; i < width; i++) {
            if (minDist > distTo[i][height - 1]) {
                minDist = distTo[i][height - 1];
                minPos = i;
            }
        }
        for (int j = height - 1; j >= 0; j--) {
            seam[j] = minPos;
            minPos = edgeFrom[minPos][j];
        }
        return seam;
    }

    private void validSeam(boolean isHorizontal, int[] seam) {
        if (seam == null)
            throw new IllegalArgumentException("seam[] is null");
        if (isHorizontal) {
            if (this.height <= 1)
                throw new IllegalArgumentException("height <= 1");
            if (seam.length != this.width)
                throw new IllegalArgumentException("seam[] is not width");
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] < 0 || seam[i] > this.height - 1)
                    throw new IllegalArgumentException("seam is out of boundary for " + seam[i]);
                if (i > 0 && Math.abs(seam[i - 1] - seam[i]) > 1)
                    throw new IllegalArgumentException("i " + seam[i] + " i-1 " + seam[i - 1] + " differs more than 1");
            }
        } else {
            if (this.width <= 1)
                throw new IllegalArgumentException("width <= 1");
            if (seam.length != this.height)
                throw new IllegalArgumentException("seam[] is not height");
            for (int i = 0; i < seam.length; i++) {
                if (seam[i] < 0 || seam[i] > this.width - 1)
                    throw new IllegalArgumentException("seam is out of boundary for " + seam[i]);
                if (i > 0 && Math.abs(seam[i - 1] - seam[i]) > 1)
                    throw new IllegalArgumentException("i " + seam[i] + " i-1 " + seam[i - 1] + " differs more than 1");
            }
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validSeam(true, seam);
        this.height--;
        for (int i = 0; i < this.width; i++) {
            int rm = seam[i];
            int j = rm;
            while (j < this.height) {
                this.color[i][j] = this.color[i][j + 1];
                j++;
            }
        }
        for (int i = 0; i < this.width; i++) {
            int rm = seam[i];
            if (rm == this.height) {
                this.energy[i][rm - 1] = calcEnergy(i, rm - 1);
            } else {
                this.energy[i][rm] = calcEnergy(i, rm);
                if (i > 0) this.energy[i - 1][rm] = calcEnergy(i - 1, rm);
                if (i < this.width - 1) this.energy[i + 1][rm] = calcEnergy(i + 1, rm);
                if (rm > 0) this.energy[i][rm - 1] = calcEnergy(i, rm - 1);
                int j = rm + 1;
                while (j < this.height) {
                    this.energy[i][j] = calcEnergy(i, j);
                    j++;
                }
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validSeam(false, seam);
        this.width--;
        for (int j = 0; j < this.height; j++) {
            int rm = seam[j];
            int i = rm;
            while (i < this.width) {
                this.color[i][j] = this.color[i + 1][j];
                i++;
            }
        }
        for (int j = 0; j < this.height; j++) {
            int rm = seam[j];
            if (rm == width) {
                this.energy[rm - 1][j] = calcEnergy(rm - 1, j);
            } else {
                this.energy[rm][j] = calcEnergy(rm, j);
                if (j > 0) this.energy[rm][j - 1] = calcEnergy(rm, j - 1);
                if (j < this.height - 1) this.energy[rm][j + 1] = calcEnergy(rm, j + 1);
                if (rm > 0) this.energy[rm - 1][j] = calcEnergy(rm - 1, j);
                int i = rm + 1;
                while (i < this.width) {
                    this.energy[i][j] = calcEnergy(i, j);
                    i++;
                }
            }
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture pic = new Picture("tower.jpg");
        SeamCarver sc = new SeamCarver(pic);
        // sc.picture().show();
        StdOut.printf("width = %d, height = %d\n", sc.width(), sc.height());
        StdOut.printf("(1000, 500) = (%d, %d, %d)\n", sc.getRed(pic.getARGB(1000, 500)), sc.getGreen(pic.getARGB(1000, 500)), sc.getBlue(pic.getARGB(1000, 500)));
        int count = 500;
        while (count > 0) {
            int[] vertical = sc.findVerticalSeam();
            //   StdOut.printf("vertical: " + vertical + "\n");
            sc.removeVerticalSeam(vertical);
            count--;
        }
        sc.picture().show();
        StdOut.printf("width = %d, height = %d\n", sc.width(), sc.height());
    }

}
