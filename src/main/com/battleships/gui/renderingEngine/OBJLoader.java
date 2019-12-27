package com.battleships.gui.renderingEngine;


import com.battleships.gui.models.RawModel;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Loader that can convert a OBJFile into a {@link RawModel}.
 * 
 * @author Tim Staudenmaier
 */
public class OBJLoader {

    /**
     * Loads all the data contained in the .obj file into a {@link RawModel}.
     * @param fileName Path to the OBJFile.
     * @return A RawModel containing all the data from the .obj.
     */
    public static RawModel loadObjModel(String fileName){

        InputStream is = OBJLoader.class.getResourceAsStream("/com/battleships/gui/res/models/"+ fileName +".obj");  //read file with file reader
        if (is == null)
            throw new RuntimeException("Resource not found: " + fileName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(is)); //Create BufferedReader to be able to read line by line

        String line;
        List<Vector3f> vertices = new ArrayList<>(); //List to save vertices position coordinates (x,y,z)
        List<Vector2f> textures = new ArrayList<>(); //List to save TextureCoordinates
        List<Vector3f> normals = new ArrayList<>(); //List to save normal vectors of faces
        List<Integer> indices = new ArrayList<>(); //List to save indices of which vertices are connected

        //arrays to later sort vertices, normals, textures and indices
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray;

        try {
            int i = 0;
            while (true){
                i++;
                line = reader.readLine();  //read one line
                String[] currentLine = line.split(" "); //split read line at whitespaces
                if(line.startsWith("v ")){ //check if line are vertices
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])); //convert line to vector
                    vertices.add(vertex); //add vector to vertices list
                }else if(line.startsWith("vt ")){  //check if line are textureCoordinates
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                }else if(line.startsWith("vn ")){  //check if line are normalVectors
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])); //convert line to normalvector
                    normals.add(normal); //add vector to normals list
                }else if(line.startsWith("f")){   //check if line is material --> following lines now show which vertex,texture and normal data go together, so all data has been read
                    textureArray = new float[vertices.size()*2];
                    normalsArray = new float[vertices.size()*3];
                    break;
                }
            }
            verticesArray = new float[vertices.size() * 3];
            int vertexPointer = 0;
            //convert vertexCoordinateVector from verticesList to verticesArray
            for(Vector3f vertex : vertices){
                verticesArray[vertexPointer++] = vertex.x;
                verticesArray[vertexPointer++] = vertex.y;
                verticesArray[vertexPointer++] = vertex.z;
            }
            while(line != null){
                if(!line.startsWith("f ")){ //check if line really starts with f else read next
                    line = reader.readLine();
                    continue;
                }

                String[] currentLine = line.split(" "); //split line at white spaces

                if(line.startsWith("f ")) {
                    //split vertex indices from all 3 vertex, texture, normal combinations
                    //obj format: 77/5/2 (vertex index/texture index/normal index) with index pointing to the line where the data for that vertex/texture/normal is
                    String[] vertex1 = currentLine[1].split("/");
                    String[] vertex2 = currentLine[2].split("/");
                    String[] vertex3 = currentLine[3].split("/");

                    processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                    processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                    processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
                    line = reader.readLine();
                }
            }
            reader.close();

        }catch (Exception e){
            e.printStackTrace();
        }

        //convert indices from List to indicesArray;
        indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }

        //return new RawModel
        Loader loader = new Loader();
        return loader.loadToVAO(verticesArray, textureArray, normalsArray, indicesArray);
    }

    /**
     * Combine the right textureCoords, index and normals for a vertex because they are not stored in the same line in an .obj file.
     * @param vertexData String of the line in the .obj containing the index of the vertex and the indices for the texture and normals data of this vertex.
     * @param indices List the index of this vertex should be added to.
     * @param textures List of all read textureCoords from the .obj File
     *                sorted by the order in which they were read (first line at index 0, ...).
     * @param normals List of all read normals from the .obj File
     *               sorted by the order in which they were read (first line at index 0, ...).
     * @param textureArray Array the texture coords for this array should be added to
     * @param normalsArray Array the normals for this vertex should be added to
     *                     both arrays contain the data for all vertices in the same order (ordered by indices)
     */
    //sort vertex, texture and normals in array in right order by giving indices to the element in the lists
    private static void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray){
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1; //get index for current vertex (-1 because obj lines start with 1 instead of 0)
        indices.add(currentVertexPointer); //add vertex index to indices list, now in right order in which vertex should be connected

        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1); //get index for current texture and get corresponding texture
        textureArray[currentVertexPointer*2] = currentTex.x; //put texture in array, now sorted from first to last texture
        textureArray[currentVertexPointer*2+1] = 1 - currentTex.y; //1- because blender starts at bottom left with textures, obj at top left (flip texture)

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1); //get index for current normal and get corresponding normal
        //add normals to normalsArray now in right order
        normalsArray[currentVertexPointer*3] = currentNorm.x;
        normalsArray[currentVertexPointer*3+1] = currentNorm.y;
        normalsArray[currentVertexPointer*3+2] = currentNorm.z;
    }

}
