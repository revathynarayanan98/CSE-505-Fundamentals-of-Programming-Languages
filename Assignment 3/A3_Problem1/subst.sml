(*Comments- Higher Order function
- takes a list of type 'a list and gives a list of 'a list by applying unction f on every element h in it
- 'a -> 'b
- the output type of map is a 'b list
*)

(* 4 a *)
(*
-leaf(h) = node([leaf("x"), node([leaf("y"), leaf("x"), leaf("z")])])
-v1 = x
-v2 = a
 *)

datatype 'a ntree = leaf of 'a | node of 'a ntree list;

fun map(f,[]) = []                                              (*	Map function takes a function and a list, applies the function to head and the tail	*)
    | map(f, h::t) = f(h) :: map(f,t);

fun subst(leaf(value), v1, v2) = 		(* Subst has node function which takes the list and passes it to map function till each head is passed to leaf() where v1 is replaced with v2*)
			if value = v1 then
		    leaf(v2)
            else
            leaf(value)
  | subst(node(list), v1, v2) = 
		let fun h(tr) = subst(tr, v1, v2)
		in              
            let val h1 = map(h,list)
                in node(h1)
            end
		end;

(* input 4 a -> val tr = subst(node([leaf("x"), node([leaf("y"), leaf("x"), leaf("z")])]), "x", "a") *)