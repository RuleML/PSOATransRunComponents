fof(rule1,axiom,
		![Hu,Wi,Ch,O,Var1,Var2] :
		(
			('http://example.com/eg#family'(O) & slot(O, 'http://example.com/eg#husb', Hu) &
			slot(O, 'http://example.com/eg#wife', Wi) & slot(O, 'http://example.com/eg#child', Ch)) <= 
			(
				'http://example.com/eg#family'(O) &
				slot(O, 'http://example.com/eg#husb', Hu) &
				slot(O, 'http://example.com/eg#wife', Wi) &
				(
					('http://example.com/eg#kid'(Var1) & tuple(Var1, Hu, Ch)) |
					('http://example.com/eg#kid'(Var2) & tuple(Var2, Wi, Ch))
				)
			)
		)
	).
	
fof(fact1,hypothesis,
		'http://example.com/eg#family'('http://example.com/eg#inst4') &
		slot('http://example.com/eg#inst4', 'http://example.com/eg#husb', 'http://example.com/eg#joe') &
		slot('http://example.com/eg#inst4', 'http://example.com/eg#wife', 'http://example.com/eg#sue')
	).

fof(fact2,hypothesis,
		?[Var3] :
		(
			'http://example.com/eg#kid'(Var3) & tuple(Var3, 'http://example.com/eg#sue', 'http://example.com/eg#pete')
		)
	).

fof(conj1,conjecture,
		?[Var4] :
		(
			'http://example.com/eg#family'(Var4) & slot(Var4, 'http://example.com/eg#child', 'http://example.com/eg#pete')
		)
	).