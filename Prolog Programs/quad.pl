% Sample Goal: Lecture 20, Slide 13

quad(A,B,C,R) :-
	D is B*B - 4*A*C,
        (D = 0.0
           ->  R is -B/(2*A)
            ;  (D > 0
                   ->  S is sqrt(D),
                       R1 is (-B + S)/(2*A),
                       R2 is (-B - S)/(2*A),
                       R = (R1,R2)
                    ;  R = imaginary )
        ).




