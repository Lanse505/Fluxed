package lanse505.fluxed.api.wip;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class FluxNoise
{
  private final int[] permutation;

  // Configurable parameters
  private NoiseConfig config;

  public FluxNoise(RandomSource source, NoiseConfig config)
  {
    permutation = new int[512];
    int[] p = new int[256];
    for (int i = 0; i < 256; i++) p[i] = i;
    for (int i = 0; i < 256; i++) {
      int j = source.nextInt(256);
      int tmp = p[i];
      p[i] = p[j];
      p[j] = tmp;
    }
    System.arraycopy(p, 0, permutation, 0, 256);
    System.arraycopy(p, 0, permutation, 256, 256);
  }

  // Basic noise function (original)
  public float noise(float chunkX, float chunkZ)
  {
    return sampleNoise(chunkX * config.getFrequency() + config.getOffsetX(), chunkZ * config.getFrequency() + config.getOffsetZ()) * config.getAmplitude();
  }

  // Enhanced noise function with all parameters
  public float enhancedNoise(float chunkX, float chunkZ)
  {
    float result = 0.0f;
    float currentAmplitude = config.getAmplitude();
    float currentFrequency = config.getFrequency();
    float maxValue = 0.0f; // Used for normalizing result to [-1, 1]

    for (int i = 0; i < config.getOctaves(); i++) {
      result += sampleNoise((chunkX + config.getOffsetX()) * currentFrequency,
              (chunkZ + config.getOffsetZ()) * currentFrequency) * currentAmplitude;

      maxValue += currentAmplitude;
      currentAmplitude *= config.getPersistence();
      currentFrequency *= config.getLacunarity();
    }

    return (result / maxValue) * config.getScale();
  }

  // Ridged noise variant (inverts and sharpens the noise)
  public float ridgedNoise(float chunkX, float chunkZ)
  {
    return (1.0f - Math.abs(enhancedNoise(chunkX, chunkZ))) * config.getScale();
  }

  // Turbulence noise (uses absolute values for more chaotic patterns)
  public float turbulenceNoise(float chunkX, float chunkZ)
  {
    float result = 0.0f;
    float currentAmplitude = config.getAmplitude();
    float currentFrequency = config.getFrequency();
    float maxValue = 0.0f;

    for (int i = 0; i < config.getOctaves(); i++) {
      result += Math.abs(sampleNoise((chunkX + config.getOffsetX()) * currentFrequency,
              (chunkZ + config.getOffsetZ()) * currentFrequency)) * currentAmplitude;

      maxValue += currentAmplitude;
      currentAmplitude *= config.getPersistence();
      currentFrequency *= config.getLacunarity();
    }

    return (result / maxValue) * config.getScale();
  }

  // Core noise sampling function
  private float sampleNoise(float x, float z)
  {
    int intX = (int) Math.floor(x) & 255;
    int intZ = (int) Math.floor(z) & 255;
    float fracX = x - (float) Math.floor(x);
    float fracZ = z - (float) Math.floor(z);
    float u = fade(fracX);
    float v = fade(fracZ);
    int a = permutation[intX] + intZ;
    int b = permutation[intX + 1] + intZ;

    float gradAA = grad(permutation[a], fracX, fracZ);
    float gradBA = grad(permutation[b], fracX - 1, fracZ);
    float gradAB = grad(permutation[a + 1], fracX, fracZ - 1);
    float gradBB = grad(permutation[b + 1], fracX - 1, fracZ - 1);

    float lerpX1 = Mth.lerp(u, gradAA, gradBA);
    float lerpX2 = Mth.lerp(u, gradAB, gradBB);
    return Mth.lerp(v, lerpX1, lerpX2);
  }

  private float grad(int hash, float x, float z)
  {
    int h = hash & 15;
    float u = (h < 8) ? x : z;
    float v = (h < 4) ? z : ((h == 12 || h == 14) ? x : 0);
    return (((h & 1) == 0) ? u : -u) + (((h & 2) == 0) ? v : -v);
  }

  private float fade(float t)
  {
    return t * t * t * (t * (t * 6 - 15) + 10);
  }
}
