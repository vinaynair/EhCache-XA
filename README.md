
To run test against BigMemory 4x, perform :-
$> mvn clean test

To run test against BigMemory 3.7.5, perform :-
$> mvn clean test -Pbm-3.7.5-local

To run test against BigMemory 3.7.5 in a clustered setup, start TSA and then perform :-
$> mvn clean test -Pbm-3.7.5-distributed


