package com.github.overlogic.util;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class IntRangeIdentities extends AbstractStackIdentities<Integer> {
	public IntRangeIdentities(final int minInclusive, final int maxExclusive) {
		super(IntStream
				.range(minInclusive, maxExclusive)
				.boxed()
				.collect(Collectors.toList())
		);
	}
}
