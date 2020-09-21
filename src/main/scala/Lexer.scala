import scala.collection.immutable
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

  def tryReadNext(): Option[Char] = tryReadNext(_ => true)


  def tryReadNext(cond: Char => Boolean): Option[Char] = {
    val ch = tryGetNext().filter(cond)
    ch match {
      case Some(ch) => advance(Some(ch)); Some(ch)
      case None => None
    }
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

    if (!tryGetNext().exists(ch => ch == '_' || ch.isLetter))
      return None
    val col = this.col;
    val buf = new StringBuilder()
    while (tryGetNext().isDefined) {
      val next = tryGetNext()
      next match {
        case Some(ch) if ch == '_' || ch.isLetterOrDigit =>
          buf.append(ch)
          tryReadNext()
        case _ => return Some(new IdentifierToken(new Position(row, col), buf.toString()))
      }
    }
    None
  }

  // number = [0-9]+
  def tryParseNumeric(): Option[NumericToken] = {
    if (!tryGetNext().exists(Character.isDigit))
      return None

    val col = this.col;
    val buf = new StringBuilder()
    while (true) {
      val next = tryGetNext()
      next match {
        case Some(ch) if ch.isDigit =>
          buf.append(ch)
          tryReadNext()

        case _ => return Some(new NumericToken(new Position(row, col), buf.toString()))
      }
    }
    None
  }

  //  bracket = [()[]{}]
  def tryParseBracket(): Option[BracketToken] = {
    val col = this.col;
    tryGetNext()
      .filter(x => x == '(' || x == ')')
      .map(x => {
        tryReadNext()
        new BracketToken(new Position(row, col), x.toString)
      })
  }

  def omitWhiteSpace(): Unit = {
    while (tryReadNext(Character.isWhitespace).isDefined) {

    }
  }

  def tryParseSemicolon(): Option[SemicolonToken] = {
    val col = this.col
    tryReadNext(ch => ch == ';').map(_ => new SemicolonToken(new Position(row, col)))
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
    tryParseSemicolon() match {
      case Some(x) => return Some(x)
      case None =>
    }
    None
  }

  def doLexer(): immutable.Seq[Token] = {
    var tokens = new ArrayBuffer[Token]()
    while (tryGetNext().isDefined) {
      tryMatch() match {
        case Some(token) => tokens += token
        case None => throw new Exception("Parse error")
      }
    }

    tokens.toSeq
  }
}
