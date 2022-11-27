package com.company;

import java.util.Arrays;
import java.util.Scanner;


public class Main {


    private static double[] func = null; // x1 x2 ... xn b
    private static double[][] res = null;   // ограничения формата x1 x2 ... xn b1
    //                     x1 x2 ... xn b2 итд
    private static double[][] table = null; // Симплекс таблица
    private static int n=0, m = 0, d = 0;   //n - кол-во переменных в исх функции,
    //m - кол-во ограничений
    //d - кол-во дополнительных базовых переменных в ограничениях
    private static boolean isMax = false; // f->max?



    public static void main(String[] args) {
        autoInput();
        transform();
        createTable();
        System.out.println(Arrays.deepToString(res));
    }

    static void autoInput(){
        n=3; m=2;
        isMax = true;
        func = new double[] {3,4,6,0};
        res = new double[][]{   {2,5,2,12},    //{2,1,-2,0,0,0,0},
                {7,1,2,18}};    //{1,1,-1,-1,0,0,8},
    }

    static void transform() {
        boolean hasBase = false;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n+d; j++) {
                if (res[i][j] == 1) {
                    hasBase = isBase(i, j);
                    if(hasBase){
                        //resolveFunc(j);
                    }
                }
            }
            if (!hasBase)
                addBase(i);
            hasBase = false;
        }
    }

    static void resolveFunc(int row,int col){
        for(int i = 0;i<n+d;i++){

            if (i==col)
                continue;
            func[i] +=res[row][i] * func[col] ;
        }
        func[col] = 0;
    }

    static void addBase(int num){
        double[][] buf = res;
        res = new double[m][n+1+d+1];
        for(int i=0;i<m;i++){
            for(int j=0;j<n+d;j++)
                res[i][j] = buf[i][j];
            res[i][n+1+d] = buf[i][n+1+d-1];
            res[i][n+1+d-1] = i!=num?0:(isMax?1:-1);
        }
        d++;
    }

    static boolean isBase(int y,int x){
        for(int i = 0;i<m;i++){
            if(i!=y&&res[i][x]!=0)
                return false;
        }
        return true;
    }

    static void createTable(){

    }

    //public static void main(String[] args) {
//
    //    //autoTest();
    //    input();
    //    solve(true);
    //}
    static void input(){
        Scanner sc = new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        table = new double[n][m];
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                table[i][j]=(i==(n-1) && j==(m-1))?0:sc.nextDouble();

            }
        }
        System.out.println();
    }
    static void autoTest(){
        n=4;m=6;
        table = new double[][]{
                {9,2,1,1,0,0},
                {7,1,2,0,1,0},
                {5,1,1,0,0,1},
                {12,2,3,0,0,0}
        };
    }

    public static void solve(boolean isMax){
        while(true){
            int x = findX(isMax);

            if(x==-1){
                break;
            }
            int y =findY(x);
            printMatr(x,y);

            if (table[n - 1][x] == 0)
                break;

            resolve(x, y);
            System.out.println("______________________");

        }
        printMatr();

        System.out.println("нет разрешающего столбца");
        double[] result = new double[m-1];
        for (int i=1;i<m;i++){
            int zero = 0;
            int one = -1;
            for (int j=0;j<n-1;j++){
                if(table[j][i] == 0)
                    zero++;
                else if(table[j][i] == 1)
                    one=j;
                else
                    break;

            }
            if(one !=-1 && zero == n-2)
                result[i-1] = table[one][0];
            else
                result[i-1] = 0;

        }
        System.out.println("Результирующий вектор:");
        System.out.println(Arrays.toString(Arrays.stream(result).toArray()));
    }
    public static void printMatr(int x, int y){
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                System.out.print((y==i &&x==j?String.format("  (%4.2f)",table[i][j]):String.format(" %6.2f ",table[i][j])));
            }
            System.out.println(i==n-1?"  |":String.format("  |%6.2f",table[i][0]/table[i][x]));
        }
    }
    public static void printMatr(){
        for (int i=0;i<n;i++){
            for (int j=0;j<m;j++){
                System.out.print(String.format(" %6.2f ",table[i][j]));
            }
            System.out.println();
        }
    }

    public static int findX(boolean isMax){
        double max = 0;
        int num = -1;
        for(int j = 1;j<m;j++){
            double value = table[n-1][j];
            if(value==0)continue;
            if((isMax?value<=max:value>=max)) {
                max = value;
                num = j;
            }
        }
        return num;
    }

    public static int findY(int x){
        double min = Double.MAX_VALUE;
        int num = 0;
        for(int i = 0;i<n-1;i++){
            double value = table[i][0]/table[i][x];
            if(value <= min && value >=0){
                min = value;
                num = i;
            }
        }
        return min == Double.MAX_VALUE ? -1:num;
    }

    public static void resolve(int y, int x){
        for(int i = 0; i < n;i++){
            for(int j = 0;  j < m; j++){
                if(i == x || j == y)
                    continue;
                table[i][j] -= table[x][j] * table[i][y] / table[x][y];
            }
        }
        for(int i = 0;i<m;i++){
            if(i!=y)table[x][i] /= table[x][y];
        }
        for(int i = 0;i<n;i++){
            if(i==x) table[i][y] = 1;
            else table[i][y] = 0;
        }
    }

}

