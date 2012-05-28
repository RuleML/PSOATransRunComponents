fof(rule1,axiom,
		![Var1] :
		(
			person(Var1) <= male(Var1)
		)
	).
	
fof(fact1,hypothesis,
		male('john') & tuple('john', 30, 'married') & slot('john', 'job', 'doctor')
	).

fof(conj1,conjecture,
		person('john') & tuple('john', 30, 'married')
	).