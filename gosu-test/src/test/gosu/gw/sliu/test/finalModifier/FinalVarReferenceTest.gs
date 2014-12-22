package gw.sliu.test.finalModifier

uses gw.BaseVerifyErrantTest

class FinalVarReferenceTest extends BaseVerifyErrantTest {

  class C1 extends BaseVerifyErrantTest{
    function method1(){}
  }

  function testFinalReference(){
    final var x1 : int = 10
    var obj1 = new C1(){
      override function method1(){
        print(x1)
        assertTrue(x1 == 10)          // In Java, anonymous method can only access the final variable outside the scope of this method
      }
    }
    obj1.method1()
  }


  function testNonFinalReference(){
    var x2 : int = 5
    var b = \-> { print(x2) }
    var obj1 = new C1(){
      override function method1(){
        print(x2)
        assertNull(x2)                // In Gosu, each time the block runs, it can access all variables declared in that original scope in which it was defined.
      }
    }
    obj1.method1()
  }



}