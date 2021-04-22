% Fill out this file for A3 Problem 2.

subclass(C1, C2) :-
      _________________.

subclass(C1, C2) :-
      _________________,
      _________________.


recursive(C) :-
      _________________.

recursive(C) :-
      ________________,
      ________________.

over_ridden(B, C, M, T) :-   
       _________________,
       _________________,
       _________________,
       _________________.


inherits(C,L) :-
      % use setof and \+ over_ridden
      % in writing your answer.  Okay
      % to define helper predicate(s).
      % 
      % Use print_list(L) shown below
      % for printing out your answer.

print_list([]).
print_list([H|T]) :- write(H), nl, print_list(T).


% basic is true T is a built-in type; needed by cycle
basic(T) :-
      _____________________.

cycle(C) :-
      db_method(____, ____, ____),
      member(_____, _____).
cycle(C) :-
      _________________,
      _________________,
      _________________,
      cycle(___, ___, ___).

cycle(C,C0,_) :-
       _________________,
       _________________.
cycle(C,C0,CL) :-
       __________________,
       __________________,
       __________________,
       __________________,
       cycle(___, ___, ___).





