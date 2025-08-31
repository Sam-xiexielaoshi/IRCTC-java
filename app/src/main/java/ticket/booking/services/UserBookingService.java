package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user; //current user using the service

    private List<User> userList; //list of all users in the local json file database

    private ObjectMapper objectMapper = new ObjectMapper(); //object mapper instance to read and write json files

    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json"; //path to the local json file database

    public UserBookingService(User user1) throws IOException {
        this.user = user1; // setting the current user
        File users = new File(USERS_PATH); //reading the users from the local json file database
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {} ); //deserializing the json array to list of users
        // TypeReference is used to get the type of the generic list at runtime
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(user1 -> {//filtering the list of users to find the user with the same username and password
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashPassword()); //checking if the username and password match
        }).findFirst();   //finding the user in the list of users
        return foundUser.isPresent();//returning true if the user is found, false otherwise
    }

    public boolean signUp(User user1){
        try {//checking if the username already exists
            userList.add(user1);//adding the new user to the list of users
            saveUserListToFile();//saving the updated list of users to the local json file database
            return Boolean.TRUE;//returning true if the user is added successfully
        }catch (IOException ex){//catching any IO exceptions
            return Boolean.FALSE;//returning false if there is an exception
        }
    }

    private void saveUserListToFile() throws IOException{//method to save the list of users to the local json file database
        File usersFile = new File(USERS_PATH);//creating a file object for the local json file database
        objectMapper.writeValue(usersFile, userList);//writing the list of users to the local json file database
    }

}
