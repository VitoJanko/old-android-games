package components;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JTextArea;

public class ObjectOptimizer {
	private JTextArea log;
    static private String newline = "\n";
	
	public void optimize(File file, JTextArea log){
		log.append("Optimizing file: " + file.getName() + "." + "Optimization started." + newline);
		
		try {
			Scanner fileReader = new Scanner(file);
			
			LinkedList<Float> vertexCoordinates = new LinkedList<Float>();
			LinkedList<Float> textureCoordinates = new LinkedList<Float>();
			LinkedList<String> indexArray = new LinkedList<String>();
			
			LinkedList<InfoObject>  newVertices = new LinkedList<InfoObject>();
			HashMap<String, InfoObject> newMap = new HashMap<String, InfoObject>();
			
			int indexCounter = 0;
			
			while(fileReader.hasNextLine()){
				String line = fileReader.nextLine();
				
				if(line.startsWith("f")){//vertex order
					String[] groups = line.split(" ");
					for(int i = 1; i < 4; i++){
						int vindex = ((Integer.valueOf(groups[i].split("/")[0])-1));
						int tindex = ((Integer.valueOf(groups[i].split("/")[1])-1));
						int nindex = ((Integer.valueOf(groups[i].split("/")[2])-1));
						
						String key = vindex+""+tindex;
						
						indexArray.add(key);
						
						InfoObject mObj = new InfoObject();
						int[] tmp = {vindex, tindex, indexCounter};
						
						if(newMap.containsKey(key)){
							mObj = newMap.get(key);
						}else{
							
							mObj.key = key;
							mObj.vertexIndex = vindex;
							mObj.textureIndex = tindex;
							mObj.normalIndex = nindex;
							mObj.newPosition = indexCounter;
							
							newMap.put(key, mObj);
							newVertices.add(mObj);
							
							indexCounter++;
						}
						
					}

				}else if(line.startsWith("vt")){//texture coords
					String[] groups = line.split(" ");
					textureCoordinates.add(Float.valueOf(groups[1]));
					textureCoordinates.add(Float.valueOf(groups[2]));
				}else if(line.startsWith("v") && !line.startsWith("vn")){//vertex coords
					String[] groups = line.split(" ");
					vertexCoordinates.add(Float.valueOf(groups[1]));
					vertexCoordinates.add(Float.valueOf(groups[2]));
					vertexCoordinates.add(Float.valueOf(groups[3])); //z is always zero in .ojb file
				}
				
			}
			
			
			
			fileReader.close();
			
			log.append("Optimizing file: " + file.getName() + "." + "Building finished." + newline);
			
			LinkedList<Float> newVertexCoordinates = new LinkedList<Float>();
			LinkedList<Float> newTextureCoordinates = new LinkedList<Float>();
			LinkedList<Integer> newIndexArray = new LinkedList<Integer>();
			
			int ic = 0;
			
		    for(InfoObject obj : newVertices){
		    	newVertexCoordinates.add(vertexCoordinates.get(obj.vertexIndex*3));
		    	newVertexCoordinates.add(vertexCoordinates.get(obj.vertexIndex*3+1));
		    	newVertexCoordinates.add(vertexCoordinates.get(obj.vertexIndex*3+2));
		    	
		    	newTextureCoordinates.add(textureCoordinates.get(obj.textureIndex*2));
		    	newTextureCoordinates.add(textureCoordinates.get(obj.textureIndex*2+1));
		    	
		    	obj.newPosition = ic;
		    	
		    	ic++;
		    }
		    
		    for(String key : indexArray){
		    	newIndexArray.add(newMap.get(key).newPosition);
		    }
		    
			String path = file.getParent();
			String newName = "new_"+file.getName();
			File newFile = new File(path+"/"+newName);
			
			log.append("Optimizing file: " + file.getName() + "." + "Rearanging finished." + newline);
			
			log.append("Optimizing file: " + file.getName() + "." + "Writing to file: "+newName+" " + newline);
			
		    PrintWriter writer = new PrintWriter(newFile, "UTF-8");
		    
		    int i = 0;
		    for(Float nv : newVertexCoordinates){
		    	i++;
		    	
		    	if(i == 1){
		    		writer.print("v "+nv+" ");
		    	}else if(i == 3){
		    		writer.print(nv+" \n");
		    		i = 0;
		    	}else{
		    		writer.print(nv+" ");
		    	}
		    }
		    
		    i = 0;
		    
		    for(Float nt : newTextureCoordinates){
		    	i++;
		    	
		    	if(i == 1){
		    		writer.print("vt "+nt+" ");
		    	}else if(i == 2){
		    		writer.print(nt+" \n");
		    		i = 0;
		    	}
		    }
		    
		    i = 0;
		    for(String key : indexArray){
		    	i++;
		    	
		    	int tmpIn = newMap.get(key).newPosition+1;
		    	
		    	if(i == 1){
		    		writer.print("f "+tmpIn+"/"+tmpIn+"/"+tmpIn+" ");
		    	}else if(i == 3){
		    		writer.print(tmpIn+"/"+tmpIn+"/"+tmpIn+" \n");
		    		i = 0;
		    	}else{
		    		writer.print(tmpIn+"/"+tmpIn+"/"+tmpIn+" ");
		    	}
		    }
		    
		    writer.close();
		    
		    log.append("Optimizing file: " + file.getName() + "." + "Optimization finished." + newline);
		    
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			log.append(file.getName() + " FAILED: " + "No file found." + newline);
		}
	}
}
