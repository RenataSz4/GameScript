# Gramática del Parser de GameScript

## Estructura General

```
Programa ::= GAME IDENTIFICADOR SeccionEntidades SeccionReglas

SeccionEntidades ::= Entidad+

SeccionReglas ::= Regla+
```

## Entidades

```
Entidad ::= ENTITY IDENTIFICADOR '{' Atributos '}'

Atributos ::= Atributo (',' Atributo)*

Atributo ::= IDENTIFICADOR ':' Valor
```

## Valores y Expresiones

```
Valor ::= NUMERO | CADENA | Expresion

Expresion ::= Termino (('+' | '-') Termino)*

Termino ::= Factor (('*' | '/') Factor)*

Factor ::= NUMERO
         | IDENTIFICADOR ('.' IDENTIFICADOR)?
         | '(' Expresion ')'
```

## Reglas

```
Regla ::= RULE TipoRegla '{' Instrucciones '}'

TipoRegla ::= ON_START | ON_UPDATE | ON_WIN | ON_LOSE

Instrucciones ::= Instruccion*
```

## Instrucciones

```
Instruccion ::= InstruccionMove
              | InstruccionPrint
              | InstruccionIf
              | InstruccionWhile
              | InstruccionSet
              | InstruccionEndGame

InstruccionMove ::= MOVE IDENTIFICADOR Expresion Expresion ';'

InstruccionPrint ::= PRINT CADENA ';'

InstruccionIf ::= IF '(' Condicion ')' '{' Instrucciones '}' (ELSE '{' Instrucciones '}')?

InstruccionWhile ::= WHILE '(' Condicion ')' '{' Instrucciones '}'

InstruccionSet ::= SET IDENTIFICADOR '.' IDENTIFICADOR '=' Expresion ';'

InstruccionEndGame ::= END_GAME ';'
```

## Condiciones

```
Condicion ::= Expresion OPRELACIONAL Expresion

OPRELACIONAL ::= '<' | '>' | '<=' | '>=' | '!=' | '='
```

## Tokens Terminales

```
GAME        ::= "GAME"
ENTITY      ::= "ENTITY"
RULE        ::= "RULE"
ON_START    ::= "ON_START"
ON_UPDATE   ::= "ON_UPDATE"
ON_WIN      ::= "ON_WIN"
ON_LOSE     ::= "ON_LOSE"
MOVE        ::= "MOVE"
PRINT       ::= "PRINT"
IF          ::= "IF"
ELSE        ::= "ELSE"
WHILE       ::= "WHILE"
END_GAME    ::= "END_GAME"
SET         ::= "SET"

NUMERO          ::= -?[0-9]+(\.[0-9]+)?
CADENA          ::= "[^"]*"
IDENTIFICADOR   ::= [a-zA-Z_][a-zA-Z0-9_]*

DOSPUNTOS       ::= ':'
PUNTOYCOMA      ::= ';'
COMA            ::= ','
PUNTO           ::= '.'
IGUAL           ::= '='
LLAVEIZQ        ::= '{'
LLAVEDER        ::= '}'
PARENTESISIZQ   ::= '('
PARENTESISDER   ::= ')'
OPARITMETICO    ::= '+' | '-' | '*' | '/'
```

## Notas de Implementación

1. **Orden de Análisis**: El parser debe procesar las secciones en orden: GAME → ENTITIES → RULES
2. **Atributos Obligatorios**: Cada entidad debe tener `posX` y `posY`
3. **Precedencia de Operadores**:
   - Mayor precedencia: `*`, `/`
   - Menor precedencia: `+`, `-`
4. **Asociatividad**: Todos los operadores son asociativos por la izquierda
5. **Ámbito**: Los identificadores de entidades y atributos deben ser únicos en su contexto
