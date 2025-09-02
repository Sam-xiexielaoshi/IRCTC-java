package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user; //current user using the service

    private List<User> userList; //list of all users in the local json file database

    private ObjectMapper objectMapper = new ObjectMapper(); //object mapper instance to read and write json files

    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json"; //path to the local json file database

    public UserBookingService(User user1) throws IOException {
        this.user = user1; // setting the current user
        loadUsers();
    }

    public UserBookingService() throws IOException {
        loadUsers();
    }

    public List<User> loadUsers() throws IOException{
        File users = new File(USERS_PATH);
        return userList = objectMapper.readValue(users, new TypeReference<List<User>>() {} );
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

    public void fetchBooking(){
        user.printTickets();//printing all tickets booked by the user
    }

    public boolean cancelBooking(String ticketId){
        try {
            List<Ticket> tickets = user.getTicketsBooked();//getting the list of tickets booked by the user
            boolean removed = tickets.removeIf(ticket -> ticket.getTicketId().equals(ticketId));//removing the ticket with the given ticket id from the list of tickets
            if (removed){
                user.setTicketsBooked(tickets);//update user's tickets list
                for(int i=0;i<userList.size();i++){//updating the user in the list of users
                    if(userList.get(i).getUserId().equals(user.getUserId())){//finding the user in the list of users
                        userList.set(i,user);//updating the user in the list of users
                        break;
                    }
                }

                saveUserListToFile();//saving the updated list of users to the local json file database
                return Boolean.TRUE;//returning true if the ticket is removed successfully
            }return Boolean.FALSE;//returning false if the ticket is not found
        }catch (IOException ex){//catching any IO exceptions
            return Boolean.FALSE;//returning false if there is an exception
        }
    }

    public List<Train> getTrains(String source,String destination){
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);//returning the list of trains from the train service}
    }catch(IOException ex){
        System.out.println("Error fetching trains: " + ex.getMessage());
        return new ArrayList<>();
    }
}
