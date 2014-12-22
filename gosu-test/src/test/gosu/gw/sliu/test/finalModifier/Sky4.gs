package gw.sliu.test.finalModifier

class Sky4 {
  static final var x : int = 9
  static final var x_array : int[] = {1,2,3}

  final construct(){}

  function methodFromParentClass(){

  }

  final function instanceMethod(){}
  static function classMethod(){ x_array[1] = 4}
  static final function finalClassMethod(){}

}