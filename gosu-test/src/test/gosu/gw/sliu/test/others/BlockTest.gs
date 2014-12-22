package gw.sliu.test.others

uses java.util.ArrayList
uses java.lang.Integer

class BlockTest {

  function test1 <T> (p: block() : T) : ArrayList<T>{
    return (new ArrayList<T>())
  }

  function test2 <Q> (p: block() : Q) : Q {
    return p()
  }

  function test3 <Integer> () {
    test2( (/ -> return new Integer(10)))
  }

}