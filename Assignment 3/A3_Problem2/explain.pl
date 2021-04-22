% Explain the proof tree to a certain depth bound.

% Also includes builtin predicates in solve,
% including negation, but not cut.

% Do not show details of input-output and other builtins,
% including omitting arithmetic, but do show setof details.


:- dynamic builtin_pred/1.

why(G) :-
	solve(G, P),
	explain(P),!.

primitive([]).
primitive([H|T]) :-
	asserta(builtin_pred(H)),
	primitive(T).

solve(true, true) :- !.

solve(!, true) :- !.    % ignore cut

solve(A, A) :-
	builtin(A), !, A.

solve((A , B), (ProofA, ProofB)) :- !,
	solve(A, ProofA),
	solve(B, ProofB).

solve( (A -> B ; C), (Cond -> Proof)) :-
	(solve(A,Cond)
	  -> solve(B,Proof)
	  ;  solve(C,Proof)
	  ).

solve((A ; _), Proof) :-
	solve(A, Proof), !.

solve((_ ; B), Proof) :- !,
	solve(B, Proof).

solve(setof(T,G,L), setof(T,G,L,TGPL)) :- !,
	setof([T, G-Proof], solve(G, Proof), TGPL),
	extract_set(TGPL, L).

solve(A, (A :- Proof)) :-
	clause(A, B),
	solve(B, Proof).


extract_set([], []).
extract_set([[T|_]|L], [T|S]) :-
	extract_set(L,S).


builtin(G) :-
	G =.. [P | _],
	(member(P, ['=', '==', '=<', '>=', '>', '<', '\\==', '\\+' ])
	    ;
	builtin_pred(P)).

builtin_pred(append).
builtin_pred(member).
builtin_pred(write).
builtin_pred(print_list).
builtin_pred(nl).

explain((A :- Proof)) :-
	%nl, write('To what depth? '), read(DMax),
	DMax=10,
	write(A),
	explain(Proof, 1, DMax).

explain(true, _, _) :- !.

explain(A, Depth, DMax) :-
	Depth < DMax,
	builtin(A), !,
	nl, tab(Depth*6), write('<-- '), write(A).

explain((A :- Proof), Depth, DMax) :-
	Depth < DMax,
	nl, tab(Depth*6), write('<-- '), write(A),
	D is Depth + 1,
	explain(Proof, D, DMax).

explain((A, B), Depth, DMax) :-
	Depth < DMax,
	explain(A, Depth, DMax),
	explain(B, Depth, DMax).

explain(setof(T,G,L,TGPL), Depth, DMax) :-
	Depth < DMax,
	nl, tab(Depth*6),
	write('<--'), write(setof(T,G,L)),
	D is Depth + 1,
	explain_setof(TGPL, D, DMax).

explain(_, _, _).

explain_setof([], _, _).
explain_setof([[_, _-Proof] | L], Depth, DMax) :-
	explain(Proof, Depth, DMax),
	explain_setof(L, Depth, DMax).


/*
explain((not(A) :- true), Depth, DMax) :-
	Depth < DMax, !,
	nl, tab(Depth*6),
	write('because it is not true that '),
	talk(A).


Builtins = [arg, assert, asserta, assertz, atom, atomic,
		      bagof, call, clause, close, consult,
		      display, fail, functor, get, get0,
		      integer, is, nonvar, number, nl,
		      not,
		      print, put, read, retract,
		      sort,
		      true, var, write

*/
