% Sample Goal:  

% ?- qsort([5,4,6,3,7,2,1], Answer).

qsort([], []).
qsort([P|L],  S)  :-   
	partition(L, P,  L1, L2),
	qsort(L1,  S1),
	qsort(L2,  S2),
	append(S1, [P|S2], S).

partition([], _, [], []).
partition([X|L], P, [X|L1],  L2) :-  
	X =< P, partition(L, P, L1, L2).
partition([X|L], P, L1, [X|L2]) :-  
	X > P,  partition(L, P, L1, L2).


