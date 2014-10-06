package core.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import core.audio.Ensemble;

public class Config {

	public static String joinIP = "127.0.0.1";
	public static int joinPort = 34334;
	public static String joinName = "Client";
	public static boolean showHostIP = true;
	public static int hostPort = 34334;
	
	public static void loadConfig() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/config"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		if(line.matches("<AUDIO>")) {
	    			String[] temp = reader.readLine().split("=");
	    			Ensemble.get().setMasterVolume(Float.parseFloat(temp[1]));
	    		} else if(line.matches("<VIDEO>")) {
	    			// TODO Custom video settings
	    		} else if(line.matches("<NETWORK>")) {
	    			while((line = reader.readLine()) != null) { 
	    				String[] temp = line.split("=");
	    				if(temp[0].matches("joinip"))
	    					joinIP = temp[1];
	    				else if(temp[0].matches("joinport"))
	    					joinPort = Integer.parseInt(temp[1]);
	    				else if(temp[0].matches("joinname"))
	    					joinName = temp[1];
	    				else if(temp[0].matches("showip"))
	    					showHostIP = Boolean.parseBoolean(temp[1]);
	    				else if(temp[0].matches("hostport"))
	    					hostPort = Integer.parseInt(temp[1]);
	    			}
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	createConfig();
	    } catch (IOException e) {
	    	System.out.println("Config file failed to load!");
	    	e.printStackTrace();
	    }
	}
	
	public static void createConfig() {
		File dir = new File(System.getProperty("user.dir") + "/config");
		
		// Create save directory
		if(dir.mkdir()) {
			try {
				saveConfig();
			} catch(Exception e) {
				System.err.println("Config file failed to be created");
				e.fillInStackTrace();
			}
			
			System.out.println("Config file created");
		} else {
			// Save directory failed to be created
			System.err.println("Config file was not created");
		}
	}
	
	public static void saveConfig() throws IOException {
		File current = new File(System.getProperty("user.dir") + "/config");
		if(current.exists()) {
			current.delete();
		}
		current.createNewFile();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(current));
		
		writer.write("<AUDIO>");
		writer.newLine();
		writer.write("volume=" + 1);
		writer.newLine();
		writer.newLine();
		
		writer.write("<NETWORK>");
		writer.newLine();
		writer.write("joinip=" + joinIP);
		writer.newLine();
		writer.write("joinport=" + joinPort);
		writer.newLine();
		writer.write("joinname=" + joinName);
		writer.newLine();
		writer.write("showip=" + showHostIP);
		writer.newLine();
		writer.write("hostport=" + hostPort);
		writer.newLine();
		writer.newLine();
		
		writer.close();
	}
	
}
