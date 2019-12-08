#version 330

in vec2 pass_textureCoords;

out vec4 out_color;

uniform vec3 color;
uniform sampler2D fontAtlas;

const float width = 0.5f;
const float edge = 0.1;

uniform float borderWidth;
uniform float borderEdge;

uniform vec2 offset;

uniform vec3 outlineColor;

void main(void){

    float distance = 1.0 - texture(fontAtlas, pass_textureCoords + offset).a; //get inverted alpha value from distance font chracters
    float alpha = 1.0 - smoothstep(width, width + edge, distance); //calculate alpha for character, with smooth transition at edge of character to prevent sharp pixelated edges

    float distance2 = 1.0 - texture(fontAtlas, pass_textureCoords).a;
    float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);

    float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
    vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha);

    out_color = vec4(overallColor, overallAlpha);

}
