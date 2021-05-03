
:- use_module(library(clpr)).

db_student(tom, [85,95,80,75], 75, _).
db_student(ding,[80,90,70,80], 85, _).
db_student(hari,[90,70,60,55], 65, _).
db_student(ann,[95,80,70,85], 55, _).
db_student(aisha,[100,90,95,100], 95, _).
db_student(aidong,[70,65,70,55], 65, _).
db_student(zhazha,[5,5,5,5], 10, _).

map(90,100.01,'A').
map(80,90,'A-').
map(70,80,'B+').
map(60,70,'B').
map(50,60,'B-').
map(40,50,'C+').
map(30,40,'C').
map(20,30,'C-').
map(10,20,'D').
map(0,10,'F').

grade(Student,Final,Grade) :-
        % Make use of db_student, map, sum_list, length
	% and write some constraints.
        db_student(Student,Y,X,_),
        sum_list(Y,Z),
        map(L,H,Grade),
        A is Z/4,
        B is A*0.4,
        C is X*0.3,
        E is B+C,
        {D = Final * 0.3, F = E + D, F >= L, F < H, Final =< 100, Final >= 0}.

