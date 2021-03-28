(* Problem 3(a) *)

fun f2(n) =
let
    fun f2_helper(n, p) = 
    if n = 1 
    then p
    else f2_helper(n - 1, n * n + p);
in
    f2_helper(n, 1)
end;

(* Problem 3(b) *)

fun flatten([ ]) = [ ]
| flatten(h::t) = h @ flatten(t);

fun flatten2(p) =
let
    fun flatten2_helper(a,[]) = a
    | flatten2_helper(a,(h::t)) = flatten2_helper((a @ h), t)
in
    flatten2_helper([ ], p)
end;