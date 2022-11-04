package hr.unidu.ris.parsingjson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JavaJacksonExample {
	private ObjectMapper mapper;
	public static void main(String[] args) {
		JavaJacksonExample ex = new JavaJacksonExample();
		ex.mapper = new ObjectMapper();
	      try{
		      //map json to objekt tipa Kolegiji
	    	  ex.obradiObjekt();
		      //map json to lista objekata tipa Kolegij
	    	  ex.obradiListuObjekata();
	      }
	      catch (JsonParseException e) { e.printStackTrace();}
	      catch (JsonMappingException e) { e.printStackTrace(); }
	      catch (IOException e) { e.printStackTrace(); }

	}
	private void obradiObjekt() throws JsonMappingException, JsonProcessingException, IOException  {
		final String FILENAME = "data/kolegiji.json";
		Path filePath = Path.of(FILENAME);
		String jsonString = Files.readString(filePath);
        Kolegiji ki = mapper.readValue(jsonString, Kolegiji.class);
        System.out.println(ki);
        System.out.println("****************************************************");
	}
	private void obradiListuObjekata() throws JsonMappingException, JsonProcessingException, IOException {
		final String FILENAME = "data/kolegiji1.json";
		Path filePath = Path.of(FILENAME);
		String jsonString = Files.readString(filePath);
        List<Kolegij> ki = mapper.readValue(jsonString, new TypeReference<List<Kolegij>>() {});
        ki.forEach(System.out::println);
	}
}
