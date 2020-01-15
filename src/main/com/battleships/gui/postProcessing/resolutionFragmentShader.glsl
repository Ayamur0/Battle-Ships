#version 140

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D colorTexture;

const float contrast = 0.2;

void main(void){

    out_Color = texture(colorTexture, textureCoords);


}
