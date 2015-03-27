package engine.space;
import java.nio.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

/**
 *
 * @author Kassandra Kaeck
 */
public class Matrix {
    private double[][] array;
    public Matrix (Quaternion rot, Vertex translation) {
        array = new double[4][3];
        array[0][0] = 1 - 2.0 * rot.y * rot.y - 2.0 * rot.z * rot.z;
        array[1][0] = 2.0 * rot.x * rot.y - 2.0 * rot.w * rot.z;
        array[2][0] = 2.0 * rot.x * rot.z + 2.0 * rot.w * rot.y;
        array[3][0] = translation.x;
	array[0][1] = 2.0 * rot.x * rot.y + 2.0 * rot.w * rot.z;
	array[1][1] = 1.0 - 2.0 * rot.x * rot.x - 2.0 * rot.z * rot.z;
	array[2][1] = 2.0 * rot.y * rot.z + 2.0 * rot.w * rot.x;
	array[3][1] = translation.y;
        array[0][2] = 2.0 * rot.x * rot.z - 2.0 * rot.w * rot.y;
	array[1][2] = 2.0 * rot.y * rot.z - 2.0 * rot.w * rot.x;
	array[2][2] = 1.0 - 2.0 * rot.x * rot.x - 2.0 * rot.y * rot.y;
	array[3][2] = translation.z;
    }
    public Matrix (Quaternion rot) {
        array = new double[4][3];
        array[0][0] = 1 - 2.0 * rot.y * rot.y - 2.0 * rot.z * rot.z;
        array[1][0] = 2.0 * rot.x * rot.y - 2.0 * rot.w * rot.z;
        array[2][0] = 2.0 * rot.x * rot.z + 2.0 * rot.w * rot.y;
        array[3][0] = 0.0;
	array[0][1] = 2.0 * rot.x * rot.y + 2.0 * rot.w * rot.z;
	array[1][1] = 1.0 - 2.0 * rot.x * rot.x - 2.0 * rot.z * rot.z;
	array[2][1] = 2.0 * rot.y * rot.z + 2.0 * rot.w * rot.x;
	array[3][1] = 0.0;
        array[0][2] = 2.0 * rot.x * rot.z - 2.0 * rot.w * rot.y;
	array[1][2] = 2.0 * rot.y * rot.z - 2.0 * rot.w * rot.x;
	array[2][2] = 1.0 - 2.0 * rot.x * rot.x - 2.0 * rot.y * rot.y;
	array[3][2] = 0.0;
    }
    public Matrix (Vertex translation) {
        array = new double[4][3];
        int i, j;
        for (i = 0;i < 3;i++) {
            for (j = 0;j < 3;j++) {
                if (i == j) {
                    array[i][j] = 1.0;
                }
                else {
                    array[i][j] = 0.0;
                }
            }
        }
        array[3][0] = translation.x;
	array[3][1] = translation.y;
	array[3][2] = translation.z;
    }
    public Matrix () {
        array = new double[4][3];
        int i, j;
        for (i = 0;i < 4;i++) {
            for (j = 0;j < 3;j++) {
                if (i == j) {
                    array[i][j] = 1.0;
                }
                else {
                    array[i][j] = 0.0;
                }
            }
        }
    }
    private Matrix (boolean init) {
        array = new double[4][3];
        if (init) {
            int i, j;
            for (i = 0;i < 4;i++) {
                for (j = 0;j < 3;j++) {
                    array[i][j] = 0.0;
                }
            }
        }
    }
    public Vertex transform (Vertex pos) {
        Vertex ret = new Vertex ();
        double[] temp = new double[] { pos.x, pos.y, pos.z, 1.0 };
        int i;
        for (i = 0;i < 4;i++) {
            ret.x += array[i][0] * temp[i];
            ret.y += array[i][1] * temp[i];
            ret.z += array[i][2] * temp[i];
        }
        return ret;
    }
    public void transform () {
        DoubleBuffer buf = BufferUtils.createDoubleBuffer (16);
        int i, j;
        for (i = 0;i < 4;i++) {
            for (j = 0;j < 3;j++) {
                buf.put (array[i][j]);
            }
            if (i < 3) {
                buf.put (0.0);
            }
        }
        buf.put (1.0);
        buf.flip ();
        GL11.glMultMatrix (buf);
    }
    public Matrix translate (Vertex move) {
        return transform (new Matrix (move));
    }
    public Matrix rotate (Quaternion rotation) {
        return transform (new Matrix (rotation));
    }
    public Matrix transform (Matrix other) {
        return multiply (other, this);
    }
    public static Matrix multiply (Matrix one, Matrix two) {
        Matrix ret = new Matrix (false);
        int i, j, k;
        for (j = 0;j < 3;j++) {
            for (i = 0;i < 4;i++) {
                ret.array[i][j] = 0.0;
                for (k = 0;k < 3;k++) {
                    ret.array[i][j] += one.array[k][j] * two.array[i][k];
                }
            }
            ret.array[3][j] += one.array[3][j];
        }
        return ret;
    }
    public static final Matrix identity = new Matrix ();
}
