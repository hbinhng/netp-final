package org.uet.int3304.node.Generator;

import java.util.Random;

public class BloodPressureGenerator implements Generator<Float> {
    private static final float BASE_DIASTOLIC = 77f;
    private static final float BASE_SYSTOLIC = 115f;
    private static final float SYSTOLIC_NOISE = 10f;
    private static final float DIASTOLIC_NOISE = 6f;

    private final long spawn;
    private final Random random;
    private float last;

    public BloodPressureGenerator() {
        spawn = System.currentTimeMillis();
        random = new Random();

        last = 0.5f;
    }

    @Override
    public Float next() {
        var time = System.currentTimeMillis();
        var timeOffset = time - spawn;

        var directionFactor = random.nextFloat();

        // If the last point is near upper bound, it should tend to go
        // down, otherwise it should tend to go up.
        var direction = directionFactor < last ? -1 : 1;

        // Applying sine to create cycle effect on generated data.
        var next = last + random.nextFloat() * direction * 0.04f * Math.abs(Math.sin(timeOffset));

        // Limit data to [0, 1] range.
        last = Math.max(0f, Math.min((float) next, 1f));

        var systolic = (float) Math.floor(BASE_SYSTOLIC + SYSTOLIC_NOISE * last);
        var diastolic = (float) Math.floor(BASE_DIASTOLIC + DIASTOLIC_NOISE * last);

        return systolic + diastolic / 100f;
    }
}
