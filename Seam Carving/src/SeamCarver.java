import java.awt.Color;

// Can be faster if use transpose!!!!!!!
public class SeamCarver
{
    private int[][] color;
    private double[][] energy;
    private int height;
    private int width;
    
    public SeamCarver(Picture picture)
    {
        width = picture.width();
        height = picture.height();
        color = new int[width][height];
        energy = new double[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
            {  
                color[x][y] = picture.get(x, y).getRGB();
            }
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
            {  
                energy[x][y] = energy(x, y);
            }
    }
    
    // current picture
    public Picture picture()
    {
        Picture picture = new Picture(width, height);
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                picture.set(x, y, new Color(color[x][y]));
        return picture;
    }
    
    // width  of current picture
    public int width()
    {
        return width;
    }
    
    // height of current picture
    public int height()
    {
        return height;
    }
    
    private double gradientSquare(int x, int y, int dx, int dy)
    {
        if (x == 0 || x == width - 1 || y == 0 || y == height - 1)
            return 195075.0 / 2;
        else
        {
            Color tColor1 = new Color(color[x+dx][y+dy]);
            Color tColor2 = new Color(color[x-dx][y-dy]); 
            int redGrad = tColor1.getRed() - tColor2.getRed();
            int greenGrad = tColor1.getGreen() - tColor2.getGreen();
            int blueGrad = tColor1.getBlue() - tColor2.getBlue();
            return redGrad*redGrad + greenGrad*greenGrad + blueGrad*blueGrad;
        }
    }
    
    // energy of pixel at column x and row y in current picture
    public double energy(int x, int y)
    {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IndexOutOfBoundsException();
        else
        {
            return gradientSquare(x, y, 1, 0) + gradientSquare(x, y, 0, 1);
        }
    }
    
    // sequence of indices for horizontal seam in current picture
    public int[] findHorizontalSeam()
    {
        double[][] smallestEnergy = new double[width][height+2];
        int[][] backPointer = new int[width][height];
        for (int x = 0; x < width; x++)
        {
            smallestEnergy[x][0] = Double.POSITIVE_INFINITY;
            smallestEnergy[x][height+1] = Double.POSITIVE_INFINITY;
        }
        for (int y = 1; y <= height; y++)
        {
            smallestEnergy[0][y] = energy[0][y-1];
        }
        for (int x = 1; x < width; x++)
        {
            for (int y = 1; y <= height; y++)
            {
                smallestEnergy[x][y] = smallestEnergy[x-1][y-1];
                backPointer[x][y-1] = y - 2;
                for (int i = 0; i <= 1; i++)
                {
                    if (smallestEnergy[x-1][y+i] < smallestEnergy[x][y])
                    {
                        smallestEnergy[x][y] = smallestEnergy[x-1][y+i];
                        backPointer[x][y-1] = y + i - 1;
                    }
                }
                smallestEnergy[x][y] += energy[x][y-1];
            }
        }
        int[] result = new int[width];
        int t = width - 1;
        result[t] = 0;
        for (int y = 1; y < height; y++)
        {
            if (smallestEnergy[t][result[t]+1] > smallestEnergy[t][y+1])
                result[t] = y;
        }
        for (int x = width - 2; x >= 0; x--)
        {
            result[x] = backPointer[x+1][result[x+1]];
        }
        return result;
    }
    
    // sequence of indices for vertical seam in current picture
    public int[] findVerticalSeam()
    {
        double[][] smallestEnergy = new double[width+2][height];
        int[][] backPointer = new int[width][height];
        for (int y = 0; y < height; y++)
        {
            smallestEnergy[0][y] = Double.POSITIVE_INFINITY;
            smallestEnergy[width+1][y] = Double.POSITIVE_INFINITY;
        }
        for (int x = 1; x <= width; x++)
        {
            smallestEnergy[x][0] = energy[x-1][0];
        }
        for (int y = 1; y < height; y++)
        {
            for (int x = 1; x <= width; x++)
            {
                smallestEnergy[x][y] = smallestEnergy[x-1][y-1];
                backPointer[x-1][y] = x - 2;
                for (int i = 0; i <= 1; i++)
                {
                    if (smallestEnergy[x+i][y-1] < smallestEnergy[x][y])
                    {
                        smallestEnergy[x][y] = smallestEnergy[x+i][y-1];
                        backPointer[x-1][y] = x + i - 1;
                    }
                }
                smallestEnergy[x][y] += energy[x-1][y];
            }
        }
        int[] result = new int[height];
        int t = height - 1;
        result[t] = 0;
        for (int x = 1; x < width; x++)
        {
            if (smallestEnergy[result[t]+1][t] > smallestEnergy[x+1][t])
                result[t] = x;
        }
        for (int y = height - 2; y >= 0; y--)
        {
            result[y] = backPointer[result[y+1]][y+1];
        }
        return result;
    }
    
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] a)
    {
        if (height <= 1)
            throw new IllegalArgumentException();
        if (a.length != width)
            throw new IllegalArgumentException();
        if (a[0] < 0 || a[0] >= height)
            throw new IllegalArgumentException();
        for (int i = 1; i < a.length; i++)
            if (a[i] < 0 || a[i] >= height || Math.abs(a[i] - a[i-1]) > 1)
                throw new IllegalArgumentException();
        
        height--;
        for (int x = 0; x < width; x++)
        {
            System.arraycopy(color[x], a[x]+1, color[x], a[x], height-a[x]);
            System.arraycopy(energy[x], a[x]+1, energy[x], a[x], height-a[x]);
        }
        
        for (int x = 0; x < width; x++)
        {
            if (a[x] >= 1)
                energy[x][a[x]-1] = energy(x, a[x]-1);
            if (a[x] < height)
                energy[x][a[x]] = energy(x, a[x]);
        }
    }
    
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] a)
    {
        if (width <= 1)
            throw new IllegalArgumentException();
        if (a.length != height)
            throw new IllegalArgumentException();
        if (a[0] < 0 || a[0] >= width)
            throw new IllegalArgumentException();
        for (int i = 1; i < a.length; i++)
            if (a[i] < 0 || a[i] >= width || Math.abs(a[i] - a[i-1]) > 1)
                throw new IllegalArgumentException();
        
        width--;
        for (int y = 0; y < height; y++)
        {
            for (int x = a[y]; x < width; x++)
            {
                color[x][y] = color[x+1][y];
                energy[x][y] = energy[x+1][y];
            }
        }
        
        for (int y = 0; y < height; y++)
        {
            if (a[y] >= 1)
                energy[a[y]-1][y] = energy(a[y]-1, y);
            if (a[y] < width)
                energy[a[y]][y] = energy(a[y], y);
        }
    }
}
