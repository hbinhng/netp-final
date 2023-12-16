package org.uet.int3304.node.Generator;

import java.util.Random;

/**
 * This generate pseudo signal to emulate realtime thermometer.
 */
public class TemperatureGenerator implements Generator {
    private static final float BASE_TEMP = 36f;
    private final long spawn;
    private final Random random;
    private float last;

    public TemperatureGenerator() {
        spawn = System.currentTimeMillis();
        random = new Random(spawn);
        last = random.nextFloat();
    }

    @Override
    public float next() {
        var time = System.currentTimeMillis();
        var timeOffset = time - spawn;

        var directionFactor = random.nextFloat();
        var directionDiff = Math.abs(directionFactor - last);

        // Create drift
        if (directionDiff < 0.1)
            return BASE_TEMP + 4 * last;

        // If the last point is near upper bound, it should tend to go
        // down, otherwise it should tend to go up.
        var direction = directionFactor < last ? -1 : 1;

        // Applying sine to create cycle effect on generated data.
        var next = last + random.nextFloat() * direction * 0.04f * Math.abs(Math.sin(timeOffset));

        // Limit data to [0, 1] range.
        last = Math.max(0f, Math.min((float) next, 1f));

        return BASE_TEMP + 4 * last;
    }
}
