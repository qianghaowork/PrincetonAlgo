import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
   private boolean[][] peropen;
   private int top;
   private int bottom;
   private int size;
   private WeightedQuickUnionUF wqu;
   private WeightedQuickUnionUF fqu;
 
   // create n-by-n grid, with all sites blocked
   public Percolation(int n) {
       if (n <= 0) 
          throw new IllegalArgumentException("n<=0");
               
       size = n;
       top = 0;
       bottom = n*n + 1;
       peropen = new boolean[size][size];
       wqu = new WeightedQuickUnionUF(n*n+2);
       fqu = new WeightedQuickUnionUF(n*n+1);
   }
   
   private int get1Dpos(int i, int j) {
       return (i-1)*size + j;
   }
   
   // open site (row i, column j) if it is not open already
   public void open(int i, int j) {
       if (i > 0 && i <= size && j > 0 && j <= size) {
          if (isOpen(i, j)) {
             return;
          }
          peropen[i-1][j-1] = true;
          int npos = get1Dpos(i, j);
          if (i == 1) {
             wqu.union(top, npos);
             fqu.union(top, npos);
          }

          if (i == size)
             wqu.union(bottom, npos);
          
          if (i > 1 && isOpen(i-1, j)) {
             wqu.union(get1Dpos(i-1, j), npos);
             fqu.union(get1Dpos(i-1, j), npos);
          }

          if (i < size && isOpen(i+1, j)) {
             wqu.union(get1Dpos(i+1, j), npos);
             fqu.union(get1Dpos(i+1, j), npos);
          }

          if (j > 1 && isOpen(i, j-1)) {
             wqu.union(get1Dpos(i, j-1), npos);
             fqu.union(get1Dpos(i, j-1), npos);
          }

          if (j < size && isOpen(i, j+1)) {
             wqu.union(get1Dpos(i, j+1), npos);
             fqu.union(get1Dpos(i, j+1), npos);
          }
       }
       else
          throw new IndexOutOfBoundsException();
   }
   
   // is site (row i, column j) open?
   public boolean isOpen(int i, int j) {
       if (i > 0 && i <= size && j > 0 && j <= size) { 
         return peropen[i-1][j-1];
       }
       else
         throw new IndexOutOfBoundsException();
   }

   // is site (row i, column j) full?
   public boolean isFull(int i, int j) {
       if (i > 0 && i <= size && j > 0 && j <= size) {
           return fqu.connected(top, get1Dpos(i, j));
       }
       else
           throw new IndexOutOfBoundsException();
   }
   
   // does the system percolate?
   public boolean percolates() {
       return wqu.connected(top, bottom);
   }
   
   public static void main(String[] args) {
       Percolation pc = new Percolation(5);
       pc.open(1, 3);
       pc.open(2, 2);
       pc.open(2, 3);
       System.out.println(pc.percolates());
       pc.open(5, 4);
       pc.open(4, 4);
       pc.open(3, 4);
       System.out.println(pc.percolates());
       pc.open(3, 3);
       System.out.println(pc.percolates());
   }
}
