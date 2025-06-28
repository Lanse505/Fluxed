package lanse505.fluxed.api.wip;

public class NoiseConfig
{

  private final float frequency;      // Base frequency
  private final float amplitude;      // Base amplitude
  private final int octaves;          // Number of octaves for fractal noise
  private final float persistence;    // Amplitude reduction per octave
  private final float lacunarity;     // Frequency increase per octave
  private final float scale;          // Overall scale multiplier
  private final float offsetX;        // X offset
  private final float offsetZ;        // Z offset

  private NoiseConfig(float frequency, float amplitude, int octaves, float persistence, float lacunarity, float scale, float offsetX, float offsetZ)
  {
    this.frequency = frequency;
    this.amplitude = amplitude;
    this.octaves = octaves;
    this.persistence = persistence;
    this.lacunarity = lacunarity;
    this.scale = scale;
    this.offsetX = offsetX;
    this.offsetZ = offsetZ;
  }

  public float getFrequency()
  {
    return frequency;
  }

  public float getAmplitude()
  {
    return amplitude;
  }

  public int getOctaves()
  {
    return octaves;
  }

  public float getPersistence()
  {
    return persistence;
  }

  public float getLacunarity()
  {
    return lacunarity;
  }

  public float getScale()
  {
    return scale;
  }

  public float getOffsetX()
  {
    return offsetX;
  }

  public float getOffsetZ()
  {
    return offsetZ;
  }

  public static Builder builder()
  {
    return new Builder();
  }

  public static class Builder
  {
    private float frequency;      // Base frequency
    private float amplitude;      // Base amplitude
    private int octaves;          // Number of octaves for fractal noise
    private float persistence;    // Amplitude reduction per octave
    private float lacunarity;     // Frequency increase per octave
    private float scale;          // Overall scale multiplier
    private float offsetX;        // X offset
    private float offsetZ;        // Z offset

    public Builder() { }

    public Builder frequency(float frequency)
    {
      this.frequency = frequency;
      return this;
    }

    public Builder amplitude(float amplitude)
    {
      this.amplitude = amplitude;
      return this;
    }

    public Builder octaves(int octaves)
    {
      this.octaves = octaves;
      return this;
    }

    public Builder persistence(float persistence)
    {
      this.persistence = persistence;
      return this;
    }

    public Builder lacunarity(float lacunarity)
    {
      this.lacunarity = lacunarity;
      return this;
    }

    public Builder scale(float scale)
    {
      this.scale = scale;
      return this;
    }

    public Builder offset(float offsetX, float offsetZ)
    {
      this.offsetX = offsetX;
      this.offsetZ = offsetZ;
      return this;
    }

    public NoiseConfig build()
    {
      return new NoiseConfig(frequency, amplitude, octaves, persistence, lacunarity, scale, offsetX, offsetZ);
    }
  }
}
