#version 150

//converts per vertex data to per pixel data for screen to display

in vec2 pass_textureCoords; //get textureCoords from vertexShader
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D waterTexture;
uniform sampler2D pathTexture;
uniform sampler2D gravelTexture;
uniform sampler2D grassTexture;
uniform sampler2D wetSandTexture;
uniform sampler2D sandTexture;
uniform sampler2D blendMap;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
//shader multiply with 40 to get texture Coords outside terrain texture, so textured gets tiled over terrain instad of stretched
void main(){

    vec4 blendMapColor = texture(blendMap, pass_textureCoords); //get color for pixel on blendMap

    //calculate how visible each texture should be for the pixel
    float waterTextureAmount = 0;

    float pathTextureAmount = 0;

    float gravelTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);

    float grassTextureAmount = blendMapColor.g;

    float wetSandTextureAmount = blendMapColor.b;

    float sandTextureAmount = blendMapColor.r;

//    if(blendMapColor.g * 255 + blendMapColor.b * 255 > 450 && blendMapColor.g * 255 + blendMapColor.b * 255 < 600){
    if(blendMapColor.g == 1 && blendMapColor.b == 1){
        waterTextureAmount = 1;
        pathTextureAmount = 0;
        gravelTextureAmount = 0;
        grassTextureAmount = 0;
        wetSandTextureAmount = 0;
        sandTextureAmount = 0;
    }
    if(blendMapColor.g * 255 + blendMapColor.r * 255 + blendMapColor.b * 255 > 700){
        waterTextureAmount = 0;
        pathTextureAmount = 1;
        gravelTextureAmount = 0;
        grassTextureAmount = 0;
        wetSandTextureAmount = 0;
        sandTextureAmount = 0;
    }

    vec2 tiledCoords = pass_textureCoords * 40; //multiply with 40 to get texture Coords outside terrain texture, so textured gets tiled over terrain instad of stretched
    //calculate color for each texture depending on texture visibility
    vec4 waterTextureColor = texture(waterTexture, tiledCoords) * waterTextureAmount;
    vec4 pathTextureColor = texture(pathTexture, tiledCoords) * pathTextureAmount;
    vec4 gravelTextureColor = texture(gravelTexture, tiledCoords) * gravelTextureAmount;
    vec4 grassTextureColor = texture(grassTexture, tiledCoords) * grassTextureAmount;
    vec4 wetSandTextureColor = texture(wetSandTexture, tiledCoords) * wetSandTextureAmount;
    vec4 sandTextureColor = texture(sandTexture, tiledCoords) * sandTextureAmount;

    //calculate final color of pixel
    vec4 totalColor = waterTextureColor + pathTextureColor + gravelTextureColor + grassTextureColor + wetSandTextureColor + sandTextureColor;

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

    out_Color = vec4(diffuse,1.0) * totalColor + vec4(finalSpecular,1.0); //return color of pixel at coordinates
    out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility); //mix skyColor (fog Color( and object color depending on visibility
}
