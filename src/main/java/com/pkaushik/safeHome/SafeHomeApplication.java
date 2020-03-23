package com.pkaushik.safeHome;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.pkaushik.safeHome.model.*;
import com.pkaushik.safeHome.repository.SafeHomeUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EntityScan
@EnableAsync
public class SafeHomeApplication {
	public static void main(String[] args) {
		SpringApplication.run(SafeHomeApplication.class, args);
	}

	//map ensures that with same id, user can't be logged in twice. no duplicate keys
	private static Map<Integer, UserRole> loggedInUsers = new HashMap<Integer, UserRole>();
	
	//map for all the current requests that are not fulfilled, and their location data
	private static Map<SpecificRequest, List<Location>> currentRequestsMap = new HashMap<SpecificRequest, List<Location>>(); 

	//map for open assignments that haven't been accepted yet. 
	private static Map<Assignment, Walker> openAssignmentsMap = new HashMap<Assignment, Walker>();

	public static void resetAll(){
		loggedInUsers.clear();
		currentRequestsMap.clear(); 
		openAssignmentsMap.clear(); 
	}

	//Getters & Setters
	public static Map<Integer, UserRole> getLoggedInUsersMap() {
		return Collections.unmodifiableMap(loggedInUsers);
	}

	public static void logInUser(int mcgillID, UserRole role){
		SafeHomeApplication.loggedInUsers.put(mcgillID, role);
	}

	public static void logOutUser(int mcgillID){
		if(SafeHomeApplication.loggedInUsers == null) throw new IllegalStateException("Can't log out user that hasn't logged in");
		SafeHomeApplication.loggedInUsers.remove(mcgillID);
	}

	public static Map<SpecificRequest, List<Location>> getCurrentRequestsMap(){
		return currentRequestsMap; 
	}
	
	public static void setCurrentRequestsMap(Map<SpecificRequest, List<Location>> currentRequestsMap){
		SafeHomeApplication.currentRequestsMap = currentRequestsMap; 
	}

	//implementation dependent operations
	public static void addNewRequest(SpecificRequest request, List<Location> locations){
		currentRequestsMap.put(request, locations); 
	}

	public static void removeRequest(SpecificRequest request, List<Location> locations){
		currentRequestsMap.remove(request); 
	}

	public static void addAssignmentToMap(Assignment assignment, Walker walker){
		(openAssignmentsMap).put(assignment, walker);
	}

	public static Map<Assignment, Walker> getOpenAssignmentsMap(){
		return Collections.unmodifiableMap(openAssignmentsMap); 
	}

	public static void removeAssignmentFromMap(UUID uuid){
		openAssignmentsMap.remove(uuid); 
	}

	public static void setOpenAssignmentsMap(Map<Assignment, Walker> openAssignmentsMap){
		SafeHomeApplication.openAssignmentsMap = openAssignmentsMap; 
	}
}