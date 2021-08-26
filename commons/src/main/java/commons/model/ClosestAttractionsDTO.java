package commons.model;

import gpsUtil.location.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * This DTO class contains : 
 * <ul>
 * <li>The user's location lat/long</li>
 * <li>The list of closest attractions</li>
 * </ul>
 * 
 * @author jerome
 *
 */
//Lombok:
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClosestAttractionsDTO {

	private Location userLocation;
	private List<AttractionDistance> attractionList;
	
}
