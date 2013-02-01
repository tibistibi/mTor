package nl.bhit.model;

import java.util.Arrays;
import java.util.List;

public enum Status {

	/**
	 * normal info message
	 */
	INFO,
	/**
	 * not a real problem jet
	 */
	WARN,
	/**
	 * a real problem, application is not
	 */
	ERROR;
	
	 public static List<Status> getAsList() {
		  return Arrays.asList(Status.values());

		 }
}
