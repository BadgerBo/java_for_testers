package ru.stqa.pft.sandbox;

public class MyFirstProgram {

  public static void main(String[] args) {
    
    Square s = new Square(5);
    System.out.println("Площадь квадрата со стороной " + s.l + " = " + s.area());

    Rectangle r = new Rectangle(4, 6);
    System.out.println("Площадь прямоугольника со сторонами " + r.a + " и " + r.b + " = " + r.area());

    Point p1 = new Point(1, 1);
    Point p2 = new Point(2, 12);

    System.out.println("Расстояние между точками = " + p2.getDistance(p1));
  }

  public static void hello(String somebody) {
    System.out.println("Hello, " + somebody + "!");
  }

}