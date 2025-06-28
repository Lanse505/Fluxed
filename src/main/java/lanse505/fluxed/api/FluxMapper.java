package lanse505.fluxed.api;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import org.joml.Vector2d;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


/**
 * FluxMapper generates Perlin-like noise values for a 2D space using a deterministic RandomSource.
 * Results are cached to avoid computation and improve performance.
 */
public class FluxMapper
{
  private final FluxNoise noise;
  private final float frequency;
  private final Cache<ChunkPos, Float> noiseCache;

  public FluxMapper(RandomSource source, float frequency)
  {
    this.noise = new FluxNoise(source);
    this.frequency = frequency;
    this.noiseCache = CacheBuilder.newBuilder().maximumSize(50_000).expireAfterAccess(30, TimeUnit.MINUTES).build();
  }

  public Float getNoise(ChunkPos pos, float frequency)
  {
    if (noiseCache.getIfPresent(pos) != null)
    {
      return noiseCache.getIfPresent(pos);
    }
    float val = noise.noise(pos.x, pos.z);
    noiseCache.put(pos, val);
    return val;
  }

}
