package de.jonas.object;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class Coupon {

    private final String recipient;
    private final String reason;
    private final String creator;
    private final int width;
    private final int height;
    private final int amount;

}
