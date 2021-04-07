package ge.tsu.spring.lecture3.car;


import java.util.List;

public interface CarService {

  void add(AddCar addCar) throws RecordAlreadyExistsException;

  void update(String id, AddCar addCar) throws RecordAlreadyExistsException, RecordNotFoundException;

  List<CarView> getList(String manufacturer, String model);

  CarView getById(String id) throws RecordNotFoundException;

  void delete(String id) throws RecordNotFoundException;
}
