#version 400 core

//converts per vertex data to per pixel data for screen to display

in vec2 pass_textureCoords; //get textureCoords from vertexShader
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D modelTexture;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(){

    //normalize vectors, so xyz are between -1 and 1
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal, unitLightVector);  //calculate dot product (scalar product) of the two normalized vectors
    float brightness = max(nDot1, 0.1); //convert negative dot products to 0.2, because there is no negative light and normally no completely black spots which would be near 0
    vec3 diffuse = brightness * lightColor;  //add color to light

    vec3 unitVectorToCamera = normalize(toCameraVector);
    vec3 lightDirection = -unitLightVector; //vector to the light is opposite of vector from the light
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal); //calculate vector of reflected light from normal vector of vertex and vector of the incoming light

    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera); //calculate dot product of reflected light and vector to camera to calculate, how much reflected light gets into the camera
    specularFactor = max(specularFactor, 0.0); //convert negative reflection value to 0, because reflection can't be negative
    float dampedFactor = pow(specularFactor, shineDamper); //lower reflectiveness when it is not shining directly into camera
    vec3 finalSpecular = dampedFactor * reflectivity * lightColor; //add color and reflectivity to reflected light

    vec4 textureColor = texture(modelTexture, pass_textureCoords); //calculate per pixel color
    if(textureColor.a < 0.5){ //don't render pixels with alpha < 0.5
        discard;
    }

    out_Color = vec4(diffuse,1.0) * textureColor + vec4(finalSpecular,1.0); //return color of pixel at coordinates
    out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility); //mix skyColor (fog Color( and object color depending on visibility
}
