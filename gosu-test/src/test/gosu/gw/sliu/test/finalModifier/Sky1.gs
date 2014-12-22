package gw.sliu.test.finalModifier

uses java.lang.Integer

/**
 * Created by sliu on 12/3/2014.
 */
class Sky1 {
    ///////////////////////////////
    final var x1: int = 10
    final var x2: int
    final var x3: Integer
    final var x4: int
    final var x5: String
    static final var x6: int
    static final var x7: int = 10

    final enum x8 {
      TEST1, TEST2, TEST3}

    static final enum x9 {
      TEST1, TEST2, TEST3}

    static construct(final x) {
      x6 = 10
    }

    construct() {
      var vv = "vicky"
      vv = 4

      x1 = 11

      x2 = 10
      x3 = new Integer(30)

      x4 = x2

      if (x2 > 5){
        x5 = "assign value to x5"
      }
    }

    final construct(x: String) {
    }

    construct(b: boolean) {
      x3 = new Integer(20)
      x4 = 10
      x5 = "test"

      print(x7)
    }

    ////////////////////////////////

    final abstract class Class1 {
      // <-- should show error: 'final' cannot be along with 'abstract'

      final abstract function Method1()

      // <-- show error for 'final' cannot combine with 'abstract'
    }

    final class Class2 {
    }

    class Class3 {
    }

    class Class4 extends Class1 {
    }

    class Class5 extends Class2 {
    }

    class Class6 extends Class3 {
    }

    /////////////////////////////////

    function method1(final xx1: int) {
      final var x = 10
      x = 20

      final var y: int
      if (1 == 0){
        y = 10
      }

      final var z: int
      if (1 == 0) {
        z = 100
      } else {
        z = 200
      }
    }

    //////////////////////////////////

    class Auto {
      private var _field4: String
      // a final property -- no subtype can reimplement / override this!

      final property get Plate(): String {
        return _field4
      }

      final property set Plate(newPlate: String) {
        _field4 = newPlate
      }

      // a final function/method -- no concrete subtype can reimplement / override this!

      final function RegisterWithDMV(registrationURL: String) {
        print("test")
      }
    }

    class Auto1 extends Auto {
      override property get Plate(): String {
        return "override"
      }

      override function RegisterWithDMV(registrationURL: String) {
        print("override")
      }

      construct() {
      }
    }

    //////////////////////////////////

    class final1
    {
      function SuffixTest(final greeting: String) {
        greeting = greeting + "fly"
        print(greeting)
      }
    }

    ///////////////////////////////////////

    class AnotherClass {
      static function classMethod() {
      }
    }

    function TestMethod2(final final x: int){
        final volatile var test: int = 10
        final final var test2: int = 15
    }

        ////////////////////////////////////////



        /////////////////////////////////////

  class Test1{
        var b = block(String): Number
        var bb = \ s: String -> s.length()

        var y = int
        var x = 5
        y = 53
        function fim1(p: block()) {}

        function bar() {
        fim1(\ -> print("sdf"))
        }
  }

}