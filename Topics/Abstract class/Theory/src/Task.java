// You can experiment here, it won’t be checked

public class Task {
  public static void main(String[] args) {
    // put your code here
  }
}

abstract class A {

  public static void staticFoo() { }

  public void instanceBar() { }

  public abstract void abstractFoo();

  public abstract void abstractBar();
}

abstract class B extends A {

  public static void anotherStaticFoo() { }

  public void anotherInstanceBar() { }

  @Override
  public void abstractBar() { }

  public abstract void qq();
}

class C extends B {
  @Override
  public void abstractFoo() {

  }

  @Override
  public void qq() {

  }
}
