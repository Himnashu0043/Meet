#pragma version(1)
#pragma rs_fp_relaxed

rs_allocation inAlloc;
rs_allocation outAlloc;
int width;
int height;
float radius;

static const float3 weights = {0.299f, 0.587f, 0.114f};

void root(const uchar4 *in, uchar4 *out, uint32_t x, uint32_t y) {
    float3 sum = {0.0f, 0.0f, 0.0f};

    for (int i = -radius; i <= radius; i++) {
        for (int j = -radius; j <= radius; j++) {
            int currentX = clamp(x + i, 0, width - 1);
            int currentY = clamp(y + j, 0, height - 1);

            uchar4 color = rsGetElementAt_uchar4(inAlloc, currentX, currentY);
            float3 gray = dot(convert_float4(color), weights);
            float weight = 1.0f - length(float2(i, j)) / radius;
            sum += gray * weight;
        }
    }

    *out = convert_uchar4(sum);
}
