precision mediump float;

uniform sampler2D u_TextureUnit;//actual texture data
varying vec2 v_TextureCoordinates;

varying vec4 mvPos;
uniform vec4 u_LightPositionVector;

uniform float u_Alpha;

void main(){
	//float diffuse = 1.0;
	//float bb = 0.0;
	
	//for(int i=0; i < 1; i++){
	//	bb =  distance(u_LightPositionVector.xy, mvPos.xy);
	//}
	//vec4 v_amb;
	
	//if(bb < 200.0){
	//	v_amb = vec4( 1.0 * diffuse, 1.0 * diffuse, 1.0 * diffuse, 1.0 * diffuse);
	//}else{
	//	v_amb = vec4(1.0 * (400.0 / 2.0 / bb), 1.0 * (400.0 / 2.0 / bb), 1.0 * (200.0 / bb), 1.0);
	//}
	
	
	//gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates) * v_amb; //tole doda luci
	
	if(u_Alpha < 1.0){
		gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates) * vec4(1.0,1.0,1.0,u_Alpha);
	}else{
		gl_FragColor = texture2D(u_TextureUnit, v_TextureCoordinates);
	}
}