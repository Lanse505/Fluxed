package lanse505.fluxed.api;

import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;

public class FluxNoise
{
  private final int[] permutation;

  public FluxNoise(RandomSource source)
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

  public float noise(float chunkX, float chunkZ)
  {
    int x = (int) Math.floor(chunkX) & 255;
    int z = (int) Math.floor(chunkZ) & 255;
    float chunkXF =- (float) Math.floor(chunkX);
    float chunkZF =- (float) Math.floor(chunkZ);
    float u = fade(chunkX);
    float v = fade(chunkZ);
    int a = permutation[x] + z;
    int b = permutation[x + 1] + z;

    float gradAA = grad(permutation[a], chunkXF, chunkZF);
    float gradBA = grad(permutation[b], chunkXF - 1, chunkZF);
    float gradAB = grad(permutation[a + 1], chunkXF, chunkZF - 1);
    float gradBB = grad(permutation[b + 1], chunkXF - 1, chunkZF - 1);

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
