package info.danbecker.streams;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import static info.danbecker.streams.Streams.Task;
import static info.danbecker.streams.Streams.Status;

public class StreamsTest {

	@Before
	public void before() {
	}
	
	@Test
	public void testFilterMapReduce() {
		final Collection<Task> tasks = Arrays.asList(
			    new Task( Status.OPEN, 5 ),
			    new Task( Status.OPEN, 13 ),
			    new Task( Status.CLOSED, 8 ) 
			);

		// Calculate total points of all tasks
		final double totalPoints = tasks
		   .stream()
		   .parallel()
		   .map( task -> task.getPoints() ) // or map( Task::getPoints ) 
		   .reduce( 0, Integer::sum );
		    
		Assert.assertEquals( "All tasks", 26.0, totalPoints, 0.0001 );
		// System.out.println( "Total points (all tasks): " + totalPoints );
		
		// Calculate total points of OPEN tasks using sum()
		final long totalPointsOfOpenTasks = tasks
		    .stream()
		    .filter( task -> task.getStatus() == Status.OPEN )
		    .mapToInt( Task::getPoints )
		    .sum();
		        
		Assert.assertEquals( "Open tasks", 18L, totalPointsOfOpenTasks );
		// System.out.println( "Total points: " + totalPointsOfOpenTasks );
		
		// Group tasks by their status
		final Map<Status, List<Task>> map = tasks
		    .stream()
		    .collect( Collectors.groupingBy( Task::getStatus ) );
		Assert.assertEquals( "Map size", 2, map.size() );
		// System.out.println( map ); // {OPEN=[[OPEN, 5], [OPEN, 13]], CLOSED=[[CLOSED, 8]]}

		// Do some complex reduction
		// Calculate the weight of each tasks (as percent of total points) 
		final Collection<String> weights = tasks
		    .stream()                                        // Stream< String >
		    .mapToInt( Task::getPoints )                     // IntStream
		    .asLongStream()                                  // LongStream
		    .mapToDouble( points -> points / totalPoints )   // DoubleStream
		    .boxed()                                         // Stream< Double >
		    .mapToLong( weigth -> ( long )( weigth * 100 ) ) // LongStream
		    .mapToObj( percentage -> percentage + "%" )      // Stream< String> 
		    .collect( Collectors.toList() );                 // List< String > 
		        
		Assert.assertEquals( "Collection size", 3, weights.size() );
		System.out.println( weights ); // [19%, 50%, 30%]
	}
}