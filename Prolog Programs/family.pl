% Sample Queries:  
%
% ?- gp(bob, sue).
% ?- anc(bob, sue).
% ?- anc(bob, X).
% ?- setof(X, anc(bob, X), L).


f(bob, mark).
f(ann, mark).
f(mark, joe).
f(mary, steve).

m(bob, mary).
m(ann, mary).
m(mark, jane).
m(mary, sue).

p(X, Y) :- f(X, Y).
p(X, Y) :- m(X, Y).

gp(X, Y) :- p(X, Z), p(Z, Y).

anc(X,Y) :- p(X,Y).
anc(X,Y) :- p(X,Z), anc(Z, Y).
