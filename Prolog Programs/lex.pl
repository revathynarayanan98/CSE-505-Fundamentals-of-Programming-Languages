% ===================== SIMPLE LEXICAL ANALYZER  ========================

lex(Stream,Tokens)  :-  get_chars(Stream,L), tokenize(L,Tokens), !.

get_chars(Str,L) :-  get_code(Str,C), get_chars(Str,C,L).

get_chars(_,36, []) :- !.   			% 36 = $
get_chars(Str,C, [C|L1]) :- get_chars(Str,L1).

tokenize([], []).
tokenize([C|L], L3) 	:- white(C), skip_whites(L,L2), tokenize(L2,L3).
tokenize([C|L], [X|L3]) :- d09(C), digits(X,[C|L],L2), tokenize(L2,L3).
tokenize([C|L], [X|L3]) :- alpha(C), identifier(X,[C|L],L2), tokenize(L2,L3).
tokenize(L, [X|L3])     :- special(X,L,L2), tokenize(L2,L3).

%-------------------------- WHITE SPACES ---------------------------
white(9).  % tab
white(32). % blank
white(10). % newline

skip_whites([], []).
skip_whites([C|L], L2) :- (white(C) -> skip_whites(L,L2) ;  L2 = [C|L]).

%------------------------------------------ DIGITS --------------------------
digit(X) -->  [X],  {d09(X)}.
d09(X) :- X > 47,  X < 58.

digits(dnum(N,M)) --> digs(K),specialdec(_),digs(L), {!}, {name(N,K),name(M,L)}.
digits(N) --> digs(L), {name(N,L)}. 
digs([X|L]) --> digit(X), digs(L).
digs([X]) --> digit(X).

%----------------------------------------- IDENTIFIER -----------------------
identifier(N) --> ident(L), {name(N,L)}.
ident([X|L]) --> letter(X), legits(L).
ident([X])   --> letter(X).

legits([X|W]) --> legit(X), legits(W).
legits([X])   --> legit(X).
legit(X) --> letter(X) ; digit(X).
letter(X) --> [X],  {alpha(X)}.

alpha(X) :-  X > 64,  X < 91.
alpha(X) :-  X > 96,  X < 123.
alpha(95). 				% ascii value of _

%----------------------------------------- SPECIAL CHARS --------------------
specialdec(.,[46|L],L).
special(quote, [39|L],L).
special('<',[60|L],L).
special('>',[62|L],L).
special(':',[58|L],L).
special(',',[44|L],L).
special(/,[47|L],L).
special(.,[46|L],L).
special('(',[40|L],L).
special(')',[41|L],L).
special('"',[34|L],L).
