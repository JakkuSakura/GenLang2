# GenLang 2
This is a project to implement a simple objective&functional language while getting familiar with Scala-lang.
Last GenLang was poorly implemented in C++. It barely works but due to unrealistic ambition of the language itself and lack of garbage collection.
This time, the goals are:
- [ ] very basic syntax and semantics
- [ ] limited support for objective
- [ ] support for functional operations
- [ ] a few containers
- [ ] translate GenLang to other languages.
- [ ] incrementing improvements
## Lexers
```text
number = [0-9]+
identifier = [_a-zA-Z][-0-9a-zA-Z]*
bracket = [()[]{}]
```
## Grammars
```text
root = statement +
statement = expression ;
expression = func_call | if(with else) | closure
func_call = func_identifier ([args,]) | func_identifier [args,]
block = { statement * }
if = if expression {} (elif expression {})* (else {})?
closure = (args)[: type] block
args = [arg[: type],] *
```
## Types
Object -> Int, Float, String, Array
## builtins
```text
decl = (name: String, type: TypeName) -> Type {}
ref = (name: Type, value: Type) -> Type {}
copy = (name: Type, value: Type) -> Type {}
write = (fd, value) -> Exception {}
```
