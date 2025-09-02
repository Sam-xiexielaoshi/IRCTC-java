package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {

    private List<Train>  trainList;

    private ObjectMapper objectmapper = new ObjectMapper();

    private static final String TRAINS_DB_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";

    public TrainService() throws IOException{
        File trains = new File(TRAINS_DB_PATH);
        trainList = objectmapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    public List<Train> searchTrains(String source, String destination){
        return trainList.stream().filter(train -> validTrain(train,source, destination)).collect(Collectors.toList());
    }

    private boolean validTrain(Train train, String source, String destination){
        List<String> stationOrder = train.getStations();

        int sourceIndex = stationOrder.indexOf(source);
        int destinationIndex = stationOrder.indexOf(destination);

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }
}
