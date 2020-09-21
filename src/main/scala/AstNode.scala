import scala.collection.immutable

class AstNode {

}
class StatementNode(val expressionNode: ExpressionNode) extends AstNode
class RootNode(val stats: immutable.Seq[StatementNode]) extends AstNode
class ExpressionNode extends AstNode
class NumericNode(val numericToken: NumericToken) extends ExpressionNode
class FunctionCallNode extends ExpressionNode

class BlockNode extends AstNode
class IfNode extends AstNode
class ClosureNode extends AstNode
class ArgumentsNode extends AstNode
