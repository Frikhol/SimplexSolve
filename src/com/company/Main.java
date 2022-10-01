package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static double[][] table = null;
    private static int n=1, m = 1;

    public static void main(String[] args) {
        /*System.out.println("Input n and m");
        Scanner line = new Scanner(System.in);
        n = line.nextInt();
        m = line.nextInt();
        table = new double[n][m];
        */
        autoTest();
        solve(false);
    }

    static void autoTest(){
        n=4;m=6;
        table = new double[][]{ {9,2,1,1,0,0},
                                {7,1,2,0,1,0},
                                {5,1,1,0,0,1},
                                {12,2,3,0,0,0}
        };
    }

    public static void solve(boolean isMax){
        while(true){
            int x = findX(isMax);
            if (table[n - 1][x] == 0)
                break;
            int y = findY(x);
            for (int i=0;i<n;i++){
                for (int j=0;j<m;j++){
                    System.out.print((y==i &&x==j?"(":"")+table[i][j]+(y==i &&x==j?") ":" "));
                }
                System.out.println();
            }


            resolve(x, y);
            System.out.println("______________________");

        }
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

    public static int findX(boolean isMax){
        double max = 0;
        int num = 2;
        for(int j = 1;j<m;j++){
            double value = table[n-1][j];
            if(value==0)continue;
            if((isMax?-value:value)>=max) {
                max = value;
                num = j;
            }
        }
        return num;
    }

    public static int findY(int x){
        double min = table[0][0]/table[0][x];
        int num = 0;
        for(int i = 0;i<n-1;i++){
            double value = table[i][0]/table[i][x];
            if(value <= min && value >=0){
                min = value;
                num = i;
            }
        }
        return num;
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
