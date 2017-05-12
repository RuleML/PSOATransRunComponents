package org.ruleml.psoa.psoatransrun;

import java.util.Iterator;

public class SimpleAnswerIterator extends AnswerIterator {
	protected Iterator<Substitution> m_iter;
	
	public SimpleAnswerIterator(Iterator<Substitution> iter) {
		m_iter = iter;
	}
	
	@Override
	public boolean hasNext() {
		return m_iter.hasNext();
	}

	@Override
	public Substitution next() {
		return m_iter.next();
	}

	@Override
	public void dispose() {
		if (m_iter instanceof AnswerIterator)
			((AnswerIterator) m_iter).dispose();
	}
}
