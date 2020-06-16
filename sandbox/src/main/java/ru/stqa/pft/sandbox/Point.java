package ru.stqa.pft.sandbox;

public class Point {

    double x;
    double y;
  
    public Point(double x, double y) {
      this.x = x;
      this.y = y;
    }
  
    public double getDistance(Point p) {
      return Math.round(Math.sqrt(Math.pow(x - p.x, 2) + Math.pow(y - p.y, 2))); // Math.pow возведение в степень
    }
  }

