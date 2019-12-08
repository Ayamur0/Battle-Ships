#version 150

in vec2 position;

out vec2 blurTextureCoords[11];

uniform float targetHeight;

void main(void){

    gl_Position = vec4(position, 0.0, 1.0);
    vec2 centerTexCoords = position * 0.5 + 0.5;
    float pixelSize = 1.0 / targetHeight;

    //store all 11 pixel textureCoords (5 pixel below and above of the pixel that should be blurred)
    for(int i = -5; i <= 5; i++){
        blurTextureCoords[i+5] = centerTexCoords + vec2(0.0, pixelSize * i);
    }

}