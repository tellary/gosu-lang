package gw.sliu.test.others

class AccessModifierTest {
  class Parent {
    public var publicField : String
    private var privateField : String
    var notSpecifiedField : String
    protected var protectedField : String
    internal var internalField : String
  }
  class Child extends Parent {
    var a = publicField
    var b = privateField
    var c = notSpecifiedField
    var d = protectedField
    var e = internalField
  }

  interface IMyClass {
    function funA()
  }

  class MyClassA implements IMyClass {
    override function funA() {}
  }

  class MyClassB implements IMyClass {
    delegate _myClassB1 represents
    //    delegate _myClass2: MyClassA represents
    override function funA() {}
  }

}