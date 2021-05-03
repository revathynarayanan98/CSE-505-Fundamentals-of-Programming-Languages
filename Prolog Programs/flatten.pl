% NAIVE FLATTEN

% ?- flatten([[1,2], [], [3,4,5]], Answer).

flatten([], []).
flatten([L | T], A) :- 
	flatten(T, Flat), 
	append(L, Flat, A).