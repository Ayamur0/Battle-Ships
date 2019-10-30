#version 150

in vec3 outPositions;
in vec2 outTextureCoordinates;

out vec4 outputColor;

uniform sampler2D diffuseTexture;
uniform vec3 lightColor;

void main(){

    outputColor = texture(diffuseTexture, outTextureCoordinates);
}