package gw.sliu.test.finalModifier

class Sky3 {

  abstract final class Test1{}
  final abstract class Test2{
    final abstract function method1(){}
    abstract final function method2(){}
  }

}