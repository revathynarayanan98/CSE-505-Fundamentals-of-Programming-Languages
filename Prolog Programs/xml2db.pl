% =================== Parser for restricted XML ======================

% ?- xml2db(input).
% ?- tree(I, A, M).
% ?- treeelem(I, A, M).


:- include(lex).

xml2db(File) :-   
   open(File,read, Stream),
   lex(Stream, Tokens), 
   xml(L, Tokens, []),
   makeDB(L).

% ----------------- DCG for restricted XML  ------------------------

xml([E|L]) --> element(E),  xml(L).
xml([]) --> [].

element(E) -->  begintag(N), elements(L), endtag(N), {E =.. [N|L]}.

elements([E|L]) --> element(E), xml(L).
elements([E]) --> [E].

begintag(N) --> ['<', N, '>'].
endtag(N) -->   ['<', '/', N, '>'].

% ----------------- Make a simple Database  ------------------------

makeDB([]).
makeDB([H|T]) :- asserta(H), makeDB(T).
