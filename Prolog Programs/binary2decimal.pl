%Sample Query:
%
% ?- number([1,0,0,0], [], Answer).

number(Binary, Decimal) :- 
	bits(Decimal, _, Binary, []).  


/**********  Definite Clause Grammar ****************/

bits(X,0) --> bit(X).
bits(M,D) --> bit(X), bits(N,C),
                {D is C+1, 
                 exp(2,D,R),            
                 M is X*R + N
		}.

bit(0)  --> [0].
bit(1)  --> [1].


/**********   EXPONENTIATION PREDICATE     **********/

exp(0,0,_) :-  !,
               nl, write('0**0 is undefined.'), 
               fail.
exp(0,_,0) :-  !.
exp(_,0,1) :-  !.
exp(1,_,1) :-  !.
exp(I,N,R) :-  N > 0,
               expo(I,N,R).
expo(_,0,1) :-  !.
expo(I,N,R) :-  M is N-1,
                exp(I,M,R2),
                R is I*R2.

