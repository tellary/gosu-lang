package gw.sliu.test.finalModifier

class Sky2 {
    var i: int = 100;
    static function classMethod() {
      final var l: int = 200
      var k: int = i
      class LocalInStaticContext {
        var k: int = i;
        // Compile-time error
        var m: int = l;
        // OK
      }
    }

    function foo() {
      var lk: int = i
      class Local {
        // A local class
        var j = i;
      }
    }

    var toBe: boolean;
    function WithDeepNesting(b: boolean) {
      toBe = b;
    }

    class Nested {
      var theQuestion: boolean;

      class DeeplyNested {
        function DeeplyNested() {
          theQuestion = toBe || !toBe;
        }
      }
    }

    function testMethod() {
      final var finalVar: int = 10
      var obj: Sky4 = new Sky4(){
        override
        function methodFromParentClass(){
          var insideOverrideFunctionVar : int = finalVar
          print (insideOverrideFunctionVar)
        }
      }
    }


    static function testMethod2(){
      final var finalVar : int = 10
      var obj : Sky4 = new Sky4(){
        override function methodFromParentClass(){
          var insideOverrideFunctionVar : int = finalVar
          print(insideOverrideFunctionVar)
        }
      }
    }

}