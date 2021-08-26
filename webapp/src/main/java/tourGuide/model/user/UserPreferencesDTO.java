package tourGuide.model.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserPreferencesDTO {

	private String userName;
	private Integer tripDuration;
	private Integer numberOfAdults;
	private Integer numberOfChildren;
	
	//need a user input since userName is not in UserPreference object
	public UserPreferencesDTO (User user) {
		this.userName = user.getUserName();
		this.numberOfAdults = user.getUserPreferences().getNumberOfAdults();
		this.numberOfChildren = user.getUserPreferences().getNumberOfChildren();
		this.tripDuration = user.getUserPreferences().getTripDuration();
		
	}
	
	
}
