% Fill out this file for A3 Problem 2.

subclass(C1, C2) :-
      db_class(C1, C2).

subclass(C1, C2) :-
      db_class(X, C2),
      subclass(C1, X).

% recursive(C) :-
%       db_field(X, Y:C).

recursive(C) :-
      subclass(X, C),
      db_field(X, Y:C).

over_ridden(B, C, M, T) :-   
      subclass(X,C),
      (B==X;subclass(B,X)),
      db_method(C,M,T),
      db_method(X,M,T).

helper(C, X, M, T) :-
      subclass(C, X),
      db_method(X, M, T),
      \+over_ridden(C, X, M, T).

inherits(C,L) :-
      % use setof and \+ over_ridden
      % in writing your answer.  Okay
      % to define helper predicate(s).
      % 
      % Use print_list(L) shown below
      % for printing out your answer.
      setof(X:M:T, helper(C,X,M,T), L),
      print_list(L).

print_list([]).
print_list([H|T]) :- write(H), nl, print_list(T).

% basic is true T is a built-in type; needed by cycle
basic(T) :-
      builtin(T).
      
cycle(C) :-
      db_method(C,X,(K->Y)),
      member(Z,K),
      \+basic(Z),
      cycle(Z,C,[Z]).

cycle(C) :-
       db_method(X,Y,(Z->K)),
       member(C,Z).

cycle(C,C0,CX) :-
      db_method(C,X,(Y->K)),
      member(C0,Y).

cycle(C,C0,CL) :-
      db_method(C,X,(Y->K)),
      member(Z,Y),
      \+basic(Z),
      \+member(Z,CL),
      cycle(Z,C0,[Z,CL]).