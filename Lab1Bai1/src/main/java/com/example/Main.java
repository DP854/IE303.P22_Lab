package com.example;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static double approximatePi(int totalPoints) {
        Random rand = new Random();
        int insideCircle = 0;

        for(int i = 0; i < totalPoints; i++) {
            //Tọa độ ngẫu nhiên trong khoảng [-1, 1]
            double x = rand.nextDouble() * 2 - 1;
            double y = rand.nextDouble() * 2 - 1;

            //Kiểm tra điểm có nằm trong đường tròn không
            if(x * x + y * y <= 1) {
                insideCircle++;
            }
        }

        //Xấp xỉ giá trị của Pi
        return 4.0 * insideCircle / totalPoints;
    }
    public static void main(String[] args) {
        int totalPoints = 1000000;

        double pi = approximatePi(totalPoints);
        Scanner input = new Scanner(System.in);
        System.out.println("Nhập bán kính: ");
        int radius = input.nextInt();

        double area = pi * radius * radius;

        System.out.println("Diện tích của hình tròn bán kính r là: " + area);
    }
}