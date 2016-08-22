package com.giou.opengltest;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyGLSurfaceView view = new MyGLSurfaceView(this);
        MyRenderer renderer = new MyRenderer();
        view.setRenderer(renderer);
        setContentView(view);
    }

    /**
     * 这个只是一个载体
     */
    class MyGLSurfaceView extends GLSurfaceView{

        public MyGLSurfaceView(Context context) {
            super(context);
        }

        public MyGLSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
    }


    /**
     * 自定义渲染器,渲染器是最重要的
     */
    class MyRenderer implements GLSurfaceView.Renderer{

        //表层创建时
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            gl.glClearColor(0,0,0,1);//设置清屏色  1是透明度
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//启用顶点缓冲区
        }

        //表层size改变
        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0,0,width,height);//视图口,输出的画面区域,从左下角00开始
            float ratio = (float) width / (float) height;//比例

            gl.glMatrixMode(GL10.GL_PROJECTION);//矩阵模式
            gl.glLoadIdentity();//加载单位矩阵
            gl.glFrustumf(-1,1f,-ratio,ratio,3,7);//平截头体,视景体,离近平面和远平面的距离

        }

        //绘制图形
        @Override
        public void onDrawFrame(GL10 gl) {

            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);//清楚颜色缓存区

            gl.glMatrixMode(GL10.GL_MODELVIEW);//模型视图矩阵
            gl.glLoadIdentity();//加载单位矩阵

            //eyex eyey eyez 放置眼球坐标
            //centerx centery centerz 眼球观察点
            //upx upy upz 指定眼球向上的向量
            //x 左右 y 上下 z 前后
            GLU.gluLookAt(gl,0,0,5,0,0,0,0,1,0);
            //画三角形
            //绘制数组
            //近平面 左-1  右1
            //三角形坐标
            float[] coords = {
                    0f,0.5f,0f,
                    -0.5f,-0.5f,0f,
                    0.5f,-0.5f,0f
            };
            //一个字典一个字节,float是4个字节
            //分配字节缓存空间,存放顶点坐标数据
            ByteBuffer buffer = ByteBuffer.allocateDirect(coords.length * 4);
            buffer.order(ByteOrder.nativeOrder());//设置本地顺序
            FloatBuffer floatBuffer = buffer.asFloatBuffer();
            floatBuffer.put(coords);//放置顶点坐标
            buffer.position(0);//定位指针的位置,从该位置开始读取顶点数据
            gl.glColor4f(1f,0f,0f,1f);//设置颜色
            //顶点指针;3表示三维的点,3个坐标值,什么数据表现,跨度,指定顶点缓冲区
            gl.glVertexPointer(3,GL10.GL_FLOAT,0,buffer);
            gl.glDrawArrays(GL10.GL_TRIANGLES,0,3);//以点来说是画3个点
        }
    }

}
