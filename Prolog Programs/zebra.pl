zebra(Sol) :-
   Sol = [_,_,_,_,_],

   member( [englishman,_,_,_,red], Sol ),
   member( [spaniard,dog,_,_,_], Sol),
   member( [_,_,_,coffee,green], Sol),
   member( [ukrainian,_,_,tea,_] ,Sol),

   next([_,_,_,_,green], [_,_,_,_,white], Sol),

   member( [_,snake,winston,_,_], Sol),
   member( [_,_,kool,_,yellow], Sol),

   Sol = [_, _, [_,_,_,milk,_] ,_ ,_],
   Sol = [[norwegian,_,_,_,_] ,_, _, _, _],

   next( [_,_,chesterfield,_,_] ,[_,fox,_,_,_], Sol),
   next( [_,_,kool,_,_], [_,horse,_,_,_], Sol),

   member( [_,_,lucky,juice,_], Sol),
   member( [japanese,_,kent,_,_], Sol),

   next( [norwegian,_,_,_,_], [_,_,_,_,blue], Sol),

   member( [_,_,_,water,_], Sol),
   member( [_,zebra,_,_,_], Sol).


right(X,Y,Sol) :- append(_,[X,Y|_],Sol).

next(X,Y,Sol) :- right(X,Y,Sol) ;
                 right(Y,X,Sol).
