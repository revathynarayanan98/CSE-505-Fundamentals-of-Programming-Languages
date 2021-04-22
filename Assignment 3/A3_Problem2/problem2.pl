:- include(database).
:- include(analyzer).
:- include(explain).

analyze :-
      write('Tiny OOPL Analyzer. Sample Commands:'), nl, nl,
      write('   subclass(c, a).'), nl,
      write('   recursive(a).'), nl,
      write('   over_ridden(c, M).'), nl,  
      write('   inherits(c, L).'), nl,
      write('   cycle(a).'), nl, nl,
      write('   why(subclass(c, a)).'), nl,
      write('   why(recursive(a)).'), nl,
      write('   why(over_ridden(c, a:f:([int, d]->int))).'), nl,
      write('   why(inherits(c, L)).'), nl,
      write('   why(cycle(a)).'), nl,
      nl.