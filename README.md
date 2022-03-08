# NanoBASIC
An extremely minimal interpreter for BASIC with only a handful of legal instructions. Below is an example script which prints all numbers up to 1 million.

```basic
10 LET A = 0
20 LET A = A + 1
30 PRINT A
40 IF A >= 1000000 THEN GOTO 60
50 GOTO 20
60 PRINT "DONE"
```

To run a script, first clone this repository and build the jar using `./gradlew shadowJar`, then you may find the jar in the `build/libs` folder. You can run your script using `java -jar NanoBASIC-all.jar path/to/script.bas`.

## Instructions

`LET <var> = <expr>` - Assign a variable to a numeric value
`PRINT <expr>` - Print a numeric value
`PRINT "string"` - Print a string
`IF <condition> THEN <instruction>` - Conditionally run an instruction
`GOTO <line>` - Jumps to a line number
`GOSUB <line>` - Jumps to a line number and pushes the previous line number onto the stack
`RETURN` - Returns to the last line pushed onto the stack
`INPUT <var>` - Reads a number from stdin and puts it into a variable
`END` - Ends the program
