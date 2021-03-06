package gw.specContrib.typeinference

class SwitchTypeNarrowing {
  private enum MyEnum  { ONE, TWO }

  function test() {
    var x: Object = "neat"
    var f = x typeis String

    switch (typeof( ((x)) )) {  // crazy parentheses are valid
      case MyEnum:
        if (f) {
          switch (x) {
            case ONE:
              return
            case TWO:
              throw new java.lang.RuntimeException()
          }
        } else {
          break
        }
      /* This should work as the previous case IS terminal, we considered all the possible cases (ONE, TWO) */
      case String:
        x.contains("ne")
    }
  }
}
