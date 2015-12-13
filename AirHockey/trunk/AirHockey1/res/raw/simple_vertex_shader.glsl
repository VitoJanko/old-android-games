//This shader is called once for every sigle vertex

attribute vec4 a_Position; //shader receives position x,y,z,w
							//if not specified default value is 0,0,0,1
							

void main(){
	gl_Position = a_Position;//gl_Position is final position of vertex
							//here we just copy position to it
							
	gl_PointSize = 10.0; //specifies size of single point
}