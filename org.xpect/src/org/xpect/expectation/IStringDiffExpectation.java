/*******************************************************************************
 * Copyright (c) 2012-2017 TypeFox GmbH and itemis AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Moritz Eysholdt - Initial contribution and API
 *******************************************************************************/

package org.xpect.expectation;

import org.xpect.XpectImport;
import org.xpect.expectation.impl.StringDiffExpectationImpl;

/**
 * Asserts that a specific diff has been created, used to test modifications (e.g., delete or add elements).
 */
@XpectImport(StringDiffExpectationImpl.class)
public interface IStringDiffExpectation {

	public interface ITokenAdapter<T> {
		boolean isHidden(T token, String segment);

		Iterable<String> splitIntoSegments(T token);

		float similarity(T token1, String segment1, T token2, String segment2);
	}

	public interface IToken<T> {
		boolean isHidden(String segment);

		Iterable<String> splitIntoSegments();

		float similarity(String ownSegment, T otherToken, String otherSegment);
	}

	/**
	 * Diff the tokens, often more reliable then the simply string comparison.
	 */
	<T extends IToken<? super T>> void assertDiffEquals(Iterable<T> leftTokens, Iterable<T> rightTokens);

	/**
	 * Similar to {@link #assertDiffEquals(Iterable, Iterable)} with an adapter converting the tokens to the {@link IToken} interface.
	 */
	<T> void assertDiffEquals(Iterable<T> leftTokens, Iterable<T> rightTokens, ITokenAdapter<T> adapter);

	/**
	 * Simply diffs strings.
	 */
	void assertDiffEquals(String left, String right);
}
