import scala.collection.mutable.ArrayBuffer
import scala.collection.{immutable, mutable}

abstract class Combinator
class And[L, R, H](val rule: (L, R) => H) extends Combinator {
  def apply(rule: (L, R) => H): And[L, R, H] = new And(rule)
}
class Parser(val tokens: immutable.List[Token]) {
  var ptr = 0
  def check_forward(cond: Token => Boolean) : Boolean = {
    get_forward(cond).isDefined
  }
  def get_forward(cond: Token => Boolean) : Option[Token] = {
    if(ptr >= tokens.length) {
      return None
    }
    if(cond(tokens(ptr))) {
      val result = tokens(ptr)
      ptr += 1
      return Some(result)
    }
    None
  }
  def tryParseExpression() : Option[ExpressionNode] = {
    get_forward(_.isInstanceOf[NumericToken]).map(x => new NumericNode(x.asInstanceOf[NumericToken]))
  }
  def statement(): Option[StatementNode] = {
    val ptr = this.ptr;
    val expr = tryParseExpression()
    if(expr.isEmpty)
      return None
    if (check_forward(_.isInstanceOf[SemicolonToken]))
      return Some(new StatementNode(expr.get))
    this.ptr = ptr
    None
  }
  def doParse(): RootNode = {
    val root = ArrayBuffer[StatementNode]()
    while (ptr < tokens.length)
      {
        statement() match {
          case Some(value) => root += value
          case None =>
        }
      }
    new RootNode(root.toSeq)
  }
}
