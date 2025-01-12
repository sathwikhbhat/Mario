package jade;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {

    private String vertextShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos;\n" +
            "layout (location=1) in vec4 aColour;\n" +
            "\n" +
            "out vec4 fColour;\n" +
            "\n" +
            "void main() {\n" +
            "    fColour = aColour;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColour;\n" +
            "\n" +
            "out vec4 colour;\n" +
            "\n" +
            "void main() {\n" +
            "    colour = fColour;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram;

    private float[] vertexArray = {
            //Position                  //Colour
            0.5f, -0.5f, 0.0f,          1.0f, 0.0f, 0.0f, 1.0f, //Bottom Right = 0
            -0.5f, 0.5f, 0.0f,          0.0f, 1.0f, 0.0f, 1.0f, //Top Left = 1
            0.5f, 0.5f, 0.0f,            0.0f, 0.0f, 1.0f, 1.0f, //Top Right = 2
            -0.5f, -0.5f, 0.0f,          1.0f, 1.0f, 0.0f, 1.0f, //Bottom Left = 3

    };

    //Anti - Clockwise Direction
    private int[] elementArray = {
            2, 1, 0, //Top Right Triangle
            0, 1, 3, //Bottom Left Triangle
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene () {

    }

    @Override
    public void init() {
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        glShaderSource(vertexID, vertextShaderSrc);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex Shader Compilation Failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(fragmentID, fragmentShaderSrc);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE) {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment Shader Compilation Failed");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if(success == GL_FALSE) {
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tShader Program Linking Failed");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionSize = 3;
        int colourSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colourSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colourSize, GL_FLOAT, false, vertexSizeBytes, positionSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void update(float dt) {
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoID);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }
}
