package info.danbecker.streams.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Stream;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class StreamsFilesTest {
	final String fileName = "src/test/resources/map.txt";
	final Pattern regEx = Pattern.compile("^(?<key>.*)=(?<value>.*)$");

	@Before
	public void before() {
	}
	
	@Test
	public void testRegExStream() {		
		// Read file into stream, try-with-resources
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			Map<String,String> map = new Hashtable<String,String>();
			
			stream.forEach(text -> {
				Matcher m = regEx.matcher(text);
				if (m.find()) {
					map.put(m.group("key"), m.group("value"));
					// System.out.println( "key=" + m.group( "key" ) + ",value="
					// + m.group( "value" ));
				}
			});
			Assert.assertEquals( "Collection size", 10, map.size());
		} catch (IOException e) {
			e.printStackTrace();
		}		        
	}

	@Test
	public void testRegExFilter() {		
		// Read file into stream, try-with-resources
//		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
//			Map<String,String> map = new Hashtable<String,String>();
//			stream
//				.forEach(text -> regEx.matcher(text))
//				.filter( matcher -> matcher.find())
//				.reduce( map -> map.put(m.group("key"), m.group("value")));
//			Assert.assertEquals( "Collection size", 10, map.size());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}		        
	}
}