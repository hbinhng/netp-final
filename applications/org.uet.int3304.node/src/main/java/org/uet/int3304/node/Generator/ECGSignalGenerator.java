package org.uet.int3304.node.Generator;

import java.util.Random;

public class ECGSignalGenerator implements Generator {
    private static final float BASELINE_HEART_RATE = 78f / 60;
    private final long spawn;
    private final Random random;

    private final float beatDuration;
    private final long beatDurationMs;
    private final float pWaveAmplitude;
    private final long pWaveDuration;
    private final float qWaveAmplitude;
    private final long qWaveDuration;
    private final float rWaveAmplitude;
    private final long rWaveDuration;
    private final float sWaveAmplitude;
    private final long sWaveDuration;
    private final float tWaveAmplitude;
    private final long tWaveDuration;
    private final long pqDuration;
    private final long pqrDuration;
    private final long pqrsDuration;
    private final long pqrstDuration;
    private final float noiseAmplitude;

    public ECGSignalGenerator() {
        spawn = System.currentTimeMillis();
        random = new Random();

        pWaveAmplitude = 0.1f;
        qWaveAmplitude = 0.1f;
        rWaveAmplitude = 0.5f;
        sWaveAmplitude = 0.2f;
        tWaveAmplitude = 0.15f;
        noiseAmplitude = 0.01f;

        beatDuration = 1f / BASELINE_HEART_RATE;
        beatDurationMs = (long) (beatDuration * 1000);

        pWaveDuration = (long) (0.1f * beatDurationMs);
        qWaveDuration = (long) (0.04f * beatDurationMs);
        rWaveDuration = (long) (0.07f * beatDurationMs);
        sWaveDuration = (long) (0.05f * beatDurationMs);
        tWaveDuration = (long) (0.2f * beatDurationMs);

        pqDuration = pWaveDuration + qWaveDuration;
        pqrDuration = pqDuration + rWaveDuration;
        pqrsDuration = pqrDuration + sWaveDuration;
        pqrstDuration = pqrsDuration + tWaveDuration;
    }

    @Override
    public float next() {
        var timeDiff = System.currentTimeMillis() - spawn;

        var result = 0f;
        var tInCycle = timeDiff % beatDurationMs;

        if (tInCycle > 0 && tInCycle <= pWaveDuration)
            result += pWaveAmplitude;

        if (tInCycle > pWaveDuration && tInCycle <= pqDuration)
            result -= qWaveAmplitude;

        if (tInCycle > pqDuration && tInCycle <= pqrDuration)
            result += rWaveAmplitude;

        if (tInCycle > pqrDuration && tInCycle <= pqrsDuration)
            result -= sWaveAmplitude;

        if (tInCycle > pqrsDuration && tInCycle <= pqrstDuration) {
            var angle = (double) (tInCycle - pqrsDuration) / tWaveDuration;
            var curve = Math.sin(Math.PI * angle);
            result += tWaveAmplitude * curve;
        }

        var noise = (random.nextFloat() - 0.5) * 2 * noiseAmplitude;
        result += noise;

        return (float) (result + (0.5f - result) * 0.5);
    }
}
