class Position(val row: Int, val col: Int) {
  override def toString: String = "(%d, %d)".format(row, col)
}

class Token(val pos: Position) {
  override def toString: String = this.getClass.getSimpleName + pos
}

class IdentifierToken(override val pos: Position, val name: String) extends Token(pos) {
  override def toString: String = this.getClass.getSimpleName + "(" + name + ")" + pos
}

class NumericToken(override val pos: Position, val value: String) extends Token(pos) {
  override def toString: String = this.getClass.getSimpleName + "(" + value + ")" + pos
}

class BracketToken(override val pos: Position, val value: String) extends Token(pos) {
  override def toString: String = this.getClass.getSimpleName + "(" + value + ")" + pos
}

class SemicolonToken(override val pos: Position) extends Token(pos) {
  override def toString: String = this.getClass.getSimpleName + pos
}

