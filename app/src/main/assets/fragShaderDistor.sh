#extension GL_OES_EGL_image_external : require
precision mediump float;
varying vec2 vTextureCoord;
uniform samplerExternalOES sTexture;

 void main(){

    highp float x = vTextureCoord.x ;
    highp float y = vTextureCoord.y ;

    highp float d_cu = (x - (0.5)) ; //计算特定像素点距离球形的中心有多远
    highp float d_cv = ( y - (0.5)) ; //计算特定像素点距离球形的中心有多远

    highp float  dcu = (d_cu)*(d_cu);
    highp float  dcv = (d_cv)*(d_cv);

 // 1280/720 = 0.90 / ?

     highp float  k_r_x = 0.50;
     highp float  k_g_x = 0.50;
     highp float  k_b_x = 0.50;

     highp float  k_r_y = 0.90;//0.50;
     highp float  k_g_y = 0.90;//0.50;
     highp float  k_b_y = 0.90;//0.50;

    highp float  dist = (0.75)*(dcu)+ (0.75)*(dcv);
 highp float  dist_R = (0.78)*(dcu)+ (0.78)*(dcv);
  highp float  x_d_R =  x+  d_cu * k_r_x * dist_R;
     highp float  y_d_R =  y+  d_cv * k_r_y * dist_R;

    highp float   x_d =  x+  d_cu * dist;
    highp float   y_d =  y+  d_cv * dist;
  highp vec2 textureCoordinateToUse_R = vec2(  x_d_R , y_d_R); //归一化坐标空间需要考虑屏幕是一个单位宽和一个单位长.

 highp float   dist_G = (0.80)*(dcu)+ (0.80)*(dcv);

     highp float   x_d_G =  x+  d_cu * k_g_x * dist_G;
     highp float   y_d_G =  y+  d_cv * k_g_y * dist_G;

  highp vec2 textureCoordinateToUse_G = vec2(  x_d_G , y_d_G); //归一化坐标空间需要考虑屏幕是一个单位宽和一个单位长。

     ////////////////////////////////////////////
     highp float   dist_B = (0.82)*(dcu)+ (0.82)*(dcv);

     highp float   x_d_B  =  x+  d_cu * k_b_x * dist_B;
     highp float   y_d_B  =  y+  d_cv * k_b_y * dist_B;

     highp vec2 textureCoordinateToUse_B = vec2(  x_d_B , y_d_B); //归一化坐标空间需要考虑屏幕是一个单位宽和一个单位长。
     ////////////////////////////////////////////
     //lowp vec4 textureColor = texture2D(sTexture, textureCoordinateToUse_R);
     lowp vec4 textureColor_R = texture2D(sTexture, textureCoordinateToUse_R);
     lowp vec4 textureColor_G = texture2D(sTexture, textureCoordinateToUse_G);
     lowp vec4 textureColor_B = texture2D(sTexture, textureCoordinateToUse_B);

     highp vec4 textureColor = vec4( textureColor_R.x ,  textureColor_G.y  ,   textureColor_B.z , textureColor_B.w);

     if(x_d_B>1.0 || y_d_B>1.0 || x_d_B< (1e-9)|| y_d_B< (1e-9))
         gl_FragColor  =   vec4( textureColor.rgb - textureColor.rgb  + vec3(0,0,0), textureColor.w); //最后凑齐所有计算需要颜
     else
         gl_FragColor  =   vec4( textureColor.rgb, textureColor.w); //texture2D(sTexture, textureCoordinateToUse); //最后凑齐所有计算需要的颜色信息。
 }