
__kernel void apply_threshold_3d(__read_only    image3d_t  src,
                                 const    float      threshold,
                          __write_only    image3d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);
  const int z = get_global_id(2);

  const int4 pos = (int4){x,y,z,0};

  const float inputValue = READ_IMAGE(src, pos).x;
  DTYPE_OUT value = 1.0;
  if (inputValue < threshold) {
    value = 0.0;
  }

  WRITE_IMAGE (dst, pos, value);
}

__kernel void apply_threshold_2d(__read_only    image2d_t  src,
                                 const    float      threshold,
                          __write_only    image2d_t  dst
                     )
{
  const int x = get_global_id(0);
  const int y = get_global_id(1);

  const int2 pos = (int2){x,y};

  const float inputValue = READ_IMAGE(src, pos).x;
  DTYPE_OUT value = 1.0;
  if (inputValue < threshold) {
    value = 0.0;
  }

  WRITE_IMAGE (dst, pos, value);
}