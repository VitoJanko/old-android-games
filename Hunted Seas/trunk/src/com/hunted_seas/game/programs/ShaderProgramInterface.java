package com.hunted_seas.game.programs;

/**
 * Interface for shader programs.
 * 
 * @author Jani Bizjak <janibizjak@gmail.com>
 *
 */
public interface ShaderProgramInterface {

	public void useProgram();
	
	public int getMatrixUniformLocation();
	
	public int getPositionAttributeLocation();
}
