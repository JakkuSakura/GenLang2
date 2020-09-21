object Main {
  def main(args: Array[String]): Unit = {
    val source = "123456;123456"
    val lexer = new Lexer(source)
    val tokens = lexer.doLexer()
    tokens.foreach(println)
  }
}
