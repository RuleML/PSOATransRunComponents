
/*
File:           test.p
Description:    Dummy file in the TPTP format, which provides
                a good coverage of TPTP features.
Author:         Andrei Tchaltsev
Last modified:  Apr 06, 2006, by Alexandre Riazanov, semantic errors fixed.
*/

/* START 
*/
% --- fof annotated formula ---
fof(qwe, axiom,$false, unknown).
fof(qwe, axiom,prop, unknown).
fof(123,assumption,~prop, 2135).
fof('asdg',unknown,(~~~~~prop), '32\\4',
[iquote('')]).
fof('',unknown,(prop) | ~ $$prop1 != pro2p).
fof('\\',unknown,?[AA] : ![Q,Q] : prop3).
fof('\'',unknown,~![BB,DD,CC] : ~? [EE, Qqwerqwe___r] : (prop) <=> q).
fof(x2109,conjecture, ( ? [X] : p)).
fof(x2109,conjecture, ( ? [X] : ![BB] : p)).
fof(x2109,conjecture, ( ? [X] : p & q)).
fof(x2109,conjecture, ( ? [X] : (p & q))).
fof(x2109,conjecture, ( ? [X] : ~(p & q))).
fof('',axiom,ee<=>ee).
fof('',axiom,ee<=ee).
fof('',axiom,ee=>ee).
fof('',axiom,ee<~>(ee)).
fof('',axiom,ee~|ee).
fof('',axiom,ee~&ee).
fof('',axiom,ee & ee & ~rr).
fof('',axiom,~~ee | df | ~~ee).
fof('',axiom,(ee & df) | (df & ee)).
fof('',axiom,(ee & prop1 != prop2 & df)).
fof(15, axiom, agatha, inference(assume,[status(cth)],[14])).
% --- cnf formula ---
cnf(qwer,hypothesis,(dd)).
cnf('qwer',definition,dd,3).
cnf(123,theorem,ee|~sd,213,[]).
cnf(123,theorem,~ee|sd|~s(-23.2)=f(a)|qq,213,[]).
cnf('', conjecture,(e)).
% --- include ----
include('ds\'f').
include('dsf\\', [233]).
include('', [233,'']).
include('dsf', [233,asd,'','',a, z, 1, 0, 9]).
% --- input formula (old fof) and input clause (old cnf) ---
input_formula(123, type, (ert)).
input_formula(123, negated_conjecture, ~ert <=> (er|p|d|er)).
input_clause(12,plain,[]).
input_clause(we,unknown,[++aa]).
input_clause('asdf',axiom,[--as]).
input_clause('',axiom, [++ $true, ---3="", +++3=+2,--equal(r,t)]).
% --- atom ---- in the third argument of cnf
cnf('',axiom, prop).
cnf('',axiom, prop5(prop1)).
cnf('',axiom, 'prop15'(prop1, prop2)).
cnf('',axiom, ''('2',prop2, prop31)).
cnf('',axiom, prop6(2135)).
cnf('',axiom, prop7("")).
cnf('',axiom, $true).
cnf('',axiom, $false).
cnf('',axiom, equal(AA,BB)).
cnf('',axiom, prop8 = prop9).
cnf('',axiom, prop11(prop1) != prop13(prop2)).
cnf('',axiom, prop12(prop2, prop1) = prop14).
cnf('',axiom, prop1 = $$prop2).
cnf('',axiom, $$prop1 != pro2p).
cnf('',axiom, $$prop1 != $$prop2).
cnf('',axiom, $prop1(prop1) != $prop2(prop2)).
cnf('',axiom, $prop3 != $prop4).
cnf('',axiom, 1234 != EE).
cnf('',axiom, "" = 21343).
cnf('',axiom, prop16 != "").
cnf('',axiom, $$rf).
cnf('',axiom, $$rf2($$f1)).
% --- term ------ on both sides of "=" in the third argument of CNF
cnf('',axiom, const = VVDF).
cnf('',axiom, DD = AA).
cnf('',axiom, const17(const1, copn) = a2(a1)).
cnf('',axiom, b1(b,b,b,b,b) = 1235).
cnf('',axiom, 1234.1234 = +2456).
cnf('',axiom, -1234.4 = +1.2).
cnf('',axiom, "awdg" = "").
cnf('',axiom, "\"" = "\\"). %"
cnf('',axiom, $$dfa = $$asg(a,a)).
% --- source --------
cnf('', axiom, a3, qwer).
cnf('', axiom, a4, 'adgahg').
cnf('', axiom, a5, 12345234).
cnf('', axiom, a6, inference(deduction, [qqq,www], [qwe])).
cnf('', axiom, a7, inference(paramodulation, [],[2134:'12345'])).
cnf('', axiom, a8, inference(modus_ponens, [], 
          [inference(cnf_refutation, [qqq], 
	             [qwe, qwer:'www', rre]):'', eee])).
cnf('', axiom, a9, introduced(definition)).
cnf('', axiom, a9, introduced(axiom_of_choice)).
cnf('', axiom, a9, introduced(tautology)).
cnf('', axiom, a9, introduced(assumption)).
cnf('', axiom, a9, introduced(definition, [refution(file('', unknown))])).
cnf('', axiom, a9, file('___')).
cnf('', axiom, a9, file('___', 123)).
cnf('', axiom, a9, file('___', unkn)).
cnf('', axiom, a9, file('', unknown)).
cnf('', axiom, a9, creator('asdgag', [iquote('iquote')])).
cnf('', axiom, a9, creator('qqq')).
cnf('', axiom, a9, theory(equality)).
cnf('', axiom, a9, theory(ac)).
cnf('', axiom, a9, unknown).
% --- useful info --- the forth argument of CNF
cnf('', axiom, a12, unknown, []).
cnf('', axiom, a12, unknown, [asdfa]).
cnf('', axiom, a12, unknown, [fun(wer), 
	description('asdf'),
	iquote('asdd'),
	status(tau),
	deduction([s]),
	deduction(atom, [] ),
	deduction(atom, [s] ),
	deduction(atom, [s,a,d] ),
	deduction(s1(s2)),
	deduction_(atom, [s] ),
	ded(atom, [as]),
	assumption(['asdf']),
	assumption(['asdf', 1234]),
	assumption(['asdf', 1234, agdg]),
	refutation(file('asdf', unknow)),
	refutation(file('asdf', 12355)),
	infer(atom, as),
	as,
  er34,
  2346,
  +45.64,
  345:67,
  4:rfg:"kj":234:[[]],
  -4
	]).	
% --- general terms --- as arguments of function "f" in the forth argument of CNF 
cnf('', axiom, a13, unknown, [f(
const,
fun (asdf,a),
'fun' (a),
f (a,a,a,a),
[],
[q],
['q', b, a, rt:fgt, 234, 3.4, -4, +0, "", "adsg", "@":[123:[]],
d,
VAR,
(![A,B] : ~a12 <=> a13),
(~a14 ~| a15(a17,18)),
(a17 | a18)
])]).
% --- names --- as the first argument of FOF 
fof(qwerqwer, lemma, a14).
fof(a, lemma, a14).
fof('', lemma, a14).
fof('s98 as6df _ a', lemma, a14).
fof('\'', lemma, a14). %'
fof(123441, lemma, a14).
% ---- numbers ---- on both sides of "=" in the third argument of CNF
cnf('',axiom, +1 = 0).
cnf('',axiom, +1 = 234).
cnf('',axiom, 0 = +0).
cnf('',axiom, 1234.23456 = 0.2345).
cnf('',axiom, -2345 = -3245.3245).
cnf('',axiom, -2345.0 = -3245e0).
cnf('',axiom, -2345.0e-10 = -3245e-0).
cnf('',axiom, 2345.0e23462376 = -3245e+0).
cnf('',axiom, +2345.0e+23462376 = 3245e0).
/*% --- comments---*/
%$$asdf
/*asdg */  /*$$ asdf */
%$$
% $$
/* */
/*$$ */
/* $$ */
/*
*/
/* END */
