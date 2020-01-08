#version 140

in vec2 position;

out vec2 pass_textureCoords;

uniform int numberOfRows;
uniform vec2 offset;

uniform mat4 transformationMatrix;

void main() {

    gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);  //set z position to 0
    //convert positions to texture positions
    //positions start from (-1,1) top left to (1, -1) bottom right
    //textureCoords start from (0,0) top left to (1,1) bottom right
    vec2 textureCoords = vec2((position.x + 1.0) / 2.0, 1 - (position.y + 1.0) / 2.0);
    pass_textureCoords = (textureCoords/numberOfRows) + offset;
}
