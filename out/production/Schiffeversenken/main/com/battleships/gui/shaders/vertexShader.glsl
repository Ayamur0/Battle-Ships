#version 400 core

//makes per vertex data

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;
out float visibility; //how foggy a object is, 0 for invisible (only fog) 1 for no fog

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

uniform float useFakeLighting;

uniform float numberOfRows;
uniform vec2 offset;

const float density = 0.0015; //density of fog (how far objects need to be from camera before they get foggy)
const float gradient = 3.0; //how fast objects get foggy once they are in fog range

uniform vec4 plane;

void main(){

    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);  //convert vec3 position into vec4 and calculate new position with transfMatrix

    gl_ClipDistance[0] = dot(worldPosition, plane);

    vec4 positionRelativeToCam = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCam;  //convert position of model in 3D space to a displayable form on a 2D screen
    pass_textureCoords = (textureCoords/numberOfRows) + offset; //pass textureCoords to fragment shader after calculating right spot if texture atlas is used

    vec3 actualNormal = normal; //if fake lighting is used make all normals point upwards
    if(useFakeLighting > 0.5){
        actualNormal = vec3(0.0,1.0,0.0);
    }

    surfaceNormal = (transformationMatrix * vec4(actualNormal,0.0)).xyz; //recalculate normal vector in case model has been moved with transformationMatrix
    toLightVector = lightPosition - worldPosition.xyz; //calculate a vector pointing from each vertex to the light source
    toCameraVector = (inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz; //calculate camera position (negative from viewMatrix) and then calculate vector from vertex to camera

    float distance = length(positionRelativeToCam.xyz);
    visibility = exp(-pow((distance * density), gradient)); //realistic function for increasing fog the further away an object is
    visibility = clamp(visibility, 0.0, 1.0); //make all values between 0 and 1
}