import scala.collection.mutable.ArrayBuffer

class Lexer(source: String) {
  private var ptr = 0
  private var row = 1
  private var col = 1

  def advance(ch: Option[Char]): Unit = {
    ch match {
      case Some(ch) =>
        ptr += 1
        if (ch == '\n') {
          row += 1
          col = 1
        } else {
          col += 1
        }
      case None =>
    }
  }

  def tryReadNext(): Option[Char] = {
    val ch = tryGetNext()
    advance(ch)
    ch
  }

  def tryGetNext(): Option[Char] = {
    if (ptr < source.length()) {
      Some(source(ptr))
    } else {
      None
    }
  }

  // identifier = [_a-zA-Z][-0-9a-zA-Z]*
  def tryParseIdentifier(): Option[IdentifierToken] = {
    var buf = new StringBuilder()

    if (!tryGetNext().exists(ch => ch == '_' || 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z'))
      return None

    while (tryGetNext().isDefined) {
      val next = tryGetNext()
      next match {
        case Some(ch) if ch == '_' || '0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'z' || 'A' <= ch && ch <= 'Z' =>
          buf.append(ch)
          tryReadNext()
        case _ => return Some(new IdentifierToken(new Position(row, col), buf.toString()))
      }
    }
    None
  }

  // number = [0-9]+
  def tryParseNumeric(): Option[NumericToken] = {
    var buf = new StringBuilder()
    if (tryGetNext().isEmpty)
      return None
    while (true) {
      val next = tryGetNext()
      next match {
        case Some(ch) if '0' <= ch && ch <= '9' =>
          buf.append(ch)
          tryReadNext()

        case _ => return Some(new NumericToken(new Position(row, col - buf.length), buf.toString()))
      }
    }
    None
  }

  //  bracket = [()[]{}]
  def tryParseBracket(): Option[BracketToken] = {
    tryGetNext()
      .filter(x => x == '(' || x == ')')
      .map(x => new BracketToken(new Position(row, col), x.toString))
  }

  def omitWhiteSpace(): Unit = {
    while (tryGetNext().isDefined) {
      if (!java.lang.Character.isWhitespace(tryGetNext().get))
        return
    }
  }

  def tryMatch(): Option[Token] = {
    omitWhiteSpace()
    tryParseBracket() match {
      case Some(x) => return Some(x)
      case None =>
    }
    tryParseNumeric() match {
      case Some(x) => return Some(x)
      case None =>
    }

    tryParseIdentifier() match {
      case Some(x) => return Some(x)
      case None =>
    }
    None
  }

  def doLexer(): Array[Token] = {
    var tokens = new ArrayBuffer[Token]()
    while (tryGetNext().isDefined) {
      tryMatch() match {
        case Some(token) => tokens += token
        case None => throw new Exception("Parse error")
      }
    }

    tokens.toArray
  }
}
