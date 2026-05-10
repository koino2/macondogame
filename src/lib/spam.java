package lib;

public class spam {
    public static int[] convertFromARGB(int rgb){
        int a = (rgb >> 24) & 0xFF;
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        return new int[]{a,r,g,b};
    }
    public static int convertToARGB(int a, int r, int g, int b){
        return (a << 24) |
                (r << 16) |
                (g << 8) |
                b;
    }
}
