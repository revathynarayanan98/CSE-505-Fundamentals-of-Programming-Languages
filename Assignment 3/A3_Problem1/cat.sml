(*4 b*)

datatype 'a tree = leaf of 'a | node of 'a tree * 'a tree;

val tr = node(node(leaf("Go"),leaf("Java")),node(leaf("Python"),leaf("Scala")));

fun cat_cps (leaf(s),K) = K(s)
    | cat_cps (node(t1, t2),K) = cat_cps(t1,(fn x => cat_cps(t2,(fn y=> K(x^" "^y)))))

fun cat2(tr) = cat_cps(tr,(fn x => x)) 

(*call cat2(tr)*)


