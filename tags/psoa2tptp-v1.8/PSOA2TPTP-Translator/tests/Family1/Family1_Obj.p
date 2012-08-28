fof(rule1,axiom,
	![Hu,Wi,Ch,Var2,Var3,Var4] :
		?[Var1] : (
			(family(Var1) & slot(Var1, 'husb', Hu) & slot(Var1, 'wife', Wi) & slot(Var1, 'child', Ch)) <=
			(
				married(Var2) & tuple(Var2, Hu, Wi) &
				(
					(kid(Var3) & tuple(Var3, Hu, Ch)) |
					(kid(Var4) & tuple(Var4, Wi, Ch))
				)
			)
		)
	).

fof(fact1,hypothesis,
		married('o1') & tuple('o1', 'joe', 'sue')
	).

fof(fact2,hypothesis,
		kid('o2') & tuple('o2', 'sue', 'pete')
	).

fof(conc1,conjecture,
	?[Var5] : (
		family(Var5) & slot(Var5, 'husb', 'joe')
	)
).