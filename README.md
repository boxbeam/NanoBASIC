# NanoBASIC
An extremely minimal interpreter for BASIC with only five legal instructions

The five instructions are `PRINT`, `INPUT` `GOTO`, `IF`, and `LET`. Each line must be preceded with its line number. Variables may only be single uppercase letters. Below is an example script which prints all the numbers up to 1 million:

```basic
10 LET A = 0
20 LET A = A + 1
30 PRINT A
40 IF A >= 1000000 THEN GOTO 60
50 GOTO 20
60 PRINT "DONE"
```

Not shown here is the `INPUT` instruction, which is used like `INPUT A` to read a number from stdin to the `A` variable.

To run a script, first clone this repository and build the jar using `./gradlew shadowJar`, then you may find the jar in the `build/libs` folder. You can run your script using `java -jar NanoBASIC-all.jar path/to/script.bas`.
