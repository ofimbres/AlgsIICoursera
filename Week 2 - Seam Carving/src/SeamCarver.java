import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    
    private static final double MAX_ENERGY = 1000;
    
    private int width;
    private int height;
    private int[][] pixels;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture p) {
        if (p == null) {
            throw new IllegalArgumentException("p cannot be null.");
        }
        
        width = p.width();
        height = p.height();
        pixels = new int[width][height];
        
        storePictureData(p);
    }
    
    // current picture
    public Picture picture() {
        Picture p = new Picture(width, height);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                p.setRGB(x, y, pixels[x][y]);
            }
        }
        
        return p;
    }
    
    // width of current picture
    public int width() {
        return width;
    }
    
    // height of current picture
    public int height() {
        return height;
    }
    
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width - 1) {
            throw new IllegalArgumentException("x is outside its prescribed range.");
        }
        
        if (y < 0 || y > height - 1) {
            throw new IllegalArgumentException("y is outside its prescribed range.");
        }
        
        // define the energy of a pixel at the border of the image to be 1000
        if (x == 0 || x == width - 1 ||
            y == 0 || y == height - 1) {
            return MAX_ENERGY;
        } else {
            // get the energy of the interior pixel.        
            int deltaX = getSquaredGradient(pixels[x - 1][y], pixels[x + 1][y]);
            int deltaY = getSquaredGradient(pixels[x][y - 1], pixels[x][y + 1]);
            return Math.sqrt(deltaX + deltaY);
        }
    }
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        
        return seam;
    }
    
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = new int[height];
        
        // if length is 1, return all indices as zero
        if (width == 1) { 
            return seam;
        }
        
        double[] energyTo = new double[width];
        double[] oldEnergyTo = new double[width];
        int[][] edgeTo = new int[width][height];
        
        // * what is the total energy of the smallest path that got to this pixel?
        // * what is the X value of the pixel in the row above that's part of that smallest path?
        //   Or at least, which pixel above does it want, since there are only three possibilities
        //   and you could store this fact in something smaller than an int.
        // 
        // At the bottom, look around for the bottom pixel with the lowest total energy.
        // This is the end of the smallest seam. This pixel knows which pixel above it (up left, up, or up right)
        // is part of the seam so you can work back to the top and you have the seam.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                relaxVertically(x, y, energyTo, oldEnergyTo, edgeTo);
            }
            System.arraycopy(energyTo, 0, oldEnergyTo, 0, width);
        }
        
        // find minimum total energy
        double minEnergy = oldEnergyTo[0];
        int best = 0;
        for (int x = 1; x < width; x++) {
            if (oldEnergyTo[x] < minEnergy) {
                minEnergy = oldEnergyTo[x];
                best = x;
            }
        }
        
        // determine seam
        seam[height - 1] = best;

        for (int y = height - 2; y >= 0; y--) {
            seam[y] = edgeTo[best][y + 1];
            best = edgeTo[best][y + 1];
        }

        return seam;
    }
    
    // remove horizontal seam from curent picture
    public void removeHorizontalSeam(int[] seam) {
        checkSeamValidity(seam, false);
        
        transpose();
        removeVerticalSeamInternal(seam);
        transpose();
    }
    
    // remove vertical seam from curent picture
    public void removeVerticalSeam(int[] seam) {
        checkSeamValidity(seam, true);
        
        removeVerticalSeamInternal(seam);
    }
    
    // unit testing
    public static void main(String[] args) {
        // run PrintEnergy ../test-input/6x5.png
        // run ShowEnergy ../test-input/HJocean.png
        // run ShowSeams ../test-input/HJocean.png
        // run PrintSeams ../test-input/6x5.png
        // run ResizeDemo ../test-input/HJocean.png 300 0
    }
    
    // removeVerticalSeam implementation
    private void removeVerticalSeamInternal(int[] seam) {
        for (int y = 0; y < height; y++) {
            int k = 0;
            for (int x = 0; x < width; x++) {
                if (x != seam[y]) {
                    pixels[k][y] = pixels[x][y];
                    k++;
                }
            }
        }
        
        width--;
    }
    
    // set of validations
    private void checkSeamValidity(int[] seam, boolean vertical) {
        if (seam == null) {
            throw new IllegalArgumentException("seam cannot be null");
        }
        
        if (vertical && width <= 1) {
            throw new IllegalArgumentException("width must be greatern than 1");
        }
        
        if (!vertical && height <= 1) {
            throw new IllegalArgumentException("height must be greatern than 1");
        }
        
        int length = vertical ? height : width;
        if (seam.length != length) {
            throw new IllegalArgumentException("seam size must be equals to the height of the picture");
        }
        
        int limit = vertical ? width : height;
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= limit) {
                throw new IllegalArgumentException("seam index is outside its prescribed range");
            }
            
            if (i < seam.length - 1 && Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("two adjacent entries differ by more than 1");
            }
        }
    }
    
    // store picture data into a matrix
    private void storePictureData(Picture p) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[x][y] = p.getRGB(x, y);
            }
        }
    }
    
    // square of the gradient: Δ^2(x, y) = R(x, y)^2 + G(x, y)^2 + B(x, y)^2
    private int getSquaredGradient(int lcolor, int rcolor) {        
        int lr = (lcolor >> 16) & 0xFF;
        int lg = (lcolor >> 8) & 0xFF;
        int lb = (lcolor >> 0) & 0xFF;
        
        int rr = (rcolor >> 16) & 0xFF;
        int rg = (rcolor >> 8) & 0xFF;
        int rb = (rcolor >> 0) & 0xFF;
        
        int rx = (rr - lr);
        int gx = (rg - lg);
        int bx = (rb - lb);
        
        return (rx * rx) + (gx * gx) + (bx * bx);
    }
    
    // column x and row y
    private void relaxVertically(int x, int y, double[] energyTo, double[] oldEnergyTo, int[][] edgeTo) {
        if (y == 0) {
            energyTo[x] = energy(x, y);
            edgeTo[x][y] = -1;
            return;
        }
        
        double min;
        int edge;

        if (x == 0) {     
            // only have 2-edges (mid, right)
            double a = oldEnergyTo[x];
            double b = oldEnergyTo[x + 1];
            min = Math.min(a, b);
            edge = (a == min) ? x : x + 1;
            
        } else if (x == width - 1) {
            // only have 2-edges (left, mid)
            double a = oldEnergyTo[x];
            double b = oldEnergyTo[x - 1];
            min = Math.min(a, b);
            edge = (a == min) ? x : x - 1;

        } else {
            // for 3-edges
            double a = oldEnergyTo[x - 1];
            double b = oldEnergyTo[x];
            double c = oldEnergyTo[x + 1];
            min = Math.min(Math.min(a, b), c);
            
            if (a == min) {
                edge = x - 1;
            } else if (b == min) {
                edge = x;
            } else {
                edge = x + 1;
            }
        }
            
        energyTo[x] = min + energy(x, y);
        edgeTo[x][y] = edge;
    }
    
    // transposes the picture data
    private void transpose() {
        int[][] tPixels = new int[height][width];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tPixels[y][x] = pixels[x][y];
            }
        }
        
        pixels = tPixels;
        int temp = width;
        width = height;
        height = temp;
    }
}