package info.danbecker.streams.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;
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
		try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
			Map<String,String> map = new Hashtable<String,String>();
			
			lines.forEach(text -> {
				Matcher m = regEx.matcher(text);
				if (m.matches()) {
					map.put(m.group("key"), m.group("value"));
					// System.out.println( "key=" + m.group( "key" ) + ",value=" + m.group( "value" ));
				}
			});
			Assert.assertEquals( "Collection size", 10, map.size());
			Assert.assertEquals( "Collection element", "valueJ", map.get( "key9" ));
		} catch (IOException e) {
			e.printStackTrace();
		}		        
	}

	@Test
	public void testRegExFilter() {		
		// Read file into stream, try-with-resources
		try (Stream<String> lines = Files.lines(Paths.get(fileName)) ) {
			Map<String,String> map = lines
				.parallel()
			    .map(text -> regEx.matcher(text))
				.filter(matcher -> matcher.matches())
				// .forEach(matcher -> System.out.format("key=%s, value=%s\n", matcher.group("key"), matcher.group("value") ));
				.collect( Collectors.toMap( matcher -> matcher.group("key"), matcher -> matcher.group("value")))
			;
			// System.out.println( "Map=" + map);
			Assert.assertEquals( "Collection size", 10, map.size());
			Assert.assertEquals( "Collection element", "valueJ", map.get( "key9" ));
		} catch (IOException e) {
			e.printStackTrace();
		}		        
	}
}