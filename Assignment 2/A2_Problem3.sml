(* Problem 3(a) *)

fun f(1) = 1
| f(n) = n * n + f(n-1);

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

(* Problem 3(c) *)

datatype 'a tree = leaf of 'a | node of 'a tree * 'a tree;
fun cat(leaf(s)) = s
| cat(node(t1, t2)) = cat(t1) ^ " " ^ cat(t2);

fun cat2(tr) =
let fun cat2_helper([], a) = a
    | cat2_helper(leaf(s) :: t, a) = cat2_helper(t, a ^ " " ^ s)
    | cat2_helper(node(leaf(s), t2) :: t, a) = cat2_helper(t2 :: t, a ^ " " ^ s)
    | cat2_helper(node(t1, t2) :: t, a) = cat2_helper([t1, t2], a)
in
    cat2_helper([tr], "")
end;