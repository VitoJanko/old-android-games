//here collor of each fragment (pixel) is calculated
//this is being called for each fragment, fragments are
// calculated automatically from points, lines and triangles

precision mediump float; //defines default precission for this shader (programm)

//uniforms are the same for all runs through shader, they don't change (vor every vertex/pixel)
uniform vec4 u_Color;//color passed in, uniform keeps the same value for all vertices until we change it again
					//r, g, b, alpha
void main(){
	gl_FragColor = u_Color;//color mapped to fragment
}