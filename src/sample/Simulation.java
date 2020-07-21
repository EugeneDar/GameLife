package sample;

public class Simulation {

    int height;
    int width;
    int[][] matrix;

    public Simulation(int height, int width) {
        this.width = width;
        this.height = height;
        matrix = new int[height][width];
    }

    public void printMatrix () {
        for (int y = 0;y < height;y++) {
            for (int x = 0;x < width;x++) {
                if (isAlive(x, y) == 1) {
                    System.out.print('*');
                } else {
                    System.out.print('.');
                }

            }
            System.out.println();
        }
        System.out.println();
    }

    public int countNeighbors (int x, int y) {
        int count = 0;

        count += isAlive(x + 1, y);
        count += isAlive(x - 1, y);
        count += isAlive(x, y + 1);
        count += isAlive(x, y - 1);

        count += isAlive(x + 1, y + 1);
        count += isAlive(x + 1, y - 1);
        count += isAlive(x - 1, y + 1);
        count += isAlive(x - 1, y - 1);

        return count;
    }

    public int isAlive (int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return 0;
        }
        return matrix[y][x];
    }

    public void setAlive (int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        matrix[y][x] = 1;
    }
    public void setDead (int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            return;
        }
        matrix[y][x] = 0;
    }

    public void step () {
        int[][] newMatrix = new int[height][width];

        for (int y = 0;y < height;y++) {
            for (int x = 0;x < width;x++) {
                int count = countNeighbors(x, y);
                if (isAlive(x, y) == 1) {
                    if (count < 2 || count > 3) {
                        newMatrix[y][x] = 0;
                    } else {
                        newMatrix[y][x] = 1;
                    }
                } else {
                    if (count == 3) {
                        newMatrix[y][x] = 1;
                    } else {
                        newMatrix[y][x] = 0;
                    }
                }
            }
        }

        matrix = newMatrix;
    }

    public void randomFilling (int density) {
        for (int y = 0;y < height;y++) {
            for (int x = 0;x < width;x++) {
                int r = (int) (Math.random() * 100);
                if (density > r) {
                    setAlive(x, y);
                } else {
                    setDead(x, y);
                }
            }
        }
    }

    public void clear () {
        for (int y = 0;y < height;y++) {
            for (int x = 0;x < width;x++) {
                setDead(x, y);
            }
        }
    }
}
