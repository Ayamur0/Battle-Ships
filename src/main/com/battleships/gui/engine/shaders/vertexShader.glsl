#version 150

//makes per vertex data

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

uniform float useFakeLighting;

void main(){

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);  //convert vec3 position into vec4 and calculate new position with transfMatrix
    gl_Position = projectionMatrix * viewMatrix * worldPosition;  //convert position of model in 3D space to a displayable form on a 2D screen
    pass_textureCoords = textureCoords; //pass textureCoords to fragment shader

    vec3 actualNormal = normal; //if fake lighting is used make all normals point upwards
    if(useFakeLighting > 0.5){
        actualNormal = vec3(0.0,1.0,0.0);
    }

    surfaceNormal = (transformationMatrix * vec4(actualNormal,0.0)).xyz; //recalculate normal vector in case model has been moved with transformationMatrix
    toLightVector = lightPosition - worldPosition.xyz; //calculate a vector pointing from each vertex to the light source
    toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz; //calculate camera position (negative from viewMatrix) and then calculate vector from vertex to camera

}